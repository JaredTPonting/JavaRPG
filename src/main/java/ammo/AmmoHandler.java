package ammo;

import java.awt.*;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.List;

public class AmmoHandler {
    private final List<Projectile> projectiles;
    private Class<? extends Projectile> currentAmmoType;
    private final int screenWidth;
    private final int screenHeight;
    private double speedMultiplier = 1.0;

    public AmmoHandler(int screenWidth, int screenHeight) {
        this.screenWidth = screenWidth;
        this.screenHeight = screenHeight;
        this.projectiles = new CopyOnWriteArrayList<>();
        this.currentAmmoType = EggProjectile.class; // default
    }

    public void update() {
        for (Projectile p : projectiles) {
            p.update();
        }
        projectiles.removeIf(p -> !p.isActive());
    }

    public void render(Graphics g) {
        for (Projectile p : projectiles) {
            p.render(g);
        }
    }

    public void fire(double x, double y, double targetX, double targetY) {
        try {
            Projectile p = currentAmmoType
                    .getDeclaredConstructor(double.class, double.class, double.class, double.class, int.class, int.class, double.class)
                    .newInstance(x, y, targetX, targetY, screenWidth, screenHeight, speedMultiplier);
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
}
