package com.fornothing.snowmansam.screens;


import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.fornothing.snowmansam.MainClass;

import static com.fornothing.snowmansam.MainClass.V_HEIGHT;
import static com.fornothing.snowmansam.MainClass.V_WIDTH;
import static com.fornothing.snowmansam.MainClass.gameCamera;

public class AdsScreen  extends ApplicationAdapter implements Screen{
    private final MainClass main;

    private static OrthographicCamera camera;
    private Stage stage;


    public AdsScreen(MainClass main) {
        this.main = main;
        final AdsScreen adsScreen = this;
        stage = new Stage(new FitViewport(V_WIDTH, V_HEIGHT));
        camera = new OrthographicCamera();
        camera.setToOrtho(false, MainClass.V_WIDTH, MainClass.V_HEIGHT);
    }

    @Override
    public void create() {
        super.create();
    }

    @Override
    public void show() {
        System.out.println("AdsScreen show...");



    }

    private void update(float delta) {

    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(.1f, 1, .1f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        main.batch.setProjectionMatrix(gameCamera.combined());


    }




    @Override
    public void resize(int width, int height) {  }
    @Override
    public void pause() {  }
    @Override
    public void resume() { System.out.println("AdsScreen resume...");  }
    @Override
    public void hide() { System.out.println("AdsScreen hide..."); }
    @Override
    public void dispose() { System.out.println("AdsScreen dispose..."); }
}
