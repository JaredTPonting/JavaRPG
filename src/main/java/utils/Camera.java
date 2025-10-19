package utils;

import player.Player;

import java.awt.*;
import java.util.Random;

public class Camera {
    double camX, camY;
    int screenWidth;
    int screenHeight;

    public void centerOn(Player player, int screenWidth, int screenHeight) {
        camX = player.getX() - (double) screenWidth / 2;
        camY = player.getY() - (double) screenHeight / 2;
        this.screenHeight = screenHeight;
        this.screenWidth = screenWidth;
    }

    public double getX() { return camX; }
    public double getY() { return camY; }
}

