package com.lab.task1;

import com.lab.task1.synchronizedObjects.Synchronizer;
import com.lab.task1.synchronizedObjects.SynchronizerFactory;

public class Bank {
    public static final int NTEST = 10000;
    private final int[] accounts;
    private final Synchronizer synchronizer;

    public long getNtransacts() {
        return ntransacts;
    }

    public long ntransacts = 0;

    public Bank(int n, int initialBalance, SynchronizerProfile syncProfile) {
        var synchronizerFactory = new SynchronizerFactory();
        synchronizer = synchronizerFactory.get(this, syncProfile);

        accounts = new int[n];
        for (var i = 0; i < accounts.length; i++) {
            accounts[i] = initialBalance;
        }

        ntransacts = 0;
    }

    public void transfer(int from, int to, int amount) {
        synchronizer.transfer(accounts, from, to, amount);
    }

    public void test() {
        int sum = 0;

        for (var account : accounts) {
            sum += account;
        }

        System.out.println("Transactions:" + ntransacts + " Sum: " + sum);
    }

    public int size() {
        return accounts.length;
    }
}
