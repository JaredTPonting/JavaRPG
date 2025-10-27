package entities;

import java.awt.*;

import utils.Camera;
import core.GameWorld;
import utils.Renderable;
import utils.WorldContext;

public abstract class Entity implements Renderable {
    protected WorldContext gameWorld;

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

    // For Hitbox
    private double hitBoxOffsetXPercent = 0.5;
    private double hitBoxOffsetYPercent = 0.5;
    private double hitBoxRadiusPercent = 0.5;

    public Entity(WorldContext gameWorld, double x, double y, int size, double offsetWidth, double offsetHeight) {
        this.gameWorld = gameWorld;
        this.x = x;
        this.y = y;
        this.size = size;
        this.offsetWidth = offsetWidth;
        this.offsetHeight = offsetHeight;
        initHitBox();
    }

    public abstract void update(double dt);


    // HitBox

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

    public void setHitBox(double xPercent, double yPercent, double radiusPercent) {
        this.hitBoxOffsetXPercent = xPercent;
        this.hitBoxOffsetYPercent = yPercent;
        this.hitBoxRadiusPercent = radiusPercent;
    }

    public double getHitBoxCenterX() {
        return x + (size * hitBoxOffsetXPercent);
    }

    public double getHitBoxCenterY() {
        return y + (size * hitBoxOffsetYPercent);
    }
    public double getHitBoxRadius() {
        return size * hitBoxRadiusPercent;
    }
    public void drawHitBox(Graphics g, Camera c) {
        Color old = g.getColor();
        g.setColor(new Color(0, 100, 155, 100));
        int drawX = (int) (getHitBoxCenterX() - getHitBoxRadius() - c.getX());
        int drawY = (int) (getHitBoxCenterY() - getHitBoxRadius() - c.getY());
        int diameter = (int) (getHitBoxRadius() * 2);

        g.fillOval(drawX, drawY, diameter, diameter);
        g.setColor(old);
    }


    //
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

    public double getY() {
        return this.y;
    }
}
