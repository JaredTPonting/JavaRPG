package ui;

import java.awt.*;
import java.awt.geom.RoundRectangle2D;
import entities.player.PlayerManager;

public class UI {
    private PlayerManager playerManager;

    private float displayedHealth;
    private float displayedXP;
    private float displayedStamina;

    // Game timer (in seconds)
    private double elapsedTime = 0;

    // Colors matching home screen style
    private static final Color OVERLAY_COLOR = new Color(0, 0, 0, 150);
    private static final Color SHADOW_COLOR = new Color(0, 0, 0, 50);
    private static final Color BAR_BACKGROUND = new Color(60, 40, 30);
    private static final Color BAR_BORDER = new Color(255, 200, 120);
    private static final Color HEALTH_COLOR = new Color(220, 60, 60);
    private static final Color HEALTH_GLOW = new Color(255, 100, 100);
    private static final Color STAMINA_COLOR = new Color(60, 180, 60);
    private static final Color STAMINA_GLOW = new Color(100, 220, 100);
    private static final Color XP_COLOR = new Color(255, 180, 60);
    private static final Color XP_GLOW = new Color(255, 220, 100);
    private static final Color TEXT_COLOR = new Color(255, 255, 255);
    private static final Color TIMER_BG = new Color(255, 90, 60);

    public UI(PlayerManager playerManager) {
        this.playerManager = playerManager;
        this.displayedHealth = (float) playerManager.getMaxHealth();
        this.displayedStamina = (float) playerManager.getMaxStamina();
        this.displayedXP = playerManager.getXP();
    }

    public void update(double dt) {
        elapsedTime += dt;
        displayedHealth = lerp(displayedHealth, (float) playerManager.getCurrentHealth(), 0.1f);
        displayedStamina = lerp(displayedStamina, (float) playerManager.getCurrentStamina(), 0.1f);
        displayedXP = lerp(displayedXP, (float) playerManager.getXP(), 0.1f);
    }

    private float lerp(float a, float b, float f) {
        return a + f * (b - a);
    }

    public void render(Graphics2D g, int screenWidth, int screenHeight) {
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

        // Responsive sizing
        int barWidth = Math.min(400, (int) (screenWidth * 0.35));
        int barHeight = Math.max(16, Math.min(22, screenHeight / 40));
        int padding = Math.max(12, screenWidth / 80);
        int spacing = Math.max(6, barHeight / 3);
        int arc = barHeight / 2;

        int x = padding;
        int y = padding;

        // Panel background with shadow
        int panelWidth = barWidth + padding * 2;
        int panelHeight = (barHeight + spacing) * 3 + padding;

        g.setColor(SHADOW_COLOR);
        g.fill(new RoundRectangle2D.Double(x + 3, y + 3, panelWidth, panelHeight, 15, 15));

        g.setColor(OVERLAY_COLOR);
        g.fill(new RoundRectangle2D.Double(x, y, panelWidth, panelHeight, 15, 15));

        // Bars
        int barX = x + padding;
        int barY = y + padding / 2;

        drawBar(g, barX, barY, barWidth, barHeight, arc,
                (float) (displayedHealth / playerManager.getMaxHealth()),
                HEALTH_COLOR, HEALTH_GLOW, "HP");

        barY += barHeight + spacing;
        drawBar(g, barX, barY, barWidth, barHeight, arc,
                (float) (displayedStamina / playerManager.getMaxStamina()),
                STAMINA_COLOR, STAMINA_GLOW, "STAMINA");

        barY += barHeight + spacing;
        String xpLabel = playerManager.checkLevelUp() ? "LEVEL UP!" : "XP";
        drawBar(g, barX, barY, barWidth, barHeight, arc,
                (float) (displayedXP / playerManager.getMaxXP()),
                XP_COLOR, XP_GLOW, xpLabel);

        // Draw timer and level in top right
        drawTimer(g, screenWidth, screenHeight);
    }

