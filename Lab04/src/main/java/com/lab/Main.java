package com.lab;

public class Main {
    public static void main(String[] args) {
        final var runningProfile = RunningProfile.TASK_1;

        switch (runningProfile) {
            case TASK_1 -> new com.lab.task1.Runner().run();
            case TASK_2 -> new com.lab.task2.Runner().run();
            case TASK_3 -> new com.lab.task3.Runner().run();
            case TASK_4 -> new com.lab.task4.Runner().run();
        }

    }
}
