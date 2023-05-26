package lab.counter.contracts;

import lab.counter.CalculatorProcessor;
import lab.counter.SyncType;

@FunctionalInterface
public interface CalculatingThreadsFactory {
    Thread[] createThreads(CalculatorProcessor processor, SyncType syncType);
}


