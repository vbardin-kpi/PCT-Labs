package lab.balls;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class BallCanvas extends JPanel {
    private final ArrayList<Ball> balls = new ArrayList<>();
    private ArrayList<Pocket> pockets = new ArrayList<>();
    private JLabel scoreLabel;

    public void setScoreLabel(JLabel scoreLabel) {
        this.scoreLabel = scoreLabel;
    }

    public void add(Ball ball) {
        this.balls.add(ball);
    }

    public void remove(Ball ball) {
        this.balls.remove(ball);
    }

    public void appendPockets(int pocketSize) {
        pockets = new ArrayList<>();
        double halfSize = ((double) pocketSize) / 2;
        for (double i = 0; i < 6; i++) {
            if (i % 2 == 0) {
                pockets.add(new Pocket((i / 4 * this.getWidth()) - halfSize, -halfSize, pocketSize));
            } else {
                pockets.add(new Pocket(((i - 1) / 4 * this.getWidth()) - halfSize, this.getHeight() - halfSize, pocketSize));
            }
        }
    }

    public List<Pocket> getPockets() {
        return pockets;
    }

    @Override
    public void paintComponent(Graphics graphics) {
        super.paintComponent(graphics);
        Graphics2D graphics2D = (Graphics2D) graphics;

        var score = 0;

        for (Pocket pocket : pockets) {
            pocket.draw(graphics2D);
            score += pocket.getScore();
        }

        for (Ball ball : balls) {
            ball.draw(graphics2D);
        }

        scoreLabel.setText("Current score - " + score);
    }
}
