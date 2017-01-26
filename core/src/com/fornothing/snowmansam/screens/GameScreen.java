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
import com.fornothing.snowmansam.entities.ScrollingBackground;
import com.fornothing.snowmansam.entities.SnowballRotate;
import com.fornothing.snowmansam.entities.SnowflakeRotate;
import com.fornothing.snowmansam.entities.SnowmanDead;
import com.fornothing.snowmansam.entities.SnowmanHit;
import com.fornothing.snowmansam.entities.Spike;
import com.fornothing.snowmansam.utilities.CollisionRect;
import com.fornothing.snowmansam.utilities.GameCamera;
import com.fornothing.snowmansam.utilities.Hud;
import com.fornothing.snowmansam.utilities.PauseMenu;
import com.fornothing.snowmansam.utilities.PlayerMovement;

import java.util.ArrayList;
import java.util.Random;

import static com.fornothing.snowmansam.MainClass.V_HEIGHT;
import static com.fornothing.snowmansam.MainClass.V_WIDTH;
import static com.fornothing.snowmansam.MainClass.collectSound;
import static com.fornothing.snowmansam.MainClass.gameCamera;
import static com.fornothing.snowmansam.MainClass.hitSound;
import static com.fornothing.snowmansam.MainClass.hitSound_Dead;
import static com.fornothing.snowmansam.MainClass.musicLoop;
import static com.fornothing.snowmansam.MainClass.musicVolHigh;
import static com.fornothing.snowmansam.MainClass.musicVolLow;
import static com.fornothing.snowmansam.entities.SnowmanHit.getGotHit;
import static com.fornothing.snowmansam.entities.Spike.MAX_SPIKE_SPAWN_TIME;
import static com.fornothing.snowmansam.entities.Spike.MIN_SPIKE_SPAWN_TIME;
import static com.fornothing.snowmansam.entities.Spike.SPIKE_INCREASE_NUMBER;
import static com.fornothing.snowmansam.entities.Spike.SPIKE_SPAWN_TIMER;
import static com.fornothing.snowmansam.entities.Spike.getSpikeCount;
import static com.fornothing.snowmansam.entities.Spike.spikeCount;
import static com.fornothing.snowmansam.utilities.Hud.FINAL_PLAY_SCORE;

public class GameScreen implements Screen {
    private MainClass main;

    public static float PLAYER_SPEED = 350;
    private static final float HEALTH_DROP_RATE = 0.2f;  // 1= FULL
    private static final float ANIMATION_SPEED = 0.5f;
    private static final int SNOWMAN_PXL_WIDTH = 96;
    private static final int SNOWMAN_PXL_HEIGHT = 96;
    public static final int SNOWMAN_WIDTH = SNOWMAN_PXL_WIDTH ;
    public static final int SNOWMAN_HEIGHT = SNOWMAN_PXL_HEIGHT ;

    private static Random RANDOM;
    public static int TURN;
    private float stateTime;

    private CollisionRect playerRect;
    public static float x, y;

    private Texture imageFloor;
    private float floorImageScale;

    private Texture healthBlank;
    public static float health = 1;

    private static boolean showControls = true;
    private Texture controls, controlsInstructions;
    private static final float CTRL_WAIT_TIME = 0.3f;
    private float ctrlTimer = 0;

    private Animation[] turns;
    private ArrayList<Spike> spikes;
    private ArrayList<ExplodeIce> explosionsIce;
    private ArrayList<SnowmanHit> snowmanHit;
    private ArrayList<SnowballRotate> snowballs;
    private ArrayList<SnowflakeRotate> snowflakes;
    private ArrayList<SnowmanDead> snowmanDeath;

    private float defaultYPosition = V_HEIGHT * 0.2f - SNOWMAN_HEIGHT /2;
    private float offsetYPosition = -V_HEIGHT - SNOWMAN_HEIGHT;

    private Hud hud;
    private PauseMenu pauseMenu;


