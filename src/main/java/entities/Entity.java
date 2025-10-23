package entities;

import java.awt.*;

import utils.Camera;
import core.GameWorld;
import utils.Renderable;

public abstract class Entity implements Renderable {
    protected GameWorld gameWorld;

    protected double x, y;
    protected double dx, dy;
    protected int size;
    protected double offsetWidth = 0;
    protected double offsetHeight = 0;

    protected Rectangle hitBox;
    public int width;
    public int height;
    public int xOffset;
    public int yOffset;

    protected boolean active = true;

    // for animation
    public boolean facingLeft = true;

    public Entity(GameWorld gameWorld, double x, double y, int size, double offsetWidth, double offsetHeight) {
        this.gameWorld = gameWorld;
        this.x = x;
        this.y = y;
        this.size = size;
        this.offsetWidth = offsetWidth;
        this.offsetHeight = offsetHeight;

//        this.hitBox = new Rectangle((int) x, (int) y, size, size);
        initHitBox();
    }

    public abstract void update(double dt);

    public void initHitBox() {
        this.xOffset = (int) (size * offsetWidth);
        this.yOffset = (int) (size * offsetHeight);
        this.width = (int) (size * (1 - 2*offsetWidth));
        this.height = (int) (size * (1 - 2*offsetHeight));
        this.hitBox = new Rectangle((int)this.x +this.xOffset, (int)this.y + this.yOffset, this.width, this.height);
    }

    protected void updateHitBox() {
        hitBox.setLocation((int) this.x + this.xOffset, (int) this.y + this.yOffset);
    }
    public Rectangle getHitBox() {
        return hitBox;
    }
    public double getX() {
        return x;
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
