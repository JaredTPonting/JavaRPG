package ammo;

import utils.SpriteLoader;
import java.awt.image.BufferedImage;
import java.util.Random;

public class EggProjectile extends Projectile {
    private static final BufferedImage[] eggSprites = new BufferedImage[30];
    private static boolean loaded = false;
    private static final Random rand = new Random();

    public EggProjectile(double x, double y, double targetX, double targetY, int screenWidth, int screenHeight, double speedMultiplier) {
        super(x, y, targetX, targetY, screenWidth, screenHeight, 5.0 * speedMultiplier, 50.0);
    }

    @Override
    protected void loadSprite() {
        if (!loaded) {
            BufferedImage sheet = SpriteLoader.load("/sprites/eggBulletSpriteSheet.png");
            int i = 0;
            for (int t = 0; t < 5; t++) {
                for (int k = 0; k < 6; k++) {
                    assert sheet != null;
                    eggSprites[i++] = sheet.getSubimage(t * spriteSize, k * spriteSize, spriteSize, spriteSize);
                }
            }
            loaded = true;
        }

        this.sprite = eggSprites[rand.nextInt(30)];
    }
}
