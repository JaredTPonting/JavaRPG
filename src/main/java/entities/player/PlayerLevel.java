package entities.player;

public class PlayerLevel {
    private int playerLevel;
    private double experiencePoints;
    private int upgradePoints;
    private boolean levelUpNotified = false; // Tracks if we've shown the level up screen

    public PlayerLevel() {
        this.playerLevel = 1;
        this.experiencePoints = 0;
        this.upgradePoints = 0;
    }

    public void addExperiencePoints(double amount) {
        boolean couldLevelBefore = canLevelUp();
        this.experiencePoints += amount;
        // Reset notification flag when player crosses the threshold (couldn't level before, can now)
        if (!couldLevelBefore && canLevelUp()) {
            levelUpNotified = false;
        }
    }

    /**
     * Returns true if player can level up AND hasn't been notified yet.
     * Call markLevelUpNotified() after showing the level up screen.
     */
    public boolean shouldShowLevelUp() {
        return canLevelUp() && !levelUpNotified;
    }

    public void markLevelUpNotified() {
        levelUpNotified = true;
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
