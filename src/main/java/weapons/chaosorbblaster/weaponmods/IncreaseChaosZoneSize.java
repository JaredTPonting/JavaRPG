package weapons.chaosorbblaster.weaponmods;

import core.GameWorld;
import projectiles.Projectile;
import projectiles.chaosorb.ChaosOrb;
import weapons.WeaponMods;

import java.util.List;

public class IncreaseChaosZoneSize extends WeaponMods {

    public IncreaseChaosZoneSize() {
        this.multiple = true;
    }

    @Override
    public List<Projectile> modifyProjectiles(GameWorld gameWorld, List<Projectile> projectiles) {
        for (Projectile proj : projectiles) {
            if (proj instanceof ChaosOrb) {
                ((ChaosOrb) proj).increaseZoneSize(1.5);
            }
        }
        return projectiles;
    }
}
