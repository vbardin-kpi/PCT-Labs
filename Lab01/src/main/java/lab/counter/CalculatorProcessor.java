package lab.counter;

public class CalculatorProcessor {
    private int internalCounter;

    public int getCounterValue() {
        return internalCounter;
    }

    public void resetCounterSync() {
        internalCounter = 0;
    }

    public void increment() {
        internalCounter++;
    }

    public void decrement() {
        internalCounter--;
    }
}
