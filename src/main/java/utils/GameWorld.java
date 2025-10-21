package utils;

import entities.enemies.EnemySpawner;
import enviroment.ChunkLoader;
import entities.player.Player;
import lingeringzones.LingeringZoneManager;
import states.GameState;
import states.MenuState;
import ui.UI;
import weapons.WeaponManager;

import java.awt.*;

public class GameWorld implements WorldContext {

    private Player player;
    private EnemySpawner enemySpawner;
    private ChunkLoader chunkLoader;
    private CollisionChecker collisionChecker;
    private UI ui;
    private final StateStack stateStack;
    private WeaponManager weaponManager;
    private LingeringZoneManager lingeringZoneManager;
    private Point mousePosition = new Point(0, 0);

    // game dims
    private final int gameWidth;
    private final int gameHeight;

    // constructor
    public GameWorld(int gameWidth, int gameHeight) {
        this.gameWidth = gameWidth;
        this.gameHeight = gameHeight;
        this.stateStack = new StateStack();
        this.collisionChecker = new CollisionChecker();

        initWorld();
        stateStack.push(new MenuState(this));
    }

    // Init
    private void initWorld() {
        this.player = new Player(this, gameWidth / 2, gameHeight / 2, 48, 48);
        this.enemySpawner = new EnemySpawner(this, gameWidth, gameHeight);
        this.chunkLoader = new ChunkLoader(player, gameWidth, gameHeight, 1500);
        this.ui = new UI(player);
        this.weaponManager = new WeaponManager();
        this.lingeringZoneManager = new LingeringZoneManager();
    }

    /** Resets core gameplay components while preserving persistent state. */
    public void refresh() {
        initWorld();
    }

    // COre Loops
    public void update() {
        stateStack.update();
    }

    public void render(Graphics g) {
        stateStack.render(g);
    }

    // Getters & Setters
    public int getGameWidth() { return gameWidth; }
    public int getGameHeight() { return gameHeight; }

    public Point getMousePosition() { return this.mousePosition; }
    public void setMousePosition(Point p) {this.mousePosition = p; }

    public StateStack getStateStack() { return stateStack; }
    public GameState getGameState() { return stateStack.peek(); }

    public Player getPlayer() { return player; }
    public EnemySpawner getEnemySpawner() { return enemySpawner; }
    public ChunkLoader getChunkLoader() { return chunkLoader; }
    public CollisionChecker getCollisionChecker() { return collisionChecker; }
    public UI getUi() { return ui; }
    public WeaponManager getWeaponManager() { return weaponManager; }
    public LingeringZoneManager getLingeringZoneManager() { return lingeringZoneManager; }
}
