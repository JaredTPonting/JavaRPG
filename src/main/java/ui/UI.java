package ui;

import java.awt.*;
import player.Player;

public class UI {
    private Player player;

    private float displayedHealth;
    private float displayedXP;
    private float displayedStamina;

    public UI(Player player) {
        this.player = player;
        this.displayedHealth = player.getMaxXP();
        this.displayedStamina = (float) player.getMaxStamina();
        this.displayedXP = player.getXP();
    }

    public void update() {
        displayedHealth = lerp(displayedHealth, (float) player.getCurrentHealth(), 0.1f);
        displayedStamina = lerp(displayedStamina, (float) player.getCurrentStamina(), 0.1f);
        displayedXP = lerp(displayedXP, (float) player.getXP(), 0.1f);
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

        g.setColor(new Color(0, 0, 0, 120));
        g.fillRoundRect(x - 10, y - 10, barWidth + 20, (barHeight + spacing) * 3 + 20, arc, arc);

        drawBar(g, x, y, barWidth, barHeight, arc, (float) (displayedHealth / player.getMaxHealth()), new Color(200, 50, 50), "HP");

        y += barHeight + spacing;
        drawBar(g, x, y, barWidth, barHeight, arc, (float) (displayedStamina / player.getMaxStamina()), new Color(38, 156, 31), "STAMINA");

        y += barHeight + spacing;
        drawBar(g, x, y, barWidth, barHeight, arc, (float) (displayedXP / player.getMaxXP()), new Color(38, 156, 215), "XP");


    }

    private void drawBar(Graphics2D g, int x, int y, int width, int height, int arc, float fillPercent, Color colour, String label) {
        fillPercent = Math.max(0, Math.min(1, fillPercent));

        g.setColor(new Color(50, 50, 50));
        g.fillRoundRect(x, y, width, height, arc, arc);

        int fillWidth = (int) (width * fillPercent);
        g.setColor(colour);
        g.fillRoundRect(x, y, fillWidth, height, arc, arc);

        g.setFont(new Font("Arial", Font.BOLD, 12));
        g.setColor(Color.WHITE);
        g.drawString(label, x + 5, y + height - 5);
    }
}
