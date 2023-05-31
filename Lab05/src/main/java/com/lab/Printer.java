package com.lab;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicInteger;

public class Printer implements Runnable {
    private final int modelId;
    private final int clientQueueSize;
    private final AtomicInteger clientsServed;
    private final AtomicInteger clientsRejected;

    private final CountDownLatch clientsLatch;

    public Printer(int modelId, int clientQueueSize,
                   AtomicInteger clientsServed, AtomicInteger clientsRejected, CountDownLatch clientsLatch) {
        this.modelId = modelId;
        this.clientQueueSize = clientQueueSize;
        this.clientsServed = clientsServed;
        this.clientsRejected = clientsRejected;
        this.clientsLatch = clientsLatch;
    }

    @Override
    public synchronized void run() {
        while (clientsLatch.getCount() > 0) {
            String sb = "------------------------------------------------------" + "\n\r" +
                    "ModelId: " + modelId + "\n\r" +
                    "Clients served: " + clientsServed.get() + "\n\r" +
                    "Clients rejected: " + clientsRejected.get() + "\n\r" +
                    "Queue size: " + clientQueueSize + "\n\r" +
                    "------------------------------------------------------" + "\n\r";

            System.out.println(sb);
        }
    }
}
