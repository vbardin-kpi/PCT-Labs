package com.lab;

import java.util.ArrayList;
import java.util.concurrent.*;

public class Main {
    public static void main(String[] args) throws ExecutionException, InterruptedException {
        int numSimulations = 4;
        double totalClientsServed = 0;
        double totalClientsRejected = 0;
        double totalAverageQueueLength = 0;
        double totalRejectionProbability = 0;

        var simulationResults = new ArrayList<Future<SimulationResult>>();
        ExecutorService executorService = Executors.newCachedThreadPool();

        for (int i = 0; i < numSimulations; i++) {
            var simulationProcessor = new SimulationProcessor(i + 1, getConfig());
            simulationResults.add(executorService.submit(simulationProcessor));
        }

        try {
            executorService.shutdown();
            executorService.awaitTermination(100L, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        processSimulationsResults(
                numSimulations,
                totalClientsServed,
                totalClientsRejected,
                totalAverageQueueLength,
                totalRejectionProbability,
                simulationResults);
    }

    private static void processSimulationsResults(int numSimulations, double totalClientsServed, double totalClientsRejected, double totalAverageQueueLength, double totalRejectionProbability, ArrayList<Future<SimulationResult>> simulationResults) throws InterruptedException, ExecutionException {
        for (Future<SimulationResult> result : simulationResults) {
            SimulationResult modelResult = result.get();

            totalClientsServed += modelResult.served();
            totalClientsRejected += modelResult.rejected();
            totalAverageQueueLength += modelResult.queueAverageLength();
            totalRejectionProbability += modelResult.rejectProbability();

            var modelState =
                    "////////////////////////////////////////////" + "\n\r" +
                    "Model Id: " + modelResult.modelId() + "\n\r" +
                    "Clients served: " + modelResult.served() + "\n\r" +
                    "Clients rejected: " + modelResult.rejected() + "\n\r" +
                    "Queue: " + modelResult.queueAverageLength() + "\n\r" +
                    "Decline probability: " + modelResult.rejectProbability() + "\n\r" +
                    "////////////////////////////////////////////" + "\n\r";

            System.out.println(modelState);
        }

        var simulationResultString =
                "Avg simulation clients serviced: " + (totalClientsServed / numSimulations) + "\n\r" +
                "Avg simulation clients declined: " + (totalClientsRejected / numSimulations) + "\n\r" +
                "Avg simulation queue length: " + (totalAverageQueueLength / numSimulations) + "\n\r" +
                "Avg simulation decline probability: " + (totalRejectionProbability / numSimulations);
        System.out.println(simulationResultString);
    }

    private static SimulationProcessorConfiguration getConfig() {
        final int numChannels = 5;
        final int queueSize = 5;
        final long serviceTime = 100;
        final int numClients = 100;
        final long clientArrivalInterval = 80;

        return new SimulationProcessorConfiguration(
                numChannels,
                queueSize,
                serviceTime,
                numClients,
                clientArrivalInterval);
    }
}