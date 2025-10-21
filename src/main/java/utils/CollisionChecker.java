package utils;

import entities.Entity;
import projectiles.Projectile;
import lingeringzones.LingeringZone;

import java.awt.*;

public class CollisionChecker {
    public boolean checkCollision(Entity a, Entity b) {
        return a.getHitBox().intersects(b.getHitBox());
    }

    public boolean entityProjectileCollision(Entity a, Projectile p) {
        return a.getHitBox().intersects(p.getHitBox());
    }

    public boolean entityStandingInZone(Entity e, LingeringZone z) {
        Rectangle rect = e.getHitBox();
        double circleX = z.getX();
        double circleY = z.getY();
        double radius = z.getRadius();

        // Bottom edge of the entity's hitbox
        double x1 = rect.getX();
        double x2 = rect.getX() + rect.getWidth();
        double y = rect.getY() + rect.getHeight(); // bottom edge Y coordinate

        // Step 1: Clamp the circle center’s X to the segment representing the bottom edge
        double closestX = clamp(x1, x2, circleX);

        // Step 2: Compute distance from the circle center to the closest point on the bottom edge
        double dx = circleX - closestX;
        double dy = circleY - y;

        // Step 3: Check if the distance is less than the radius
        return (dx * dx + dy * dy) < (radius * radius);
    }

    private double clamp(double min, double max, double value) {
        return Math.max(min, Math.min(max, value));
    }
}
