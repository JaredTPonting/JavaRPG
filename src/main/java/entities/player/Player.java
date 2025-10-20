package entities.player;

import entities.Entity;

import java.awt.*;
import java.awt.image.BufferedImage;
import utils.Camera;
import utils.GameWorld;
import utils.SpriteLoader;

public class Player extends Entity {
    // Position & movement
    private boolean up, down, left, right;

    // Stats
    private PlayerStats playerStats;
    private double range = 600.0;

    // XP system
    private PlayerLevel playerLevel;
    private boolean leveledUp = false;

    // Sprites & animation
    private BufferedImage[] idleSprites, walkSprites, runSprites;
    private int currentFrame = 0;
    private long lastFrameTime = 0;
    private final int frameDelay = 100;
    private boolean facingLeft = true;
    private boolean facingRight = false;

    private boolean lastFacingLeft = true;
    private boolean lastFacingRight = false;
    private boolean lastFacingUp = false;
    private boolean lastFacingDown = false;

    public boolean isLastFacingLeft() {
        return lastFacingLeft;
    }

    public void setLastFacingLeft(boolean lastFacingLeft) {
        this.lastFacingLeft = lastFacingLeft;
    }

    public boolean isLastFacingRight() {
        return lastFacingRight;
    }

    public void setLastFacingRight(boolean lastFacingRight) {
        this.lastFacingRight = lastFacingRight;
    }

    public boolean isLastFacingUp() {
        return lastFacingUp;
    }

    public void setLastFacingUp(boolean lastFacingUp) {
        this.lastFacingUp = lastFacingUp;
    }

    public boolean isLastFacingDown() {
        return lastFacingDown;
    }

    public void setLastFacingDown(boolean lastFacingDown) {
        this.lastFacingDown = lastFacingDown;
    }


    private enum State { IDLE, WALK, RUN }
    private State currentState = State.IDLE;
    private State previousState = State.IDLE;

    private long lastUpdateTime = System.nanoTime();

    // Constructor
    public Player(GameWorld gameWorld, int x, int y, int width, int height) {
        super(gameWorld, x, y, width, height);

        this.playerStats = new PlayerStats(200.0, 100.0, 0.5, 100.0, 5.0, 50, 50);
        this.playerLevel = new PlayerLevel();

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

    public boolean getUp() { return this.up; }
    public boolean getDown() { return this.down; }
    public boolean getLeft() { return this.left; }
    public boolean getRight() { return this.right; }

    public void resetInput() {
        this.up = false;
        this.down = false;
        this.left = false;
        this.right = false;
    }

    // Position getters
    public double getX() { return x; }
    public double getY() { return y; }

    // Stat getters
    public double getSpeed() { return this.playerStats.getSpeed(); }
    public double getRange() { return range; }
    public double getMaxHealth() { return this.playerStats.getMaxHealth(); }
    public double getCurrentHealth() { return this.playerStats.getCurrentHealth(); }
    public double getHealthRegen() { return this.playerStats.getHealthRegen(); }
    public double getMaxStamina() { return this.playerStats.getMaxStamina(); };
    public double getCurrentStamina() { return this.playerStats.getCurrentStamina(); }
    public int getLevel() { return this.playerStats.getLevel(); }
    public int getXP() { return (int) this.playerLevel.getExperiencePoints();}
    public int getXPToNextLevel() { return (int) this.playerLevel.getNextLevelCost() - (int) this.playerLevel.getExperiencePoints();}
    public int getMaxXP() { return (int) this.playerLevel.getNextLevelCost(); }
    public PlayerStats getPlayerStats() {
        return this.playerStats;
    }

    public void takeDamage(double damage) {
        this.playerStats.takeDamage(damage);
    }


    // Update method
    @Override
    public void update() {
        long now = System.nanoTime();
        double deltaTime = (now - lastUpdateTime) / 1_000_000_000.0;
        lastUpdateTime = now;

        int dx = 0;
        int dy = 0;

        if (up) {dy -= 1; setLastFacingUp(true); setLastFacingDown(false);}
        if (down) {dy += 1; setLastFacingDown(true); setLastFacingUp(false);}
        if (!up & !down) {setLastFacingUp(false); setLastFacingDown(false);}
        if (left) {dx -= 1; setLastFacingLeft(true); setLastFacingRight(false);}
        if (right) {dx += 1; setLastFacingRight(true); setLastFacingLeft(false);}

        // Normalize if moving diagonally
        double diagonalBoost = 1.05;
        if (dx != 0 && dy != 0) {
            double diagonal = Math.sqrt(2);
            x += ((double) dx / diagonal) * this.playerStats.getSpeed() * diagonalBoost * deltaTime;
            y += ((double) dy / diagonal) * this.playerStats.getSpeed() * diagonalBoost * deltaTime;
        } else {
            x += dx * this.playerStats.getSpeed() * deltaTime;
            y += dy * this.playerStats.getSpeed() * deltaTime;
        }

        // Facing & animation state
        if (left){ facingLeft = true;}
        if (right){ facingLeft = false;}

        boolean isMoving = up || down || left || right;
        boolean isRunning = left || right;

        currentState = isMoving ? (isRunning ? State.RUN : State.WALK) : State.IDLE;
        if (currentState != previousState) {
            currentFrame = 0;
            previousState = currentState;
        }

        playerStats.update();

        // Frame animation
        long nowMillis = System.currentTimeMillis();
        if (nowMillis - lastFrameTime > frameDelay) {
            currentFrame = (currentFrame + 1) % getCurrentSprites().length;
            lastFrameTime = nowMillis;
        }
        updateHitBox();
    }


    // Render
    @Override
    public void render(Graphics g, Camera camera) {
        BufferedImage sprite = getCurrentSprites()[currentFrame];
        if (facingLeft) {
            Graphics2D g2d = (Graphics2D) g;
            g2d.drawImage(sprite, (int) (x + 48 - camera.getX()), (int) (y - camera.getY()), -this.width, this.height, null);
        } else {
            g.drawImage(sprite, (int) (x - camera.getX()), (int) (y - camera.getY()), this.width, this.height, null);
        }
    }

    // XP & level system
    public void gainXP(int amount) {
        this.playerLevel.addExperiencePoints(amount);
        checkLevelUp();
    }

    public boolean checkLevelUp() {
        if (this.playerLevel.canLevelUp()) {
            return true;
        } else {
            return false;
        }
    }

    public boolean hasLeveledUp() { return leveledUp; }
    public void clearLevelUpFlag() { leveledUp = false; }

    // Stat upgrades
    public void increaseMaxHealth() { this.playerStats.increaseHealthLevel(); }
    public void increaseSpeed() { this.playerStats.increaseSpeedLevel(); }
    public void increaseEndurance() { this.playerStats.increaseEndurance();}
    public void increaseRange() { range *= 1.1; range = Math.round(range * 100.0) / 100.0;}
    public void increaseHealthRegen() { this.playerStats.increaseHealthRegen(); }

    public void levelUp() { if (this.playerLevel.canLevelUp()) this.playerLevel.levelUp(); }

    // Debug
    public void printStats() {
        System.out.println("Level: " + this.playerStats.getLevel() + ", XP: " + (int) this.playerLevel.getExperiencePoints() + "/" + (int) this.playerLevel.getNextLevelCost());
        System.out.println("Speed: " + this.playerStats.getSpeed() + ", Range: " + range);
        System.out.println("Max Health: " + this.playerStats.getMaxHealth() + ", Regen: " + this.playerStats.getHealthRegen());
        System.out.println("Unspent Points: " + this.playerLevel.getUpgradePoints());
    }
}
