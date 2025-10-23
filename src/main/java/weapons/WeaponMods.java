package weapons;

import projectiles.Projectile;
import core.GameWorld;

import java.util.List;

public abstract class WeaponMods {
    public abstract List<Projectile> addProjectile(GameWorld gameWorld, List projectiles);
}
