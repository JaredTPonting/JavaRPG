package states;

import entities.player.Player;
import core.GameWorld;
import entities.player.PlayerManager;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.geom.RoundRectangle2D;

public class PauseState implements GameState {

    private final PlayerManager playerManager;
    private final Player player;
    private final GameWorld gameWorld;
    private Point mousePosition;

    private boolean showStats = false;

    // Colors matching home screen
    private static final Color BG_TOP = new Color(255, 200, 120, 200);
    private static final Color BG_BOTTOM = new Color(255, 140, 70, 200);
    private static final Color OVERLAY_COLOR = new Color(0, 0, 0, 120);
    private static final Color BUTTON_COLOR = new Color(255, 90, 60);
    private static final Color BUTTON_HOVER = new Color(255, 120, 90);
    private static final Color SHADOW_COLOR = new Color(0, 0, 0, 50);
    private static final Color PANEL_COLOR = new Color(0, 0, 0, 200);

    public PauseState(GameWorld gameWorld) {
        this.gameWorld = gameWorld;
        this.playerManager = gameWorld.getPlayer().getPlayerManager();
        this.player = gameWorld.getPlayer();
    }

    @Override
    public void update() {
        // No updates while paused
    }

    @Override
    public void render(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

        int width = gameWorld.getGameWidth();
        int height = gameWorld.getGameHeight();

        // Gradient overlay
        GradientPaint gradient = new GradientPaint(0, 0, BG_TOP, 0, height, BG_BOTTOM);
        g2.setPaint(gradient);
        g2.fillRect(0, 0, width, height);

        // Title
        int titleFontSize = Math.min(48, width / 20);
        g2.setFont(new Font("Arial", Font.BOLD, titleFontSize));
        String title = "PAUSED";
        FontMetrics fm = g2.getFontMetrics();
        int titleX = (width - fm.stringWidth(title)) / 2;
        int titleY = (int) (height * 0.15);

        // Title shadow
        g2.setColor(SHADOW_COLOR);
        g2.drawString(title, titleX + 3, titleY + 3);
        g2.setColor(Color.WHITE);
        g2.drawString(title, titleX, titleY);

        // Responsive button sizing
        int buttonWidth = Math.min(280, width / 4);
        int buttonHeight = Math.min(55, height / 14);
        int buttonX = (width - buttonWidth) / 2;
        int buttonSpacing = buttonHeight + 15;
        int startY = (int) (height * 0.28);

        // Buttons
        Rectangle resumeBtn = new Rectangle(buttonX, startY, buttonWidth, buttonHeight);
        Rectangle statsBtn = new Rectangle(buttonX, startY + buttonSpacing, buttonWidth, buttonHeight);
        Rectangle levelUpBtn = new Rectangle(buttonX, startY + buttonSpacing * 2, buttonWidth, buttonHeight);
        Rectangle mainMenuBtn = new Rectangle(buttonX, startY + buttonSpacing * 3, buttonWidth, buttonHeight);
        Rectangle quitBtn = new Rectangle(buttonX, startY + buttonSpacing * 4, buttonWidth, buttonHeight);

        drawButton(g2, resumeBtn, "RESUME", buttonHeight);
        drawButton(g2, statsBtn, "STATS", buttonHeight);

        // Level up button - highlight if can level up
        boolean canLevel = playerManager.checkLevelUp();
        drawButton(g2, levelUpBtn, canLevel ? "LEVEL UP!" : "LEVEL UP", buttonHeight, canLevel);

        drawButton(g2, mainMenuBtn, "MAIN MENU", buttonHeight);
        drawButton(g2, quitBtn, "QUIT GAME", buttonHeight);

        // Instructions
        int instrFontSize = Math.min(16, width / 60);
        g2.setFont(new Font("Arial", Font.PLAIN, instrFontSize));
        String instr = "Press ESC to resume";
        fm = g2.getFontMetrics();
        g2.setColor(new Color(255, 255, 255, 180));
        g2.drawString(instr, (width - fm.stringWidth(instr)) / 2, height - 30);

        // Draw stats panel if toggled
        if (showStats) {
            drawStatsPanel(g2, width, height);
        }
    }

