package ca.hackercat.playground.utils;

import ca.hackercat.logging.Logger;
import ca.hackercat.playground.io.PlaygroundIO;

import java.awt.*;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

public final class PGFontManager {

    private static final Logger LOGGER = Logger.get(PGFontManager.class);
    private PGFontManager() {}

    private static class FontContainer {
        Font font;
        String name;
        public FontContainer() {}
        public FontContainer(Font font, String name) {
            this.font = font;
            this.name = name;
        }
    }

    private static final ArrayList<FontContainer> fonts = new ArrayList<>(0);
    
    private static final Font defaultFont;
    private static final float defaultSize = 12f;
    
    static {
        installFont("Courier", "/font/cour.ttf");
        installFont("JetBrains Mono", "/font/JetBrainsMono-Regular.ttf");

        defaultFont = new Font("", Font.PLAIN, 12);
    }
    
    public static void installFont(String name, String path) {
        InputStream fontStream = PlaygroundIO.getInputStream(path);
        if (fontStream == null) {
            LOGGER.error("Font not found at '" + path + "', fontStream == null");
            return;
        }

        try {
            Font font = Font.createFont(Font.TRUETYPE_FONT, fontStream);
            FontContainer container = new FontContainer(font, name);
            fonts.add(container);
            LOGGER.log("Sucessfully installed font '" + name + "'");
        } catch (FontFormatException | IOException e) {
            LOGGER.error("Attempted to install invalid font at path '" + path + "'");
        }

    }

    public static Font getFont(String name) {
        return getFont(name, defaultSize);
    }
    public static Font getFont(String name, float size) {
        for (FontContainer font : fonts) {
            if (font.name.equals(name)) {
                return font.font.deriveFont(size);
            }
        }
        return defaultFont.deriveFont(size);
    }

    public static String[] getInstalledFonts() {
        String[] names = new String[fonts.size()];
        for (int i = 0; i < names.length; i++) {
            names[i] = fonts.get(i).name;
        }
        return names;
    }
    
}
