//package enviroment;
//
//import utils.Camera;
//import utils.SpriteLoader;
//
//import java.awt.*;
//import java.awt.image.BufferedImage;
//import java.util.Random;
//
//public class GrassChunk extends Chunk {
//    private static final int TILE_SIZE = 16;      // size inside sprite sheet
//    private static final int SCALED_TILE_SIZE = 64; // how big it draws in game
//    private static final int[][] GRASS_COORDS = {
//            {1, 1}, {0, 5}, {0, 6}, {1, 5}, {1, 6},
//            {2, 5}, {2, 6}, {3, 5}, {3, 6}, {4, 5},
//            {4, 6}, {5, 5}, {5, 6}
//    };
//    private final BufferedImage chunkImage;
//
//    private static BufferedImage[] grassTiles; // shared across all chunks
//
//    public GrassChunk(int chunkX, int chunkY, int size, int tilesX, int tilesY, Random random) {
//        super(chunkX, chunkY, size, tilesX, tilesY);
//
//        if (grassTiles == null) {
//            loadSprites();
//        }
//        chunkImage = new BufferedImage(size, size, BufferedImage.TYPE_INT_ARGB);
//        Graphics2D g2d = chunkImage.createGraphics();
//
//        for (int x = 0; x < tilesX; x++) {
//            for (int y = 0; y < tilesY; y++) {
//                BufferedImage tile = grassTiles[random.nextInt(grassTiles.length)];
//                g2d.drawImage(tile, x * SCALED_TILE_SIZE, y * SCALED_TILE_SIZE,
//                        SCALED_TILE_SIZE, SCALED_TILE_SIZE, null);
//            }
//        }
//
//        g2d.dispose();
//    }
//
//    private void loadSprites() {
//        BufferedImage sheet = SpriteLoader.load("/sprites/Grass.png");
//        assert sheet != null;
//
//        grassTiles = new BufferedImage[GRASS_COORDS.length];
//        for (int i = 0; i < GRASS_COORDS.length; i++) {
//            int col = GRASS_COORDS[i][0];
//            int row = GRASS_COORDS[i][1];
//            grassTiles[i] = sheet.getSubimage(col * TILE_SIZE, row * TILE_SIZE, TILE_SIZE, TILE_SIZE);
//        }
//    }
//
//    @Override
//    protected void generate() {
//        // unused: idea was to generate terrain as we go, but was too slow
//    }
//
//    @Override
//    public void render(Graphics g, Camera camera) {
//        int worldX = chunkX * size - camera.getX();
//        int worldY = chunkY * size - camera.getY();
//        g.drawImage(chunkImage, worldX, worldY, null);
//    }
//}
