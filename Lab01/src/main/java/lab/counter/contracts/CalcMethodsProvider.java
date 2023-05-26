package lab.counter.contracts;

import lab.counter.CalculatorProcessor;

public interface CalcMethodsProvider {
    void increment(CalculatorProcessor processor);
    void decrement(CalculatorProcessor processor);
}