    private void drawTimer(Graphics2D g, int screenWidth, int screenHeight) {
        int totalSeconds = (int) elapsedTime;
        int hours = totalSeconds / 3600;
        int minutes = (totalSeconds % 3600) / 60;
        int seconds = totalSeconds % 60;

        String timeStr;
        if (hours > 0) {
            timeStr = String.format("%d:%02d:%02d", hours, minutes, seconds);
        } else {
            timeStr = String.format("%02d:%02d", minutes, seconds);
        }

        // Responsive font size
        int fontSize = Math.max(14, Math.min(20, screenWidth / 60));
        Font timerFont = new Font("Arial", Font.BOLD, fontSize);
        g.setFont(timerFont);
        FontMetrics fm = g.getFontMetrics();

        int padding = Math.max(10, screenWidth / 100);
        int boxPadding = padding / 2;
        int boxWidth = fm.stringWidth(timeStr) + boxPadding * 4;
        int boxHeight = fm.getHeight() + boxPadding * 2;
        int boxX = screenWidth - boxWidth - padding;
        int boxY = padding;

        // Shadow
        g.setColor(SHADOW_COLOR);
        g.fill(new RoundRectangle2D.Double(boxX + 3, boxY + 3, boxWidth, boxHeight, 15, 15));

        // Background
        g.setColor(TIMER_BG);
        g.fill(new RoundRectangle2D.Double(boxX, boxY, boxWidth, boxHeight, 15, 15));

        // Border
        g.setColor(Color.WHITE);
        g.setStroke(new BasicStroke(2));
        g.draw(new RoundRectangle2D.Double(boxX, boxY, boxWidth, boxHeight, 15, 15));

        // Text
        g.setColor(TEXT_COLOR);
        int textX = boxX + (boxWidth - fm.stringWidth(timeStr)) / 2;
        int textY = boxY + ((boxHeight - fm.getHeight()) / 2) + fm.getAscent();
        g.drawString(timeStr, textX, textY);

        // Level indicator below timer
        String levelStr = "Lv. " + playerManager.getLevel();
        int levelFontSize = fontSize - 2;
        g.setFont(new Font("Arial", Font.BOLD, levelFontSize));
        fm = g.getFontMetrics();

        int levelBoxWidth = fm.stringWidth(levelStr) + boxPadding * 4;
        int levelBoxHeight = fm.getHeight() + boxPadding;
        int levelBoxX = screenWidth - levelBoxWidth - padding;
        int levelBoxY = boxY + boxHeight + 8;

        g.setColor(SHADOW_COLOR);
        g.fill(new RoundRectangle2D.Double(levelBoxX + 2, levelBoxY + 2, levelBoxWidth, levelBoxHeight, 10, 10));

        g.setColor(OVERLAY_COLOR);
        g.fill(new RoundRectangle2D.Double(levelBoxX, levelBoxY, levelBoxWidth, levelBoxHeight, 10, 10));

        g.setColor(XP_COLOR);
        g.setStroke(new BasicStroke(1.5f));
        g.draw(new RoundRectangle2D.Double(levelBoxX, levelBoxY, levelBoxWidth, levelBoxHeight, 10, 10));

        g.setColor(TEXT_COLOR);
        textX = levelBoxX + (levelBoxWidth - fm.stringWidth(levelStr)) / 2;
        textY = levelBoxY + ((levelBoxHeight - fm.getHeight()) / 2) + fm.getAscent();
        g.drawString(levelStr, textX, textY);
    }

    private void drawBar(Graphics2D g, int x, int y, int width, int height, int arc,
                         float fillPercent, Color fillColor, Color glowColor, String label) {
        fillPercent = Math.max(0, Math.min(1, fillPercent));

        // Bar background
        g.setColor(BAR_BACKGROUND);
        g.fill(new RoundRectangle2D.Double(x, y, width, height, arc, arc));

        // Fill
        int fillWidth = (int) (width * fillPercent);
        if (fillWidth > 0) {
            // Gradient fill for depth
            GradientPaint gradient = new GradientPaint(
                    x, y, glowColor,
                    x, y + height, fillColor
            );
            g.setPaint(gradient);
            g.fill(new RoundRectangle2D.Double(x, y, fillWidth, height, arc, arc));
        }

        // Border
        g.setColor(BAR_BORDER);
        g.setStroke(new BasicStroke(1.5f));
        g.draw(new RoundRectangle2D.Double(x, y, width, height, arc, arc));

        // Label with shadow
        int fontSize = Math.max(10, height - 6);
        g.setFont(new Font("Arial", Font.BOLD, fontSize));
        FontMetrics fm = g.getFontMetrics();

        int textX = x + 8;
        int textY = y + ((height - fm.getHeight()) / 2) + fm.getAscent();

        g.setColor(new Color(0, 0, 0, 100));
        g.drawString(label, textX + 1, textY + 1);

        g.setColor(TEXT_COLOR);
        g.drawString(label, textX, textY);
    }
}
