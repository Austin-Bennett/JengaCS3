package game.utils;

public class StopWatch {
    private long start;


    public StopWatch() {

    }

    public void reset() {
        this.start = System.nanoTime();
    }

    public long nanoseconds() {
        return System.nanoTime() - start;
    }


    public float nanosecondsF() {
        return (float) (System.nanoTime() - start);
    }

    public float secondsF() {
        return nanosecondsF() / 1e9f;
    }
}
