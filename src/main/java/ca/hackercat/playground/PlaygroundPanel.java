package ca.hackercat.playground;

import ca.hackercat.playground.io.Logger;
import ca.hackercat.playground.object.CameraController;
import ca.hackercat.playground.object.Drawable;
import ca.hackercat.playground.object.Manager;
import ca.hackercat.playground.object.PGMenu;
import ca.hackercat.playground.object.Sprite;
import ca.hackercat.playground.utils.PGFontManager;
import ca.hackercat.playground.utils.PGMath;
import javax.swing.*;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.ConcurrentModificationException;

/**
 * The main class that does all of the heavy lifting.
 * <p>
 * {@code PlaygroundPanel} is responsible for handling the gameloop and rendering all objects to the screen
 * (or to be more specific, to a {@code JPanel}).
 */
public class PlaygroundPanel extends JPanel {
    protected ArrayList<Drawable> drawables = new ArrayList<>(0);
    protected ArrayList<Manager> managers = new ArrayList<>(0);

    public static final Logger LOGGER = new Logger();
    
    private int internalWidth;
    private int internalHeight;
    
    public static double UNCAPPED_FPS = Integer.MAX_VALUE;
    
    private double targetFPS = 240;
    private double targetTPS = 30;
    private double currentFPS;
    private double currentTPS;
    private double windowScale = 1;
    private double windowXOffset, windowYOffset;
    private int globalCounter;

    private double cameraX;
    private double cameraY;
    private double cameraZoom = 1;
    private double cameraTheta;
    private double lastCameraX;
    private double lastCameraY;
    private double lastCameraZoom = 1;
    private double lastCameraTheta;
    private boolean showPerformance = true;
    
    private Object antialiasing = RenderingHints.VALUE_ANTIALIAS_ON;
    private Object textAntialiasing = RenderingHints.VALUE_TEXT_ANTIALIAS_ON;
    private Object interpolation = RenderingHints.VALUE_INTERPOLATION_BILINEAR;

    private Color frameBackground = Color.BLACK;
    
    private BufferedImage screenBuffer;
    private PGMenu menu = new PGMenu(this);
    private String applicationTitle = "";

    public enum PanelState {
        NORMAL,
        PAUSED
    }
    private PanelState state = PanelState.NORMAL;

    public PlaygroundPanel() {
        this(1f);
    }
    /**
     * Creates a 854x480 (480p, 16:9) instance (standard resolution)
     */
    public PlaygroundPanel(float windowScale) {
        this(854, 480, windowScale);
    }
    public PlaygroundPanel(int width, int height, float windowScale) {
        internalWidth = width;
        internalHeight = height;
        initSwingValues(windowScale);
        screenBuffer = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
    }
    
    private void initSwingValues(float windowScale) {
        setPreferredSize(new Dimension((int) (internalWidth * windowScale), (int) (internalHeight * windowScale)));
        setBackground(new Color(0xFFAAAAAA));
        setForeground(Color.WHITE);

        setFont(PGFontManager.getFont("Courier"));
        String lookAndFeelClassName = UIManager.getSystemLookAndFeelClassName();
        try {
            UIManager.setLookAndFeel(lookAndFeelClassName);
            LOGGER.log("Loaded look and feel '" + lookAndFeelClassName + "'");
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException |
                 UnsupportedLookAndFeelException e) {
            LOGGER.error("Could not load look and feel '" + lookAndFeelClassName + "'");
        }
    }

