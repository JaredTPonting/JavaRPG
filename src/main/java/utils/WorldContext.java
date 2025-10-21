package utils;

import entities.enemies.EnemySpawner;
import enviroment.ChunkLoader;
import entities.player.Player;
import lingeringzones.LingeringZoneManager;
import ui.UI;
import weapons.WeaponManager;

public interface WorldContext {
    Player getPlayer();
    EnemySpawner getEnemySpawner();
    ChunkLoader getChunkLoader();
    CollisionChecker getCollisionChecker();
    UI getUi();
    WeaponManager getWeaponManager();
    LingeringZoneManager getLingeringZoneManager();
    int getGameWidth();
    int getGameHeight();
    StateStack getStateStack();
}
