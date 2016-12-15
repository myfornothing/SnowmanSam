package com.fornothing.snowmansam.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.fornothing.snowmansam.MainClass;
import com.fornothing.snowmansam.entities.Spike;
import com.fornothing.snowmansam.utilities.CollisionRect;
import com.fornothing.snowmansam.utilities.ExplodeIce;

import java.util.ArrayList;
import java.util.Random;

import static com.fornothing.snowmansam.MainClass.V_WIDTH;
import static com.fornothing.snowmansam.entities.Spike.getSpikeCount;
import static com.fornothing.snowmansam.entities.Spike.spikeCount;


public class GameScreen implements Screen {
    private MainClass main;

    private static final float SPEED = 350;
    private static final float HEALTH_DROP_RATE = 0.1f;  // 1= FULL
    private static final float ANIMATION_SPEED = 0.5f;
    private static final int SNOWMAN_PXL_WIDTH = 32;
    private static final int SNOWMAN_PXL_HEIGHT = 32;
    private static final int SNOWMAN_WIDTH = SNOWMAN_PXL_WIDTH *3;
    private static final int SNOWMAN_HEIGHT = SNOWMAN_PXL_HEIGHT *3;
    private static final float TURN_TIMER_SWITCH_TIME = 0.15f;
    private static float MIN_SPIKE_SPAWN_TIME = 0f;
    private static float MAX_SPIKE_SPAWN_TIME = 1.2f;
    public static int SPIKE_INCREASE_NUMBER = 15;

    private Texture imageFloor;
    private float floorImageScale;

    private Texture healthBlank;
    private float health = 1;

    private Animation[] turns;
    private float x;
    private float y;
    private int turn;
    private float turnTimer;
    private float stateTime;
    private float spikeSpawnTimer;
    private CollisionRect playerRect;

    private ArrayList<Spike> spikes;
    private ArrayList<ExplodeIce> explosionsIce;
    private Random random;

//    private static GameCamera gameCamera;
    private Vector2 displacement;
    private float strenght = 15;


    public GameScreen(MainClass main) {
        this.main = main;
        main.scrollingBackground.setSpeedFixed(false);
        turn = 2;
        turnTimer = 0;
        turns = new Animation[5];

        // Starting position
        x = MainClass.V_WIDTH /2 - SNOWMAN_WIDTH /2;
        y = MainClass.V_HEIGHT * 0.2f - SNOWMAN_HEIGHT /2;

        playerRect = new CollisionRect(x + SNOWMAN_WIDTH /3, y,
                SNOWMAN_WIDTH - SNOWMAN_WIDTH /3, SNOWMAN_HEIGHT);
    }

    @Override
    public void show() {
        TextureRegion[] snowmanSamSheet = TextureRegion.split(new Texture("animations/SnowmanSamSheet2.png"),
                SNOWMAN_PXL_WIDTH, SNOWMAN_PXL_HEIGHT)[0];

        turns[0] = new Animation(ANIMATION_SPEED, snowmanSamSheet[0]); //turn left
        turns[1] = new Animation(ANIMATION_SPEED, snowmanSamSheet[1]);
        turns[2] = new Animation(ANIMATION_SPEED, snowmanSamSheet[2]); //front
        turns[3] = new Animation(ANIMATION_SPEED, snowmanSamSheet[3]);
        turns[4] = new Animation(ANIMATION_SPEED, snowmanSamSheet[4]); //turn right

        random = new Random();

        spikes = new ArrayList<Spike>();
        spikeSpawnTimer = random.nextFloat() *
                (MAX_SPIKE_SPAWN_TIME - MIN_SPIKE_SPAWN_TIME) + MIN_SPIKE_SPAWN_TIME;

        explosionsIce = new ArrayList<ExplodeIce>();

        imageFloor = new Texture("images/iceblockFloor.png");
        floorImageScale = imageFloor.getWidth() / (MainClass.V_WIDTH /2);

        healthBlank = new Texture("images/health_blank.png");
    }

