// Enemy.java
import java.awt.*;
import java.awt.image.BufferedImage;

public class Enemy {
    private double x, y;
    private int size = 32;
    private int hp = 2;
    private double speed = 2;
    private boolean dead = false;
    private BufferedImage enemySprite;

    private Player target;

    public Enemy(int x, int y, Player target) {
        this.x = x;
        this.y = y;
        this.target = target;

        enemySprite = SpriteLoader.load("/sprites/fox_front_idle-removebg.png");
    }

    public void update() {
        if (dead) {
            return;
        }
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
                // Enemy dies Logic
                System.out.println("Enemy defeated");
                die();
            }
        }
    }

    private void die() {
        dead = true;
    }

    public boolean isDead() {
        return dead;
    }

    public Rectangle getBounds() {
        return new Rectangle((int) x, (int) y, size, size);
    }

    public void render(Graphics g) {
        if (dead) {
            return;
        }
        g.drawImage(enemySprite, (int)x, (int) y, 64, 64, null);
    }
}
