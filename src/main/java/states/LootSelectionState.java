package states;

import core.GameWorld;
import weapons.*;
import weapons.chaosorbblaster.ChaosOrbBlaster;
import weapons.eggcannon.EggCannon;
import weapons.fireballblaster.FireBallBlaster;
import weapons.powerofzeus.PowerOfZeus;
import ui.Button;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class LootSelectionState implements GameState {

    private final GameWorld gameWorld;
    private final WeaponManager weaponManager;
    private final List<Button> buttons = new ArrayList<>();

    public LootSelectionState(GameWorld gameWorld) {
        this.gameWorld = gameWorld;
        this.weaponManager = gameWorld.getWeaponManager();
        initButtons();
    }

    private void initButtons() {
        List<Weapon> allWeapons = new ArrayList<>();
        allWeapons.add(new PowerOfZeus(gameWorld.getPlayer(), 2, gameWorld));
        allWeapons.add(new FireBallBlaster(gameWorld.getPlayer(), 1, gameWorld));
        allWeapons.add(new EggCannon(gameWorld.getPlayer(), 1, gameWorld));
        allWeapons.add(new ChaosOrbBlaster(gameWorld.getPlayer(), 2, gameWorld));

        // Shuffle and pick 3 random weapons
        Collections.shuffle(allWeapons);
        List<Weapon> lootChoices = allWeapons.subList(0, 3);

        int startY = 150;
        int spacing = 60;

        for (int i = 0; i < lootChoices.size(); i++) {
            Weapon w = lootChoices.get(i);
            String name = w.getClass().getSimpleName().replaceAll("([A-Z])", " $1").trim(); // pretty name
            int y = startY + i * spacing;

            buttons.add(new Button(200, y, 200, 40, name, () -> selectWeapon(w)));
        }
    }

    private void selectWeapon(Weapon weapon) {
        weaponManager.addWeapon(weapon);
        gameWorld.getStateStack().pop(); // remove LootSelectionState
    }

    @Override
    public void update() {}

    @Override
    public void render(Graphics g) {
        g.setColor(Color.BLACK);
        g.fillRect(0, 0, gameWorld.getGameWidth(), gameWorld.getGameHeight());

        g.setColor(Color.WHITE);
        g.setFont(new Font("Arial", Font.BOLD, 24));
        g.drawString("You Found a Chest! Pick a Weapon", 100, 100);

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
