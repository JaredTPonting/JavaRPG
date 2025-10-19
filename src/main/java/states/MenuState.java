package states;// states.MenuState.java

import utils.GameWorld;

import java.awt.*;
import java.awt.event.*;

public class MenuState implements GameState {
    private final GameWorld gameWorld;

    public MenuState(GameWorld game) {
        this.gameWorld = game;
    }

    public void update() {}

    public void render(Graphics g) {
        g.setColor(Color.BLACK);
        g.fillRect(0, 0, gameWorld.getGameWidth(), gameWorld.getGameHeight());

        g.setColor(Color.WHITE);
        g.setFont(new Font("Arial", Font.BOLD, 48));
        g.drawString("Chicken Run", gameWorld.getGameWidth() / 2 - 160, 200);
        g.setFont(new Font("Arial", Font.PLAIN, 36));
        g.drawString("Click to Play", gameWorld.getGameWidth() / 2 - 100, 300);
    }

    public void keyPressed(KeyEvent e) {}

    public void keyReleased(KeyEvent e) {}

    public void mouseMoved(MouseEvent e) {}

    public void mousePressed(MouseEvent e) {
        gameWorld.getStateStack().push(new PlayingState(gameWorld));  // Switch to game
    }
}
