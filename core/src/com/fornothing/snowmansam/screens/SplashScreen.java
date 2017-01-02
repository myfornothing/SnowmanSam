package com.fornothing.snowmansam.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.utils.Timer;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.fornothing.snowmansam.MainClass;

import static com.badlogic.gdx.scenes.scene2d.actions.Actions.alpha;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.delay;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.fadeIn;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.fadeOut;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.moveBy;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.moveTo;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.parallel;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.rotateBy;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.run;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.scaleTo;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.sequence;
import static com.fornothing.snowmansam.MainClass.V_HEIGHT;
import static com.fornothing.snowmansam.MainClass.V_WIDTH;

public class SplashScreen implements Screen {

    private final MainClass main;
    private static OrthographicCamera camera;
    private Stage stage;

    private Image background, snowman;

    private Image letter_S, letter_N, letter_O, letter_W, letter_M, letter_A,
            letter_N2, letter_S2, letter_A2, letter_M2;

    public SplashScreen(MainClass main) {
        this.main = main;
        final SplashScreen splashScreen = this;
        stage = new Stage(new FitViewport(V_WIDTH, V_HEIGHT));
        camera = new OrthographicCamera();
        camera.setToOrtho(false, MainClass.V_WIDTH, MainClass.V_HEIGHT);
    }

    @Override
    public void show() {

        background = new Image(new Texture("images/background.png"));
        background.setOrigin(-background.getWidth(), -background.getHeight() );
        background.setPosition(0,0);

        snowman = new Image(new Texture("images/Snowman_Splash.png"));

        letter_S = new Image(new Texture("images/letter_S.png"));
        letter_N = new Image(new Texture("images/letter_N.png"));
        letter_O = new Image(new Texture("images/letter_O.png"));
        letter_W = new Image(new Texture("images/letter_W.png"));
        letter_M = new Image(new Texture("images/letter_M.png"));
        letter_A = new Image(new Texture("images/letter_A.png"));
        letter_N2 = new Image(new Texture("images/letter_N.png"));
        letter_S2 = new Image(new Texture("images/letter_S.png"));
        letter_A2 = new Image(new Texture("images/letter_A.png"));
        letter_M2 = new Image(new Texture("images/letter_M.png"));

        initActors();
    }

