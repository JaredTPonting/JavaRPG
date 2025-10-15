package enemies;

import com.fasterxml.jackson.core.exc.StreamReadException;
import com.fasterxml.jackson.databind.DatabindException;
import player.Player;
import utils.SpriteLoader;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

public class EnemyFactory {
    private static Map<String, EnemyConfig> configs = new HashMap<>();

    static {
        try {
            ObjectMapper mapper = new ObjectMapper();
            InputStream is = EnemyFactory.class.getResourceAsStream("/config/enemies.json");
            Map<String, EnemyConfig> loaded = mapper.readValue(is, mapper.getTypeFactory().constructMapType(Map.class, String.class, EnemyConfig.class));
            configs.putAll(loaded);
            System.out.println("Enemu configs loaded: " + configs.keySet());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static Enemy create(String type, int x, int y, Player target) {
        EnemyConfig cfg = configs.get(type);
        if (cfg == null) {
            throw new IllegalArgumentException("Unkown enemy seleciton");
        }
        Enemy e = new Enemy(x, y, target);
        e.hp = cfg.hp;
        e.speed = cfg.speed;
        e.sprite = SpriteLoader.load(cfg.sprite);
        e.XP = cfg.xpBase + (target.getLevel() * cfg.xpPerLevel);
        return e;

    }



    public static class EnemyConfig {
        public double hp;
        public double speed;
        public String sprite;
        public int xpBase;
        public int xpPerLevel;
    }
}
