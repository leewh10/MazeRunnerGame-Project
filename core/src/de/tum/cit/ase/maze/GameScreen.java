
package de.tum.cit.ase.maze;

        import com.badlogic.gdx.Gdx;
        import com.badlogic.gdx.Input;
        import com.badlogic.gdx.Screen;
        import com.badlogic.gdx.graphics.OrthographicCamera;
        import com.badlogic.gdx.graphics.Texture;
        import com.badlogic.gdx.graphics.g2d.Animation;
        import com.badlogic.gdx.graphics.g2d.BitmapFont;
        import com.badlogic.gdx.graphics.g2d.SpriteBatch;
        import com.badlogic.gdx.graphics.g2d.TextureRegion;
        import com.badlogic.gdx.utils.ScreenUtils;

/**
 * The GameScreen class is responsible for rendering the gameplay screen.
 * It handles the game logic and rendering of the game elements.
 */
public class GameScreen implements Screen {

    private final MazeRunnerGame game;
    private final OrthographicCamera camera;
    private final BitmapFont font;

    private float sinusInput = 0f;
    private SpriteBatch batch;
    private Character character;












    /**
     * Constructor for GameScreen. Sets up the camera and font.
     *
     * @param game The main game class, used to access global resources and methods.
     */
    public GameScreen(MazeRunnerGame game) {
        this.game = game;

        // Create and configure the camera for the game view
        camera = new OrthographicCamera();
        camera.setToOrtho(true);
        camera.zoom = 0.75f;

        // Get the font from the game's skin
        font = game.getSkin().getFont("font");

        Animation<TextureRegion> characterAnimation = game.getCharacterDownAnimation();

        this.character = new Character(100, 100, false, 3, characterAnimation);
    }



    // Screen interface methods with necessary functionality
    @Override
    public void render(float delta) {
        // Check for escape key press to go back to the menu
        if (Gdx.input.isKeyJustPressed(Input.Keys.BACKSPACE)) {
            game.dispose1();
        }


        if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
            game.goToMenu();
        }

        ScreenUtils.clear(0, 0, 0, 1); // Clear the screen




        //camera stuff
        game.getSpriteBatch().begin();
        game.getSpriteBatch().draw(game.getBackgroundTexture(), 0, 0, 500, 600);
        game.getSpriteBatch().draw(game.getBackgroundTexture(), 500, 0, 500, 600);
        game.getSpriteBatch().draw(game.getBackgroundTexture(), 1000, 0, 500, 600);

        game.getSpriteBatch().end();





        camera.update(); // Update the camera

        // Move text in a circular path to have an example of a moving object
        sinusInput += delta;
        float textX = (float) (camera.position.x + Math.sin(sinusInput) * 100);
        float textY = (float) (camera.position.y + Math.cos(sinusInput) * 100);

        // Set up and begin drawing with the sprite batch
        game.getSpriteBatch().setProjectionMatrix(camera.combined);
        game.getSpriteBatch().begin(); // Important to call this before drawing anything

        // Render the text


        font.draw(game.getSpriteBatch(), "Press ESC to go to menu", textX, textY);

        TextureRegion characterRegion;
        if (Gdx.input.isKeyPressed(Input.Keys.UP)) {
            characterRegion = game.getCharacterUpAnimation().getKeyFrame(sinusInput, true);
            character.setY(character.getY() + 5);

        } else if (Gdx.input.isKeyPressed(Input.Keys.DOWN)) {
            characterRegion = game.getCharacterDownAnimation().getKeyFrame(sinusInput, true);
            character.setY(character.getY() - 5);


        } else if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
            characterRegion = game.getCharacterLeftAnimation().getKeyFrame(sinusInput, true);
            character.setX(character.getX() - 5);


        } else if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
            characterRegion = game.getCharacterRightAnimation().getKeyFrame(sinusInput, true);
            character.setX(character.getX() + 5);

        } else {
            // Use a default frame when not moving
            characterRegion = game.getCharacterDownAnimation().getKeyFrame(0, true);

        }

        game.getSpriteBatch().draw(
                characterRegion,
                textX - 96,
                textY - 64,
                64,
                128
        );

    /*game.getSpriteBatch().draw(
            game.getCharacterDownAnimation().getKeyFrame(sinusInput, true),
            textX - 96,
            textY - 64,
            64,
            128


            OG
    );
     */

        game.getSpriteBatch().end();

        // Set up and begin drawing with the sprite batch for the character
        batch.setProjectionMatrix(camera.combined);

        batch.begin();

        // Render the character
        character.render(batch);

        batch.end();

        // Move the character based on key input
        character.move();

    }

    @Override
    public void resize(int width, int height) {
        camera.setToOrtho(false, width, height);
    }

    @Override
    public void pause() {
    }

    @Override
    public void resume() {
    }

    @Override
    public void show() {




        batch = new SpriteBatch();





    }

    @Override
    public void hide() {
    }

    @Override
    public void dispose() {

    }

    // Additional methods and logic can be added as needed for the game screen
}

