package lab.balls.threads;

import lab.balls.Ball;

public class BallThreadJoin extends Thread {
    private Ball ball;
    private BallThreadJoin other;
    private int runtime;

    public BallThreadJoin(Ball ball, int runtime) {
        this.runtime = runtime;
        this.ball = ball;
    }

    public BallThreadJoin(Ball b, BallThreadJoin other, int runtime) {
        this.runtime = runtime;
        this.ball = b;
        this.other = other;
    }

    @Override
    public void run() {
        try {
            if (this.other == null) {
                for (int i = 1; i < runtime; i++) {
                    ball.move();
                    System.out.println("Thread name" + Thread.currentThread().getName());
                    Thread.sleep(5);
                }
            } else {
                for (int i = 1; i < runtime; i++) {
                    other.join();
                    ball.move();
                    System.out.println("Thread name" + Thread.currentThread().getName());
                    Thread.sleep(5);
                }
            }
        } catch (InterruptedException ex) {
            System.out.println("Exception in thread " + Thread.currentThread().getName());
        }
    }
}
