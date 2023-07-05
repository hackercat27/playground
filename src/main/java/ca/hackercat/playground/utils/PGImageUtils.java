package ca.hackercat.playground.utils;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;

/**
 * A utility class with methods that can manipulate a given {@code BufferedImage}.
 */
public final class PGImageUtils {
    private PGImageUtils() {}
    
    public static BufferedImage multiply(BufferedImage texture1, BufferedImage texture2) {
        return texture1;
    }
    public static BufferedImage multiply(BufferedImage texture, Color color) {

        float aMul = color.getAlpha() / 255f;
        float rMul = color.getRed() / 255f;
        float gMul = color.getGreen() / 255f;
        float bMul = color.getBlue() / 255f;
        
        if (aMul == 1 && rMul == 1 && gMul == 1 && bMul == 1) {
            return texture;
        }
        
        BufferedImage newTexture = new BufferedImage(texture.getWidth(), texture.getHeight(), BufferedImage.TYPE_INT_ARGB);
        
        
        int x = 0;
        int y = 0;
        while (y < texture.getHeight()) {
            
            int rgb = texture.getRGB(x, y);
            
            int alpha = (rgb & 0xFF000000) >> 24;
            int red   = (rgb & 0x00FF0000) >> 16;
            int green = (rgb & 0x0000FF00) >> 8;
            int blue  = (rgb & 0x000000FF);
            
            alpha = Math.round(alpha * aMul);
            red   = Math.round(red * rMul);
            green = Math.round(green * gMul);
            blue  = Math.round(blue * bMul);
            
            rgb = (alpha << 24) + (red << 16) + (green << 8) + (blue);
            newTexture.setRGB(x, y, rgb);
            
            x++;
            if (x >= texture.getWidth()) {
                x = 0;
                y++;
            }
        }
        return newTexture;
    }
    private static int extractRGB(byte[] pixels, int x, int y, int width, int height, boolean hasAlphaChannel) {
        int pixelLength = hasAlphaChannel? 4 : 3;
        
        int pos = (y * pixelLength * width) + (x * pixelLength);
        int argb = 0xff000000;
        if (hasAlphaChannel) {
            argb = (((int) pixels[pos++] & 0xff) << 24);
        }
        argb |= ((int) pixels[pos++] & 0xff);
        argb |= ((int) pixels[pos++] & 0xff) << 8;
        argb |= ((int) pixels[pos] & 0xff) << 16;
        return argb;
    }
    
    public static BufferedImage add(BufferedImage texture, Color color) {
        int rAdd = color.getRed();
        int gAdd = color.getGreen();
        int bAdd = color.getBlue();
        
        BufferedImage newTexture = new BufferedImage(texture.getWidth(), texture.getHeight(), texture.getType());
        
        int x = 0;
        int y = 0;
        while (y < texture.getHeight()) {
            
            int rgb = texture.getRGB(x, y);
            
            int alpha = 255;
            int red   = (rgb & 0x00FF0000) >> 16;
            int green = (rgb & 0x0000FF00) >> 8;
            int blue  = (rgb & 0x000000FF);
            
            red += rAdd;
            green += gAdd;
            blue += bAdd;
            
            if (red > 255) red = 255;
            if (green > 255) green = 255;
            if (blue > 255) blue = 255;
            
            rgb = (alpha << 24) + (red << 16) + (green << 8) + (blue);
            newTexture.setRGB(x, y, rgb);
            
            x++;
            if (x >= texture.getWidth()) {
                x = 0;
                y++;
            }
        }
        return newTexture;
    }
    
    public static BufferedImage reflectX(BufferedImage original) {
        BufferedImage texture = new BufferedImage(original.getWidth(), original.getHeight(), BufferedImage.TYPE_INT_ARGB);
        
        Graphics2D g2 = texture.createGraphics();
        g2.drawImage(original, original.getWidth(), 0, -original.getWidth(), original.getHeight(), null);
        return texture;
    }
    public static BufferedImage reflectY(BufferedImage original) {
        BufferedImage texture = new BufferedImage(original.getWidth(), original.getHeight(), BufferedImage.TYPE_INT_ARGB);
        
        Graphics2D g2 = texture.createGraphics();
        g2.drawImage(original, 0, original.getHeight(), original.getWidth(), -original.getHeight(), null);
        return texture;
    }
}
