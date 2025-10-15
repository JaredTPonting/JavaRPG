package enviroment;

import java.awt.*;
import utils.Camera;

public abstract class Chunk {
    protected final int chunkX, chunkY;   // chunk grid coordinates
    protected final int size;             // pixel size of a chunk

    public Chunk(int chunkX, int chunkY, int size) {
        this.chunkX = chunkX;
        this.chunkY = chunkY;
        this.size = size;
        generate(); // let subclass fill tiles
    }

    // Each subclass must define its own generation rules
    protected abstract void generate();

    // Subclasses may override rendering if needed
    public void render(Graphics g, Camera camera) {
    }

    // Default tile rendering (subclasses can override if needed)
    protected void renderTile(Graphics g, int tileId, int drawX, int drawY, int tileSize) {
        switch (tileId) {
            case 0 -> g.setColor(Color.GREEN);
            case 1 -> g.setColor(Color.YELLOW);
            case 2 -> g.setColor(Color.GRAY);
            default -> g.setColor(Color.BLACK);
        }
        g.fillRect(drawX, drawY, tileSize, tileSize);
    }
}

