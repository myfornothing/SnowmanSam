package com.fornothing.snowmansam.utilities;


import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.fornothing.snowmansam.MainClass;

import java.util.Locale;

public final class Hud  {

    public final Stage hudStage;
    private static Integer score;
    private final Label scoreLabel;
    private final Label healthLabel;
    private static Label scoreCountLabel;
    public static int FINAL_PLAY_SCORE;

    public Hud(SpriteBatch sb) {
        score = 0;
        hudStage = new Stage(new FitViewport(MainClass.V_WIDTH, MainClass.V_HEIGHT));

        scoreLabel = new Label("Score", new Label.LabelStyle(FontGenerator.Flatwheat,
            Color.WHITE));
        scoreLabel.setFontScale(1f);

        healthLabel = new Label("health", new Label.LabelStyle(FontGenerator.Flatwheat,
                Color.WHITE));
        healthLabel.setFontScale(0.7f);

        scoreCountLabel = new Label(String.format(Locale.US, "0", score),
                new Label.LabelStyle(FontGenerator.Flatwheat, Color.WHITE));
        scoreCountLabel.setFontScale(1f);

    //HUD TABLE
        Table table = new Table();
        table.top().left();
        table.setFillParent(true);
        table.top().left().add(healthLabel).padTop(hudStage.getHeight() * 0.03f);
        table.row();
        table.add(scoreLabel).padTop(hudStage.getHeight() * 0.01f).padLeft(12);
        table.add(scoreCountLabel).padTop(hudStage.getHeight() * 0.01f).padLeft(22);

        hudStage.addActor(table);
    }

    public static void addScore(int value){
        score += value;
        scoreCountLabel.setText(String.format(Locale.US,"%01d", score));
    }

    //Timer
    public void update(float dt){  }

    public void render(SpriteBatch sb) {  }

    public static Label getScoreCountLabel() { return scoreCountLabel; }
    public static Integer getScore() { return score; }

    public static int getFinalPlayScore() {
        return FINAL_PLAY_SCORE;
    }
    public static void resetFinalPlayScore(){
        FINAL_PLAY_SCORE = 0;
    }

    public void dispose(){
        hudStage.dispose();
    }
}
