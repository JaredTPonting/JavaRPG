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

    // Player Manager
    private final PlayerManager playerManager;

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
        if (dashCooldown.ready() && (dashEnduranceCost < this.playerManager.getCurrentStamina())) {
            this.isDashing = true;
            this.playerManager.getPlayerStats().exhaustStamia(this.dashEnduranceCost);
        }
    }

    public boolean isDashing() { return this.isDashing;}


    private enum State { IDLE, WALK, RUN }
    private State currentState = State.IDLE;
    private State previousState = State.IDLE;

    // Constructor
    public Player(WorldContext gameWorld, int x, int y, int size, double xOffset, double yOffset) {
        super(gameWorld, x, y, size, xOffset, yOffset);
        this.playerManager = new PlayerManager();
        this.setHitBox(0.5, 0.5, 0.5);

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

    // Manager Getter
    public PlayerManager getPlayerManager() {
        return this.playerManager;
    }

    // Position getters
    public double getX() { return x; }
    public double getY() { return y; }

    public Point getCenter() {
        int centerX = (int) (this.x + (this.size / 2));
        int centerY = (int) (this.y + (this.size / 2));
        return new Point(centerX, centerY);
    }

    public boolean getInvunerable() { return this.invunerable; }
    public PlayerStats getPlayerStats() {
        return this.playerManager.getPlayerStats();
    }
    public void takeDamage(double damage) {
        this.playerManager.takeDamage(damage);
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
            dashDuration.update(dt);
            this.invunerable = true;
            double dashSpeed = playerManager.getSpeed() * dashSpeedMultiplier;

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
            x += dx * playerManager.getSpeed() * diagonalBoost * dt;
            y += dy * playerManager.getSpeed() * diagonalBoost * dt;
        } else {
            x += dx * playerManager.getSpeed() * dt;
            y += dy * playerManager.getSpeed() * dt;
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

        this.playerManager.getPlayerStats().update();

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
        if (gameWorld.isDebugMode()) {
            drawHitBox(g, camera);
        }
    }

    @Override
    public double getRenderY() {
        return this.y + size;
    }



    public boolean isDead() { return this.playerManager.getPlayerStats().isDead();}
}
