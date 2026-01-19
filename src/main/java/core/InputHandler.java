package core;

import states.GameState;
import java.awt.event.*;

public class InputHandler implements KeyListener, MouseListener, MouseMotionListener {
    private final GameStateProvider stateProvider;
    private final Game game;

    public interface GameStateProvider {
        GameState getGameState();
    }

    public InputHandler(GameStateProvider provider, Game game) {
        this.stateProvider = provider;
        this.game = game;
    }

    private GameState getState() {
        return stateProvider.getGameState();
    }

    @Override
    public void keyPressed(KeyEvent e) {
        // Global keybinds (work in any state)
        if (e.getKeyCode() == KeyEvent.VK_F11) {
            game.toggleFullscreen();
            return;
        }

        if (getState() != null) getState().keyPressed(e);
    }

    @Override
    public void keyReleased(KeyEvent e) {
        if (getState() != null) getState().keyReleased(e);
    }

    @Override
    public void mousePressed(MouseEvent e) {
        if (getState() != null) getState().mousePressed(e);
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        if (getState() != null) getState().mouseMoved(e);
    }

    // other listener methods (empty overrides)
    public void keyTyped(KeyEvent e) {}
    public void mouseReleased(MouseEvent e) {}
    public void mouseClicked(MouseEvent e) {}
    public void mouseEntered(MouseEvent e) {}
    public void mouseExited(MouseEvent e) {}
    public void mouseDragged(MouseEvent e) {}
}
