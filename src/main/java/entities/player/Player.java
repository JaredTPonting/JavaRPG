package entities.player;

import entities.Entity;

import java.awt.*;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;

import utils.*;

public class Player extends Entity {
    // 8-directional facing for weapons/projectiles
    public enum Direction {
        UP(0, -1), DOWN(0, 1), LEFT(-1, 0), RIGHT(1, 0),
        UP_LEFT(-1, -1), UP_RIGHT(1, -1), DOWN_LEFT(-1, 1), DOWN_RIGHT(1, 1),
        NONE(0, 0);

        public final int dx, dy;
        Direction(int dx, int dy) { this.dx = dx; this.dy = dy; }

        public static Direction fromInput(int dx, int dy) {
            if (dx == 0 && dy == -1) return UP;
            if (dx == 0 && dy == 1) return DOWN;
            if (dx == -1 && dy == 0) return LEFT;
            if (dx == 1 && dy == 0) return RIGHT;
            if (dx == -1 && dy == -1) return UP_LEFT;
            if (dx == 1 && dy == -1) return UP_RIGHT;
            if (dx == -1 && dy == 1) return DOWN_LEFT;
            if (dx == 1 && dy == 1) return DOWN_RIGHT;
            return NONE;
        }
    }

    // Movement input flags
    private boolean up, down, left, right;

    // Utilities
    private final VectorManipulation vectorManipulation = new VectorManipulation();

    // Player Manager
    private final PlayerManager playerManager;

    // Sprites & animation (pre-scaled and pre-flipped)
    private final BufferedImage[] idleSprites;
    private final BufferedImage[] idleSpritesFlipped;
    private final BufferedImage[] walkSprites;
    private final BufferedImage[] walkSpritesFlipped;
    private final BufferedImage[] runSprites;
    private final BufferedImage[] runSpritesFlipped;
    private int currentFrame = 0;
    private long lastFrameTime = 0;
    private static final int FRAME_DELAY_MS = 100;
    private boolean facingLeft = true;

    // Direction tracking
    private Direction lastDirection = Direction.LEFT;
    private boolean invulnerable = false;

    // Dash configuration
    private boolean isDashing = false;
    private final Cooldown dashDuration = new Cooldown(0.15);
    private final Cooldown dashCooldown = new Cooldown(0.95);
    private static final double DASH_SPEED_MULTIPLIER = 4.0;
    private static final double DASH_STAMINA_COST = 50.0;
    private static final double DIAGONAL_SPEED_BOOST = 1.05;

    // Direction getters for external systems (weapons, etc.)
    public Direction getLastDirection() { return lastDirection; }
    public boolean isLastFacingLeft() { return lastDirection == Direction.LEFT || lastDirection == Direction.UP_LEFT || lastDirection == Direction.DOWN_LEFT; }
    public boolean isLastFacingRight() { return lastDirection == Direction.RIGHT || lastDirection == Direction.UP_RIGHT || lastDirection == Direction.DOWN_RIGHT; }
    public boolean isLastFacingUp() { return lastDirection == Direction.UP || lastDirection == Direction.UP_LEFT || lastDirection == Direction.UP_RIGHT; }
    public boolean isLastFacingDown() { return lastDirection == Direction.DOWN || lastDirection == Direction.DOWN_LEFT || lastDirection == Direction.DOWN_RIGHT; }

    public void dash() {
        if (dashCooldown.ready() && playerManager.getCurrentStamina() >= DASH_STAMINA_COST) {
            this.isDashing = true;
            this.playerManager.getPlayerStats().exhaustStamina(DASH_STAMINA_COST);
        }
    }

    public boolean isDashing() { return this.isDashing; }


    private enum State { IDLE, WALK, RUN }
    private State currentState = State.IDLE;
    private State previousState = State.IDLE;

    // Constructor
    public Player(WorldContext gameWorld, int x, int y, int size, double xOffset, double yOffset) {
        super(gameWorld, x, y, size, xOffset, yOffset);
        this.playerManager = new PlayerManager();
        this.setHitBox(0.5, 0.5, 0.5);

        idleSprites = loadSprites("/sprites/chicken/cute_chicken_idle.png", 6, size);
        idleSpritesFlipped = flipSprites(idleSprites);
        walkSprites = loadSprites("/sprites/chicken/cute_chicken_walk.png", 6, size);
        walkSpritesFlipped = flipSprites(walkSprites);
        runSprites = loadSprites("/sprites/chicken/cute_chicken_run.png", 2, size);
        runSpritesFlipped = flipSprites(runSprites);
    }

