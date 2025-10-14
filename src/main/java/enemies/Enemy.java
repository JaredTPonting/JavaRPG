package enemies;// enemies.Enemy.java
import java.awt.*;
import java.awt.image.BufferedImage;
import player.Player;
import utils.Camera;

public abstract class Enemy {
    protected double x, y;
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

    public void update() {
        if (dead) return;

        int targetX = target.getX();
        int targetY = target.getY();

        double dx = targetX - x;
        double dy = targetY - y;
        double distance = Math.sqrt(dx * dx + dy * dy);

        if (distance > 0) {
            x += (dx / distance) * speed;
            y += (dy / distance) * speed;
        }
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