    public GameScreen(MainClass main) {
        this.main = main;
        gameCamera = new GameCamera(V_WIDTH, V_HEIGHT);

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

        TextureRegion[] snowmanSamSheet = TextureRegion.split(
                new Texture("animations/SnowmanSheet_Scarf.png"),
                SNOWMAN_PXL_WIDTH, SNOWMAN_PXL_HEIGHT)[0];

        turns[0] = new Animation(ANIMATION_SPEED, snowmanSamSheet[0]); //TURN left
        turns[1] = new Animation(ANIMATION_SPEED, snowmanSamSheet[1]);
        turns[2] = new Animation(ANIMATION_SPEED, snowmanSamSheet[2]); //front
        turns[3] = new Animation(ANIMATION_SPEED, snowmanSamSheet[3]);
        turns[4] = new Animation(ANIMATION_SPEED, snowmanSamSheet[4]); //TURN right

        spikes = new ArrayList<Spike>();
        explosionsIce = new ArrayList<ExplodeIce>();
        snowmanHit = new ArrayList<SnowmanHit>();
        snowballs = new ArrayList<SnowballRotate>();
        snowflakes = new ArrayList<SnowflakeRotate>();
        snowmanDeath = new ArrayList<SnowmanDead>();

        imageFloor = new Texture("images/iceblockFloor.png");
        floorImageScale = imageFloor.getWidth() / (V_WIDTH /2);

        healthBlank = new Texture("images/health_blank.png");
        controls = new Texture("ui/controls_2.png");
        controlsInstructions = new Texture("ui/controls_instructions3.png");

        hud = new Hud(main.batch);
        pauseMenu = new PauseMenu(main.batch);
        RANDOM = new Random();

        SPIKE_SPAWN_TIMER = RANDOM.nextFloat() *
                (MAX_SPIKE_SPAWN_TIME - MIN_SPIKE_SPAWN_TIME) + MIN_SPIKE_SPAWN_TIME;
    }

