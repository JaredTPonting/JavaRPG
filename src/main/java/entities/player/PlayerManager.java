package entities.player;

import loot.ItemManager;

public class PlayerManager {
    private PlayerStats playerStats;
    private PlayerLevel playerLevel;
    private ItemManager playerItems;

    public PlayerManager() {
        this.playerItems = new ItemManager();
        this.playerStats = new PlayerStats(playerItems);
        this.playerLevel = new PlayerLevel();
    }

    // getters
    public PlayerStats getPlayerStats() {
        return this.playerStats;
    }
    public PlayerLevel getPlayerLevel() {
        return this.playerLevel;
    }
    public ItemManager getPlayerItems() {
        return this.playerItems;
    }

    // Stat methods

    // print stats
    public void printStats() {
        System.out.println("Level: " + this.playerLevel.getPlayerLevel() + ", XP: " + (int) this.playerLevel.getExperiencePoints() + "/" + (int) this.playerLevel.getNextLevelCost());
        System.out.println("Speed: " + this.playerStats.getSpeed());
        System.out.println("Max Health: " + this.playerStats.getMaxHealth() + ", Regen: " + this.playerStats.getHealthRegen());
        System.out.println("Unspent Points: " + this.playerLevel.getUpgradePoints());
    }


    // Level Up Methods
    public void gainXP(int amount) {
        this.playerLevel.addExperiencePoints(amount);
        checkLevelUp();
    }
    public void levelUp() {
        this.playerLevel.levelUp();
    }

    public boolean checkLevelUp() {
        if (this.playerLevel.canLevelUp()) {
            return true;
        } else {
            return false;
        }
    }

    public void levelUpHealth() {
        if (checkLevelUp()) {
            this.playerStats.increaseMaxHealth();
            this.levelUp();
        }
    }


    public void levelUpHealthRegen() {
        if (checkLevelUp()){
            this.playerStats.increaseHealthRegen();
            this.levelUp();
        }
    }

    public void levelUpSpeed() {
        if (checkLevelUp()) {
            this.playerStats.increaseSpeed();
            this.levelUp();
        }
    }

    public void levelUpEndurance() {
        if (checkLevelUp()) {
            this.playerStats.increaseEndurance();
            this.levelUp();
        }
    }

    public void levelUpStaminaRegen() {
        if (checkLevelUp()) {
            this.playerStats.increaseStaminaRegen();
            this.levelUp();
        }
    }

    public void levelUpDamage() {
        if (checkLevelUp()) {
            this.playerStats.increaseDamage();
            this.levelUp();
        }
    }

    public void levelUpMagicDamage() {
        if (checkLevelUp()) {
            this.playerStats.increaseMagicDamage();
            this.levelUp();
        }
    }

    // Get Methods
    public int getLevel() {
        return this.playerLevel.getPlayerLevel();
    }
    public int getXP() {
        return (int) this.playerLevel.getExperiencePoints();
    }
    public int getMaxXP() {
        return (int) this.playerLevel.getNextLevelCost();
    }

    public double getMaxHealth() {
        return this.playerStats.getMaxHealth() + playerItems.getItemHP();
    }
    public double getHealthRegen() {
        return this.playerStats.getHealthRegen() + playerStats.getHealthRegen();
    }
    public double getCurrentHealth() {
        return this.playerStats.getCurrentHealth();
    }

    public double getSpeed() {
        return this.playerStats.getSpeed() + playerItems.getItemSpeed();
    }


    public double getMaxStamina() {
        return this.playerStats.getMaxStamina() + playerItems.getItemStamina();
    }
    public double getStaminaRegen() {
        return this.playerStats.getStaminaRegen() + playerItems.getItemStaminaRegen();
    }
    public double getCurrentStamina() {
        return this.playerStats.getCurrentStamina();
    }

    public double getDamage(){
        return this.playerStats.getDamage() + playerItems.getItemDamage();
    }
    public double getMagicDamage() {
        return this.playerStats.getMagicDamage() + playerItems.getItemMagicDamage();
    }

    // Damage
    public void takeDamage(double damage) {
        this.playerStats.takeDamage(damage);
    }


}
