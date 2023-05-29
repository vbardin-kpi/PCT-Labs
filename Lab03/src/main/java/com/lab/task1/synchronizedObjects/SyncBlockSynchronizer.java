package com.lab.task1.synchronizedObjects;

import com.lab.task1.Bank;

public class SyncBlockSynchronizer implements Synchronizer {
    private final Bank bank;

    public SyncBlockSynchronizer(Bank bank) {
        this.bank = bank;
    }

    @Override
    public void transfer(int[] accounts, int from, int to, int amount) {
        synchronized (this) {
            accounts[from] -= amount;
            accounts[to] += amount;
            bank.ntransacts++;
            if (bank.ntransacts % bank.NTEST == 0)
                bank.test();
        }
    }
}
