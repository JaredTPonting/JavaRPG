package core;

import states.GameState;
import utils.GameWorld;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferStrategy;

public class Game extends Canvas implements Runnable, KeyListener, MouseListener {

    // Screen dimensions
    public static final int SCREEN_WIDTH = 1200;
    public static final int SCREEN_HEIGHT = 900;

    public static final int MAP_WIDTH = SCREEN_WIDTH * 2;
    public static final int MAP_HEIGHT = SCREEN_HEIGHT * 2;

    private Thread thread;
    private boolean running = false;

    private GameState currentState;
    public GameWorld gameWorld;

    public Game() {
        JFrame frame = new JFrame("Chicken Run");
        frame.setSize(SCREEN_WIDTH, SCREEN_HEIGHT);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setResizable(false);
        frame.add(this);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);

        this.addKeyListener(this);
        this.addMouseListener(this);
        this.setFocusable(true);
        this.requestFocus();

        this.addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseMoved(MouseEvent e) {
                if (getGameState() != null) {
                    getGameState().mouseMoved(e);
                }
            }
        });

        this.gameWorld = new GameWorld(getWidth(), getHeight());
    }

    public GameState getGameState() {
        return gameWorld.getGameState();
    }

    public int getWidth() {
        return SCREEN_WIDTH;
    }

    public int getHeight() {
        return SCREEN_HEIGHT;
    }

    public int getMapWidth() {
        return MAP_WIDTH;
    }

    public int getMapHeight() {
        return MAP_HEIGHT;
    }

    public synchronized void start() {
        if (running) return;
        running = true;
        thread = new Thread(this);
        thread.start();
    }

    public synchronized void stop() {
        if (!running) return;
        running = false;
        try {
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void run() {
        this.createBufferStrategy(3);
        BufferStrategy bs = this.getBufferStrategy();

        long lastTime = System.nanoTime();
        double nsPerUpdate = 1000000000.0 / 60.0;
        double delta = 0;

        while (running) {
            long now = System.nanoTime();
            delta += (now - lastTime) / nsPerUpdate;
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
        gameWorld.update();
    }

    private void render(BufferStrategy bs) {
        Graphics g = bs.getDrawGraphics();

        gameWorld.render(g);

        g.dispose();
        bs.show();
    }

    // Event forwarding
    public void keyPressed(KeyEvent e) {
        if (getGameState() != null) {
            getGameState().keyPressed(e);
        }
    }

    public void keyReleased(KeyEvent e) {
        if (getGameState() != null) {
            getGameState().keyReleased(e);
        }
    }

    public void keyTyped(KeyEvent e) {}

    public void mousePressed(MouseEvent e) {
        if (getGameState() != null) {
            getGameState().mousePressed(e);
        }
    }

    public void mouseReleased(MouseEvent e) {}
    public void mouseClicked(MouseEvent e) {}
    public void mouseEntered(MouseEvent e) {}
    public void mouseExited(MouseEvent e) {}

    public static void main(String[] args) {
        Game game = new Game();
        game.start();
    }
}
