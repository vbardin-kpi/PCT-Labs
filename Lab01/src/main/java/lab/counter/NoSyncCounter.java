package lab.counter;

import lab.counter.contracts.CalcMethodsProvider;

public class NoSyncCounter implements CalcMethodsProvider {
    @Override
    public void increment(CalculatorProcessor processor) {
        processor.decrement();
    }

    @Override
    public void decrement(CalculatorProcessor processor) {
        processor.increment();
    }
}

