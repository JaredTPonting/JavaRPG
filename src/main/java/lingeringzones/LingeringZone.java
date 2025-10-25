package lingeringzones;

import entities.enemies.Enemy;
import utils.Camera;
import utils.Cooldown;
import core.GameWorld;

import java.awt.*;

// Nested class for lingering damage zone
public abstract class LingeringZone {
    private double lifeSpan;
    public final GameWorld gameWorld;
    private final Cooldown tick;
    private double x, y, radius;
    private boolean alive = true;


    public LingeringZone(GameWorld gameWorld, double x, double y, double radius, long lifeSpan) {
        this.gameWorld = gameWorld;
        this.x = x;
        this.y = y;
        this.radius = radius;
        this.lifeSpan = lifeSpan;
        this.tick = new Cooldown(0.5);
    }

    public void update(double dt) {
        lifeSpan -= dt;
        this.tick.update(dt);
        if (lifeSpan<=0) {
            this.destroy();
            return;
        }

        updateEnemies();
    }

    public abstract void affectEnemy(Enemy e);

    public void updateEnemies() {
        if (tick.ready()){
            for (Enemy e : gameWorld.getEnemySpawner().getEnemies()) {
                if (gameWorld.getCollisionChecker().entityStandingInZone(e, this)) {
                    this.affectEnemy(e);
                }
            }
            tick.reset();
        }
    }

    public void render(Graphics g, Camera camera) {
        if (!alive) return;
        g.setColor(new Color(255, 0, 255, 80));
        g.fillOval((int) (x - radius - camera.getX()), (int) (y - radius - camera.getY()),
                (int) (radius * 2), (int) (radius * 2));
    }

    public double getX() {
        return this.x;
    }

    public double getY() {
        return this.y;
    }

    public double getRadius() {
        return this.radius;
    }

    public boolean isAlive() {
        return alive;
    }

    public void destroy() {
        this.alive = false;
    }
}