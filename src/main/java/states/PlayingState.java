package states;// states.PlayingState.java
import entities.enemies.EnemySpawner;
import entities.player.Player;
import utils.Camera;
import enviroment.ChunkLoader;
import ui.UI;
import utils.GameWorld;
import weapons.WeaponManager;
import weapons.eggcannon.EggCannon;

import java.awt.*;
import java.awt.event.*;

public class PlayingState implements GameState {
    private final Camera camera;
    public GameWorld gameWorld;
    public Player player;
    public EnemySpawner spawner;
    public ChunkLoader chunkLoader;
    public UI ui;
    public WeaponManager weaponManager;

    public PlayingState(GameWorld gameWorld) {
        this.camera = new Camera();
        this.gameWorld = gameWorld;
        this.player = gameWorld.getPlayer();
        this.spawner = gameWorld.getEnemySpawner();
        this.chunkLoader = gameWorld.getChunkLoader();
        this.ui = gameWorld.getUi();
        this.weaponManager = new WeaponManager();
        weaponManager.addWeapon(new EggCannon(this.player, 1000, gameWorld));

    }

    public void update() {
        if (player.hasLeveledUp()) {
            player.resetInput();
            gameWorld.getStateStack().push(new LevelUpState(gameWorld));
            return;
        }

        player.update();
        camera.centerOn(player, gameWorld.getGameWidth(), gameWorld.getGameHeight());
        weaponManager.update();
        spawner.update(player);
        chunkLoader.update();
        ui.update();
    }

    public void render(Graphics g) {
        g.setColor(Color.WHITE);
        g.fillRect(0, 0, gameWorld.getGameWidth(), gameWorld.getGameHeight());
        chunkLoader.render(g, camera);
        weaponManager.render(g, camera);
        player.render(g, camera);
        spawner.render(g, camera);

        ui.render((Graphics2D) g, gameWorld.getGameWidth(), gameWorld.getGameHeight());

    }

    public void keyPressed(KeyEvent e) {

        if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
            gameWorld.getStateStack().push(new PauseState(gameWorld));
            return;
        }

        switch (e.getKeyCode()) {
            case KeyEvent.VK_W -> player.setUp(true);
            case KeyEvent.VK_S -> player.setDown(true);
            case KeyEvent.VK_A -> player.setLeft(true);
            case KeyEvent.VK_D -> player.setRight(true);
        }
    }

    public void keyReleased(KeyEvent e) {
        switch (e.getKeyCode()) {
            case KeyEvent.VK_W -> player.setUp(false);
            case KeyEvent.VK_S -> player.setDown(false);
            case KeyEvent.VK_A -> player.setLeft(false);
            case KeyEvent.VK_D -> player.setRight(false);
        }
    }


    public void mousePressed(MouseEvent e) {
    }

    @Override
    public void mouseMoved(MouseEvent e) {

    }
}
