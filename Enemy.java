// Enemy.java
import java.awt.*;

public class Enemy {
    private double x, y;
    private int size = 32;
    private int hp = 2;
    private double speed = 2;

    private Player target;

    public Enemy(int x, int y, Player target) {
        this.x = x;
        this.y = y;
        this.target = target;
    }

    public void update() {
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
        hp -= amount;
        if (hp <= 0) {
            // Enemy dies Logic
        }
    }

    public Rectangle getBounds() {
        return new Rectangle((int) x, (int) y, size, size);
    }

    public void render(Graphics g) {
        g.setColor(Color.RED);
        g.fillRect((int)x,(int) y, size, size);
    }
}
