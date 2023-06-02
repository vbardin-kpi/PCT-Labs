package com.lab;

import lombok.Getter;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.ReentrantLock;

@Getter
public class SimulationProcessor implements Callable<SimulationResult> {
    private final int modelId;
    private final SimulationProcessorConfiguration configuration;

    private final List<Future<Integer>> channelsResults = new ArrayList<>();
    private final LinkedBlockingQueue<Integer> clientQueue;
    private final AtomicInteger clientsServed;
    private final AtomicInteger clientsRejected;
    private final AtomicInteger totalQueueLength;

    private final CountDownLatch channelLatch;
    private final CountDownLatch clientsLatch;

    public SimulationProcessor(
            int modelId,
            SimulationProcessorConfiguration configuration) {
        this.modelId = modelId;
        this.configuration = configuration;

        this.clientQueue = new LinkedBlockingQueue<>(configuration.queueSize());
        this.clientsServed = new AtomicInteger(0);
        this.clientsRejected = new AtomicInteger(0);
        this.totalQueueLength = new AtomicInteger(0);

        // channels latch probably should be channelsAmount based
        this.channelLatch = new CountDownLatch(configuration.clientsAmount());
        this.clientsLatch = new CountDownLatch(configuration.clientsAmount());
    }

    @Override
    public SimulationResult call() {
        // why + 2? Why not + 5?
        var channelThreadPool = Executors.newFixedThreadPool(configuration.channelsAmount() + 2);

        for (var i = 0; i < configuration.channelsAmount(); i++) {
            channelThreadPool.execute(() -> {
                ServiceChannel serviceChannel = new ServiceChannel(getConfiguration().serviceTime());
                while (channelLatch.getCount() > 0) {
                    try {
                        var clientId = clientQueue.poll(1, TimeUnit.SECONDS);
                        if (clientId != null) {
                            serviceChannel.serveClient();
                            clientsServed.incrementAndGet();
                            channelLatch.countDown();
                        }
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                    }
                }
            });
        }

        channelThreadPool.execute(new Printer(modelId, clientQueue, clientsServed, clientsRejected, clientsLatch));

        ScheduledExecutorService clientsExecutor = Executors.newScheduledThreadPool(1);
        for (int clientNumber = 1; clientNumber <= configuration.clientsAmount(); clientNumber++) {
            int finalI = clientNumber;

            clientsExecutor.schedule(() -> {
                if (!clientQueue.offer(finalI)) {
                    clientsRejected.incrementAndGet();
                    channelLatch.countDown();
                }

                clientsLatch.countDown();
            }, configuration.clientArrivalIntervalMs(), TimeUnit.MILLISECONDS);

            totalQueueLength.addAndGet(clientQueue.size());
            try {
                TimeUnit.MILLISECONDS.sleep(configuration.clientArrivalIntervalMs());
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }

        try {
            clientsLatch.await();
            channelThreadPool.shutdownNow();
            channelLatch.await();
            clientsExecutor.shutdownNow();

        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        return new SimulationResult(modelId, clientsServed.get(), clientsRejected.get(),
                (double) totalQueueLength.get() / configuration.clientsAmount(),
                (double) clientsRejected.get() / configuration.clientsAmount());

    }
}
