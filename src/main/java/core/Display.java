package core;

import javax.swing.*;
import java.awt.*;

public class Display {
    private final JFrame frame;

    public Display(String title, int width, int height, Canvas canvas) {
        frame = new JFrame(title);
        frame.setSize(width, height);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setResizable(false);
        frame.add(canvas);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    public JFrame getFrame() {
        return frame;
    }
}
