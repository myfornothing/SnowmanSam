package com.fornothing.snowmansam.utilities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.fornothing.snowmansam.MainClass;

import static com.fornothing.snowmansam.MainClass.V_WIDTH;
import static com.fornothing.snowmansam.screens.GameScreen.PLAYER_SPEED;
import static com.fornothing.snowmansam.screens.GameScreen.SNOWMAN_WIDTH;
import static com.fornothing.snowmansam.screens.GameScreen.TURN;
import static com.fornothing.snowmansam.screens.GameScreen.x;

public abstract class PlayerMovement {

    private final static float TURN_TIMER_SWITCH_TIME = 0.15f;
    private static float TURN_TIMER = 0;


    public static void handleInput(float delta) {
        if (Gdx.input.isKeyPressed(Input.Keys.ESCAPE)) {
            //this.setScreen(gameScreen);
            Gdx.app.exit();
        }

        //Movement code
        //BOTH DIRECTIONS PRESSED

        //for (int i = 0; i<3; i++) {

            //(isLeft() && isJustRight()) || (isJustLeft() && isRight())

            if (Gdx.input.isTouched() && Gdx.input.justTouched()) {
                if (TURN < 2) {
                    //Turn back to center
                    TURN_TIMER += Gdx.graphics.getDeltaTime();
                    if (Math.abs(TURN_TIMER) > TURN_TIMER_SWITCH_TIME && TURN < 4) {
                        TURN_TIMER -= TURN_TIMER_SWITCH_TIME;
                        TURN++;
                    }
                } else {
                    if (TURN > 2) {
                        //Turn back to center
                        TURN_TIMER -= Gdx.graphics.getDeltaTime();
                        if (Math.abs(TURN_TIMER) > TURN_TIMER_SWITCH_TIME && TURN > 0) {
                            TURN_TIMER -= TURN_TIMER_SWITCH_TIME;
                            TURN--;
                        }
                    }
                }
            }
        x -= 0 * Gdx.graphics.getDeltaTime();

        //LEFT
        if (isLeft()) { //Left pressed
            x -= PLAYER_SPEED * Gdx.graphics.getDeltaTime();
            if (x < 0)
                x = 0;
            //Update if Left clicked
            if (isJustLeft() && !isRight() && TURN > 0) {
                TURN_TIMER = 0;
                TURN--;
            }
            //Turn update
            TURN_TIMER -= Gdx.graphics.getDeltaTime();
            if (Math.abs(TURN_TIMER) > TURN_TIMER_SWITCH_TIME && TURN > 0) {
                TURN_TIMER -= TURN_TIMER_SWITCH_TIME;
                TURN--;
            }
        } else {
            if (TURN < 2) {
                //Turn back to center
                TURN_TIMER += Gdx.graphics.getDeltaTime();
                if (Math.abs(TURN_TIMER) > TURN_TIMER_SWITCH_TIME && TURN < 4) {
                    TURN_TIMER -= TURN_TIMER_SWITCH_TIME;
                    TURN++;
                }
            }
        }
        //RIGHT
        if (isRight()) { //Right pressed
            x += PLAYER_SPEED * Gdx.graphics.getDeltaTime();
            if (x + SNOWMAN_WIDTH > V_WIDTH)
                x = V_WIDTH - SNOWMAN_WIDTH;
            //Update if Right clicked
            if (isJustRight() && !isLeft() && TURN < 4) {
                TURN_TIMER = 0;
                TURN++;
            }
            //Update TURN right
            TURN_TIMER += Gdx.graphics.getDeltaTime();
            if (Math.abs(TURN_TIMER) > TURN_TIMER_SWITCH_TIME && TURN < 4) {
                TURN_TIMER -= TURN_TIMER_SWITCH_TIME;
                TURN++;
            }
        } else {
            if (TURN > 2) {
                //Turn back to center
                TURN_TIMER -= Gdx.graphics.getDeltaTime();
                if (Math.abs(TURN_TIMER) > TURN_TIMER_SWITCH_TIME && TURN > 0) {
                    TURN_TIMER -= TURN_TIMER_SWITCH_TIME;
                    TURN--;
                }
            }
        }
    }


    public static boolean isRight() {
        return Gdx.input.isKeyPressed(Input.Keys.RIGHT) || (Gdx.input.isTouched()
                && MainClass.gameCamera.getInputInGameWorld().x >= V_WIDTH / 2);
    }
    private static boolean isJustRight() {
        return Gdx.input.isKeyJustPressed(Input.Keys.RIGHT) || (Gdx.input.justTouched()
                && MainClass.gameCamera.getInputInGameWorld().x >= V_WIDTH / 2);
    }
    public static boolean isLeft() {
        return Gdx.input.isKeyPressed(Input.Keys.LEFT) || (Gdx.input.isTouched()
                && MainClass.gameCamera.getInputInGameWorld().x < V_WIDTH / 2);
    }
    private static boolean isJustLeft() {
        return Gdx.input.isKeyJustPressed(Input.Keys.LEFT) || (Gdx.input.justTouched()
                && MainClass.gameCamera.getInputInGameWorld().x < V_WIDTH / 2);
    }

}