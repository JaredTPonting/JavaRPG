package weapons;

import utils.Camera;

import java.awt.*;

// Nested class for lingering damage zone
public class LingeringZone {
    private double x, y, radius, dps;
    private long startTime;
    private long durationMillis;
    private boolean alive = true;

    public LingeringZone(double x, double y, double radius, long durationMillis, double dps) {
        this.x = x;
        this.y = y;
        this.radius = radius;
        this.durationMillis = durationMillis;
        this.dps = dps;
        this.startTime = System.currentTimeMillis();
    }

    public void update() {
        if (System.currentTimeMillis() - startTime > durationMillis) {
            alive = false;
        }

        // TODO: deal periodic damage to enemies within radius
    }

    public void render(Graphics g, Camera camera) {
        if (!alive) return;
        g.setColor(new Color(255, 0, 255, 80));
        g.fillOval((int) (x - radius - camera.getX()), (int) (y - radius - camera.getY()),
                (int) (radius * 2), (int) (radius * 2));
    }

    public boolean isAlive() {
        return alive;
    }
}