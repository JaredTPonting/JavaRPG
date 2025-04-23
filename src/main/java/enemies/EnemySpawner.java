package enemies;

import player.Player;

import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class EnemySpawner {
    // wave logic variables
    private int currentWave = 1;
    public static enum EnemyType {
        FOX, WOLF, HUNTER
    }
    private List<List<EnemySpawner.EnemyType>> waves = new ArrayList<>();
    public List<Enemy> enemies;

    public EnemySpawner(Player player) {
        waves.add(Arrays.asList(EnemySpawner.EnemyType.FOX, EnemySpawner.EnemyType.FOX));
        waves.add(Arrays.asList(EnemySpawner.EnemyType.FOX, EnemySpawner.EnemyType.FOX, EnemySpawner.EnemyType.FOX, EnemySpawner.EnemyType.FOX));
        waves.add(Arrays.asList(EnemySpawner.EnemyType.FOX, EnemySpawner.EnemyType.FOX, EnemySpawner.EnemyType.FOX, EnemySpawner.EnemyType.FOX, EnemySpawner.EnemyType.FOX, EnemySpawner.EnemyType.FOX, EnemySpawner.EnemyType.FOX, EnemySpawner.EnemyType.FOX));
        List<EnemyType> nextWave = waves.get(0);
        enemies = new ArrayList<>();
        for (EnemyType type : nextWave) {
            enemies.add(createEnemy(type, 100, 100, player));
        }
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
        for (int i = 0; i < enemies.size(); i++) {
            Enemy enemy = enemies.get(i);
            enemy.update();

            if (enemy.isDead()){
                enemies.remove(i);
                i--;
            }
        }
        tryStartNewWave(player);
    }
    
    private void tryStartNewWave(Player player) {
        if (enemies.isEmpty() && currentWave < waves.size()) {
            List<EnemyType> nextWave = waves.get(currentWave);
            for (EnemyType type : nextWave) {
                enemies.add(createEnemy(type, 100, 100, player));
            }
            currentWave += 1;
        }
    }

    public void render(Graphics g) {
        for (Enemy enemy : enemies) {
            enemy.render(g);
        }
    }




}
