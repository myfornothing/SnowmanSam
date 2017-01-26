package com.fornothing.snowmansam.entities;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.fornothing.snowmansam.MainClass;
import com.fornothing.snowmansam.utilities.CollisionRect;
import com.fornothing.snowmansam.utilities.PauseMenu;

public final class SnowflakeRotate {

    private static final int INITIAL_SPEED = 15;
    private static final int ACCELERATION = 250;
    private static final int GOAL_SPEED = 750;
    private int speed;
    private int goalspeed;

    private static final float FRAME_LENGTH = 0.1f;
    private static final int OFFSET = 40;
    public static int IMAGE_SIZE = 228;
    private static final int SCALE_SIZE = 64;

    public static float MIN_SNOWFLAKE_SPAWN_TIME = 6f;
    public static float MAX_SNOWFLAKE_SPAWN_TIME_DEFAULT = 8f;
    public static float MAX_SNOWFLAKE_SPAWN_TIME = MAX_SNOWFLAKE_SPAWN_TIME_DEFAULT;
    public static float SNOWFLAKE_SPAWN_TIMER;

    private static Animation animation = null;
    private float x, y;
    private float statetime;

    private CollisionRect rect;
    public boolean remove = false;

    public SnowflakeRotate(float x, float y) {
        this.x = x;
        this.y = y;
        statetime = 0;
        speed = INITIAL_SPEED;
        goalspeed = GOAL_SPEED;

        animation = new Animation(FRAME_LENGTH,
                TextureRegion.split(new Texture("animations/snowflake_rotating.png"),
                        IMAGE_SIZE, IMAGE_SIZE)[0]);

        this.rect = new CollisionRect(
                x + (IMAGE_SIZE * .2f),
                y  + (IMAGE_SIZE * .2f),
                SCALE_SIZE - (SCALE_SIZE /2),
                IMAGE_SIZE - (IMAGE_SIZE /2));
    }

    public void update (float deltaTime) {
        statetime += deltaTime;
        //Speed adjustment to reach goal
        if (speed < goalspeed) {
            switch (PauseMenu.getIsPaused()) {
                case 1: speed += ACCELERATION * deltaTime;
                    break;
                case 2: speed = 0;
                    break;
            }
            if (speed > goalspeed)
                speed = goalspeed;
        }
        switch (PauseMenu.getIsPaused()) {
            case 1:
                speed += ACCELERATION * deltaTime;
                break;
            case 2:
                speed = 0;
                break;
        }
        y -= speed * deltaTime;
        if (y < -MainClass.V_HEIGHT) {
            remove = true;
        }
        rect.move(x, y);
    }
    public void render (SpriteBatch batch) {
        batch.draw(animation.getKeyFrame(statetime, true), x, y, SCALE_SIZE, SCALE_SIZE);
    }

    public CollisionRect getCollisionRect () {
        return rect;
    }
    public float getX () {
        return x;
    }
    public float getY() {
        return y;
    }

}
