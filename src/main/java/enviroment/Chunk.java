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
}

