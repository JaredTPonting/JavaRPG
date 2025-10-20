package states;

import entities.player.Player;
import utils.GameWorld;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;

public class PauseState implements GameState {
    private final Player player;
    private final GameWorld gameWorld;

    private boolean showStats = false;

    // Button positions and sizes
    private final Rectangle resumeButton = new Rectangle(500, 300, 200, 50);
    private final Rectangle statsButton = new Rectangle(500, 400, 200, 50);
    private final Rectangle levelUpButton = new Rectangle(500, 500, 200, 50);
    private final Rectangle quitButton = new Rectangle(500, 600, 200, 50);


    public PauseState(GameWorld gameWorld) {
        this.gameWorld = gameWorld;
        this.player = gameWorld.getPlayer();
    }

    @Override
    public void update() {
        // No game updates while paused
    }

    @Override
    public void render(Graphics g) {
        // Dark overlay
        g.setColor(new Color(0, 0, 0, 180));
        g.fillRect(0, 0, gameWorld.getGameWidth(), gameWorld.getGameHeight());

        // Draw buttons
        g.setColor(Color.WHITE);
        g.fillRect(resumeButton.x, resumeButton.y, resumeButton.width, resumeButton.height);
        g.fillRect(statsButton.x, statsButton.y, statsButton.width, statsButton.height);
        g.fillRect(levelUpButton.x, levelUpButton.y, levelUpButton.width, levelUpButton.height);
        g.fillRect(quitButton.x, quitButton.y, quitButton.width, quitButton.height);

        g.setColor(Color.BLACK);
        g.setFont(new Font("Arial", Font.BOLD, 20));
        g.drawString("RESUME", resumeButton.x + 50, resumeButton.y + 30);
        g.drawString("STATS", statsButton.x + 60, statsButton.y + 30);
        g.drawString("LEVEL UP", levelUpButton.x + 60, levelUpButton.y + 30);
        g.drawString("QUIT", quitButton.x + 70, quitButton.y + 30);

        // Show stats overlay
        if (showStats) {
            g.setColor(new Color(0, 0, 0, 200));
            g.fillRect(200, 150, 800, 600);

            g.setColor(Color.WHITE);
            g.setFont(new Font("Arial", Font.PLAIN, 18));
            g.drawString("Player Stats:", 350, 200);
            g.drawString("Level: " + player.getLevel(), 350, 230);
            g.drawString("Max Health: " + player.getMaxHealth(), 350, 260);
            g.drawString("Speed: " + player.getSpeed(), 350, 290);
            g.drawString("Health Regen: " + player.getHealthRegen(), 350, 320);
            g.drawString("XP: " + player.getXP() + "/" + player.getMaxXP(), 350, 350);
        }
    }

    @Override
    public void keyPressed(KeyEvent e) {
        // Press ESC again to resume
        if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
            resumeGame();
        }
    }

    @Override
    public void keyReleased(KeyEvent e) { }

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
        } else if (quitButton.contains(p)) {
            System.exit(0); // quit game immediately
        } else if (levelUpButton.contains(p)) {
            levelUpPlayer();
        }
    }

    private void resumeGame() {
        // Reset input so entities.player doesn't keep moving
        player.resetInput();
        gameWorld.getStateStack().pop();
    }

    private void levelUpPlayer() {
        if (player.checkLevelUp()) {
            gameWorld.getStateStack().push(new LevelUpState(gameWorld));
        }
    }

    @Override
    public void mouseMoved(MouseEvent e) { }
}
