package utils;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.util.Random;

public class DamageIndicator {
    private static final double RISE_SPEED = 0.5;
    private static final double DRIFT_RANGE = 0.5;

    private int damageValue;
    private double x, y;
    private double dx;
    private boolean finished = false;
    private Cooldown timer;

    private final Color baseColor;
    private final Font font = new Font("Franklin Gothic Heavy", Font.BOLD, 18);
    private final Random random = new Random();
    private final double rotation; // slight random rotation
    private final double scale; // scaling effect

    public DamageIndicator(int damageValue, int x, int y) {
        this.damageValue = damageValue;
        this.x = x;
        this.y = y;

        this.dx = (random.nextDouble() - 0.5) * DRIFT_RANGE;
        this.rotation = (random.nextDouble() - 0.5) * 20; // rotate up to ±20 degrees
        this.scale = 1 + random.nextDouble() * 0.5; // initial scale between 1 and 1.5

        this.timer = new Cooldown(0.7);

        int red = Math.min(255, 150 + damageValue * 3);
        int green = Math.max(0, 255 - damageValue * 8);
        this.baseColor = new Color(red, green, 0);
    }

    public void update(double dt) {
        timer.update(dt);
        if (timer.ready()) finished = true;

        y -= RISE_SPEED;
        x += dx;
    }

    public void render(Graphics g, Camera camera) {
        int screenX = (int) (x - camera.getX());
        int screenY = (int) (y - camera.getY());

        float alpha = 1f - (float) timer.percentDone();
        alpha = Math.max(0, Math.min(1, alpha));

        Graphics2D g2d = (Graphics2D) g;
        Composite oldComp = g2d.getComposite();
        AffineTransform oldTransform = g2d.getTransform();

        g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alpha));
        g2d.setFont(font);

        // Apply rotation and scaling
        g2d.translate(screenX, screenY);
        double currentScale = scale * (1.0 - timer.percentDone() * 0.5);
        g2d.scale(currentScale, currentScale);
        g2d.rotate(Math.toRadians(rotation * (1 - timer.percentDone())));

        // Black outline (draw shadow in 4 directions for stronger outline)
        g2d.setColor(Color.BLACK);
        int offset = 2;
        g2d.drawString(String.valueOf(damageValue), 0 + offset, 0);
        g2d.drawString(String.valueOf(damageValue), 0 - offset, 0);
        g2d.drawString(String.valueOf(damageValue), 0, 0 + offset);
        g2d.drawString(String.valueOf(damageValue), 0, 0 - offset);

        // Main pale red number
        g2d.setColor(new Color(255, 150, 150));
        g2d.drawString(String.valueOf(damageValue), 0, 0);

        // Restore
        g2d.setTransform(oldTransform);
        g2d.setComposite(oldComp);
    }

    public boolean isFinished() {
        return finished;
    }
}
