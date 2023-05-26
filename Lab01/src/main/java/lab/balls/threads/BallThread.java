package lab.balls.threads;

import lab.balls.BallCanvas;
import lab.balls.Pocket;
import lab.balls.Ball;

import java.util.Optional;

public class BallThread extends Thread {
    private final Ball ball;
    private final BallCanvas canvas;

    public BallThread(BallCanvas ballCanvas, Ball ball) {
        this.ball = ball;
        this.canvas = ballCanvas;
    }

    @Override
    public void run() {
        try {
            while (true) {
                ball.move();
                var currentThread = Thread.currentThread();
                System.out.println("Thread name = " + currentThread.getName() + " priority: " + currentThread.getPriority());

                Optional<Pocket> pocket = canvas.getPockets().stream()
                        .filter(ball::IsCollision).findFirst();

                if (pocket.isPresent()) {
                    canvas.remove(ball);
                    pocket.get().incrementScore();
                    break;
                }

                Thread.sleep(5);
            }
        } catch (InterruptedException ex) {
            System.out.println("Exception in thread " + Thread.currentThread().getName());
        }
    }
}
