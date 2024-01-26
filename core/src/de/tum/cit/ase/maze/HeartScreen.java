package de.tum.cit.ase.maze;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

public class HeartScreen implements Screen {
    private final Stage stage;
    private final Image heartImage;
    private final MazeRunnerGame game;
    private float elapsedTime;

    public HeartScreen(MazeRunnerGame game) {
        this.game = game;  // Store the MazeRunnerGame instance

        var camera = new OrthographicCamera();
        camera.zoom = 1.5f; // Set camera zoom for a closer view

        Viewport viewport = new ScreenViewport(camera); // Create a viewport with the camera
        stage = new Stage(viewport, game.getSpriteBatch()); // Create a stage for UI elements

        Table table = new Table(); // Create a table for layout
        table.setFillParent(true); // Make the table fill the stage
        stage.addActor(table); // Add the table to the stage

        Heart.loadHeartAnimation();

        // Create an Image widget to display the heart animation
        heartImage = new Image();

        // Add the Image widget to the table
        table.add(heartImage).padBottom(80).row();

        elapsedTime = 0;
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT); // Clear the screen
        stage.act(Math.min(Gdx.graphics.getDeltaTime(), 1 / 30f)); // Update the stage
        stage.draw(); // Draw the stage

        // Get the TextureRegion from Heart
        TextureRegion heartTextureRegion = Heart.renderHeart();

        // Convert TextureRegion to Drawable
        Drawable drawable = new TextureRegionDrawable(heartTextureRegion);

        // Set the drawable to the Image widget
        heartImage.setDrawable(drawable);

        // Increment elapsed time
        elapsedTime += delta;

        // Check if 3 seconds have passed, then transition to the game screen
        if (elapsedTime >= 1) {
            game.goToGame();
        }
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
        // Dispose of the stage when the screen is disposed
        stage.dispose();
    }

    @Override
    public void show() {
        // Set the input processor so the stage can receive input events
        Gdx.input.setInputProcessor(stage);
    }

}
