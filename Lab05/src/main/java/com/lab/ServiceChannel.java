package com.lab;

import java.util.concurrent.TimeUnit;

public class ServiceChannel {
    private final long serviceTime;

    public ServiceChannel(long serviceTime) {
        this.serviceTime = serviceTime;
    }

    public void serveClient() {
        try {
            TimeUnit.MILLISECONDS.sleep(serviceTime);
        } catch (InterruptedException ignored) {
        }
    }
}
