package states;

import entities.player.Player;
import utils.GameWorld;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;

public class GameOverState implements GameState {

    private final GameWorld gameWorld;

    private static final Color overlayColor = new Color(255, 0, 0, 10); // faint red

    public GameOverState(GameWorld gameWorld) {
        this.gameWorld = gameWorld;

    }

    @Override
    public void update() {
        // No updates while in game over
    }

    @Override
    public void render(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;

        GameState stateBeneath = gameWorld.getStateStack().peekBelowTop();
        if (stateBeneath != null) {
            stateBeneath.render(g2);
        }

        int width = gameWorld.getGameWidth();
        int height = gameWorld.getGameHeight();

        // Draw faint red overlay
        g2.setColor(overlayColor);
        g2.fillRect(0, 0, width, height);

        // Draw "YOU DIED"
        g2.setColor(Color.WHITE);
        g2.setFont(new Font("Arial", Font.BOLD, 80));
        String deathText = "YOU DIED";
        FontMetrics fm = g2.getFontMetrics();
        int textX = (width - fm.stringWidth(deathText)) / 2;
        int textY = height / 2 - 20;
        g2.drawString(deathText, textX, textY);

        // Draw "click to go to main menu"
        g2.setFont(new Font("Arial", Font.PLAIN, 30));
        String prompt = "Click to go to main menu";
        fm = g2.getFontMetrics();
        textX = (width - fm.stringWidth(prompt)) / 2;
        textY += 80;
        g2.drawString(prompt, textX, textY);
    }

    @Override
    public void keyPressed(KeyEvent e) {
        // Optional: allow escape to main menu too
        if (e.getKeyCode() == KeyEvent.VK_ESCAPE) goToMainMenu();
    }

    @Override
    public void keyReleased(KeyEvent e) {}

    @Override
    public void mousePressed(MouseEvent e) {
        goToMainMenu();
    }

    private void goToMainMenu() {// remove all states
        gameWorld.getStateStack().resetToMainMenu(gameWorld);
    }

    @Override
    public void mouseMoved(MouseEvent e) {}
}
