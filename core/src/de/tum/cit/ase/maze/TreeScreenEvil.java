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


public class TreeScreenEvil implements Screen {
    private final Stage stage;

    /**
     * Constructor for TreeScreenEvil
     * Sets up the camera, viewport, stage, and UI elements.
     * TreeScreenEvil appears if the Character collides with the Tree and the random boolean generated is true
     * @param game
     */
    public TreeScreenEvil(MazeRunnerGame game) {

        var camera = new OrthographicCamera();
        camera.zoom = 1.5f; // Set camera zoom for a closer view

        Viewport viewport = new ScreenViewport(camera); // Create a viewport with the camera
        stage = new Stage(viewport, game.getSpriteBatch()); // Create a stage for UI elements

        /**
         * https://www.youtube.com/watch?si=yYTXOdGTnNVzy2EY&v=kIGOSewuZdA&feature=youtu.be
         * Gorgeous Fantasy Fairy Tale Music Magical Royalty Free Background Music No Copyright by NO COPYRIGHT PLAY on YouTube
         */
        Music treeMusic = Gdx.audio.newMusic(Gdx.files.internal("Tree.mp3"));
        treeMusic.setLooping(true);
        treeMusic.setVolume(0.1f);
        treeMusic.play();

        Table table = new Table(); // Create a table for layout
        table.setFillParent(true); // Make the table fill the stage
        stage.addActor(table); // Add the table to the stage

        Image treeImage = new Image(Tree.getTreeImageRegion());
        table.add(treeImage).width(300).height(420).padBottom(80).row();

        table.add(new Label("This is the Tree of GOOD and EVIL", game.getSkin(),"title")).padBottom(80).row();
        table.add(new Label("Unfortunately you got EVIL", game.getSkin(),"bold")).padBottom(80).row();
        table.add(new Label("From now, you will only see enimies when they are close to you.", game.getSkin(),"bold")).padBottom(80).row();

        TextButton resumeGameButton = new TextButton("Continue", game.getSkin());
        table.add(resumeGameButton).width(400).row();
        resumeGameButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                PauseScreen.setReset(false);
                treeMusic.stop();
                game.goToGame();

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