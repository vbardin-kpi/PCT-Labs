package com.lab.task1.synchronizedObjects;

import com.lab.task1.Bank;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class LockObjectSynchronizer implements Synchronizer {
    private final Lock lockObject = new ReentrantLock();
    private final Bank bank;

    public LockObjectSynchronizer(Bank bank) {
        this.bank = bank;
    }

    @Override
    public void transfer(int[] accounts, int from, int to, int amount) {
        lockObject.lock();
        accounts[from] -= amount;
        accounts[to] += amount;
        bank.ntransacts++;
        if (bank.ntransacts % bank.NTEST == 0)
            bank.test();
        lockObject.unlock();
    }
}