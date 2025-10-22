package weapons;

import entities.player.Player;
import projectiles.Projectile;
import utils.GameWorld;

import java.util.List;

public abstract class WeaponMods {
    public abstract List<Projectile> addProjectile(GameWorld gameWorld, List projectiles);
}
