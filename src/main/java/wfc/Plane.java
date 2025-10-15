package wfc;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

/* Stores the current state of the finished piece. Starts empty, but gets filled as the WFC completes */

public class Plane {
    private final int size; // how many tiles across and up finished piece should be. (final is size x size)
    private final Tile[][] grid;

    public Plane(int size) {
        this.size = size;
        this.grid = new Tile[size][size];
    }

    public int getSize() {
        return size;
    }

    public Tile getTile(int x, int y) {
        return grid[y][x];
    }

    public void setTile(int x, int y, Tile tile) {
        grid[y][x] = tile;
    }

    public boolean isCollapsed() {
        // Checks if individual cell is collapsed (has an assigned tile)
        for (int y = 0; y < size; y++) {
            for (int x = 0; x < size; x++) {
                if (grid[y][x] == null) return false;
            }
        }
        return true;
    }

    // Returns an array of neighbors: UP, RIGHT, DOWN, LEFT (or null if on edge)
    public Tile[] getNeighbours(int x, int y) {
        // Gets neighbours from current cell, handles cells outside of range
        Tile[] neighbors = new Tile[4];
        neighbors[0] = (y > 0) ? grid[y - 1][x] : null; // UP
        neighbors[1] = (x < size - 1) ? grid[y][x + 1] : null; // RIGHT
        neighbors[2] = (y < size - 1) ? grid[y + 1][x] : null; // DOWN
        neighbors[3] = (x > 0) ? grid[y][x - 1] : null; // LEFT
        return neighbors;
    }

    // Optional: render the grid using TileMapParser to a BufferedImage
    public BufferedImage render(TileMapParser parser) {
        // Renders the final piece, mainly done for testing purposes
        int tileWidth = parser.tileWidth;
        int tileHeight = parser.tileHeight;
        BufferedImage result = new BufferedImage(size * tileWidth, size * tileHeight, BufferedImage.TYPE_INT_ARGB);
        var g = result.getGraphics();

        for (int y = 0; y < size; y++) {
            for (int x = 0; x < size; x++) {
                Tile tile = grid[y][x];
                if (tile != null) {
                    var subImage = parser.getTile(tile.getTilemapLocation().x, tile.getTilemapLocation().y);
                    g.drawImage(subImage, x * tileWidth, y * tileHeight, null);
                }
            }
        }
        g.dispose();
        return result;
    }
}
