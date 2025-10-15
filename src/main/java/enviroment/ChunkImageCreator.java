package enviroment;

import wfc.utils.ImageUtils;
import wfc.WaveFunctionCollapse;
import wfc.Tile;
import wfc.TileFactory;
import wfc.TileMapParser;
import wfc.Plane;
import wfc.Direction;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.*;
import java.util.List;


public class ChunkImageCreator {
    private final List<BufferedImage> preGeneratedImages = new ArrayList<>();
    private final Random random = new Random();

    public ChunkImageCreator(int imageCount, int planeSize) {
        generateChunkImages(imageCount, planeSize);
    }

    private void generateChunkImages(int count, int planeSize) {
        try {
            // Set up grass
            Plane grassPlane;
            List<Tile> grassTiles;
            TileMapParser grassParser;
            WaveFunctionCollapse grassWFC;

            // Set up water
            Plane waterPlane;
            List<Tile> waterTiles;
            TileMapParser waterParser;
            WaveFunctionCollapse waterWFC;

            for (int i = 0; i < count; i++) {
                // --- GRASS ---
                grassPlane = new Plane(planeSize);
                grassTiles = TileFactory.generateTiles();
                grassParser = new TileMapParser("/tiles/Grass.png", 16, 16);
                grassWFC = new WaveFunctionCollapse(grassPlane, grassTiles);
                grassWFC.SetEdge(new Tile(
                        "GRASS",
                        new Point(1, 1),
                        Map.of(
                                Direction.UP, "AAA",
                                Direction.DOWN, "AAA",
                                Direction.LEFT, "AAA",
                                Direction.RIGHT, "AAA"
                        )
                ));
                grassWFC.collapse();

                // --- WATER ---
                waterPlane = new Plane(planeSize);
                waterTiles = new ArrayList<>();
                waterTiles.add(new Tile(
                        "WATER",
                        new Point(0, 0),
                        Map.of(
                                Direction.UP, "*",
                                Direction.DOWN, "*",
                                Direction.LEFT, "*",
                                Direction.RIGHT, "*"
                        )
                ));
                waterParser = new TileMapParser("/tiles/Water.png", 16, 16);
                waterWFC = new WaveFunctionCollapse(waterPlane, waterTiles);
                waterWFC.collapse();

                // Combine layers
                BufferedImage grassImage = grassPlane.render(grassParser);
                BufferedImage waterImage = waterPlane.render(waterParser);
                BufferedImage finalImage = ImageUtils.overlayImages(waterImage, grassImage);

                ImageUtils.saveImage(finalImage, "src/main/resources/tiles/finalTiles/final"+i+".png");

                preGeneratedImages.add(finalImage);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}