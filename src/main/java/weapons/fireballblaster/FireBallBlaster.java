package weapons.fireballblaster;

import core.GameWorld;
import entities.Entity;
import projectiles.Projectile;
import projectiles.fireball.FireBall;
import weapons.Weapon;
import weapons.WeaponMods;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class FireBallBlaster extends Weapon {
    private Random random = new Random();

    public FireBallBlaster(Entity owner, long cooldownMillis, GameWorld gameWorld) {
        super(owner, cooldownMillis, gameWorld);
    }

    @Override
    protected List<Projectile> createProjectiles() {
        List<Entity> enemiesInRange = gameWorld.getEnemySpawner().getEnemiesInRange(owner.getX(), owner.getY(), 300);
        if (enemiesInRange != null) {
            List<Projectile> newFireBalls = new ArrayList<>();
            newFireBalls.add(new FireBall(gameWorld, enemiesInRange.get(random.nextInt(enemiesInRange.size()))));
            for (WeaponMods mod : this.weaponMods) {
                mod.addProjectile(this.gameWorld, newFireBalls);
            }
            return newFireBalls;
        }
        else return List.of();
    }

}
