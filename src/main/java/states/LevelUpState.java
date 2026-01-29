package states;

import entities.player.Player;
import core.GameWorld;
import entities.player.PlayerManager;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.geom.RoundRectangle2D;
import java.util.ArrayList;
import java.util.List;

public class LevelUpState implements GameState {
    private final PlayerManager playerManager;
    private final Player player;
    private final GameWorld gameWorld;
    private final boolean shouldResumeOnClose;

    private Point mousePosition;

    // Colors matching home screen
    private static final Color BG_TOP = new Color(255, 200, 120, 220);
    private static final Color BG_BOTTOM = new Color(255, 140, 70, 220);
    private static final Color BUTTON_COLOR = new Color(255, 90, 60);
    private static final Color BUTTON_HOVER = new Color(255, 120, 90);
    private static final Color BUTTON_DISABLED = new Color(120, 80, 60);
    private static final Color CLOSE_BUTTON_COLOR = new Color(80, 80, 80);
    private static final Color CLOSE_BUTTON_HOVER = new Color(120, 120, 120);
    private static final Color SHADOW_COLOR = new Color(0, 0, 0, 50);
    private static final Color PANEL_COLOR = new Color(0, 0, 0, 180);

    // Upgrade options
    private static final String[] UPGRADE_NAMES = {
            "Max Health", "Health Regen", "Move Speed", "Damage",
            "Magic Damage", "Endurance", "Stamina Regen"
    };

    private final Runnable[] upgradeActions;

    public LevelUpState(GameWorld gameWorld) {
        this(gameWorld, true);
    }

    public LevelUpState(GameWorld gameWorld, boolean shouldResumeOnClose) {
        this.gameWorld = gameWorld;
        this.player = gameWorld.getPlayer();
        this.playerManager = gameWorld.getPlayer().getPlayerManager();
        this.shouldResumeOnClose = shouldResumeOnClose;

        upgradeActions = new Runnable[] {
                playerManager::levelUpHealth,
                playerManager::levelUpHealthRegen,
                playerManager::levelUpSpeed,
                playerManager::levelUpDamage,
                playerManager::levelUpMagicDamage,
                playerManager::levelUpEndurance,
                playerManager::levelUpStaminaRegen
        };
    }

    @Override
    public void update() {
        // no movement
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

        // Panel
        int panelWidth = Math.min(450, (int) (width * 0.5));
        int panelHeight = Math.min(500, (int) (height * 0.7));
        int panelX = (width - panelWidth) / 2;
        int panelY = (height - panelHeight) / 2;

        // Panel shadow
        g2.setColor(SHADOW_COLOR);
        g2.fill(new RoundRectangle2D.Double(panelX + 5, panelY + 5, panelWidth, panelHeight, 25, 25));

        // Panel background
        g2.setColor(PANEL_COLOR);
        g2.fill(new RoundRectangle2D.Double(panelX, panelY, panelWidth, panelHeight, 25, 25));

        // Panel border
        g2.setColor(new Color(255, 200, 120));
        g2.setStroke(new BasicStroke(3));
        g2.draw(new RoundRectangle2D.Double(panelX, panelY, panelWidth, panelHeight, 25, 25));

        // Title
        int titleFontSize = Math.min(36, panelWidth / 10);
        g2.setFont(new Font("Arial", Font.BOLD, titleFontSize));
        String title = "LEVEL UP!";
        FontMetrics fm = g2.getFontMetrics();
        int titleX = panelX + (panelWidth - fm.stringWidth(title)) / 2;
        int titleY = panelY + 50;

        g2.setColor(SHADOW_COLOR);
        g2.drawString(title, titleX + 2, titleY + 2);
        g2.setColor(new Color(255, 220, 100));
        g2.drawString(title, titleX, titleY);

        // XP info
        int infoFontSize = Math.min(18, panelWidth / 22);
        g2.setFont(new Font("Arial", Font.PLAIN, infoFontSize));
        String xpInfo = "XP: " + playerManager.getXP() + " / " + playerManager.getMaxXP();
        fm = g2.getFontMetrics();
        g2.setColor(Color.WHITE);
        g2.drawString(xpInfo, panelX + (panelWidth - fm.stringWidth(xpInfo)) / 2, titleY + 30);

        // Buttons
        int buttonWidth = Math.min(200, (int) (panelWidth * 0.7));
        int buttonHeight = Math.min(40, panelHeight / 14);
        int buttonX = panelX + (panelWidth - buttonWidth) / 2;
        int buttonSpacing = buttonHeight + 10;
        int startY = titleY + 55;

        boolean canLevel = playerManager.checkLevelUp();

        for (int i = 0; i < UPGRADE_NAMES.length; i++) {
            Rectangle btn = new Rectangle(buttonX, startY + buttonSpacing * i, buttonWidth, buttonHeight);
            drawUpgradeButton(g2, btn, "+ " + UPGRADE_NAMES[i], buttonHeight, canLevel);
        }

        // Close button
        int closeButtonY = startY + buttonSpacing * UPGRADE_NAMES.length + 15;
        Rectangle closeBtn = new Rectangle(buttonX, closeButtonY, buttonWidth, buttonHeight);
        drawCloseButton(g2, closeBtn, "BACK TO GAME", buttonHeight);
    }

