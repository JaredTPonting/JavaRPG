package utils;

import entities.Entity;
import projectiles.Projectile;

public class CollisionChecker {
    public boolean checkCollision(Entity a, Entity b) {
        return a.getHitBox().intersects(b.getHitBox());
    }

    public boolean entityProjectileCollision(Entity a, Projectile p) {
        return a.getHitBox().intersects(p.getHitBox());
    }
}
