package projectiles.lightingbolt;

import core.GameWorld;
import entities.Entity;
import entities.enemies.Enemy;
import projectiles.Projectile;
import utils.Animation;
import utils.Camera;
import utils.SpriteLoader;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

public class LightningBolt extends Projectile {
    private final int frameWidth;
    private final int height;
    private Entity target;
    private int size = 150;

    private List damagedEnemies;

    // statics
    private static BufferedImage SPRITE_SHEET;
    private static final int FRAME_COUNT = 10;

    public LightningBolt(GameWorld gameWorld, Entity target) {
        super(gameWorld);
        this.target = target;
        this.damage = 80 + this.owner.getPlayerManager().getMagicDamage();

        if (SPRITE_SHEET == null) {
            SPRITE_SHEET = SpriteLoader.load("/sprites/weapons/lightningbolt/lightningbolt.png");
        }


        this.animation = new Animation(SPRITE_SHEET, FRAME_COUNT, 90, false);

        this.frameWidth = SPRITE_SHEET.getWidth() / FRAME_COUNT;
        this.height = SPRITE_SHEET.getHeight();


        this.x = target.getX() - ((double) target.getSize() / 2) + ((double) this.size / 2);
        this.y = target.getY() + target.getSize() - this.size;
        this.hitBox = new Rectangle((int) x, (int) (y + (size * 0.8)), size, (int) (size * 0.2));
    }

    @Override
    public void onUpdate(double dt) {
        if (target == null) {
            destroyProjectile();
            return;
        }
        animation.update();

        checkEnemyCollision();

        if (animation.isFinished()) {
            destroyProjectile();
        }

    }

    @Override
    public void render(Graphics g, Camera camera) {
        int screenX = (int) (x - camera.getX());
        int screenY = (int) (y - camera.getY());
        drawHitbox(g, camera);
        g.drawImage(animation.getCurrentFrame(), screenX, screenY, size, size, null);

    }

    @Override
    public void reduceMaxHits(){

    }

    @Override
    protected void onHitEffect(Enemy e) {

    }

    @Override
    protected void onDeletionEffect() {

    }

    @Override
    public void checkEnemyCollision() {
        for (Enemy e : gameWorld.getEnemySpawner().getEnemies()) {
            if (!enemiesHit.contains(e) & !e.triggeredDeath) {
                if (gameWorld.getCollisionChecker().entityBaseCollision(e, this)) {
                    e.takeDamage(this.getDamage());
                    this.onHitEffect(e);
                    enemiesHit.add(e);
                    this.reduceMaxHits();
                }
            }
        }
    }
}
