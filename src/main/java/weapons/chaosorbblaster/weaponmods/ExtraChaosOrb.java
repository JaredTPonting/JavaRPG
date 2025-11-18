package weapons.chaosorbblaster.weaponmods;

import projectiles.chaosorb.ChaosOrb;
import core.GameWorld;
import weapons.WeaponMods;

import java.util.List;

public class ExtraChaosOrb extends WeaponMods {

    public ExtraChaosOrb() {
        this.multiple = true;
    }

    @Override
    public List addProjectile(GameWorld gameWorld, List chaosOrbs) {
        chaosOrbs.add(new ChaosOrb(gameWorld));
        return chaosOrbs;
    }
}
