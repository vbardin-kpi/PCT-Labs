package org.example;

public class Main {
    final static TaskConfig config = new TaskConfig(1000);

    public static void main(String[] args) {
        var profile = RunningProfile.NON_BLOCKING_MESSAGING;

        switch (profile) {
            case BLOCKING_MESSAGING -> getMatrixCalculatorBlocking(args, config).multiply();
            case NON_BLOCKING_MESSAGING -> getMatrixCalculatorNonBlocking(args, config).multiply();
        }
    }

    private static MultiplyBlocking getMatrixCalculatorBlocking(String[] args, TaskConfig config) {
        return new MultiplyBlocking(args, config);
    }

    private static MultiplyNonBlocking getMatrixCalculatorNonBlocking(String[] args, TaskConfig config) {
        return new MultiplyNonBlocking(args, config);
    }
}
