package ammo;

import utils.Camera;

import java.awt.*;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.List;

public class AmmoHandler {
    private final List<Projectile> projectiles;
    private Class<? extends Projectile> currentAmmoType;
    private final int mapWidth;
    private final int mapHeight;
    private double speedMultiplier = 2.0; // can upgrade
    private long shootCooldown = 500; // can upgrade
    private double damageMultiplier = 1.0; // can upgrade
    private long lastShotTime = 0;

    public AmmoHandler(int mapWidth, int mapHeight) {
        this.mapWidth = mapWidth;
        this.mapHeight = mapHeight;
        this.projectiles = new CopyOnWriteArrayList<>();
        this.currentAmmoType = EggProjectile.class; // default
    }

    public void update() {
        for (Projectile p : projectiles) {
            p.update();
        }
        projectiles.removeIf(p -> !p.isActive());
    }

    public void render(Graphics g, Camera camera) {
        for (Projectile p : projectiles) {
            p.render(g, camera);
        }
    }

    public boolean canShoot() {
        long now = System.currentTimeMillis();
        if (now - lastShotTime >= shootCooldown) {
            lastShotTime = now;  // reset cooldown
            return true;
        }
        return false;
    }

    public void fire(double x, double y, double dirX, double dirY) {
        try {
            Projectile p = currentAmmoType
                    .getDeclaredConstructor(double.class, double.class, double.class, double.class, int.class, int.class, double.class)
                    .newInstance(x, y, dirX, dirY, mapWidth, mapHeight, speedMultiplier);
            projectiles.add(p);
        } catch (Exception e) {
            e.printStackTrace(); // handle bad constructors etc
        }
    }

    public void setAmmo(Class<? extends Projectile> ammoType) {
        this.currentAmmoType = ammoType;
    }

    public List<Projectile> getProjectiles() {
        return projectiles;
    }

    public void increaseMulitplier(double multiplier) {
        this.speedMultiplier *= multiplier;
    }

    public double getDamage() {
        if (projectiles.isEmpty()) return 0;
        Projectile last = projectiles.get(projectiles.size() - 1);
        return last.getDamage() * damageMultiplier;
    }
}
