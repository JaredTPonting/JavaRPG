package projectiles.egg;

import entities.enemies.Enemy;
import projectiles.Projectile;
import utils.Camera;
import core.GameWorld;
import utils.SpriteLoader;
import utils.VectorManipulation;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.util.Random;

public class Egg extends Projectile {
    boolean loaded = false;
    protected int spriteSize = 32;
    private static final BufferedImage[] eggSprites = new BufferedImage[30];
    protected BufferedImage sprite;
    private static final Random rand = new Random();
    boolean up, down, left, right;
    VectorManipulation vectorManipulation = new VectorManipulation();

    private double rotation = 0;          // Current rotation in radians
    private final double rotationSpeed = Math.toRadians(180); // 180 degrees per second

    public Egg(GameWorld gameWorld) {
        super(gameWorld);
        this.speed = 400;
        this.damage = 50.0 + gameWorld.getPlayer().getPlayerStats().getDamage();
        initEgg();

        this.setDxDy();
        Point2D.Double dxDy = vectorManipulation.normalise(dx, dy);
        this.dx = dxDy.getX();
        this.dy = dxDy.getY();

        loadSprite();
        hitBox = new Rectangle((int) x, (int) y, 32, 32);
    }

    public Egg(GameWorld gameWorld, double angleDegrees) {
        super(gameWorld);
        this.speed = 400;
        this.damage = 50.0 + gameWorld.getPlayer().getPlayerStats().getDamage();
        initEgg();
        Point2D.Double dxDy = vectorManipulation.rotateNormalise(dx, dy, angleDegrees);
        this.dx = dxDy.getX();
        this.dy = dxDy.getY();
        loadSprite();
        hitBox = new Rectangle((int) x, (int) y, 32, 32);
    }

    public void initEgg() {
        this.x = owner.getX();
        this.y = owner.getY();
        this.up = owner.isLastFacingUp();
        this.down = owner.isLastFacingDown();
        this.left = owner.isLastFacingLeft();
        this.right = owner.isLastFacingRight();
        this.lastUpdateTime = System.nanoTime();
        this.setDxDy();
    }

    public void setDxDy() {
        if (this.up) dy -= 1;
        if (this.down) dy += 1;
        if (this.left) dx -= 1;
        if (this.right) dx += 1;
    }

    @Override
    public void update(double dt) {
        double diagonalBoost = 1.05;
        if (dx != 0 && dy != 0) {
            double diagonal = Math.sqrt(2);
            x += (dx / diagonal) * this.speed * diagonalBoost * dt;
            y += (dy / diagonal) * this.speed * diagonalBoost * dt;
        } else {
            x += dx * this.speed * dt;
            y += dy * this.speed * dt;
        }
        this.checkEnemyCollision();

        updateHitBox();

        // Increment rotation
        rotation += rotationSpeed * dt;
    }

    protected void loadSprite() {
        if (!loaded) {
            BufferedImage sheet = SpriteLoader.load("/sprites/weapons/eggs/eggBulletSpriteSheet.png");
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
            Graphics2D g2d = (Graphics2D) g;
            AffineTransform old = g2d.getTransform();

            int drawX = (int) (x - camera.getX());
            int drawY = (int) (y - camera.getY());

            // Rotate around the center of the sprite
            g2d.rotate(rotation, drawX + spriteSize / 2.0, drawY + spriteSize / 2.0);
            g2d.drawImage(sprite, drawX, drawY, spriteSize, spriteSize, null);

            g2d.setTransform(old);
        }
    }

    @Override
    protected void onHitEffect(Enemy e) {

    }

    @Override
    protected void onDeletionEffect() {

    }
}
