package ca.hackercat.playground.object;

public abstract class Actor extends Sprite {
    protected double gravityMagnitude = 1;
    protected double gravityTheta = Math.PI / 2;
    
    protected double vX, vY;

    protected double gravityX;
    protected double gravityY;
    protected boolean useGravityCenter;

    protected double velocityCoefficient = 1;

    @Override
    protected void update() {
        if (useGravityCenter) {
            gravityTheta = Math.atan2(x - gravityX, -y + gravityY) + Math.PI / 2;
        }
        double gravityX = Math.cos(gravityTheta) * gravityMagnitude;
        double gravityY = Math.sin(gravityTheta) * gravityMagnitude;
        vX += gravityX;
        vY += gravityY;
        vX *= velocityCoefficient;
        vY *= velocityCoefficient;
        x += vX;
        y += vY;
    }
}
