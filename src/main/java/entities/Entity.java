package entities;

import java.awt.*;

import utils.Camera;
import utils.GameWorld;

public abstract class Entity{
    protected GameWorld gameWorld;

    protected double x, y;
    protected double dx, dy;
    protected int size;

    protected Rectangle hitBox;

    protected boolean active = true;

    public Entity(GameWorld gameWorld, double x, double y, int size) {
        this.gameWorld = gameWorld;
        this.x = x;
        this.y = y;
        this.hitBox = new Rectangle((int) x, (int) y, size, size);
        this.size = size;
    }

    public abstract void update();

    protected void updateHitBox() {
        hitBox.setLocation((int) this.x, (int) this.y);
    }
    public Rectangle getHitBox() {
        return hitBox;
    }
    public double getX() {
        return x;
    }
    public double getY() {
        return y;
    }
    public int getSize() { return this.size; }
    public boolean isActive() {
        return active;
    }
    public void deactivate() {
        active = false;
    }

    // Render
    public abstract void render(Graphics g, Camera camera);
}
