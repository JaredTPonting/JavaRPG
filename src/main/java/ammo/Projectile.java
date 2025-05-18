package ammo;

import java.awt.*;
import java.awt.image.BufferedImage;

public abstract class Projectile {
    protected double x, y;
    protected double dx, dy;
    protected double speed;
    protected boolean active = true;
    private double damage;

    protected BufferedImage sprite;
    protected int spriteSize = 32; // default size

    protected int screenWidth, screenHeight;

    public Projectile(double x, double y, double targetX, double targetY, int screenWidth, int screenHeight, double speed, double damage) {
        this.x = x;
        this.y = y;
        this.screenWidth = screenWidth;
        this.screenHeight = screenHeight;
        this.speed = speed;
        this.damage = damage;

        double angle = Math.atan2(targetY - y, targetX - x);
        this.dx = Math.cos(angle) * speed;
        this.dy = Math.sin(angle) * speed;

        loadSprite(); // implemented by subclass
    }

    public void update() {
        x += dx;
        y += dy;

        if (x < -spriteSize || x > screenWidth + spriteSize || y < -spriteSize || y > screenHeight + spriteSize) {
            active = false;
        }
    }

    public void render(Graphics g) {
        if (sprite != null) {
            g.drawImage(sprite, (int) x, (int) y, spriteSize, spriteSize, null);
        }
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public Rectangle getBounds() {
        return new Rectangle((int) x, (int) y, spriteSize, spriteSize);
    }

    public double getX() { return x; }
    public double getY() { return y; }

    public double getDamage() { return damage; }

    protected abstract void loadSprite(); // subclasses implement this
}
