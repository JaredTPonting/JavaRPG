package utils;

import player.Player;

public class Camera {
    int camX, camY;

    public void centerOn(Player player, int screenWidth, int screenHeight, int mapWidth, int mapHeight) {
        camX = player.getX() - screenWidth / 2;
        camY = player.getY() - screenHeight / 2;
    }

    public int getX() { return camX; }
    public int getY() { return camY; }
}

