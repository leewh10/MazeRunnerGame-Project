package de.tum.cit.ase.maze;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.utils.viewport.Viewport;



/**
 * The MenuScreen class is responsible for displaying the main menu of the game.
 * It extends the LibGDX Screen class and sets up the UI components for the menu.
 */
public class InstructionScreen implements Screen {

    private final Stage stage;

    // Add the following Image field to hold the enemy image
    private Image enemyImage;
    private Image exitImage;
    private Image treasureImage;
    private Image leverImage;
    private Image moveableWallImage;
    private Image angelImage;

    /**
     * Constructor for MenuScreen. Sets up the camera, viewport, stage, and UI elements.
     *
     * @param game The main game class, used to access global resources and methods.
     */
    public InstructionScreen(MazeRunnerGame game) {
        var camera = new OrthographicCamera();
        camera.zoom = 1.5f; // Set camera zoom for a closer view


        Viewport viewport = new ScreenViewport(camera); // Create a viewport with the camera
        stage = new Stage(viewport, game.getSpriteBatch()); // Create a stage for UI elements

        Table table = new Table(); // Create a table for layout
        table.setFillParent(true); // Make the table fill the stage
        stage.addActor(table); // Add the table to the stage

        /**
         * The same concepts as HeartScreen is used
         * Creates a drawable from the original TextureRegion, then sets the drawables to the image regions
         * Using Images helps with playing the object at specific locations on the screen with reference to the text
         */
        enemyImage = new Image();
        exitImage = new Image();
        treasureImage = new Image();
        leverImage = new Image();
        moveableWallImage = new Image();
        angelImage = new Image();

        Label.LabelStyle labelStyle = new Label.LabelStyle(game.getSkin().get("bold", Label.LabelStyle.class));
        labelStyle.font.getData().setScale(1f);

        // Add a label as a title
        table.add(new Label("How to Play", game.getSkin(), "title")).padBottom(20).row();
        table.add(new Label("GOAL: Successfully ESCAPE the MAZE by opening the GATE", labelStyle)).padBottom(20);
        table.add(exitImage).padLeft(50).width(75).height(75).padBottom(20).row();
        table.add(new Label("To move your CHARACTER, use your ARROW or WASD key", game.getSkin(), "bold")).padBottom(20).row();
        table.add(new Label("Kill GHOSTS by pressing the move key and SPACE simultaneously", game.getSkin(), "bold")).padBottom(20).row();
        table.add(new Label("Find a TREASURE to help you open the GATE", game.getSkin(), "bold")).padBottom(20);
        table.add(treasureImage).padLeft(-300).width(75).height(75).padBottom(20).row();
        table.add(new Label("MOVEABLE WALLS must be opened using a LEVER", game.getSkin(), "bold")).padBottom(20);
        table.add(moveableWallImage).padLeft(-180).width(75).height(75).padBottom(20);
        table.add(leverImage).padLeft(-100).width(75).height(75).padBottom(20).row();
        table.add(new Label("BEWARE OF THE GHOSTS!", game.getSkin(), "bold")).padBottom(20);
        table.add(enemyImage).padLeft(-700).width(75).height(75).padBottom(20).row(); // Add ghost image next to the line "BEWARE OF GHOSTS"
        table.add(new Label("Kill 5 GHOSTS to get a LIFE!", game.getSkin(), "bold")).padBottom(20).row();
        table.add(new Label("If you see an ANGEL, go say hi!", game.getSkin(), "bold")).padBottom(20);
        table.add(angelImage).padLeft(-550).width(75).height(75).padBottom(20).row();
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


        TextureRegion enemyTextureRegion = Enemy.getEnemyImageRegion();
        TextureRegion exitTextureRegion = Exit.getExitPointImageRegion(); // Get the exit texture region
        TextureRegion treasureTextureRegion = Treasure.getTreasurePointImageRegion();
        TextureRegion leverTextureRegion = Lever.getLeverPointImageRegion();
        TextureRegion moveableWallTextureRegion = Wall.getMoveableWallImageRegion();
        TextureRegion angelTextureRegion = GuardianAngel.getAngelImageRegion();



        // Convert TextureRegions to Drawables
        Drawable enemyDrawable = new TextureRegionDrawable(enemyTextureRegion);
        Drawable exitDrawable = new TextureRegionDrawable(exitTextureRegion); // Convert exit texture region to Drawable
        Drawable treasureDrawable = new TextureRegionDrawable(treasureTextureRegion);
        Drawable leverDrawable = new TextureRegionDrawable(leverTextureRegion);
        Drawable moveableWallDrawable = new TextureRegionDrawable(moveableWallTextureRegion);
        Drawable angelDrawable = new TextureRegionDrawable(angelTextureRegion);



        // Set the drawables to the respective Image widgets
        enemyImage.setDrawable(enemyDrawable);
        exitImage.setDrawable(exitDrawable);
        treasureImage.setDrawable(treasureDrawable);
        leverImage.setDrawable(leverDrawable);
        moveableWallImage.setDrawable(moveableWallDrawable);
        angelImage.setDrawable(angelDrawable);
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