package com.lab;

public record SimulationResult(
        int modelId,
        int served,
        int rejected,
        double queueAverageLength,
        double rejectProbability) {}