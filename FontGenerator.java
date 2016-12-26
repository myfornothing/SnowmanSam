package com.fornothing.snowmansam.utilities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;

public final class FontGenerator {

    public static BitmapFont Flatwheat, Bulldozer;

    private FontGenerator(){  }

    public static void initFonts() {
        FreeTypeFontGenerator generator1 = new FreeTypeFontGenerator(Gdx.files.internal("fonts/Flatwheat-Regular.ttf"));
        FreeTypeFontGenerator.FreeTypeFontParameter params1 = new FreeTypeFontGenerator.FreeTypeFontParameter();
        params1.size = 30;
        params1.color = Color.ROYAL;
        params1.borderColor = Color.BLACK;
        params1.borderWidth = 2;
        params1.shadowColor = Color.WHITE;
        params1.shadowOffsetX = 1;
        params1.shadowOffsetY = 1;
        Flatwheat = generator1.generateFont(params1);

        FreeTypeFontGenerator generator2 = new FreeTypeFontGenerator(Gdx.files.internal("fonts/Bulldozer.ttf"));
        FreeTypeFontGenerator.FreeTypeFontParameter params2 = new FreeTypeFontGenerator.FreeTypeFontParameter();
        params2.size = 80;
        params2.color = Color.CYAN;
        params2.borderColor = Color.BLACK;
        params2.borderWidth = 3;
        params2.shadowColor = Color.FIREBRICK;
        params2.shadowOffsetX = 5;
        params2.shadowOffsetY = 5;
        Bulldozer = generator2.generateFont(params2);
    }
}
