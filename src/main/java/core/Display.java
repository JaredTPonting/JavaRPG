package core;

import javax.swing.*;
import java.awt.*;

public class Display {
    private final JFrame frame;
    private final Canvas canvas;
    private final int windowedWidth;
    private final int windowedHeight;
    private boolean fullscreen = false;

    public Display(String title, int width, int height, Canvas canvas) {
        this.canvas = canvas;
        this.windowedWidth = width;
        this.windowedHeight = height;

        frame = new JFrame(title);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setResizable(false);
        frame.setUndecorated(false);
        frame.add(canvas);
        frame.setSize(width, height);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    public void toggleFullscreen() {
        GraphicsDevice device = GraphicsEnvironment
                .getLocalGraphicsEnvironment()
                .getDefaultScreenDevice();

        if (!fullscreen) {
            // Enter fullscreen
            frame.dispose();
            frame.setUndecorated(true);
            device.setFullScreenWindow(frame);
            fullscreen = true;
        } else {
            // Exit fullscreen
            device.setFullScreenWindow(null);
            frame.dispose();
            frame.setUndecorated(false);
            frame.setSize(windowedWidth, windowedHeight);
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
            fullscreen = false;
        }

        // Re-focus canvas for input
        canvas.requestFocus();
    }

    public boolean isFullscreen() {
        return fullscreen;
    }

    public int getWidth() {
        return canvas.getWidth();
    }

    public int getHeight() {
        return canvas.getHeight();
    }

    public JFrame getFrame() {
        return frame;
    }
}
