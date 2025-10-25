package loot;

import utils.Camera;
import utils.Renderable;
import utils.SpriteLoader;

import java.awt.*;
import java.awt.image.BufferedImage;

public abstract class Chest implements Renderable {
    private boolean active = true;
    private boolean opened = false;
    private double x, y;
    private double renderY;
    private double despwanTimer = 30;
    private double openedTimer = 3;
    private int size = 60;

    // chest image
    static BufferedImage Chests;
    private static int w, h;
    protected BufferedImage closedChest;
    protected BufferedImage openChest;

    public Chest(double x, double y) {
        if (Chests == null) {
            Chests = SpriteLoader.load("/sprites/Chests/all_chests.png");
        }

        assert Chests != null;
        w = Chests.getWidth() / 9;
        h = Chests.getHeight() / 4;
        System.out.println(w + "  " + h);

        this.x = x;
        this.y = y;
        this.renderY = this.y - 50;
    }

    protected BufferedImage getSubImage(int x, int y) {
        return Chests.getSubimage(x * w, y * h, w, h);
    }

    public void deActivate() {
        this.active = false;
    }

    public static void preload() {
        if (Chests == null) {
            Chests = SpriteLoader.load("/sprites/Chests/all_chests.png");
            w = Chests.getWidth() / 9;
            h = Chests.getHeight() / 4;
        }
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
        BufferedImage img = opened ? openChest : closedChest;

        if (img != null)
            g.drawImage(img, drawX, drawY, this.size + 10, this.size, null);
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
