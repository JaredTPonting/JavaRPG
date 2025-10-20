package utils;

public class Cooldown {
    private long duration;
    private long lastUsed;

    public Cooldown(long duration) {
        this.duration = duration;
        this.lastUsed = 0;
    }

    public boolean ready() {
        return System.currentTimeMillis() - lastUsed >= duration;
    }

    public void reset() {
        lastUsed = System.currentTimeMillis();
    }
}
