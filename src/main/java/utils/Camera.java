package utils;

import player.Player;

class Camera {
    int camX, camY;

    public void centerOn(Player player, int screenWidth, int screenHeight, int mapWidth, int mapHeight) {
        camX = Math.max(0, Math.min(player.getX() - screenWidth / 2, mapWidth - screenWidth));
        camY = Math.max(0, Math.min(player.getY() - screenHeight / 2, mapHeight - screenHeight));
    }

    public int getX() { return camX; }
    public int getY() { return camY; }
}

