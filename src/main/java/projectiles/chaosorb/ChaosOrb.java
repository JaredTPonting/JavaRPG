package projectiles.chaosorb;

import entities.enemies.Enemy;
import lingeringzones.chaoszone.ChaosZone;
import projectiles.Projectile;
import utils.Camera;
import core.GameWorld;

import java.util.Random;
import java.awt.*;

public class ChaosOrb extends Projectile {

    private double radius = 10;
    private long lifeTimeMillis = 1500; // projectile lifetime
    private long spawnTime;

    public ChaosOrb(GameWorld gameWorld) {
        super(gameWorld);
        // random direction unit vector
        Random random = new Random();
        double angle = random.nextDouble() * 2 * Math.PI;
        this.dx = Math.cos(angle);
        this.dy = Math.sin(angle);
        this.x = this.owner.getX();
        this.y = this.owner.getY();
        this.speed = 200;
        this.damage = 50 + owner.getPlayerStats().getMagicDamage();
        this.spawnTime = System.currentTimeMillis();
        this.hitBox = new Rectangle((int) x, (int) y, 12, 12);
        this.lastUpdateTime = System.nanoTime();
    }

    @Override
    public void update(double dt) {

        x += dx * speed * dt;
        y += dy * speed * dt;
        updateHitBox();
        this.checkEnemyCollision();
    }

    @Override
    public void render(Graphics g, Camera camera) {
        // render projectile
        if (alive) {
            g.setColor(Color.MAGENTA);
            g.fillOval((int) (x - camera.getX()), (int) (y - camera.getY()), (int) radius * 2, (int) radius * 2);
        }
    }

    @Override
    protected void onHitEffect(Enemy e) {

    }

    @Override
    protected void onDeletionEffect() {
        this.gameWorld.getLingeringZoneManager().addLingeringZone(new ChaosZone(gameWorld, this.x, this.y, 30, 2));
    }


}
