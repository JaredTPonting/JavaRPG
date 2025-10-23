package entities.enemies;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.InputStream;
import java.util.*;

public class RoundManager {

    private final Map<Integer, RoundsData> rounds = new HashMap<>();
    private int currentRound = 1;

    private static final String ROUND_FILE_PATH = "/config/rounds.json";

    public RoundManager() {
        loadRoundsFromJson();
    }

    private void loadRoundsFromJson() {
        try (InputStream input = getClass().getResourceAsStream(ROUND_FILE_PATH)) {
            if (input == null) {
                throw new RuntimeException("Rounds JSON not found at: " + ROUND_FILE_PATH);
            }

            ObjectMapper mapper = new ObjectMapper();
            JsonNode root = mapper.readTree(input);
            JsonNode roundsNode = root.get("rounds");

            for (JsonNode node : roundsNode) {
                RoundsData round = mapper.treeToValue(node, RoundsData.class);
                rounds.put(round.getId(), round);
            }

            System.out.println("[RoundManager] Loaded " + rounds.size() + " rounds.");

        } catch (Exception e) {
            throw new RuntimeException("Failed to load rounds JSON: " + e.getMessage(), e);
        }
    }

    // --- Public API ---

    public RoundsData getCurrentRoundData() {
        return rounds.getOrDefault(currentRound, getLastRound());
    }

    public void nextRound() {
        if (rounds.containsKey(currentRound + 1)) {
            currentRound++;
            System.out.println("[RoundManager] Advanced to Round " + currentRound);
        }
    }

    public int getCurrentRound() {
        return currentRound;
    }

    private RoundsData getLastRound() {
        return rounds.get(rounds.keySet().stream().max(Integer::compareTo).orElse(1));
    }

    public boolean shouldSpawnMiniBoss(Random random) {
        RoundsData round = getCurrentRoundData();
        return random.nextDouble() < round.getBossChance();
    }
}
