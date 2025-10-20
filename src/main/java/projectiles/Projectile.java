package projectiles;

import utils.Camera;
import utils.GameWorld;


public abstract class Projectile {
    protected double x, y;
    protected double dx, dy;
    protected double speed;
    protected double damage;
    protected boolean alive = true;
    protected GameWorld gameWorld;
    public long lastUpdateTime;

    public Projectile(GameWorld gameWorld) {
        this.gameWorld = gameWorld;
    }

    public abstract void update();

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

    // Accessors for modifiers
    public double getX() { return x; }
    public double getY() { return y; }
    public double getDamage() { return damage; }
    public GameWorld getGameWorld() { return gameWorld; }
}