    private long lastTickTimeMillis = System.currentTimeMillis();
    public void start() {


        Runnable updater = new Runnable() {
            double[] values = new double[10];
            int index;
//            long lastTickTimeMillis = System.currentTimeMillis();
            @Override
            public void run() {
                double tickTime = 1000d / targetTPS;
                double nextUpdateTime = System.currentTimeMillis() + tickTime;

                Arrays.fill(values, targetTPS);

                currentTPS = targetTPS;
                while (true) {
                    tickTime = 1000d / targetTPS;
    
                    //update game logic
                    update();
                    globalCounter++;


                    index = (index + 1) % values.length;
                    values[index] = 1000d / (System.currentTimeMillis() - lastTickTimeMillis);
                    currentTPS = PGMath.average(values);
                    lastTickTimeMillis = System.currentTimeMillis();
                    
                    
                    try {
                        double remainingTime = nextUpdateTime - System.currentTimeMillis();
            
                        if (remainingTime < 0) {
                            remainingTime = 0;
                        }
            
                        Thread.sleep((long) remainingTime);
            
                        nextUpdateTime += tickTime;
                    }
                    catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        };
        Runnable renderer = new Runnable() {
            double[] values = new double[10];
            int index;
            long lastFrameTimeNanos = System.nanoTime();
            @Override
            public void run() {
                double frameTime = 1000d / targetFPS;
                double nextFrameTime = System.currentTimeMillis() + frameTime;

                currentFPS = targetFPS;
                while (true) {
                    frameTime = 1000d / targetFPS;

                    //update game logic
                    repaint();

                    index = (index + 1) % values.length;
                    values[index] = 1000000000d / (System.nanoTime() - lastFrameTimeNanos);

                    currentFPS = PGMath.average(values);
                    lastFrameTimeNanos = System.nanoTime();


                    try {
                        double remainingTime = nextFrameTime - System.currentTimeMillis();

                        if (remainingTime < 0) {
                            remainingTime = 0;
                        }

                        Thread.sleep((long) remainingTime);

                        nextFrameTime += frameTime;
                    }
                    catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        };
        Thread updateThread = new Thread(updater, "playground-update-thread");
        updateThread.start();
        Thread renderThread = new Thread(renderer, "playground-render-thread");
        renderThread.start();
        
    }
    
    private void update() {
        if (getState() == PanelState.NORMAL) {
            drawables.sort(new Comparator<Drawable>() {
                @Override
                public int compare(Drawable o1, Drawable o2) {
                    return Float.compare(o1.getOrder(), o2.getOrder());
                }
            });
            for (Drawable d : drawables) {
                d.updateSuper();
                if (d instanceof CameraController) {
                    lastCameraX = cameraX;
                    lastCameraY = cameraY;
                    lastCameraZoom = cameraZoom;
                    lastCameraTheta = cameraTheta;
                    cameraX = ((CameraController) d).getX();
                    cameraY = ((CameraController) d).getY();
                    cameraZoom = ((CameraController) d).getZoom();
                    cameraTheta = ((CameraController) d).getTheta();
                }
            }
            for (Manager m : managers) {
                m.update();
            }
        }
        else if (getState() == PanelState.PAUSED) {
            if (menu != null) {
                menu.updateSuper();
            }
        }
    }

    private void render(Graphics2D g2, AffineTransform cameraTransform, AffineTransform screenTransform) {
        for (int i = 0, drawablesSize = drawables.size(); i < drawablesSize; i++) {
            Drawable d = null;
            while (d == null) {
                try {
                    d = drawables.get(i);
                }
                catch (ConcurrentModificationException ignored) {}
            }

            if (d.isHudElement()) {
                g2.setTransform(screenTransform);
                Graphics2D graphics2D = (Graphics2D) g2.create();
                d.render(graphics2D, 0);
                graphics2D.dispose();
            } else {
                double targetTime = 1000 / getTPS();
                long elapsed = System.currentTimeMillis() - lastTickTimeMillis;

                double t = elapsed / targetTime;

                if (t < 0) t = 0;
                if (t > 1) t = 1;

                if (getState() == PanelState.PAUSED) t = 1;

                g2.setTransform(cameraTransform);

                Graphics2D graphics2D = (Graphics2D) g2.create();
                d.render(graphics2D, t);
                graphics2D.dispose();
            }
        }
        for (Manager m : managers) {
            ArrayList<Drawable> objs = m.objs;
            for (int i = 0, objsSize = objs.size(); i < objsSize; i++) {
                Drawable d = null;
                while (d == null) {
                    try {
                        d = objs.get(i);
                    }
                    catch (ConcurrentModificationException ignored) {}
                }
                if (d.isHudElement()) {
                    g2.setTransform(screenTransform);
                    Graphics2D graphics2D = (Graphics2D) g2.create();
                    d.render(graphics2D, 0);
                    graphics2D.dispose();
                } else {
                    double targetTime = 1000 / getTPS();
                    long elapsed = System.currentTimeMillis() - lastTickTimeMillis;

                    double t = elapsed / targetTime;

                    if (t < 0) t = 0;
                    if (t > 1) t = 1;

                    if (getState() == PanelState.PAUSED) t = 1;

                    g2.setTransform(cameraTransform);
                    Graphics2D graphics2D = (Graphics2D) g2.create();
                    d.render(graphics2D, t);
                    graphics2D.dispose();
                }
            }
        }
        if (menu != null && getState() == PanelState.PAUSED) {
            g2.setTransform(screenTransform);
            Graphics2D graphics2D = (Graphics2D) g2.create();
            menu.render(graphics2D, 0);
            graphics2D.dispose();
        }
    }
    
    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        
        AffineTransform transform = g2.getTransform();
        
        g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, interpolation);
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, antialiasing);
        g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, textAntialiasing);
        
        
        double xScale = (double) getWidth() / getInternalWidth();
        double yScale = (double) getHeight() / getInternalHeight();
        
        if (xScale >= yScale) {
            windowScale = yScale;
            windowXOffset = (getWidth() - (windowScale * getInternalWidth())) / 2;
            windowYOffset = 0;
        }
        else {
            windowScale = xScale;
            windowXOffset = 0;
            windowYOffset = (getHeight() - (windowScale * getInternalHeight())) / 2;
        }
        
        double targetTime = 1000 / getTPS();
        long elapsed = System.currentTimeMillis() - lastTickTimeMillis;
    
        double t = elapsed / targetTime;
    
        if (t < 0) t = 0;
        if (t > 1) t = 1;

        if (getState() == PanelState.PAUSED)
            t = 1;

        g2.setColor(getBackground().darker());
        g2.fillRect(0, 0, getWidth(), getHeight());
        
        g2.translate(windowXOffset, windowYOffset);
        g2.scale(windowScale, windowScale);
        AffineTransform screenTransform = g2.getTransform();
    
        double zoom = PGMath.lerp(lastCameraZoom, cameraZoom, t);
        double xTranslate = internalWidth / 2d;
        double yTranslate = internalHeight / 2d;
        g2.translate(xTranslate, yTranslate);
        g2.scale(zoom, zoom);
        g2.rotate(PGMath.lerp(lastCameraTheta, cameraTheta, t));
//        g2.translate(-xTranslate, -yTranslate);
        g2.translate(-PGMath.lerp(lastCameraX, cameraX, t), -PGMath.lerp(lastCameraY, cameraY, t));
        g2.setColor(getBackground());
        g2.fillRect(0, 0, getInternalWidth(), getInternalHeight());
        AffineTransform cameraTransform = g2.getTransform();
        render(g2, cameraTransform, screenTransform);

        if (showPerformance) {
            g2.setTransform(transform);
            g2.translate(windowXOffset / windowScale, 0);
            g2.setFont(getFont());
            g2.setColor(Color.WHITE);
            g2.drawString(String.format("%.1f", getFPS()) + "fps", (int) (-windowXOffset / windowScale), g.getFont().getSize());
            g2.drawString(String.format("%.1f", getTPS()) + "tps", (int) (-windowXOffset / windowScale), g.getFont().getSize() * 2);
        }
    }

