package ca.hackercat.playground.utils;

import ca.hackercat.logging.Logger;

public class BezierSpline {

    private static final Logger LOGGER = Logger.get(BezierSpline.class);

    Bezier[] beziers;

    public BezierSpline(Bezier... beziers) {
        this.beziers = beziers;
    }

    public double getX(double t) {
        int i = (int) (t) % beziers.length;
        double f = t % 1;
        return beziers[i].getX(f);
    }
    public double getY(double t) {
        int i = (int) (t) % beziers.length;
        double f = t % 1;
        return beziers[i].getY(f);
    }

    public void connect() {
        connect(0);
    }
    public void connect(int continuityLevel) {
        if (continuityLevel >= 0) {
            for (int i = 1; i < beziers.length; i++) {
                double x = beziers[i - 1].getX(1);
                beziers[i].setX(x, 0);
                double y = beziers[i - 1].getY(1);
                beziers[i].setY(y, 0);

//                if (continuityLevel >= 1) {
//                    double x = beziers[i - 1].getX(1);
//                    beziers[i].setX(x, 0);
//                    double y = beziers[i - 1].getY(1);
//                    beziers[i].setY(y, 0);
//                }
            }
        }
    }

}
