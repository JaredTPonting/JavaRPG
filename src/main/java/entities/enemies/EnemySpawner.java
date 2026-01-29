package entities.enemies;

import entities.Entity;
import entities.player.Player;
import entities.player.PlayerManager;
import utils.Camera;
import core.GameWorld;
import utils.DamageIndicatorManager;

import java.awt.*;
import java.util.*;
import java.util.List;

public class EnemySpawner {

    public enum EnemyType {
        MUSHROOM,
        BASICGREENSLIME,
        R1WOLF,
        R1GOBLIN,
        BOMBSLIME,
        FIRESLIME,
        PURPLESLIME,
        BASICGREENGOBLIN,
        BASICYELLOWGOBLIN,
        BASICDARKGREENGOBLIN,
        BOSSGOBLIN,
        GREENORC,
        BLUEORC,
        BOSSORC
    }
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

    // Damage indicator
    private DamageIndicatorManager damageIndicators;

    // Round manager
    private RoundManager roundManager;
    private boolean active = false;  // Starts inactive until weapon is selected
    public void setActive(boolean newActive) { this.active = newActive;}
    public boolean isActive() { return this.active; }

    public EnemySpawner(GameWorld gameWorld, int screenWidth, int screenHeight) {
        this.SCREEN_WIDTH = screenWidth;
        this.SCREEN_HEIGHT = screenHeight;
        this.gameWorld = gameWorld;
        this.damageIndicators = new DamageIndicatorManager();
        this.roundManager = new RoundManager();
    }

    public List<Enemy> getEnemies() {
        return enemies;  // Return direct reference to avoid allocation
    }

    // --- Main update loop ---
    public void update(Player player, double dt) {
        long now = System.currentTimeMillis();
        PlayerManager playerManager = player.getPlayerManager();
        if (playerManager.getLevel() < 20 && playerManager.getLevel() >= 10 && roundManager.getCurrentRound() != 2) {
            roundManager.setCurrentRound(2);
        }
        if (playerManager.getLevel() < 30 && playerManager.getLevel() >= 20 && roundManager.getCurrentRound() != 3) {
            roundManager.setCurrentRound(3);
        }

        // Adjust spawn interval (faster spawns at higher level)
        long spawnInterval = (long) (BASE_SPAWN_INTERVAL_MS / (1 + playerManager.getLevel() * 0.05));

        // Maintain minimum entities.enemies alive
        int minEnemies = calculateMinEnemies(playerManager.getLevel());

        if (enemies.size() < minEnemies && now - lastSpawnTime > spawnInterval && active) {
            spawnEnemy(player);
            lastSpawnTime = now;
        }

        // Update existing entities.enemies
        for (Enemy e : enemies) {
            e.update(dt);
        }

        // Remove dead enemies (XP is awarded immediately in Enemy.startDeath())
        enemies.removeIf(enemy -> {
            if (enemy.isDead() || enemy.despawned()) {
                // Remove from collision grid before removing from list
                if (enemy.getCurrentGridCell() != null) {
                    gameWorld.getCollisionGrid().remove(enemy, enemy.getCurrentGridCell());
                }
                return true;
            }
            return false;
        });

        relocateFarEnemies(player);
        this.damageIndicators.update(dt);
    }

    public void checkEnemyDamage(List<Enemy> enemies, Player player) {
        for (Enemy e : enemies) {
            if (e.isDead() || e.isTriggeredDeath()) {
                continue;
            }

            if (gameWorld.getCollisionChecker().checkCollision(e, player)) {
                double damage = e.attackPlayer();
                if (damage > 0 && !player.isInvulnerable()) {
                    player.takeDamage(damage);
                }
            }

        }
    }

    public void makeEnemiesFlee() {
        for (Enemy e : enemies) {
            e.flee();
        }
        setActive(false);
    }

    // --- Enemy selection logic ---
    private EnemyType selectEnemyType(Player player) {
        RoundsData round = roundManager.getCurrentRoundData();
        if (roundManager.shouldSpawnMiniBoss(random)) {
            List<String> pool = round.getBosses();
            String chosen = pool.get(random.nextInt(pool.size()));
            return EnemyType.valueOf(chosen);
        }

        List<String> pool = round.getEnemies();
        String chosen = pool.get(random.nextInt(pool.size()));

        return EnemyType.valueOf(chosen);
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
                enemy.updateHitBox();
                enemy.updateGridPosition(gameWorld.getCollisionGrid());
            }
        }
    }

    public Entity getRandomEnemy() {
        return this.enemies.get(random.nextInt(enemies.size()));
    }

    public List<Entity> getEnemiesInRange(double x, double y, int range) {
        List<Entity> enemiesInRange = new ArrayList<Entity>();
        for (Entity e : enemies) {
            double distX = (e.getX() - x) * (e.getX() - x);
            double distY = (e.getY() - y) * (e.getY() - y);
            if ((distX + distY) <= (range * range)) {
                enemiesInRange.add(e);
            }
        }
        if (enemiesInRange.isEmpty()) {
            return null;
        }
        return enemiesInRange;
    }

    public Entity getRandomEnemyInRange(double x, double y, int range) {
        List<Entity> enemiesInRange = new ArrayList<Entity>();
        for (Entity e : enemies) {
            double distX = (e.getX() - x) * (e.getX() - x);
            double distY = (e.getY() - y) * (e.getY() - y);
            if ((distX + distY) <= (range * range)) {
                enemiesInRange.add(e);
            }
        }
        if (enemiesInRange.isEmpty()) {
            return null;
        }
        return enemiesInRange.get(random.nextInt(enemiesInRange.size()));
    }

    public DamageIndicatorManager getDamageIndicators() {
        return this.damageIndicators;
    }

    public void renderDamageIndicators(Graphics g, Camera c) {
        this.damageIndicators.render(g, c);
    }

    public void render(Graphics g, Camera camera) {
        enemies.forEach(enemy -> enemy.render(g, camera));
        this.damageIndicators.render(g, camera);
    }
}
