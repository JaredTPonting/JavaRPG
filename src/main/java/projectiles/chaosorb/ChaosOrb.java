package projectiles.chaosorb;

import projectiles.Projectile;
import utils.Camera;
import utils.GameWorld;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class ChaosOrb extends Projectile {

    private double radius = 10;
    private long lifeTimeMillis = 1500; // projectile lifetime
    private long spawnTime;

    public ChaosOrb(GameWorld gameWorld, double x, double y, double dx, double dy) {
        super(gameWorld);
        this.x = x;
        this.y = y;
        this.dx = dx;
        this.dy = dy;
        this.speed = 200;
        this.damage = 50;
        this.spawnTime = System.currentTimeMillis();
        this.hitBox = new Rectangle((int) x, (int) y, 12, 12);
        this.lastUpdateTime = System.nanoTime();
    }

    @Override
    public void update() {
        double deltaTime = this.deltaTimer.getDelta();


        x += dx * speed * deltaTime;
        y += dy * speed * deltaTime;
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
    protected void onHitEffect() {
        // could trigger explosion or immediate AoE
    }

}
