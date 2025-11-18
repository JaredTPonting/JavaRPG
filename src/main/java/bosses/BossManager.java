package bosses;

import core.GameWorld;

public class BossManager {
    private final GameWorld world;
    private Boss activeBoss;
    private boolean bossActive;

    public BossManager(GameWorld world) {
        this.world = world;
    }

    public void triggerBossFight(int level) {
        if (bossActive) return;
        bossActive = true;

        // 1. Stop enemy spawns
        world.getEnemySpawner().setActive(false);

        // 2. Make all mobs run away
        world.getEnemySpawner().makeEnemiesFlee();

        // 3. Lock camera
        world.getCamera().freeze(world.getPlayer());

        // 4. Spawn boss
//        activeBoss = BossFactory.createBossForLevel(level, world);
//        world.addEntity(activeBoss);
//
//        // 5. Show health bar
//        world.getUI().showBossHealthBar(activeBoss);
    }

    public void update() {
//        if (bossActive && activeBoss != null && activeBoss.isDead()) {
//            endBossFight();
//        }
    }

    public void endBossFight() {
        bossActive = false;
        world.getCamera().unfreeze();
        world.getEnemySpawner().setActive(true);
//        world.getUI().hideBossHealthBar();
//        world.spawnChest(activeBoss.getX(), activeBoss.getY()); // drop weapon chest
    }

    public boolean isBossActive() {
        return bossActive;
    }
}
