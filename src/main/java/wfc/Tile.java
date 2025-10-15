package wfc;

import java.awt.*;
import java.util.Map;

/** Represents an individual tile in a tile sheet **/

public class Tile {
    private final String name; // Arbitrary name of tile, must be unique for each tile
    private final Point tilemapLocation; //column, row in the tilemap PNG
    private final Map<Direction, String> edges; // UP, DOWN, LEFT, RIGHT

    public Tile(String name, Point tilemapLocation, Map<Direction, String> edges) {
        this.name = name;
        this.tilemapLocation = tilemapLocation;
        this.edges = edges;
    }
    public Point getTilemapLocation() {
        return this.tilemapLocation;
    }
    public String getEdge(Direction dir) {
        return edges.get(dir);
    }

    @Override
    public String toString() {
        return "Tile{name='" + name + "', tilemapLocation=" + tilemapLocation + ", edges=" + edges + "}";
    }
}
