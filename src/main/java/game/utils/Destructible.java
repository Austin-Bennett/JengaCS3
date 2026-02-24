package game.utils;

import java.lang.ref.Cleaner;

public abstract class Destructible {

    private final Cleaner.Cleanable cleanable;
    private static final Cleaner cleaner = Cleaner.create();

    private static final class DestroyingAngel implements Runnable {
        Destructible reference;

        public DestroyingAngel(Destructible d) {
            this.reference = d;
        }

        @Override
        public void run() {
            reference.destruct();
        }
    }

    public Destructible() {
        cleanable = cleaner.register(this, new DestroyingAngel(this));
    }


    public abstract void destruct();
}
