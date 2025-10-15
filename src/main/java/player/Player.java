package player;

import java.awt.*;
import java.awt.image.BufferedImage;
import utils.Camera;
import utils.SpriteLoader;

public class Player {
    // Position & movement
    private int x, y;
    private double speed = 4.0;        // Can upgrade
    private boolean up, down, left, right;
    private final int SCREEN_WIDTH, SCREEN_HEIGHT;
    private final int X_MOVEMENT, Y_MOVEMENT;

    // Stats
    private double range = 600.0;      // Can upgrade
    private double maxHealth = 100.0;  // Can upgrade
    private double currentHealth = maxHealth;
    private double healthRegen = 0.5;  // Can upgrade
    private double maxStamina = 100; // can upgrade
    private double currentStamina = maxStamina;
    private double staminaRegen = 5.0;

    private long lastRegenTime;

    // XP system
    private int xp = 0;
    private int level = 1;
    private int maxXP = 100;
    private int xpToNextLevel = 100;
    private int upgradePoints = 0;
    private boolean leveledUp = false;

    // Sprites & animation
    private BufferedImage[] idleSprites, walkSprites, runSprites;
    private int currentFrame = 0;
    private long lastFrameTime = 0;
    private final int frameDelay = 100;
    private boolean facingLeft = true;

    private enum State { IDLE, WALK, RUN }
    private State currentState = State.IDLE;
    private State previousState = State.IDLE;

    // Constructor
    public Player(int x, int y, int screenWidth, int screenHeight) {
        this.x = x;
        this.y = y;
        this.SCREEN_WIDTH = screenWidth;
        this.SCREEN_HEIGHT = screenHeight;
        this.X_MOVEMENT = screenWidth - 32;
        this.Y_MOVEMENT = screenHeight - 32;

        this.lastRegenTime = System.currentTimeMillis();

        idleSprites = loadSprites("/sprites/cute_chicken_idle.png", 6);
        walkSprites = loadSprites("/sprites/cute_chicken_walk.png", 6);
        runSprites = loadSprites("/sprites/cute_chicken_run.png", 2);
    }

    private BufferedImage[] loadSprites(String path, int frameCount) {
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

    // Movement flags
    public void setUp(boolean up) { this.up = up; }
    public void setDown(boolean down) { this.down = down; }
    public void setLeft(boolean left) { this.left = left; }
    public void setRight(boolean right) { this.right = right; }

    public void resetInput() {
        this.up = false;
        this.down = false;
        this.left = false;
        this.right = false;
    }

    // Position getters
    public int getX() { return x; }
    public int getY() { return y; }

    // Stat getters
    public double getSpeed() { return speed; }
    public double getRange() { return range; }
    public double getMaxHealth() { return maxHealth; }
    public double getCurrentHealth() { return currentHealth; }
    public double getHealthRegen() { return healthRegen; }
    public double getMaxStamina() { return maxStamina; };
    public double getCurrentStamina() { return currentStamina; }
    public int getLevel() { return level; }
    public int getUnspentPoints() { return upgradePoints; }
    public int getXP() { return this.xp;}
    public int getXPToNextLevel() { return this.xpToNextLevel;}
    public int getMaxXP() { return this.maxXP; }

    // handle regens
    private void regen() {
        long now = System.currentTimeMillis();
        if (now - lastRegenTime >= 1000) {
            lastRegenTime = now;
            if (currentHealth < maxHealth) {
                currentHealth = Math.min(maxHealth, currentHealth + healthRegen);
            }

            if (currentStamina < maxStamina) {
                currentStamina = Math.min(maxStamina, currentStamina + staminaRegen);
            }
        }
    }


    // Update method
    public void update() {
        int dx = 0;
        int dy = 0;

        if (up) dy -= 1;
        if (down) dy += 1;
        if (left) dx -= 1;
        if (right) dx += 1;

        // Normalize if moving diagonally
        if (dx != 0 && dy != 0) {
            double diagonal = Math.sqrt(2);
            x += (dx / diagonal) * speed * 1.05;
            y += (dy / diagonal) * speed * 1.05;
        } else {
            x += dx * speed;
            y += dy * speed;
        }

        // Facing & animation state
        if (left) facingLeft = true;
        if (right) facingLeft = false;

        boolean isMoving = up || down || left || right;
        boolean isRunning = left || right;

        currentState = isMoving ? (isRunning ? State.RUN : State.WALK) : State.IDLE;
        if (currentState != previousState) {
            currentFrame = 0;
            previousState = currentState;
        }

        regen();

        // Frame animation
        long now = System.currentTimeMillis();
        if (now - lastFrameTime > frameDelay) {
            currentFrame = (currentFrame + 1) % getCurrentSprites().length;
            lastFrameTime = now;
        }
    }


    // Render
    public void render(Graphics g, Camera camera) {
        BufferedImage sprite = getCurrentSprites()[currentFrame];
        if (facingLeft) {
            Graphics2D g2d = (Graphics2D) g;
            g2d.drawImage(sprite, x + 48 - camera.getX(), y - camera.getY(), -48, 48, null);
        } else {
            g.drawImage(sprite, x - camera.getX(), y - camera.getY(), 48, 48, null);
        }
    }

    // XP & level system
    public void gainXP(int amount) {
        xp += amount;
        System.out.println("Gained " + amount + " XP. Total: " + xp);
        checkLevelUp();
    }

    private void checkLevelUp() {
        while (xp >= maxXP) {
            xp -= maxXP;
            level++;
            upgradePoints += 3;
            maxXP = (int)(maxXP * 1.2);
            System.out.println("Leveled Up! Now level " + level);
            leveledUp = true;
        }
    }

    public boolean hasLeveledUp() { return leveledUp; }
    public void clearLevelUpFlag() { leveledUp = false; }

    // Stat upgrades
    public void increaseMaxHealth() { maxHealth *= 1.1; maxHealth = Math.round(maxHealth * 100.0) / 100.0; currentHealth = maxHealth; }
    public void increaseSpeed() { speed *= 1.1; speed = Math.round(speed * 100.0) / 100.0; }
    public void increaseEndurance() { maxStamina *= 1.1; maxStamina = Math.round(maxStamina * 100.0) / 100.0;}
    public void increaseRange() { range *= 1.1; range = Math.round(range * 100.0) / 100.0;}
    public void increaseHealthRegen() { healthRegen *= 1.1; healthRegen = Math.round(healthRegen * 100.0) / 100.0; }

    public void spendPoint() { if (upgradePoints > 0) upgradePoints--; }

    // Debug
    public void printStats() {
        System.out.println("Level: " + level + ", XP: " + xp + "/" + xpToNextLevel);
        System.out.println("Speed: " + speed + ", Range: " + range);
        System.out.println("Max Health: " + maxHealth + ", Regen: " + healthRegen);
        System.out.println("Unspent Points: " + upgradePoints);
    }
}
