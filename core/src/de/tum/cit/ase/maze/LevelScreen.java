package de.tum.cit.ase.maze;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;


public class LevelScreen implements Screen {
    private final Stage stage;
    private static int selectedLevel;


    public LevelScreen(MazeRunnerGame game) {
        var camera = new OrthographicCamera();
        camera.zoom = 1.5f; // Set camera zoom for a closer view

        Viewport viewport = new ScreenViewport(camera); // Create a viewport with the camera
        stage = new Stage(viewport, game.getSpriteBatch()); // Create a stage for UI elements

        Table table = new Table(); // Create a table for layout
        table.setFillParent(true); // Make the table fill the stage
        stage.addActor(table); // Add the table to the stage

        // Add a label as a title
        table.add(new Label("Choose your Level!", game.getSkin(), "title")).padBottom(80).row();

        // Create and add a button to go to the Level 1
        TextButton goToLevel1 = new TextButton("Level 1", game.getSkin());
        table.add(goToLevel1).width(300).row();
        goToLevel1.addListener(new ChangeListener() {
            Properties properties = new Properties();
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                GameScreen.loadMazeDataFromPropertiesFile(1);
                game.goToGame();
            }
        });



        // Create and add a button to go to the Level 2
        TextButton goToLevel2 = new TextButton("Level 2", game.getSkin());
        table.add(goToLevel2).width(300).row();
        goToLevel2.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                GameScreen.loadMazeDataFromPropertiesFile(2);
                game.goToGame();
            }

        });



        // Create and add a button to go to the Level 3
        TextButton goToLevel3 = new TextButton("Level 3", game.getSkin());
        table.add(goToLevel3).width(300).row();
        goToLevel3.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                GameScreen.loadMazeDataFromPropertiesFile(3);
                game.goToGame();
            }
        });



        // Create and add a button to go to the Level 4
        TextButton goToLevel4 = new TextButton("Level 4", game.getSkin());
        table.add(goToLevel4).width(300).row();
        goToLevel4.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                GameScreen.loadMazeDataFromPropertiesFile(4);
                game.goToGame(); // Change to the game screen when button is pressed
            }
        });

        // Create and add a button to go to the Level 5
        TextButton goToLevel5 = new TextButton("Level 5", game.getSkin());
        table.add(goToLevel5).width(300).row();
        goToLevel5.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                GameScreen.loadMazeDataFromPropertiesFile(5);
                game.goToGame(); // Change to the game screen when button is pressed
            }
        });
    }

    public static int getSelectedLevel() {
        return selectedLevel;
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