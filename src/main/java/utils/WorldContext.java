package utils;

import entities.enemies.EnemySpawner;
import entities.player.Player;
import lingeringzones.LingeringZoneManager;
import loot.LootManager;
import utils.CollisionChecker;
import utils.DeltaTimer;
import weapons.WeaponManager;

import java.awt.*;

public interface WorldContext {
    int getGameWidth();
    int getGameHeight();

    DeltaTimer getDeltaTimer();
    CollisionChecker getCollisionChecker();

    LootManager getLootManager();
    WeaponManager getWeaponManager();
    LingeringZoneManager getLingeringZoneManager();

    Player getPlayer();  // optional but convenient

    EnemySpawner getEnemySpawner();

    boolean isDebugMode();

    StateStack getStateStack();
}
