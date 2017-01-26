package com.fornothing.snowmansam.entities;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.fornothing.snowmansam.MainClass;

public final class ScrollingBackground {

    public static final int DEFAULT_SPEED_1 = 102;  //100
    public static final int DEFAULT_SPEED_2 = 80;  //82
    private static final int ACCELERATION = 58;     //58
    private static final int GOAL_SPEED_1 = 260;    //230
    private static final int GOAL_SPEED_2 = 230;    //200

    private Texture imageSnowflakes_1, imageSnowflakes_2;
    private float y1, y11, y2, y22;
    private int speed_1, speed_2;
    private int goalSpeed_1,goalSpeed_2;
    private float imageScale;

    private boolean speedFixed = true;

    public ScrollingBackground () {
        imageSnowflakes_1 = new Texture("animations/snowflakes_falling_big.png");
        imageSnowflakes_2 = new Texture("animations/snowflakes_falling_small.png");
        imageScale = imageSnowflakes_1.getWidth() / MainClass.V_WIDTH;

        y1 = -MainClass.V_HEIGHT;
        y11 = -MainClass.V_HEIGHT;

        y2 = imageSnowflakes_1.getHeight() * imageScale;
        y22 = imageSnowflakes_2.getHeight() * imageScale;

        speed_1 = DEFAULT_SPEED_1;
        speed_2 = DEFAULT_SPEED_2;

        goalSpeed_1 = GOAL_SPEED_1;
        goalSpeed_2 = GOAL_SPEED_2;

    }

    public void updateAndRender (float deltaTime, SpriteBatch batch) {
        if (!speedFixed) { //if not fixed speed; increase speed
            speed_1 += ACCELERATION * deltaTime;
            speed_2 += ACCELERATION * deltaTime;
            //Speed adjustment to reach goal
            if (speed_1 < goalSpeed_1) {
                speed_1 += ACCELERATION * deltaTime;
                if (speed_1 > goalSpeed_1) {
                    speed_1 = goalSpeed_1;
                }
            }
            if (speed_2 < goalSpeed_2) {
                speed_2 += ACCELERATION * deltaTime;
                if (speed_2 > goalSpeed_2) {
                    speed_2 = goalSpeed_2;
                }
            }
        }

        // scroll down
        y1 -= speed_1 * deltaTime;
        y2 -= speed_1 * deltaTime;
        y11 -= speed_2 * deltaTime;
        y22 -= speed_2 * deltaTime;

        // reposition snowflakes
        if (y1 + imageSnowflakes_1.getHeight() * imageScale <= 0)
            y1 = y2 + imageSnowflakes_1.getHeight() * imageScale;

        if (y2 + imageSnowflakes_1.getHeight() * imageScale <= 0)
            y2 = y1 + imageSnowflakes_1.getHeight() * imageScale;

        if (y11 + imageSnowflakes_2.getHeight() * imageScale <= 0)
            y11 = y22 + imageSnowflakes_2.getHeight() * imageScale;

        if (y22 + imageSnowflakes_2.getHeight() * imageScale <= 0)
            y22 = y11 + imageSnowflakes_2.getHeight() * imageScale;

        //Render
        batch.draw(imageSnowflakes_1, 0, y1, MainClass.V_WIDTH,
                imageSnowflakes_1.getHeight() * imageScale);
        batch.draw(imageSnowflakes_1, 0, y2, MainClass.V_WIDTH,
                imageSnowflakes_1.getHeight() * imageScale);

        batch.draw(imageSnowflakes_2, 0, y11, MainClass.V_WIDTH,
                imageSnowflakes_2.getHeight() * imageScale);
        batch.draw(imageSnowflakes_2, 0, y22, MainClass.V_WIDTH,
                imageSnowflakes_2.getHeight() * imageScale);
    }

    public void resize (int width, int height) {
        imageScale = width / imageSnowflakes_1.getWidth();
    }

    public void setSpeed_1(int speed_1) {
        this.speed_1 = speed_1;
    }
    public void setSpeed_2(int speed_2) {
        this.speed_2 = speed_2;
    }
    public void setSpeedFixed (boolean speedFixed) {
        this.speedFixed = speedFixed;
    }
}
