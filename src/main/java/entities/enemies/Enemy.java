package entities.enemies;

import entities.Entity;
import loot.BasicChest;
import utils.*;
import utils.CollisionGrid;

import java.awt.*;
import java.awt.image.BufferedImage;
import entities.player.Player;

import java.util.List;
import java.util.Map;

public class Enemy extends Entity {
    // Physics constants
    private static final double ACCELERATION_FORCE = 700.0;
    private static final double SEPARATION_FORCE = 700.0;
    private static final double JITTER_FORCE = 100.0;
    private static final double FRICTION = 0.9;
    private static final double FLEE_FRICTION = 0.92;
    private static final double FLEE_SPEED_MULTIPLIER = 1.4;
    private static final double SEPARATION_RADIUS_MULTIPLIER = 0.8;
    private static final double DESPAWN_DISTANCE_MULTIPLIER = 0.6;

    // Velocity
    protected double vx = 0, vy = 0;

    // Stats
    protected double hp;
    protected double speed;
    protected int XP;
    protected double damage;
    protected Cooldown attackCooldown;
    private double speedDebuff = 1.0;

    // State flags
    protected boolean dead = false;
    protected boolean despawn = false;
    private boolean triggeredDeath = false;
    private boolean fleeing = false;
    private boolean isBoss = false;

    // Grid cell tracking for incremental collision updates
    private Point currentGridCell = null;

    protected Player target;

    // Animations
    private final Map<String, Animation> animations;
    private String state = "run";

    public Enemy(WorldContext gameWorld, int x, int y, double attackSpeed, int size, Map<String, Animation> animations, double xOffset, double yOffset) {
        super(gameWorld, x, y, size, xOffset, yOffset);
        this.target = gameWorld.getPlayer();
        long baseDuration = 1;
        long adjustedDuration = (long) (baseDuration / attackSpeed);
        this.attackCooldown = new Cooldown(adjustedDuration);
        this.speedDebuff = 1;
        this.size = size;
        this.animations = animations;
    }

    public void setSpeed(int speed) {
        this.speed = speed;
    }

    public void setBoss(boolean boss) {
        this.isBoss = boss;
    }

    public boolean isBoss() {
        return this.isBoss;
    }

    public boolean isTriggeredDeath() {
        return this.triggeredDeath;
    }

    public void setX(double newX) {
        this.x = newX;
    }

    public void setY(double newY) {
        this.y = newY;
    }

    public void flee() { this.fleeing = true; }

    public int getXP() {
        return this.XP;
    }

    public double getVx() {
        return this.vx;
    }

    public void resetVxVy() {
        this.vx = 0;
        this.vy = 0;
    }

    public String getState() { return this.state; }
    public void setState(String newState) {
        if (animations.containsKey(newState)) {
            this.state = newState;
        }
    }

    private void dropChest() {
        this.gameWorld.getLootManager().addChest(new BasicChest(this.x + ((double) this.size / 2), this.y + ((double) this.size / 2), gameWorld));
    }

    @Override
    public void update(double dt) {
        attackCooldown.update(dt);

        if (triggeredDeath) {
            updateDeathAnimation();
            return;
        }

        if (fleeing) {
            updateFleeing(dt);
            return;
        }

        if (gameWorld.getCollisionChecker().checkCollision(this, target) && !target.isInvulnerable()) {
            updateAttacking();
            return;
        }

        updateChasing(dt);
    }

    private void updateDeathAnimation() {
        animations.get(state).update();
        if (animations.get(state).isFinished()) {
            die();
        }
        vx = 0;
        vy = 0;
        updateHitBox();
    }

    private void updateFleeing(double dt) {
        setState("run");

        double fdx = x - target.getX();
        double fdy = y - target.getY();
        double dist = Math.hypot(fdx, fdy);

        if (dist > gameWorld.getGameWidth() * DESPAWN_DISTANCE_MULTIPLIER) {
            despawn();
            return;
        }

        if (dist > 0) {
            vx += (fdx / dist) * ACCELERATION_FORCE * dt;
            vy += (fdy / dist) * ACCELERATION_FORCE * dt;
        }

        applyFriction(dt, FLEE_FRICTION);
        clampSpeed(speed * FLEE_SPEED_MULTIPLIER);
        applyMovement(dt);

        animations.get(state).update();
        facingLeft = vx <= 0;
        finalizePosition();
    }

    private void updateAttacking() {
        setState("attack");
        vx = 0;
        vy = 0;
        animations.get(state).update();
        target.takeDamage(attackPlayer());
    }

