package lingeringzones;

import entities.enemies.Enemy;
import utils.Camera;
import utils.Cooldown;
import core.GameWorld;

import java.awt.*;

public abstract class LingeringZone {
    protected double x, y;
    protected double width, height;  // ellipse dimensions
    private double lifeSpan;
    protected boolean alive = true;

    public final GameWorld gameWorld;
    private final Cooldown tick;

    public LingeringZone(GameWorld gameWorld, double x, double y, double radius, double lifeSpan) {
        this(gameWorld, x, y, radius, radius * 0.7, lifeSpan); // default ellipse (wider than tall)
    }

    public LingeringZone(GameWorld gameWorld, double x, double y, double width, double height, double lifeSpan) {
        this.gameWorld = gameWorld;
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.lifeSpan = lifeSpan;
        this.tick = new Cooldown(0.5);
    }

    public void update(double dt) {
        lifeSpan -= dt;
        tick.update(dt);
        if (lifeSpan <= 0) {
            destroy();
            return;
        }

        updateEnemies();
    }

    public abstract void affectEnemy(Enemy e);
    public abstract void tickAffectEnemy(Enemy e);

    private void updateEnemies() {
        for (Enemy e : gameWorld.getEnemySpawner().getEnemies()) {
            if (gameWorld.getCollisionChecker().entityStandingInZone(e, this)) {
                if (tick.ready()) {
                    tickAffectEnemy(e);
                    tick.reset();
                }
                affectEnemy(e);
            }
        }
    }

    public abstract void render(Graphics g, Camera camera);

    // --- Getters ---
    public double getX() { return x; }
    public double getY() { return y; }
    public double getWidth() { return width; }
    public double getHeight() { return height; }
    public boolean isAlive() { return alive; }

    public void destroy() { alive = false; }

    // --- Ellipse math for collisions ---
    public boolean pointInside(double px, double py) {
        // normalize point relative to ellipse center
        double dx = (px - x) / (width / 2.0);
        double dy = (py - y) / (height / 2.0);
        // inside ellipse if (x/a)^2 + (y/b)^2 <= 1
        return dx * dx + dy * dy <= 1.0;
    }
}
