package envviroment;

import utils.SpriteLoader;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Random;

import static java.lang.Math.ceil;

public class Grass {
    int[][] grassCoords = {
            {1, 1},
            {0, 5},
            {0, 6},
            {1, 5},
            {1, 6},
            {2, 5},
            {2, 6},
            {3, 5},
            {3, 6},
            {4, 5},
            {4, 6},
            {5, 5},
            {5, 6}
    };
    private int SCREEN_WIDTH;
    private int SCREEN_HEIGHT;

    private Random random = new Random();

    private final int TILE_SIZE = 16;
    private final int SCALED_TILE_SIZE = 64;
    private final int MAP_WIDTH = 11;
    private final int MAP_HEIGHT = 5;
    private BufferedImage grassTileset;
    private BufferedImage grassTile;
    private BufferedImage[] grassTiles;
    private BufferedImage[][] grassLayout;
    private int TILE_SCREEN_WIDTH;
    private int TILE_SCREEN_HEIGHT;

    public Grass(int screenWidth, int screenHeight) {
        SCREEN_HEIGHT = screenHeight;
        SCREEN_WIDTH = screenWidth;
        TILE_SCREEN_WIDTH = (int) ceil(SCREEN_WIDTH / SCALED_TILE_SIZE);
        TILE_SCREEN_HEIGHT = (int) ceil(SCREEN_HEIGHT / SCALED_TILE_SIZE);

        grassTileset = SpriteLoader.load("/sprites/Grass.png");
        grassTile = grassTileset.getSubimage(1 * TILE_SIZE, 1 * TILE_SIZE, TILE_SIZE, TILE_SIZE);
        grassTiles = new BufferedImage[grassCoords.length];

        for (int i = 0; i < grassCoords.length; i++) {
            int col = grassCoords[i][0];
            int row = grassCoords[i][1];

            int x  = col * TILE_SIZE;
            int y  = row * TILE_SIZE;

            grassTiles[i] = grassTileset.getSubimage(x, y, TILE_SIZE, TILE_SIZE);
        }

        grassLayout = new BufferedImage[TILE_SCREEN_WIDTH][TILE_SCREEN_HEIGHT];

        for (int x = 0; x < TILE_SCREEN_WIDTH; x++) {
            for (int y = 0; y < TILE_SCREEN_HEIGHT; y++) {
                grassLayout[x][y] = grassTiles[random.nextInt(grassTiles.length)];
            }
        }
    }

    public void render(Graphics g) {
        for (int x = 0; x < TILE_SCREEN_WIDTH; x++){
            for (int y = 0; y < TILE_SCREEN_HEIGHT; y++) {
                int screenX = x * SCALED_TILE_SIZE;
                int screenY = y * SCALED_TILE_SIZE;
                g.drawImage(grassLayout[x][y], screenX, screenY, SCALED_TILE_SIZE, SCALED_TILE_SIZE, null);
            }
        }
    }
}
