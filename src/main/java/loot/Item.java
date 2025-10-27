package loot;

public abstract class Item {
    protected double speed;
    protected double hp;
    protected double healthRegen;
    protected double stamina;
    protected double staminaRegen;
    protected double damage;
    protected double magicDamage;

    public Item() {
        this.speed = 0;
        this.hp = 0;
        this.healthRegen = 0;
        this.stamina = 0;
        this.staminaRegen = 0;
        this.damage = 0;
        this.magicDamage = 0;
    }

    public double getSpeed() { return this.speed; }
    public double getHP() { return this.hp; }
    public double getHealthRegen() { return this.healthRegen; }
    public double getStamina() { return this.stamina; }
    public double getStaminaRegen() { return this.staminaRegen; }
    public double getDamage() { return this.damage; }
    public double getMagicDamage() { return this.magicDamage; }

}