    public void exit() {
        System.exit(0);
    }
    
    public int getInternalWidth() {
        return internalWidth;
    }
    public int getInternalHeight() {
        return internalHeight;
    }
    public double getScaledWidth() {
        return getWidth() / getWindowScale();
    }
    public double getScaledHeight() {
        return getHeight() / getWindowScale();
    }
    public double getTargetFPS() {
        return targetFPS;
    }
    public double getTargetTPS() {
        return targetTPS;
    }
    public double getFPS() {
        return currentFPS;
    }
    public double getTPS() {
        return currentTPS;
    }
    public double getWindowScale() {
        return windowScale;
    }
    public double getWindowXOffset() {
        return windowXOffset;
    }
    public double getWindowYOffset() {
        return windowYOffset;
    }
    
    public void setTargetFPS(double targetFPS) {
        this.targetFPS = targetFPS;
    }
    public void setTargetTPS(double targetTPS) {
        this.targetTPS = Math.max(1, targetTPS);
    }
    public Color getFrameBackground() {
        return frameBackground;
    }
    public void setFrameBackground(Color frameBackground) {
        this.frameBackground = frameBackground;
    }
    public void setAntialiasing(Object hint) {
        antialiasing = hint;
    }
    public void setInterpolation(Object hint) {
        interpolation = hint;
    }
    public void setTextAntialiasing(Object hint) {
        this.textAntialiasing = hint;
    }
    public int getGlobalCounter() {
        return globalCounter;
    }

    public Object getAntialiasing() {
        return antialiasing;
    }
    public Object getTextAntialiasing() {
        return textAntialiasing;
    }
    public Object getInterpolation() {
        return interpolation;
    }
    public void setPreferences(Object preferences) {
        menu.setPreferences(preferences);
    }
    public void setState(PanelState state) {
        this.state = state;
    }
    public PanelState getState() {
        return state;
    }

    public String getApplicationTitle() {
        return applicationTitle;
    }
    public void setApplicationTitle(String applicationTitle) {
        this.applicationTitle = applicationTitle;
    }

    public void add(Drawable d) {
        drawables.add(d);
    }
    public void remove(Sprite d) {
        drawables.remove(d);
    }
    public void add(Manager m) {
        managers.add(m);
    }
    public void remove(Manager m) {
        managers.remove(m);
    }

    public void setShowPerformance(boolean showPerformance) {
        this.showPerformance = showPerformance;
    }
}
