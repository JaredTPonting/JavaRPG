package entities.player;

public class PlayerStats {


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


    private long lastRegenTime;

    public PlayerStats() {
        this.lastRegenTime = System.currentTimeMillis();
        this.speed = 200.0;
        this.maxHealth = 1000000.0;
        this.currentHealth = maxHealth;
        this.healthRegen = 0.5;
        this.maxStamina = 10000.0;
        this.currentStamina = maxStamina;
        this.staminaRegen = 5.0;
        this.damage = 50;
        this.magicDamage = 50;

    }

    public boolean isDead() {
        return dead;
    }

    public double  getSpeed() {
        return speed;
    }
    public void increaseSpeed() {
        this.speedLevel += 1;
        this.speed += 50;
    }

    public void increaseDamage() {
        this.damageLevel += 1;
        this.damage += 10;
    }
    public void increaseMagicDamage() {
        this.magicDamageLevel += 1;
        this.magicDamage += 10;
    }

    public double getMaxHealth() {
        return maxHealth;
    }
    public void increaseMaxHealth() {
        this.healthLevel += 1;
        this.maxHealth += 50;
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
        this.healthRegen = (0.5) + (9.5 * (this.healthRegen/(this.healthRegen + 6)));
        this.healthRegen = Math.round(this.healthRegen * 100.0) / 100.0;
    }

    public double getMaxStamina() {
        return maxStamina;
    }
    public void exhaustStamia(double staminaUsed) { this.currentStamina -= staminaUsed;}
    public void increaseEndurance() {
        this.enduranceLevel += 1;
        this.maxStamina += 50;
    }

    public double getCurrentStamina() {
        return this.currentStamina;
    }
    public double getStaminaRegen() {
        return staminaRegen;
    }
    public void increaseStaminaRegen() {
        this.staminaRegenLevel += 1;
        this.staminaRegen = (5) + (9.5 * (this.staminaRegen/(this.staminaRegen + 6)));
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
                currentHealth = Math.min(maxHealth, currentHealth + healthRegen);
            }

            if  (this.currentStamina < this.maxStamina) {
                currentStamina = Math.min(maxStamina, currentStamina + staminaRegen);
            }
        }
    }



    public void update() {
        this.regen();
    }

}
