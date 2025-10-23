package ui;

import java.awt.*;

class Button {
    int x, y, width, height;
    String text;
    Runnable action;

    public Button(int x, int y, int width, int height, String text, Runnable action) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.text = text;
        this.action = action;
    }

    public void render(Graphics g) {
        g.setColor(Color.DARK_GRAY);
        g.fillRect(x, y, width, height);
        g.setColor(Color.WHITE);
        g.drawRect(x, y, width, height);
        g.drawString(text, x + 10, y + height / 2 + 5);
    }

    public boolean contains(int mx, int my) {
        return mx >= x && mx <= x + width && my >= y && my <= y + height;
    }

    public void click() {
        if (action != null) action.run();
    }
}

