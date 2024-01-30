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

public class KeyScreen implements Screen {
    private final Stage stage;
    private final Image keyImage;
    private final MazeRunnerGame game;

    /**
     * Constructor for KeyScreen
     * KeyScreen appears when the TreasureScreen is done rendering
     * Allows the player to continue the game after informing them they must collect the Key
     * Serves as an informative Screen
     * @param game
     */
    public KeyScreen(MazeRunnerGame game){
        this.game = game;  // Store the MazeRunnerGame instance

        var camera = new OrthographicCamera();
        camera.zoom = 1.5f; // Set camera zoom for a closer view

        Viewport viewport = new ScreenViewport(camera); // Create a viewport with the camera
        stage = new Stage(viewport, game.getSpriteBatch()); // Create a stage for UI elements

        Table table = new Table(); // Create a table for layout
        table.setFillParent(true); // Make the table fill the stage
        stage.addActor(table); // Add the table to the stage

        Key.getKeyAnimation();

        keyImage = new Image();

        Label.LabelStyle labelStyle = new Label.LabelStyle(game.getSkin().get("bold", Label.LabelStyle.class));
        labelStyle.font.getData().setScale(2f);
        Label label = new Label("Collect this KEY to escape the maze!", labelStyle);
        table.add(label).padBottom(80).row();

        // Add the Image widget to the table
        table.add(keyImage).width(600).height(600).padBottom(80).row();

        TextButton resumeGameButton = new TextButton("Continue", game.getSkin());
        table.add(resumeGameButton).width(400).row();
        resumeGameButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                PauseScreen.setReset(false);
                TreasureScreen.getBackgroundMusic().stop();
                game.goToGame();
            }
        });
    }


    @Override
    public void render(float delta) {
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT); // Clear the screen
        stage.act(Math.min(Gdx.graphics.getDeltaTime(), 1 / 30f)); // Update the stage
        stage.draw(); // Draw the stage

        // Get the TextureRegion from Heart
        TextureRegion keyTextureRegion = Key.renderTexture();

        // Convert TextureRegion to Drawable
        Drawable drawable = new TextureRegionDrawable(keyTextureRegion);

        // Set the drawable to the Image widget
        keyImage.setDrawable(drawable);
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
