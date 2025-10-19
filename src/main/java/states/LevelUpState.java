package states;

import ammo.AmmoHandler;
import player.Player;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import core.Game;
import utils.GameWorld;

public class LevelUpState implements GameState {
    private final Player player;
    private final AmmoHandler ammoHandler;
    private final GameWorld gameWorld;


    public LevelUpState(GameWorld gameWorld) {
        this.gameWorld = gameWorld;
        this.player = gameWorld.getPlayer();
        this.ammoHandler = gameWorld.getAmmoHandler();
    }

    @Override
    public void update() {
        // no movement :0
    }

    @Override
    public void render(Graphics g) {
        g.setColor(new Color(0, 0, 0, 180));
        g.fillRect(0, 0, gameWorld.getGameWidth(), gameWorld.getGameHeight());

        g.setColor(Color.WHITE);
        g.setFont(new Font("Arial", Font.BOLD, 20));
        g.drawString("LEVEL UP! You have " + player.getUnspentPoints() + " points.", 100, 100);

        g.setFont(new Font("Arial", Font.PLAIN, 18));
        g.drawString("1. Increase Max Health (+20)", 120, 150);
        g.drawString("2. Increase Health Regen (+0.1/sec)", 120, 180);
        g.drawString("3. Increase Move Speed (+0.5)", 120, 210);
        g.drawString("4. Increase Bullet Speed (+1)", 120, 240);
        g.drawString("5. Increase Damage (+1)", 120, 270);
    }

    @Override
    public void keyPressed(KeyEvent e) {
        switch (e.getKeyCode()) {
            case KeyEvent.VK_1 -> { player.increaseMaxHealth(); player.spendPoint(); }
            case KeyEvent.VK_2 -> { player.increaseHealthRegen(); player.spendPoint(); }
            case KeyEvent.VK_3 -> { player.increaseSpeed(); player.spendPoint(); }
            case KeyEvent.VK_4 -> { ammoHandler.increaseBulletSpeedMultiplier(); player.spendPoint(); }
            case KeyEvent.VK_5 -> { ammoHandler.increaseDamageMultiplier(); player.spendPoint(); }
        }


        if (player.getUnspentPoints() <= 0) {
            player.clearLevelUpFlag();
            player.printStats();
            player.resetInput();
            gameWorld.getStateStack().pop(); // resume gameplay
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {

    }

    @Override
    public void mousePressed(MouseEvent e) {

    }

    @Override
    public void mouseMoved(MouseEvent e) {

    }
}
