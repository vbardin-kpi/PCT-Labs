package lab.counter;

import lab.counter.contracts.CalcMethodsProvider;

public class SyncBlockCounter implements CalcMethodsProvider {
    @Override
    public void increment(CalculatorProcessor processor) {
        synchronized (SyncBlockCounter.class) {
            processor.decrement();
        }
    }

    @Override
    public synchronized void decrement(CalculatorProcessor processor) {
        synchronized (SyncBlockCounter.class) {
            processor.increment();
        }
    }
}
