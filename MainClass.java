package com.fornothing.snowmansam;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.fornothing.snowmansam.entities.ScrollingBackground;
import com.fornothing.snowmansam.screens.GameOverScreen;
import com.fornothing.snowmansam.screens.GameScreen;
import com.fornothing.snowmansam.screens.LoadingScreen;
import com.fornothing.snowmansam.screens.SplashScreen;
import com.fornothing.snowmansam.utilities.GameCamera;

public class MainClass extends Game {
	public static final String APP_TITLE = "SnowmanSam";
	public static final double APP_VERSION = 0.1;
	public static final int APP_DESKTOP_WIDTH = 900;
	public static final int APP_DESKTOP_HEIGHT = 1500;
	public static final int APP_FPS = 60;
	public static final int V_WIDTH = 420;
	public static final int V_HEIGHT = 720;

	public SpriteBatch batch;
    public ShapeRenderer shapeRenderer;
	public ScrollingBackground scrollingBackground;

	public SplashScreen splashScreen;
	public LoadingScreen loadingScreen;
    public GameScreen gameScreen;
    public GameOverScreen gameOverScreen;

	public static GameCamera gameCamera;  // made static for shake
    public static AssetManager assets;

	@Override
	public void create () {
        assets = new AssetManager();
        batch = new SpriteBatch();
        this.scrollingBackground = new ScrollingBackground();
        this.scrollingBackground.setSpeedFixed(true);
        shapeRenderer = new ShapeRenderer();
		gameCamera = new GameCamera(V_WIDTH, V_HEIGHT);

        //game screens
        splashScreen = new SplashScreen(this);
		loadingScreen = new LoadingScreen(this);
        gameScreen = new GameScreen(this);
		gameOverScreen = new GameOverScreen(this);

		//initFonts();

		this.setScreen(new SplashScreen(this));
	}

	@Override
	public void render () {
		Gdx.gl.glClearColor(1, 1, 1, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        batch.setProjectionMatrix(gameCamera.combined());
		super.render();
	}

	@Override
	public void resize(int width, int height) {
		this.scrollingBackground.resize(width, height);
		super.resize(width, height);
	}

    @Override
    public void resume() {  }

    @Override
	public void dispose () {
		super.dispose(); System.out.println("super dispose");
		batch.dispose(); System.out.println("batch dispose");
        shapeRenderer.dispose(); System.out.println("shaperRend dispose");
        assets.dispose(); System.out.println("assets dispose");
	}
}