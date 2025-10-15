package enviroment;

import utils.Camera;
import utils.SpriteLoader;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;

public class WfcChunk extends Chunk {
    private final BufferedImage chunkImage;

    private static BufferedImage[] grassTiles; // shared across all chunks

    public WfcChunk(int chunkX, int chunkY, int size, int chunkCount) {
        super(chunkX, chunkY, size);
        this.chunkImage = WfcImagePool.getRandom();
    }

    @Override
    protected void generate() {
        // unused: idea was to generate terrain as we go, but was too slow
    }

    @Override
    public void render(Graphics g, Camera camera) {
        int worldX = chunkX * size - camera.getX();
        int worldY = chunkY * size - camera.getY();
        g.drawImage(chunkImage, worldX, worldY, null);
    }
}
