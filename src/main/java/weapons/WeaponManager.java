package weapons;

import utils.Camera;

import java.awt.*;
import java.util.ArrayList;

public class WeaponManager {
    ArrayList<Weapon> weapons = new ArrayList<>();

    public void addWeapon(Weapon weapon) {
        weapons.add(weapon);
    }

    public Weapon getWeaponByName(String name) {
        for (Weapon weapon : weapons) {
            if (weapon.getName().equalsIgnoreCase(name)) {
                return weapon;
            }
        }
        return null;
    }

    public void addWeaponMod(String name, WeaponMods mod) {
        Weapon weapon = getWeaponByName(name);
        weapon.addWeaponMod(mod);
    }

    public void update(double dt) {
        for (Weapon weapon : weapons) {
            weapon.update(dt);
        }
    }

    public void render(Graphics g, Camera camera) {
        for (Weapon weapon : weapons) {
            weapon.render(g, camera);
        }
    }
}
