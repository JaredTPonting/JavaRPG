package utils;

public class DeltaTimer {
    private long lastTime;
    private boolean paused;

    public DeltaTimer() {
        lastTime = System.nanoTime();
    }

    public double getDelta() {
        if (paused) {
            return 0;
        }
        long now = System.nanoTime();
        double delta = (now - lastTime) / 1_000_000_000.0;
        lastTime = now;
        return delta;
    }

    public void pause() {
        if (!paused) {
            paused = true;
        }
    }

    public void resume() {
        if (paused) {
            paused = false;
            lastTime = System.nanoTime(); // reset so delta after resume is small
        }
    }
}

