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
import java.util.Scanner;

public class PlayingState implements GameState {
    private final Game game;
    private final Player player;
    private final AmmoHandler ammoHandler;
    private final Grass grass;
    private final EnemySpawner spawner;
    private int mouseX = 0, mouseY = 0;
    private final Camera camera;
    private boolean paused = false;
    private boolean levelUpMenuActive = false;

    public PlayingState(Game game) {
        this.game = game;

        int w = game.getMapWidth();
        int h = game.getMapHeight();

        player = new Player(w / 2, h / 2, w, h);
        ammoHandler = new AmmoHandler(game.getMapWidth(), game.getMapHeight());
        spawner = new EnemySpawner(w, h);
        grass = new Grass(w, h);

        camera = new Camera();
    }

    public void mouseMoved(MouseEvent e) {
        mouseX = e.getX();
        mouseY = e.getY();
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
        this.paused = false;
    }

    public void update() {
        if (player.hasLeveledUp()) {
            this.paused = true;
            this.levelUpMenuActive = true;
            player.clearLevelUpFlag();
            return;
        }

        if (this.paused) {
            return;
        }

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

        grass.render(g, camera);
        player.render(g, camera);
        ammoHandler.render(g, camera);
        spawner.render(g, camera);

        if (levelUpMenuActive) {
            g.setColor(new Color(0, 0, 0, 180)); // semi-transparent overlay
            g.fillRect(0, 0, game.getWidth(), game.getHeight());

            g.setColor(Color.WHITE);
            g.setFont(new Font("Arial", Font.BOLD, 20));
            g.drawString("LEVEL UP! You have " + player.getUnspentPoints() + " points.", 100, 100);

            g.setFont(new Font("Arial", Font.PLAIN, 18));
            g.drawString("1. Increase Max Health (+20)", 120, 150);
            g.drawString("2. Increase Health Regen (+0.1/sec)", 120, 180);
            g.drawString("3. Increase Move Speed (+0.5)", 120, 210);
            g.drawString("4. Increase Bullet Speed (+1)", 120, 240);
            g.drawString("5. Increase Damage (+1)", 120, 270);
        }
    }

    public void keyPressed(KeyEvent e) {
        if (levelUpMenuActive) {
            switch (e.getKeyCode()) {
                case KeyEvent.VK_1 -> { player.increaseMaxHealth(); player.spendPoint(); }
                case KeyEvent.VK_2 -> { player.increaseHealthRegen(); player.spendPoint(); }
                case KeyEvent.VK_3 -> { player.increaseSpeed(); player.spendPoint(); }
                case KeyEvent.VK_4 -> { ammoHandler.increaseBulletSpeedMulitplier(); player.spendPoint(); }
                case KeyEvent.VK_5 -> { ammoHandler.increaseDamageMultiplier(); player.spendPoint();  }
            }

            if (player.getUnspentPoints() <= 0) {
                System.out.println("All points spent! Back to the game.");
                levelUpMenuActive = false;
                paused = false;
            }
            System.out.println(player.getMaxHealth());
            System.out.println(player.getSpeed());
            System.out.println();
            System.out.println();
            System.out.println();

            return; // stop here, don’t run normal controls while menu is open
        }

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
