package ca.hackercat.playground.object;

import ca.hackercat.playground.io.PGKeyListener;
import ca.hackercat.playground.io.PGMouseListener;
import ca.hackercat.playground.io.PGXInputListener;

public abstract class InputManager {
    protected PGKeyListener kl = PGKeyListener.get();
    protected PGMouseListener ml = PGMouseListener.get();
    protected PGXInputListener cl = PGXInputListener.get();

    public void unstickButtons() {
        kl.unstickKeys();
        ml.unstickButtons();
//        cl.unstickButtons();
    }
}
