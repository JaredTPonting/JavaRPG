package utils;

import java.awt.*;
import java.util.Random;

import java.awt.Color;

public class DamageIndicator {
    private static final double RISE_SPEED = 0.5;
    private static final double DRIFT_RANGE = 0.5;

    private int damageValue;
    private double x;
    private double y;
    private double dx;
    private boolean finished = false;
    private Cooldown timer;

    private final Color baseColor;
    private final Font font = new Font("Arial", Font.BOLD, 16);

    public DamageIndicator(int damageValue, int x, int y){
        this.damageValue = damageValue;
        this.x = x;
        this.y = y;

        Random r = new Random();
        this.dx = (r.nextDouble() - 0.5) * DRIFT_RANGE;


        this.timer = new Cooldown(0.7);
        int red = Math.min(255, 100 + damageValue * 5);
        int green = Math.max(0, 255 - damageValue * 8);

        baseColor = new Color(red, green, 0);
    }

    public void update(double dt) {
        this.timer.update(dt);
        if (timer.ready()) {this.finished = true;}

        this.y -= RISE_SPEED;
        this.x += dx;

    }

    public void render(Graphics g, Camera camera) {
        int screenX = (int) (x - camera.getX());
        int screenY = (int) (y - camera.getY());

        float alpha = 1f - (float) timer.percentDone();
        alpha = Math.max(0, Math.min(1, alpha));

        Graphics2D g2d = (Graphics2D) g;
        Composite oldComp = g2d.getComposite();

        g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alpha));
        g2d.setFont(this.font);

        g2d.setColor(Color.BLACK);
        g2d.drawString(String.valueOf(damageValue), screenX + 1, screenY + 1);

        g2d.setColor(baseColor);
        g2d.drawString(String.valueOf(damageValue), screenX, screenY);

        g2d.setComposite(oldComp);

    }

    public boolean isFinished() {
        return this.finished;
    }
}
