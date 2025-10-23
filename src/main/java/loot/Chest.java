package loot;

import utils.Camera;
import utils.Renderable;
import utils.SpriteLoader;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.nio.Buffer;

public class Chest implements Renderable {
    private boolean active = true;
    private boolean opened = false;
    private double x, y;
    private double renderY;
    private double despwanTimer = 30;
    private double openedTimer = 3;
    private int size = 60;

    // chest image
    private static BufferedImage closedChest;
    private static BufferedImage openChest;

    static {
        closedChest = SpriteLoader.load("/sprites/Chests/basic/closed.png");
        openChest = SpriteLoader.load("/sprites/Chests/basic/open.png");
        if (closedChest == null) {
            System.err.println("NO CHEST IMAGE");
        }
    }

    public Chest(double x, double y) {
        this.x = x;
        this.y = y;
        this.renderY = this.y - 50;
    }

    public void deActivate() {
        this.active = false;
    }


    public void update(double dt) {
        despwanTimer -= dt;
        if (despwanTimer <= 0) {
            deActivate();
        }
        if (opened){
            openedTimer -= dt;
            if (openedTimer <= 0) {
                deActivate();
            }
        }
        this.renderY = Math.min(this.y, this.renderY + 10);

    }

    public void render(Graphics g, Camera c) {
        int drawX = (int) (x - c.getX());
        int drawY = (int) (renderY - c.getY());
        if (opened) {
            g.drawImage(openChest, drawX, drawY, this.size + 10, this.size, null);
        } else {
            g.drawImage(closedChest, drawX, drawY, this.size + 10, this.size, null);
        }
    }

    @Override
    public double getRenderY() {
        return this.y + this.size;
    }

    public double getY() {
        return this.y;
    }

    public boolean isActive() {
        return this.active;
    }
}
