package entities.player;

import entities.Entity;

import java.awt.*;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;

import utils.*;

public class Player extends Entity {
    // Position & movement
    private boolean up, down, left, right;

    // util
    private final VectorManipulation vectorManipulation = new VectorManipulation();

    // Stats
    private final PlayerStats playerStats;
    private double range = 600.0;

    // XP system
    private final PlayerLevel playerLevel;

    // Sprites & animation
    private final BufferedImage[] idleSprites;
    private final BufferedImage[] walkSprites;
    private final BufferedImage[] runSprites;
    private int currentFrame = 0;
    private long lastFrameTime = 0;
    private final int frameDelay = 100;
    private boolean facingLeft = true;

    private boolean lastFacingLeft = true;
    private boolean lastFacingRight = false;
    private boolean lastFacingUp = false;
    private boolean lastFacingDown = false;
    private boolean invunerable = false;

    // Dash values
    private boolean isDashing = false;
    private Cooldown dashDuration = new Cooldown(0.15);
    private Cooldown dashCooldown = new Cooldown(0.95);
    private double dashSpeedMultiplier = 4.0;
    private double dashEnduranceCost = 50.0;

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

    public void dash() {
        if (dashCooldown.ready() && (dashEnduranceCost < this.getCurrentStamina())) {
            this.isDashing = true;
            this.playerStats.exhaustStamia(this.dashEnduranceCost);
        }
    }

    public boolean isDashing() { return this.isDashing;}


    private enum State { IDLE, WALK, RUN }
    private State currentState = State.IDLE;
    private State previousState = State.IDLE;

    // Constructor
    public Player(WorldContext gameWorld, int x, int y, int size, double xOffset, double yOffset) {
        super(gameWorld, x, y, size, xOffset, yOffset);

        this.playerStats = new PlayerStats(200.0, 20000.0, 0.5, 10000.0, 5.0, 50, 50);
        this.playerLevel = new PlayerLevel();

        idleSprites = loadSprites("/sprites/chicken/cute_chicken_idle_new.png", 6);
        walkSprites = loadSprites("/sprites/chicken/cute_chicken_walk_new.png", 6);
        runSprites = loadSprites("/sprites/chicken/cute_chicken_run_new.png", 2);
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
    public boolean getInvunerable() { return this.invunerable; }
    public int getLevel() { return this.playerLevel.getPlayerLevel(); }
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
    public void update(double dt) {
        double dx = 0;
        double dy = 0;
        double diagonalBoost = 1.05;

        if (up) {dy -= 1;}
        if (down) {dy += 1;}
        if (left) {dx -= 1;}
        if (right) {dx += 1;}

        if (dx == 0 & dy == 1) { // down
            setLastFacingDown(true);
            setLastFacingUp(false);
            setLastFacingLeft(false);
            setLastFacingRight(false);
        } else if (dx == 0 & dy == -1) { // up
            setLastFacingDown(false);
            setLastFacingUp(true);
            setLastFacingLeft(false);
            setLastFacingRight(false);
        } else if (dx == 1 & dy == -1) { // right up
            setLastFacingDown(false);
            setLastFacingUp(true);
            setLastFacingLeft(false);
            setLastFacingRight(true);
        } else if (dx == -1 & dy == 1) { // left down
            setLastFacingDown(true);
            setLastFacingUp(false);
            setLastFacingLeft(true);
            setLastFacingRight(false);
        } else if (dx == 1 & dy == 0) { // right
            setLastFacingDown(false);
            setLastFacingUp(false);
            setLastFacingLeft(false);
            setLastFacingRight(true);
        } else if (dx == -1 & dy == 0) { // left
            setLastFacingDown(false);
            setLastFacingUp(false);
            setLastFacingLeft(true);
            setLastFacingRight(false);
        } else if (dx == 1 & dy == 1) { // right down
            setLastFacingDown(true);
            setLastFacingUp(false);
            setLastFacingLeft(false);
            setLastFacingRight(true);
        } else if (dx == -1 & dy == -1) { // left up
            setLastFacingDown(false);
            setLastFacingUp(true);
            setLastFacingLeft(true);
            setLastFacingRight(false);
        }
        Point2D.Double dxDy = vectorManipulation.normalise(dx, dy);
        dx = dxDy.getX();
        dy = dxDy.getY();


        if (isDashing) {
            System.out.println("DASHING");
            dashDuration.update(dt);
            double dashSpeed = playerStats.getSpeed() * dashSpeedMultiplier;

            // Diagonal Boost
            if (dx != 0  && dy != 0) {
                x += dx * dashSpeed * diagonalBoost * dt;
                y += dy * dashSpeed * diagonalBoost * dt;
            } else {
                x += dx * dashSpeed * dt;
                y += dy * dashSpeed * dt;
            }

            updateHitBox();

            if (dashDuration.ready()) {
                dashDuration.reset();
                this.isDashing = false;
                this.invunerable = false;
            }
            return;
        }

        dashCooldown.update(dt);

        // Diagonal Boost
        if (dx != 0 && dy != 0) {
            x += dx * this.playerStats.getSpeed() * diagonalBoost * dt;
            y += dy * this.playerStats.getSpeed() * diagonalBoost * dt;
        } else {
            x += dx * this.playerStats.getSpeed() * dt;
            y += dy * this.playerStats.getSpeed() * dt;
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
            g.drawImage(sprite, (int) (x + this.size - camera.getX()), (int) (y - camera.getY()), -this.size, this.size, null);
        } else {
            g.drawImage(sprite, (int) (x - camera.getX()), (int) (y - camera.getY()), this.size, this.size, null);
        }
    }

    @Override
    public double getRenderY() {
        return this.y + size;
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

    // Stat upgrades
    public void levelUpMaxHealth() { this.playerStats.increaseMaxHealth(); this.levelUp();}
    public void levelUpSpeed() { this.playerStats.increaseSpeed(); this.levelUp();}
    public void levelUpEndurance() { this.playerStats.increaseEndurance(); this.levelUp();}
    public void levelUpStaminaRegen() { this.playerStats.increaseStaminaRegen(); this.levelUp();}
    public void levelUpRange() { range *= 1.1; range = Math.round(range * 100.0) / 100.0;this.levelUp();}
    public void levelUpHealthRegen() { this.playerStats.increaseHealthRegen(); this.levelUp();}
    public void levelUpDamage() { this.playerStats.increaseDamage(); this.levelUp();}
    public void levelUpMagicDamage() { this.playerStats.increaseMagicDamage(); this.levelUp();}

    public void levelUp() { if (this.playerLevel.canLevelUp()) this.playerLevel.levelUp(); }

    public boolean isDead() { return this.playerStats.isDead();}

    // Debug
    public void printStats() {
        System.out.println("Level: " + this.playerLevel.getPlayerLevel() + ", XP: " + (int) this.playerLevel.getExperiencePoints() + "/" + (int) this.playerLevel.getNextLevelCost());
        System.out.println("Speed: " + this.playerStats.getSpeed() + ", Range: " + range);
        System.out.println("Max Health: " + this.playerStats.getMaxHealth() + ", Regen: " + this.playerStats.getHealthRegen());
        System.out.println("Unspent Points: " + this.playerLevel.getUpgradePoints());
    }
}