    private BufferedImage[] loadSprites(String path, int frameCount, int targetSize) {
        BufferedImage sheet = SpriteLoader.load(path);
        BufferedImage[] sprites = new BufferedImage[frameCount];
        int frameWidth = sheet.getWidth() / frameCount;
        int frameHeight = sheet.getHeight();

        for (int i = 0; i < frameCount; i++) {
            BufferedImage raw = sheet.getSubimage(i * frameWidth, 0, frameWidth, frameHeight);
            // Pre-scale to target size
            sprites[i] = scaleImage(raw, targetSize, targetSize);
        }
        return sprites;
    }

    private BufferedImage scaleImage(BufferedImage src, int width, int height) {
        BufferedImage scaled = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        java.awt.Graphics2D g2d = scaled.createGraphics();
        g2d.setRenderingHint(java.awt.RenderingHints.KEY_INTERPOLATION,
                             java.awt.RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        g2d.drawImage(src, 0, 0, width, height, null);
        g2d.dispose();
        return scaled;
    }

    private BufferedImage[] flipSprites(BufferedImage[] sprites) {
        BufferedImage[] flipped = new BufferedImage[sprites.length];
        for (int i = 0; i < sprites.length; i++) {
            int w = sprites[i].getWidth();
            int h = sprites[i].getHeight();
            flipped[i] = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
            java.awt.Graphics2D g2d = flipped[i].createGraphics();
            g2d.drawImage(sprites[i], w, 0, -w, h, null);
            g2d.dispose();
        }
        return flipped;
    }

    private BufferedImage[] getCurrentSprites() {
        // Note: facingLeft means we need the flipped sprites (original code used negative width when facingLeft)
        return switch (currentState) {
            case WALK -> facingLeft ? walkSpritesFlipped : walkSprites;
            case RUN -> facingLeft ? runSpritesFlipped : runSprites;
            default -> facingLeft ? idleSpritesFlipped : idleSprites;
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

    public boolean isInvulnerable() { return this.invulnerable; }
    public PlayerStats getPlayerStats() {
        return this.playerManager.getPlayerStats();
    }
    public void takeDamage(double damage) {
        this.playerManager.takeDamage(damage);
    }


    @Override
    public void update(double dt) {
        // Calculate input direction
        int inputDx = (right ? 1 : 0) - (left ? 1 : 0);
        int inputDy = (down ? 1 : 0) - (up ? 1 : 0);

        // Update last facing direction (only when moving)
        Direction inputDirection = Direction.fromInput(inputDx, inputDy);
        if (inputDirection != Direction.NONE) {
            lastDirection = inputDirection;
        }

        // Normalize for diagonal movement
        Point2D.Double normalized = vectorManipulation.normalise(inputDx, inputDy);
        double dx = normalized.getX();
        double dy = normalized.getY();

        // Handle dash or normal movement
        if (isDashing) {
            updateDash(dt, dx, dy);
            return;
        }

        dashCooldown.update(dt);
        applyMovement(dt, dx, dy, playerManager.getSpeed());
        updateAnimationState();
        updateAnimationFrame();

        this.playerManager.getPlayerStats().update();
        updateHitBox();
    }

    private void updateDash(double dt, double dx, double dy) {
        dashDuration.update(dt);
        this.invulnerable = true;

        double dashSpeed = playerManager.getSpeed() * DASH_SPEED_MULTIPLIER;
        applyMovement(dt, dx, dy, dashSpeed);
        updateHitBox();

        if (dashDuration.ready()) {
            dashDuration.reset();
            this.isDashing = false;
            this.invulnerable = false;
        }
    }

    private void applyMovement(double dt, double dx, double dy, double speed) {
        boolean isDiagonal = dx != 0 && dy != 0;
        double effectiveSpeed = isDiagonal ? speed * DIAGONAL_SPEED_BOOST : speed;
        x += dx * effectiveSpeed * dt;
        y += dy * effectiveSpeed * dt;
    }

    private void updateAnimationState() {
        if (left) facingLeft = true;
        if (right) facingLeft = false;

        boolean isMoving = up || down || left || right;
        boolean isRunning = left || right;

        currentState = isMoving ? (isRunning ? State.RUN : State.WALK) : State.IDLE;
        if (currentState != previousState) {
            currentFrame = 0;
            previousState = currentState;
        }
    }

    private void updateAnimationFrame() {
        long nowMillis = System.currentTimeMillis();
        if (nowMillis - lastFrameTime > FRAME_DELAY_MS) {
            currentFrame = (currentFrame + 1) % getCurrentSprites().length;
            lastFrameTime = nowMillis;
        }
    }


    // Render
    @Override
    public void render(Graphics g, Camera camera) {
        // Sprites are pre-scaled and pre-flipped - no runtime transformation needed
        BufferedImage sprite = getCurrentSprites()[currentFrame];
        int drawX = (int) (x - camera.getX());
        int drawY = (int) (y - camera.getY());
        g.drawImage(sprite, drawX, drawY, null);

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
