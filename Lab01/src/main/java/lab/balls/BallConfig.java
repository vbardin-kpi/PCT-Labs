package lab.balls;

import lab.balls.configs.Point;

import java.awt.*;

public class BallConfig {
    public static BallConfig Default = new BallConfig(
            Point.Zero, 2, 2, Color.GREEN);

    private final Point ballCoordinates;
    private final int yMoveSpeed;
    private final int xMoveSpeed;
    private final Color color;

    public BallConfig(
            Point ballCoordinates,
            int yMoveSpeed,
            int xMoveSpeed,
            Color color) {
        this.ballCoordinates = ballCoordinates;
        this.yMoveSpeed = yMoveSpeed;
        this.xMoveSpeed = xMoveSpeed;
        this.color = color;
    }

    public Point getBallCoordinates() {
        return ballCoordinates;
    }

    public int getXMoveSpeed() {
        return xMoveSpeed;
    }

    public int getYMoveSpeed() {
        return yMoveSpeed;
    }

    public Color getColor() {
        return color;
    }
}
