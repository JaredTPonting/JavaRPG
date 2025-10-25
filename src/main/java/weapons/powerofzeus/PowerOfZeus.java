package weapons.powerofzeus;

import core.GameWorld;
import entities.Entity;
import projectiles.Projectile;
import projectiles.lightingbolt.LightningBolt;
import weapons.Weapon;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class PowerOfZeus extends Weapon {
    private Random random = new Random();
    public PowerOfZeus(Entity owner, long cooldownMillis, GameWorld gameWorld) {
        super(owner, cooldownMillis, gameWorld);
    }

    @Override
    protected List<Projectile> createProjectiles() {
        List<Entity> enemiesInRange = gameWorld.getEnemySpawner().getEnemiesInRange(owner.getX(), owner.getY(), 300);
        if (enemiesInRange != null) {
            List<Projectile> newBolts = new ArrayList<>();
            newBolts.add(new LightningBolt(gameWorld, enemiesInRange.get(random.nextInt(enemiesInRange.size()))));
            return newBolts;
        }
        else return List.of();
    }
}
