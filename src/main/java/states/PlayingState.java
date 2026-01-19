package states;// states.PlayingState.java
import entities.enemies.Enemy;
import entities.enemies.EnemySpawner;
import entities.player.Player;
import lingeringzones.LingeringZoneManager;
import loot.LootManager;
import utils.Camera;
import environment.ChunkLoader;
import ui.UI;
import core.GameWorld;
import utils.DeltaTimer;
import utils.Renderable;
import weapons.WeaponManager;

import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Comparator;

public class PlayingState implements GameState {

    private final Camera camera;
    private final GameWorld gameWorld;
    private final Player player;
    private final EnemySpawner spawner;
    private final ChunkLoader chunkLoader;
    private final UI ui;
    private final WeaponManager weaponManager;
    private final LingeringZoneManager lingeringZoneManager;
    private final DeltaTimer deltaTimer;
    private final LootManager lootManager;

    public PlayingState(GameWorld gameWorld) {
        this.camera = new Camera();
        this.gameWorld = gameWorld;
        gameWorld.setCamera(camera);
        this.player = gameWorld.getPlayer();
        this.spawner = gameWorld.getEnemySpawner();
        this.chunkLoader = gameWorld.getChunkLoader();
        this.ui = gameWorld.getUi();
        this.weaponManager = gameWorld.getWeaponManager();
        this.lingeringZoneManager = gameWorld.getLingeringZoneManager();
        this.deltaTimer = this.gameWorld.getDeltaTimer();
        this.lootManager = gameWorld.getLootManager();
        initWeapons();
    }

    private void initWeapons() {
        // Weapons are added via loot selection, not hardcoded here
    }

    @Override
    public void update() {
        double dt = deltaTimer.getDelta();

        if (player.isDead()) {
            handleDeath();
            return;
        }

        player.update(dt);
        camera.update(player, gameWorld.getGameWidth(), gameWorld.getGameHeight());
        lingeringZoneManager.update(dt);
        spawner.update(player, dt);
        weaponManager.update(dt);
        lootManager.update(dt);
        chunkLoader.update();
        ui.update();
    }

    private void handleDeath() {
        gameWorld.refresh();
        gameWorld.getStateStack().push(new GameOverState(gameWorld));
    }

    private void toggleBossMode() {
        var bossManager = gameWorld.getBossManager();
        if (bossManager.isBossActive()) {
            bossManager.endBossFight();
        } else {
            bossManager.triggerBossFight(player.getPlayerManager().getLevel());
        }
    }

    @Override
    public void render(Graphics g) {
        g.setColor(Color.WHITE);
        g.fillRect(0, 0, gameWorld.getGameWidth(), gameWorld.getGameHeight());
        chunkLoader.render(g, camera);
        lingeringZoneManager.render(g, camera);
        ArrayList<Renderable> depthObjects = new ArrayList<>();
        depthObjects.add(player);
        depthObjects.addAll(spawner.getEnemies());
        depthObjects.addAll(lootManager.getChests());
        depthObjects.sort(Comparator.comparingDouble(Renderable::getRenderY));
        for (Renderable r : depthObjects) {
            r.render(g, camera);
        }
        weaponManager.render(g, camera);
        spawner.renderDamageIndicators(g, camera);
        ui.render((Graphics2D) g, gameWorld.getGameWidth(), gameWorld.getGameHeight());
        gameWorld.getCollisionGrid().draw(g, camera);
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
            this.deltaTimer.pause();
            gameWorld.getStateStack().push(new PauseState(gameWorld));
            return;
        }

        switch (e.getKeyCode()) {
            case KeyEvent.VK_W -> player.setUp(true);
            case KeyEvent.VK_S -> player.setDown(true);
            case KeyEvent.VK_A -> player.setLeft(true);
            case KeyEvent.VK_D -> player.setRight(true);
            case KeyEvent.VK_SPACE -> player.dash();
            case KeyEvent.VK_F3 -> gameWorld.toggleDebugMode();
            case KeyEvent.VK_F5 -> toggleBossMode();
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        switch (e.getKeyCode()) {
            case KeyEvent.VK_W -> player.setUp(false);
            case KeyEvent.VK_S -> player.setDown(false);
            case KeyEvent.VK_A -> player.setLeft(false);
            case KeyEvent.VK_D -> player.setRight(false);
        }
    }

    @Override
    public void mousePressed(MouseEvent e) {}
    @Override
    public void mouseMoved(MouseEvent e) {}
}

