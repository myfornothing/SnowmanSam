package com.fornothing.snowmansam.entities;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import static com.fornothing.snowmansam.screens.GameScreen.PLAYER_SPEED;

public final class SnowmanHit {

    private static final float FRAME_LENGTH = 0.15f;
    private static final int OFFSET = 40;
    private static final int SIZE = 96;
    private static final int IMAGE_SIZE = 96;

    private static Animation animation = null;
    private float x, y;
    private float statetime;

    public boolean remove = false;

    public static int getGotHit() { return gotHit; }
    public static void setGotHit(int gotHit) { SnowmanHit.gotHit = gotHit; }
    private static int gotHit = 1;  //1=no, 2=yes

    public SnowmanHit(float x, float y) {
        this.x = x ;
        this.y = y ;
        statetime = 0;

//        if (animation == null)
            animation = new Animation(FRAME_LENGTH,
                    TextureRegion.split(new Texture("animations/SnowmanHit_Scarf.png"),
                            IMAGE_SIZE, IMAGE_SIZE)[0]);
    }

    public void update (float deltaTime) {
        statetime += deltaTime;
        if (animation.isAnimationFinished(statetime)){
            remove = true;
            SnowmanHit.setGotHit(1);
            PLAYER_SPEED = 350;
        }
    }

    public void render (SpriteBatch batch) {
        batch.draw(animation.getKeyFrame(statetime), x, y, SIZE, SIZE);
    }
}
