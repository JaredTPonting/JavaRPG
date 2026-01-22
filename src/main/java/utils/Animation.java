package utils;

import java.awt.*;
import java.awt.image.BufferedImage;

public class Animation {
    private BufferedImage[] frames;
    private BufferedImage[] framesFlipped; // Pre-flipped for horizontal mirror
    private int currentFrame = 0;
    private long frameTime; // ms per frame
    private long lastTime;
    private boolean loop;
    private boolean finished = false;

    /** Original constructor - no pre-scaling (for projectiles, etc.) */
    public Animation(BufferedImage spriteSheet, int frameCount, long frameTime, boolean loop) {
        this(spriteSheet, frameCount, frameTime, loop, -1);
    }

    /** Constructor with target size - pre-scales and pre-flips all frames */
    public Animation(BufferedImage spriteSheet, int frameCount, long frameTime, boolean loop, int targetSize) {
        this.frameTime = frameTime;
        this.lastTime = System.currentTimeMillis();
        this.loop = loop;

        int frameWidth = spriteSheet.getWidth() / frameCount;
        int height = spriteSheet.getHeight();

        frames = new BufferedImage[frameCount];
        framesFlipped = new BufferedImage[frameCount];

        for (int i = 0; i < frameCount; i++) {
            BufferedImage raw = spriteSheet.getSubimage(i * frameWidth, 0, frameWidth, height);

            if (targetSize > 0) {
                // Pre-scale to target size
                frames[i] = scaleImage(raw, targetSize, targetSize);
                framesFlipped[i] = scaleAndFlipImage(raw, targetSize, targetSize);
            } else {
                // No scaling, just store original and flipped
                frames[i] = raw;
                framesFlipped[i] = flipImage(raw);
            }
        }
    }

    /** Scale image to target dimensions */
    private BufferedImage scaleImage(BufferedImage src, int width, int height) {
        BufferedImage scaled = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = scaled.createGraphics();
        g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        g2d.drawImage(src, 0, 0, width, height, null);
        g2d.dispose();
        return scaled;
    }

    /** Scale and flip image horizontally */
    private BufferedImage scaleAndFlipImage(BufferedImage src, int width, int height) {
        BufferedImage result = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = result.createGraphics();
        g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        // Draw flipped: translate to right edge, then draw with negative width
        g2d.drawImage(src, width, 0, -width, height, null);
        g2d.dispose();
        return result;
    }

    /** Flip image horizontally without scaling */
    private BufferedImage flipImage(BufferedImage src) {
        int w = src.getWidth();
        int h = src.getHeight();
        BufferedImage flipped = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = flipped.createGraphics();
        g2d.drawImage(src, w, 0, -w, h, null);
        g2d.dispose();
        return flipped;
    }

    public void update() {
        if (finished) return;

        long now = System.currentTimeMillis();
        if (now - lastTime >= frameTime) {
            currentFrame++;
            if (currentFrame >= frames.length) {
                if (loop) {
                    currentFrame = 0;
                } else {
                    currentFrame = frames.length - 1; // stay on last frame
                    finished = true;
                }
            }
            lastTime = now;
        }
    }

    public boolean isFinished() {
        return finished;
    }

    public void reset() {
        currentFrame = 0;
        finished = false;
        lastTime = System.currentTimeMillis();
    }


    public BufferedImage getCurrentFrame() {
        return frames[currentFrame];
    }

    /** Get current frame flipped horizontally (pre-computed) */
    public BufferedImage getCurrentFrameFlipped() {
        return framesFlipped[currentFrame];
    }
}
