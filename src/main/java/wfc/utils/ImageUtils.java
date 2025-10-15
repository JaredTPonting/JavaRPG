package wfc.utils;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class ImageUtils {
    private ImageUtils() {}

    public static BufferedImage overlayImages(BufferedImage base, BufferedImage overlay) {
        int width = Math.max(base.getWidth(), overlay.getWidth());
        int height = Math.max(base.getHeight(), overlay.getHeight());

        BufferedImage combined = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = combined.createGraphics();

        g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        g.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        g.drawImage(base, 0, 0, null);
        g.drawImage(overlay, 0, 0, null);

        g.dispose();
        return combined;
    }

    public static void saveImage(BufferedImage image, String path) throws IOException {
        File file = new File(path);
        file.getParentFile().mkdirs();
        ImageIO.write(image, "png", file);
        System.out.println(path);
    }
}