    private void updateChasing(double dt) {
        setState("run");

        double dx = target.getX() - x;
        double dy = target.getY() - y;
        double distance = Math.hypot(dx, dy);

        facingLeft = dx <= 0;

        // Calculate flocking forces
        double ax = 0, ay = 0;

        // Attraction toward player
        if (distance > 0) {
            ax += (dx / distance) * ACCELERATION_FORCE;
            ay += (dy / distance) * ACCELERATION_FORCE;
        }

        // Separation from nearby enemies
        double[] separation = calculateSeparation();
        ax += separation[0];
        ay += separation[1];

        // Random jitter for organic movement
        ax += (Math.random() - 0.5) * JITTER_FORCE;
        ay += (Math.random() - 0.5) * JITTER_FORCE;

        // Apply physics
        vx += ax * dt;
        vy += ay * dt;
        applyFriction(dt, FRICTION);
        clampSpeed(speed);
        applyMovement(dt);

        animations.get(state).update();
        finalizePosition();
        resetSpeedDebuff();
    }

    private double[] calculateSeparation() {
        double ax = 0, ay = 0;
        double separationRadius = size * SEPARATION_RADIUS_MULTIPLIER;

        for (Enemy e : gameWorld.getEnemySpawner().getEnemies()) {
            if (e == this || e.isDead() || e.isTriggeredDeath()) continue;

            double dx = x - e.getX();
            double dy = y - e.getY();
            double dist = Math.hypot(dx, dy);

            if (dist < separationRadius && dist > 0) {
                ax += (dx / dist) * SEPARATION_FORCE;
                ay += (dy / dist) * SEPARATION_FORCE;
            }
        }
        return new double[]{ax, ay};
    }

    private void applyFriction(double dt, double friction) {
        // Proper frame-rate independent friction using exponential decay
        double decay = Math.exp(-((1 - friction) * 10) * dt);
        vx *= decay;
        vy *= decay;
    }

    private void clampSpeed(double maxSpeed) {
        double mag = Math.hypot(vx, vy);
        if (mag > maxSpeed) {
            vx = (vx / mag) * maxSpeed;
            vy = (vy / mag) * maxSpeed;
        }
    }

    private void applyMovement(double dt) {
        x += vx * dt * speedDebuff;
        y += vy * dt * speedDebuff;
    }

    private void finalizePosition() {
        updateHitBox();
        updateGridPosition(gameWorld.getCollisionGrid());
    }

    public void resetSpeedDebuff() {
        this.speedDebuff = 1;
    }

    public void setSpeedDebuff(Double debuff) {
        this.speedDebuff = debuff;
    }


    public void takeDamage(double amount) {
        if (!triggeredDeath) {
            hp -= amount;
            this.addDamageIndicator((int) amount, (int) (this.x + ((double) this.size / 2)), (int) (this.y + ((double) this.size / 2)));
            if (hp <= 0) {
                startDeath();
                setState("die");
            }
        }
    }

    public double attackPlayer() {
        if (attackCooldown.ready()) {
            attackCooldown.reset();
            return damage;
        }

        return 0;
    }

    public void startDeath() {
        triggeredDeath = true;
        if (this.isBoss) {
            dropChest();
        }
    }

    protected void die() {
        dead = true;
    }

    protected void despawn() {
        despawn = true;
    }

    public boolean despawned() {
        return this.despawn;
    }

    public boolean isDead() {
        return dead;
    }

    public Point getCurrentGridCell() {
        return currentGridCell;
    }

    public void updateGridPosition(CollisionGrid grid) {
        Rectangle hitbox = getHitBox();
        Point newCell = grid.getCellFor(hitbox.getCenterX(), hitbox.getCenterY());
        if (currentGridCell == null) {
            grid.add(this);
            currentGridCell = newCell;
        } else if (!newCell.equals(currentGridCell)) {
            grid.remove(this, currentGridCell);
            grid.add(this);
            currentGridCell = newCell;
        }
    }

    public Rectangle getBounds() {
        return new Rectangle((int) x, (int) y, size, size);
    }

    public void addDamageIndicator(int damage, int x, int y) {
        this.gameWorld.getEnemySpawner().getDamageIndicators().addIndicator(damage, x, y);
    }


    @Override
    public void render(Graphics g, Camera camera) {
        BufferedImage frame = animations.get(state).getCurrentFrame();
        if (facingLeft) {
            g.drawImage(frame, (int)(x - camera.getX()), (int)(y - camera.getY()), size, size, null);
        } else {
            g.drawImage(frame, (int)(x - camera.getX() + size), (int)(y - camera.getY()), -size, size, null);
        }

        if (gameWorld.isDebugMode()) {
            drawHitBox(g, camera);
        }
    }

    @Override
    public double getRenderY() {
        return this.y + size;
    }

    public double getY() {
        return this.y;
    }
}
