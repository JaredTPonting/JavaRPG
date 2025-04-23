package enemies;// enemies.Enemy.java
import java.awt.*;
import java.awt.image.BufferedImage;
import player.Player;

public abstract class Enemy {
    protected double x, y;
    protected int size = 32;
    protected int hp;
    protected double speed;
    protected boolean dead = false;
    protected BufferedImage sprite;

    protected Player target;

    public Enemy(int x, int y, Player target) {
        this.x = x;
        this.y = y;
        this.target = target;
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

    public void damage(int amount) {
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

    public void render(Graphics g) {
        if (!dead) {
            g.drawImage(sprite, (int) x, (int) y, 64, 64, null);
        }
    }
}
