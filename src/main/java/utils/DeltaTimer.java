package utils;

public class DeltaTimer {
    private long lastTime;

    public DeltaTimer() {
        this.lastTime = System.nanoTime();
    }
    public double getDelta() {
        long now = System.nanoTime();
        double delta = (now - lastTime) / 1_000_000_000.0;
        lastTime = now;
        return delta;
    }

    public void reset() {
        lastTime = System.nanoTime();
    }
}