    private void drawButton(Graphics2D g2, Rectangle rect, String text, int buttonHeight) {
        drawButton(g2, rect, text, buttonHeight, false);
    }

    private void drawButton(Graphics2D g2, Rectangle rect, String text, int buttonHeight, boolean highlight) {
        boolean hover = mousePosition != null && rect.contains(mousePosition);

        // Shadow
        g2.setColor(SHADOW_COLOR);
        g2.fill(new RoundRectangle2D.Double(rect.x + 3, rect.y + 3, rect.width, rect.height, 20, 20));

        // Background
        Color bgColor = hover ? BUTTON_HOVER : BUTTON_COLOR;
        if (highlight && !hover) {
            bgColor = new Color(255, 200, 60); // Gold for level up available
        }
        g2.setColor(bgColor);
        g2.fill(new RoundRectangle2D.Double(rect.x, rect.y, rect.width, rect.height, 20, 20));

        // Border
        g2.setColor(Color.WHITE);
        g2.setStroke(new BasicStroke(2.5f));
        g2.draw(new RoundRectangle2D.Double(rect.x, rect.y, rect.width, rect.height, 20, 20));

        // Text
        int fontSize = Math.min(24, buttonHeight / 2 + 2);
        g2.setFont(new Font("Arial", Font.BOLD, fontSize));
        FontMetrics fm = g2.getFontMetrics();
        int textX = rect.x + (rect.width - fm.stringWidth(text)) / 2;
        int textY = rect.y + ((rect.height - fm.getHeight()) / 2) + fm.getAscent();

        g2.setColor(new Color(0, 0, 0, 80));
        g2.drawString(text, textX + 1, textY + 1);
        g2.setColor(Color.WHITE);
        g2.drawString(text, textX, textY);
    }

    private void drawStatsPanel(Graphics2D g2, int width, int height) {
        int panelWidth = Math.min(500, (int) (width * 0.6));
        int panelHeight = Math.min(400, (int) (height * 0.5));
        int panelX = (width - panelWidth) / 2;
        int panelY = (height - panelHeight) / 2;

        // Shadow
        g2.setColor(SHADOW_COLOR);
        g2.fill(new RoundRectangle2D.Double(panelX + 5, panelY + 5, panelWidth, panelHeight, 20, 20));

        // Panel background
        g2.setColor(PANEL_COLOR);
        g2.fill(new RoundRectangle2D.Double(panelX, panelY, panelWidth, panelHeight, 20, 20));

        // Border
        g2.setColor(new Color(255, 200, 120));
        g2.setStroke(new BasicStroke(3));
        g2.draw(new RoundRectangle2D.Double(panelX, panelY, panelWidth, panelHeight, 20, 20));

        // Title
        int titleFontSize = Math.min(28, panelWidth / 15);
        g2.setFont(new Font("Arial", Font.BOLD, titleFontSize));
        g2.setColor(Color.WHITE);
        String statsTitle = "PLAYER STATS";
        FontMetrics fm = g2.getFontMetrics();
        g2.drawString(statsTitle, panelX + (panelWidth - fm.stringWidth(statsTitle)) / 2, panelY + 40);

        // Stats
        int statFontSize = Math.min(18, panelWidth / 25);
        g2.setFont(new Font("Arial", Font.PLAIN, statFontSize));
        int textX = panelX + 40;
        int textY = panelY + 80;
        int lineSpacing = statFontSize + 12;

        Color labelColor = new Color(255, 200, 120);
        Color valueColor = Color.WHITE;

        drawStat(g2, textX, textY, "Level", String.valueOf(playerManager.getLevel()), labelColor, valueColor, panelWidth - 80);
        textY += lineSpacing;
        drawStat(g2, textX, textY, "XP", playerManager.getXP() + " / " + playerManager.getMaxXP(), labelColor, valueColor, panelWidth - 80);
        textY += lineSpacing;
        drawStat(g2, textX, textY, "Max Health", String.format("%.0f", playerManager.getMaxHealth()), labelColor, valueColor, panelWidth - 80);
        textY += lineSpacing;
        drawStat(g2, textX, textY, "Health Regen", String.format("%.1f/s", playerManager.getHealthRegen()), labelColor, valueColor, panelWidth - 80);
        textY += lineSpacing;
        drawStat(g2, textX, textY, "Speed", String.format("%.0f", playerManager.getSpeed()), labelColor, valueColor, panelWidth - 80);
        textY += lineSpacing;
        drawStat(g2, textX, textY, "Damage", String.format("%.0f", playerManager.getDamage()), labelColor, valueColor, panelWidth - 80);
        textY += lineSpacing;
        drawStat(g2, textX, textY, "Magic Damage", String.format("%.0f", playerManager.getMagicDamage()), labelColor, valueColor, panelWidth - 80);
        textY += lineSpacing;
        drawStat(g2, textX, textY, "Max Stamina", String.format("%.0f", playerManager.getMaxStamina()), labelColor, valueColor, panelWidth - 80);
        textY += lineSpacing;
        drawStat(g2, textX, textY, "Stamina Regen", String.format("%.1f/s", playerManager.getStaminaRegen()), labelColor, valueColor, panelWidth - 80);

        // Close hint
        g2.setFont(new Font("Arial", Font.ITALIC, Math.min(14, panelWidth / 30)));
        g2.setColor(new Color(255, 255, 255, 150));
        String closeHint = "Click anywhere to close";
        fm = g2.getFontMetrics();
        g2.drawString(closeHint, panelX + (panelWidth - fm.stringWidth(closeHint)) / 2, panelY + panelHeight - 20);
    }

