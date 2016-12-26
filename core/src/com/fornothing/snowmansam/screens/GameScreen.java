package com.fornothing.snowmansam.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.fornothing.snowmansam.MainClass;
import com.fornothing.snowmansam.entities.ExplodeIce;
import com.fornothing.snowmansam.entities.SnowballRotate;
import com.fornothing.snowmansam.entities.SnowmanHit;
import com.fornothing.snowmansam.entities.Spike;
import com.fornothing.snowmansam.utilities.CollisionRect;
import com.fornothing.snowmansam.utilities.GameCamera;
import com.fornothing.snowmansam.utilities.Hud;
import com.fornothing.snowmansam.utilities.PlayerMovement;

import java.util.ArrayList;
import java.util.Random;

import static com.fornothing.snowmansam.MainClass.V_HEIGHT;
import static com.fornothing.snowmansam.MainClass.V_WIDTH;
import static com.fornothing.snowmansam.MainClass.gameCamera;
import static com.fornothing.snowmansam.entities.SnowmanHit.getGotHit;
import static com.fornothing.snowmansam.entities.Spike.getSpikeCount;
import static com.fornothing.snowmansam.entities.Spike.spikeCount;


public class GameScreen implements Screen {
    private MainClass main;

    public static float PLAYER_SPEED = 350;
    private static final float HEALTH_DROP_RATE = 0.2f;  // 1= FULL
    private static final float ANIMATION_SPEED = 0.5f;
    private static final int SNOWMAN_PXL_WIDTH = 96;
    private static final int SNOWMAN_PXL_HEIGHT = 96;
    public static final int SNOWMAN_WIDTH = SNOWMAN_PXL_WIDTH ;
    public static final int SNOWMAN_HEIGHT = SNOWMAN_PXL_HEIGHT ;
    private static float MIN_SPIKE_SPAWN_TIME = 0f;
    public static float MAX_SPIKE_SPAWN_TIME_DEFAULT = 1f;
    public static float MAX_SPIKE_SPAWN_TIME = MAX_SPIKE_SPAWN_TIME_DEFAULT;

    public static int SPIKE_INCREASE_NUMBER = 15;
    private static float SPIKE_SPAWN_TIMER;
    private static Random RANDOM;
    public static int TURN;
    private float stateTime;

    private CollisionRect playerRect;
    public static float x, y;

    private Texture imageFloor;
    private float floorImageScale;

    private Texture healthBlank;
    public static float health = 1;

    private Animation[] turns;
    private ArrayList<Spike> spikes;
    private ArrayList<ExplodeIce> explosionsIce;
    private ArrayList<SnowmanHit> snowmanHit;
    private ArrayList<SnowballRotate> snowballs;

    private float defaultYPosition = V_HEIGHT * 0.2f - SNOWMAN_HEIGHT /2;
    private float offsetYPosition = -V_HEIGHT - SNOWMAN_HEIGHT;

    private Hud hud;
    public static int FINAL_PLAY_SCORE;

    public GameScreen(MainClass main) {
        this.main = main;
        main.gameCamera = new GameCamera(V_WIDTH, V_HEIGHT);
        main.scrollingBackground.setSpeedFixed(false);
        TURN = 2;
        turns = new Animation[5];

        // Starting position
        x = V_WIDTH /2 - SNOWMAN_WIDTH /2;
        y = defaultYPosition;

        playerRect = new CollisionRect(x + SNOWMAN_WIDTH /3, y,
                SNOWMAN_WIDTH - SNOWMAN_WIDTH /3, SNOWMAN_HEIGHT);
    }