    private void drawUpgradeButton(Graphics2D g2, Rectangle rect, String text, int buttonHeight, boolean enabled) {
        boolean hover = mousePosition != null && rect.contains(mousePosition);

        // Shadow
        g2.setColor(SHADOW_COLOR);
        g2.fill(new RoundRectangle2D.Double(rect.x + 2, rect.y + 2, rect.width, rect.height, 15, 15));

        // Background
        Color bgColor;
        if (!enabled) {
            bgColor = BUTTON_DISABLED;
        } else if (hover) {
            bgColor = BUTTON_HOVER;
        } else {
            bgColor = BUTTON_COLOR;
        }
        g2.setColor(bgColor);
        g2.fill(new RoundRectangle2D.Double(rect.x, rect.y, rect.width, rect.height, 15, 15));

        // Border
        g2.setColor(enabled ? Color.WHITE : new Color(180, 180, 180));
        g2.setStroke(new BasicStroke(2));
        g2.draw(new RoundRectangle2D.Double(rect.x, rect.y, rect.width, rect.height, 15, 15));

        // Text
        int fontSize = Math.min(18, buttonHeight / 2 + 4);
        g2.setFont(new Font("Arial", Font.BOLD, fontSize));
        FontMetrics fm = g2.getFontMetrics();
        int textX = rect.x + (rect.width - fm.stringWidth(text)) / 2;
        int textY = rect.y + ((rect.height - fm.getHeight()) / 2) + fm.getAscent();

        g2.setColor(enabled ? Color.WHITE : new Color(180, 180, 180));
        g2.drawString(text, textX, textY);
    }

    private void drawCloseButton(Graphics2D g2, Rectangle rect, String text, int buttonHeight) {
        boolean hover = mousePosition != null && rect.contains(mousePosition);

        // Shadow
        g2.setColor(SHADOW_COLOR);
        g2.fill(new RoundRectangle2D.Double(rect.x + 2, rect.y + 2, rect.width, rect.height, 15, 15));

        // Background
        g2.setColor(hover ? CLOSE_BUTTON_HOVER : CLOSE_BUTTON_COLOR);
        g2.fill(new RoundRectangle2D.Double(rect.x, rect.y, rect.width, rect.height, 15, 15));

        // Border
        g2.setColor(Color.WHITE);
        g2.setStroke(new BasicStroke(2));
        g2.draw(new RoundRectangle2D.Double(rect.x, rect.y, rect.width, rect.height, 15, 15));

        // Text
        int fontSize = Math.min(16, buttonHeight / 2 + 2);
        g2.setFont(new Font("Arial", Font.BOLD, fontSize));
        FontMetrics fm = g2.getFontMetrics();
        int textX = rect.x + (rect.width - fm.stringWidth(text)) / 2;
        int textY = rect.y + ((rect.height - fm.getHeight()) / 2) + fm.getAscent();

        g2.setColor(Color.WHITE);
        g2.drawString(text, textX, textY);
    }

    private Rectangle getButtonBounds(int index) {
        int width = gameWorld.getGameWidth();
        int height = gameWorld.getGameHeight();

        int panelWidth = Math.min(450, (int) (width * 0.5));
        int panelHeight = Math.min(500, (int) (height * 0.7));
        int panelX = (width - panelWidth) / 2;
        int panelY = (height - panelHeight) / 2;

        int titleFontSize = Math.min(36, panelWidth / 10);
        int titleY = panelY + 50;

        int buttonWidth = Math.min(200, (int) (panelWidth * 0.7));
        int buttonHeight = Math.min(40, panelHeight / 14);
        int buttonX = panelX + (panelWidth - buttonWidth) / 2;
        int buttonSpacing = buttonHeight + 10;
        int startY = titleY + 55;

        return new Rectangle(buttonX, startY + buttonSpacing * index, buttonWidth, buttonHeight);
    }

    private Rectangle getCloseButtonBounds() {
        int width = gameWorld.getGameWidth();
        int height = gameWorld.getGameHeight();

        int panelWidth = Math.min(450, (int) (width * 0.5));
        int panelHeight = Math.min(500, (int) (height * 0.7));
        int panelX = (width - panelWidth) / 2;
        int panelY = (height - panelHeight) / 2;

        int titleY = panelY + 50;

        int buttonWidth = Math.min(200, (int) (panelWidth * 0.7));
        int buttonHeight = Math.min(40, panelHeight / 14);
        int buttonX = panelX + (panelWidth - buttonWidth) / 2;
        int buttonSpacing = buttonHeight + 10;
        int startY = titleY + 55;

        int closeButtonY = startY + buttonSpacing * UPGRADE_NAMES.length + 15;
        return new Rectangle(buttonX, closeButtonY, buttonWidth, buttonHeight);
    }

    private void closeScreen() {
        if (shouldResumeOnClose) {
            player.resetInput();
            gameWorld.getDeltaTimer().resume();
        }
        gameWorld.getStateStack().pop();
    }

    @Override
    public void mousePressed(MouseEvent e) {
        Point p = e.getPoint();

        // Check close button first
        if (getCloseButtonBounds().contains(p)) {
            closeScreen();
            return;
        }

        // Check upgrade buttons
        if (!playerManager.checkLevelUp()) return;

        for (int i = 0; i < upgradeActions.length; i++) {
            if (getButtonBounds(i).contains(p)) {
                upgradeActions[i].run();
                break;
            }
        }
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        mousePosition = e.getPoint();
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
            closeScreen();
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
    }
}
