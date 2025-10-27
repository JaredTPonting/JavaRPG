package loot;

import java.util.ArrayList;
import java.util.List;

public class ItemManager {
    List<Item> ownedItems;

    public ItemManager() {
        this.ownedItems = new ArrayList<Item>();
    }

    public void addItem(Item newItem) {
        ownedItems.add(newItem);
    }

    public double getItemHP() {
        double totalHP = 0;
        for (Item items : ownedItems) {
            totalHP += items.getHP();
        }
        return totalHP;
    }
    public double getItemHealthRegen() {
        double totalHelathRegen = 0;
        for (Item items : ownedItems) {
            totalHelathRegen += items.getHealthRegen();
        }
        return totalHelathRegen;
    }
    public double getItemSpeed() {
        double totalSpeed = 0;
        for (Item items : ownedItems) {
            totalSpeed += items.getSpeed();
        }
        return totalSpeed;
    }
    public double getItemStamina() {
        double totalStamina = 0;
        for (Item items : ownedItems) {
            totalStamina += items.getStamina();
        }
        return totalStamina;
    }
    public double getItemStaminaRegen() {
        double totalStaminaRegen = 0;
        for (Item items : ownedItems) {
            totalStaminaRegen += items.getStaminaRegen();
        }
        return totalStaminaRegen;
    }
    public double getItemDamage() {
        double totalDamage = 0;
        for (Item items : ownedItems) {
            totalDamage += items.getDamage();
        }
        return totalDamage;
    }
    public double getItemMagicDamage() {
        double totalMagicDamage = 0;
        for (Item items : ownedItems) {
            totalMagicDamage += items.getMagicDamage();
        }
        return totalMagicDamage;
    }
}
