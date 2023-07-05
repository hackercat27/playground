package ca.hackercat.playground.object;


import ca.hackercat.playground.io.PlaygroundIO;
import ca.hackercat.playground.utils.PGMath;

import java.awt.*;
import java.awt.image.BufferedImage;

public abstract class Sprite extends Drawable {
    
    protected double x, y;
    protected double theta;
    protected double parallax = 1;
    private double textureScale = 1;
    private double lastX, lastY;
    private double lastTheta;
    private double lastParallax = 1;
    protected BufferedImage texture;
    protected boolean visible = true;
    protected AnimationController animationController;

    public void updateSuper() {
        theta = PGMath.unsignedMod(theta, Math.PI * 2);
        lastX = x;
        lastY = y;
        lastTheta = theta;
        update();
    }
    protected abstract void update();

    @Override
    public void render(Graphics2D g2, double t) {
        if (!visible) return;
        if (animationController != null) {
            animationController.updateAnimations();
        }
        if (texture == null) {
            texture = PlaygroundIO.getMissingTexture();
        }
        Graphics2D newG2 = (Graphics2D) g2.create();

        double x = PGMath.lerp(lastX, this.x, t);
        double y = PGMath.lerp(lastY, this.y, t);
        double rotation = PGMath.cyclicalLerp(lastTheta, this.theta, t, 0, Math.PI * 2);

        newG2.translate(x, y);
        newG2.rotate(rotation, (texture.getWidth() * textureScale) / 2d, (texture.getHeight() * textureScale) / 2d);
        draw(newG2);

        newG2.dispose();
    }
    protected void draw(Graphics2D g2) {
        g2.drawImage(texture, 0, 0, (int) (texture.getWidth() * textureScale), (int) (texture.getHeight() * textureScale), null);
    }

    public double getX() {
        return x;
    }
    public double getY() {
        return y;
    }
    public void setX(double x) {
        this.x = x;
    }
    public void setY(double y) {
        this.y = y;
    }
    public double getTheta() {
        return theta;
    }
    public void setTheta(double theta) {
        this.theta = theta;
    }
    
    public void setTexture(BufferedImage texture) {
        this.texture = texture;
    }
    public void setTextureScale(double textureScale) {
        this.textureScale = textureScale;
    }
    protected double getTextureScale() {
        return textureScale;
    }

}
