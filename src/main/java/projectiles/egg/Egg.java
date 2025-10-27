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
    protected int size = 20;
    private static BufferedImage[] eggSprites;
    protected BufferedImage sprite;
    protected static int spriteCount = 7;
    private static final Random rand = new Random();
    boolean up, down, left, right;
    VectorManipulation vectorManipulation = new VectorManipulation();

    private double rotation = 0;          // Current rotation in radians
    private final double rotationSpeed = Math.toRadians(180); // 180 degrees per second

    // INIT
    public static void init() {
        if (eggSprites != null) return; // already loaded

        BufferedImage sheet = SpriteLoader.load("/sprites/weapons/eggs/eggs.png");
        if (sheet == null) {
            System.err.println("Failed to load egg sprite sheet!");
            return;
        }

        eggSprites = new BufferedImage[spriteCount];
        int SPRITE_SIZE = sheet.getHeight();

        for (int i = 0; i < spriteCount; i++) {
            eggSprites[i] = sheet.getSubimage(i * SPRITE_SIZE, 0, SPRITE_SIZE, SPRITE_SIZE);
        }
    }


    public Egg(GameWorld gameWorld) {
        super(gameWorld);
        this.speed = 400;
        this.damage = 50.0 + gameWorld.getPlayer().getPlayerManager().getDamage();
        initEgg();

        this.setDxDy();
        Point2D.Double dxDy = vectorManipulation.normalise(dx, dy);
        this.dx = dxDy.getX();
        this.dy = dxDy.getY();

        this.sprite = getRandomEggSprite();
    }

    public Egg(GameWorld gameWorld, double angleDegrees) {
        super(gameWorld);
        this.speed = 400;
        this.damage = 50.0 + gameWorld.getPlayer().getPlayerStats().getDamage();
        initEgg();
        Point2D.Double dxDy = vectorManipulation.rotateNormalise(dx, dy, angleDegrees);
        this.dx = dxDy.getX();
        this.dy = dxDy.getY();
        this.sprite = getRandomEggSprite();
    }

    public void initEgg() {
        this.x = owner.getCenter().getX();
        this.y = owner.getCenter().getY();
        this.up = owner.isLastFacingUp();
        this.down = owner.isLastFacingDown();
        this.left = owner.isLastFacingLeft();
        this.right = owner.isLastFacingRight();
        this.lastUpdateTime = System.nanoTime();
        this.setDxDy();
        hitBox = new Rectangle((int) x, (int) y, size, size);
    }

    private BufferedImage getRandomEggSprite() {
        if (eggSprites == null) {
            init(); // fallback: load them now
        }
        return eggSprites[rand.nextInt(eggSprites.length)];
    }

    public void setDxDy() {
        if (this.up) dy -= 1;
        if (this.down) dy += 1;
        if (this.left) dx -= 1;
        if (this.right) dx += 1;
    }

    @Override
    public void onUpdate(double dt) {
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

    @Override
    public void render(Graphics g, Camera camera) {
        drawHitbox(g, camera);
        if (sprite != null) {
            Graphics2D g2d = (Graphics2D) g;
            AffineTransform old = g2d.getTransform();

            int drawX = (int) (x - camera.getX());
            int drawY = (int) (y - camera.getY());

            // Rotate around the center of the sprite
            g2d.rotate(rotation, drawX + size / 2.0, drawY + size / 2.0);
            g2d.drawImage(sprite, drawX, drawY, size, size, null);

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
