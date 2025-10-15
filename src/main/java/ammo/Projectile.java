package ammo;

import utils.Camera;

import java.awt.*;
import java.awt.image.BufferedImage;

public abstract class Projectile {
    protected double startx, starty;
    protected double x, y;
    protected double maxDistance = 500;
    protected double dx, dy;
    protected double speed;
    protected boolean active = true;
    private double damage;

    protected BufferedImage sprite;
    protected int spriteSize = 32; // default size

    protected int mapWidth, mapHeight;

    public Projectile(double x, double y, double dirX, double dirY, int mapWidth, int mapHeight, double speed, double damage) {
        this.startx = x;
        this.starty = y;
        this.x = x;
        this.y = y;
        this.mapWidth = mapWidth;
        this.mapHeight = mapHeight;
        this.speed = speed;
        this.damage = damage;

        double len = Math.sqrt(dirX * dirX + dirY * dirY);
        if (len == 0) len = 1;

        this.dx = (dirX / len) * speed;
        this.dy = (dirY / len) * speed;

        loadSprite(); // implemented by subclass
    }

    public void update() {
        x += dx;
        y += dy;

        // If travelled further than max distance, deactivate it
        if (maxDistance > 0) {
            double dist = Math.hypot(x-startx, y-starty);
            if (dist >= maxDistance) {
                active = false;
            }
        }
    }

    public void render(Graphics g, Camera camera) {
        if (sprite != null) {
            g.drawImage(sprite, (int) (x - camera.getX()), (int) (y - camera.getY()), spriteSize, spriteSize, null);
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
