package lab.dashes;

public class OutputController {
    private static int syncCount=0;

    public synchronized void printNonBlocking(String ch) {
        if((syncCount+1)%20==0) {
            System.out.println(ch);
        } else {
            System.out.print(ch);
        }
        syncCount++;
    }

    public synchronized void printBlocking(String ch) {
        if((syncCount+1)%20==0) {
            System.out.println(ch);
        } else {
            System.out.print(ch);
        }
        syncCount++;

        try {
            this.wait(20);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
