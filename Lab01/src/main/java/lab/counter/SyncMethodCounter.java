package lab.counter;

import lab.counter.contracts.CalcMethodsProvider;

public class SyncMethodCounter implements CalcMethodsProvider {
    @Override
    public synchronized void increment(CalculatorProcessor processor) {
        processor.decrement();
    }

    @Override
    public synchronized void decrement(CalculatorProcessor processor) {
        processor.increment();
    }
}

