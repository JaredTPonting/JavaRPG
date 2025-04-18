// Game.java
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferStrategy;
import java.util.List;
import java.util.ArrayList;



public class Game extends Canvas implements Runnable, KeyListener {
    private boolean running = false;
    private Thread thread;
    private Player player;
    private Enemy enemy;

    private List<Bullet> bullets = new ArrayList<>();



    public Game() {
        JFrame frame = new JFrame("Monster Escape");
        frame.setSize(800, 600);
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
                bullets.add(new Bullet(player.getX(), player.getY(), e.getX(), e.getY()));
            }
        });

        player = new Player(400, 300); // Start in the middle
        enemy = new Enemy(100, 100, player);
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

    private void update() {
        player.update();
        enemy.update();
        for (Bullet bullet : bullets) {
            bullet.update();
        }

        for (Bullet bullet : bullets) {
                if (bullet.isActive() && enemy.getBounds().intersects(bullet.getBounds())) {
                    enemy.damage(1); // Deal 1 damage to enemy
                    bullet.setActive(false); // Deactivate the bullet after it hits
                }
        }

        bullets.removeIf(bullet -> !bullet.isActive());
    }

    private void render(BufferStrategy bs) {
        Graphics g = bs.getDrawGraphics();

        g.setColor(Color.WHITE);
        g.fillRect(0, 0, getWidth(), getHeight()); // Clear screen

        player.render(g);
        enemy.render(g);

        for (Bullet bullet : bullets) {
            bullet.render(g);
        }

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
