package entities.enemies;

import entities.Entity;
import utils.DeltaTimer;
import utils.GameWorld;
import utils.Cooldown;

import java.awt.*;
import java.awt.image.BufferedImage;
import entities.player.Player;
import utils.Camera;
import java.util.List;

public class Enemy extends Entity {
    protected double vx = 0, vy = 0;
    protected int size = 32;
    protected double hp;
    protected double speed;
    protected boolean dead = false;
    protected BufferedImage sprite;
    protected int XP;
    protected double damage;
    protected Cooldown attackCooldown;
    public DeltaTimer deltaTimer;
    public double speedDebuff;

    protected Player target;

    private long lastUpdateTime = System.nanoTime();

    public Enemy(GameWorld gameWorld, int x, int y, int width, int height, double attackSpeed) {
        super(gameWorld, x, y, width, height);
        this.target = gameWorld.getPlayer();
        long baseDuration = 1000;
        long adjustedDuration = (long) (baseDuration / attackSpeed);
        this.attackCooldown = new Cooldown(adjustedDuration);
        this.deltaTimer = new DeltaTimer();
        this.speedDebuff = 1;
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

    @Override
    public void update() {
        List<Enemy> allEnemies = gameWorld.getEnemySpawner().getEnemies();
        double deltaTime = this.deltaTimer.getDelta();

        if (dead) return;

        double targetX = target.getX();
        double targetY = target.getY();

        // Direction to entities.player
        double dx = targetX - x;
        double dy = targetY - y;
        double distance = Math.hypot(dx, dy);

        // Movement Forces
        double ax = 0;
        double ay = 0;

        // Attraction toward entities.player
        if (distance > 0) {
            ax += (dx / distance) * 700;  // acceleration toward entities.player
            ay += (dy / distance) * 700;
        }

        // Separation from nearby entities.enemies
        double separationRadius = size * 1.5;
        for (Enemy e : allEnemies) {
            if (e == this || e.isDead()) continue;
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
        resetSpeedDebufF();
    }

    public void resetSpeedDebufF() {
        this.speedDebuff = 1;
    }

    public void setSpeedDebuff(Double debuff) {
        this.speedDebuff = debuff;
    }


    public void takeDamage(double amount) {
        if (!dead) {
            hp -= amount;
            if (hp <= 0) {
                die();
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

    protected void die() {
        dead = true;
    }

    public boolean isDead() {
        return dead;
    }

    public Rectangle getBounds() {
        return new Rectangle((int) x, (int) y, size, size);
    }

    @Override
    public void render(Graphics g, Camera camera) {
        if (!dead) {
            g.drawImage(sprite, (int) (x - camera.getX()), (int) (y - camera.getY()), this.width, this.height, null);
        }
    }
}