    private void drawStat(Graphics2D g2, int x, int y, String label, String value, Color labelColor, Color valueColor, int width) {
        g2.setColor(labelColor);
        g2.drawString(label + ":", x, y);

        FontMetrics fm = g2.getFontMetrics();
        g2.setColor(valueColor);
        g2.drawString(value, x + width - fm.stringWidth(value), y);
    }

    // Button bounds - recalculated for responsive layout
    private Rectangle getButtonBounds(int index) {
        int width = gameWorld.getGameWidth();
        int height = gameWorld.getGameHeight();
        int buttonWidth = Math.min(280, width / 4);
        int buttonHeight = Math.min(55, height / 14);
        int buttonX = (width - buttonWidth) / 2;
        int buttonSpacing = buttonHeight + 15;
        int startY = (int) (height * 0.28);
        return new Rectangle(buttonX, startY + buttonSpacing * index, buttonWidth, buttonHeight);
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
            if (showStats) {
                showStats = false;
            } else {
                resumeGame();
            }
        }
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

        if (getButtonBounds(0).contains(p)) {
            resumeGame();
        } else if (getButtonBounds(1).contains(p)) {
            showStats = !showStats;
        } else if (getButtonBounds(2).contains(p)) {
            levelUpPlayer();
        } else if (getButtonBounds(3).contains(p)) {
            goToMainMenu();
        } else if (getButtonBounds(4).contains(p)) {
            System.exit(0);
        }
    }

    private void resumeGame() {
        player.resetInput();
        gameWorld.getDeltaTimer().resume();
        gameWorld.getStateStack().pop();
    }

    private void levelUpPlayer() {
        if (playerManager.checkLevelUp()) {
            // Don't resume timer when closing - we're still in pause menu
            gameWorld.getStateStack().push(new LevelUpState(gameWorld, false));
        }
    }

    private void goToMainMenu() {
        // Reset game state and return to main menu
        gameWorld.refresh();
        gameWorld.getDeltaTimer().resume();
        gameWorld.getStateStack().resetToMainMenu(gameWorld);
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        mousePosition = e.getPoint();
    }
}
