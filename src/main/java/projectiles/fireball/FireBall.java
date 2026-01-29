package projectiles.fireball;

import core.GameWorld;
import entities.Entity;
import entities.enemies.Enemy;
import projectiles.Projectile;
import utils.Animation;
import utils.Camera;
import utils.SpriteLoader;

import java.awt.*;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;

public class FireBall extends Projectile {
    private final int frameWidth;
    private final int height;
    private Entity target;
    private double lastTargetX;
    private double lastTargetY;
    private boolean trackingLocked = false;

    // statics
    private static BufferedImage SPRITE_SHEET;
    private static final int FRAME_COUNT = 8;


    public FireBall(GameWorld gameWorld, Entity target) {
        super(gameWorld);
        this.target = target;
        this.x = this.owner.getX();
        this.y = this.owner.getY();
        this.speed = 150;
        this.damage = 50 + this.owner.getPlayerManager().getMagicDamage();
        this.dx = 0;
        this.dy = 0;

        if (SPRITE_SHEET == null) {
            SPRITE_SHEET = SpriteLoader.load("/sprites/weapons/fireball/fireball_centered.png");
        }

        this.animation = new Animation(SPRITE_SHEET, FRAME_COUNT, FRAME_TIME, LOOP);

        this.frameWidth = SPRITE_SHEET.getWidth() / FRAME_COUNT;
        this.height = SPRITE_SHEET.getHeight();
        this.hitBox = new Rectangle((int) (x + frameWidth * 0.4), (int) (y*0.5), (int) (frameWidth * 0.6), (int) (height * 0.5));

        this.lastTargetX = target.getX();
        this.lastTargetY = target.getY();
    }

    private void updateDxDY() {
        if (trackingLocked) return;

        double targetX = target.getX();
        double targetY = target.getY();

        // Detect teleport - if target moved more than possible in one frame, lock direction
        double movedDistSq = (targetX - lastTargetX) * (targetX - lastTargetX)
                           + (targetY - lastTargetY) * (targetY - lastTargetY);
        if (movedDistSq > 200 * 200) {
            trackingLocked = true;
            return;
        }

        lastTargetX = targetX;
        lastTargetY = targetY;

        double newDx = this.x - targetX;
        double newDy = this.y - targetY;

        Point2D.Double dxDy = vectorManipulation.normalise(newDx, newDy);
        this.dx = dxDy.getX();
        this.dy = dxDy.getY();
    }

    @Override
    public void onUpdate(double dt) {
        this.updateDxDY();
        x += this.dx * speed * dt;
        y += this.dy * speed * dt;

        updateHitBox();
        this.checkEnemyCollision();
        animation.update();
    }

    @Override
    public void updateHitBox() {
        int hitBoxWidth = (int) (frameWidth * 0.5);
        int hitBoxHeight = (int) (height * 0.5);

        int hitBoxX = (int) (x + (frameWidth - hitBoxWidth) / 2.0);
        int hitBoxY = (int) (y + (height - hitBoxHeight) / 2.0);

        this.hitBox.setBounds(hitBoxX, hitBoxY, hitBoxWidth, hitBoxHeight);
    }

    @Override
    public void render(Graphics g, Camera camera) {
        // rotate to point at enemy
        Graphics2D g2d = (Graphics2D) g.create();
        double angle = Math.atan2(dy, dx);

        int screenX = (int) (x - camera.getX());
        int screenY = (int) (y - camera.getY());

        g2d.translate(screenX + frameWidth / 2.0, screenY + height / 2.0);
        g2d.rotate(angle);
        g2d.drawImage(animation.getCurrentFrame(), -frameWidth/2, -height/2, null);
        g2d.dispose();
        drawHitbox(g, camera);
    }

    @Override
    protected void onHitEffect(Enemy e) {

    }

    @Override
    protected void onDeletionEffect() {

    }
}
