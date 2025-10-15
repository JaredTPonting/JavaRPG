package enemies;
import java.awt.*;
import java.awt.image.BufferedImage;
import player.Player;
import utils.Camera;

public abstract class Enemy {
    protected double x, y;
    protected double vx = 0, vy = 0;
    protected int size = 32;
    protected double hp;
    protected double speed;
    protected boolean dead = false;
    protected BufferedImage sprite;
    protected int XP;

    protected Player target;

    public Enemy(int x, int y, Player target) {
        this.x = x;
        this.y = y;
        this.target = target;
    }

    public void setSpeed(int speed) {
        this.speed = speed;
    }

    public double getX() {
        return this.x;
    }

    public void setX(double newX) {
        this.x = newX;
    }

    public double getY() {
        return this.y;
    }

    public void setY(double newY) {
        this.y = newY;
    }

    public int getXP() {
        return this.XP;
    }

    public double getVx() {
        return this.vx;
    }

    public void resetVxVy() {
        this.vx = 0;
        this.vy = 0;
    }

    public void update(java.util.List<Enemy> allEnemies) {
        if (dead) return;

        int targetX = target.getX();
        int targetY = target.getY();

        double dx = targetX - x;
        double dy = targetY - y;
        double distance = Math.sqrt(dx * dx + dy * dy);

        // Attraction toward player
        if (distance > 0) {
            vx += (dx / distance) * 0.2;  // small acceleration toward player
            vy += (dy / distance) * 0.2;
        }

        // Separation from nearby enemies
        double separationForceX = 0;
        double separationForceY = 0;
        double separationRadius = size * 1.5; // radius to avoid overlap

        for (Enemy e : allEnemies) {
            if (e == this || e.isDead()) continue;
            double ex = e.getX();
            double ey = e.getY();
            double dist = Math.hypot(x - ex, y - ey);
            if (dist < separationRadius && dist > 0) {
                separationForceX += (x - ex) / dist;
                separationForceY += (y - ey) / dist;
            }
        }

        vx += separationForceX * 0.2;
        vy += separationForceY * 0.2;

        // Add small random jitter so enemies don't sync up perfectly
        vx += (Math.random() - 0.5) * 0.05;
        vy += (Math.random() - 0.5) * 0.05;

        // SPEED LIMIT
        double speedMag = Math.hypot(vx, vy);
        if (speedMag > speed) {
            vx = (vx / speedMag) * speed;
            vy = (vy / speedMag) * speed;
        }

        // Apply movement
        x += vx;
        y += vy;
    }


    public void damage(double amount) {
        if (!dead) {
            hp -= amount;
            if (hp <= 0) {
                die();
            }
        }
    }

    protected void die() {
        dead = true;
        System.out.println(this.getClass().getSimpleName() + " defeated");
    }

    public boolean isDead() {
        return dead;
    }

    public Rectangle getBounds() {
        return new Rectangle((int) x, (int) y, size, size);
    }

    public void render(Graphics g, Camera camera) {
        if (!dead) {
            g.drawImage(sprite, (int) x - camera.getX(), (int) y - camera.getY(), 64, 64, null);
        }
    }
}
