package ca.hackercat.playground.object;

import ca.hackercat.playground.JFrameManager;
import ca.hackercat.playground.PlaygroundPanel;
import ca.hackercat.playground.io.PGMouseListener;
import ca.hackercat.playground.io.PlaygroundIO;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.lang.reflect.Field;

import static ca.hackercat.playground.PlaygroundPanel.LOGGER;

public class PGMenu extends Drawable {

    private int selectedIndex;

    private Color overlayColor = new Color(0x40000000, true);
    private Color deselectedFontColor = new Color(0xFFAAAAAA, true);
    private Color selectedFontColor = new Color(0xFFFFFFFF, true);

    private static BufferedImage checkOn = PlaygroundIO.loadImage("/textures/ui/check_box_on.png"), checkOff = PlaygroundIO.loadImage("/textures/ui/check_box_off.png");

    private static class Submenu {
        String title;
        Option[] options;
        public Submenu(String title, Option... options) {
            this.title = title;
            this.options = options;
        }
    }
    private static class Option {
        String name;
        public Option(String name) {
            this.name = name;
        }
    }
    private static abstract class Entry {
        String label;
        public Entry(String label) {
            this.label = label;
        }
        abstract void enter();
        Rectangle2D bounds;
    }

    private Entry[] entries = new Entry[] {
            new Entry("Resume") {
                @Override
                void enter() {
                    pgp.setState(PlaygroundPanel.PanelState.NORMAL);
                }
            },
            new Entry("Preferences") {
                @Override
                void enter() {
                    inOptionsDialog = true;
                }
            },
            new Entry("Exit") {
                @Override
                void enter() {
                    pgp.exit();
                }
            }
    };

    private static class MenuInputManager extends InputManager {
        int getCursorX() {
            return (int) ml.getX();
        }
        int getCursorY() {
            return (int) ml.getY();
        }
        boolean forwardButtonPressed() {
            return ml.isButtonPressed(PGMouseListener.BUTTON_LEFT);
        }
        boolean backButtonPressed() {
            return kl.isKeyPressed(KeyEvent.VK_ESCAPE) /*|| cl.isButtonPressed(PGXInputListener.Button.B)*/;
        }
    }
    private MenuInputManager im = new MenuInputManager();

    private PlaygroundPanel pgp;
    private Object preferences;
    public PGMenu(PlaygroundPanel pgp) {
        this.pgp = pgp;
    }

    private boolean inOptionsDialog = false;

    @Override
    protected void update() {
        Rectangle cursorRect = new Rectangle(im.getCursorX(), im.getCursorY(), 1, 1);

        isHudElement = true;

        if (!inOptionsDialog) {
            selectedIndex = -1;
            for (int i = 0; i < entries.length; i++) {
                if (entries[i].bounds == null) continue;
                if (cursorRect.intersects(entries[i].bounds)) {
                    selectedIndex = i;
                    break;
                }
            }
        }

        if (im.forwardButtonPressed() && selectedIndex >= 0 && selectedIndex < entries.length) {
            entries[selectedIndex].enter();
        }

        if (im.backButtonPressed()) {
            if (inOptionsDialog) {
                inOptionsDialog = false;
            }
            else {
                pgp.setState(PlaygroundPanel.PanelState.NORMAL);
            }
        }

    }

    private enum PreferenceType {
        BOOLEAN
    }

    @Override
    protected void draw(Graphics2D g2) {
        g2.setColor(overlayColor);
        g2.fillRect((int) -pgp.getWindowXOffset(), (int) -pgp.getWindowYOffset(), (int) (pgp.getInternalWidth() + (pgp.getWindowXOffset() * 2)), (int) (pgp.getInternalHeight() + (pgp.getWindowYOffset() * 2)));

        if (inOptionsDialog) {
            Font normalFont = g2.getFont();

            g2.setFont(g2.getFont().deriveFont(g2.getFont().getSize2D() * 2));
            g2.setColor(selectedFontColor);
            int x = 16;
            int y = pgp.getInternalHeight() / 2;

            g2.drawString(entries[1].label, x, y);

            g2.setFont(normalFont);

            if (preferences != null) {
                Field[] fields = preferences.getClass().getFields();

                double maxX = 0;

                for (int i = 0; i < fields.length; i++) {
                    String label = fields[i].getName();
                    String type = fields[i].getType().toString();
                    double strWidth = g2.getFont().getStringBounds(label, g2.getFontRenderContext()).getWidth();

                    maxX = Math.max(maxX, strWidth);
                }

                maxX += g2.getFont().getSize2D();

                int width = (int) maxX;
                for (int i = 0; i < fields.length; i++) {
                    try {
                        String label = fields[i].getAnnotation(Setting.class).properName();
                        String type = fields[i].getType().toString();
                        String value = fields[i].get(preferences).toString();

                        if (!(type.equals(boolean.class.toString()))) {
                            label = "Unsupported type '" + type + "'";
                        }


                        if (type.equals(boolean.class.toString())) {
//                            double strWidth = g2.getFont().getStringBounds(label, g2.getFontRenderContext()).getWidth();
                            g2.drawImage(
                                    Boolean.parseBoolean(value)? checkOn : checkOff,
                                    (x + width),
                                    y + (i * g2.getFont().getSize()),
                                    g2.getFont().getSize(),
                                    g2.getFont().getSize(),
                                    null
                            );
                        }

                        g2.drawString(label, x, y + ((i + 1) * g2.getFont().getSize()));
                    } catch (IllegalAccessException e) {
                        throw new RuntimeException(e);
                    }

                }
            }
        }
        else {
            Font normalFont = g2.getFont();

            g2.setFont(g2.getFont().deriveFont(g2.getFont().getSize2D() * 2));
            g2.setColor(selectedFontColor);
            int x = 16;
            int y = pgp.getInternalHeight() / 2;

            g2.drawString(pgp.getApplicationTitle(), x, y);

            g2.setFont(normalFont);
            for (int i = 0; i < entries.length; i++) {
                if (i == selectedIndex) {
                    g2.setColor(selectedFontColor);
                }
                else {
                    g2.setColor(deselectedFontColor);
                }
                g2.drawString(entries[i].label, x, y + ((i + 1) * g2.getFont().getSize()));
                entries[i].bounds = g2.getFont().getStringBounds(entries[i].label, g2.getFontRenderContext());
                entries[i].bounds.setRect(x, y + (i * g2.getFont().getSize()), entries[i].bounds.getWidth(), entries[i].bounds.getHeight());
            }
        }
//        Rectangle cursorRect = new Rectangle(im.getCursorX(), im.getCursorY(), 1, 1);
//        g2.draw(cursorRect);
//        for (int i = 0; i < entries.length; i++) {
//            if (entries[i].bounds == null) continue;
//            g2.draw(entries[i].bounds);
//        }
    }

    public void setPreferences(Object preferences) {
        this.preferences = preferences;
    }
}
