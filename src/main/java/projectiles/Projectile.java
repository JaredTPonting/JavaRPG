package projectiles;

import utils.Camera;
import utils.GameWorld;

import java.awt.*;


public abstract class Projectile {
    protected double x, y;
    protected double dx, dy;
    protected double speed;
    protected double damage;
    protected boolean alive = true;
    protected GameWorld gameWorld;
    public long lastUpdateTime;

    protected Rectangle hitBox;

    public Projectile(GameWorld gameWorld) {
        this.gameWorld = gameWorld;
    }

    public abstract void update();
    protected void updateHitBox() {
        hitBox.setLocation((int) this.x, (int) this.y);
    }
    public Rectangle getHitBox() { return this.hitBox; }

    public abstract void render(java.awt.Graphics g, Camera camera);

    protected void onHit() {
        onHitEffect();
    }

    protected abstract void onHitEffect();

    protected boolean hasCollided() {
        // Placeholder for collision detection
        return false;
    }

    public boolean isAlive() { return alive; }
    public void destroyProjectile() { this.alive=false; }

    // Accessors for modifiers
    public double getX() { return x; }
    public double getY() { return y; }
    public double getDamage() { return damage; }
    public GameWorld getGameWorld() { return gameWorld; }
}
