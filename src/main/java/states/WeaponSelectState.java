package states;

import core.GameWorld;
import weapons.Weapon;
import weapons.WeaponManager;
import weapons.chaosorbblaster.ChaosOrbBlaster;
import weapons.eggcannon.EggCannon;
import weapons.fireballblaster.FireBallBlaster;
import weapons.powerofzeus.PowerOfZeus;
import ui.Button;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;

public class WeaponSelectState implements GameState {

    private final GameWorld gameWorld;
    private final List<Button> buttons = new ArrayList<>();
    private final WeaponManager weaponManager;

    public WeaponSelectState(GameWorld gameWorld) {
        this.gameWorld = gameWorld;
        this.weaponManager = gameWorld.getWeaponManager();
        initButtons();
    }

    private void initButtons() {
        int startY = 150;
        int spacing = 60;

        buttons.add(new Button(200, startY, 200, 40, "Power of Zeus", () -> selectWeapon(new PowerOfZeus(gameWorld.getPlayer(), 2, gameWorld))));
        buttons.add(new Button(200, startY + spacing, 200, 40, "Fireball Blaster", () -> selectWeapon(new FireBallBlaster(gameWorld.getPlayer(), 1, gameWorld))));
        buttons.add(new Button(200, startY + spacing * 2, 200, 40, "Egg Cannon", () -> selectWeapon(new EggCannon(gameWorld.getPlayer(), 1, gameWorld))));
        buttons.add(new Button(200, startY + spacing * 3, 200, 40, "Chaos Orb Blaster", () -> selectWeapon(new ChaosOrbBlaster(gameWorld.getPlayer(), 2, gameWorld))));
    }

    private void selectWeapon(Weapon weapon) {
        weaponManager.addWeapon(weapon); // store choice in GameWorld
        gameWorld.getStateStack().push(new PlayingState(gameWorld));
    }

    @Override
    public void update() {}

    @Override
    public void render(Graphics g) {
        g.setColor(Color.BLACK);
        g.fillRect(0, 0, gameWorld.getGameWidth(), gameWorld.getGameHeight());

        g.setColor(Color.WHITE);
        g.setFont(new Font("Arial", Font.BOLD, 24));
        g.drawString("Select Your Weapon", 180, 100);

        for (Button b : buttons) {
            b.render(g);
        }
    }

    @Override
    public void keyPressed(java.awt.event.KeyEvent e) {}
    @Override
    public void keyReleased(java.awt.event.KeyEvent e) {}

    @Override
    public void mousePressed(MouseEvent e) {
        int mx = e.getX();
        int my = e.getY();

        for (Button b : buttons) {
            if (b.contains(mx, my)) {
                b.click();
                break;
            }
        }
    }

    @Override
    public void mouseMoved(MouseEvent e) {}
}
