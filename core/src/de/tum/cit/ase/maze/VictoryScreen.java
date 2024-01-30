package de.tum.cit.ase.maze;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

import javax.swing.*;
import java.io.File;


public class VictoryScreen implements Screen {
    private final Stage stage;
    private Music victoryMusic;

    /**
     * Constructor for VictoryScreen
     * Sets up the camera, viewport, stage, and UI elements.
     * VictoryScreen appears when the Character successfully exits the Maze with a Key and at least 1 Life
     * From here, the character can Restart, Go to Menu or Exit
     * @param game
     * @param gameScreen
     */
    public VictoryScreen(MazeRunnerGame game, GameScreen gameScreen) {
        var camera = new OrthographicCamera();
        camera.zoom = 1.5f; // Set camera zoom for a closer view


        Viewport viewport = new ScreenViewport(camera); // Create a viewport with the camera
        stage = new Stage(viewport, game.getSpriteBatch()); // Create a stage for UI elements

        /**
         * https://www.youtube.com/watch?v=teUWsONJkk8&ab_channel=SoundEffectsFree
         * Victory Sound Effect by Sound Effects Free on YouTube
         */
        victoryMusic = Gdx.audio.newMusic(Gdx.files.internal("Victory.mp3"));
        victoryMusic.setLooping(false);
        victoryMusic.play();

        Table table = new Table(); // Create a table for layout
        table.setFillParent(true); // Make the table fill the stage
        stage.addActor(table); // Add the table to the stage

        long elapsedTimeMillis = System.currentTimeMillis()-GameScreen.getStartTime();
        String formattedTime = formatElapsedTime(elapsedTimeMillis);

        Label.LabelStyle labelStyle = new Label.LabelStyle(game.getSkin().get("bold", Label.LabelStyle.class));
        labelStyle.font.getData().setScale(1.75f);

        // Add a label to display the elapsed time on VictoryScreen
        table.add(new Label("Time: " + formattedTime, labelStyle)).padBottom(40).row();

        // Add a label as a title
        table.add(new Label("VICTORY", game.getSkin(), "title")).padBottom(80).row();

        TextButton restartGameButton = new TextButton("Restart", game.getSkin());
        table.add(restartGameButton).width(400).row();
        restartGameButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                GameScreen.getGameScreenMusic().stop();
                victoryMusic.stop();
                PauseScreen.setReset(true);
                GameScreen.reset();
                game.goToGame();
            }
        });

        TextButton menuGameButton = new TextButton("Go to Menu", game.getSkin());
        table.add(menuGameButton).width(400).row();
        menuGameButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                GameScreen.getGameScreenMusic().stop();
                PauseScreen.setReset(true);
                victoryMusic.stop();
                GameScreen.reset();
                game.goToMenu();
            }
        });

        // Create and add a button to go to the game screen
        TextButton exitButton = new TextButton("Exit Game", game.getSkin());
        table.add(exitButton).width(400).row();
        exitButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                PauseScreen.setReset(true);
                Gdx.app.exit();
            }
        });

    }
    private String formatElapsedTime(long elapsedTimeMillis) {
        long totalSeconds = elapsedTimeMillis / 1000;
        long minutes = totalSeconds / 60;
        long seconds = totalSeconds % 60;

        return String.format("%02d:%02d", minutes, seconds);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        stage.act(Math.min(Gdx.graphics.getDeltaTime(), 1 / 30f));
        stage.draw();

    }


    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(width, height, true); // Update the stage viewport on resize
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {
        // Dispose of the stage when screen is disposed
        stage.dispose();
    }

    @Override
    public void show() {
        // Set the input processor so the stage can receive input events
        Gdx.input.setInputProcessor(stage);
    }

}