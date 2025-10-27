package weapons.chaosorbblaster;

import entities.Entity;
import projectiles.Projectile;
import projectiles.chaosorb.ChaosOrb;
import core.GameWorld;
import weapons.Weapon;
import weapons.WeaponMods;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class ChaosOrbBlaster extends Weapon {

    public ChaosOrbBlaster(Entity owner, long cooldownMillis, GameWorld gameWorld) {
        super(owner, cooldownMillis, gameWorld);
    }

    @Override
    protected List<Projectile> createProjectiles() {
        List<Projectile> newOrbs = new ArrayList<>();
        newOrbs.add(new ChaosOrb(gameWorld));
        for (WeaponMods mod : this.weaponMods) {
            mod.addProjectile(this.gameWorld, newOrbs);
        }
        for (WeaponMods mod : this.weaponMods) {
            mod.modifyProjectiles(this.gameWorld, newOrbs);
        }
        return newOrbs;
    }
}
