package weapons.fireballblaster.weaponmods;

import projectiles.Projectile;
import projectiles.egg.Egg;
import core.GameWorld;
import projectiles.fireball.FireBall;
import weapons.WeaponMods;

import java.util.List;

public class ExtraFireBall extends WeaponMods {
    @Override
    public List<Projectile> addProjectile(GameWorld gameWorld, List<Projectile> projectiles) {
        projectiles.add(new FireBall(gameWorld, gameWorld.getEnemySpawner().getRandomEnemyInRange(gameWorld.getPlayer().getX(), gameWorld.getPlayer().getY(), 300)));
        return projectiles;
    }
}
