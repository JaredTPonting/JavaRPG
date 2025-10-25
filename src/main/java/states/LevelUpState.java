package states;

import entities.player.Player;
import core.GameWorld;
import entities.player.PlayerManager;
import ui.Button;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;

public class LevelUpState implements GameState {
    private final PlayerManager playerManager;
    private final Player player;
    private final GameWorld gameWorld;

    private final List<Button> buttons = new ArrayList<>();

    public LevelUpState(GameWorld gameWorld) {
        this.gameWorld = gameWorld;
        this.player = gameWorld.getPlayer();
        this.playerManager = gameWorld.getPlayer().getPlayerManager();

        int startX = 120;
        int startY = 150;
        int gap = 40;
        int btnWidth = 150;
        int btnHeight = 30;

        buttons.add(new Button(startX, startY, btnWidth, btnHeight, "+ Max Health", playerManager::levelUpHealth));
        buttons.add(new Button(startX, startY + gap, btnWidth, btnHeight, "+ Health Regen", playerManager::levelUpHealthRegen));
        buttons.add(new Button(startX, startY + gap * 2, btnWidth, btnHeight, "+ Move Speed", playerManager::levelUpSpeed));
        buttons.add(new Button(startX, startY + gap * 3, btnWidth, btnHeight, "+ Damage", playerManager::levelUpDamage));
        buttons.add(new Button(startX, startY + gap * 4, btnWidth, btnHeight, "+ Magic Damage", playerManager::levelUpMagicDamage));
        buttons.add(new Button(startX, startY + gap * 5, btnWidth, btnHeight, "+ Endurance", playerManager::levelUpEndurance));
        buttons.add(new Button(startX, startY + gap * 6, btnWidth, btnHeight, "+ Stamina Regen", playerManager::levelUpStaminaRegen));
    }

    @Override
    public void update() {
        // no movement
    }

    @Override
    public void render(Graphics g) {
        g.setColor(new Color(0, 0, 0, 180));
        g.fillRect(0, 0, gameWorld.getGameWidth(), gameWorld.getGameHeight());

        g.setColor(Color.WHITE);
        g.setFont(new Font("Arial", Font.BOLD, 20));
        g.drawString("LEVEL UP! You have " + playerManager.getXP() + " / " + playerManager.getMaxXP(), 100, 100);

        g.setFont(new Font("Arial", Font.PLAIN, 18));
        for (Button btn : buttons) {
            btn.render(g);
        }

        g.setFont(new Font("Arial", Font.ITALIC, 14));
        g.drawString("Click + to level up, ESC to cancel", 120, 460);
    }

    @Override
    public void mousePressed(MouseEvent e) {
        int mx = e.getX();
        int my = e.getY();
        for (Button btn : buttons) {
            if (btn.contains(mx, my)) {
                btn.click();
//                if (!playerManager.checkLevelUp()) {
//                    player.resetInput();
//                    gameWorld.getStateStack().pop(); // resume gameplay
//                }
                break;
            }
        }
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        // Optional: Highlight buttons when hovered
    }

    @Override
    public void keyPressed(java.awt.event.KeyEvent e) {
        if (e.getKeyCode() == java.awt.event.KeyEvent.VK_ESCAPE) {
            gameWorld.getStateStack().pop();
        }
    }

    @Override
    public void keyReleased(java.awt.event.KeyEvent e) {
    }
}
