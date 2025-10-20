package weapons;

import entities.player.Player;
import projectiles.Projectile;
import utils.Camera;
import utils.Cooldown;
import utils.GameWorld;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public abstract class Weapon {
    public final GameWorld gameWorld;
    protected Player owner;
    protected Cooldown cooldown;
    protected List<Projectile> activeProjectiles = new ArrayList<>();

    public Weapon(Player owner, long cooldownMillis, GameWorld gameWorld) {
        this.owner = owner;
        this.cooldown = new Cooldown(cooldownMillis);
        this.gameWorld = gameWorld;
    }

    public void tryFire() {
        if (cooldown.ready()) {
            System.out.println("FIREING");
            List<Projectile> shots = createProjectiles();
            activeProjectiles.addAll(shots);
            cooldown.reset();
        }
    }

    public void update() {
        tryFire();
        for (Projectile p : activeProjectiles) {
            p.update();
            if (!p.isAlive()) {
                activeProjectiles.remove(p);
            }
        }
    }

    public void render(Graphics g, Camera camera) {
        for (Projectile p : activeProjectiles) {
            p.render(g, camera);
        }
    }

    protected abstract List<Projectile> createProjectiles();
}
