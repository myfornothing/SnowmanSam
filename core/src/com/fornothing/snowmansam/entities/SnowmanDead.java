package com.fornothing.snowmansam.entities;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public final class SnowmanDead {

    private static final float FRAME_LENGTH = 0.15f;
    private static final int IMAGE_WIDTH = 120;
    private static final int IMAGE_HEIGHT = 96;

    private static Animation animation = null;
    private float x, y;
    private float statetime;

    public boolean remove = false;
    public static boolean isDead = false;

    public SnowmanDead(float x, float y) {
        this.x = x ;
        this.y = y ;
        statetime = 0;

        animation = new Animation(FRAME_LENGTH,
                TextureRegion.split(new Texture("animations/SnowmanDead2.png"),
                        IMAGE_WIDTH, IMAGE_HEIGHT)[0]);
    }

    public  void update (float deltaTime) {
        statetime += deltaTime;
        if (animation.isAnimationFinished(statetime)){
            remove = true;
            isDead = true;
        }
    }

    public void render (SpriteBatch batch) {
        batch.draw(animation.getKeyFrame(statetime), x, y, IMAGE_WIDTH, IMAGE_HEIGHT);
    }

}
