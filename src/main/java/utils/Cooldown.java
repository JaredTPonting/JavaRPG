package utils;

public class Cooldown {
    private final double duration; // seconds
    private double timer;          // remaining time

    public Cooldown(double durationSeconds) {
        this.duration = durationSeconds;
        this.timer = duration;
    }

    public void update(double dt) {
        if (timer > 0) {
            timer -= dt;
        }
    }

    public boolean ready() {
        return timer <= 0;
    }

    public void reset() {
        timer = duration;
    }

    public double percentDone() {
        return 1.0 - Math.max(0, timer / duration);
    }
}

