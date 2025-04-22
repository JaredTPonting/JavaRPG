import java.awt.*;

public class Bullet {
    private double x, y;
    private double speed = 10;
    private double dx, dy;
    private boolean isActive = true;  // Bullet disappears when it's inactive

    public Bullet(double x, double y, double targetX, double targetY) {
        this.x = x;
        this.y = y;

        // Calculate direction towards target (mouse position)
        double angle = Math.atan2(targetY - y, targetX - x);
        dx = Math.cos(angle) * speed;
        dy = Math.sin(angle) * speed;
    }

    public void update() {
        x += dx;
        y += dy;

        // If the bullet moves out of bounds, deactivate it
        if (x < 0 || x > 800 || y < 0 || y > 600) { // Assuming 800x600 screen size NEEDS DMADE DYNAMIC
            isActive = false;
        }
    }

    public void render(Graphics g) {
        g.setColor(Color.BLACK);
        g.fillRect((int) x, (int) y, 5, 5);  // 5x5 pixel square bullet
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        this.isActive = active;
    }

    public Rectangle getBounds() {
        return new Rectangle((int) x, (int) y, 5, 5);
    }
}
