package ui;

import java.awt.*;

public class Button {
    int x, y, width, height;
    String text;
    Runnable action;
    boolean hovered = false;

    private final Color baseColor = new Color(50, 50, 50);
    private final Color hoverColor = new Color(80, 80, 80);
    private final Color textColor = Color.WHITE;
    private final int arc = 15; // rounded corners

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

        // Background
        g2.setColor(hovered ? hoverColor : baseColor);
        g2.fillRoundRect(x, y, width, height, arc, arc);

        // Border
        g2.setColor(Color.WHITE);
        g2.setStroke(new BasicStroke(2));
        g2.drawRoundRect(x, y, width, height, arc, arc);

        // Text
        g2.setColor(textColor);
        FontMetrics fm = g2.getFontMetrics();
        int textWidth = fm.stringWidth(text);
        int textHeight = fm.getAscent();
        int tx = x + (width - textWidth) / 2;
        int ty = y + (height + textHeight) / 2 - 3;
        g2.drawString(text, tx, ty);
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
