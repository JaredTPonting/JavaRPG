package core;

import states.GameState;

import java.awt.*;
import java.awt.image.BufferStrategy;

public class Game extends Canvas implements Runnable, InputHandler.GameStateProvider {

    // Default windowed dimensions
    public static final int DEFAULT_WIDTH = 1200;
    public static final int DEFAULT_HEIGHT = 900;

    private Thread thread;
    private boolean running = false;
    private Display display;

    public GameWorld gameWorld;

    public Game() {
        display = new Display("Chicken Isekai Attack", DEFAULT_WIDTH, DEFAULT_HEIGHT, this);
        this.gameWorld = new GameWorld(this);
        InputHandler input = new InputHandler(this::getGameState, this);
        addKeyListener(input);
        addMouseListener(input);
        addMouseMotionListener(input);
    }

    public GameState getGameState() {
        return gameWorld.getGameState();
    }

    public void toggleFullscreen() {
        display.toggleFullscreen();
    }

    public boolean isFullscreen() {
        return display.isFullscreen();
    }

    @Override
    public int getWidth() {
        // Use actual canvas size (works for both windowed and fullscreen)
        int w = super.getWidth();
        return w > 0 ? w : DEFAULT_WIDTH;
    }

    @Override
    public int getHeight() {
        int h = super.getHeight();
        return h > 0 ? h : DEFAULT_HEIGHT;
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
