package enviroment;

import java.awt.*;
import utils.Camera;

public abstract class Chunk {
    protected final int chunkX, chunkY;   // chunk grid coordinates
    protected final int size;             // pixel size of a chunk
    protected final int tilesX, tilesY;   // how many tiles across
    protected final int[][] tiles;        // tile type IDs

    public Chunk(int chunkX, int chunkY, int size, int tilesX, int tilesY) {
        this.chunkX = chunkX;
        this.chunkY = chunkY;
        this.size = size;
        this.tilesX = tilesX;
        this.tilesY = tilesY;
        this.tiles = new int[tilesX][tilesY];
        generate(); // let subclass fill tiles
    }

    // Each subclass must define its own generation rules
    protected abstract void generate();

    // Subclasses may override rendering if needed
    public void render(Graphics g, Camera camera) {
        int worldX = chunkX * size;
        int worldY = chunkY * size;

        int tileSize = size / tilesX;

        for (int x = 0; x < tilesX; x++) {
            for (int y = 0; y < tilesY; y++) {
                renderTile(g, tiles[x][y],
                        worldX + x * tileSize - camera.getX(),
                        worldY + y * tileSize - camera.getY(),
                        tileSize);
            }
        }
    }

    // Default tile rendering (subclasses can override if needed)
    protected void renderTile(Graphics g, int tileId, int drawX, int drawY, int tileSize) {
        switch (tileId) {
            case 0 -> g.setColor(Color.GREEN);
            case 1 -> g.setColor(Color.YELLOW);
            case 2 -> g.setColor(Color.GRAY);
            default -> g.setColor(Color.BLACK);
        }
        g.fillRect(drawX, drawY, tileSize, tileSize);
    }
}

