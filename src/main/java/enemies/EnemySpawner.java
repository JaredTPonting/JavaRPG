package enemies;

import player.Player;
import utils.Camera;

import java.awt.*;
import java.util.*;
import java.util.List;

public class EnemySpawner {

    public enum EnemyType { FOX, WOLF, HUNTER }

    private final List<Enemy> enemies = new ArrayList<>();
    private final Random random = new Random();

    private final int SCREEN_WIDTH;
    private final int SCREEN_HEIGHT;

    private long lastSpawnTime = 0;

    // Configurable parameters
    private static final int BASE_MIN_ENEMIES = 40;
    private static final int MAX_MIN_ENEMIES = 1500;
    private static final long BASE_SPAWN_INTERVAL_MS = 100;

    public EnemySpawner(int screenWidth, int screenHeight) {
        this.SCREEN_WIDTH = screenWidth;
        this.SCREEN_HEIGHT = screenHeight;
    }

    public List<Enemy> getEnemies() {
        return new ArrayList<>(enemies);
    }

    // --- Main update loop ---
    public void update(Player player) {
        long now = System.currentTimeMillis();

        // Adjust spawn interval (faster spawns at higher level)
        long spawnInterval = (long) (BASE_SPAWN_INTERVAL_MS / (1 + player.getLevel() * 0.05));

        // Maintain minimum enemies alive
        int minEnemies = calculateMinEnemies(player.getLevel());

        if (enemies.size() < minEnemies && now - lastSpawnTime > spawnInterval) {
            spawnEnemy(player);
            lastSpawnTime = now;
        }

        // Update existing enemies
        enemies.forEach(Enemy::update);

        // Remove dead enemies & give XP
        enemies.removeIf(enemy -> {
            if (enemy.isDead()) {
                player.gainXP(enemy.getXP());
                return true;
            }
            return false;
        });

        relocateFarEnemies(player);

//        checkForBossEvent(player);
    }

    // --- Enemy selection logic ---
    private EnemyType selectEnemyType(Player player) {
        int level = player.getLevel();
        int roll = random.nextInt(100);

        if (level < 5) {
            return EnemyType.FOX;
        } else if (level < 10) {
            return roll < 70 ? EnemyType.FOX : EnemyType.WOLF;
        } else if (level < 20) {
            return roll < 50 ? EnemyType.WOLF : EnemyType.HUNTER;
        } else {
            return roll < 20 ? EnemyType.WOLF : EnemyType.HUNTER;
        }
    }

    private int calculateMinEnemies(int level) {
        // Linearly scale from base up to max
        return Math.min(BASE_MIN_ENEMIES + level / 2, MAX_MIN_ENEMIES);
    }

    private void spawnEnemy(Player player) {
        EnemyType type = selectEnemyType(player);
        Point spawnPoint = getRandomSpawnPointOutsideCamera(player);

        enemies.add(createEnemy(type, spawnPoint.x, spawnPoint.y, player));
        System.out.println("Spawned " + type + " at " + spawnPoint);
    }

    private Enemy createEnemy(EnemyType type, int x, int y, Player player) {
        return switch (type) {
            case FOX, HUNTER -> new Fox(x, y, player);
            case WOLF -> new Wolf(x, y, player);
        };
    }

    private Point getRandomSpawnPointOutsideCamera(Player player) {
        int side = random.nextInt(4);
        int x = 0, y = 0;
        int playerX = player.getX();
        int playerY = player.getY();

        switch (side) {
            case 0 -> { x = playerX + random.nextInt(SCREEN_WIDTH) - (SCREEN_WIDTH / 2); y = playerY - (SCREEN_HEIGHT / 2) - 100; }      // top
            case 1 -> { x = playerX + (SCREEN_WIDTH / 2) + 100; y = playerY + random.nextInt(SCREEN_HEIGHT) - (SCREEN_HEIGHT / 2); } // right
            case 2 -> { x = playerX + random.nextInt(SCREEN_WIDTH) - (SCREEN_WIDTH / 2); y = playerY + (SCREEN_HEIGHT / 2) + 100; } // bottom
            case 3 -> { x = playerX - (SCREEN_WIDTH / 2) - 100; y = playerY + random.nextInt(SCREEN_HEIGHT) - (SCREEN_HEIGHT / 2); }     // left
        }

        return new Point(x, y);
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

    private void relocateFarEnemies(Player player) {
        int playerX = player.getX();
        int playerY = player.getY();
        double maxDistance = Math.sqrt(((double) (SCREEN_WIDTH * SCREEN_WIDTH) /4) + ((double) (SCREEN_HEIGHT * SCREEN_HEIGHT) /4));

        for (Enemy enemy : enemies) {
            double dx = enemy.getX() - playerX;
            double dy = enemy.getY() - playerY;
            double distanceSq = (dx * dx) + (dy * dy);

            if (distanceSq > (maxDistance * maxDistance)) {
                Point newPos = getRandomSpawnPointOutsideCamera(player);
                enemy.setX(newPos.x);
                enemy.setY(newPos.y);
                System.out.println("ENEMY RELOCATED TO: " + newPos);
            }
        }
    }

    // --- Placeholder for future systems ---
//    private void checkForBossEvent(Player player) {
//        // Example: every 10 levels, spawn a mini-boss
//        if (player.getLevel() % 10 == 0 && !isBossPresent()) {
//            spawnBoss(player);
//        }
//    }

//    private boolean isBossPresent() {
//        return enemies.stream().anyMatch(e -> e instanceof BossEnemy);
//    }
//
//    private void spawnBoss(Player player) {
//        Point spawn = getRandomSpawnPointOutsideCamera();
//        enemies.add(new BossEnemy(spawn.x, spawn.y, player));
//        System.out.println("🔥 Boss spawned!");
//    }

    public void render(Graphics g, Camera camera) {
        enemies.forEach(enemy -> enemy.render(g, camera));
    }
}
