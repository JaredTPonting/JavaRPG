package entities.player;

import loot.ItemManager;

public class PlayerStats {

    // Set to true for testing - gives high HP/stamina so you don't die while testing
    public static final boolean DEV_MODE = true;

    // Stat caps (asymptotic - you approach but never reach these)
    private static final double SPEED_CAP = 600.0;
    private static final double MAX_HEALTH_CAP = 1000.0;
    private static final double HEALTH_REGEN_CAP = 20.0;
    private static final double MAX_STAMINA_CAP = 500.0;
    private static final double STAMINA_REGEN_CAP = 30.0;
    private static final double DAMAGE_CAP = 500.0;
    private static final double MAGIC_DAMAGE_CAP = 500.0;

    // Growth rate - fraction of remaining distance gained per level (0.1 = 10%)
    private static final double GROWTH_RATE = 0.1;

    private boolean dead = false;

    private double speed;
    private int speedLevel = 1;

    private double maxHealth;
    private int healthLevel = 1;
    private double currentHealth;
    private double healthRegen;
    private int healthRegenLevel = 1;

    private double maxStamina;
    private double enduranceLevel = 1;
    private double currentStamina;
    private double staminaRegen;
    private int staminaRegenLevel = 1;

    private double damage;
    private int damageLevel = 1;
    private double magicDamage;
    private int magicDamageLevel = 1;

    private ItemManager items;

    private long lastRegenTime;

    public PlayerStats(ItemManager items) {
        this.lastRegenTime = System.currentTimeMillis();
        this.items = items;

        // Base starting stats
        this.speed = 200.0;
        this.damage = 50;
        this.magicDamage = 50;
        this.healthRegen = 0.5;
        this.staminaRegen = 5.0;

        if (DEV_MODE) {
            // High stats for testing
            this.maxHealth = 100000.0;
            this.maxStamina = 10000.0;
        } else {
            // Normal starting stats for actual gameplay
            this.maxHealth = 100.0;
            this.maxStamina = 100.0;
        }

        this.currentHealth = maxHealth;
        this.currentStamina = maxStamina;
    }

    /**
     * Calculates asymptotic gain: approaches cap but never reaches it.
     * Each level gives a fraction of the remaining distance to the cap.
     */
    private double calculateAsymptoticGain(double current, double cap) {
        return (cap - current) * GROWTH_RATE;
    }

    public boolean isDead() {
        return dead;
    }

    public double getSpeed() {
        return speed;
    }
    public void increaseSpeed() {
        this.speedLevel += 1;
        this.speed += calculateAsymptoticGain(speed, SPEED_CAP);
    }

    public void increaseDamage() {
        this.damageLevel += 1;
        this.damage += calculateAsymptoticGain(damage, DAMAGE_CAP);
    }
    public void increaseMagicDamage() {
        this.magicDamageLevel += 1;
        this.magicDamage += calculateAsymptoticGain(magicDamage, MAGIC_DAMAGE_CAP);
    }

    public double getMaxHealth() {
        return maxHealth;
    }
    public void increaseMaxHealth() {
        this.healthLevel += 1;
        double gain = calculateAsymptoticGain(maxHealth, MAX_HEALTH_CAP);
        this.maxHealth += gain;
        this.currentHealth += gain; // Also increase current health so you don't lose HP ratio
    }
    public double getCurrentHealth() {
        return currentHealth;
    }
    public void takeDamage(double damage) {
        this.currentHealth = Math.max(0, this.currentHealth - damage);
        if (currentHealth <= 0) {
            dead = true;
        }
    }
    public double getHealthRegen() {
        return healthRegen;
    }
    public void increaseHealthRegen() {
        this.healthRegenLevel += 1;
        this.healthRegen += calculateAsymptoticGain(healthRegen, HEALTH_REGEN_CAP);
    }

    public double getMaxStamina() {
        return maxStamina;
    }
    public void exhaustStamina(double staminaUsed) { this.currentStamina -= staminaUsed; }
    public void increaseEndurance() {
        this.enduranceLevel += 1;
        double gain = calculateAsymptoticGain(maxStamina, MAX_STAMINA_CAP);
        this.maxStamina += gain;
        this.currentStamina += gain; // Also increase current stamina
    }

    public double getCurrentStamina() {
        return this.currentStamina;
    }
    public double getStaminaRegen() {
        return staminaRegen;
    }
    public void increaseStaminaRegen() {
        this.staminaRegenLevel += 1;
        this.staminaRegen += calculateAsymptoticGain(staminaRegen, STAMINA_REGEN_CAP);
    }

    public double getDamage() {
        return damage;
    }
    public double getMagicDamage() {
        return magicDamage;
    }

    private void regen() {
        long now = System.currentTimeMillis();
        if (now - lastRegenTime > 1000) {
            lastRegenTime = now;
            if (this.currentHealth < this.maxHealth) {
                currentHealth = Math.min(maxHealth, currentHealth + healthRegen + items.getItemHealthRegen());
            }

            if  (this.currentStamina < this.maxStamina) {
                currentStamina = Math.min(maxStamina, currentStamina + staminaRegen + items.getItemStaminaRegen());
            }
        }
    }



    public void update() {
        this.regen();
    }

}
