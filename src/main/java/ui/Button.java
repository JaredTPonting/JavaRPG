package ui;

import java.awt.*;
import java.awt.geom.RoundRectangle2D;

public class Button {
    int x, y, width, height;
    String text;
    Runnable action;
    boolean hovered = false;

    // Colors matching home screen style
    private static final Color BUTTON_COLOR = new Color(255, 90, 60);
    private static final Color BUTTON_HOVER = new Color(255, 120, 90);
    private static final Color SHADOW_COLOR = new Color(0, 0, 0, 50);
    private static final Color TEXT_COLOR = Color.WHITE;
    private static final int ARC = 15;

    public Button(int x, int y, int width, int height, String text, Runnable action) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.text = text;
        this.action = action;
    }

    public void render(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // Shadow
        g2.setColor(SHADOW_COLOR);
        g2.fill(new RoundRectangle2D.Double(x + 2, y + 2, width, height, ARC, ARC));

        // Background
        g2.setColor(hovered ? BUTTON_HOVER : BUTTON_COLOR);
        g2.fill(new RoundRectangle2D.Double(x, y, width, height, ARC, ARC));

        // Border
        g2.setColor(Color.WHITE);
        g2.setStroke(new BasicStroke(2));
        g2.draw(new RoundRectangle2D.Double(x, y, width, height, ARC, ARC));

        // Text
        g2.setColor(TEXT_COLOR);
        FontMetrics fm = g2.getFontMetrics();
        int textWidth = fm.stringWidth(text);
        int textX = x + (width - textWidth) / 2;
        int textY = y + ((height - fm.getHeight()) / 2) + fm.getAscent();
        g2.drawString(text, textX, textY);
    }

    public boolean contains(int mx, int my) {
        return mx >= x && mx <= x + width && my >= y && my <= y + height;
    }

    public void click() {
        if (action != null) action.run();
    }

    public void setHovered(boolean hovered) {
        this.hovered = hovered;
    }
}
