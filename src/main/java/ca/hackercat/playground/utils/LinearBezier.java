package ca.hackercat.playground.utils;

public class LinearBezier implements Bezier {

    private double x1, y1, x2, y2;

    public LinearBezier(double x1, double y1, double x2, double y2) {
        this.x1 = x1;
        this.y1 = y1;
        this.x2 = x2;
        this.y2 = y2;
    }

    @Override
    public double getX(double t) {
        return PGMath.lerp(x1, x2, t);
    }

    @Override
    public double getY(double t) {
        return PGMath.lerp(y1, y2, t);
    }

    @Override
    public void setX(double x, int i) {
        switch (i) {
            case 0 -> x1 = x;
            case 1 -> x2 = x;
        }
    }

    @Override
    public void setY(double y, int i) {
        switch (i) {
            case 0 -> y1 = y;
            case 1 -> y2 = y;
        }
    }
}
