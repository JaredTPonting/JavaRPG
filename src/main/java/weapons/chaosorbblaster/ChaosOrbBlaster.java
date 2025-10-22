package weapons.chaosorbblaster;

import entities.Entity;
import projectiles.Projectile;
import projectiles.chaosorb.ChaosOrb;
import utils.GameWorld;
import weapons.Weapon;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class ChaosOrbBlaster extends Weapon {

    private Random random = new Random();

    public ChaosOrbBlaster(Entity owner, long cooldownMillis, GameWorld gameWorld) {
        super(owner, cooldownMillis, gameWorld);
    }

    @Override
    protected List<Projectile> createProjectiles() {
        List<Projectile> projectiles = new ArrayList<>();

        // random direction unit vector
        double angle = random.nextDouble() * 2 * Math.PI;
        double dx = Math.cos(angle);
        double dy = Math.sin(angle);

        projectiles.add(new ChaosOrb(gameWorld, owner.getX(), owner.getY(), dx, dy));
        return projectiles;
    }
}
