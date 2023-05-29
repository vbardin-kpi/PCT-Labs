package com.lab;


import com.lab.task1.SynchronizerProfile;

public class Main {
    private static final int messagesAmount = 100;

    public static void main(String[] args) {
        switch (RunningProfile.TASK_1) {
            case TASK_1 -> new com.lab.task1.Runner(10, 10000, SynchronizerProfile.NO_SYNC).run();
            case TASK_2 -> new com.lab.task2.Runner(messagesAmount).run();
            case TASK_3 -> new com.lab.task3.Runner().run();
        }
    }
}