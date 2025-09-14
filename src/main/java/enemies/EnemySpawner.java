package enemies;

import player.Player;
import utils.Camera;

import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class EnemySpawner {
    // wave logic variables
    private int currentWave = 0;
    public enum EnemyType {
        FOX, WOLF, HUNTER
    }
    private final List<List<EnemySpawner.EnemyType>> waves = new ArrayList<>();
    public List<Enemy> enemies;

    private final int SCREEN_WIDTH;
    private final int SCREEN_HEIGHT;

    private final Random random = new Random();

    public EnemySpawner(Player player, int screenWidth, int screenHeight) {
        waves.add(Arrays.asList(EnemySpawner.EnemyType.FOX, EnemySpawner.EnemyType.FOX));
        waves.add(Arrays.asList(EnemySpawner.EnemyType.FOX, EnemySpawner.EnemyType.FOX, EnemySpawner.EnemyType.FOX, EnemySpawner.EnemyType.FOX));
        waves.add(List.of(EnemyType.WOLF));
        waves.add(Arrays.asList(EnemyType.WOLF, EnemyType.FOX, EnemyType.FOX));
        List<EnemyType> nextWave = waves.get(0);
        enemies = new ArrayList<>();

        SCREEN_HEIGHT = screenHeight;
        SCREEN_WIDTH = screenWidth;
    }

    private List<EnemyType> generateWave(int waveNumber) {
        List<EnemyType> waveEnemies = new ArrayList<>();
        int numEnemies = 2 + 2 * waveNumber;
        Random rand  =new Random();

        for (int i = 0; i< numEnemies; i++) {
            int chance = rand.nextInt(100);

            if (waveNumber < 5) {
                waveEnemies.add(EnemyType.FOX);
            } else if (waveNumber < 10) {
                if (chance < 80) waveEnemies.add(EnemyType.FOX);
                else waveEnemies.add(EnemyType.WOLF);
            } else {
                if (chance < 40) waveEnemies.add(EnemyType.FOX);
                else waveEnemies.add(EnemyType.WOLF);
            }
        }

        return waveEnemies;
    }

    public Enemy createEnemy(EnemyType type, int x, int y, Player player) {
        return switch (type) {
            case FOX -> new Fox(x, y, player);
            case WOLF -> new Wolf(x, y, player);
            default -> throw new IllegalArgumentException("Unknown enemy type: " + type);
        };
    }

    public Enemy findNearestEnemy(int x, int y, double range) {
        Enemy nearest = null;
        double nearestDistSq = Double.MAX_VALUE;

        for (Enemy e : enemies) {
            if (e.isDead()) continue;

            double dx = e.getX() - x;
            double dy = e.getY() - y;

            double distSq = dx * dx + dy * dy;

            if (distSq <= range * range && distSq < nearestDistSq) {
                nearestDistSq = distSq;
                nearest = e;
            }
        }
        return nearest;
    }

    public void update(Player player) {
        tryStartNewWave(player);
        for (int i = 0; i < enemies.size(); i++) {
            Enemy enemy = enemies.get(i);
            enemy.update();

            if (enemy.isDead()){
                enemies.remove(i);
                player.gainXP(enemy.getXP());
                i--;
            }
        }
    }

    private void tryStartNewWave(Player player) {
        if (enemies.isEmpty()) {
            List<EnemyType> nextWave = generateWave(currentWave);
            for (EnemyType type : nextWave) {
                Point spawnPoint = getRandomSpawnPointOutsideScreen();
                enemies.add(createEnemy(type, spawnPoint.x, spawnPoint.y, player));
            }
            currentWave++;
            System.out.println("Wave " + currentWave + " started with " + nextWave.size() + " enemies!");
        }
    }

    public void render(Graphics g, Camera camera) {
        for (Enemy enemy : enemies) {
            enemy.render(g, camera);
        }
    }

    private Point getRandomSpawnPointOutsideScreen() {
        int side = random.nextInt(4); // 0=top, 1=right, 2=bottom, 3=left
        int x = 0, y = 0;

        y = switch (side) {
            case 0 -> {
                x = random.nextInt(SCREEN_WIDTH);
                yield -50;
            }
            case 1 -> {
                x = SCREEN_WIDTH + 50;
                yield random.nextInt(SCREEN_HEIGHT);
            }
            case 2 -> {
                x = random.nextInt(SCREEN_WIDTH);
                yield SCREEN_HEIGHT + 50;
            }
            case 3 -> {
                x = -50;
                yield random.nextInt(SCREEN_HEIGHT);
            }
            default -> y;
        };

        return new Point(x, y);
    }

}
