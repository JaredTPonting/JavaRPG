package core;

import bosses.BossManager;
import entities.enemies.EnemySpawner;
import environment.ChunkLoader;
import entities.player.Player;
import lingeringzones.LingeringZoneManager;
import loot.Chest;
import loot.LootManager;
import projectiles.egg.Egg;
import states.GameState;
import states.MenuState;
import ui.UI;
import utils.*;
import weapons.WeaponManager;

import java.awt.*;

public class GameWorld implements WorldContext {

    private final Game game;

    private Player player;
    private BossManager bossManager;
    private EnemySpawner enemySpawner;
    private ChunkLoader chunkLoader;
    private LootManager lootManager;
    private final CollisionChecker collisionChecker;
    private UI ui;
    private final StateStack stateStack;
    private WeaponManager weaponManager;
    private LingeringZoneManager lingeringZoneManager;
    private Point mousePosition = new Point(0, 0);
    private Camera camera;
    private CollisionGrid collisionGrid;

    private DeltaTimer deltaTimer;

    // DEBUG
    public static boolean DEBUG_MODE = false;
    public boolean isDebugMode() {
        return DEBUG_MODE;
    }
    public void toggleDebugMode() {
        DEBUG_MODE = !DEBUG_MODE;
        if (DEBUG_MODE) {
            this.bossManager.triggerBossFight(3);
        } else {
            this.bossManager.endBossFight();
        }
    }

    // constructor
    public GameWorld(Game game) {
        this.game = game;
        this.stateStack = new StateStack();
        this.collisionChecker = new CollisionChecker();

        initWorld();
        stateStack.push(new MenuState(this));
    }

    // Init
    private void initWorld() {
        int width = getGameWidth();
        int height = getGameHeight();

        this.collisionGrid = new CollisionGrid(100);
        this.player = new Player(this, width / 2, height / 2, 48, 0.22, 0.22);
        this.enemySpawner = new EnemySpawner(this, width, height);
        this.bossManager = new BossManager(this);
        this.chunkLoader = new ChunkLoader(player, width, height, 1500);
        this.ui = new UI(player.getPlayerManager());
        this.weaponManager = new WeaponManager();
        this.lingeringZoneManager = new LingeringZoneManager();
        this.deltaTimer = new DeltaTimer();
        this.lootManager = new LootManager();
        Chest.preload();
        Egg.init();
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
    public int getGameWidth() { return game.getWidth(); }
    public int getGameHeight() { return game.getHeight(); }

    public Game getGame() { return game; }

    public DeltaTimer getDeltaTimer() { return this.deltaTimer; }

    public Point getMousePosition() { return this.mousePosition; }
    public void setMousePosition(Point p) {this.mousePosition = p; }

    public StateStack getStateStack() { return stateStack; }
    public GameState getGameState() { return stateStack.peek(); }

    public LootManager getLootManager() { return this.lootManager; }

    public Player getPlayer() { return player; }
    public EnemySpawner getEnemySpawner() { return enemySpawner; }
    public BossManager getBossManager() { return this.bossManager; }
    public ChunkLoader getChunkLoader() { return chunkLoader; }
    public CollisionChecker getCollisionChecker() { return collisionChecker; }
    public CollisionGrid getCollisionGrid() { return collisionGrid; }
    public UI getUi() { return ui; }
    public WeaponManager getWeaponManager() { return weaponManager; }
    public LingeringZoneManager getLingeringZoneManager() { return lingeringZoneManager; }

    public void setCamera(Camera camera) { this.camera = camera;}
    public Camera getCamera() { return this.camera; }
}
