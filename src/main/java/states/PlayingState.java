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
import utils.Profiler;
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

    // Reusable list for depth sorting - avoids allocation every frame
    private final ArrayList<Renderable> depthObjects = new ArrayList<>();

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
        Profiler.get().frameStart();
        double dt = deltaTimer.getDelta();

        if (player.isDead()) {
            handleDeath();
            return;
        }

        Profiler.get().start("Player");
        player.update(dt);
        Profiler.get().end("Player");

        Profiler.get().start("Camera");
        camera.update(player, gameWorld.getGameWidth(), gameWorld.getGameHeight());
        Profiler.get().end("Camera");

        Profiler.get().start("Zones");
        lingeringZoneManager.update(dt);
        Profiler.get().end("Zones");

        Profiler.get().start("Enemies");
        spawner.update(player, dt);
        Profiler.get().end("Enemies");

        Profiler.get().start("Weapons");
        weaponManager.update(dt);
        Profiler.get().end("Weapons");

        Profiler.get().start("Loot");
        lootManager.update(dt);
        Profiler.get().end("Loot");

        Profiler.get().start("Chunks");
        chunkLoader.update();
        Profiler.get().end("Chunks");

        Profiler.get().start("UI");
        ui.update();
        Profiler.get().end("UI");

        // Update profiler entity counts
        Profiler.get().setEntityCounts(
            spawner.getEnemies().size(),
            weaponManager.getProjectileCount(),
            lootManager.getChests().size()
        );
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
        Profiler.get().start("Render");

        g.setColor(Color.WHITE);
        g.fillRect(0, 0, gameWorld.getGameWidth(), gameWorld.getGameHeight());

        Profiler.get().start("R:Chunks");
        chunkLoader.render(g, camera);
        Profiler.get().end("R:Chunks");

        Profiler.get().start("R:Zones");
        lingeringZoneManager.render(g, camera);
        Profiler.get().end("R:Zones");

        Profiler.get().start("R:Sort");
        depthObjects.clear();
        depthObjects.add(player);
        depthObjects.addAll(spawner.getEnemies());
        depthObjects.addAll(lootManager.getChests());
        depthObjects.sort(Comparator.comparingDouble(Renderable::getRenderY));
        Profiler.get().end("R:Sort");

        Profiler.get().start("R:Entities");
        for (Renderable r : depthObjects) {
            r.render(g, camera);
        }
        Profiler.get().end("R:Entities");

        Profiler.get().start("R:Weapons");
        weaponManager.render(g, camera);
        Profiler.get().end("R:Weapons");

        spawner.renderDamageIndicators(g, camera);
        ui.render((Graphics2D) g, gameWorld.getGameWidth(), gameWorld.getGameHeight());
        gameWorld.getCollisionGrid().draw(g, camera);

        Profiler.get().end("Render");

        // Render profiler overlay when debug mode is on
        if (gameWorld.isDebugMode()) {
            Profiler.get().render((Graphics2D) g, gameWorld.getGameWidth());
        }

        Profiler.get().frameEnd();
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
            case KeyEvent.VK_F4 -> Profiler.get().toggleLogging();
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

