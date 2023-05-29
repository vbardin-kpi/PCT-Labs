package com.lab.task2;

public class Drop {
    private Object message;
    private boolean empty = true;

    public synchronized Object take() {
        while (empty) {
            try {
                wait();
            } catch (InterruptedException ignored) {}
        }

        empty = true;
        notifyAll();

        return message;
    }

    public synchronized void put(Object message) {
        while (!empty) {
            try {
                wait();
            } catch (InterruptedException ignored) {}
        }

        empty = false;
        this.message = message;

        notifyAll();
    }
}
