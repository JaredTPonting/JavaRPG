package weapons;

import utils.Camera;

import java.awt.*;
import java.util.ArrayList;

public class WeaponManager {
    ArrayList<Weapon> weapons = new ArrayList<>();

    public void addWeapon(Weapon weapon) {
        weapons.add(weapon);
    }

    public void update() {
        for (Weapon weapon : weapons) {
            weapon.update();
        }
    }

    public void render(Graphics g, Camera camera) {
        for (Weapon weapon : weapons) {
            weapon.render(g, camera);
        }
    }
}
