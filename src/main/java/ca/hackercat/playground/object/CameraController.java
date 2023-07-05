package ca.hackercat.playground.object;

import ca.hackercat.playground.PlaygroundPanel;

public class CameraController extends Sprite {

    private double zoom = 1;
    private Sprite target;
    private PlaygroundPanel pgp;

//    PGMouseListener ml = PGMouseListener.get();
    
    public CameraController(PlaygroundPanel pgp) {
        visible = false;
        this.pgp = pgp;
    }

    @Override
    protected void update() {
//        zoom = 1d / ((ml.getScrollAmount() / 16d) + 1);
        
//        zoom = Math.pow(0.9, Math.hypot(((Actor) target).vX, ((Actor) target).vY));
        
        if (target != null) {
            
            double margin = 16;
            double easing = 4;
    
            double textureWidth = 0;
            double textureHeight = 0;
            
            if (target.texture != null) {
                textureWidth = target.texture.getWidth() * target.getTextureScale();
                textureHeight = target.texture.getHeight() * target.getTextureScale();
            }
            
            double xOff = (pgp.getInternalWidth() / 2d) - (textureWidth / 2d);
            double yOff = (pgp.getInternalHeight() / 2d) - (textureHeight / 2d);
            
            if (target.x - margin > x + xOff) {
                x += Math.max((target.x - margin) - (x + xOff), 0) / easing;
            }
            else if (target.x < x + xOff - margin) {
                x -= Math.max((x + xOff) - target.x - margin, 0) / easing;
            }
            if (target.y - margin > y + yOff) {
                y += Math.max((target.y - margin) - (y + yOff), 0) / easing;
            }
            else if (target.y < y + yOff - margin) {
                y -= Math.max((y + yOff) - target.y - margin, 0) / easing;
            }
        }
    }

    public void setTarget(Sprite target) {
        this.target = target;
    }
    
    public double getZoom() {
        return zoom;
    }
    public void setZoom(double zoom) {
        this.zoom = Math.abs(zoom);
    }
}
