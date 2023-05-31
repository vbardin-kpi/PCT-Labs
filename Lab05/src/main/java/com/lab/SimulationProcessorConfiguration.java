package com.lab;

public record SimulationProcessorConfiguration(
        int channelsAmount,
        int queueSize,
        long serviceTime,
        int clientsAmount,
        long clientArrivalIntervalMs) {
}