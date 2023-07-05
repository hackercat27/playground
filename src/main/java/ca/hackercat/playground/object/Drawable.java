package ca.hackercat.playground.object;

import java.awt.*;

/**
 * Defines a HUD element: ie, something that requires a more complex
 * drawing routine rather than a simple texture.
 */
public abstract class Drawable {
    private boolean visible = true;
    protected boolean isHudElement = false;
    protected Rectangle collisionBox = new Rectangle(0, 0);

    protected float order = 0;

    public void updateSuper() {
        update();
    }
    protected abstract void update();
    public void render(Graphics2D g2, double t) {
        if (!visible) return;
        draw(g2);
    }
    protected abstract void draw(Graphics2D g2);

    public boolean isHudElement() {
        return isHudElement;
    }
    public Rectangle getCollisionBox() {
        return collisionBox;
    }

    public float getOrder() {
        return order;
    }
}
