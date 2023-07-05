package ca.hackercat.playground.object;

import ca.hackercat.playground.io.PlaygroundIO;

import java.awt.image.BufferedImage;

import static ca.hackercat.playground.PlaygroundPanel.LOGGER;

public class AnimationController {
    
    private Sprite parent;
    
    public AnimationController(Sprite parent, Animation... animations) {
        if (animations.length == 0) LOGGER.fatal("No animations supplied to AnimationController!");
        this.animations = animations;
        this.parent = parent;
    }
    
    Animation[] animations;
    int animationID;
    int animationIndex;
    
    long lastTimeMillis = System.currentTimeMillis();
    
    public static class Animation {
        public String name;

        public Animation(String name, int frameTimeMillis, double textureScale, BufferedImage... textures) {
            this.name = name;
            this.textureScale = textureScale;
            this.textures = textures;
            this.frameTimeMillis = frameTimeMillis;
        }
        public Animation(String name, int frameTimeMillis, double textureScale, String... texturePaths) {
            this.name = name;
            BufferedImage[] textures = new BufferedImage[texturePaths.length];
            for (int i = 0; i < textures.length; i++) {
                textures[i] = PlaygroundIO.loadImage(texturePaths[i]);
            }
            this.textureScale = textureScale;
            this.textures = textures;
            this.frameTimeMillis = frameTimeMillis;
        }
        protected BufferedImage[] textures;
        protected double textureScale;
        protected int frameTimeMillis;
    }
    
    public void updateAnimations() {
        if (animationID >= animations.length) setAnimation(0);
        Animation animation = animations[animationID];
        
        if (System.currentTimeMillis() >= lastTimeMillis + animation.frameTimeMillis) {
            lastTimeMillis = System.currentTimeMillis();
            animationIndex++;
        }
        
        parent.setTexture(
                animation.textures[animationIndex % animation.textures.length]
        );
        parent.setTextureScale(animation.textureScale);
    }
    
    public void setAnimation(int animationID) {
        if (this.animationID != animationID) {
            this.animationID = animationID;
            animationIndex = 0;
        }
    }
    public void setAnimation(String animationName) {
        for (int i = 0; i < animations.length; i++) {
            if (animations[i].name.equals(animationName)) {
                setAnimation(i);
                break;
            }
        }
    }
    
}
