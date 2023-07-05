package ca.hackercat.playground.utils;

public class CubicBezier implements Bezier {

    private double x1, x2, x3, x4;
    private double y1, y2, y3, y4;


    public CubicBezier(double x1, double y1, double x2, double y2, double x3, double y3, double x4, double y4) {
        this.x1 = x1;
        this.y1 = y1;
        this.x2 = x2;
        this.y2 = y2;
        this.x3 = x3;
        this.y3 = y3;
        this.x4 = x4;
        this.y4 = y4;
    }

    @Override
    public double getX(double t) {
        double x01 = PGMath.lerp(x1, x2, t);
        double x02 = PGMath.lerp(x2, x3, t);
        double x03 = PGMath.lerp(x3, x4, t);

        double x11 = PGMath.lerp(x01, x02, t);
        double x12 = PGMath.lerp(x02, x03, t);

        return PGMath.lerp(x11, x12, t);
    }
    @Override
    public double getY(double t) {
        double y01 = PGMath.lerp(y1, y2, t);
        double y02 = PGMath.lerp(y2, y3, t);
        double y03 = PGMath.lerp(y3, y4, t);

        double y11 = PGMath.lerp(y01, y02, t);
        double y12 = PGMath.lerp(y02, y03, t);

        return PGMath.lerp(y11, y12, t);
    }

    @Override
    public void setX(double x, int i) {
        switch (i) {
            case 0 -> x1 = x;
            case 1 -> x2 = x;
            case 2 -> x3 = x;
            case 3 -> x4 = x;
        }
    }

    @Override
    public void setY(double y, int i) {
        switch (i) {
            case 0 -> y1 = y;
            case 1 -> y2 = y;
            case 2 -> y3 = y;
            case 3 -> y4 = y;
        }
    }

}
