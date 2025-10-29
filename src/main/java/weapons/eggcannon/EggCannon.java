package weapons.eggcannon;

import entities.Entity;
import projectiles.Projectile;
import projectiles.egg.Egg;
import core.GameWorld;
import weapons.Weapon;
import weapons.WeaponMods;
import weapons.eggcannon.weaponmods.*;

import java.util.ArrayList;
import java.util.List;

public class EggCannon extends Weapon {

    public EggCannon(Entity owner, long cooldownMillis, GameWorld gameWorld) {
        super(owner, cooldownMillis, gameWorld);
    }

    @Override
    public List<Class<? extends WeaponMods>> getAvailableModClasses() {
        List<Class<? extends WeaponMods>> mods = new ArrayList<>();
        mods.add(BackwardShot.class);
        mods.add(LeftShot.class);
        mods.add(RightShot.class);
        mods.add(PiercingEggs.class);
        mods.add(TripleEggMod.class);
        return mods;
    }

    @Override
    protected List<Projectile> createProjectiles() {
        List newEggs = new ArrayList();
        newEggs.add(new Egg(this.gameWorld));
        for (WeaponMods mod : this.weaponMods) {
            mod.addProjectile(this.gameWorld, newEggs);
        }
        for (WeaponMods mod : this.weaponMods) {
            mod.modifyProjectiles(this.gameWorld, newEggs);
        }
        return newEggs;
    }
}