    @Override  //Update and Render
    public void render(float delta) {
        MainClass.handleInput(delta);

        displacement = new Vector2(x +25, y +25);

        //Spikes spawn code
        spikeSpawnTimer -= delta;
        if (spikeSpawnTimer <= 0) {
            spikeSpawnTimer = random.nextFloat() *
                    (MAX_SPIKE_SPAWN_TIME - MIN_SPIKE_SPAWN_TIME) + MIN_SPIKE_SPAWN_TIME;
            spikes.add(new Spike(random.nextInt(V_WIDTH) - (Spike.WIDTH /2)));
        }

        //Update spikes
        ArrayList<Spike> spikesToRemove = new ArrayList<Spike>();
        for (Spike spike : spikes) {
            //SPIKE SPAWN SPEED
            if (getSpikeCount() > SPIKE_INCREASE_NUMBER) {
                spikeCount = 0;
                SPIKE_INCREASE_NUMBER += 2;
                setMaxSpikeSpawnTime(getMaxSpikeSpawnTime() - 0.1f);
                if (getMaxSpikeSpawnTime() <= 0.2f) {
                    setMaxSpikeSpawnTime(0.2f);
                }
            }
            spike.update(delta);
            if (spike.remove)
                spikesToRemove.add(spike);
        }

        //Update Ice explosions
        ArrayList<ExplodeIce> explosionsToRemove = new ArrayList<ExplodeIce>();
        for (ExplodeIce explodeIce : explosionsIce) {
            explodeIce.update(delta);
            if (explodeIce.remove)
                explosionsToRemove.add(explodeIce);
        }
        explosionsIce.removeAll(explosionsToRemove);

        //Movement code
        if (isLeft()) { //Left pressed
            x -= SPEED * Gdx.graphics.getDeltaTime();
            if (x < 0)
                x = 0;
            //Update turn Left
            if (isJustLeft() && !isRight() && turn > 0) {
                turnTimer = 0;
                turn--;
            }
            //Turn update
            turnTimer -= Gdx.graphics.getDeltaTime();
            if (Math.abs(turnTimer) > TURN_TIMER_SWITCH_TIME && turn > 0) {
                turnTimer -= TURN_TIMER_SWITCH_TIME;
                turn--;
            }
        } else {
            if (turn < 2) {
                //Turn back to center
                turnTimer += Gdx.graphics.getDeltaTime();
                if (Math.abs(turnTimer) > TURN_TIMER_SWITCH_TIME && turn < 4) {
                    turnTimer -= TURN_TIMER_SWITCH_TIME;
                    turn++;
                }
            }
        }

        if (isRight()) { //Right pressed
            x += SPEED * Gdx.graphics.getDeltaTime();
            if (x + SNOWMAN_WIDTH > MainClass.V_WIDTH)
                x = MainClass.V_WIDTH - SNOWMAN_WIDTH;
            //Update if Right clicked
            if (isJustRight() && !isLeft() && turn > 0) {
                turnTimer = 0;
                turn++;
            }
            //Update turn right
            turnTimer += Gdx.graphics.getDeltaTime();
            if (Math.abs(turnTimer) > TURN_TIMER_SWITCH_TIME && turn < 4) {
                turnTimer -= TURN_TIMER_SWITCH_TIME;
                turn++;
            }
        } else {
            if (turn > 2) {
                //Turn update
                turnTimer -= Gdx.graphics.getDeltaTime();
                if (Math.abs(turnTimer) > TURN_TIMER_SWITCH_TIME && turn > 0) {
                    turnTimer -= TURN_TIMER_SWITCH_TIME;
                    turn--;
                }
            }
        }

        playerRect.move(x, y);

        for (Spike spike : spikes) {
            if (spike.getCollisionRect().collidesWith(playerRect)) {
                explosionsIce.add(new ExplodeIce(spike.getX(), spike.getY()));
                spikesToRemove.add(spike);
                spikeCount++;
                health -= HEALTH_DROP_RATE;

                if (health <= 0) {
                    this.dispose();
                    main.setScreen(new GameOverScreen(main));  //Go to GameOverScreen
                }
            }

            if (spike.getY() <= (y + imageFloor.getHeight() /3.5f)) {
                explosionsIce.add(new ExplodeIce(spike.getX(), spike.getY()));
                spikesToRemove.add(spike);
                spikeCount ++;
            }
        }

        spikes.removeAll(spikesToRemove);

        stateTime += delta;

        Gdx.gl.glClearColor(.2f, .2f, .2f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        main.batch.begin();
        //draw background
        main.scrollingBackground.updateAndRender(delta, main.batch);
        //draw spikes
        for (Spike spike : spikes) { spike.render(main.batch); }
        //draw explosions
        for (ExplodeIce explodeIce : explosionsIce) {
            explodeIce.render(main.batch);
        }
        //draw snowman
        main.batch.draw(turns[turn].getKeyFrame(stateTime, true), x, y,
                SNOWMAN_WIDTH, SNOWMAN_HEIGHT);
        //draw floor
        main.batch.draw(imageFloor, 0,  y - (imageFloor.getHeight() ),
                (MainClass.V_WIDTH /2) * floorImageScale,
                (imageFloor.getHeight() /2) * floorImageScale);
        //draw health
        if (health > 0.5f)
            main.batch.setColor(0, 1, 0, 0.5f);
        else if (health > 0.2f)
            main.batch.setColor(0.8f, 0.4f, 0, 0.7f);
        else
            main.batch.setColor(1, 0, 0, 0.9f);

        main.batch.draw(healthBlank, 0,
                MainClass.V_HEIGHT * 0.99f,
                MainClass.V_WIDTH * 0.99f * health,
                MainClass.V_HEIGHT * 0.01f);
        main.batch.setColor(Color.WHITE);
        main.batch.end();
    }

    private boolean isRight () {
        return Gdx.input.isKeyPressed(Input.Keys.RIGHT) || (Gdx.input.isTouched()
                && main.gameCamera.getInputInGameWorld().x >= V_WIDTH /2);
    }

    private boolean isLeft () {
        return Gdx.input.isKeyPressed(Input.Keys.LEFT) || (Gdx.input.isTouched()
                && main.gameCamera.getInputInGameWorld().x < V_WIDTH / 2);
    }

    private boolean isJustRight () {
        return Gdx.input.isKeyJustPressed(Input.Keys.RIGHT) || (Gdx.input.justTouched()
                && main.gameCamera.getInputInGameWorld().x >= V_WIDTH / 2);
    }

    private boolean isJustLeft () {
        return Gdx.input.isKeyJustPressed(Input.Keys.LEFT) || (Gdx.input.justTouched()
                && main.gameCamera.getInputInGameWorld().x < V_WIDTH / 2);
    }

    public static void setMaxSpikeSpawnTime(float maxSpikeSpawnTime) {
        MAX_SPIKE_SPAWN_TIME = maxSpikeSpawnTime;
    }

    public static float getMaxSpikeSpawnTime() {
        return MAX_SPIKE_SPAWN_TIME;
    }

    @Override
    public void resize(int width, int height) {  }
    @Override
    public void pause() {  }
    @Override
    public void resume() {  }
    @Override
    public void hide() {  }
    @Override
    public void dispose() {  }

}
