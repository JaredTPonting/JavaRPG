package loot;

import core.GameWorld;
import states.BasicLootSelectionState;
import states.LootSelectionState;
import utils.WorldContext;

public class BasicChest extends Chest{
    public BasicChest(double x, double y, WorldContext gameWorld) {
        super(x, y, gameWorld);
        this.closedChest = getSubImage(0, 0);
        this.openChest = getSubImage(0, 3);
    }

    @Override
    public void onUpdate() {
        if (!opened && gameWorld.getCollisionChecker().circleRectCollision(gameWorld.getPlayer(), this.hitBox)){
            openChest();
            gameWorld.getPlayer().resetInput();
            gameWorld.getStateStack().push(new BasicLootSelectionState((GameWorld) gameWorld));
        }
    }
}
