import enemies.Enemy;
import envviroment.Grass;
import player.Player;
import enemies.EnemySpawner;
import utils.SpriteLoader;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.util.Random;
import java.util.List;
import java.util.ArrayList;

import static java.lang.Math.ceil;


public class Game extends Canvas implements Runnable, KeyListener {
    private Random random = new Random();
    private boolean running = false;
    private Thread thread;
    private int SCREEN_WIDTH = 1200;
    private int SCREEN_HEIGHT = 900;
    private Player player;
    private Grass grass;

    private List<Bullet> bullets = new ArrayList<>();

    // wave logic variables
    private EnemySpawner spawner;

    public Game() {
        JFrame frame = new JFrame("Chicken Run");
        frame.setSize(SCREEN_WIDTH, SCREEN_HEIGHT);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setResizable(false);
        frame.add(this);
        frame.setVisible(true);

        this.addKeyListener(this);
        this.setFocusable(true);
        this.requestFocus();

        this.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                // Shoot bullet when left-clicking
                bullets.add(new Bullet(player.getX(), player.getY(), e.getX(), e.getY(), SCREEN_WIDTH, SCREEN_HEIGHT));
            }
        });

        player = new Player(SCREEN_WIDTH / 2, SCREEN_HEIGHT / 2, SCREEN_WIDTH, SCREEN_HEIGHT); // Start in the middle
        spawner = new EnemySpawner(player, SCREEN_WIDTH, SCREEN_HEIGHT);

        grass = new Grass(SCREEN_WIDTH, SCREEN_HEIGHT);
    }

    public synchronized void start() {
        running = true;
        thread = new Thread(this);
        thread.start();
    }

    public synchronized void stop() {
        running = false;
        try {
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void run() {
        this.createBufferStrategy(2);
        BufferStrategy bs = this.getBufferStrategy();

        long lastTime = System.nanoTime();
        double nsPerTick = 1000000000D / 60D;
        double delta = 0;

        while (running) {
            long now = System.nanoTime();
            delta += (now - lastTime) / nsPerTick;
            lastTime = now;

            while (delta >= 1) {
                update();
                delta--;
            }

            render(bs);
        }

        stop();
    }

    private void updateBullets() {
        for (Bullet bullet : bullets) {
            bullet.update();

            if (bullet.getX() < 0 || bullet.getX() > SCREEN_WIDTH || bullet.getY() < 0 || bullet.getY() > SCREEN_HEIGHT) {
                bullet.setActive(false);
            }
        }

        for (Bullet bullet : bullets) {
            if (!bullet.isActive()) continue;

            for (Enemy enemy : spawner.enemies) {
                if (!enemy.isDead() && bullet.getBounds().intersects(enemy.getBounds())) {
                    enemy.damage(1);
                    bullet.setActive(false);
                    break;
                }
            }
        }

        bullets.removeIf(bullet -> !bullet.isActive());
    }

    private void update() {
        player.update();
        updateBullets();
        spawner.update(player);
    }

    private void render(BufferStrategy bs) {
        Graphics g = bs.getDrawGraphics();

        g.setColor(Color.WHITE);
        g.fillRect(0, 0, getWidth(), getHeight()); // Clear screen

        grass.render(g);

        player.render(g);

        for (Bullet bullet : bullets) {
            bullet.render(g);
        }
        spawner.render(g);

        g.dispose();
        bs.show();
    }

    // KeyListener methods
    public void keyPressed(KeyEvent e) {
        int key = e.getKeyCode();
        if (key == KeyEvent.VK_W) player.setUp(true);
        if (key == KeyEvent.VK_S) player.setDown(true);
        if (key == KeyEvent.VK_A) player.setLeft(true);
        if (key == KeyEvent.VK_D) player.setRight(true);
    }

    public void keyReleased(KeyEvent e) {
        int key = e.getKeyCode();
        if (key == KeyEvent.VK_W) player.setUp(false);
        if (key == KeyEvent.VK_S) player.setDown(false);
        if (key == KeyEvent.VK_A) player.setLeft(false);
        if (key == KeyEvent.VK_D) player.setRight(false);
    }


    public void keyTyped(KeyEvent e) {}

    public static void main(String[] args) {
        Game game = new Game();
        game.start();
    }
}