package utils;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class DamageIndicatorManager {
    private List<DamageIndicator> damageIndicators;

    public DamageIndicatorManager() {
        this.damageIndicators = new ArrayList<DamageIndicator>();
    }

    public void addIndicator(int damage, int x, int y) {
        this.damageIndicators.add(new DamageIndicator(damage, x, y));
    }

    public void update() {
        List<DamageIndicator> toRemove = new ArrayList<DamageIndicator>();
        for (DamageIndicator I: this.damageIndicators) {
            I.update();
            if (I.isFinished()) {
                toRemove.add(I);
            }
        }
        this.damageIndicators.removeAll(toRemove);
    }

    public void render(Graphics g, Camera camera) {
        for (DamageIndicator I : this.damageIndicators) {
            I.render(g, camera);
        }
    }
}
