package projectiles;

import entities.enemies.Enemy;
import entities.player.Player;
import entities.player.PlayerManager;
import utils.*;
import core.GameWorld;

import java.awt.*;
import java.util.ArrayList;


public abstract class Projectile {
    protected double x, y;
    protected double dx, dy;
    protected double speed;
    protected double damage;
    protected boolean alive = true;
    protected int size;
    protected GameWorld gameWorld;
    public long lastUpdateTime;
    public Player owner;
    public int maxHits;
    public ArrayList<Enemy> enemiesHit = new ArrayList<>();

    public static VectorManipulation vectorManipulation = new VectorManipulation();

    // Animation Stuff
    public static final boolean LOOP = true;
    public static final long FRAME_TIME = 100;
    public Animation animation;

    // LifeSpan
    public Cooldown lifeSpan;


    protected Rectangle hitBox;

    public Projectile(GameWorld gameWorld) {
        this.gameWorld = gameWorld;
        this.owner = gameWorld.getPlayer();
        this.maxHits = 1;
        this.lifeSpan = new Cooldown(10);
    }

    public void checkEnemyCollision() {
        for (Enemy e : gameWorld.getEnemySpawner().getEnemies()) {
            if (!enemiesHit.contains(e) & !e.triggeredDeath) {
                if (gameWorld.getCollisionChecker().entityProjectileCollision(e, this)) {
                    e.takeDamage(this.getDamage());
                    this.onHitEffect(e);
                    enemiesHit.add(e);
                    this.reduceMaxHits();
                }
            }
        }
    }

    public void updateLifeSpan(double dt) {
        lifeSpan.update(dt);
        if (lifeSpan.ready()) {
            this.destroyProjectile();
        }
    }

    public final void update(double dt) {
        updateLifeSpan(dt);
        if (alive) {
            onUpdate(dt);
        }
    }

    protected abstract void onUpdate(double dt);

    protected void updateHitBox() {
        hitBox.setLocation((int) this.x, (int) this.y);
    }
    public Rectangle getHitBox() { return this.hitBox; }

    public abstract void render(java.awt.Graphics g, Camera camera);

    public void setMaxHits(int newMaxHits) {
        this.maxHits = newMaxHits;
    }

    protected abstract void onHitEffect(Enemy e);
    protected abstract void onDeletionEffect();

    protected boolean hasCollided() {
        // Placeholder for collision detection
        return false;
    }

    public boolean isAlive() { return alive; }
    public void reduceMaxHits() {
        this.maxHits--;
        if (this.maxHits<=0){
            this.destroyProjectile();
        }
    }
    public void destroyProjectile() { this.onDeletionEffect(); this.alive=false; }

    // Accessors for modifiers
    public double getX() { return x; }
    public double getY() { return y; }
    public double getDamage() { return damage; }
    public GameWorld getGameWorld() { return gameWorld; }

    // Debugging
    public void drawHitbox(Graphics g, Camera c) {
        if (gameWorld.isDebugMode()) {
            g.drawRect((int) (hitBox.x - c.getX()), (int) (hitBox.y - c.getY()), hitBox.width, hitBox.height);
        }
    }

    public void increaseMaxHits(int amount) {
        this.maxHits += amount;
    }

    public int getSize() {
        return this.size;
    }

    public void scaleSize(double scaleFactor) {
        this.size = (int) (this.size * scaleFactor);
        resetHitBox();
    }

    public void resetHitBox() {
        this.hitBox = new Rectangle((int) x, (int) y, size, size);
    }
}
