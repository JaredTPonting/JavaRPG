package lingeringzones.chaoszone;

import entities.enemies.Enemy;
import lingeringzones.LingeringZone;
import core.GameWorld;
import utils.Camera;

import java.awt.*;

public class ChaosZone extends LingeringZone {
    public ChaosZone(GameWorld gameWorld, double x, double y, double radius, double lifeSpan) {
        super(gameWorld, x, y, radius, lifeSpan);
    }

    @Override
    public void affectEnemy(Enemy e) {
        e.setSpeedDebuff(0.7);
    }

    @Override
    public void tickAffectEnemy(Enemy e) {
        e.takeDamage(6.0 + (gameWorld.getPlayer().getPlayerStats().getMagicDamage() * 0.5));
    }

    @Override
    public void render(Graphics g, Camera camera) {
        if (!alive) return;
        Color oldColor = g.getColor();
        g.setColor(new Color(255, 0, 255, 80));
        g.fillOval(
                (int) (x - width / 2 - camera.getX()),
                (int) (y - height / 2 - camera.getY()),
                (int) width,
                (int) height
        );
        g.setColor(oldColor);
    }
}
