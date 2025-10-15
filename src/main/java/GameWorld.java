import ammo.AmmoHandler;
import enemies.EnemySpawner;
import enviroment.ChunkLoader;
import player.Player;

public class GameWorld {
    public Player player;
    public AmmoHandler ammoHandler;
    public EnemySpawner enemySpawner;
    public ChunkLoader chunkLoader;

    public GameWorld(Player player, AmmoHandler ammoHandler, EnemySpawner enemySpawner, ChunkLoader chunkLoader) {
        this.player = player;
        this.ammoHandler = ammoHandler;
        this.enemySpawner = enemySpawner;
        this.chunkLoader = chunkLoader;
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


}
