package weapons.eggcannon;

import entities.Entity;
import entities.player.Player;
import projectiles.Projectile;
import projectiles.egg.Egg;
import utils.GameWorld;
import weapons.Weapon;
import weapons.WeaponMods;

import java.util.ArrayList;
import java.util.List;

public class EggCannon extends Weapon {

    public EggCannon(Entity owner, long cooldownMillis, GameWorld gameWorld) {
        super(owner, cooldownMillis, gameWorld);
    }

    @Override
    protected List<Projectile> createProjectiles() {
        List newEggs = new ArrayList();
        newEggs.add(new Egg(this.gameWorld));
        for (WeaponMods mod : this.weaponMods) {
            mod.addProjectile(this.gameWorld, newEggs);
        }
        return newEggs;
    }
}
