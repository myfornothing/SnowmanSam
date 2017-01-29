package com.fornothing.snowmansam.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Timer;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.fornothing.snowmansam.MainClass;
import com.fornothing.snowmansam.entities.ScrollingBackground;
import com.fornothing.snowmansam.utilities.FontGenerator;
import com.fornothing.snowmansam.utilities.PlayerMovement;

import static com.badlogic.gdx.scenes.scene2d.actions.Actions.alpha;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.fadeIn;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.moveBy;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.moveTo;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.parallel;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.run;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.sequence;
import static com.fornothing.snowmansam.MainClass.V_HEIGHT;
import static com.fornothing.snowmansam.MainClass.V_WIDTH;

public class GameOverScreen implements Screen {

    private MainClass main;
    private Stage stage;
    private TextureAtlas atlasPlayAgain, atlasExit;
    private Skin skinPlayAgain, skinExit;
    private Button buttonPlayAgain, buttonExit;
    private static int score;
    public static Preferences prefs;


    public GameOverScreen(MainClass main) {
        this.main = main;
        final GameOverScreen gameOverScreen = this;
        stage = new Stage(new FitViewport(V_WIDTH, V_HEIGHT));
        main.scrollingBackground.setSpeedFixed(true);
        main.scrollingBackground.setSpeed_1(ScrollingBackground.DEFAULT_SPEED_1);
        main.scrollingBackground.setSpeed_2(ScrollingBackground.DEFAULT_SPEED_2);

        //HIGH SCORE
//        prefs = Gdx.app.getPreferences("SnowmanSam");
//        if (!prefs.contains("highScore")) {
//            prefs.putInteger("highScore", 0);
//        }
    }
    //HIGH SCORE
//    public static void sethighScoreEasy(int val) {
//        prefs.putInteger("highScore", val);
//        prefs.flush();
//    }
//
//    public static int gethighScore() {
//        return prefs.getInteger("highScore");
//    }



    @Override
    public void show() {
        Gdx.input.setInputProcessor(stage);
        stage.clear();

        // Play Again button
        atlasPlayAgain = new TextureAtlas("ui/button_playAgain.pack");
        skinPlayAgain = new Skin(atlasPlayAgain);
        // Exit button
        atlasExit = new TextureAtlas("ui/button_exit.pack");
        skinExit = new Skin(atlasExit);

        initButtons();
    }

    private void update(float delta){
        Timer.instance().clear();
        stage.act(delta);
    }

    @Override
    public void render(float delta) {
        PlayerMovement.handleInput(delta);
        Gdx.input.setInputProcessor(stage);

        Gdx.gl.glClearColor(.2f, .2f, .2f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        update(delta);
        stage.draw();

        main.batch.begin();
        main.scrollingBackground.updateAndRender(delta, main.batch);

        FontGenerator.Bulldozer.draw(main.batch, "Game Over",
                V_WIDTH *0.13f, V_HEIGHT * .85f);
        main.batch.end();
    }

    private void initButtons() {
        // PLAY Again Button:
        Button.ButtonStyle buttonStylePlayAgain = new Button.ButtonStyle();
        buttonStylePlayAgain.up = skinPlayAgain.getDrawable("playAgainUp");
        buttonStylePlayAgain.down = skinPlayAgain.getDrawable("playAgainDown");
        buttonStylePlayAgain.pressedOffsetX = 10;
        buttonStylePlayAgain.pressedOffsetY = -10;
        // EXIT Button:
        Button.ButtonStyle buttonStyleExit = new Button.ButtonStyle();
        buttonStyleExit.up = skinExit.getDrawable("exitUp");
        buttonStyleExit.down = skinExit.getDrawable("exitDown");
        buttonStyleExit.pressedOffsetX = 10;
        buttonStyleExit.pressedOffsetY = -10;

        // PLAY Again Button:
        buttonPlayAgain = new Button(buttonStylePlayAgain);
        buttonPlayAgain.pad(10f);
        buttonPlayAgain.setPosition(V_WIDTH /2 - buttonPlayAgain.getWidth() /2,
                V_HEIGHT /2);
        buttonPlayAgain.addAction(sequence(alpha(0),
                parallel(fadeIn(.5f), moveBy(0, -50, 2f, Interpolation.pow5Out))));

        buttonPlayAgain.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                stage.addAction(sequence(moveTo(0, -stage.getHeight(),
                        1f), run(new Runnable() {
                    @Override
                    public void run() {
                        main.setScreen(new LoadingScreen(main));
                    }
                })));
            }
        });

        // EXIT Button:
        buttonExit = new Button(buttonStyleExit);
        buttonExit.pad(15);
        buttonExit.setPosition(V_WIDTH /2 - buttonExit.getWidth() /2,
                V_HEIGHT /2);
        buttonExit.addAction(sequence(alpha(0),
                parallel(fadeIn(1f), moveBy(0, -176, 2f, Interpolation.pow5Out))));

        buttonExit.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                stage.addAction(run(new Runnable() {
                    @Override
                    public void run() {
//                        main.setScreen(new GameOverScreen(main));
                        Gdx.app.exit();

                    }
                }));
            }
        });

        stage.addActor(buttonPlayAgain);
        stage.addActor(buttonExit);
    }


    @Override
    public void resize(int width, int height) {}
    @Override
    public void pause() {}
    @Override
    public void resume() {}
    @Override
    public void hide() { System.out.println("GameOver hide..."); }
    @Override
    public void dispose() {System.out.println("GameOver dispose..."); }
}