    private void update(float delta){
        Timer.instance().clear();
        stage.act(delta);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(.2f, .2f, .2f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        main.batch.setProjectionMatrix(camera.combined);
        update(delta);

        stage.draw();

        main.batch.begin();
        main.batch.end();
    }

    private void initActors() {
        Runnable transitionRunnable = new Runnable() {
            @Override
            public void run() {
//                main.setScreen(new SplashScreen(main));
                main.setScreen(main.gameScreen);
            }
        };

        background.addAction(sequence(alpha(0), fadeIn(0.5f)));

        letter_S.setScale(.5f);
        letter_S.setPosition(V_WIDTH *0.5f, V_HEIGHT + letter_S.getImageHeight());
        letter_S.addAction(sequence(delay(0.01f),alpha(0),
                parallel(fadeIn(.5f), moveBy(-190 , -250, 2f, Interpolation.pow5Out),
                        rotateBy(360, 1f))));

        letter_N.setScale(.5f);
        letter_N.setPosition(V_WIDTH *0.5f, V_HEIGHT + letter_N.getImageHeight());
        letter_N.addAction(sequence(delay(0.1f),alpha(0),
                parallel(fadeIn(.5f), moveBy(-130, -250, 2f, Interpolation.pow5Out),
                        rotateBy(360, 1f))));

        letter_O.setScale(.5f);
        letter_O.setPosition(V_WIDTH *0.5f, V_HEIGHT + letter_O.getImageHeight());
        letter_O.addAction(sequence(delay(.3f),alpha(0),
                parallel(fadeIn(.5f), moveBy(-80, -250, 2f, Interpolation.pow5Out),
                        rotateBy(360, 1f))));

        letter_W.setScale(.5f);
        letter_W.setPosition(V_WIDTH *0.5f, V_HEIGHT + letter_W.getImageHeight());
        letter_W.addAction(sequence(delay(.5f),alpha(0),
                parallel(fadeIn(.5f), moveBy(-35, -250, 2f, Interpolation.pow5Out),
                        rotateBy(-360, 1f))));

        letter_M.setScale(.5f);
        letter_M.setPosition(V_WIDTH *0.5f, V_HEIGHT + letter_M.getImageHeight());
        letter_M.addAction(sequence(delay(.7f),alpha(0),
                parallel(fadeIn(.5f), moveBy(40, -250, 2f, Interpolation.pow5Out),
                        rotateBy(-360, 1f))));

        letter_A.setScale(.5f);
        letter_A.setPosition(V_WIDTH *0.5f, V_HEIGHT + letter_A.getImageHeight());
        letter_A.addAction(sequence(delay(.9f),alpha(0),
                parallel(fadeIn(.5f), moveBy(100, -250, 2f, Interpolation.pow5Out),
                        rotateBy(-360, 1f))));

        letter_N2.setScale(.5f);
        letter_N2.setPosition(V_WIDTH *0.5f, V_HEIGHT + letter_N2.getImageHeight());
        letter_N2.addAction(sequence(delay(1.1f),alpha(0),
                parallel(fadeIn(.5f), moveBy(150, -250, 2f, Interpolation.pow5Out),
                        rotateBy(-360, 1f))));

        letter_S2.setScale(.6f);
        letter_S2.setPosition(V_WIDTH *0.5f, V_HEIGHT + letter_S2.getImageHeight());
        letter_S2.addAction(sequence(delay(1.4f),alpha(0),
                parallel(fadeIn(.5f), moveBy(-45, -350, 2f, Interpolation.pow5Out))));

        letter_A2.setScale(.6f);
        letter_A2.setPosition(V_WIDTH *0.5f, V_HEIGHT + letter_A2.getImageHeight());
        letter_A2.addAction(sequence(delay(1.4f),alpha(0),
                parallel(fadeIn(.5f), moveBy(20, -350, 2f, Interpolation.pow5Out))));

        letter_M2.setScale(.6f);
        letter_M2.setPosition(V_WIDTH *0.5f, V_HEIGHT + letter_M2.getImageHeight());
        letter_M2.addAction(sequence(delay(1.4f),alpha(0),
                parallel(fadeIn(.5f), moveBy(80, -350, 2f, Interpolation.pow5Out))));

        //Snowman bounce-in
        snowman.setPosition(V_WIDTH *0.4f, V_HEIGHT  * 1.5f);
        snowman.addAction(sequence(alpha(0), scaleTo(.01f, .01f),
                parallel(fadeIn(1f, Interpolation.pow2),
                        scaleTo(1.5f, 1.5f, 3f, Interpolation.swingIn),
                        moveTo(V_WIDTH * 0.3f, V_HEIGHT * .25f,
                                3f, Interpolation.bounceIn))));
        //Stage fade away
        stage.addAction(sequence(delay(3.5f), alpha(1), parallel(fadeOut(1f, Interpolation.pow2),
                moveTo(V_WIDTH, 0, 1.8f, Interpolation.pow3OutInverse),
                sequence(delay(.9f), run(transitionRunnable)))));

        stage.addActor(background);
        stage.addActor(letter_S);
        stage.addActor(letter_N);
        stage.addActor(letter_O);
        stage.addActor(letter_W);
        stage.addActor(letter_M);
        stage.addActor(letter_A);
        stage.addActor(letter_N2);
        stage.addActor(letter_S2);
        stage.addActor(letter_A2);
        stage.addActor(letter_M2);
        stage.addActor(snowman);
    }

    @Override
    public void resize(int width, int height) {  }
    @Override
    public void pause()  {System.out.println("SplashScr pause...");  }
    @Override
    public void resume()  {System.out.println("SplashScr resume..."); }
    @Override
    public void hide() {
        System.out.println("SplashScr hide...");
        dispose();
    }
    @Override
    public void dispose() {
        System.out.println("SplashScr dispose...");
        stage.dispose();
    }
}
