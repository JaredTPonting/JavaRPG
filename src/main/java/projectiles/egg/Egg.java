package projectiles.egg;

import entities.enemies.EnemySpawner;
import entities.player.Player;
import projectiles.Projectile;
import utils.Camera;
import utils.GameWorld;
import utils.SpriteLoader;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Random;

public class Egg extends Projectile {
    Player player;
    boolean loaded = false;
    protected int spriteSize = 32;
    private static final BufferedImage[] eggSprites = new BufferedImage[30];
    private boolean up, down, left, right;
    protected BufferedImage sprite;
    private static final Random rand = new Random();

    public Egg(GameWorld gameWorld) {
        super(gameWorld);
        this.player = gameWorld.getPlayer();
        this.speed = 400;
        this.damage = 50.0;
        this.x = player.getX();
        this.y = player.getY();
        this.up = player.isLastFacingUp();
        this.down = player.isLastFacingDown();
        this.left = player.isLastFacingLeft();
        this.right = player.isLastFacingRight();
        this.lastUpdateTime = System.nanoTime();

        if (up) dy -= 1;
        if (down) dy += 1;
        if (left) dx -= 1;
        if (right) dx += 1;
        loadSprite();
    }

    @Override
    public void update() {
        long now = System.nanoTime();
        double deltaTime = (now - lastUpdateTime) / 1_000_000_000.0;
        lastUpdateTime = now;
        double diagonalBoost = 1.05;
        System.out.println(dx + "  " + dy);
        if (dx != 0 && dy != 0) {
            double diagonal = Math.sqrt(2);
            x += ((double) dx / diagonal) * this.speed * diagonalBoost * deltaTime;
            y += ((double) dy / diagonal) * this.speed * diagonalBoost * deltaTime;
        } else {
            x += dx * this.speed * deltaTime;
            y += dy * this.speed * deltaTime;
        }
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
