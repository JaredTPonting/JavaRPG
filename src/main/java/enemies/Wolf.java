package enemies;

import player.Player;
import utils.SpriteLoader;

public class Wolf extends Enemy {
    public Wolf(int x, int y, Player target) {
        super(x, y, target);
        this.hp = 4;
        this.speed = 4;
        this.sprite = SpriteLoader.load("/sprites/wolf_front_idle-removebg.png");
    }
}
