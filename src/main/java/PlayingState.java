// PlayingState.java
import ammo.AmmoHandler;
import ammo.Projectile;
import enemies.Enemy;
import enemies.EnemySpawner;
import envviroment.Grass;
import player.Player;
import utils.Camera;

import java.awt.*;
import java.awt.event.*;

public class PlayingState implements GameState {
    private final Game game;
    private final Player player;
    private final AmmoHandler ammoHandler;
    private final Grass grass;
    private final EnemySpawner spawner;
    private int mouseX = 0, mouseY = 0;
    private Camera camera;

    public PlayingState(Game game) {
        this.game = game;

        int w = game.getMapWidth();
        int h = game.getMapHeight();

        player = new Player(w / 2, h / 2, w, h);
        ammoHandler = new AmmoHandler(game.getMapWidth(), game.getMapHeight());
        spawner = new EnemySpawner(player, w, h);
        grass = new Grass(w, h);

        camera = new Camera();
    }

    public void mouseMoved(MouseEvent e) {
        mouseX = e.getX();
        mouseY = e.getY();
    }

    public void update() {
        player.update();
        camera.centerOn(player, game.getWidth(), game.getHeight(), game.getMapWidth(), game.getMapHeight());
        updateAmmo();
        spawner.update(player);

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
            for (Enemy enemy : spawner.enemies) {
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

        grass.render(g, camera);
        player.render(g, camera);
        ammoHandler.render(g, camera);
        spawner.render(g, camera);
    }

    public void keyPressed(KeyEvent e) {
        switch (e.getKeyCode()) {
            case KeyEvent.VK_W -> player.setUp(true);
            case KeyEvent.VK_S -> player.setDown(true);
            case KeyEvent.VK_A -> player.setLeft(true);
            case KeyEvent.VK_D -> player.setRight(true);
            case KeyEvent.VK_SPACE -> ammoHandler.fire(player.getX(), player.getY(), mouseX, mouseY);
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
        ammoHandler.fire(player.getX(), player.getY(), e.getX(), e.getY());
    }
}
