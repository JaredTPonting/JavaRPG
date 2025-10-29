package states;

import core.GameWorld;
import loot.Item;
import loot.ItemManager;
import loot.items.LeatherBoots;
import loot.items.WoodenSword;
import loot.items.WoodenWand;
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

public class BasicLootSelectionState implements GameState {

    private final GameWorld gameWorld;
    private final ItemManager itemManager;
    private final List<Button> buttons = new ArrayList<>();

    public BasicLootSelectionState(GameWorld gameWorld) {
        this.gameWorld = gameWorld;
        this.itemManager = gameWorld.getPlayer().getPlayerManager().getPlayerItems();
        initButtons();
    }

    private static class WeaponModChoice {
        Weapon weapon;
        Class<? extends WeaponMods> modClass;

        WeaponModChoice(Weapon weapon, Class<? extends WeaponMods> modClass) {
            this.weapon = weapon;
            this.modClass = modClass;
        }
    }

    private void initButtons() {
        WeaponManager weaponManager = gameWorld.getWeaponManager();
        List<Weapon> ownedWeapons = weaponManager.getOwnedWeapons();
        List<WeaponModChoice> allChoices = new ArrayList<>();

        // Get ellegible mods from owned weapons
        for (Weapon w : ownedWeapons) {
            for (Class<? extends  WeaponMods> modClass : w.getAvailableModClasses()) {
                boolean alreadyApplied = w.weaponMods.stream().anyMatch(m -> m.getClass().equals(modClass));
                if (!alreadyApplied) {
                    allChoices.add(new WeaponModChoice(w, modClass));
                }
            }
        }

        Collections.shuffle(allChoices);
        List<WeaponModChoice> lootChoices = allChoices.subList(0, Math.min(3, allChoices.size()));


        int startY = 150;
        int spacing = 60;

        for (int i = 0; i < lootChoices.size(); i++) {
            WeaponModChoice choice = lootChoices.get(i);
            String modName = choice.modClass.getSimpleName().replaceAll("([A-Z])", " $1").trim();
            String weaponName = choice.weapon.getName();
            int y = startY + i*spacing;

            buttons.add(
                    new Button(200, y, 400,40, weaponName + ": " + modName, () -> selectMod(choice)
                    )
            );
        }
    }

    private void selectMod(WeaponModChoice choice) {
        try {
            WeaponMods modInstance = choice.modClass
                    .getDeclaredConstructor()
                    .newInstance();
            choice.weapon.addWeaponMod(modInstance);
            System.out.println("Applied " + modInstance.getName());
        } catch (Exception e) {
            e.printStackTrace();
        }
        gameWorld.getStateStack().pop();
    }

    @Override
    public void update() {}

    @Override
    public void render(Graphics g) {
        g.setColor(Color.BLACK);
        g.fillRect(0, 0, gameWorld.getGameWidth(), gameWorld.getGameHeight());

        g.setColor(Color.WHITE);
        g.setFont(new Font("Arial", Font.BOLD, 24));
        g.drawString("You Found a Chest! Pick a Weapon Mod", 100, 100);

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
