package projectiles.egg;

import entities.enemies.Enemy;
import projectiles.Projectile;
import utils.Camera;
import utils.GameWorld;
import utils.SpriteLoader;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Random;

public class Egg extends Projectile {
    boolean loaded = false;
    protected int spriteSize = 32;
    private static final BufferedImage[] eggSprites = new BufferedImage[30];
    protected BufferedImage sprite;
    private static final Random rand = new Random();

    public Egg(GameWorld gameWorld) {
        super(gameWorld);
        this.speed = 400;
        this.damage = 50.0;
        this.x = owner.getX();
        this.y = owner.getY();
        boolean up = owner.isLastFacingUp();
        boolean down = owner.isLastFacingDown();
        boolean left = owner.isLastFacingLeft();
        boolean right = owner.isLastFacingRight();
        this.lastUpdateTime = System.nanoTime();


        if (up) dy -= 1;
        if (down) dy += 1;
        if (left) dx -= 1;
        if (right) dx += 1;
        loadSprite();
        hitBox = new Rectangle((int) x, (int) y, 32, 32);
    }

    @Override
    public void update() {
        double deltaTime = this.deltaTimer.getDelta();
        double diagonalBoost = 1.05;
        if (dx != 0 && dy != 0) {
            double diagonal = Math.sqrt(2);
            x += ( dx / diagonal) * this.speed * diagonalBoost * deltaTime;
            y += ( dy / diagonal) * this.speed * diagonalBoost * deltaTime;
        } else {
            x += dx * this.speed * deltaTime;
            y += dy * this.speed * deltaTime;
        }
        this.checkEnemyCollision();

        updateHitBox();
    }

    protected void loadSprite() {
        if (!loaded) {
            BufferedImage sheet = SpriteLoader.load("/sprites/eggBulletSpriteSheet.png");
            int i = 0;
            for (int t = 0; t < 5; t++) {
                for (int k = 0; k < 6; k++) {
                    assert sheet != null;
                    eggSprites[i++] = sheet.getSubimage(t * spriteSize, k * spriteSize, spriteSize, spriteSize);
                }
            }
            loaded = true;
        }
        this.sprite = eggSprites[rand.nextInt(30)];
    }

    @Override
    public void render(Graphics g, Camera camera) {
        if (sprite != null) {
            g.drawImage(sprite, (int) (x - camera.getX()), (int) (y - camera.getY()), spriteSize, spriteSize, null);
        }
    }

    @Override
    protected void onHitEffect() {

    }
}
