package wfc;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;

/* The big boy. Uses all the other classes to complete wave function collapse.
Have issues where if a cell doesn't have any options it just uses  an ERROR_CELL that you will have to define below
Hopefully you will just have a complete tileset that doesn't have any missing pieces */

public class WaveFunctionCollapse {
    private final Plane plane;
    private final List<Tile> tiles;
    private final ArrayList[][] possibilities;
    private final Random random = new Random();

    public static final Tile ERROR_TILE = new Tile(
            "ERROR",
            new Point(10, 0), // grabs empty tile from grass.png tileset, set this to what ever tile you want
            Map.of(
                    Direction.UP, "*",
                    Direction.DOWN, "*",
                    Direction.LEFT, "*",
                    Direction.RIGHT, "*"
            )
    );

    public WaveFunctionCollapse(Plane plane, List<Tile> tiles) {
        this.plane = plane;
        this.tiles = tiles;
        int size = plane.getSize();

        // Initialize possibilities: each cell starts with all tiles
        possibilities = new ArrayList[size][size];
        for (int y = 0; y < size; y++) {
            for (int x = 0; x < size; x++) {
                possibilities[y][x] = new ArrayList<>(tiles);
            }
        }
    }

    /** Main loop to collapse the plane */
    public void collapse() {
        while (!plane.isCollapsed()) {
            int[] cell = pickLowestEntropyCell();
            if (cell == null) break; // no un collapsed cells left

            int x = cell[0], y = cell[1];
            List<Tile> options = possibilities[y][x];

            if (options.isEmpty()) {
                System.out.println("No options for (" + x + "," + y + "), forcing ERROR_TILE");
                plane.setTile(x, y, ERROR_TILE);
                possibilities[y][x].add(ERROR_TILE);
                continue;
            }

            // Randomly pick a tile from possibilities
            Tile chosen = options.get(random.nextInt(options.size()));
            plane.setTile(x, y, chosen);

            // Collapse possibilities for this cell
            possibilities[y][x].clear();
            possibilities[y][x].add(chosen);

            // Propagate constraints to neighbors
            propagateConstraints(x, y);
        }
    }

    /** Pick cell with the lowest entropy (excluding collapsed) */
    private int[] pickLowestEntropyCell() {
        int minEntropy = Integer.MAX_VALUE;
        List<int[]> candidates = new ArrayList<>();

        int size = plane.getSize();
        for (int y = 0; y < size; y++) {
            for (int x = 0; x < size; x++) {
                if (plane.getTile(x, y) != null) continue; // already collapsed so onto next cell

                // check cell entropy, if equal to lowest, add to candidates, if lowest, clear candidates and update the lowest entropy
                int entropy = possibilities[y][x].size();
                if (entropy < minEntropy && entropy > 0) {
                    minEntropy = entropy;
                    candidates.clear();
                    candidates.add(new int[]{x, y});
                } else if (entropy == minEntropy) {
                    candidates.add(new int[]{x, y});
                }
            }
        }

        if (candidates.isEmpty()) return null;

        return candidates.get(random.nextInt(candidates.size()));
    }

    /** Propagate constraints to neighbors */
    private void propagateConstraints(int x, int y) {
        Tile chosen = plane.getTile(x, y);
        Tile[] neighbors = plane.getNeighbours(x, y);

        Direction[] directions = {Direction.UP, Direction.RIGHT, Direction.DOWN, Direction.LEFT};

        /** cycle through neighbours **/
        for (int i = 0; i < neighbors.length; i++) {
            Tile neighbor = neighbors[i];
            if (neighbor == null) {
                /** get coords for neighbour **/
                int nx = x, ny = y;
                switch (i) {
                    case 0 -> ny = y - 1; // UP
                    case 1 -> nx = x + 1; // RIGHT
                    case 2 -> ny = y + 1; // DOWN
                    case 3 -> nx = x - 1; // LEFT
                }

                /** skip if neighbor is outside bounds **/
                if (nx < 0 || ny < 0 || nx >= plane.getSize() || ny >= plane.getSize()) continue;

                /** Get possibilities for neighbour **/
                List<Tile> neighborOptions = possibilities[ny][nx];
                int finalI = i;

                /** Remove possibilities from neighbour**/
                neighborOptions.removeIf(t -> !edgesMatch(chosen, t, directions[finalI]));

                if (neighborOptions.isEmpty()) {
                    System.out.println("Contradiciton at(" + nx + ", " + ny + "), inserting ERROR TILE");
                    neighborOptions.add(ERROR_TILE);
                    plane.setTile(nx, ny, ERROR_TILE);
                }
            }
        }
    }

    public void SetEdge(Tile tile) {
        int size = plane.getSize();

        for (int y = 0; y < size; y++) {
            for (int x = 0; x < size; x++) {
                boolean isEdge = x == 0 || y == 0 || x == size - 1 || y == size - 1;
                if (isEdge) {
                    plane.setTile(x, y, tile);
                    possibilities[y][x].clear();
                    possibilities[y][x].add(tile);
                    propagateConstraints(x, y);
                }
            }
        }
    }

    public void fillMap(Tile tile) {
        int size = plane.getSize();
        for (int y = 0; y < size; y++){
            for (int x = 0; x < size; x++) {
                plane.setTile(x, y, tile);
                possibilities[y][x].clear();
                possibilities[y][x].add(tile);
            }
        }
    }

    /** Check if two tiles are compatible in a given direction */
    private boolean edgesMatch(Tile t1, Tile t2, Direction dir) {
        switch (dir) {
            case UP -> {
                return matches(t1.getEdge(Direction.UP), t2.getEdge(Direction.DOWN));
            }
            case RIGHT -> {
                return matches(t1.getEdge(Direction.RIGHT), t2.getEdge(Direction.LEFT));
            }
            case DOWN -> {
                return matches(t1.getEdge(Direction.DOWN), t2.getEdge(Direction.UP));
            }
            case LEFT -> {
                return matches(t1.getEdge(Direction.LEFT), t2.getEdge(Direction.RIGHT));
            }
        }
        return false;
    }

    /** Helper to allow wildcard edges ("*") */
    private boolean matches(String edgeA, String edgeB) {
        return "*".equals(edgeA) || "*".equals(edgeB) || edgeA.equals(edgeB);
    }

    /** Get remaining possible tiles for a cell (for debugging) */
    public List<Tile> getPossibilities(int x, int y) {
        return possibilities[y][x];
    }
}
