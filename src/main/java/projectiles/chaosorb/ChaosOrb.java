package projectiles.chaosorb;

import entities.enemies.Enemy;
import entities.player.PlayerManager;
import lingeringzones.chaoszone.ChaosZone;
import projectiles.Projectile;
import utils.Animation;
import utils.Camera;
import core.GameWorld;
import utils.SpriteLoader;

import java.awt.image.BufferedImage;
import java.util.Random;
import java.awt.*;

public class ChaosOrb extends Projectile {

    // static Animation
    private static BufferedImage SPRITE_SHEET;
    private static final int FRAME_COUNT = 8;

    public ChaosOrb(GameWorld gameWorld) {
        super(gameWorld);
        // random direction unit vector
        Random random = new Random();
        PlayerManager playerManager = owner.getPlayerManager();
        double angle = random.nextDouble() * 2 * Math.PI;
        this.dx = Math.cos(angle);
        this.dy = Math.sin(angle);
        this.x = this.owner.getX();
        this.y = this.owner.getY();
        this.speed = 200;
//        this.damage = 50 + playerManager.getMagicDamage();
        this.damage = 0;
        this.size = 40;
        this.hitBox = new Rectangle((int) x, (int) y, size, size);

        if (SPRITE_SHEET == null) {
            SPRITE_SHEET = SpriteLoader.load("/sprites/weapons/chaosorb/chaosorb.png");
        }

        this.animation = new Animation(SPRITE_SHEET, FRAME_COUNT, FRAME_TIME, LOOP);

    }

    @Override
    public void onUpdate(double dt) {

        x += dx * speed * dt;
        y += dy * speed * dt;
        updateHitBox();
        this.checkEnemyCollision();
        animation.update();
    }

    @Override
    public void render(Graphics g, Camera camera) {
        // render projectile
        drawHitbox(g, camera);
        if (alive) {
            g.drawImage(animation.getCurrentFrame(), (int) (x - camera.getX()), (int) (y - camera.getY()), this.size, this.size, null);
        }
    }

    @Override
    protected void onHitEffect(Enemy e) {

    }

    @Override
    protected void onDeletionEffect() {
        this.gameWorld.getLingeringZoneManager().addLingeringZone(new ChaosZone(gameWorld, this.x, this.y, 80, 3.5));
    }


}
