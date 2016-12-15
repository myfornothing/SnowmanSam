package com.fornothing.snowmansam.utilities;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public final class ExplodeIce {

    private static final float FRAME_LENGTH = 0.1f;
    private static final int OFFSET = 40;
    private static final int SIZE = 128;
    private static final int IMAGE_SIZE = 64;

    private static Animation animation = null;
    private float x, y;
    private float statetime;

    public boolean remove = false;

    public ExplodeIce(float x, float y) {
        this.x = x ;
        this.y = y - OFFSET;
        statetime = 0;

        if (animation == null)
            animation = new Animation(FRAME_LENGTH,
                    TextureRegion.split(new Texture("animations/explode_spike2.png"),
                            IMAGE_SIZE, IMAGE_SIZE)[0]);
    }

    public void update (float deltaTime) {
        statetime += deltaTime;
        if (animation.isAnimationFinished(statetime))
            remove = true;
    }

    public void render (SpriteBatch batch) {
        batch.draw(animation.getKeyFrame(statetime), x, y, SIZE /2, SIZE /2);
    }
}
