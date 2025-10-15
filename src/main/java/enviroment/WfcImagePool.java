package enviroment;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.nio.Buffer;
import java.util.Random;

public class WfcImagePool {
    private static BufferedImage[]  chunkImages;
    private static final Random random = new Random();

    public static void init(int chunkCount, int chunkSize) {
        chunkImages = new BufferedImage[chunkCount];
        for (int i = 0; i < chunkCount; i++) {
            String resourcePath = "/tiles/finalTiles/final" + i + ".png";
            try (InputStream is = WfcImagePool.class.getResourceAsStream(resourcePath)) {
                if (is == null) {
                    throw new IOException("Resource not found: " + resourcePath);
                }
                BufferedImage original = ImageIO.read(is);
                BufferedImage scaled = new BufferedImage(chunkSize, chunkSize, BufferedImage.TYPE_INT_ARGB);
                Graphics2D g2d = scaled.createGraphics();
                g2d.drawImage(original, 0, 0, chunkSize, chunkSize, null);
                g2d.dispose();
                chunkImages[i] = scaled;
            } catch (IOException e) {
                e.printStackTrace();
                chunkImages[i] = new BufferedImage(chunkSize, chunkSize, BufferedImage.TYPE_INT_ARGB);
            }
        }
        System.out.println("Loaded images into memory");
    }

    public static BufferedImage getRandom() {
        if (chunkImages == null || chunkImages.length == 0) {
            throw new IllegalStateException("WfcImagePool not initialized!!!");
        }
        return chunkImages[random.nextInt(chunkImages.length)];
    }
}
