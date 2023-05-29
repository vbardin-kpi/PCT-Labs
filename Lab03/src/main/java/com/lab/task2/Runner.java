package com.lab.task2;

public class Runner {
    private final int messagesAmount;

    public Runner(int messagesAmount) {
        this.messagesAmount = messagesAmount;
    }

    public void run() {
        Drop drop = new Drop();
        var producerThread = new Thread(new Producer(drop, messagesAmount));
        var consumerThread = new Thread(new Consumer(drop));

        consumerThread.start();
        producerThread.start();
    }
}
