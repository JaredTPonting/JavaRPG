package lingeringzones.chaoszone;

import entities.enemies.Enemy;
import lingeringzones.LingeringZone;
import utils.GameWorld;

public class ChaosZone extends LingeringZone {
    public ChaosZone(GameWorld gameWorld, double x, double y, double radius, long lifeSpan) {
        super(gameWorld, x, y, radius, lifeSpan);
    }

    @Override
    public void affectEnemy(Enemy e) {
        e.takeDamage(60.0 + (gameWorld.getPlayer().getPlayerStats().getMagicDamage() * 0.5));
        e.setSpeedDebuff(0.1);
    }
}
