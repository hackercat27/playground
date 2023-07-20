package ca.hackercat.playground.io;

import ca.hackercat.logging.Logger;
import ca.hackercat.playground.PlaygroundPanel;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;

/**
 *
 */
public class PGMouseListener implements MouseListener, MouseMotionListener, MouseWheelListener {

    private static final Logger LOGGER = Logger.get(PGMouseListener.class);

    private PGMouseListener() {}
    private static PGMouseListener instance;
    public static PGMouseListener get() {
        if (instance == null) {
            instance = new PGMouseListener();
        }
        return instance;
    }
    private Thread thread;
    private Runnable dxUpdater = new Runnable() {
        @Override
        public void run() {
            double updateTimeMillis = 1000d / pgp.getTargetTPS();
            double nextUpdateTimeMillis = System.currentTimeMillis() + updateTimeMillis;
    
            while (true) {
                updateTimeMillis = 1000d / pgp.getTargetTPS();
                updateDX();
        
                try {
                    double remainingTime = nextUpdateTimeMillis - System.currentTimeMillis();
            
                    if (remainingTime > 0) {
                        Thread.sleep((long) remainingTime);
                    }
            
                    nextUpdateTimeMillis += updateTimeMillis;
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
        
            }
        }
    };
    
    private PlaygroundPanel pgp;
    public void setPanel(PlaygroundPanel pgp) {
        this.pgp = pgp;
    }
    public void start() {
        if (thread == null) {
            thread = new Thread(dxUpdater, "mouse-dx-updater");
            thread.start();
        }
    }
    
    private final int arrayLength = 0x10;
    private final boolean[] held = new boolean[arrayLength];
    private final long[] pressedTimeMillis = new long[arrayLength];
    private final long[] releasedTimeMillis = new long[arrayLength];
    private boolean mouseOnScreen;
    private int mouseX, mouseY;
    private int lastMouseX, lastMouseY;
    private int mouseDX, mouseDY;
    private int mouseScrollAmount;
    private int lastMouseScrollAmount;
    private int mouseDScroll;
    
    public static final int BUTTON_LEFT = MouseEvent.BUTTON1;
    public static final int BUTTON_MIDDLE = MouseEvent.BUTTON2;
    public static final int BUTTON_RIGHT = MouseEvent.BUTTON3;
    public static final int BUTTON_BACK = 4;
    public static final int BUTTON_FORWARD = 5;
    
    
    @Override
    public void mouseClicked(MouseEvent e) {}
    
    @Override
    public void mousePressed(MouseEvent e) {
        int button = e.getButton();
        if (held[button]) return;
        held[button] = true;
        pressedTimeMillis[button] = System.currentTimeMillis();
    }
    
    @Override
    public void mouseReleased(MouseEvent e) {
        int button = e.getButton();
        if (!held[button]) return;
        held[button] = false;
        releasedTimeMillis[button] = System.currentTimeMillis();
    }
    
    @Override
    public void mouseEntered(MouseEvent e) {
        mouseOnScreen = true;
    }
    
    @Override
    public void mouseExited(MouseEvent e) {
        mouseOnScreen = false;
    }
    
    @Override
    public void mouseDragged(MouseEvent e) {
        mouseMoved(e);
    }
    @Override
    public void mouseMoved(MouseEvent e) {
        mouseX = e.getX();
        mouseY = e.getY();
    }
    
    @Override
    public void mouseWheelMoved(MouseWheelEvent e) {
        mouseScrollAmount += e.getWheelRotation();
    }
    
    private void updateDX() {
        mouseDX = mouseX - lastMouseX;
        mouseDY = mouseY - lastMouseY;
        mouseDScroll = mouseScrollAmount - lastMouseScrollAmount;
        lastMouseX = mouseX;
        lastMouseY = mouseY;
        lastMouseScrollAmount = mouseScrollAmount;
    }
    
    public boolean isOnScreen() {
        return mouseOnScreen;
    }
    public boolean isButtonHeld(int button) {
        if (button < 0 || button >= arrayLength) {
            LOGGER.warn("Out of bounds mouse button was polled! (" + button + ")");
            return false;
        }
        return held[button];
    }
    public boolean isButtonPressed(int button) {
        if (button < 0 || button >= arrayLength) {
            LOGGER.warn("Out of bounds mouse button was polled! (" + button + ")");
            return false;
        }
        if (pgp == null) {
            return false;
        }
        double tickTimeMillis = 1000 / pgp.getTPS();
        return System.currentTimeMillis() - pressedTimeMillis[button] <= tickTimeMillis;
    }
    public boolean isButtonReleased(int button) {
        if (button < 0 || button >= arrayLength) {
            LOGGER.warn("Out of bounds mouse button was polled! (" + button + ")");
            return false;
        }
        if (pgp == null) {
            return false;
        }
        double tickTimeMillis = 1000 / pgp.getTPS();
        return System.currentTimeMillis() - releasedTimeMillis[button] <= tickTimeMillis;
    }
    public double getX() {
        return (mouseX - pgp.getWindowXOffset()) / pgp.getWindowScale();
    }
    public double getY() {
        return (mouseY - pgp.getWindowYOffset()) / pgp.getWindowScale();
    }
    public double getDX() {
        return mouseDX / pgp.getWindowScale();
    }
    public double getDY() {
        return mouseDY / pgp.getWindowScale();
    }
    
    public int getScrollAmount() {
        return mouseScrollAmount;
    }
    public int getDScroll() {
        return mouseDScroll;
    }
    public void unstickButtons() {
        for (int i = 0; i < held.length; i++) {
            held[i] = false;
            releasedTimeMillis[i] = System.currentTimeMillis() - 1000;
        }
    }

}
