package com.lab.task1;

public class Runner {
    private final int accountsAmount;
    private final int initialBalance;
    private final SynchronizerProfile syncProfile;

    public Runner(int accountsNumber, int initBalance, SynchronizerProfile syncProfile) {
        accountsAmount = accountsNumber;
        initialBalance = initBalance;
        this.syncProfile = syncProfile;
    }

    public void run() {
        Bank b = new Bank(accountsAmount, initialBalance, syncProfile);
        int i;
        for (i = 0; i < accountsAmount; i++) {
            TransferThread t = new TransferThread(b, i,
                    initialBalance);
            t.setPriority(Thread.NORM_PRIORITY + i % 2);
            t.start();
        }
    }
}
