package com.fornothing.snowmansam.entities;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.fornothing.snowmansam.MainClass;
import com.fornothing.snowmansam.utilities.CollisionRect;

public final class Spike {

    private static final int INITIAL_SPEED = 20;
    private static final int ACCELERATION = 175;
    private static final int GOAL_SPEED = 400;
    public static final int WIDTH = 24;
    public static final int HEIGHT = 60;
    public static int spikeCount = 0;
    private static Texture spikeTexture;
    private float x, y;
    private int speed;
    private int goalspeed;

    private CollisionRect rect;
    public boolean remove = false;

    public Spike(float x) {
        this.x = x;
        y = MainClass.V_HEIGHT;
        speed = INITIAL_SPEED;
        goalspeed = GOAL_SPEED;

        spikeTexture = new Texture("images/Ice_Spike_1.png");

        this.rect = new CollisionRect(
                x + spikeTexture.getWidth() /2,
                y + spikeTexture.getHeight() * 0.2f,
                WIDTH - spikeTexture.getWidth() /2,
                HEIGHT - spikeTexture.getHeight() /2);
    }

    public void update (float deltaTime) {
        //Speed adjustment to reach goal
        if (speed < goalspeed) {
            speed += ACCELERATION * deltaTime;
            if (speed > goalspeed)
                speed = goalspeed;
        }
        speed += ACCELERATION * deltaTime;
        y -= speed * deltaTime;
        if (y < -MainClass.V_HEIGHT) {
            remove = true;
        }
        rect.move(x, y);
    }

    public void render (SpriteBatch batch) {
        batch.draw(spikeTexture, x, y, WIDTH, HEIGHT);
    }

    public CollisionRect getCollisionRect () {
        return rect;
    }
    public static int getSpikeCount() { return spikeCount; }
    public float getX () {
        return x;
    }
    public float getY() {
        return y;
    }

}
