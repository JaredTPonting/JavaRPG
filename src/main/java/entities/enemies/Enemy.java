package entities.enemies;

import entities.Entity;
import loot.BasicChest;
import utils.*;

import java.awt.*;
import java.awt.image.BufferedImage;
import entities.player.Player;

import java.util.List;
import java.util.Map;

public class Enemy extends Entity {
    protected double vx = 0, vy = 0;

    protected double hp;
    protected double speed;
    protected boolean dead = false;
    protected boolean despawn = false;
    public boolean triggeredDeath = false;
    protected int XP;
    protected double damage;
    protected Cooldown attackCooldown;
    public double speedDebuff;
    private boolean fleeing = false;

    public boolean isBoss = false;

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
        List<Enemy> allEnemies = gameWorld.getEnemySpawner().getEnemies();
        this.attackCooldown.update(dt);
        double dx = target.getX() - x;
        double dy = target.getY() - y;
        double distance = Math.hypot(dx, dy);

        if (triggeredDeath) {
            animations.get(state).update();
            if (animations.get(state).isFinished()){
                die();
            }
            vx = 0;
            vy = 0;
            updateHitBox();
            return;
        } else if (fleeing) {
            setState("run");
            // Vector **away** from player
            double fdx = x - target.getX();
            double fdy = y - target.getY();
            double dist = Math.hypot(fdx, fdy);
            if (dist > 0) {
                // Accelerate away (slightly weaker than chasing)
                double fleeAccel = 700; // tweakable
                vx += (fdx / dist) * fleeAccel * dt;
                vy += (fdy / dist) * fleeAccel * dt;
            }
            if (dist > gameWorld.getGameWidth() * 0.6) {
                this.despawn();
                return;
            }
            double fleeFriction = 0.92;
            vx *= Math.pow(fleeFriction, dt * 60);
            vy *= Math.pow(fleeFriction, dt * 60);
            double fleeSpeed = speed * 1.4; // runaway panic speed
            double mag = Math.hypot(vx, vy);
            if (mag > fleeSpeed) {
                vx = (vx / mag) * fleeSpeed;
                vy = (vy / mag) * fleeSpeed;
            }

            x += vx * dt;
            y += vy * dt;
            animations.get(state).update();
            facingLeft = !(vx > 0);
            updateHitBox();
            return;
        }
        else if (gameWorld.getCollisionChecker().checkCollision(this, target) && !target.getInvunerable()) {
            setState("attack");
            vx=0;
            vy=0;
            animations.get(state).update();   // update attack animation
            target.takeDamage(this.attackPlayer());

            return;
        } else {
            setState("run");
        }
        animations.get(state).update();

        facingLeft = !(dx > 0);

        // Movement Forces
        double ax = 0;
        double ay = 0;

        // Attraction toward entities.player
        if (distance > 0) {
            ax += (dx / distance) * 700;  // acceleration toward entities.player
            ay += (dy / distance) * 700;
        }

        // Separation from nearby entities.enemies
        double separationRadius = size * 0.8;
        for (Enemy e : allEnemies) {
            if (e == this || e.isDead() || e.triggeredDeath) continue;
            double ex = e.getX();
            double ey = e.getY();
            double dist = Math.hypot(x - ex, y - ey);
            if (dist < separationRadius && dist > 0) {
                ax += (x - ex) / dist * 700;
                ay += (y - ey) / dist * 700;
            }
        }

        //Small random jitter to keep swarm organic
        ax += (Math.random() - 0.5) * 100;
        ay += (Math.random() - 0.5) * 100;

        // Apply acceleration to velocity
        // Scale by deltaTime to make acceleration time-based
        vx += ax * dt;
        vy += ay * dt;

        // --- Apply friction to stop runaway drift ---
        double friction = 0.9;
        vx *= Math.pow(friction, dt * 60);  // stable across FPS
        vy *= Math.pow(friction, dt * 60);

        // --- Clamp speed ---
        double speedMag = Math.hypot(vx, vy);
        if (speedMag > speed) {
            vx = (vx / speedMag) * speed;
            vy = (vy / speedMag) * speed;
        }

        // --- Apply movement ---
        if (speedDebuff != 1){
            System.out.println(speedDebuff);
        }
        x += (vx * dt) * speedDebuff;
        y += (vy * dt) * speedDebuff;

        updateHitBox();
        resetSpeedDebuff();
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