    @Override  //Update and Render
    public void render(float delta) {

        //Scrolling background ? FIXED SPEED
        if (showControls) {
            MainClass.scrollingBackground.setSpeedFixed(true);
            MainClass.scrollingBackground.setSpeed_1(ScrollingBackground.DEFAULT_SPEED_1);
            MainClass.scrollingBackground.setSpeed_2(ScrollingBackground.DEFAULT_SPEED_2);
            musicLoop.setVolume(musicVolLow);
            musicLoop.setLooping(true);
            if (MainClass.getmusicOnOff()) {
                musicLoop.play();
            }else{
                musicLoop.pause();
            }
        } else
            MainClass.scrollingBackground.setSpeedFixed(false);

        //CONTROLS show/hide
        ctrlTimer += delta;
        if (PlayerMovement.isRight() || PlayerMovement.isLeft() &&
                ctrlTimer >= CTRL_WAIT_TIME) {
            ctrlTimer = 0;
            showControls = false;
            musicLoop.setVolume(musicVolHigh);
        }

        PlayerMovement.handleInput(delta);
        hud.update(delta);
        pauseMenu.update(delta);

        //Spikes spawn code
        if (!PauseMenu.showPause) {   //PAUSE MENU
            SPIKE_SPAWN_TIMER -= delta;
            if (SPIKE_SPAWN_TIMER <= 0 && !showControls) {
                SPIKE_SPAWN_TIMER = RANDOM.nextFloat() *
                        (MAX_SPIKE_SPAWN_TIME - MIN_SPIKE_SPAWN_TIME) + MIN_SPIKE_SPAWN_TIME;
                spikes.add(new Spike(RANDOM.nextInt(V_WIDTH) - (Spike.WIDTH / 2)));
            }

        //Snowball spawn code
        if (showControls)
            SnowballRotate.SNOWBALL_SPAWN_TIMER = SnowballRotate.MAX_SNOWBALL_SPAWN_TIME_DEFAULT;
        SnowballRotate.SNOWBALL_SPAWN_TIMER -= delta;
        if (SnowballRotate.SNOWBALL_SPAWN_TIMER <= 0 && !showControls && !SnowmanDead.isDead) {
            SnowballRotate.SNOWBALL_SPAWN_TIMER = RANDOM.nextFloat() *
                    (SnowballRotate.MAX_SNOWBALL_SPAWN_TIME - SnowballRotate.MIN_SNOWBALL_SPAWN_TIME) +
                    SnowballRotate.MIN_SNOWBALL_SPAWN_TIME;
            snowballs.add(new SnowballRotate(
                    RANDOM.nextInt(V_WIDTH - SnowballRotate.IMAGE_SIZE), V_HEIGHT));
        }

        //Snowflake spawn code
        if (showControls)
            SnowflakeRotate.SNOWFLAKE_SPAWN_TIMER = SnowflakeRotate.MAX_SNOWFLAKE_SPAWN_TIME_DEFAULT;
        SnowflakeRotate.SNOWFLAKE_SPAWN_TIMER -= delta;
        if (SnowflakeRotate.SNOWFLAKE_SPAWN_TIMER <= 0 && !showControls && !SnowmanDead.isDead) {
            SnowflakeRotate.SNOWFLAKE_SPAWN_TIMER = RANDOM.nextFloat() *
                    (SnowflakeRotate.MAX_SNOWFLAKE_SPAWN_TIME - SnowflakeRotate.MIN_SNOWFLAKE_SPAWN_TIME) +
                    SnowflakeRotate.MIN_SNOWFLAKE_SPAWN_TIME;
            snowflakes.add(new SnowflakeRotate(
                    RANDOM.nextInt(V_WIDTH - SnowflakeRotate.IMAGE_SIZE), V_HEIGHT));
        }
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

        //Update snowflakes
        ArrayList<SnowflakeRotate> snowflakesToRemove = new ArrayList<SnowflakeRotate>();
        for (SnowflakeRotate snowflakesRotate : snowflakes) {
            snowflakesRotate.update(delta);
            if (snowflakesRotate.remove)
                snowflakesToRemove.add(snowflakesRotate);
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

        //Update Snowman Dead
        ArrayList<SnowmanDead> snowmanDeathsToRemove = new ArrayList<SnowmanDead>();
        for (SnowmanDead snowmanDeaths : snowmanDeath) {
            snowmanDeaths.update(delta);
            if (snowmanDeaths.remove)
                snowmanDeathsToRemove.add(snowmanDeaths);
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
                PLAYER_SPEED = 0;
                SnowmanHit.setGotHit(2);

                if (health >= 0) {
                    snowmanHit.add(new SnowmanHit(x, y));
                    if (MainClass.getsoundOnOff()) {
                        long idSnowmanHitSound = hitSound.play(musicVolHigh);
                        hitSound.setPitch(idSnowmanHitSound, 2f);
                    }
                    Gdx.input.vibrate(100);
                }else {
                    snowmanDeath.add(new SnowmanDead(x, y));
                    if (MainClass.getsoundOnOff()) {
                        long idSnowmanHitSound_Dead = hitSound_Dead.play(musicVolHigh);
                        hitSound_Dead.setPitch(idSnowmanHitSound_Dead, 1f);
                    }
                    Gdx.input.vibrate(1000);
                }
                if (health < 0 && SnowmanDead.isDead){
                    SnowmanHit.setGotHit(2);
                    MainClass.musicLoop.stop();
                    main.setScreen(new GameOverScreen(main));
                }
            }

            //Spike to floor collision
            if (spike.getY() <= (y + imageFloor.getHeight() /3.5f)) {
                explosionsIce.add(new ExplodeIce(spike.getX(), spike.getY()));
                spikesToRemove.add(spike);
                spikeCount ++;
            }
        }

        //Snowball collision, HEALTH/SCORE INCREASE
        for (SnowballRotate snowballsRotate : snowballs) {
            if (snowballsRotate.getCollisionRect().collidesWith(playerRect)) {
                snowballsToRemove.add(snowballsRotate);
                Hud.addScore(1);
                FINAL_PLAY_SCORE += 1;
                if (health >= 1){
                    health = 1;
                }else
                health += 0.05f;
                if (MainClass.getsoundOnOff()) {
                    long idSnowballSound = collectSound.play();
                    collectSound.setVolume(idSnowballSound, musicVolHigh);
                    collectSound.setPitch(idSnowballSound, 2f);
                }
                Gdx.input.vibrate(50);
            }
        }

        //Snowflake collision, HEALTH/SCORE INCREASE
        for (SnowflakeRotate snowflakesRotate : snowflakes) {
            if (snowflakesRotate.getCollisionRect().collidesWith(playerRect)) {
                snowflakesToRemove.add(snowflakesRotate);
                Hud.addScore(2);
                FINAL_PLAY_SCORE += 2;
                if (health >= 1){
                    health = 1;
                }else
                    health += 0.1f;
                if (MainClass.getsoundOnOff()) {
                    long idSnowballSound = collectSound.play();
                    collectSound.setVolume(idSnowballSound, musicVolHigh);
                    collectSound.setPitch(idSnowballSound, 1f);
                }
                Gdx.input.vibrate(50);
            }
        }

        //Remove assets
        spikes.removeAll(spikesToRemove);
        explosionsIce.removeAll(explosionsToRemove);
        snowmanHit.removeAll(snowmanHitsToRemove);
        snowballs.removeAll(snowballsToRemove);
        snowflakes.removeAll(snowflakesToRemove);
        //Death animation; goto GameOver
        if (SnowmanDead.isDead) {
            snowmanDeath.removeAll(snowmanDeathsToRemove);
            main.setScreen(new GameOverScreen(main));
        }

        stateTime += delta;

        Gdx.gl.glClearColor(.2f, .2f, .2f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        main.batch.setProjectionMatrix(gameCamera.combined());

        main.batch.begin();

        //draw SCROLLING BACKGROUND
        MainClass.scrollingBackground.updateAndRender(delta, main.batch);

        //draw SPIKES
        for (Spike spike : spikes) { spike.render(main.batch); }
        //draw explosions
        for (ExplodeIce explodeIce : explosionsIce) { explodeIce.render(main.batch); }
        //draw SNOWBALLS
        for (SnowballRotate snowballsRotate : snowballs) { snowballsRotate.render(main.batch); }
        //draw SNOWFLAKES
        for (SnowflakeRotate snowflakesRotate : snowflakes) { snowflakesRotate.render(main.batch); }

        //draw SNOWMAN playing and hit reposition
        switch (getGotHit()) { //1=default, 2=HIT
            case 1: main.batch.draw(turns[TURN].getKeyFrame(stateTime, true),
                    x, defaultYPosition, SNOWMAN_WIDTH, SNOWMAN_HEIGHT);
                break;
            case 2: main.batch.draw(turns[TURN].getKeyFrame(stateTime, true),
                    x, offsetYPosition, SNOWMAN_WIDTH, SNOWMAN_HEIGHT);
                break;
        }
        //draw snowman HITS
        for (SnowmanHit snowmanHits : snowmanHit) { snowmanHits.render(main.batch); }
        //draw snowman DEATH
        for (SnowmanDead snowmanDeaths : snowmanDeath) { snowmanDeaths.render(main.batch); }
        //draw FLOOR
        main.batch.draw(imageFloor, 0,  y - imageFloor.getHeight(),
                (V_WIDTH /2) * floorImageScale,
                (imageFloor.getHeight() /2) * floorImageScale);

        //health color change
        if (health > 0.5f)
            main.batch.setColor(0f, 1, 0f, 0.75f);
        else if (health > 0.25f)
            main.batch.setColor(0.8f, 0.4f, 0, 0.9f);
        else
            main.batch.setColor(1, 0, 0, 0.9f);
        //draw HEALTH
        main.batch.draw(healthBlank, 0,
                V_HEIGHT * 0.965f,
                V_WIDTH * 0.99f * health,
                V_HEIGHT * 0.03f);
        main.batch.setColor(Color.WHITE);

        //draw CONTROLS
        if (showControls) {
            //Draw Left
            main.batch.setColor(Color.CYAN);
            main.batch.draw(controls, 0, 0, V_WIDTH /2,V_HEIGHT, 0, 0,
                    V_WIDTH /2, V_HEIGHT, false, false);
            //Draw Right
            main.batch.setColor(Color.BLUE);
            main.batch.draw(controls, V_WIDTH /2, 0, V_WIDTH /2, V_HEIGHT, 0, 0,
                    V_WIDTH /2, V_HEIGHT, true, false);
            //Draw Instructions
            main.batch.setColor(Color.WHITE);
            main.batch.draw(controlsInstructions, 0, 0, V_WIDTH, V_HEIGHT, 0, 0,
                    V_WIDTH, V_HEIGHT, false, false);

            main.batch.setColor(Color.WHITE);
        }

        if (PauseMenu.showPause) {
            main.batch.draw(PauseMenu.pauseOverlay, 0, 0, V_WIDTH, V_HEIGHT, 0, 0,
                    V_WIDTH, V_HEIGHT, false, false);
        }

        main.batch.end();

        //draw HUD
        if (!showControls){
            hud.hudStage.draw();
            pauseMenu.pauseStage.draw();
        }
    }

    private static float getMaxSpikeSpawnTime() {
        return MAX_SPIKE_SPAWN_TIME;
    }
    private static void setMaxSpikeSpawnTime(float maxSpikeSpawnTime) {
        MAX_SPIKE_SPAWN_TIME = maxSpikeSpawnTime; }

    public static boolean isShowControls() { return showControls; }
    public static void setShowControls(boolean showControls) {
        GameScreen.showControls = showControls; }

    @Override
    public void resize(int width, int height) {  }
    @Override
    public void pause() { System.out.println("GameScreen pause..."); }
    @Override
    public void resume() { System.out.println("GameScreen resume..."); }
    @Override
    public void hide() { System.out.println("GameScreen hide...");
        musicLoop.stop(); }
    @Override
    public void dispose() { System.out.println("GameScreen dispose...");
        hitSound_Dead.dispose(); }

}
