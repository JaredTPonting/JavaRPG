package utils;

import java.awt.*;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Simple profiler for tracking frame times of game systems.
 * Toggle with F3 (DEBUG_MODE) to show overlay.
 * Press F4 to dump a snapshot to logs/profile.log
 */
public class Profiler {
    private static final Profiler INSTANCE = new Profiler();

    private final Map<String, Long> startTimes = new LinkedHashMap<>();
    private final Map<String, Long> durations = new LinkedHashMap<>();
    private final Map<String, Long> smoothedDurations = new LinkedHashMap<>();

    // Frame timing
    private long frameStart;
    private long lastFrameTime;
    private double fps;
    private double smoothedFps;

    // Entity counts (set externally)
    private int enemyCount;
    private int projectileCount;
    private int lootCount;

    // Smoothing factor (0.0 - 1.0, higher = more responsive)
    private static final double SMOOTH_FACTOR = 0.1;

    // Logging
    private boolean loggingEnabled = false;
    private PrintWriter logWriter;
    private int frameCount = 0;
    private static final int LOG_INTERVAL = 60; // Log every N frames

    private Profiler() {}

    public static Profiler get() {
        return INSTANCE;
    }

    public void frameStart() {
        frameStart = System.nanoTime();
    }

    public void frameEnd() {
        lastFrameTime = System.nanoTime() - frameStart;
        fps = 1_000_000_000.0 / lastFrameTime;
        smoothedFps = smoothedFps == 0 ? fps : lerp(smoothedFps, fps, SMOOTH_FACTOR);

        // Smooth all durations
        for (Map.Entry<String, Long> entry : durations.entrySet()) {
            String key = entry.getKey();
            long value = entry.getValue();
            long smoothed = smoothedDurations.getOrDefault(key, value);
            smoothedDurations.put(key, (long) lerp(smoothed, value, SMOOTH_FACTOR));
        }

        // Periodic logging
        if (loggingEnabled && ++frameCount >= LOG_INTERVAL) {
            frameCount = 0;
            logSnapshot();
        }
    }

    public void start(String section) {
        startTimes.put(section, System.nanoTime());
    }

    public void end(String section) {
        Long startTime = startTimes.get(section);
        if (startTime != null) {
            durations.put(section, System.nanoTime() - startTime);
        }
    }

    public void setEntityCounts(int enemies, int projectiles, int loot) {
        this.enemyCount = enemies;
        this.projectileCount = projectiles;
        this.lootCount = loot;
    }

    public void render(Graphics2D g, int screenWidth) {
        g.setFont(new Font("Monospaced", Font.PLAIN, 12));

        int x = screenWidth - 220;
        int y = 20;
        int lineHeight = 16;

        // Background
        int height = 80 + (smoothedDurations.size() * lineHeight);
        g.setColor(new Color(0, 0, 0, 180));
        g.fillRect(x - 10, y - 14, 220, height);

        // Title
        g.setColor(Color.YELLOW);
        g.drawString("=== PROFILER (F3) ===", x, y);
        y += lineHeight;

        // FPS
        g.setColor(smoothedFps < 30 ? Color.RED : smoothedFps < 55 ? Color.YELLOW : Color.GREEN);
        g.drawString(String.format("FPS: %.1f (%.2fms)", smoothedFps, lastFrameTime / 1_000_000.0), x, y);
        y += lineHeight;

        // Entity counts
        g.setColor(Color.CYAN);
        g.drawString(String.format("Enemies: %d  Proj: %d", enemyCount, projectileCount), x, y);
        y += lineHeight;
        g.drawString(String.format("Loot: %d  Total: %d", lootCount, enemyCount + projectileCount + lootCount + 1), x, y);
        y += lineHeight + 4;

        // Section timings
        g.setColor(Color.WHITE);
        g.drawString("--- Section Times ---", x, y);
        y += lineHeight;

        for (Map.Entry<String, Long> entry : smoothedDurations.entrySet()) {
            double ms = entry.getValue() / 1_000_000.0;
            double percent = (entry.getValue() * 100.0) / lastFrameTime;

            // Color based on percentage of frame
            if (percent > 30) g.setColor(Color.RED);
            else if (percent > 15) g.setColor(Color.YELLOW);
            else g.setColor(Color.WHITE);

            g.drawString(String.format("%-12s %5.2fms %5.1f%%", entry.getKey(), ms, percent), x, y);
            y += lineHeight;
        }

        // Logging status
        y += 4;
        g.setColor(loggingEnabled ? Color.GREEN : Color.GRAY);
        g.drawString("Logging: " + (loggingEnabled ? "ON (F4 toggle)" : "OFF (F4 toggle)"), x, y);
    }

    public void toggleLogging() {
        loggingEnabled = !loggingEnabled;
        if (loggingEnabled) {
            startLogging();
        } else {
            stopLogging();
        }
    }

    private void startLogging() {
        try {
            java.io.File logDir = new java.io.File("logs");
            if (!logDir.exists()) logDir.mkdirs();

            String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd_HH-mm-ss"));
            logWriter = new PrintWriter(new FileWriter("logs/profile_" + timestamp + ".log"));
            logWriter.println("JavaRPG Profiler Log - " + LocalDateTime.now());
            logWriter.println("=====================================");
            logWriter.println();
            logWriter.printf("%-12s %-8s %-8s %-8s %-8s", "Section", "Time(ms)", "FPS", "Enemies", "Projs");
            for (String section : smoothedDurations.keySet()) {
                logWriter.printf(" %-10s", section);
            }
            logWriter.println();
            logWriter.flush();
        } catch (IOException e) {
            System.err.println("Failed to start profiler logging: " + e.getMessage());
            loggingEnabled = false;
        }
    }

    private void stopLogging() {
        if (logWriter != null) {
            logWriter.println();
            logWriter.println("=== Logging stopped ===");
            logWriter.close();
            logWriter = null;
        }
    }

    private void logSnapshot() {
        if (logWriter == null) return;

        logWriter.printf("%-12s %-8.2f %-8.1f %-8d %-8d",
                LocalDateTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss")),
                lastFrameTime / 1_000_000.0,
                smoothedFps,
                enemyCount,
                projectileCount);

        for (Long duration : smoothedDurations.values()) {
            logWriter.printf(" %-10.2f", duration / 1_000_000.0);
        }
        logWriter.println();
        logWriter.flush();
    }

    private double lerp(double a, double b, double t) {
        return a + (b - a) * t;
    }
}
