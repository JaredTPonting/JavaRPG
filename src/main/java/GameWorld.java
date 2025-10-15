import ammo.AmmoHandler;
import enemies.EnemySpawner;
import enviroment.ChunkLoader;
import player.Player;
import ui.UI;

public class GameWorld {
    public Player player;
    public AmmoHandler ammoHandler;
    public EnemySpawner enemySpawner;
    public ChunkLoader chunkLoader;
    public UI ui;

    public GameWorld(Player player, AmmoHandler ammoHandler, EnemySpawner enemySpawner, ChunkLoader chunkLoader, UI ui) {
        this.player = player;
        this.ammoHandler = ammoHandler;
        this.enemySpawner = enemySpawner;
        this.chunkLoader = chunkLoader;
        this.ui = ui;
    }

    public Player getPlayer() {
        return this.player;
    }

    public AmmoHandler getAmmoHandler() {
        return this.ammoHandler;
    }

    public EnemySpawner getEnemySpawner() {
        return this.enemySpawner;
    }

    public ChunkLoader getChunkLoader() {
        return this.chunkLoader;
    }

    public UI getUi() {
        return ui;
    }
}
