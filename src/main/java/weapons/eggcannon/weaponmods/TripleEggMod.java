package weapons.eggcannon.weaponmods;

import entities.Entity;
import entities.player.Player;
import projectiles.egg.Egg;
import utils.GameWorld;
import weapons.WeaponMods;

import java.util.List;

public class TripleEggMod extends WeaponMods {
    @Override
    public List addProjectile(GameWorld gameWorld, List eggs) {
        eggs.add(new Egg(gameWorld, 20.0));
        eggs.add(new Egg(gameWorld, -20.0));
        return eggs;
    }
}
