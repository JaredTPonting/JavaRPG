package utils;

import entities.Entity;

public class CollisionChecker {
    public boolean checkCollision(Entity a, Entity b) {
        return a.getHitBox().intersects(b.getHitBox());
    }
}
