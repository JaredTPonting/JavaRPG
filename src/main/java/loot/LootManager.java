package loot;

import utils.Camera;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class LootManager {
    private List<Chest> chests;

    public LootManager() {
        this.chests = new ArrayList<>();
    }

    public void update(double dt) {
        List<Chest> toRemove = new ArrayList<>();
        for (Chest chest : chests) {
            chest.update(dt);
            if (!chest.isActive()){
                toRemove.add(chest);
            }
        }
        this.chests.removeAll(toRemove);
    }

    public void render(Graphics g, Camera c) {
        for (Chest chest : chests) {
            chest.render(g, c);
        }
    }

    public List<Chest> getChests() {return this.chests; }

    public void addChest(Chest chest){
        this.chests.add(chest);
    }

}
