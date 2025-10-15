package enviroment;

import utils.Camera;
import utils.SpriteLoader;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.util.Random;

public class WfcChunk extends Chunk {
    private final BufferedImage chunkImage;

    private static BufferedImage[] grassTiles; // shared across all chunks

    public WfcChunk(int chunkX, int chunkY, int size, int chunkCount) {
        super(chunkX, chunkY, size);
        Random random = new Random();
        int chunkImageNumber = random.nextInt(chunkCount);
        String resourcePath = "/tiles/finalTiles/final" + chunkImageNumber + ".png";


        BufferedImage loadedImage = null;
        try (InputStream is = getClass().getResourceAsStream(resourcePath)) {
            if (is == null) {
                throw new IOException("Resource not found: " + resourcePath);
            }
            loadedImage = ImageIO.read(is);
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (loadedImage == null) {
            loadedImage = new BufferedImage(size, size, BufferedImage.TYPE_INT_ARGB);
        }

        chunkImage = new BufferedImage(size, size, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = chunkImage.createGraphics();
        g2d.drawImage(loadedImage, 0, 0, size, size, null);
        g2d.dispose();
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
