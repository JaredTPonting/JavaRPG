package enemies;

import player.Player;
import utils.SpriteLoader;

public class Fox extends Enemy {
    public Fox(int x, int y, Player target) {
        super(x, y, target);
        this.hp = 100.0;
        this.speed = 2;
        this.sprite = SpriteLoader.load("/sprites/fox_front_idle-removebg.png");
        this.XP = 30 + (target.getLevel() * 2);
    }
}
