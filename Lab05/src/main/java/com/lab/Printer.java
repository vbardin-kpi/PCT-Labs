package com.lab;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicInteger;

public class Printer implements Runnable {
    private final int modelId;
    private final LinkedBlockingQueue<Integer> clientQueue;
    private final AtomicInteger clientsServed;
    private final AtomicInteger clientsRejected;

    private final CountDownLatch clientsLatch;

    public Printer(int modelId, LinkedBlockingQueue<Integer> clientQueue,
                   AtomicInteger clientsServed, AtomicInteger clientsRejected, CountDownLatch clientsLatch) {
        this.modelId = modelId;
        this.clientQueue = clientQueue;
        this.clientsServed = clientsServed;
        this.clientsRejected = clientsRejected;
        this.clientsLatch = clientsLatch;
    }

    @Override
    public void run() {
        while (clientsLatch.getCount() > 0) {
            String sb = "------------------------------------------------------" + "\n\r" +
                    "ModelId: " + modelId + "\n\r" +
                    "Clients served: " + clientsServed.get() + "\n\r" +
                    "Clients rejected: " + clientsRejected.get() + "\n\r" +
                    "Queue size: " + clientQueue.size() + "\n\r" +
                    "------------------------------------------------------" + "\n\r";

            System.out.println(sb);
        }
    }
}
