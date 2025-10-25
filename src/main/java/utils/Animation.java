package utils;

import java.awt.image.BufferedImage;
import java.util.Map;

public class Animation {
    private BufferedImage[] frames;
    private int currentFrame = 0;
    private long frameTime; // ms per frame
    private long lastTime;
    private boolean loop;
    private boolean finished = false;

    public Animation(BufferedImage spriteSheet, int frameCount, long frameTime, boolean loop) {
        this.frameTime = frameTime;
        this.lastTime = System.currentTimeMillis();
        this.loop = loop;

        int frameWidth = spriteSheet.getWidth() / frameCount;
        int height = spriteSheet.getHeight();

        frames = new BufferedImage[frameCount];
        for (int i = 0; i < frameCount; i++) {
            frames[i] = spriteSheet.getSubimage(i * frameWidth, 0, frameWidth, height);
        }
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
}
