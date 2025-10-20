package utils;

import ammo.AmmoHandler;
import entities.enemies.EnemySpawner;
import enviroment.ChunkLoader;
import entities.player.Player;
import states.GameState;
import ui.UI;
import states.MenuState;

import java.awt.*;

public class GameWorld {
    public Player player;
    public AmmoHandler ammoHandler;
    public EnemySpawner enemySpawner;
    public ChunkLoader chunkLoader;
    public CollisionChecker collisionChecker;
    public UI ui;
    private final StateStack stateStack;
    int gameWidth;
    int gameHeight;

    public GameWorld(int gameWidth, int gameHeight) {
        this.player = new Player(this,gameWidth / 2, gameHeight / 2, 48, 48);
        this.ammoHandler = new AmmoHandler(gameWidth, gameHeight);
        this.enemySpawner = new EnemySpawner(this, gameWidth, gameHeight);
        this.chunkLoader = new ChunkLoader(this.player, gameWidth, gameHeight, 1500);
        this.ui = new UI(this.player);
        this.stateStack = new StateStack();
        this.gameHeight = gameHeight;
        this.gameWidth = gameWidth;
        this.collisionChecker = new CollisionChecker();
        stateStack.push(new MenuState(this));
    }

    public int getGameWidth() {
        return this.gameWidth;
    }

    public int getGameHeight() {
        return this.gameHeight;
    }

    public void update() {
        stateStack.update();
    }

    public void render(Graphics g) {
        stateStack.render(g);
    }

    public StateStack getStateStack() {
        return stateStack;
    }

    public GameState getGameState() {
        return stateStack.peek();
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

    public CollisionChecker getCollisionChecker() {
        return this.collisionChecker;
    }

    public UI getUi() {
        return ui;
    }

}
