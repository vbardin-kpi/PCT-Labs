package lab.balls;

import lab.balls.configs.Point;
import lab.balls.threads.BallThread;
import lab.balls.threads.BallThreadJoin;

import java.awt.*;

public class StartButtonActions {
    public static void setUpDefault(BallCanvas canvas) {
        var ball = new Ball(canvas, true);
        canvas.add(ball);
        var thread = new BallThread(canvas, ball);
        thread.start();
        System.out.println("Thread name = " + thread.getName());
    }

    public static void setUpJoin(BallCanvas canvas) {
        BallThreadJoin previous = null;
        for (int i = 0; i < 10; i++) {
            var ballConfig = new BallConfig(new Point(0, i * 22), 0, 2, Color.BLUE);
            var ball = new Ball(canvas, ballConfig);
            canvas.add(ball);

            BallThreadJoin ballThread;
            if (previous == null) {
                ballThread = new BallThreadJoin(ball, 20);

            } else {
                ballThread = new BallThreadJoin(ball, previous, 20 * (i + 1));
            }

            previous = ballThread;

            ballThread.start();
            System.out.println("Thread name = " + ballThread.getName());
        }
    }

    public static void setUpPriority(BallCanvas canvas) {
        int size = 5;
        int distance = 1;
        for (int i = 0; i < 100; i++) {

            BallConfig ballConfig;
            var highPriorityThread = i % 2 == 0;

            ballConfig = getBallConfig(size, distance, i, highPriorityThread);

            Ball ball = new Ball(canvas, ballConfig);
            canvas.add(ball);

            BallThread ballThread = getBallThread(canvas, ball, highPriorityThread);
            ballThread.start();
        }
    }

    private static BallThread getBallThread(BallCanvas canvas, Ball ball, boolean highPriorityThread) {
        var ballThread = new BallThread(canvas, ball);
        ballThread.setPriority(highPriorityThread ? Thread.MAX_PRIORITY : Thread.MIN_PRIORITY);

        return ballThread;
    }

    private static BallConfig getBallConfig(int size, int distance, int i, boolean highPriorityThread) {
        return highPriorityThread
                ? new BallConfig(new Point(0, i * size + distance), 0, 2, Color.BLUE)
                : new BallConfig(new Point(0, i * size + distance), 0, 2, Color.RED);
    }
}
