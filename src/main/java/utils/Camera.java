package utils;

import entities.player.Player;

public class Camera {
    double camX, camY;
    int screenWidth;
    int screenHeight;
    boolean frozen = false;
    boolean returning = false;

    public void update(Player player, int screenWidth, int screenHeight) {
        if (frozen) return;
        camX = player.getX() - (double) screenWidth / 2;
        camY = player.getY() - (double) screenHeight / 2;
        this.screenHeight = screenHeight;
        this.screenWidth = screenWidth;
    }

    public void freeze(Player player) {
        camX = player.getX() - (double) screenWidth / 2;
        camY = player.getY() - (double) screenHeight / 2;
        this.frozen = true;
    }
    public void unfreeze() { this.frozen = false; }


    public double getX() { return camX; }
    public double getY() { return camY; }
}

