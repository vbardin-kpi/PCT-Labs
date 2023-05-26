package lab.counter;

public class CountingRunner {
    private static final ThreadsFactory threadsFactory = new ThreadsFactory();
    private static final CalculatorProcessor countingProcessor = new CalculatorProcessor();

    public static void main(String[] args) {
        runExperiment(SyncType.NO_SYNC);
        runExperiment(SyncType.LOCK_OBJECT);
        runExperiment(SyncType.SYNC_BLOCK);
        runExperiment(SyncType.SYNC_METHOD);
    }

    private static void runExperiment(SyncType syncType) {
        var calcThreads = threadsFactory.createThreads(CountingRunner.countingProcessor, syncType);

        var incThread = calcThreads[0];
        var decThread = calcThreads[1];

        decThread.start();
        incThread.start();

        try {
            incThread.join();
            decThread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.out.println(syncType + " - " + CountingRunner.countingProcessor.getCounterValue());

        CountingRunner.countingProcessor.resetCounterSync();
    }
}
