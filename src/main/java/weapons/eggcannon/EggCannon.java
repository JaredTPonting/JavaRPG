package weapons.eggcannon;

import entities.player.Player;
import projectiles.Projectile;
import projectiles.egg.Egg;
import utils.GameWorld;
import weapons.Weapon;

import java.util.List;

public class EggCannon extends Weapon {
    public EggCannon(Player player, long cooldownMillis, GameWorld gameWorld) {
        super(player, cooldownMillis, gameWorld);

    }

    @Override
    protected List<Projectile> createProjectiles() {
        return List.of(new Egg(this.gameWorld));
    }
}
