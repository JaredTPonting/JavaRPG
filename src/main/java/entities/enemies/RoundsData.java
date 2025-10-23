package entities.enemies;

import java.util.List;

public class RoundsData {
    private int id;
    private List<String> enemies;
    private double spawnRate = 1.0;
    private double bossChance = 0.0;

    // Getters & Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public List<String> getEnemies() { return enemies; }
    public void setEnemies(List<String> enemies) { this.enemies = enemies; }

    public double getSpawnRate() { return spawnRate; }
    public void setSpawnRate(double spawnRate) { this.spawnRate = spawnRate; }

    public double getBossChance() { return bossChance; }
    public void setBossChance(double bossChance) { this.bossChance = bossChance; }

    @Override
    public String toString() {
        return "RoundsData{id=" + id +
                ", enemies=" + enemies +
                ", spawnRate=" + spawnRate +
                ", bossChance=" + bossChance + "}";
    }
}
