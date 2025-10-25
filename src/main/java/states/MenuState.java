package states;

import core.GameWorld;

import java.awt.*;
import java.awt.event.*;
import java.awt.geom.RoundRectangle2D;

public class MenuState implements GameState {

    private final GameWorld gameWorld;
    private Point mousePosition;

    // Button
    private final Rectangle playButton;
    private static final int BUTTON_WIDTH = 300;
    private static final int BUTTON_HEIGHT = 70;

    public MenuState(GameWorld game) {
        this.gameWorld = game;

        int centerX = gameWorld.getGameWidth() / 2 - BUTTON_WIDTH / 2;
        int buttonY = 300;
        playButton = new Rectangle(centerX, buttonY, BUTTON_WIDTH, BUTTON_HEIGHT);
    }

    @Override
    public void update() {}

    @Override
    public void render(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;

        int width = gameWorld.getGameWidth();
        int height = gameWorld.getGameHeight();

        // Gradient background
        GradientPaint gradient = new GradientPaint(0, 0, new Color(255, 165, 0), 0, height, new Color(255, 100, 50));
        g2.setPaint(gradient);
        g2.fillRect(0, 0, width, height);

        // Title
        g2.setColor(Color.WHITE);
        g2.setFont(new Font("Arial", Font.BOLD, 72));
        String title = "Chicken Run";
        FontMetrics fm = g2.getFontMetrics();
        int titleX = (width - fm.stringWidth(title)) / 2;
        int titleY = 180;
        g2.drawString(title, titleX, titleY);

        // Play button hover effect
        boolean hover = mousePosition != null && playButton.contains(mousePosition);
        g2.setColor(hover ? new Color(255, 220, 180) : new Color(255, 200, 120));
        g2.fill(new RoundRectangle2D.Double(playButton.x, playButton.y, playButton.width, playButton.height, 30, 30));

        // Button border
        g2.setColor(Color.WHITE);
        g2.setStroke(new BasicStroke(3));
        g2.draw(new RoundRectangle2D.Double(playButton.x, playButton.y, playButton.width, playButton.height, 30, 30));

        // Button text
        g2.setFont(new Font("Arial", Font.BOLD, 36));
        String buttonText = "Play";
        fm = g2.getFontMetrics();
        int textX = playButton.x + (playButton.width - fm.stringWidth(buttonText)) / 2;
        int textY = playButton.y + ((playButton.height - fm.getHeight()) / 2) + fm.getAscent();
        g2.drawString(buttonText, textX, textY);
    }

    @Override
    public void keyPressed(KeyEvent e) {}

    @Override
    public void keyReleased(KeyEvent e) {}

    @Override
    public void mouseMoved(MouseEvent e) {
        mousePosition = e.getPoint();
    }

    @Override
    public void mousePressed(MouseEvent e) {
        if (playButton.contains(e.getPoint())) {
            gameWorld.getStateStack().push(new WeaponSelectState(gameWorld));
        }
    }
}
