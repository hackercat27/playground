package ca.hackercat.playground;

import ca.hackercat.logging.Logger;
import ca.hackercat.playground.io.PGKeyListener;
import ca.hackercat.playground.io.PGMouseListener;
import javax.swing.*;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

/**
 * A class with utility methods in an attempt to remove boilerplate code in regard to instantiating a
 * {@code JFrame} with a single {@code PlaygroundPanel} instance.
 */
public class JFrameManager {

    private static final Logger LOGGER = Logger.get(JFrameManager.class);
    
    private static GraphicsDevice[] devices = GraphicsEnvironment.getLocalGraphicsEnvironment().getScreenDevices();
    
    public static GraphicsDevice getDevice(int i) {
        if (i < 0 || i >= devices.length) {
            LOGGER.log("Attempted to access non-existing graphics device, returning null value");
        }
        return devices[i];
    }
    
    private static final KeyListener fullscreenKeyListener = new KeyListener() {
        @Override
        public void keyTyped(KeyEvent e) {}
    
        @Override
        public void keyPressed(KeyEvent e) {
            int code = e.getKeyCode();
            
            if (code == windowedKey) {
                setFullscreenState(0);
            }
            if (code == fullscreenKey) {
                setFullscreenState(1);
            }
            if (code == windowedBorderlessKey) {
                setFullscreenState(2);
            }
        }
    
        @Override
        public void keyReleased(KeyEvent e) {}
    };
    
    public static int windowedKey = KeyEvent.VK_F10;
    public static int fullscreenKey = KeyEvent.VK_F11;
    public static int windowedBorderlessKey = KeyEvent.VK_F12;
    
    private static JFrame jFrame;
    public static JFrame createDefaultJFrame(PlaygroundPanel pgp) {
        if (jFrame != null) {
            jFrame.setVisible(false);
            jFrame.dispose();
        }
        jFrame = new JFrame();
    
        PGKeyListener.get().setPanel(pgp);
        PGMouseListener.get().setPanel(pgp);
        PGMouseListener.get().start();
    
        pgp.addKeyListener(fullscreenKeyListener);
        pgp.addKeyListener(PGKeyListener.get());
        pgp.addMouseListener(PGMouseListener.get());
        pgp.addMouseMotionListener(PGMouseListener.get());
        pgp.addMouseWheelListener(PGMouseListener.get());
        
        pgp.setFocusable(true);
    
        jFrame.add(pgp);
    
        jFrame.setFocusable(false);
        jFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        jFrame.pack();
        jFrame.setLocationRelativeTo(null);
        jFrame.setVisible(true);
        
        return jFrame;
    }
    
    private static int windowState = 0;
    private static void setFullscreenState(int state) {
        if (windowState == state) return;
        windowState = state;
        GraphicsDevice screen = getDevice(0);
        if (state == 1) {
            screen.setFullScreenWindow(jFrame);
        }
        else {
            jFrame.dispose();
            switch (state) {
                case 0 -> {
                    jFrame.setUndecorated(false);
                    jFrame.setExtendedState(JFrame.NORMAL);
                    jFrame.pack();
                    jFrame.setLocationRelativeTo(null);
                }
                case 2 -> {
                    jFrame.setUndecorated(true);
                    jFrame.setExtendedState(JFrame.MAXIMIZED_BOTH);
                }
            }
            jFrame.setVisible(true);
        }
    }

    public static String getWindowTitle() {
        return jFrame.getTitle();
    }
    public static void setWindowTitle(String title) {
        jFrame.setTitle(title);
    }
}
