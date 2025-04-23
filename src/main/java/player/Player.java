package player;// Player.Player.java
import java.awt.*;
import java.awt.image.BufferedImage;
import utils.SpriteLoader;

public class Player {
    private int x, y;
    private int speed = 4;
    private boolean up, down, left, right;
    private BufferedImage playerSprite;
    private int SCREEN_WIDTH;
    private int SCREEN_HEIGHT;
    private int X_MOVEMENT;
    private int Y_MOVEMENT;

    public Player(int x, int y, int screenWidth, int screenHeight) {
        this.x = x;
        this.y = y;
        playerSprite = SpriteLoader.load("/sprites/chicken_front_idle-removebg.png");
        SCREEN_HEIGHT = screenHeight;
        SCREEN_WIDTH = screenWidth;
        X_MOVEMENT = screenWidth - 32;
        Y_MOVEMENT = screenHeight - 32;
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
        if (x > X_MOVEMENT) x = X_MOVEMENT; // 800 - 32
        if (y < 0) y = 0;
        if (y > Y_MOVEMENT) y = Y_MOVEMENT; // 600 - 32
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
