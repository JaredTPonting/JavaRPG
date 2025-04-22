import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Random;

public class Bullet {
    private Random random = new Random();
    private double x, y;
    private double speed = 10;
    private double dx, dy;
    private boolean isActive = true;

    private BufferedImage bulletSpriteSheet;
    private BufferedImage[] bulletSprites;
    private BufferedImage bulletSprite;

    private int spriteWidth = 5;
    private int spriteHeight = 6;
    private int spriteSize = 32;
    private int numberEggs = 30;

    public Bullet(double x, double y, double targetX, double targetY) {
        this.x = x;
        this.y = y;

        // Calculate direction towards target (mouse position)
        double angle = Math.atan2(targetY - y, targetX - x);
        dx = Math.cos(angle) * speed;
        dy = Math.sin(angle) * speed;

        bulletSpriteSheet = SpriteLoader.load("/sprites/bulletSpriteSheet.png");
        bulletSprites = new BufferedImage[30];
        int i = 0;
        for (int t = 0; t < spriteWidth; t++) {
            for (int k = 0; k < spriteHeight; k++) {
                bulletSprites[i] = bulletSpriteSheet.getSubimage(t * spriteSize, k * spriteSize, spriteSize, spriteSize);
                i += 1;
            }
        }
        bulletSprite = bulletSprites[random.nextInt(30)];


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
        g.drawImage(bulletSprite, (int) x, (int) y, 64, 64, null);
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

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }
}
