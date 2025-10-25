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

    private List damagedEnemies;

    // statics
    private static BufferedImage SPRITE_SHEET;
    private static final int FRAME_COUNT = 10;

    public LightningBolt(GameWorld gameWorld) {
        super(gameWorld);
        this.target = gameWorld.getEnemySpawner().getRandomEnemy();
        this.damage = 80 + this.owner.getPlayerManager().getMagicDamage();
//        this.damage = 0;
        this.damagedEnemies = new ArrayList<Entity>();

        if (SPRITE_SHEET == null) {
            SPRITE_SHEET = SpriteLoader.load("/sprites/weapons/lightningbolt/lightningbolt.png");
        }


        this.animation = new Animation(SPRITE_SHEET, FRAME_COUNT, 90, false);

        this.frameWidth = SPRITE_SHEET.getWidth() / FRAME_COUNT;
        this.height = SPRITE_SHEET.getHeight();


        this.x = target.getX();
        this.y = target.getY() + target.getSize() - height;
        this.hitBox = new Rectangle((int) x, (int) (y + (height * 0.9)), frameWidth, (int) (height * 0.1));
    }

    @Override
    public void update(double dt) {
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

        g.drawImage(animation.getCurrentFrame(), screenX, screenY, frameWidth, height, null);

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
