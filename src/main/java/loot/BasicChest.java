package loot;

public class BasicChest extends Chest{
    public BasicChest(double x, double y) {
        super(x, y);
        System.out.println("Sheet size: " + Chests.getWidth() + "x" + Chests.getHeight());
        this.closedChest = getSubImage(0, 0);
        this.openChest = getSubImage(0, 3);
    }
}
