package lab.dashes;

public class Runner {

    public static void main(String[] args) {
        final var profile = RunningProfile.SYNC;

        OutputController outputController = new OutputController();

        ConsoleWritterThread horizontal;
        ConsoleWritterThread vertical;

        if (profile == RunningProfile.NO_SYNC)  {
            horizontal = new ConsoleWritterThread("-", ch -> outputController.printNonBlocking(ch));
            vertical = new ConsoleWritterThread("|", ch -> outputController.printNonBlocking(ch));
        }
        else {
            horizontal = new ConsoleWritterThread( "-", ch -> outputController.printBlocking(ch));
            vertical = new ConsoleWritterThread("|", ch -> outputController.printBlocking(ch));
        }

        Thread t1 = new Thread(horizontal);
        Thread t2 = new Thread(vertical);

        t1.start();
        t2.start();
        try {
            t1.join();
            t2.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
