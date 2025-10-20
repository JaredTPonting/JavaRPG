package entities;

import java.awt.*;

import utils.Camera;
import utils.GameWorld;

public abstract class Entity {
    protected GameWorld gameWorld;

    protected double x, y;
    protected double dx, dy;
    protected int width, height;

    protected Rectangle hitBox;

    protected boolean active = true;

    public Entity(GameWorld gameWorld, double x, double y, int width, int height) {
        this.gameWorld = gameWorld;
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.hitBox = new Rectangle((int) x, (int) y, width, height);
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
    public int getWidth() {
        return width;
    }
    public int getHeight() {
        return height;
    }
    public boolean isActive() {
        return active;
    }
    public void deactivate() {
        active = false;
    }

    // Render
    public abstract void render(Graphics g, Camera camera);
}
