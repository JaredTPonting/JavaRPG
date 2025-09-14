package player;// Player.Player.java
import java.awt.*;
import java.awt.image.BufferedImage;

import utils.Camera;
import utils.SpriteLoader;

public class Player {
    private int x, y;
    private int speed = 4; // Can upgrade
    private int range = 600; // Can Upgrade
    private int maxHealth = 100; // Can upgrade
    private int currentHealth = maxHealth;
    private double healthRegen = 0.5; // Can upgrade
    private boolean up, down, left, right;
    private int SCREEN_WIDTH;
    private int SCREEN_HEIGHT;
    private int X_MOVEMENT;
    private int Y_MOVEMENT;

    private BufferedImage[] idleSprites;
    private BufferedImage[] walkSprites;
    private BufferedImage[] runSprites;
    private int currentFrame = 0;
    private long lastFrameTime = 0;
    private int frameDelay = 100;

    private enum State { IDLE, WALK, RUN};
    private State currentState = State.IDLE;
    private State previousState = State.IDLE;


    // XP System
    private int xp = 0;
    private int level = 1;
    private int xpToNextLevel = 100;
    private int upgradePoints = 0;


    private boolean facingLeft = true;


    public Player(int x, int y, int screenWidth, int screenHeight) {
        this.x = x;
        this.y = y;
        idleSprites = loadSprites("/sprites/cute_chicken_idle.png", 6);
        walkSprites = loadSprites("/sprites/cute_chicken_walk.png", 6);
        runSprites = loadSprites("/sprites/cute_chicken_run.png", 2);
        SCREEN_HEIGHT = screenHeight;
        SCREEN_WIDTH = screenWidth;
        X_MOVEMENT = screenWidth - 32;
        Y_MOVEMENT = screenHeight - 32;
    }

    private BufferedImage[] loadSprites(String path,int frameCount) {
        BufferedImage sheet = SpriteLoader.load(path);
        BufferedImage[] sprites = new BufferedImage[frameCount];
        for (int i = 0; i < frameCount; i++) {
            sprites[i] = sheet.getSubimage(i * 48, 0, 48, 48);
        }
        return sprites;
    }

    private BufferedImage[] getCurrentSprites() {
        return switch (currentState) {
            case WALK -> walkSprites;
            case RUN -> runSprites;
            default -> idleSprites;
        };
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getRange() {
        return range;
    }


    public void update() {
        if (up) y -= speed;
        if (down) y += speed;
        if (left) x -= speed;
        if (right) x += speed;

        boolean isMoving = up || down || left || right;
        boolean isRunning = right || left;

        if (left) facingLeft = true;
        if (right) facingLeft = false;


        if (!isMoving) currentState = State.IDLE;
        else currentState = isRunning ? State.RUN : State.WALK;

        if (currentState != previousState) {
            currentFrame = 0;
            previousState = currentState;
        }

        long now = System.currentTimeMillis();
        if (now - lastFrameTime > frameDelay) {
            currentFrame = (currentFrame + 1) % getCurrentSprites().length;
            lastFrameTime = now;
        }

        // Optional: Clamp to screen
        if (x < 0) x = 0;
        if (x > X_MOVEMENT) x = X_MOVEMENT; // 800 - 32
        if (y < 0) y = 0;
        if (y > Y_MOVEMENT) y = Y_MOVEMENT; // 600 - 32
    }

    public void render(Graphics g, Camera camera) {

        BufferedImage sprite = getCurrentSprites()[currentFrame];

        if (facingLeft) {
            Graphics2D g2d = (Graphics2D) g;
            g2d.drawImage(sprite, x + 48 - camera.getX(), y - camera.getY(), -48, 48, null);
        } else {
            g.drawImage(sprite, x - camera.getX(), y - camera.getY(), 48, 48, null);
        }
    }

    public void gainXP(int amount) {
        xp += amount;
        System.out.println("Gained " + amount + ". Total XP: " + xp);
        checkLevelUp();
    }

    private void checkLevelUp() {
        while (xp > xpToNextLevel) {
            xp -= xpToNextLevel;
            level++;
            upgradePoints += 3;
            xpToNextLevel = (int)(xpToNextLevel * 1.2);
            System.out.println("Leveled Up!: " + level);
        }
    }

    public void upgradeStat(String stat) {
        if (upgradePoints <= 0) {
            System.out.println("No upgrade points available!");
            return;
        }

        switch (stat.toLowerCase()) {
            case "speed":
                speed += 1;
                break;
            case "health":
            case "max health":
                maxHealth += 10;
                currentHealth += 10;
                break;
            case "regen":
            case "health regen":
                healthRegen += 0.2;
                break;
            default:
                System.out.println("Unknown stat: " + stat);
                return;
        }

        upgradePoints--;
        System.out.println(stat + " upgraded! Remaining points: " + upgradePoints);
        printStats();
    }

    public void printStats() {
        System.out.println("Level: " + level + ", XP: " + xp + "/" + xpToNextLevel);
        System.out.println("Speed: " + speed);
        System.out.println("Max Health: " + maxHealth + ", Health Regen: " + healthRegen);
    }

    // Movement flags
    public void setUp(boolean up) { this.up = up; }
    public void setDown(boolean down) { this.down = down; }
    public void setLeft(boolean left) { this.left = left; }
    public void setRight(boolean right) { this.right = right; }
}
