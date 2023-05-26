package lab.counter;

import lab.counter.contracts.CalcMethodsProvider;
import lab.counter.contracts.CalculatingThreadsFactory;

public class ThreadsFactory implements CalculatingThreadsFactory {
    @Override
    public Thread[] createThreads(CalculatorProcessor processor, SyncType syncType) {
        CalcMethodsProvider calculator;

        switch (syncType) {
            case NO_SYNC -> calculator = new NoSyncCounter();
            case LOCK_OBJECT -> calculator = new LockObjectCounter();
            case SYNC_METHOD -> calculator = new SyncMethodCounter();
            case SYNC_BLOCK -> calculator = new SyncBlockCounter();
            default -> throw new IllegalStateException("Unexpected value: " + syncType);
        }

        return getThreads(processor, calculator);
    }

    private static Thread[] getThreads(CalculatorProcessor processor, CalcMethodsProvider calulator) {
        Thread incrementThread = new Thread(() -> {
            for (int i = 0; i < 100000; i++) {
                calulator.increment(processor);
            }
        });

        Thread decrementThread = new Thread(() -> {
            for (int i = 0; i < 100000; i++) {
                calulator.decrement(processor);
            }
        });

        return new Thread[]{incrementThread, decrementThread};
    }
}
