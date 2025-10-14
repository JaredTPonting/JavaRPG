// PlayingState.java
import ammo.AmmoHandler;
import ammo.Projectile;
import enemies.Enemy;
import enemies.EnemySpawner;
import enviroment.Grass;
import player.Player;
import utils.Camera;
import enviroment.ChunkLoader;

import java.awt.*;
import java.awt.event.*;
import java.util.Scanner;

public class PlayingState implements GameState {
    private final Game game;
    private final Player player;
    private final AmmoHandler ammoHandler;
    private final EnemySpawner spawner;
    private int mouseX = 0, mouseY = 0;
    private final Camera camera;
    private ChunkLoader chunkLoader;

    public PlayingState(Game game) {
        this.game = game;

        int w = game.getMapWidth();
        int h = game.getMapHeight();

        player = new Player(w / 2, h / 2, w, h);
        ammoHandler = new AmmoHandler(game.getMapWidth(), game.getMapHeight());
        spawner = new EnemySpawner(w, h);

        camera = new Camera();

        spawner.setCamera(camera);
        chunkLoader = new ChunkLoader(this.player, 800, 600, 256);

    }

    public PlayingState(Game game, Player player, AmmoHandler ammoHandler, EnemySpawner enemySpawner, ChunkLoader chunkLoader) {
        this.game = game;

        int w = game.getMapWidth();
        int h = game.getMapHeight();

        this.player = player;
        this.ammoHandler = ammoHandler;
        this.spawner = enemySpawner;
        this.chunkLoader = chunkLoader;
        camera = new Camera();

        spawner.setCamera(camera);
    }

    private void showLevelUpMenu() {
        Scanner scanner = new Scanner(System.in);

        while (player.getUnspentPoints() > 0) {
            System.out.println("\n=== LEVEL UP! You have " + player.getUnspentPoints() + " points to spend ===");
            System.out.println("1. Increase Max Health (+20)");
            System.out.println("2. Increase Health Regen (+0.1/sec)");
            System.out.println("3. Increase Move Speed (+0.5)");
            System.out.println("4. Increase Bullet Speed (+1)");
            System.out.println("5. Increase Damage (+1)");

            System.out.print("Choose an upgrade: ");
            int choice;
            try {
                choice = Integer.parseInt(scanner.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("Invalid input!");
                continue;
            }

            switch (choice) {
                case 1 -> {
                    player.increaseMaxHealth();
                    player.spendPoint();
                    System.out.println("Max Health increased!");
                }
                case 2 -> {
                    player.increaseRange();
                    player.spendPoint();
                    System.out.println("Health Regen increased!");
                }
                case 3 -> {
                    player.increaseSpeed();
                    player.spendPoint();
                    System.out.println("Move Speed increased!");
                }
                case 4 -> {
                    ammoHandler.increaseBulletSpeedMulitplier();
                    player.spendPoint();
                    System.out.println("Bullet Speed increased!");
                }
                case 5 -> {
                    player.increaseHealthRegen();
                    player.spendPoint();
                    System.out.println("Damage increased!");
                }
                default -> System.out.println("Invalid choice!");
            }
        }

        System.out.println("All points spent! Back to the game.");
    }

    public void update() {
        if (player.hasLeveledUp()) {
            player.resetInput();
            game.setGameState(new LevelUpState(game, player, ammoHandler, spawner, chunkLoader));
            return;
        }

        player.update();
        camera.centerOn(player, game.getWidth(), game.getHeight(), game.getMapWidth(), game.getMapHeight());
        updateAmmo();
        spawner.update(player);
        chunkLoader.update();

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

    }

    public void keyPressed(KeyEvent e) {

        if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
            game.setGameState(new PauseState(game, player, ammoHandler, spawner, chunkLoader));
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
        mouseX = e.getX();
        mouseY = e.getY();
    }

    public void mousePressed(MouseEvent e) {
    }
}
