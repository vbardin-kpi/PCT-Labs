package com.lab.task2;

import java.util.ArrayList;
import java.util.concurrent.ThreadLocalRandom;

public class Consumer implements Runnable {
    private Drop drop;
    private final ArrayList<Integer> processedMessages = new ArrayList<>();

    public Consumer(Drop drop) {
        this.drop = drop;
    }

    public void run() {
        Object message = null;
        do {
            try {
                message = drop.take();
                System.out.format("MESSAGE RECEIVED: %s%n", message);

                try {
                    var intFromMessage = Integer.parseInt(message.toString());
                    processedMessages.add(intFromMessage);
                } catch (NumberFormatException ex) {
                    if (!message.equals("DONE")) {
                        System.out.println("Received message isn't valid");
                    }
                }

                Thread.sleep(ThreadLocalRandom.current().nextInt(50));
            } catch (InterruptedException ignored) {}
        } while (!message.equals("DONE"));

        System.out.println("All messages processed.");
    }
}