package wfc;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;

/* Made this to help read tilemapa and extract a unique tile from it based on coordinates given. (0,0) will be top left */
/* Basically after a Plane is collapsed, we then use the parser to turn the mapping into an actual square in the tile map */

public class TileMapParser {
    private final BufferedImage tilemapImage;
    final int tileWidth; // Width of each tile
    final int tileHeight; // Height of each tile (example grass are 16x16)
    public final int tilesPerRow; // how many tiles in a row
    public final int tilesPerColumn; // how many tiles in a column

    public TileMapParser(String tilemapResourcePath, int tileWidth, int tileHeight) throws IOException {
        // Load from classpath, works in IDE and JAR
        if (tilemapResourcePath == null){
            throw new IllegalArgumentException("Tilemap resource path can not be null!!!");
        }
        InputStream stream = getClass().getResourceAsStream(tilemapResourcePath);
        if (stream == null) {
            throw new IOException("Resource not found: " + tilemapResourcePath);
        }
        this.tilemapImage = ImageIO.read(stream);
        this.tileWidth = tileWidth;
        this.tileHeight = tileHeight;
        this.tilesPerRow = tilemapImage.getWidth() / tileWidth;
        this.tilesPerColumn = tilemapImage.getHeight() / tileHeight;
    }

    public BufferedImage getTile(int col, int row) {
        return tilemapImage.getSubimage(col * tileWidth, row * tileHeight, tileWidth, tileHeight);
    }
}