    @Override
    public void show() {
        TextureRegion[] snowmanSamSheet = TextureRegion.split
                (new Texture("animations/SnowmanSheet_Scarf.png"),
                SNOWMAN_PXL_WIDTH, SNOWMAN_PXL_HEIGHT)[0];

        turns[0] = new Animation(ANIMATION_SPEED, snowmanSamSheet[0]); //TURN left
        turns[1] = new Animation(ANIMATION_SPEED, snowmanSamSheet[1]);
        turns[2] = new Animation(ANIMATION_SPEED, snowmanSamSheet[2]); //front
        turns[3] = new Animation(ANIMATION_SPEED, snowmanSamSheet[3]);
        turns[4] = new Animation(ANIMATION_SPEED, snowmanSamSheet[4]); //TURN right

        RANDOM = new Random();

        spikes = new ArrayList<Spike>();
        SPIKE_SPAWN_TIMER = RANDOM.nextFloat() *
                (MAX_SPIKE_SPAWN_TIME - MIN_SPIKE_SPAWN_TIME) + MIN_SPIKE_SPAWN_TIME;

        explosionsIce = new ArrayList<ExplodeIce>();
        snowmanHit = new ArrayList<SnowmanHit>();
        snowballs = new ArrayList<SnowballRotate>();

        imageFloor = new Texture("images/iceblockFloor.png");
        floorImageScale = imageFloor.getWidth() / (V_WIDTH /2);

        healthBlank = new Texture("images/health_blank.png");

        hud = new Hud(main.batch);
    }

