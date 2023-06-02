package org.example;

public class Main {
    public static void main(String[] args) {
        final RunningProfile profile = RunningProfile.BLOCKING_MESSAGING;

        switch (profile) {
            case BLOCKING_MESSAGING -> new MultiplyBlocking(args, new TaskConfig(4)).multiplyBlocking();
            case NON_BLOCKING_MESSAGING -> new MultiplyNonBlocking(args, new TaskConfig(4)).multiplyNonBlocking();
        }
    }
}
