package lab.balls;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;

public class BounceFrame extends JFrame {
    private final BallCanvas canvas;
    public static final int WIDTH = 450;
    public static final int HEIGHT = 350;

    public BounceFrame(RunningProflle profile) {
        this.setSize(WIDTH, HEIGHT);
        this.setTitle("Balls DEMO");

        this.canvas = new BallCanvas();

        if (profile == RunningProflle.DEFAULT) {
            canvas.addComponentListener(new ComponentAdapter() {
                @Override
                public void componentResized(ComponentEvent e) {
                    canvas.appendPockets(40);
                }
            });
        }

        System.out.println("In Frame Thread name = " + Thread.currentThread().getName());

        Container content = this.getContentPane();
        content.add(this.canvas, BorderLayout.CENTER);

        JPanel infoPanel = new JPanel();
        infoPanel.setBackground(Color.lightGray);

        startButton(infoPanel, profile);
        stopButton(infoPanel);
        scoreLabel(infoPanel);

        content.add(infoPanel, BorderLayout.SOUTH);
    }

    private void scoreLabel(JPanel infoPanel) {
        JLabel scoreLabel = new JLabel();
        canvas.setScoreLabel(scoreLabel);
        infoPanel.add(scoreLabel);
    }

    private void startButton(JPanel buttonPanel, RunningProflle profile) {
        JButton buttonStart = new JButton("Start");

        buttonStart.addActionListener(e -> {
            switch (profile) {
                case DEFAULT -> StartButtonActions.setUpDefault(canvas);
                case PRIORITY -> StartButtonActions.setUpPriority(canvas);
                case JOIN -> StartButtonActions.setUpJoin(canvas);
            }
        });

        buttonPanel.add(buttonStart);
    }

    private void stopButton(JPanel buttonPanel) {
        JButton buttonStop = new JButton("Stop");
        buttonStop.addActionListener(e -> System.exit(0));
        buttonPanel.add(buttonStop);
    }
}
