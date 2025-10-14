package utils;

import player.Player;

import java.awt.*;
import java.util.Random;

public class Camera {
    int camX, camY;
    int screenWidth;
    int screenHeight;

    public void centerOn(Player player, int screenWidth, int screenHeight, int mapWidth, int mapHeight) {
        camX = player.getX() - screenWidth / 2;
        camY = player.getY() - screenHeight / 2;
        this.screenHeight = screenHeight;
        this.screenWidth = screenWidth;
    }

    public int getX() { return camX; }
    public int getY() { return camY; }
}