    @Override  //Update and Render
    public void render(float delta) {

        PlayerMovement.handleInput(delta);
        hud.update(delta);

        //Spikes spawn code
        SPIKE_SPAWN_TIMER -= delta;
        if (SPIKE_SPAWN_TIMER <= 0) {
            SPIKE_SPAWN_TIMER = RANDOM.nextFloat() *
                    (MAX_SPIKE_SPAWN_TIME - MIN_SPIKE_SPAWN_TIME) + MIN_SPIKE_SPAWN_TIME;
            spikes.add(new Spike(RANDOM.nextInt(V_WIDTH) - (Spike.WIDTH /2)));
        }
        //Snowball spawn code
        SnowballRotate.SNOWBALL_SPAWN_TIMER -= delta;
        if (SnowballRotate.SNOWBALL_SPAWN_TIMER <= 0) {
            SnowballRotate.SNOWBALL_SPAWN_TIMER = RANDOM.nextFloat() *
                    (SnowballRotate.MAX_SNOWBALL_SPAWN_TIME - SnowballRotate.MIN_SNOWBALL_SPAWN_TIME) +
                    SnowballRotate.MIN_SNOWBALL_SPAWN_TIME;
            snowballs.add(new SnowballRotate(RANDOM.nextInt(
                    V_WIDTH - SnowballRotate.IMAGE_SIZE), V_HEIGHT));
        }

        //Update spikes
        ArrayList<Spike> spikesToRemove = new ArrayList<Spike>();
        for (Spike spike : spikes) {
            //SPIKE SPAWN PLAYER_SPEED
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

        //Update snowballs
        ArrayList<SnowballRotate> snowballsToRemove = new ArrayList<SnowballRotate>();
        for (SnowballRotate snowballsRotate : snowballs) {
            snowballsRotate.update(delta);
            if (snowballsRotate.remove)
                snowballsToRemove.add(snowballsRotate);
        }

        //Update Ice explosions
        ArrayList<ExplodeIce> explosionsToRemove = new ArrayList<ExplodeIce>();
        for (ExplodeIce explodeIce : explosionsIce) {
            explodeIce.update(delta);
            if (explodeIce.remove)
                explosionsToRemove.add(explodeIce);
        }

        //Update Snowman Hit
        ArrayList<SnowmanHit> snowmanHitsToRemove = new ArrayList<SnowmanHit>();
        for (SnowmanHit snowmanHits : snowmanHit) {
            snowmanHits.update(delta);
            if (snowmanHits.remove)
                snowmanHitsToRemove.add(snowmanHits);
        }

        //Player collision rect position update
        switch (getGotHit()) { //1=default, 2=HIT
            case 1: playerRect.move(x, defaultYPosition);
                break;
            case 2: playerRect.move(x, offsetYPosition);
                break;
        }

        //Player to spike collision detection
        for (Spike spike : spikes) {
            if (spike.getCollisionRect().collidesWith(playerRect)) {
                explosionsIce.add(new ExplodeIce(spike.getX(), spike.getY()));
                spikesToRemove.add(spike);
                spikeCount++;
                health -= HEALTH_DROP_RATE;
                snowmanHit.add(new SnowmanHit(x, y));
                SnowmanHit.setGotHit(2);
                PLAYER_SPEED = 0;
            //If PLAYER DEAD
                if (health <= 0) {
                    this.dispose();
                    main.setScreen(new GameOverScreen(main));  //Go to GameOverScreen
                }
            }
            //Spike to floor collision
            if (spike.getY() <= (y + imageFloor.getHeight() /3.5f)) {
                explosionsIce.add(new ExplodeIce(spike.getX(), spike.getY()));
                spikesToRemove.add(spike);
                spikeCount ++;
            }
        }

        //Snowball collision, HEALTH INCREASE
        for (SnowballRotate snowballsRotate : snowballs) {
            if (snowballsRotate.getCollisionRect().collidesWith(playerRect)) {
                snowballsToRemove.add(snowballsRotate);
                Hud.addScore(1);
                FINAL_PLAY_SCORE += 1;
                if (health >= 1){
                    health = 1;
                }else
                health += 0.1f;
            }
        }

        spikes.removeAll(spikesToRemove);
        explosionsIce.removeAll(explosionsToRemove);
        snowmanHit.removeAll(snowmanHitsToRemove);
        snowballs.removeAll(snowballsToRemove);

        stateTime += delta;

        Gdx.gl.glClearColor(.2f, .2f, .2f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        main.batch.setProjectionMatrix(gameCamera.combined());
        main.batch.begin();
        //draw background
        main.scrollingBackground.updateAndRender(delta, main.batch);
        //draw spikes
        for (Spike spike : spikes) { spike.render(main.batch); }
        //draw explosions
        for (ExplodeIce explodeIce : explosionsIce) {
            explodeIce.render(main.batch);
        }
        //draw snowballs
        for (SnowballRotate snowballsRotate : snowballs) {
            snowballsRotate.render(main.batch);
        }
        //draw snowman playing and hit reposition
        switch (getGotHit()) { //1=default, 2=HIT
            case 1: main.batch.draw(turns[TURN].getKeyFrame(stateTime, true),
                    x, defaultYPosition, SNOWMAN_WIDTH, SNOWMAN_HEIGHT);
                break;
            case 2: main.batch.draw(turns[TURN].getKeyFrame(stateTime, true),
                    x, offsetYPosition, SNOWMAN_WIDTH, SNOWMAN_HEIGHT);
                break;
        }
        for (SnowmanHit snowmanHits : snowmanHit) {
            snowmanHits.render(main.batch);
        }

        //draw floor
        main.batch.draw(imageFloor, 0,  y - (imageFloor.getHeight() ),
                (V_WIDTH /2) * floorImageScale,
                (imageFloor.getHeight() /2) * floorImageScale);

        //health color change
        if (health > 0.5f)
            main.batch.setColor(0, 1, 0, 0.5f);
        else if (health > 0.2f)
            main.batch.setColor(0.8f, 0.4f, 0, 0.7f);
        else
            main.batch.setColor(1, 0, 0, 0.9f);
        //draw health
        main.batch.draw(healthBlank, 0,
                V_HEIGHT * 0.965f,
                V_WIDTH * 0.99f * health,
                V_HEIGHT * 0.03f);
        main.batch.setColor(Color.WHITE);

        main.batch.end();
        hud.hudStage.draw();
    }

    private static float getMaxSpikeSpawnTime() {
        return MAX_SPIKE_SPAWN_TIME;
    }
    private static void setMaxSpikeSpawnTime(float maxSpikeSpawnTime) {
        MAX_SPIKE_SPAWN_TIME = maxSpikeSpawnTime;
    }
    public static int getFinalPlayScore() {
        return FINAL_PLAY_SCORE;
    }
    public static void resetFinalPlayScore(){
        FINAL_PLAY_SCORE = 0;
    }

    @Override
    public void resize(int width, int height) {  }
    @Override
    public void pause() { System.out.println("GameScreen pause..."); }
    @Override
    public void resume() { System.out.println("GameScreen resume..."); }
    @Override
    public void hide() { System.out.println("GameScreen hide..."); }
    @Override
    public void dispose() { System.out.println("GameScreen dispose..."); }

}
