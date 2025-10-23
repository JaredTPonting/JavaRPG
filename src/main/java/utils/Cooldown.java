package utils;

public class Cooldown {
    private final long duration;
    private long lastUsed;

    public Cooldown(long duration) {
        this.duration = duration;
        this.lastUsed = System.currentTimeMillis();
    }

    public boolean ready() {
        return System.currentTimeMillis() - lastUsed >= duration;
    }

    public double percentDone() {
        return (double) (System.currentTimeMillis() / lastUsed) / duration;
    }

    public void reset() {
        lastUsed = System.currentTimeMillis();
    }
}
