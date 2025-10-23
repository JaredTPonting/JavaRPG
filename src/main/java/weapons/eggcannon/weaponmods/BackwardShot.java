package weapons.eggcannon.weaponmods;

import projectiles.egg.Egg;
import core.GameWorld;
import weapons.WeaponMods;

import java.util.List;

public class BackwardShot extends WeaponMods {
    @Override
    public List addProjectile(GameWorld gameWorld, List eggs) {
        eggs.add(new Egg(gameWorld, 180));
        return eggs;
    }
}
