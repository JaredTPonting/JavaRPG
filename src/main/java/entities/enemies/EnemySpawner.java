package entities.enemies;

import entities.player.Player;
import utils.Camera;
import utils.GameWorld;

import java.awt.*;
import java.util.*;
import java.util.List;

public class EnemySpawner {

    public enum EnemyType { FOX, WOLF, FOXMINIBOSS }
    private GameWorld gameWorld;

    private final List<Enemy> enemies = new ArrayList<>();
    private final Random random = new Random();

    private final int SCREEN_WIDTH;
    private final int SCREEN_HEIGHT;

    private long lastSpawnTime = 0;

    // Configurable parameters
    private static final int BASE_MIN_ENEMIES = 40;
    private static final int MAX_MIN_ENEMIES = 1500;
    private static final long BASE_SPAWN_INTERVAL_MS = 100;

    public EnemySpawner(GameWorld gameWorld, int screenWidth, int screenHeight) {
        this.SCREEN_WIDTH = screenWidth;
        this.SCREEN_HEIGHT = screenHeight;
        this.gameWorld = gameWorld;
    }

    public List<Enemy> getEnemies() {
        return new ArrayList<>(enemies);
    }

    // --- Main update loop ---
    public void update(Player player) {
        long now = System.currentTimeMillis();

        // Adjust spawn interval (faster spawns at higher level)
        long spawnInterval = (long) (BASE_SPAWN_INTERVAL_MS / (1 + player.getLevel() * 0.05));

        // Maintain minimum entities.enemies alive
        int minEnemies = calculateMinEnemies(player.getLevel());

        if (enemies.size() < minEnemies && now - lastSpawnTime > spawnInterval) {
            spawnEnemy(player);
            lastSpawnTime = now;
        }

        // Update existing entities.enemies
        for (Enemy e : enemies) {
            e.update();
        }

        // Remove dead entities.enemies & give XP
        enemies.removeIf(enemy -> {
            if (enemy.isDead()) {
                player.gainXP(enemy.getXP());
                return true;
            }
            return false;
        });

        checkEnemyDamage(enemies, player);

        relocateFarEnemies(player);

//        checkForBossEvent(entities.player);
    }

    public void checkEnemyDamage(List<Enemy> enemies, Player player) {
        for (Enemy e : enemies) {
            if (e.isDead()) {
                continue;
            }

            if (gameWorld.getCollisionChecker().checkCollision(e, player)) {
                double damage = e.attackPlayer();
                if (damage > 0) {
                    player.takeDamage(damage);
                }
            }

        }
    }

    // --- Enemy selection logic ---
    private EnemyType selectEnemyType(Player player) {
        int level = player.getLevel();
        int roll = random.nextInt(1000);

        if (roll < 10) {
            return EnemyType.FOXMINIBOSS;
        }

        if (level < 5) {
            return EnemyType.FOX;
        } else if (level < 100) {
            return roll < 700 ? EnemyType.FOX : EnemyType.WOLF;
        } else if (level < 200) {
            return EnemyType.WOLF;
        } else {
            return EnemyType.WOLF;
        }
    }

    private int calculateMinEnemies(int level) {
        return Math.min(BASE_MIN_ENEMIES + level / 2, MAX_MIN_ENEMIES);
    }

    private void spawnEnemy(Player player) {
        EnemyType type = selectEnemyType(player);
        Point spawnPoint = getRandomSpawnPointOutsideCamera(player);

        enemies.add(createEnemy(type, spawnPoint.x, spawnPoint.y, player));
    }

    private Enemy createEnemy(EnemyType type, int x, int y, Player player) {
        return EnemyFactory.create(type.name(), gameWorld, x, y, player);
    }

    private Point getRandomSpawnPointOutsideCamera(Player player) {
        int side = random.nextInt(4);
        int x = 0, y = 0;
        double playerX = player.getX();
        double playerY = player.getY();

        switch (side) {
            case 0 -> { x = (int) playerX + random.nextInt(SCREEN_WIDTH) - (SCREEN_WIDTH / 2); y = (int) playerY - (SCREEN_HEIGHT / 2) - 100; }      // top
            case 1 -> { x = (int) playerX + (SCREEN_WIDTH / 2) + 100; y = (int) playerY + random.nextInt(SCREEN_HEIGHT) - (SCREEN_HEIGHT / 2); } // right
            case 2 -> { x = (int) playerX + random.nextInt(SCREEN_WIDTH) - (SCREEN_WIDTH / 2); y = (int) playerY + (SCREEN_HEIGHT / 2) + 100; } // bottom
            case 3 -> { x = (int) playerX - (SCREEN_WIDTH / 2) - 100; y = (int) playerY + random.nextInt(SCREEN_HEIGHT) - (SCREEN_HEIGHT / 2); }     // left
        }

        return new Point(x, y);
    }

    public Enemy findNearestEnemy(double x, double y, double range) {
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
        double playerX = player.getX();
        double playerY = player.getY();
        double maxDistance = Math.sqrt(((double) (SCREEN_WIDTH * SCREEN_WIDTH) /4) + ((double) (SCREEN_HEIGHT * SCREEN_HEIGHT) /4));

        for (Enemy enemy : enemies) {
            double dx = enemy.getX() - playerX;
            double dy = enemy.getY() - playerY;
            double distanceSq = (dx * dx) + (dy * dy);

            if (distanceSq > (maxDistance * maxDistance)) {
                Point newPos = getRandomSpawnPointOutsideCamera(player);
                enemy.setX(newPos.x);
                enemy.setY(newPos.y);
                enemy.resetVxVy();
            }
        }
    }

    public void render(Graphics g, Camera camera) {
        enemies.forEach(enemy -> enemy.render(g, camera));
    }
}
