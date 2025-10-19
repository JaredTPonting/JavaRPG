package states;// states.PlayingState.java
import ammo.AmmoHandler;
import ammo.Projectile;
import enemies.Enemy;
import enemies.EnemySpawner;
import player.Player;
import utils.Camera;
import enviroment.ChunkLoader;
import ui.UI;
import core.Game;
import utils.GameWorld;

import java.awt.*;
import java.awt.event.*;

public class PlayingState implements GameState {
    private final Camera camera;
    public GameWorld gameWorld;
    public Player player;
    public EnemySpawner spawner;
    public ChunkLoader chunkLoader;
    public UI ui;
    public AmmoHandler ammoHandler;

    public PlayingState(GameWorld gameWorld) {
        this.camera = new Camera();
        this.gameWorld = gameWorld;
        this.player = gameWorld.getPlayer();
        this.spawner = gameWorld.getEnemySpawner();
        this.chunkLoader = gameWorld.getChunkLoader();
        this.ui = gameWorld.getUi();
        this.ammoHandler = gameWorld.getAmmoHandler();

    }

    public void update() {
        if (player.hasLeveledUp()) {
            player.resetInput();
            gameWorld.getStateStack().push(new LevelUpState(gameWorld));
            return;
        }

        player.update();
        camera.centerOn(player, gameWorld.getGameWidth(), gameWorld.getGameHeight());
        updateAmmo();
        spawner.update(player);
        chunkLoader.update();
        ui.update();

        Enemy target = spawner.findNearestEnemy(player.getX(), player.getY(), player.getRange());
        if (target != null && ammoHandler.canShoot()) {
            double dx = target.getX() - player.getX();
            double dy = target.getY() - player.getY();
            ammoHandler.fire(player.getX(), player.getY(), dx, dy);
        }
    }

    private void updateAmmo() {
        ammoHandler.update();

        for (Projectile p : ammoHandler.getProjectiles()) {
            for (Enemy enemy : spawner.getEnemies()) {
                if (!enemy.isDead() && p.getBounds().intersects(enemy.getBounds())) {
                    enemy.damage(ammoHandler.getDamage());
                    p.setActive(false);
                    break;
                }
            }
        }
    }

    public void render(Graphics g) {
        g.setColor(Color.WHITE);
        g.fillRect(0, 0, gameWorld.getGameWidth(), gameWorld.getGameHeight());
        chunkLoader.render(g, camera);
        player.render(g, camera);
        ammoHandler.render(g, camera);
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

    public void mouseMoved(MouseEvent e) {
        int mouseX = e.getX();
        int mouseY = e.getY();
    }

    public void mousePressed(MouseEvent e) {
    }
}
