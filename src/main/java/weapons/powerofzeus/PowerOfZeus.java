package weapons.powerofzeus;

import core.GameWorld;
import entities.Entity;
import projectiles.Projectile;
import projectiles.lightingbolt.LightningBolt;
import weapons.Weapon;

import java.util.ArrayList;
import java.util.List;

public class PowerOfZeus extends Weapon {
    public PowerOfZeus(Entity owner, long cooldownMillis, GameWorld gameWorld) {
        super(owner, cooldownMillis, gameWorld);
    }

    @Override
    protected List<Projectile> createProjectiles() {
        System.out.println("creating bolt");
        List<Projectile> newBolts = new ArrayList<>();
        newBolts.add(new LightningBolt(gameWorld));
        return newBolts;
    }
}
