import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferStrategy;

public class Game extends Canvas implements Runnable, KeyListener, MouseListener {

    // Screen dimensions
    public static final int SCREEN_WIDTH = 1200;
    public static final int SCREEN_HEIGHT = 900;

    private Thread thread;
    private boolean running = false;

    private GameState currentState;

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
                if (currentState != null) {
                    currentState.mouseMoved(e);
                }
            }
        });

        // Start in menu state
        setGameState(new MenuState(this));
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
        if (currentState != null) {
            currentState.update();
        }
    }

    private void render(BufferStrategy bs) {
        Graphics g = bs.getDrawGraphics();

        if (currentState != null) {
            currentState.render(g);
        }

        g.dispose();
        bs.show();
    }

    public void setGameState(GameState state) {
        this.currentState = state;
    }

    // Event forwarding
    public void keyPressed(KeyEvent e) {
        if (currentState != null) {
            currentState.keyPressed(e);
        }
    }

    public void keyReleased(KeyEvent e) {
        if (currentState != null) {
            currentState.keyReleased(e);
        }
    }

    public void keyTyped(KeyEvent e) {}

    public void mousePressed(MouseEvent e) {
        if (currentState != null) {
            currentState.mousePressed(e);
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
