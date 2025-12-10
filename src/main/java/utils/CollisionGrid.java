// C:/Users/Jared/Documents/Projects/Java/JavaRPG/src/main/java/utils/CollisionGrid.java
package utils;

import entities.enemies.Enemy;

import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CollisionGrid {

    private final int cellSize;
    // We now use a HashMap to store a sparse grid, perfect for an "infinite" world.
    private final Map<Point, List<Enemy>> grid;

    public CollisionGrid(int cellSize) {
        // We no longer need worldWidth and worldHeight!
        this.cellSize = cellSize;
        this.grid = new HashMap<>();
    }

    /**
     * Clears all entities from the grid.
     */
    public void clear() {
        // Clearing a HashMap is much faster than iterating through a giant 2D array.
        grid.clear();
    }

    /**
     * Adds an enemy to the appropriate grid cell.
     */
    public void add(Enemy enemy) {
        Rectangle hitbox = enemy.getHitBox();
        int cellX = (int) (hitbox.getCenterX() / cellSize);
        int cellY = (int) (hitbox.getCenterY() / cellSize);

        Point cellPoint = new Point(cellX, cellY);

        // Get the list of enemies for this cell, or create a new list if it's the first time.
        // This is the magic of the HashMap approach.
        List<Enemy> cellEnemies = grid.computeIfAbsent(cellPoint, k -> new ArrayList<>());
        cellEnemies.add(enemy);
    }

    /**
     * Retrieves a list of potential colliders near the given hitbox.
     */
    public List<Enemy> getPotentialColliders(Rectangle hitbox) {
        List<Enemy> potentialColliders = new ArrayList<>();
        int centerX = (int) (hitbox.getCenterX() / cellSize);
        int centerY = (int) (hitbox.getCenterY() / cellSize);

        // Iterate through the 3x3 block of cells.
        for (int i = -1; i <= 1; i++) {
            for (int j = -1; j <= 1; j++) {
                Point cellPoint = new Point(centerX + j, centerY + i);

                // If a cell exists in our map for this coordinate, add its enemies.
                if (grid.containsKey(cellPoint)) {
                    potentialColliders.addAll(grid.get(cellPoint));
                }
            }
        }
        return potentialColliders;
    }

    /**
     * Draws the grid for debugging. This is more complex for an infinite grid,
     * so we'll only draw the grid currently visible by the camera.
     */
    public void draw(Graphics g, Camera camera) {
        g.setColor(Color.YELLOW);

        // Calculate the grid boundaries visible on screen
        int startCol = (int) (camera.getX() / cellSize);
        int endCol = (int) ((camera.getX() + camera.getWidth()) / cellSize);
        int startRow = (int) (camera.getY() / cellSize);
        int endRow = (int) ((camera.getY() + camera.getHeight()) / cellSize);

        for (int row = startRow; row <= endRow; row++) {
            for (int col = startCol; col <= endCol; col++) {
                int x = col * cellSize;
                int y = row * cellSize;
                g.drawRect((int) (x - camera.getX()), (int) (y - camera.getY()), cellSize, cellSize);

                Point cellPoint = new Point(col, row);
                if (grid.containsKey(cellPoint) && !grid.get(cellPoint).isEmpty()) {
                    g.setColor(Color.RED);
                    g.fillRect((int) (x - camera.getX()) + (cellSize / 2) - 2, (int) (y - camera.getY()) + (cellSize / 2) - 2, 4, 4);
                    g.setColor(Color.YELLOW);
                }
            }
        }
    }
}