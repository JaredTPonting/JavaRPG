package entities.enemies;

import entities.player.Player;
import utils.Animation;
import core.GameWorld;
import utils.SpriteLoader;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

public class EnemyFactory {
    private static Map<String, EnemyConfig> configs = new HashMap<>();

    // Animation cache: enemyType -> (animationName -> Animation template)
    // Animations are created once and copied for each enemy instance
    private static Map<String, Map<String, Animation>> animationCache = new HashMap<>();

    public static class AnimationInfo {
        public String sheet;  // path to the PNG sprite sheet
        public int frames;    // number of frames in the sheet
        public boolean loop;  // if animation loops
    }

    static {
        try {
            ObjectMapper mapper = new ObjectMapper();
            InputStream is = EnemyFactory.class.getResourceAsStream("/config/enemies.json");
            Map<String, EnemyConfig> loaded = mapper.readValue(is, mapper.getTypeFactory().constructMapType(Map.class, String.class, EnemyConfig.class));
            configs.putAll(loaded);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Gets or creates cached animations for an enemy type.
     * Animations are created once (expensive) then copied for each enemy (cheap).
     */
    private static Map<String, Animation> getOrCreateAnimations(String type, EnemyConfig cfg) {
        return animationCache.computeIfAbsent(type, t -> {
            Map<String, Animation> templates = new HashMap<>();
            for (var entry : cfg.animations.entrySet()) {
                String name = entry.getKey();
                AnimationInfo info = entry.getValue();
                BufferedImage sheet = SpriteLoader.load(info.sheet);
                templates.put(name, new Animation(sheet, info.frames, 100, info.loop, cfg.size));
            }
            return templates;
        });
    }

    /**
     * Creates copies of cached animations for a new enemy instance.
     * Each enemy gets its own Animation objects with independent playback state,
     * but they share the underlying BufferedImage arrays (no memory duplication).
     */
    private static Map<String, Animation> copyAnimations(Map<String, Animation> templates) {
        Map<String, Animation> copies = new HashMap<>();
        for (var entry : templates.entrySet()) {
            copies.put(entry.getKey(), entry.getValue().copy());
        }
        return copies;
    }

    public static Enemy create(String type, GameWorld gameWorld, int x, int y, Player target) {
        EnemyConfig cfg = configs.get(type);
        if (cfg == null) {
            throw new IllegalArgumentException("Unknown enemy type: " + type);
        }

        // Get cached animation templates and create copies for this enemy
        Map<String, Animation> templates = getOrCreateAnimations(type, cfg);
        Map<String, Animation> enemyAnimations = copyAnimations(templates);

        Enemy e = new Enemy(gameWorld, x, y, cfg.attackSpeed, cfg.size, enemyAnimations, 0.1, 0.1);
        e.hp = cfg.hp;
        e.speed = cfg.speed;
        e.XP = cfg.xpBase + (target.getPlayerManager().getLevel() * cfg.xpPerLevel);
        e.damage = cfg.damage;
        e.setBoss(cfg.boss);
        e.setHitBox(cfg.hitBox.offsetXPercent, cfg.hitBox.offsetYPercent, cfg.hitBox.radiusPercent);
        return e;
    }


    public static class EnemyConfig {
        public double hp;
        public double speed;
        public int xpBase;
        public int xpPerLevel;
        public double attackSpeed;
        public double damage;
        public int size;
        public boolean boss;
        public Map<String, AnimationInfo> animations;
        public HitBoxInfo hitBox;

        public static class HitBoxInfo {
            public double offsetXPercent;
            public double offsetYPercent;
            public double radiusPercent;
        }
    }
}
