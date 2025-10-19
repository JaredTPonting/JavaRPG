package utils;

import ammo.AmmoHandler;
import enemies.EnemySpawner;
import enviroment.ChunkLoader;
import player.Player;
import states.GameState;
import ui.UI;
import states.MenuState;

import java.awt.*;

public class GameWorld {
    public Player player;
    public AmmoHandler ammoHandler;
    public EnemySpawner enemySpawner;
    public ChunkLoader chunkLoader;
    public UI ui;
    private StateStack stateStack;
    int gameWidth;
    int gameHeight;

    public GameWorld(int gameWidth, int gameHeight) {
        this.player = new Player(gameWidth / 2, gameHeight / 2, gameWidth, gameHeight);
        this.ammoHandler = new AmmoHandler(gameWidth, gameHeight);
        this.enemySpawner = new EnemySpawner(gameWidth, gameHeight);
        this.chunkLoader = new ChunkLoader(this.player, gameWidth, gameHeight, 1500);
        this.ui = new UI(this.player);
        this.stateStack = new StateStack();
        this.gameHeight = gameHeight;
        this.gameWidth = gameWidth;
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

    public UI getUi() {
        return ui;
    }

}
