package utils;

import player.Player;

class Camera {
    int camX, camY;

    public void centerOn(Player player, int screenWidth, int screenHeight) {
        camX = player.getX() - screenWidth / 2;
        camY = player.getY() - screenHeight / 2;
    }

    public int getX() { return camX; }
    public int getY() { return camY; }
}

