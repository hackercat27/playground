package ca.hackercat.playground.utils;

import java.awt.*;

/**
 * Utility class with useful math related functions that aren't offered in the native Java library.
 */
public final class PGMath {
    private PGMath() {}

    /**
     * A linear interpolation algorithm.
     *
     * @param t The parameter to the lerp function. Must be {@code <= 1} and {@code >= 0}.
     * @return The value between a1 and a2, specified by a
     * linear interpolation algorithm with the parameter {@code t}.
     */
    public static double lerp(double a1, double a2, double t) {
//        if (t > 1 || t < 0) throw new IllegalArgumentException("t must be >= 0 and <= 1!");
        
        t = Math.max(Math.min(t, 1), 0);
        double val = a2 - a1;
        double a = val * t;
        return a + a1;
    }

    public static double cyclicalLerp(double a1, double a2, double t, double lowerBound, double upperBound) {
        double range = Math.abs(lowerBound - upperBound);
        double a3;
        if (a2 < a1) {
            a3 = a2 + range;
        }
        else {
            a3 = a2 - range;
        }
        double dist12 = Math.abs(a2 - a1);
        double dist13 = Math.abs(a3 - a1);
        if (dist12 < dist13) {
            return lerp(a1, a2, t);
        }
        return lerp(a1, a3, t);
    }
    
    public static int rgbSpectrum(double a) {
        double rA = a;
        double gA = a + (1d / 3);
        double bA = a + (2d / 3);
    
        double r = rgbFuncHelper(rA);
        double g = rgbFuncHelper(gA);
        double b = rgbFuncHelper(bA);
        
        return 0xFF000000 | ((int) (r * 255) << 16) | ((int) (g * 255) << 8) | ((int) (b * 255));
    }
    public static Color colorSpectrum(double a) {
        double rA = a;
        double gA = a + (1d / 3);
        double bA = a + (2d / 3);
        
        double r = rgbFuncHelper(rA);
        double g = rgbFuncHelper(gA);
        double b = rgbFuncHelper(bA);
        
        return new Color((float) r, (float) g, (float) b);
    }
    
    private static double rgbFuncHelper(double a) {
        double x = a % 1;
        x = (x + 1) % 1;
    
        double y;
    
        if (x < 0.25) {
            y = 4 * x;
        }
        else if (x < 0.5) {
            y = 1;
        }
        else if (x < 0.75) {
            y = 3 - (4 * x);
        }
        else {
            y = 0;
        }
        return y;
    }

    // the idea behind this is to implement the function myself later,
    // but for now i'm too lazy to learn how PRNGS work so yeah
    public static double random() {
        return Math.random();
    }

    public static int unsignedMod(int value, int mod) {
        while (value < 0) {
            value += mod;
        }
        return value % mod;
    }
    public static double unsignedMod(double value, double mod) {
        while (value < 0) {
            value += mod;
        }
        return value % mod;
    }

    public static double ease(double value, double targetValue, double easingCoefficient) {
        double v = Math.abs(value - targetValue) * easingCoefficient;
        if (value - targetValue < 0) {
            return value + v;
        }
        if (value - targetValue > 0) {
            return value - v;
        }
        return value;
    }
    public static double cyclicalEase(double value, double targetValue, double easingCoefficient) {
        return cyclicalEase(value, targetValue, easingCoefficient, 0, Math.PI * 2);
    }
    public static double cyclicalEase(double value, double targetValue, double easingCoefficient, double lowerBound, double upperBound) {
        double range = Math.abs(lowerBound - upperBound);
        double targetValueAlt;
        if (targetValue < value) {
            targetValueAlt = targetValue + range;
        }
        else {
            targetValueAlt = targetValue - range;
        }
        double dist12 = Math.abs(targetValue - value);
        double dist13 = Math.abs(targetValueAlt - value);
        if (dist12 < dist13) {
            return ease(value, targetValue, easingCoefficient);
        }
        return ease(value, targetValueAlt, easingCoefficient);
    }

    public static double average(double... values) {
        return sum(values) / values.length;
    }
    public static double sum(double... values) {
        double sum = 0;
        for (double value : values) {
            sum += value;
        }
        return sum;
    }
}
