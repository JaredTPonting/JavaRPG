package weapons.chaosorbblaster.weaponmods;

import projectiles.chaosorb.ChaosOrb;
import projectiles.egg.Egg;
import core.GameWorld;
import weapons.WeaponMods;

import java.util.List;

public class ExtraShot extends WeaponMods {
    @Override
    public List addProjectile(GameWorld gameWorld, List chaosOrbs) {
        chaosOrbs.add(new ChaosOrb(gameWorld));
        return chaosOrbs;
    }
}
