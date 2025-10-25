package weapons.fireballblaster;

import core.GameWorld;
import entities.Entity;
import projectiles.Projectile;
import projectiles.fireball.FireBall;
import weapons.Weapon;
import weapons.WeaponMods;

import java.util.ArrayList;
import java.util.List;

public class FireBallBlaster extends Weapon {

    public FireBallBlaster(Entity owner, long cooldownMillis, GameWorld gameWorld) {
        super(owner, cooldownMillis, gameWorld);
    }

    @Override
    protected List<Projectile> createProjectiles() {
        List<Projectile> newFireBalls = new ArrayList<>();
        newFireBalls.add(new FireBall(gameWorld));
        for (WeaponMods mod : this.weaponMods) {
            mod.addProjectile(this.gameWorld, newFireBalls);
        }
        return newFireBalls;
    }

}
