package lingeringzones;

import utils.Camera;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class LingeringZoneManager {
    ArrayList<LingeringZone> lingeringZones = new ArrayList<>();

    public void addLingeringZone(LingeringZone zone) {
        lingeringZones.add(zone);
    }

    public void update(double dt) {
        List<LingeringZone> toRemove = new ArrayList<>();
        for (LingeringZone z : lingeringZones) {
            z.update(dt);
            if (!z.isAlive()) {
                toRemove.add(z);
            }
        }
        lingeringZones.removeAll(toRemove);
    }

    public void render(Graphics g, Camera camera) {
        for (LingeringZone z : lingeringZones){
            z.render(g, camera);
        }
    }

}
