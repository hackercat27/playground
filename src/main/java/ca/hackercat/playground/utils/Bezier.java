package ca.hackercat.playground.utils;

public interface Bezier {
    double getX(double t);
    double getY(double t);
    void setX(double x, int i);
    void setY(double y, int i);
}
