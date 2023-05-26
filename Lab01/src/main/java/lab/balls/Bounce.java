package lab.balls;

import javax.swing.*;

public class Bounce {
    public static void main(String[] args) {
        BounceFrame bounceFrame = new BounceFrame(RunningProflle.PRIORITY);
        bounceFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        bounceFrame.setVisible(true);
        System.out.println("Thread name = " + Thread.currentThread().getName());
    }
}
