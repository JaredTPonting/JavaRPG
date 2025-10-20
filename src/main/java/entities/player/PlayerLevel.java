package entities.player;

public class PlayerLevel {
    private int playerLevel;
    private double experiencePoints;
    private int upgradePoints;

    public PlayerLevel() {
        this.playerLevel = 1;
        this.experiencePoints = 0;
        this.upgradePoints = 0;
    }

    public void addExperiencePoints(double amount) {
        this.experiencePoints += amount;
    }

    public double getExperiencePoints() {
        return experiencePoints;
    }

    public int getPlayerLevel() {
        return playerLevel;
    }
    public double getNextLevelCost() {
        return 15 * Math.pow(playerLevel, 1.5);
    }

    public boolean canLevelUp() {
        return experiencePoints >= getNextLevelCost();
    }

    public void addUpgradePoint() {
        this.upgradePoints += 1;
    }
    public int getUpgradePoints() {
        return upgradePoints;
    }
    public void spendUpgradePoints() {
        this.upgradePoints--;
    }

    public boolean levelUp() {
        experiencePoints -= getNextLevelCost();
        playerLevel++;
        return true;
    }
}
