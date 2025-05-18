// Grass.java
package envviroment;

import utils.SpriteLoader;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Random;
import static java.lang.Math.ceil;

public class Grass {
    int[][] grassCoords = {
            {1, 1}, {0, 5}, {0, 6}, {1, 5}, {1, 6},
            {2, 5}, {2, 6}, {3, 5}, {3, 6}, {4, 5},
            {4, 6}, {5, 5}, {5, 6}
    };

    private final int SCALED_TILE_SIZE = 64;
    private final int MAP_WIDTH = 11;
    private final int MAP_HEIGHT = 5;
    private final BufferedImage[][] grassLayout;
    private final int TILE_SCREEN_WIDTH;
    private final int TILE_SCREEN_HEIGHT;

    public Grass(int screenWidth, int screenHeight) {
        TILE_SCREEN_WIDTH = (int) ceil((double) screenWidth / SCALED_TILE_SIZE);
        TILE_SCREEN_HEIGHT = (int) ceil((double) screenHeight / SCALED_TILE_SIZE);

        BufferedImage grassTileset = SpriteLoader.load("/sprites/Grass.png");
        int TILE_SIZE = 16;
        assert grassTileset != null;
        BufferedImage grassTile = grassTileset.getSubimage(TILE_SIZE, TILE_SIZE, TILE_SIZE, TILE_SIZE);
        BufferedImage[] grassTiles = new BufferedImage[grassCoords.length];

        for (int i = 0; i < grassCoords.length; i++) {
            int col = grassCoords[i][0];
            int row = grassCoords[i][1];
            int x = col * TILE_SIZE;
            int y = row * TILE_SIZE;
            grassTiles[i] = grassTileset.getSubimage(x, y, TILE_SIZE, TILE_SIZE);
        }

        grassLayout = new BufferedImage[TILE_SCREEN_WIDTH + 1][TILE_SCREEN_HEIGHT + 1];

        for (int x = 0; x < TILE_SCREEN_WIDTH + 1; x++) {
            for (int y = 0; y < TILE_SCREEN_HEIGHT + 1; y++) {
                Random random = new Random();
                grassLayout[x][y] = grassTiles[random.nextInt(grassTiles.length)];
            }
        }
    }

    public void render(Graphics g) {
        for (int x = 0; x < TILE_SCREEN_WIDTH + 1; x++) {
            for (int y = 0; y < TILE_SCREEN_HEIGHT + 1; y++) {
                int screenX = x * SCALED_TILE_SIZE;
                int screenY = y * SCALED_TILE_SIZE;
                g.drawImage(grassLayout[x][y], screenX, screenY, SCALED_TILE_SIZE, SCALED_TILE_SIZE, null);
            }
        }
    }
}
