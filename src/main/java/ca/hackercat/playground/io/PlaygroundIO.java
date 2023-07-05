package ca.hackercat.playground.io;

import javax.imageio.ImageIO;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.util.Scanner;

import static ca.hackercat.playground.PlaygroundPanel.LOGGER;

/**
 * A utility class with IO related helper methods.
 */
public final class PlaygroundIO {
    private PlaygroundIO() {}

    /**
     * The default "missing texture" texture.
     */
    private static final BufferedImage MISSING_TEXTURE = new BufferedImage(64, 64, BufferedImage.TYPE_INT_ARGB);
    static {
        BufferedImage buf = new BufferedImage(2, 2, BufferedImage.TYPE_INT_ARGB);
        buf.setRGB(0, 0, 0xFFFF00FF);
        buf.setRGB(1, 1, 0xFFFF00FF);
        buf.setRGB(0, 1, 0xFF800080);
        buf.setRGB(1, 0, 0xFF800080);
        
        Graphics2D g2 = MISSING_TEXTURE.createGraphics();
        g2.drawImage(buf, 0, 0, MISSING_TEXTURE.getWidth(), MISSING_TEXTURE.getHeight(), null);
        g2.dispose();
    }

    /**
     * Returns the "missing texture" placeholder texture (magenta/black checkerboard pattern),
     * that is intended to be used when no texture exists or was found.
     * @return A placeholder texture.
     */
    public static BufferedImage getMissingTexture() {
        return MISSING_TEXTURE;
    }

    /**
     *
     * @param path The path to the image
     * @return The image specified at the given path, or {@code MISSING_TEXTURE} if none was found
     */
    public static BufferedImage loadImage(String path) {
        if (path.charAt(0) == '/') {
            return loadResourceImage(path);
        }
        return loadRuntimeImage(path);
    }
    private static BufferedImage loadResourceImage(String path) {
        try (InputStream is = PlaygroundIO.class.getResourceAsStream(path)) {
            assert is != null;
            return ImageIO.read(is);
        }
        catch (NullPointerException | IOException | IllegalArgumentException e) {
            LOGGER.warn(path + " == null!");
            return getMissingTexture();
        }
    }
    private static BufferedImage loadRuntimeImage(String path) {
        try {
            File file = new File(path);
            return ImageIO.read(file);
        }
        catch (IOException e) {
            LOGGER.warn(path + " == null!");
            return getMissingTexture();
        }
    }
    
    public static String getTextContents(String path) {
        if (isResourcePath(path)) {
            return getResourceTextContents(path);
        }
        return getRuntimeTextContents(path);
    }
    private static String getResourceTextContents(String path) {
        StringBuilder str = new StringBuilder();
        try (InputStream is = PlaygroundIO.class.getResourceAsStream(path)) {
            if (is == null) {
                LOGGER.warn(path + " == null!");
                return "";
            }
            Scanner scan = new Scanner(is);
            while (scan.hasNextLine()) {
                str.append(scan.nextLine()).append('\n');
            }
        }
        catch (IOException | IllegalArgumentException e) {
            throw new RuntimeException(e);
        }
        return str.toString();
    }
    private static String getRuntimeTextContents(String path) {
        StringBuilder str = new StringBuilder();
        try {
            File file = new File(path);
            Scanner scan = new Scanner(file);
            while (scan.hasNextLine()) {
                str.append(scan.nextLine()).append('\n');
            }
        }
        catch (IOException | IllegalArgumentException e) {
            throw new RuntimeException(e);
        }
        return str.toString();
    }
    
//    public static <T> T getObjectFromJson(String path, Class<T> classOfT) {
//        String str = getTextContents(path);
//        Gson gson = new Gson();
//        T object = gson.fromJson(str, classOfT);
//
//        return Primitives.wrap(classOfT).cast(object);
//    }
//
//    public static void writeObjectToJson(Object o, String path) {
//        Gson gson = new Gson();
//        String json = gson.toJson(o);
//        write(json, path);
//    }
    
    public static InputStream getInputStream(String path) {
        if (isResourcePath(path)) {
            return getResourceInputStream(path);
        }
        return getRuntimeInputStream(path);
    }
    
    private static InputStream getResourceInputStream(String path) {
        return PlaygroundIO.class.getResourceAsStream(path);
    }
    private static InputStream getRuntimeInputStream(String path) {
        try {
            return new FileInputStream(path);
        }
        catch (FileNotFoundException e) {
            return null;
        }
    }
    
    /**
     *
     * @param path the path to check
     * @return {@code true} if the path points to a resource,
     * {@code false} if the path points to a runtime file location
     */
    public static boolean isResourcePath(String path) {
        return path.charAt(0) == '/';
    }

    public static void write(String str, String path) {
        try {
            File file = new File(path);
            FileWriter fw = new FileWriter(file);
            fw.write(str);
            fw.close();
            LOGGER.log("Wrote to file '" + path + "'");
        } catch (IOException e) {
            LOGGER.error(e);
        }
    }
    public static void write(BufferedImage image, String path) {
        try {
            File file = new File(path);
            ImageIO.write(image, "png", file);
            LOGGER.log("Wrote to file '" + path + "'");
        } catch (IOException e) {
            LOGGER.error(e);
        }
    }
}
