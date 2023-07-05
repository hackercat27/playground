package ca.hackercat.playground.io;

import ca.hackercat.playground.PlaygroundPanel;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import static ca.hackercat.playground.PlaygroundPanel.LOGGER;

/**
 * Implementation of a key listener that provides simple methods to check arbitrary keys on the keyboard.
 */
public final class PGKeyListener implements KeyListener {
    private PGKeyListener() {}
    private static PGKeyListener instance;
    public static PGKeyListener get() {
        if (instance == null) {
            instance = new PGKeyListener();
        }
        return instance;
    }
    
    private PlaygroundPanel pgp;
    public void setPanel(PlaygroundPanel pgp) {
        this.pgp = pgp;
    }
    
    private final int arrayLength = 0x10000;
    private final boolean[] held = new boolean[arrayLength];
    private final long[] pressedTimeMillis = new long[arrayLength];
    private final long[] releasedTimeMillis = new long[arrayLength];
    private long keyRepeatTimeMillis = 500;

    @Override
    public void keyTyped(KeyEvent e) {}
    
    @Override
    public void keyPressed(KeyEvent e) {
        int code = e.getKeyCode();
        if (held[code]) return;
        held[code] = true;
        pressedTimeMillis[code] = System.currentTimeMillis();
    }
    
    @Override
    public void keyReleased(KeyEvent e) {
        int code = e.getKeyCode();
        if (!held[code]) return;
        held[code] = false;
        releasedTimeMillis[code] = System.currentTimeMillis();
    }
    
    public boolean isKeyHeld(int keyCode) {
        if (keyCode < 0 || keyCode >= arrayLength) {
            LOGGER.warn("Out of bounds key code was polled! (" + keyCode + ")");
            return false;
        }
        return held[keyCode];
    }
    public boolean isKeyPressed(int keyCode) {
        if (keyCode < 0 || keyCode >= arrayLength) {
            LOGGER.warn("Out of bounds key code was polled! (" + keyCode + ")");
            return false;
        }
        if (pgp == null) {
            return false;
        }
        double tickTimeMillis = 1000 / pgp.getTPS();
        return System.currentTimeMillis() - pressedTimeMillis[keyCode] < tickTimeMillis;
    }
    public boolean isKeyReleased(int keyCode) {
        if (keyCode < 0 || keyCode >= arrayLength) {
            LOGGER.warn("Out of bounds key code was polled! (" + keyCode + ")");
            return false;
        }
        if (pgp == null) {
            return false;
        }
        double tickTimeMillis = 1000 / pgp.getTPS();
        return System.currentTimeMillis() - releasedTimeMillis[keyCode] <= tickTimeMillis;
    }

    public boolean isKeyHeldRepeat(int keyCode) {
        if (keyCode < 0 || keyCode >= arrayLength) {
            LOGGER.warn("Out of bounds key code was polled! (" + keyCode + ")");
            return false;
        }
        if (pgp == null) {
            return false;
        }
        if (System.currentTimeMillis() - pressedTimeMillis[keyCode] < keyRepeatTimeMillis) {
            double tickTimeMillis = 1000 / pgp.getTPS();
            return System.currentTimeMillis() - pressedTimeMillis[keyCode] < tickTimeMillis;
        }
        else {
            return held[keyCode];
        }
    }

    /**
     * default value is 500ms
     */
    public void setKeyRepeatTime(long keyRepeatTimeMillis) {
        this.keyRepeatTimeMillis = keyRepeatTimeMillis;
    }

    public void unstickKeys() {
        for (int i = 0; i < held.length; i++) {
            held[i] = false;
            releasedTimeMillis[i] = System.currentTimeMillis() - 1000;
        }
    }
}
