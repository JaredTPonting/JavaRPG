package weapons.chaosorbblaster.weaponmods;

import core.GameWorld;
import projectiles.Projectile;
import projectiles.chaosorb.ChaosOrb;
import weapons.WeaponMods;

import java.util.List;

public class IncreaseChaosOrbSize extends WeaponMods {

    @Override
    public List<Projectile> modifyProjectiles(GameWorld gameWorld, List<Projectile> projectiles) {
        for (Projectile proj : projectiles) {
            if (proj instanceof ChaosOrb) {
                proj.scaleSize(1.5);
            }
        }
        return projectiles;
    }
}
