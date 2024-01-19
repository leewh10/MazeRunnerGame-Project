package de.tum.cit.ase.maze;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
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

    public VictoryScreen(MazeRunnerGame game, GameScreen gameScreen) {
        var camera = new OrthographicCamera();
        camera.zoom = 1.5f; // Set camera zoom for a closer view


        Viewport viewport = new ScreenViewport(camera); // Create a viewport with the camera
        stage = new Stage(viewport, game.getSpriteBatch()); // Create a stage for UI elements

        Table table = new Table(); // Create a table for layout
        table.setFillParent(true); // Make the table fill the stage
        stage.addActor(table); // Add the table to the stage

        // Add a label as a title
        table.add(new Label("VICTORY", game.getSkin(), "title")).padBottom(80).row();

        TextButton restartGameButton = new TextButton("Restart", game.getSkin());
        table.add(restartGameButton).width(400).row();
        restartGameButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                PauseScreen.setReset(true);
                GameScreen.resetKeyInMazeData();
                game.goToGame();
            }
        });

        // Create and add a button to go to the game screen
        TextButton goToGameButton = new TextButton("Load New Map", game.getSkin());
        table.add(goToGameButton).width(400).row();
        goToGameButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                JFileChooser fileChooser = new JFileChooser("C:\\Users\\eshal\\IdeaProjects\\itp2324itp2324projectwork-fri2mu1nootnoot\\maps");

                int result = fileChooser.showOpenDialog(null);

                if (result == JFileChooser.APPROVE_OPTION) {
                    File selectedFile = fileChooser.getSelectedFile();
                    String path = selectedFile.getAbsolutePath();

                    GameScreen.loadMazeDataFromPropertiesFile(path);
                    PauseScreen.setReset(true);
                    GameScreen.resetKeyInMazeData();
                    game.goToGame();
                }

            }
        });

        // Create and add a button to go to the game screen
        TextButton exitButton = new TextButton("Exit Game", game.getSkin());
        table.add(exitButton).width(400).row();
        exitButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                Gdx.app.exit();
            }
        });
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT); // Clear the screen
        stage.act(Math.min(Gdx.graphics.getDeltaTime(), 1 / 30f)); // Update the stage
        stage.draw(); // Draw the stage
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