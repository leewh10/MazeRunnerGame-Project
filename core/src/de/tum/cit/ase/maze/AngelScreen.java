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


public class AngelScreen implements Screen {
    private final Stage stage;
    private static Music backgroundMusic;

    /**
     * Constructor for AngelScreen
     * Sets up the camera, viewport, stage, and UI elements.
     * AngelScreen appears after the Character collides with the angel
     * Serves as an informative screen and offers the character a life
     * @param game
     * @param gameScreen
     */
    public AngelScreen(MazeRunnerGame game, GameScreen gameScreen) {

        /**
         *https://www.youtube.com/watch?v=dSRA3ngRoW8&ab_channel=SoundFX
         * Angel - Sound Effect (HD) by Sound FX on YouTube
         */
        backgroundMusic = Gdx.audio.newMusic(Gdx.files.internal("AngelSound.mp3"));
        backgroundMusic.setLooping(true);
        backgroundMusic.play();

        var camera = new OrthographicCamera();
        camera.zoom = 1.5f; // Set camera zoom for a closer view

        Viewport viewport = new ScreenViewport(camera); // Create a viewport with the camera
        stage = new Stage(viewport, game.getSpriteBatch()); // Create a stage for UI elements

        Table table = new Table(); // Create a table for layout
        table.setFillParent(true); // Make the table fill the stage
        stage.addActor(table); // Add the table to the stage

        /**
         * Creates an Image that can be added to the screen from the ImageRegion then adds it to the screen
         */
        Image angelImage = new Image(GuardianAngel.getDefaultAngelImageRegion());
        table.add(angelImage).width(300).height(420).padBottom(80).row();

        table.add(new Label("Hello! I'm Stephanette Kruschette! I'm your Guardian Angel!", game.getSkin(),"bold")).padBottom(80).row();
        table.add(new Label("To help you on your journey, I bless you with one LIFE!!", game.getSkin(),"bold")).padBottom(80).row();

        TextButton resumeGameButton = new TextButton("(kindly) Get a LIFE!", game.getSkin());
        table.add(resumeGameButton).width(400).row();
        resumeGameButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                PauseScreen.setReset(false);
                game.goToHeartScreen();
            }
        });

    }

    public static Music getBackgroundMusic() {
        return backgroundMusic;
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