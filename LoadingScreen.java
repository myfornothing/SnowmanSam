package com.fornothing.snowmansam.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.fornothing.snowmansam.MainClass;
import com.fornothing.snowmansam.entities.SnowmanHit;
import com.fornothing.snowmansam.utilities.FontGenerator;

import static com.fornothing.snowmansam.MainClass.V_HEIGHT;
import static com.fornothing.snowmansam.MainClass.V_WIDTH;
import static com.fornothing.snowmansam.screens.GameScreen.MAX_SPIKE_SPAWN_TIME;
import static com.fornothing.snowmansam.screens.GameScreen.MAX_SPIKE_SPAWN_TIME_DEFAULT;
import static com.fornothing.snowmansam.screens.GameScreen.PLAYER_SPEED;
import static com.fornothing.snowmansam.screens.GameScreen.SNOWMAN_HEIGHT;
import static com.fornothing.snowmansam.screens.GameScreen.SNOWMAN_WIDTH;
import static com.fornothing.snowmansam.screens.GameScreen.SPIKE_INCREASE_NUMBER;
import static com.fornothing.snowmansam.screens.GameScreen.health;
import static com.fornothing.snowmansam.screens.GameScreen.x;
import static com.fornothing.snowmansam.screens.GameScreen.y;
import static com.fornothing.snowmansam.utilities.FontGenerator.initFonts;

public class LoadingScreen implements Screen {

    private final MainClass main;
    public static OrthographicCamera camera;
    private Stage stage;
    private float progress;

    private Sprite background;
    private float backgroundScale;

    public LoadingScreen(MainClass main) {
        this.main = main;
        stage = new Stage(new FitViewport(Gdx.graphics.getWidth(),
                Gdx.graphics.getHeight()));
        camera = new OrthographicCamera();
        camera.setToOrtho(false, MainClass.V_WIDTH, MainClass.V_HEIGHT);

        background = new Sprite(new Texture("images/background.png"));
        backgroundScale = MainClass.V_WIDTH / background.getWidth();

    }

    private void queueAssets(){
        System.out.println("Loading assets...");

        MainClass.assets.load("animations/explode_spike2.png", Texture.class);
        MainClass.assets.load("animations/snowball-rotate.png", Texture.class);
        MainClass.assets.load("animations/snowflakes_falling_big.png", Texture.class);
        MainClass.assets.load("animations/snowflakes_falling_small.png", Texture.class);
        MainClass.assets.load("animations/SnowmanHit_Scarf.png", Texture.class);
        MainClass.assets.load("animations/SnowmanSheet_Scarf.png", Texture.class);

        MainClass.assets.load("images/health_blank.png", Texture.class);
        MainClass.assets.load("images/Ice_Spike_1.png", Texture.class);
        MainClass.assets.load("images/iceblockFloor.png", Texture.class);
        MainClass.assets.load("images/background.png", Texture.class);


        MainClass.assets.load("ui/button_exit.png", Texture.class);
        MainClass.assets.load("ui/button_playAgain.png", Texture.class);

        initFonts();
    }

    private void resetGameParams() {
        System.out.println("LoadingScr resetParams...");
        SnowmanHit.setGotHit(1);
        PLAYER_SPEED = 350;
        health = 1;
        MAX_SPIKE_SPAWN_TIME = MAX_SPIKE_SPAWN_TIME_DEFAULT;
        SPIKE_INCREASE_NUMBER = 15;
        main.scrollingBackground.setSpeedFixed(false);
        x = V_WIDTH /2 - SNOWMAN_WIDTH /2;
        y = V_HEIGHT * 0.2f - SNOWMAN_HEIGHT /2;
    }

    @Override
    public void show() {
        System.out.println("LoadingScr show...");
        stage.clear();
        main.shapeRenderer.setProjectionMatrix(camera.combined);
        this.progress = 0f;
        queueAssets();
        resetGameParams();
    }

    private void update(float delta) {
        progress = MathUtils.lerp(progress, MainClass.assets.getProgress(), .1f);
        if (MainClass.assets.update() && progress >= MainClass.assets.getProgress() - .01f){
            main.setScreen(main.gameScreen);
        }
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(.2f, .2f, .2f, 1);
//        Gdx.gl.glClearColor(.2f, .6f, .7f, 0.1f); //blue
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        main.batch.setProjectionMatrix(camera.combined);

        main.shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        main.shapeRenderer.setColor(Color.WHITE);
        main.shapeRenderer.rect(
                camera.viewportWidth * .05f,
                camera.viewportHeight/3,
                camera.viewportWidth * .9f,
                camera.viewportHeight * .04f);
        main.shapeRenderer.setColor(Color.BLUE);
        main.shapeRenderer.rect(
                camera.viewportWidth * .05f,
                camera.viewportHeight/3,
                progress * (camera.viewportWidth * .9f),
                camera.viewportHeight * .04f);
        main.shapeRenderer.end();

        main.batch.begin();

        main.batch.draw(background, 0, 0, MainClass.V_WIDTH,
                background.getHeight() / backgroundScale);

        FontGenerator.Flatwheat.draw(main.batch, "Loading...",
                camera.viewportWidth * 0.05f, camera.viewportHeight * 0.12f);
        main.batch.end();

        update(delta);
    }

    @Override
    public void resize(int width, int height) {  }
    @Override
    public void pause()  {System.out.println("LoadingScr pause...");  }
    @Override
    public void resume()  {System.out.println("LoadingScr resume..."); }
    @Override
    public void hide()    {System.out.println("LoadingScr hide...");  }
    @Override
    public void dispose() {System.out.println("LoadingScr dispose..."); }
}
