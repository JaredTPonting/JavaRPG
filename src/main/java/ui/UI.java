package ui;

import java.awt.*;
import entities.player.Player;
import entities.player.PlayerManager;

public class UI {
    private PlayerManager playerManager;

    private float displayedHealth;
    private float displayedXP;
    private float displayedStamina;

    // Cached colors to avoid allocation every frame
    private static final Color OVERLAY_COLOR = new Color(0, 0, 0, 120);
    private static final Color BAR_BACKGROUND = new Color(50, 50, 50);
    private static final Color HEALTH_COLOR = new Color(200, 50, 50);
    private static final Color STAMINA_COLOR = new Color(38, 156, 31);
    private static final Color XP_COLOR = new Color(38, 156, 215);
    private static final Font BAR_FONT = new Font("Arial", Font.BOLD, 12);

    public UI(PlayerManager playerManager) {
        this.playerManager = playerManager;
        this.displayedHealth = (float) playerManager.getMaxHealth();
        this.displayedStamina = (float) playerManager.getMaxStamina();
        this.displayedXP = playerManager.getXP();
    }

    public void update() {
        displayedHealth = lerp(displayedHealth, (float) playerManager.getCurrentHealth(), 0.1f);
        displayedStamina = lerp(displayedStamina, (float) playerManager.getCurrentStamina(), 0.1f);
        displayedXP = lerp(displayedXP, (float) playerManager.getXP(), 0.1f);
    }

    private float lerp(float a, float b, float f) {
        return a + f * (b - a);
    }

    public void render(Graphics2D g, int screenWidth, int screenHeight) {
        int barWidth = (int) (screenWidth * 0.4);
        int barHeight = 18;
        int x = 20;
        int y = 20;
        int spacing = 10;
        int arc = 10;

        g.setColor(OVERLAY_COLOR);
        g.fillRoundRect(x - 10, y - 10, barWidth + 20, (barHeight + spacing) * 3 + 20, arc, arc);

        drawBar(g, x, y, barWidth, barHeight, arc, (float) (displayedHealth / playerManager.getMaxHealth()), HEALTH_COLOR, "HP");

        y += barHeight + spacing;
        drawBar(g, x, y, barWidth, barHeight, arc, (float) (displayedStamina / playerManager.getMaxStamina()), STAMINA_COLOR, "STAMINA");

        y += barHeight + spacing;
        drawBar(g, x, y, barWidth, barHeight, arc, (float) (displayedXP / playerManager.getMaxXP()), XP_COLOR, playerManager.checkLevelUp() ? "CAN LEVEL UP" : "XP");
    }

    private void drawBar(Graphics2D g, int x, int y, int width, int height, int arc, float fillPercent, Color colour, String label) {
        fillPercent = Math.max(0, Math.min(1, fillPercent));

        g.setColor(BAR_BACKGROUND);
        g.fillRoundRect(x, y, width, height, arc, arc);

        int fillWidth = (int) (width * fillPercent);
        g.setColor(colour);
        g.fillRoundRect(x, y, fillWidth, height, arc, arc);

        g.setFont(BAR_FONT);
        g.setColor(Color.WHITE);
        g.drawString(label, x + 5, y + height - 5);
    }
}
