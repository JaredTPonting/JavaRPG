package enviroment;

import java.awt.Graphics;
import java.util.HashMap;
import java.util.Map;

import entities.player.Player;
import utils.Camera;

public class ChunkLoader {
    private final int chunkSize; // Size of each chunk in pixels
    private final int gameWidth;
    private final int gameHeight;
    private final Player player;
    private final int chunkCount = 1;

    private final Map<String, Chunk> chunks = new HashMap<>();

    public ChunkLoader(Player player, int gameWidth, int gameHeight, int chunkSize) {
        this.player = player;
        this.gameWidth = gameWidth;
        this.gameHeight = gameHeight;
        this.chunkSize = chunkSize;
        ChunkImageCreator chunkImageCreator = new ChunkImageCreator(chunkCount, 20);
        WfcImagePool.init(chunkCount, chunkSize);
    }

    // Generate or fetch existing chunk
    private Chunk getOrCreateChunk(int chunkX, int chunkY) {
        String key = chunkX + "," + chunkY;
        return chunks.computeIfAbsent(key, k ->
                new WfcChunk(chunkX, chunkY, chunkSize, chunkCount)
        );
    }


    // Update visible chunks based on entities.player position
    public void update() {
        int playerChunkX = (int) player.getX() / chunkSize;
        int playerChunkY = (int) player.getY() / chunkSize;

        // How many chunks fit on screen?
        int chunksOnScreenX = (int) Math.ceil((double) gameWidth / chunkSize) + 1;
        int chunksOnScreenY = (int) Math.ceil((double) gameHeight / chunkSize) + 1;

        // Load nearby chunks
        for (int dx = -chunksOnScreenX; dx <= chunksOnScreenX; dx++) {
            for (int dy = -chunksOnScreenY; dy <= chunksOnScreenY; dy++) {
                int cx = playerChunkX + dx;
                int cy = playerChunkY + dy;
                getOrCreateChunk(cx, cy);
            }
        }
    }

    // Render all visible chunks
    public void render(Graphics g, Camera camera) {
        int playerChunkX = (int) player.getX() / chunkSize;
        int playerChunkY = (int) player.getY() / chunkSize;

        int chunksOnScreenX = (int) Math.ceil((double) gameWidth / chunkSize) + 1;
        int chunksOnScreenY = (int) Math.ceil((double) gameHeight / chunkSize) + 1;

        for (int dx = -chunksOnScreenX; dx <= chunksOnScreenX; dx++) {
            for (int dy = -chunksOnScreenY; dy <= chunksOnScreenY; dy++) {
                int cx = playerChunkX + dx;
                int cy = playerChunkY + dy;
                Chunk chunk = getOrCreateChunk(cx, cy);
                chunk.render(g, camera);
            }
        }
    }
}
