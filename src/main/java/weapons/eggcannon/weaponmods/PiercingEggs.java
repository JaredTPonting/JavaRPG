package weapons.eggcannon.weaponmods;

import projectiles.Projectile;
import projectiles.egg.Egg;
import core.GameWorld;
import weapons.WeaponMods;

import java.util.List;

public class PiercingEggs extends WeaponMods {
    @Override
    public List<Projectile> modifyProjectiles(GameWorld gameWorld, List<Projectile> eggs) {
        for (Projectile proj : eggs) {
            if (proj instanceof Egg) {
                proj.increaseMaxHits(1);
            }
        }
        return eggs;
    }
}