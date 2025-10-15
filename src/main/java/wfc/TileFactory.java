package wfc;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/* Used to build your tiles, defined in tileDefs. Each entry represents a tile in the tileset. */

public class TileFactory {
    public static Tile createTile(String name, int col, int row, String up, String right, String down, String left) {
        return new Tile(name, new Point(col, row), Map.of(
                Direction.UP, up,
                Direction.RIGHT, right,
                Direction.DOWN, down,
                Direction.LEFT, left
        ));
    }

    public static List<Tile> generateTiles() {
        // Generates a bunch of tiles using the createTile method and the tileDefs array
        String[][] tileDefs = {
                {"Grass1", "0", "0", "BBB", "BAA", "BAA", "BBB"},
                {"Grass2", "0", "1", "BAA", "AAA", "BAA", "BBB"},
                {"Grass3", "0", "2", "BAA", "AAB", "BBB", "BBB"},
                {"Grass4", "0", "3", "BBB", "BAB", "BBB", "BBB"},

                {"Grass5", "1", "0", "BBB", "BAA", "AAA", "BAA"},
                {"Grass6", "1", "1", "AAA", "AAA", "AAA", "AAA"},
                {"Grass7", "1", "2", "AAA", "AAB", "BBB", "AAB"},
                {"Grass8", "1", "3", "BBB", "BAB", "BBB", "BAB"},

                {"Grass9", "2", "0", "BBB", "BBB", "AAB", "BAA"},
                {"Grass10", "2", "1", "AAB", "BBB", "AAB", "AAA"},
                {"Grass11", "2", "2", "AAB", "BBB", "BBB", "AAB"},
                {"Grass12", "2", "3", "BBB", "BBB", "BBB", "BAB"},

                {"Grass13", "3", "0", "BBB", "BBB", "BAB", "BBB"},
                {"Grass14", "3", "1", "BAB", "BBB", "BAB", "BBB"},
                {"Grass15", "3", "2", "BAB", "BBB", "BBB", "BBB"},
                {"Grass16", "3", "3", "BBB", "BBB", "BBB", "BBB"},

                {"Grass17", "4", "0", "BBB", "BAB", "BAB", "BBB"},
                {"Grass18", "4", "1", "BAA", "AAB", "BAB", "BBB"},
                {"Grass19", "4", "2", "BAB", "BAA", "BAA", "BBB"}, // potentially wrong
                {"Grass20", "4", "3", "BAB", "BAB", "BBB", "BBB"},
                {"Grass21", "4", "4", "BAB", "BAB", "BAB", "BBB"}, // check

                {"Grass22", "5", "0", "BBB", "BAB", "AAB", "BAA"},
                {"Grass23", "5", "1", "AAA", "AAB", "AAB", "AAA"},
                {"Grass24", "5", "2", "AAB", "BAA", "AAA", "AAA"},
                {"Grass25", "5", "3", "AAB", "BAB", "BBB", "AAB"}, // check
                {"Grass26", "5", "4", "AAB", "BAB", "AAB", "AAA"},

                {"Grass27", "6", "0", "BBB", "BAA", "BAA", "BAB"},
                {"Grass28", "6", "1", "AAA", "AAA", "BAA", "AAB"},
                {"Grass29", "6", "2", "BAA", "AAA", "AAA", "BAA"},
                {"Grass30", "6", "3", "BAA", "AAB", "BBB", "BAB"},
                {"Grass31", "6", "4", "BAA", "AAA", "BAA", "BAB"},

                {"Grass32", "7", "0", "BBB", "BBB", "BAB", "BAB"},
                {"Grass33", "7", "1", "AAB", "BBB", "BAB", "AAB"},
                {"Grass34", "7", "2", "BAB", "BBB", "AAB", "BAA"},
                {"Grass35", "7", "3", "BAB", "BBB", "BBB", "BAB"},
                {"Grass36", "7", "4", "BAB", "BBB", "BAB", "BAB"},

                {"Grass37", "8", "0", "BBB", "BAB", "BAB", "BAB"},
                {"Grass38", "8", "1", "AAA", "AAB", "BAB", "AAB"},
                {"Grass39", "8", "2", "BAB", "BAA", "AAA", "BAA"},
                {"Grass40", "8", "3", "BAB", "BAB", "BBB", "BAB"},
                {"Grass41", "8", "4", "BAB", "BAB", "BAB", "BAB"},

                {"Grass42", "9", "0", "BAA", "AAB", "AAB", "BAA"},
                {"Grass43", "9", "1", "AAB", "BAA", "BAA", "AAB"},
                {"Grass44", "9", "2", "BAB", "BAA", "BAA", "BAB"},
                {"Grass45", "9", "3", "BAA", "AAB", "BAB", "BAB"},

                {"Grass46", "10", "0", "BBB", "BBB", "BBB", "BBB"},
                {"Grass47", "10", "2", "BAB", "BAB", "AAB", "BAA"},
                {"Grass48", "10", "3", "AAB", "BAB", "BAB", "AAB"},
                {"EXTRAWATER1", "10", "0", "BBB", "BBB", "BBB", "BBB"},
                {"EXTRAWATER2", "10", "0", "BBB", "BBB", "BBB", "BBB"},
                {"EXTRAWATER3", "10", "0", "BBB", "BBB", "BBB", "BBB"},
                {"EXTRAWATER4", "10", "0", "BBB", "BBB", "BBB", "BBB"},
                {"EXTRAWATER5", "10", "0", "BBB", "BBB", "BBB", "BBB"},
                {"EXTRAWATER6", "10", "0", "BBB", "BBB", "BBB", "BBB"},
                {"EXTRAWATER6", "10", "0", "BBB", "BBB", "BBB", "BBB"},
                {"EXTRAWATER6", "10", "0", "BBB", "BBB", "BBB", "BBB"},
                {"EXTRAWATER6", "10", "0", "BBB", "BBB", "BBB", "BBB"},
                {"EXTRAWATER6", "10", "0", "BBB", "BBB", "BBB", "BBB"},
                {"EXTRAWATER6", "10", "0", "BBB", "BBB", "BBB", "BBB"},
                {"EXTRAWATER6", "10", "0", "BBB", "BBB", "BBB", "BBB"},
                {"EXTRAWATER6", "10", "0", "BBB", "BBB", "BBB", "BBB"},
                {"EXTRAWATER6", "10", "0", "BBB", "BBB", "BBB", "BBB"},
                {"EXTRAWATER6", "10", "0", "BBB", "BBB", "BBB", "BBB"},
                {"EXTRAWATER6", "10", "0", "BBB", "BBB", "BBB", "BBB"},
                {"EXTRAWATER6", "10", "0", "BBB", "BBB", "BBB", "BBB"},
                {"EXTRAWATER6", "10", "0", "BBB", "BBB", "BBB", "BBB"},
                {"EXTRAWATER6", "10", "0", "BBB", "BBB", "BBB", "BBB"},
                {"EXTRAWATER6", "10", "0", "BBB", "BBB", "BBB", "BBB"},
                {"EXTRAWATER6", "10", "0", "BBB", "BBB", "BBB", "BBB"},
                {"EXTRAWATER6", "10", "0", "BBB", "BBB", "BBB", "BBB"},
                {"EXTRAWATER6", "10", "0", "BBB", "BBB", "BBB", "BBB"},
                {"EXTRAWATER6", "10", "0", "BBB", "BBB", "BBB", "BBB"},
                {"EXTRAWATER6", "10", "0", "BBB", "BBB", "BBB", "BBB"},
                {"EXTRAWATER6", "10", "0", "BBB", "BBB", "BBB", "BBB"},
                {"EXTRAWATER6", "10", "0", "BBB", "BBB", "BBB", "BBB"},
                {"EXTRAWATER6", "10", "0", "BBB", "BBB", "BBB", "BBB"},
                {"EXTRAWATER6", "10", "0", "BBB", "BBB", "BBB", "BBB"},
                {"EXTRAWATER6", "10", "0", "BBB", "BBB", "BBB", "BBB"},
                {"EXTRAWATER6", "10", "0", "BBB", "BBB", "BBB", "BBB"},
                {"EXTRAWATER6", "10", "0", "BBB", "BBB", "BBB", "BBB"},
                {"EXTRAWATER6", "10", "0", "BBB", "BBB", "BBB", "BBB"},
                {"EXTRAWATER6", "10", "0", "BBB", "BBB", "BBB", "BBB"},
                {"EXTRAWATER6", "10", "0", "BBB", "BBB", "BBB", "BBB"},
                {"EXTRAWATER6", "10", "0", "BBB", "BBB", "BBB", "BBB"},
                {"EXTRAWATER6", "10", "0", "BBB", "BBB", "BBB", "BBB"},
                {"EXTRAWATER6", "10", "0", "BBB", "BBB", "BBB", "BBB"},
                {"EXTRAWATER6", "10", "0", "BBB", "BBB", "BBB", "BBB"},
                {"EXTRAWATER6", "10", "0", "BBB", "BBB", "BBB", "BBB"},
                {"EXTRAWATER6", "10", "0", "BBB", "BBB", "BBB", "BBB"},
                {"EXTRAWATER6", "10", "0", "BBB", "BBB", "BBB", "BBB"},
                {"EXTRAWATER6", "10", "0", "BBB", "BBB", "BBB", "BBB"},
                {"EXTRAWATER6", "10", "0", "BBB", "BBB", "BBB", "BBB"},
                {"EXTRAWATER6", "10", "0", "BBB", "BBB", "BBB", "BBB"},
                {"EXTRAWATER6", "10", "0", "BBB", "BBB", "BBB", "BBB"},
                {"EXTRAWATER6", "10", "0", "BBB", "BBB", "BBB", "BBB"},
                {"EXTRAWATER6", "10", "0", "BBB", "BBB", "BBB", "BBB"},
                {"EXTRAWATER6", "10", "0", "BBB", "BBB", "BBB", "BBB"},
                {"EXTRAWATER6", "10", "0", "BBB", "BBB", "BBB", "BBB"},
                {"EXTRAWATER6", "10", "0", "BBB", "BBB", "BBB", "BBB"},
                {"EXTRAWATER6", "10", "0", "BBB", "BBB", "BBB", "BBB"},
                {"EXTRAWATER6", "10", "0", "BBB", "BBB", "BBB", "BBB"},
                {"EXTRAWATER6", "10", "0", "BBB", "BBB", "BBB", "BBB"},
                {"EXTRAWATER6", "10", "0", "BBB", "BBB", "BBB", "BBB"},
                {"EXTRAWATER6", "10", "0", "BBB", "BBB", "BBB", "BBB"},
                {"EXTRAWATER6", "10", "0", "BBB", "BBB", "BBB", "BBB"},
                {"EXTRAWATER7", "10", "0", "BBB", "BBB", "BBB", "BBB"},
                {"EXTRAWATER8", "10", "0", "BBB", "BBB", "BBB", "BBB"},
                {"waterDetail1", "6", "5", "BBB", "BBB", "BBB", "BBB"},
                {"waterDetail1", "6", "6", "BBB", "BBB", "BBB", "BBB"},
                {"waterDetail1", "7", "5", "BBB", "BBB", "BBB", "BBB"},
                {"waterDetail1", "7", "6", "BBB", "BBB", "BBB", "BBB"},
                {"waterDetail1", "8", "5", "BBB", "BBB", "BBB", "BBB"},
                {"waterDetail1", "8", "6", "BBB", "BBB", "BBB", "BBB"},
                {"waterDetail1", "9", "5", "BBB", "BBB", "BBB", "BBB"},
                // Replace with your own tiles or use this with example tile sheet
        };

        List<Tile> tiles = new ArrayList<>();
        for (String[] def : tileDefs) {
            tiles.add(createTile(def[0],
                    Integer.parseInt(def[1]),
                    Integer.parseInt(def[2]),
                    def[3], def[4], def[5], def[6]));
        }
        return tiles;
    }
}
