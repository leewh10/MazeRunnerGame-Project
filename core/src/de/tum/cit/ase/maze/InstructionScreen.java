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

public class InstructionScreen implements Screen {

    private final Stage stage;

    /**
     * This Screen follows the InstructionScreen1 and is specifically used to inform the player on what key to press to
     * Sets up the camera, viewport, stage, and UI elements.
     * play the game
     * @param game
     */
    public InstructionScreen(MazeRunnerGame game) {
        var camera = new OrthographicCamera();
        camera.zoom = 1.5f; // Set camera zoom for a closer view


        Viewport viewport = new ScreenViewport(camera); // Create a viewport with the camera
        stage = new Stage(viewport, game.getSpriteBatch()); // Create a stage for UI elements

        Table table = new Table(); // Create a table for layout
        table.setFillParent(true); // Make the table fill the stage
        stage.addActor(table); // Add the table to the stage

        Label.LabelStyle labelStyle = new Label.LabelStyle(game.getSkin().get("bold", Label.LabelStyle.class));
        labelStyle.font.getData().setScale(1.1f);

        // Add a label as a title
        table.add(new Label("Key Commands", game.getSkin(), "title")).padBottom(20).row();

        table.add(new Label("To move your CHARACTER, use your ARROW or WASD key.", game.getSkin(), "bold")).padBottom(20).row();
        table.add(new Label("", game.getSkin(), "bold")).padBottom(20).row(); //Add a space between each row
        table.add(new Label("Kill GHOSTS by pressing any move key and SPACE simultaneously", game.getSkin(), "bold")).padBottom(20).row();
        table.add(new Label("", game.getSkin(), "bold")).padBottom(20).row();
        table.add(new Label("To interact with Game Objects or Map Objects, stand on top of them", game.getSkin(), "bold")).padBottom(20).row();
        table.add(new Label("", game.getSkin(), "bold")).padBottom(20).row();
        table.add(new Label("Press PERIOD to zoom out. Press COMMA to zoom in.", game.getSkin(), "bold")).padBottom(20).row();
        table.add(new Label("", game.getSkin(), "bold")).padBottom(20).row();
        table.add(new Label("Hint: Kill 5 GHOSTS to get a LIFE!", game.getSkin(), "bold")).padBottom(20).row();
        table.add(new Label("", game.getSkin(), "bold")).padBottom(20).row();
        table.add(new Label("Hint: If you go to the TREASURE or the TREE OF GOOD AND EVIL, the GRIM REAPER resets", game.getSkin(), "bold")).padBottom(20).row();
        table.add(new Label("", game.getSkin(), "bold")).padBottom(20).row();

        table.add(new Label("GOOD LUCK!", game.getSkin(), "bold")).padBottom(50).row();


        TextButton goToGameButton = new TextButton("Load Map", game.getSkin());
        table.add(goToGameButton).width(300).row();
        goToGameButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                PauseScreen.setReset(true);
                GameScreen.setPaused(false);
                game.openFileChooser();
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
    public void dispose() {
        // Dispose of the stage when screen is disposed
        stage.dispose();
    }

    @Override
    public void show() {
        // Set the input processor so the stage can receive input events
        Gdx.input.setInputProcessor(stage);
    }

    // The following methods are part of the Screen interface but are not used in this screen.
    @Override
    public void pause() {
    }

    @Override
    public void resume() {
    }

    @Override
    public void hide() {
    }

}