package weapons.chaosorbblaster;

import entities.Entity;
import projectiles.Projectile;
import projectiles.chaosorb.ChaosOrb;
import core.GameWorld;
import weapons.Weapon;
import weapons.WeaponMods;
import weapons.chaosorbblaster.weaponmods.ExtraShot;
import weapons.chaosorbblaster.weaponmods.IncreaseChaosOrbSize;
import weapons.eggcannon.weaponmods.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class ChaosOrbBlaster extends Weapon {

    public ChaosOrbBlaster(Entity owner, long cooldownMillis, GameWorld gameWorld) {
        super(owner, cooldownMillis, gameWorld);
    }

    @Override
    public List<Class<? extends WeaponMods>> getAvailableModClasses() {
        List<Class<? extends WeaponMods>> mods = new ArrayList<>();
        mods.add(ExtraShot.class);
        mods.add(IncreaseChaosOrbSize.class);
        return mods;
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
