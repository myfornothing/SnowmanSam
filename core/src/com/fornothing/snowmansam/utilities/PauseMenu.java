package com.fornothing.snowmansam.utilities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.fornothing.snowmansam.MainClass;
import com.fornothing.snowmansam.screens.GameScreen;

import static com.fornothing.snowmansam.MainClass.musicLoop;
import static com.fornothing.snowmansam.MainClass.musicVolHigh;
import static com.fornothing.snowmansam.MainClass.musicVolLow;

public final class PauseMenu {

    public final Stage pauseStage;
    private static Button button_pause, musicOnOff, soundOnOff;
    public static Texture pauseOverlay;
    public static Table table;

    public static boolean showPause = false;
    private static int isPaused = 1;  //1=no, 2=yes
    public static int getIsPaused() { return isPaused; }
    private static void setIsPaused(int isPaused) { PauseMenu.isPaused = isPaused; }


    public PauseMenu(SpriteBatch batch) {

        pauseStage = new Stage(new FitViewport(MainClass.V_WIDTH, MainClass.V_HEIGHT));

        Gdx.input.setInputProcessor(pauseStage);

        TextureAtlas atlasPause = new TextureAtlas("ui/buttonPause.pack");
        Skin skinPause = new Skin(atlasPause);

        TextureAtlas atlasMusicOnOff = new TextureAtlas("ui/musicOnOff.pack");
        Skin skinMusicOnOff = new Skin(atlasMusicOnOff);

        TextureAtlas atlasSoundOnOff = new TextureAtlas("ui/soundOnOff.pack");
        Skin skinSoundOnOff = new Skin(atlasSoundOnOff);

        Button.ButtonStyle buttonStylePause = new Button.ButtonStyle();
        buttonStylePause.up = skinPause.getDrawable("button_PauseUp");
        buttonStylePause.checked = skinPause.getDrawable("button_pauseDown");

        final Button.ButtonStyle buttonStyleMusicOnOff = new Button.ButtonStyle();
        buttonStyleMusicOnOff.up = skinMusicOnOff.getDrawable("MusicOn_icon");
        buttonStyleMusicOnOff.checked = skinMusicOnOff.getDrawable("MusicOff_icon");

        final Button.ButtonStyle buttonStyleSoundOnOff = new Button.ButtonStyle();
        buttonStyleSoundOnOff.up = skinSoundOnOff.getDrawable("SoundOn_icon");
        buttonStyleSoundOnOff.checked = skinSoundOnOff.getDrawable("SoundOff_icon");


        button_pause = new Button(buttonStylePause);
//        musicOnOff = new Button(buttonStyleMusicOnOff);
//        soundOnOff = new Button(buttonStyleSoundOnOff);

        pauseOverlay = new Texture("ui/pause_overlay.png");

        button_pause.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {

                if (button_pause.isChecked()) {
                    Gdx.input.vibrate(50);
                    showPause = true;
                    setIsPaused(2); //yes
                    GameScreen.PLAYER_SPEED = 0;
                }
                if (!button_pause.isChecked()) {
                    Gdx.input.vibrate(50);
                    showPause = false;
                    setIsPaused(1); //no
                    GameScreen.PLAYER_SPEED = 350;
                }
            }
        });
        //MUSIC ON/OFF
        if (MainClass.getmusicOnOff()) {
            musicOnOff = new Button(buttonStyleMusicOnOff.up);
        } else {
            musicOnOff = new Button(buttonStyleMusicOnOff.checked);
        }
        musicOnOff.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {

                if (musicOnOff.isChecked()){
                    musicOnOff.setStyle(buttonStyleMusicOnOff);
                    MainClass.setmusicOnOff(false);
                    MainClass.musicLoop.pause();
                }
                if (!musicOnOff.isChecked()){
                    musicOnOff.setStyle(buttonStyleMusicOnOff);
                    MainClass.setmusicOnOff(true);
                    MainClass.musicLoop.play();
                }
            }
        });
        //SOUND ON/OFF
        if (MainClass.getsoundOnOff()) {
            soundOnOff = new Button(buttonStyleSoundOnOff.up);
        } else {
            soundOnOff = new Button(buttonStyleSoundOnOff.checked);
        }
        soundOnOff.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {

                if (soundOnOff.isChecked()) {
                    soundOnOff.setStyle(buttonStyleSoundOnOff);
                    MainClass.setsoundOnOff(false);
                }
                if (!soundOnOff.isChecked()) {
                    MainClass.setsoundOnOff(true);
                    soundOnOff.setStyle(buttonStyleSoundOnOff);
                }
            }
        });

        table = new Table();
        table.top().right();
        table.setFillParent(true);
        table.top().add(button_pause).padTop(pauseStage.getHeight() * 0.05f).padRight(20).size(50);
        table.row();
        table.getCell(button_pause).padBottom(20);
        table.add(musicOnOff).padRight(20).size(50);
        table.row();
        table.getCell(musicOnOff).padBottom(20);
        table.add(soundOnOff).padRight(20).size(50);

        pauseStage.addActor(table);
    }

    public void update(float dt){
        if (PauseMenu.getIsPaused() == 1 && !GameScreen.isShowControls())
            musicLoop.setVolume(musicVolHigh);
        if (PauseMenu.getIsPaused() == 2)
            musicLoop.setVolume(musicVolLow);
    }

    public void render(SpriteBatch batch) { }
}
