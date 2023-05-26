package lab.balls;

import java.awt.*;
import java.awt.geom.Ellipse2D;
import java.util.Objects;
import java.util.Random;

public class Ball {
    private static final int BALL_DIAMETER = 20;

    private static final Random random = new Random();

    private final Component canvas;

    private int x;
    private int y;
    private int moveSpeedX;
    private int moveSpeedY;

    private Color color;

    public Ball(Component canvas, boolean randomizedCoordinates) {
        this.canvas = canvas;
        initFromBallConfig(BallConfig.Default);

        if (randomizedCoordinates) {
            x = random.nextInt(canvas.getWidth());
            y = random.nextInt(canvas.getHeight());
        }
    }

    public Ball(Component canvas, BallConfig config) {
        this.canvas = canvas;
        initFromBallConfig(config);
    }

    private void initFromBallConfig(BallConfig config) {
        var coords = config.getBallCoordinates();

        x = coords.getX();
        y = coords.getY();

        moveSpeedX = config.getXMoveSpeed();
        moveSpeedY = config.getYMoveSpeed();

        this.color = config.getColor();
    }

    public void draw(Graphics2D g2) {
        g2.setColor(color);
        g2.fill(new Ellipse2D.Double(x, y, BALL_DIAMETER, BALL_DIAMETER));
    }

    public void move() {
        doStepX();
        doStepY();

        this.canvas.repaint();
    }

    private void doStepX() {
        x += moveSpeedX;

        if (x < 0) {
            x = 0;
            moveSpeedX = -moveSpeedX;
        } else if (x + BALL_DIAMETER >= this.canvas.getWidth()) {
            x = this.canvas.getWidth() - BALL_DIAMETER;
            moveSpeedX = -moveSpeedX;
        }
    }

    private void doStepY() {
        y += moveSpeedY;

        if (y < 0) {
            y = 0;
            moveSpeedY = -moveSpeedY;
        } else if (y + BALL_DIAMETER >= this.canvas.getHeight()) {
            y = this.canvas.getHeight() - BALL_DIAMETER;
            moveSpeedY = -moveSpeedY;
        }
    }

    public boolean IsCollision(Pocket pocket) {
        double centerX = x + ((double) BALL_DIAMETER) / 2;
        double centerY = y + ((double) BALL_DIAMETER) / 2;

        return ((pocket.getX() < centerX && centerX < pocket.getX() + pocket.getSize()) &&
                (pocket.getY() < centerY && centerY < pocket.getY() + pocket.getSize()));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Ball ball = (Ball) o;
        return x == ball.x && y == ball.y && moveSpeedX == ball.moveSpeedX && moveSpeedY == ball.moveSpeedY && Objects.equals(canvas, ball.canvas);
    }

    @Override
    public int hashCode() {
        return Objects.hash(canvas, x, y, moveSpeedX, moveSpeedY);
    }
}
