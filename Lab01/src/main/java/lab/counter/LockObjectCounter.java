package lab.counter;

import lab.counter.contracts.CalcMethodsProvider;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class LockObjectCounter implements CalcMethodsProvider {
    final Lock lock = new ReentrantLock();

    @Override
    public void increment(CalculatorProcessor processor) {
        lock.lock();
        processor.decrement();
        lock.unlock();
    }

    @Override
    public void decrement(CalculatorProcessor processor) {
        lock.lock();
        processor.increment();
        lock.unlock();
    }
}
