package entities.enemies;

import core.GameWorld;
import entities.Entity;
import utils.*;

import java.awt.*;
import java.awt.image.BufferedImage;
import entities.player.Player;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Enemy extends Entity {
    protected double vx = 0, vy = 0;

    protected double hp;
    protected double speed;
    protected boolean dead = false;
    public boolean triggeredDeath = false;
    protected BufferedImage sprite;
    protected int XP;
    protected double damage;
    protected Cooldown attackCooldown;
    public DeltaTimer deltaTimer;
    public double speedDebuff;
    public Cooldown deathTimer;

    protected Player target;

    private long lastUpdateTime = System.nanoTime();

    // Animations
    private Map<String, Animation> animations = new HashMap<>();
    private String state = "run";

    public Enemy(GameWorld gameWorld, int x, int y, double attackSpeed, int size, Map<String, Animation> animations, double xOffset, double yOffset) {
        super(gameWorld, x, y, size, xOffset, yOffset);
        this.target = gameWorld.getPlayer();
        long baseDuration = 1000;
        long adjustedDuration = (long) (baseDuration / attackSpeed);
        this.attackCooldown = new Cooldown(adjustedDuration);
        this.deltaTimer = new DeltaTimer();
        this.speedDebuff = 1;
        this.size = size;
        this.animations = animations;
    }

    public void setSpeed(int speed) {
        this.speed = speed;
    }

    public double getX() {
        return this.x;
    }

    public void setX(double newX) {
        this.x = newX;
    }

    public double getY() {
        return this.y;
    }

    public void setY(double newY) {
        this.y = newY;
    }

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

    @Override
    public void update() {
        List<Enemy> allEnemies = gameWorld.getEnemySpawner().getEnemies();
        double deltaTime = this.deltaTimer.getDelta();
        double dx = target.getX() - x;
        double dy = target.getY() - y;
        double distance = Math.hypot(dx, dy);

        if (triggeredDeath) {
            animations.get(state).update();
            if (deathTimer.ready()){
                die();
            }
            vx = 0;
            vy = 0;
            updateHitBox();
            return;
        } else if (gameWorld.getCollisionChecker().checkCollision(this, target)) {
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

        if (dx>0) {
            facingLeft = false;
        } else {
            facingLeft = true;
        }

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
        vx += ax * deltaTime;
        vy += ay * deltaTime;

        // --- Apply friction to stop runaway drift ---
        double friction = 0.9;
        vx *= Math.pow(friction, deltaTime * 60);  // stable across FPS
        vy *= Math.pow(friction, deltaTime * 60);

        // --- Clamp speed ---
        double speedMag = Math.hypot(vx, vy);
        if (speedMag > speed) {
            vx = (vx / speedMag) * speed;
            vy = (vy / speedMag) * speed;
        }

        // --- Apply movement ---
        x += (vx * deltaTime) * speedDebuff;
        y += (vy * deltaTime) * speedDebuff;

        updateHitBox();
        resetSpeeddebuff();
    }

    public void resetSpeeddebuff() {
        this.speedDebuff = 1;
    }

    public double getPlayerDistance() {
        // Direction to entities.player
        double dx = target.getX() - x;
        double dy = target.getY() - y;
        return Math.hypot(dx, dy);
    }

    public void setSpeedDebuff(Double debuff) {
        this.speedDebuff = debuff;
    }


    public void takeDamage(double amount) {
        if (!triggeredDeath) {
            hp -= amount;
            this.addDamageIndicator((int) amount, (int) this.x, (int) this.y);
            if (hp <= 0) {
                startDeath();
                setState("die");
            }
        } else if (deathTimer.ready()) {
            die();
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
        this.deathTimer = new Cooldown(5000);
        triggeredDeath = true;
    }

    protected void die() {
        dead = true;
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
    }
}
