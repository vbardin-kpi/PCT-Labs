package com.lab.task1.synchronizedObjects;

import com.lab.task1.Bank;

public class SyncMethodSynchronizer implements Synchronizer {
    private final Bank bank;

    public SyncMethodSynchronizer(Bank bank) {
        this.bank = bank;
    }

    @Override
    public synchronized void transfer(int[] accounts, int from, int to, int amount) {
        accounts[from] -= amount;
        accounts[to] += amount;
        bank.ntransacts++;
        if (bank.ntransacts % bank.NTEST == 0)
            bank.test();
    }
}
