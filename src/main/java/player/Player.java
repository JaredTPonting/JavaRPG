package player;// Player.Player.java
import java.awt.*;
import java.awt.image.BufferedImage;
import utils.SpriteLoader;

public class Player {
    private int x, y;
    private int speed = 4;
    private boolean up, down, left, right;
    private BufferedImage playerSprite;

    public Player(int x, int y) {
        this.x = x;
        this.y = y;
        playerSprite = SpriteLoader.load("/sprites/chicken_front_idle-removebg.png");
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }


    public void update() {
        if (up) y -= speed;
        if (down) y += speed;
        if (left) x -= speed;
        if (right) x += speed;

        // Optional: Clamp to screen
        if (x < 0) x = 0;
        if (x > 768) x = 768; // 800 - 32
        if (y < 0) y = 0;
        if (y > 568) y = 568; // 600 - 32
    }

    public void render(Graphics g) {
        g.drawImage(playerSprite, x, y, 64, 64, null);
    }

    // Movement flags
    public void setUp(boolean up) { this.up = up; }
    public void setDown(boolean down) { this.down = down; }
    public void setLeft(boolean left) { this.left = left; }
    public void setRight(boolean right) { this.right = right; }
}
