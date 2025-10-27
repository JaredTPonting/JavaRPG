package utils;

import entities.Entity;
import projectiles.Projectile;
import lingeringzones.LingeringZone;

import java.awt.*;

public class CollisionChecker {
    public boolean checkCollision(Entity a, Entity b) {
        return circleCollision(a, b);
    }

    public boolean entityProjectileCollision(Entity a, Projectile p) {
        return circleRectCollision(a, p.getHitBox());
    }

    public boolean entityBaseCollision(Entity e, Projectile p) {
        Rectangle entityHitBox = e.getHitBox();
        int baseHeight = Math.max(1, (int)(entityHitBox.height * 0.1));
        Rectangle bottomRect = new Rectangle(
                entityHitBox.x,
                entityHitBox.y + entityHitBox.height - baseHeight,
                entityHitBox.width,
                baseHeight
        );

        if (e.getHitBoxRadius() > 0)
            return circleRectCollision(e, p.getHitBox());
        else
            return bottomRect.intersects(p.getHitBox());
    }

    public boolean entityStandingInZone(Entity e, LingeringZone z) {
        // Bottom half of enemy circle
        double ex = e.getHitBoxCenterX();
        double ey = e.getHitBoxCenterY() + e.getHitBoxRadius() * 0.5; // bottom half
        double er = e.getHitBoxRadius() * 0.5;

        // Quick bounding check using ellipse formula
        double dx = (ex - z.getX()) / (z.getWidth() / 2.0);
        double dy = (ey - z.getY()) / (z.getHeight() / 2.0);
        double insideEllipse = dx * dx + dy * dy;

        // Consider enemy "standing in zone" if its bottom overlaps the ellipse boundary
        return insideEllipse <= 1.0 + (er / Math.max(z.getWidth(), z.getHeight()));
    }

    // Helper

    private double clamp(double min, double max, double value) {
        return Math.max(min, Math.min(max, value));
    }

    public boolean circleCollision(Entity a, Entity b) {
        double dx = a.getHitBoxCenterX() - b.getHitBoxCenterX();
        double dy = a.getHitBoxCenterY() - b.getHitBoxCenterY();
        double distSq = dx * dx + dy * dy;
        double radiusSum = a.getHitBoxRadius() + b.getHitBoxRadius();
        return distSq <= radiusSum * radiusSum;
    }

    public boolean circleRectCollision(Entity circleEntity, Rectangle rect) {
        double circleX = circleEntity.getHitBoxCenterX();
        double circleY = circleEntity.getHitBoxCenterY();
        double radius = circleEntity.getHitBoxRadius();

        double closestX = clamp(rect.getX(), rect.getX() + rect.getWidth(), circleX);
        double closestY = clamp(rect.getY(), rect.getY() + rect.getHeight(), circleY);

        double dx = circleX - closestX;
        double dy = circleY - closestY;

        return (dx * dx + dy * dy) <= (radius * radius);
    }
}
