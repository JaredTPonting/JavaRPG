package states;

import core.GameWorld;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.geom.RoundRectangle2D;

public class GameOverState implements GameState {

    private final GameWorld gameWorld;
    private Point mousePosition;

    // Colors
    private static final Color OVERLAY_TOP = new Color(80, 0, 0, 200);
    private static final Color OVERLAY_BOTTOM = new Color(40, 0, 0, 220);
    private static final Color SHADOW_COLOR = new Color(0, 0, 0, 100);
    private static final Color BUTTON_COLOR = new Color(255, 90, 60);
    private static final Color BUTTON_HOVER = new Color(255, 120, 90);

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
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

        // Render game state beneath (frozen)
        GameState stateBeneath = gameWorld.getStateStack().peekBelowTop();
        if (stateBeneath != null) {
            stateBeneath.render(g2);
        }

        int width = gameWorld.getGameWidth();
        int height = gameWorld.getGameHeight();

        // Dark red gradient overlay
        GradientPaint gradient = new GradientPaint(0, 0, OVERLAY_TOP, 0, height, OVERLAY_BOTTOM);
        g2.setPaint(gradient);
        g2.fillRect(0, 0, width, height);

        // "YOU DIED" text
        int titleFontSize = Math.min(80, width / 12);
        g2.setFont(new Font("Arial", Font.BOLD, titleFontSize));
        String deathText = "YOU DIED";
        FontMetrics fm = g2.getFontMetrics();
        int textX = (width - fm.stringWidth(deathText)) / 2;
        int textY = (int) (height * 0.35);

        // Text shadow
        g2.setColor(SHADOW_COLOR);
        g2.drawString(deathText, textX + 4, textY + 4);

        // Text with red glow effect
        g2.setColor(new Color(200, 0, 0));
        g2.drawString(deathText, textX + 2, textY + 2);
        g2.setColor(Color.WHITE);
        g2.drawString(deathText, textX, textY);

        // Stats summary
        var playerManager = gameWorld.getPlayer().getPlayerManager();
        int infoFontSize = Math.min(24, width / 40);
        g2.setFont(new Font("Arial", Font.PLAIN, infoFontSize));
        g2.setColor(new Color(255, 200, 120));

        String levelInfo = "Level Reached: " + playerManager.getLevel();
        fm = g2.getFontMetrics();
        g2.drawString(levelInfo, (width - fm.stringWidth(levelInfo)) / 2, textY + 60);

        // Main Menu button
        int buttonWidth = Math.min(280, width / 4);
        int buttonHeight = Math.min(60, height / 12);
        int buttonX = (width - buttonWidth) / 2;
        int buttonY = (int) (height * 0.55);
        Rectangle mainMenuBtn = new Rectangle(buttonX, buttonY, buttonWidth, buttonHeight);

        drawButton(g2, mainMenuBtn, "MAIN MENU", buttonHeight);

        // Instructions
        int instrFontSize = Math.min(16, width / 60);
        g2.setFont(new Font("Arial", Font.ITALIC, instrFontSize));
        g2.setColor(new Color(255, 255, 255, 150));
        String instr = "Press ESC or click button to continue";
        fm = g2.getFontMetrics();
        g2.drawString(instr, (width - fm.stringWidth(instr)) / 2, height - 40);
    }

    private void drawButton(Graphics2D g2, Rectangle rect, String text, int buttonHeight) {
        boolean hover = mousePosition != null && rect.contains(mousePosition);

        // Shadow
        g2.setColor(SHADOW_COLOR);
        g2.fill(new RoundRectangle2D.Double(rect.x + 3, rect.y + 3, rect.width, rect.height, 20, 20));

        // Background
        g2.setColor(hover ? BUTTON_HOVER : BUTTON_COLOR);
        g2.fill(new RoundRectangle2D.Double(rect.x, rect.y, rect.width, rect.height, 20, 20));

        // Border
        g2.setColor(Color.WHITE);
        g2.setStroke(new BasicStroke(2.5f));
        g2.draw(new RoundRectangle2D.Double(rect.x, rect.y, rect.width, rect.height, 20, 20));

        // Text
        int fontSize = Math.min(28, buttonHeight / 2 + 4);
        g2.setFont(new Font("Arial", Font.BOLD, fontSize));
        FontMetrics fm = g2.getFontMetrics();
        int textX = rect.x + (rect.width - fm.stringWidth(text)) / 2;
        int textY = rect.y + ((rect.height - fm.getHeight()) / 2) + fm.getAscent();

        g2.setColor(Color.WHITE);
        g2.drawString(text, textX, textY);
    }

    private Rectangle getButtonBounds() {
        int width = gameWorld.getGameWidth();
        int height = gameWorld.getGameHeight();
        int buttonWidth = Math.min(280, width / 4);
        int buttonHeight = Math.min(60, height / 12);
        int buttonX = (width - buttonWidth) / 2;
        int buttonY = (int) (height * 0.55);
        return new Rectangle(buttonX, buttonY, buttonWidth, buttonHeight);
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_ESCAPE || e.getKeyCode() == KeyEvent.VK_ENTER) {
            goToMainMenu();
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {}

    @Override
    public void mousePressed(MouseEvent e) {
        if (getButtonBounds().contains(e.getPoint())) {
            goToMainMenu();
        }
    }

    private void goToMainMenu() {
        gameWorld.getStateStack().resetToMainMenu(gameWorld);
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        mousePosition = e.getPoint();
    }
}
