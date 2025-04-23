package enemies;

import player.Player;

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
    private List<List<EnemySpawner.EnemyType>> waves = new ArrayList<>();
    public List<Enemy> enemies;

    private int SCREEN_WIDTH;
    private int SCREEN_HEIGHT;

    private Random random = new Random();

    public EnemySpawner(Player player, int screenWidth, int screenHeight) {
        waves.add(Arrays.asList(EnemySpawner.EnemyType.FOX, EnemySpawner.EnemyType.FOX));
        waves.add(Arrays.asList(EnemySpawner.EnemyType.FOX, EnemySpawner.EnemyType.FOX, EnemySpawner.EnemyType.FOX, EnemySpawner.EnemyType.FOX));
        waves.add(Arrays.asList(EnemySpawner.EnemyType.FOX, EnemySpawner.EnemyType.FOX, EnemySpawner.EnemyType.FOX, EnemySpawner.EnemyType.FOX, EnemySpawner.EnemyType.FOX, EnemySpawner.EnemyType.FOX, EnemySpawner.EnemyType.FOX, EnemySpawner.EnemyType.FOX));
        List<EnemyType> nextWave = waves.get(0);
        enemies = new ArrayList<>();

        SCREEN_HEIGHT = screenHeight;
        SCREEN_WIDTH = screenWidth;
    }

    public Enemy createEnemy(EnemyType type, int x, int y, Player player) {
        switch (type) {
            case FOX:
                return new Fox(x, y, player);
            default:
                throw new IllegalArgumentException("Unknown enemy type: " + type);
        }
    }

    public void update(Player player) {
        tryStartNewWave(player);
        for (int i = 0; i < enemies.size(); i++) {
            Enemy enemy = enemies.get(i);
            enemy.update();

            if (enemy.isDead()){
                enemies.remove(i);
                i--;
            }
        }
    }
    
    private void tryStartNewWave(Player player) {
        if (enemies.isEmpty() && currentWave < waves.size()) {
            List<EnemyType> nextWave = waves.get(currentWave);
            for (EnemyType type : nextWave) {
                Point spawnPoint = getRandomSpawnPointOutsideScreen();
                enemies.add(createEnemy(type, spawnPoint.x, spawnPoint.y, player));
            }
            currentWave += 1;
        }
    }

    public void render(Graphics g) {
        for (Enemy enemy : enemies) {
            enemy.render(g);
        }
    }

    private Point getRandomSpawnPointOutsideScreen() {
        int side = random.nextInt(4); // 0=top, 1=right, 2=bottom, 3=left
        int x = 0, y = 0;

        switch (side) {
            case 0: // Top
                x = random.nextInt(SCREEN_WIDTH);
                y = -50; // just above the screen
                break;
            case 1: // Right
                x = SCREEN_WIDTH + 50;
                y = random.nextInt(SCREEN_HEIGHT);
                break;
            case 2: // Bottom
                x = random.nextInt(SCREEN_WIDTH);
                y = SCREEN_HEIGHT + 50;
                break;
            case 3: // Left
                x = -50;
                y = random.nextInt(SCREEN_HEIGHT);
                break;
        }

        return new Point(x, y);
    }

}
