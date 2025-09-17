package enemies;

import player.Player;
import utils.Camera;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class EnemySpawner {

    // --- Wave and enemy tracking ---
    private int currentWave = 0;
    public enum EnemyType { FOX, WOLF, HUNTER }
    private final List<Enemy> enemies = new ArrayList<>();

    private final int SCREEN_WIDTH;
    private final int SCREEN_HEIGHT;
    private final Random random = new Random();

    public EnemySpawner(int screenWidth, int screenHeight) {
        this.SCREEN_WIDTH = screenWidth;
        this.SCREEN_HEIGHT = screenHeight;
    }

    // --- Getter methods ---
    public List<Enemy> getEnemies() {
        return new ArrayList<>(enemies); // return copy to avoid external modification
    }

    public int getCurrentWave() {
        return currentWave;
    }

    public int getScreenWidth() {
        return SCREEN_WIDTH;
    }

    public int getScreenHeight() {
        return SCREEN_HEIGHT;
    }

    // --- Wave generation ---
    private List<EnemyType> generateWave(int waveNumber) {
        List<EnemyType> waveEnemies = new ArrayList<>();
        int numEnemies = 2 + 2 * waveNumber;

        for (int i = 0; i < numEnemies; i++) {
            int chance = random.nextInt(100);
            if (waveNumber < 5) {
                waveEnemies.add(EnemyType.FOX);
            } else if (waveNumber < 10) {
                waveEnemies.add(chance < 80 ? EnemyType.FOX : EnemyType.WOLF);
            } else {
                waveEnemies.add(chance < 40 ? EnemyType.FOX : EnemyType.WOLF);
            }
        }

        return waveEnemies;
    }

    private Enemy createEnemy(EnemyType type, int x, int y, Player player) {
        return switch (type) {
            case FOX -> new Fox(x, y, player);
            case WOLF -> new Wolf(x, y, player);
            default -> throw new IllegalArgumentException("Unknown enemy type: " + type);
        };
    }

    private Point getRandomSpawnPointOutsideScreen() {
        int side = random.nextInt(4); // 0=top, 1=right, 2=bottom, 3=left
        int x = 0, y = 0;

        switch (side) {
            case 0 -> { x = random.nextInt(SCREEN_WIDTH); y = -50; } // top
            case 1 -> { x = SCREEN_WIDTH + 50; y = random.nextInt(SCREEN_HEIGHT); } // right
            case 2 -> { x = random.nextInt(SCREEN_WIDTH); y = SCREEN_HEIGHT + 50; } // bottom
            case 3 -> { x = -50; y = random.nextInt(SCREEN_HEIGHT); } // left
        }

        return new Point(x, y);
    }

    // --- Gameplay ---
    public void update(Player player) {
        tryStartNewWave(player);
        enemies.forEach(Enemy::update);
        enemies.removeIf(enemy -> {
            if (enemy.isDead()) {
                player.gainXP(enemy.getXP());
                return true;
            }
            return false;
        });
    }

    private void tryStartNewWave(Player player) {
        if (!enemies.isEmpty()) return;

        List<EnemyType> nextWave = generateWave(currentWave);
        for (EnemyType type : nextWave) {
            Point spawn = getRandomSpawnPointOutsideScreen();
            enemies.add(createEnemy(type, spawn.x, spawn.y, player));
        }
        currentWave++;
        System.out.println("Wave " + currentWave + " started with " + nextWave.size() + " enemies!");
    }

    public Enemy findNearestEnemy(int x, int y, double range) {
        Enemy nearest = null;
        double nearestDistSq = range * range;

        for (Enemy e : enemies) {
            if (e.isDead()) continue;

            double dx = e.getX() - x;
            double dy = e.getY() - y;
            double distSq = dx * dx + dy * dy;

            if (distSq < nearestDistSq) {
                nearestDistSq = distSq;
                nearest = e;
            }
        }

        return nearest;
    }

    public void render(Graphics g, Camera camera) {
        enemies.forEach(enemy -> enemy.render(g, camera));
    }
}
