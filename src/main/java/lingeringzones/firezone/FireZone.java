package lingeringzones.firezone;

import entities.enemies.Enemy;
import lingeringzones.LingeringZone;
import core.GameWorld;
import utils.Camera;

import java.awt.*;

public class FireZone extends LingeringZone {
    public FireZone(GameWorld gameWorld, double x, double y, double radius, double lifeSpan) {
        super(gameWorld, x, y, radius, lifeSpan);
    }

    @Override
    public void affectEnemy(Enemy e) {
        e.takeDamage(80.0 + (gameWorld.getPlayer().getPlayerStats().getMagicDamage()));
    }

    @Override
    public void tickAffectEnemy(Enemy e) {

    }

    @Override
    public void render(Graphics g, Camera camera) {
        if (!alive) return;
        Color oldColor = g.getColor();
        g.setColor(new Color(255, 0, 0, 80));
        g.fillOval(
                (int) (x - width / 2 - camera.getX()),
                (int) (y - height / 2 - camera.getY()),
                (int) width,
                (int) height
        );
        g.setColor(oldColor);
    }
}
