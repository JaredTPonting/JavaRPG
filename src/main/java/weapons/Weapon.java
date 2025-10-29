package weapons;

import entities.Entity;
import projectiles.Projectile;
import utils.Camera;
import utils.Cooldown;
import core.GameWorld;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public abstract class Weapon {
    public final GameWorld gameWorld;
    protected Entity owner;
    protected Cooldown cooldown;
    protected List<Projectile> activeProjectiles = new ArrayList<>();
    public List<WeaponMods> weaponMods;

    public Weapon(Entity owner, long cooldownMillis, GameWorld gameWorld) {
        this.owner = owner;
        this.cooldown = new Cooldown(cooldownMillis);
        this.gameWorld = gameWorld;
        this.weaponMods = new ArrayList<>();
    }

    public void tryFire() {
        if (cooldown.ready()) {
            List<Projectile> shots = createProjectiles();
            activeProjectiles.addAll(shots);
            cooldown.reset();
        }
    }

    public void update(double dt) {
        this.cooldown.update(dt);
        tryFire();
        List<Projectile> toRemove = new ArrayList<>();
        for (Projectile p : activeProjectiles) {
            p.update(dt);
            if (!p.isAlive()) {
                toRemove.add(p);
            }
        }
        activeProjectiles.removeAll(toRemove);
    }

    public abstract List<Class<? extends WeaponMods>> getAvailableModClasses();

    public void addWeaponMod(WeaponMods mod){
        this.weaponMods.add(mod);
    }

    public void render(Graphics g, Camera camera) {
        for (Projectile p : activeProjectiles) {
            p.render(g, camera);
        }
    }

    public String getName() {
        return this.getClass().getSimpleName();
    }

    protected abstract List<Projectile> createProjectiles();
}
