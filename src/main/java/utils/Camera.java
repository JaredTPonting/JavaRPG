package utils;

import entities.player.Player;

public class Camera {
    private double camX, camY;
    private int screenWidth;
    private int screenHeight;
    private boolean frozen = false;
    private boolean returning = false;

    private static final double RETURN_SMOOTHING = 0.3;  // Lower = smoother/slower
    private static final double SNAP_THRESHOLD = 1.0;     // Close enough to snap

    public void update(Player player, int screenWidth, int screenHeight) {
        this.screenWidth = screenWidth;
        this.screenHeight = screenHeight;

        if (frozen) return;

        double targetX = player.getX() - screenWidth / 2.0;
        double targetY = player.getY() - screenHeight / 2.0;

        if (returning) {
            // Smoothly lerp back to player
            camX = lerp(camX, targetX, RETURN_SMOOTHING);
            camY = lerp(camY, targetY, RETURN_SMOOTHING);

            // Once close enough, snap and stop returning
            if (Math.abs(camX - targetX) < SNAP_THRESHOLD && Math.abs(camY - targetY) < SNAP_THRESHOLD) {
                camX = targetX;
                camY = targetY;
                returning = false;
            }
        } else {
            camX = targetX;
            camY = targetY;
        }
    }

    private double lerp(double a, double b, double t) {
        return a + (b - a) * t;
    }

    public int getWidth() { return screenWidth; }
    public int getHeight() { return screenHeight; }

    public void freeze(Player player) {
        camX = player.getX() - screenWidth / 2.0;
        camY = player.getY() - screenHeight / 2.0;
        frozen = true;
        returning = false;
    }

    public void unfreeze() {
        frozen = false;
        returning = true;  // Start smooth return
    }

    public boolean isFrozen() { return frozen; }

    public double getX() { return camX; }
    public double getY() { return camY; }
}

