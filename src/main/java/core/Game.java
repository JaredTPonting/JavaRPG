package core;

import states.GameState;

import java.awt.*;
import java.awt.image.BufferStrategy;

public class Game extends Canvas implements Runnable, InputHandler.GameStateProvider {

    // Screen dimensions
    public static final int SCREEN_WIDTH = 1200;
    public static final int SCREEN_HEIGHT = 900;

    private Thread thread;
    private boolean running = false;

    public GameWorld gameWorld;

    public Game() {
        Display display = new Display("Chicken Run", SCREEN_WIDTH, SCREEN_HEIGHT, this);

        InputHandler input = new InputHandler(this::getGameState);
        addKeyListener(input);
        addMouseListener(input);
        addMouseMotionListener(input);
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

    public static void main(String[] args) {
        Game game = new Game();
        game.start();
    }
}
