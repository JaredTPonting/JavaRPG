package states;

import entities.player.Player;
import utils.GameWorld;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;

public class PauseState implements GameState {

    private final Player player;
    private final GameWorld gameWorld;
    private Point mousePosition;

    private boolean showStats = false;

    // Button definitions
    private static final int BUTTON_WIDTH = 200;
    private static final int BUTTON_HEIGHT = 50;
    private static final int BUTTON_X = 500;

    private final Rectangle resumeButton = new Rectangle(BUTTON_X, 300, BUTTON_WIDTH, BUTTON_HEIGHT);
    private final Rectangle statsButton = new Rectangle(BUTTON_X, 400, BUTTON_WIDTH, BUTTON_HEIGHT);
    private final Rectangle levelUpButton = new Rectangle(BUTTON_X, 500, BUTTON_WIDTH, BUTTON_HEIGHT);
    private final Rectangle quitButton = new Rectangle(BUTTON_X, 600, BUTTON_WIDTH, BUTTON_HEIGHT);

    private final Color overlayColor = new Color(0, 0, 0, 180);
    private final Color buttonColor = new Color(255, 255, 255, 220);
    private final Color buttonHoverColor = new Color(200, 200, 255, 240);
    private final Color statsOverlay = new Color(0, 0, 0, 200);

    public PauseState(GameWorld gameWorld) {
        this.gameWorld = gameWorld;
        this.player = gameWorld.getPlayer();
    }

    @Override
    public void update() {
        // No updates while paused
    }

    @Override
    public void render(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;
        int width = gameWorld.getGameWidth();
        int height = gameWorld.getGameHeight();

        // Dark semi-transparent overlay
        g2.setColor(overlayColor);
        g2.fillRect(0, 0, width, height);

        // Draw buttons with rounded edges
        drawButton(g2, resumeButton, "RESUME");
        drawButton(g2, statsButton, "STATS");
        drawButton(g2, levelUpButton, "LEVEL UP");
        drawButton(g2, quitButton, "QUIT");

        // Draw stats panel if toggled
        if (showStats) drawStatsPanel(g2);
    }

    private void drawButton(Graphics2D g2, Rectangle rect, String text) {
        // Hover effect
        Color fill = (mousePosition != null && rect.contains(mousePosition)) ? buttonHoverColor : buttonColor;

        g2.setColor(fill);
        g2.fillRoundRect(rect.x, rect.y, rect.width, rect.height, 20, 20);

        g2.setColor(Color.BLACK);
        g2.setFont(new Font("Arial", Font.BOLD, 22));

        // Center text
        FontMetrics fm = g2.getFontMetrics();
        int textX = rect.x + (rect.width - fm.stringWidth(text)) / 2;
        int textY = rect.y + ((rect.height - fm.getHeight()) / 2) + fm.getAscent();
        g2.drawString(text, textX, textY);
    }

    private void drawStatsPanel(Graphics2D g2) {
        int panelX = 200, panelY = 150, panelWidth = 800, panelHeight = 600;

        g2.setColor(statsOverlay);
        g2.fillRoundRect(panelX, panelY, panelWidth, panelHeight, 30, 30);

        g2.setColor(Color.WHITE);
        g2.setFont(new Font("Arial", Font.PLAIN, 20));

        int textX = panelX + 50;
        int textY = panelY + 50;
        int lineSpacing = 35;

        g2.drawString("Player Stats:", textX, textY);
        textY += lineSpacing;
        g2.drawString("Level: " + player.getLevel(), textX, textY);
        textY += lineSpacing;
        g2.drawString("Max Health: " + player.getMaxHealth(), textX, textY);
        textY += lineSpacing;
        g2.drawString("Speed: " + player.getSpeed(), textX, textY);
        textY += lineSpacing;
        g2.drawString("Health Regen: " + player.getHealthRegen(), textX, textY);
        textY += lineSpacing;
        g2.drawString("XP: " + player.getXP() + "/" + player.getMaxXP(), textX, textY);
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_ESCAPE) resumeGame();
    }

    @Override
    public void keyReleased(KeyEvent e) {}

    @Override
    public void mousePressed(MouseEvent e) {
        Point p = e.getPoint();

        if (showStats) {
            showStats = false;
            return;
        }

        if (resumeButton.contains(p)) {
            resumeGame();
        } else if (statsButton.contains(p)) {
            showStats = !showStats;
        } else if (levelUpButton.contains(p)) {
            levelUpPlayer();
        } else if (quitButton.contains(p)) {
            System.exit(0);
        }
    }

    private void resumeGame() {
        player.resetInput();
        gameWorld.getStateStack().pop();
    }

    private void levelUpPlayer() {
        if (player.checkLevelUp()) {
            gameWorld.getStateStack().push(new LevelUpState(gameWorld));
        }
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        mousePosition = e.getPoint();
    }
}
