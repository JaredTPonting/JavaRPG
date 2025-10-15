// PlayingState.java
import ammo.AmmoHandler;
import ammo.Projectile;
import enemies.Enemy;
import enemies.EnemySpawner;
import player.Player;
import utils.Camera;
import enviroment.ChunkLoader;
import ui.UI;

import java.awt.*;
import java.awt.event.*;

public class PlayingState implements GameState {
    private final Game game;
    private final Player player;
    private final AmmoHandler ammoHandler;
    private final EnemySpawner spawner;
    private final Camera camera;
    private final ChunkLoader chunkLoader;
    private final UI ui;
    private final GameWorld gameWorld;

    public PlayingState(Game game) {
        this.game = game;

        int w = game.getWidth();
        int h = game.getHeight();

        player = new Player(w / 2, h / 2, w, h);
        ammoHandler = new AmmoHandler(game.getMapWidth(), game.getMapHeight());
        spawner = new EnemySpawner(w, h);
        ui = new UI(player);

        camera = new Camera();
        chunkLoader = new ChunkLoader(this.player, game.getWidth(), game.getHeight(), 1500);
        gameWorld = new GameWorld(this.player, this.ammoHandler, this.spawner, this.chunkLoader, this.ui);

    }

    public PlayingState(Game game, GameWorld gameWorld) {
        this.game = game;
        this.gameWorld = gameWorld;
        this.player = gameWorld.getPlayer();
        this.ammoHandler = gameWorld.getAmmoHandler();
        this.spawner = gameWorld.getEnemySpawner();
        this.chunkLoader = gameWorld.getChunkLoader();
        this.ui = gameWorld.getUi();
        camera = new Camera();
    }

    public void update() {
        if (player.hasLeveledUp()) {
            player.resetInput();
            game.setGameState(new LevelUpState(game, gameWorld));
            return;
        }

        player.update();
        camera.centerOn(player, game.getWidth(), game.getHeight(), game.getMapWidth(), game.getMapHeight());
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
        g.fillRect(0, 0, game.getWidth(), game.getHeight());
        chunkLoader.render(g, camera);
        player.render(g, camera);
        ammoHandler.render(g, camera);
        spawner.render(g, camera);
        ui.render((Graphics2D) g, game.getWidth(), game.getHeight());

    }

    public void keyPressed(KeyEvent e) {

        if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
            game.setGameState(new PauseState(game, gameWorld));
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
