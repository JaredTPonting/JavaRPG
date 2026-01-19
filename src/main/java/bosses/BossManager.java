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

        // Make all enemies flee and stop spawning (makeEnemiesFlee also disables spawner)
        world.getEnemySpawner().makeEnemiesFlee();

        // Lock camera to current position
        world.getCamera().freeze(world.getPlayer());

        // TODO: Spawn boss entity
        // activeBoss = BossFactory.createBossForLevel(level, world);
    }

    public void update() {
        if (!bossActive) return;

        // TODO: Check if boss is dead
        // if (activeBoss != null && activeBoss.isDead()) {
        //     endBossFight();
        // }
    }

    public void endBossFight() {
        if (!bossActive) return;
        bossActive = false;

        world.getCamera().unfreeze();
        world.getEnemySpawner().setActive(true);

        // TODO: Drop loot, show victory UI, etc.
    }

    public boolean isBossActive() {
        return bossActive;
    }
}
