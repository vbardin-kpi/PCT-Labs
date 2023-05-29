package com.lab.task1.synchronizedObjects;

import com.lab.task1.Bank;
import com.lab.task1.SynchronizerProfile;

public class SynchronizerFactory {
    public Synchronizer get(Bank bank, SynchronizerProfile profile) {
        Synchronizer synchronizer = null;

        switch (profile) {
            case NO_SYNC -> synchronizer = new NoSynchronizer(bank);
            case SYNC_METHOD -> synchronizer = new SyncMethodSynchronizer(bank);
            case SYNC_BLOCK -> synchronizer = new SyncBlockSynchronizer(bank);
            case LOCK_OBJECT -> synchronizer = new LockObjectSynchronizer(bank);
        }

        return synchronizer;
    }
}
