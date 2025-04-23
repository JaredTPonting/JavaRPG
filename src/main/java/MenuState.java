// MenuState.java
import java.awt.*;
import java.awt.event.*;

public class MenuState implements GameState {
    private final Game game;

    public MenuState(Game game) {
        this.game = game;
    }

    public void update() {}

    public void render(Graphics g) {
        g.setColor(Color.BLACK);
        g.fillRect(0, 0, game.getWidth(), game.getHeight());

        g.setColor(Color.WHITE);
        g.setFont(new Font("Arial", Font.BOLD, 48));
        g.drawString("Chicken Run", game.getWidth() / 2 - 160, 200);
        g.setFont(new Font("Arial", Font.PLAIN, 36));
        g.drawString("Click to Play", game.getWidth() / 2 - 100, 300);
    }

    public void keyPressed(KeyEvent e) {}

    public void keyReleased(KeyEvent e) {}

    public void mouseMoved(MouseEvent e) {}

    public void mousePressed(MouseEvent e) {
        game.setGameState(new PlayingState(game));  // Switch to game
    }
}
