// GameState.java
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;

public interface GameState {
    void update();
    void render(Graphics g);
    void keyPressed(KeyEvent e);
    void keyReleased(KeyEvent e);
    void mousePressed(MouseEvent e);
    void mouseMoved(MouseEvent e);
}
