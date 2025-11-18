package weapons;

import projectiles.Projectile;
import core.GameWorld;

import java.util.List;

public abstract class WeaponMods {
    protected boolean multiple = false;

    public List<Projectile> addProjectile(GameWorld gameWorld, List<Projectile> projectiles){
        return projectiles;
    }

    public List<Projectile> modifyProjectiles(GameWorld gameWorld, List<Projectile> projectiles){
        return projectiles;
    }

    public String getName() {
        return this.getClass().getSimpleName();
    }

    public boolean isMultiple() {
        return multiple;
    }
}
