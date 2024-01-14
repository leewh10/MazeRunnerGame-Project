
package de.tum.cit.ase.maze;

        import com.badlogic.gdx.Gdx;
        import com.badlogic.gdx.Input;
        import com.badlogic.gdx.Screen;
        import com.badlogic.gdx.graphics.OrthographicCamera;
        import com.badlogic.gdx.graphics.g2d.Animation;
        import com.badlogic.gdx.graphics.g2d.BitmapFont;
        import com.badlogic.gdx.graphics.g2d.SpriteBatch;
        import com.badlogic.gdx.graphics.g2d.TextureRegion;
        import com.badlogic.gdx.utils.ScreenUtils;

        import java.lang.reflect.Field;

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
    private boolean isTextVisible;
    private boolean shouldMove;








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

        //starting position
        this.character = new Character(100, 100, false, 3, characterAnimation);

    }


    // Screen interface methods with necessary functionality
    @Override
    public void render(float delta) {

        // Check for escape key press to go back to the menu
        if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
            game.goToMenu();
        }

        ScreenUtils.clear(0, 0, 0, 1); // Clear the screen


        game.getSpriteBatch().begin();

        /**
         * creates background by repeating the block
         */
        for (int j = 0; j < 18; j++) {
            for (int i = 0; i < 27; i++) {
                game.getSpriteBatch().draw(game.getMapImageRegion(), i * 50, j*50, 50, 50);
            }
        }
        game.getSpriteBatch().end();




        camera.update(); // Update the camera position

        // Move text in a circular path to have an example of a moving object
        sinusInput += delta;
        float textX = (float) (camera.position.x + Math.sin(sinusInput) * 100);
        float textY = (float) (camera.position.y + Math.cos(sinusInput) * 100);


        /**
         * moves camera according to camera movement
         */
        camera.position.set(character.getX(), character.getY(), 0);


        // Set up and begin drawing with the sprite batch
        game.getSpriteBatch().setProjectionMatrix(camera.combined);
        game.getSpriteBatch().begin(); // Important to call this before drawing anything

        /**
         * boolean for the text showing
         */
        if(isTextVisible) {
            font.draw(game.getSpriteBatch(), "Press ESC to go to menu", textX, textY);
        }

        /**
         * All key controls to move the character
         */
        if (Gdx.input.isKeyPressed(Input.Keys.UP)
                || Gdx.input.isKeyPressed(Input.Keys.W)
                || Gdx.input.isKeyPressed(Input.Keys.DOWN)
                || Gdx.input.isKeyPressed(Input.Keys.S)
                || Gdx.input.isKeyPressed(Input.Keys.LEFT)
                || Gdx.input.isKeyPressed(Input.Keys.A)
                || Gdx.input.isKeyPressed(Input.Keys.RIGHT)
                || Gdx.input.isKeyPressed(Input.Keys.D)) {
            textX = (float) (camera.position.x);
            textY = (float) (camera.position.y);
            isTextVisible = false;
        } else {
            isTextVisible = true;
        }

        TextureRegion characterRegion;
        if (Gdx.input.isKeyPressed(Input.Keys.UP) || Gdx.input.isKeyPressed(Input.Keys.W)) {
            characterRegion = game.getCharacterUpAnimation().getKeyFrame(sinusInput, true);
            character.setY((int) (character.getY() + 5));
            shouldMove = true;
        } else if (Gdx.input.isKeyPressed(Input.Keys.DOWN) || Gdx.input.isKeyPressed(Input.Keys.S)) {
            characterRegion = game.getCharacterDownAnimation().getKeyFrame(sinusInput, true);
            character.setY((int) (character.getY() - 5));
            shouldMove = true;
        } else if (Gdx.input.isKeyPressed(Input.Keys.LEFT) || Gdx.input.isKeyPressed(Input.Keys.A)) {
            characterRegion = game.getCharacterLeftAnimation().getKeyFrame(sinusInput, true);
            character.setX((int) (character.getX() - 5));
            shouldMove = true;
        } else if (Gdx.input.isKeyPressed(Input.Keys.RIGHT) || Gdx.input.isKeyPressed(Input.Keys.D)) {
            characterRegion = game.getCharacterRightAnimation().getKeyFrame(sinusInput, true);
            character.setX((int) (character.getX() + 5));
            shouldMove = true;

        } else {

            /**
             * For the time between moving
             * After the game starts
             */

            if (!shouldMove){
                // Use a default frame when not moving, but before game starts
                characterRegion = game.getCharacterDownAnimation().getKeyFrame(sinusInput, true);
                isTextVisible = true;
            }
            else{
                // Use a default frame when not moving, after game starts
                characterRegion = game.getCharacterDownAnimation().getKeyFrame(0, false);
                textX = (float) (camera.position.x);   //makes te character stop moving in circles, refer to line 93
                textY = (float) (camera.position.y);   //makes te character stop moving in circles, refer to line 94
                isTextVisible = false;
            }
        }

        /**
         * creates the character
         */
        game.getSpriteBatch().draw(
                characterRegion,
                textX - 96,
                textY - 64,
                64,
                128
        );


        /**
         * Original Code
         * Creates our Character
         * Restricts it to only moving downwards
         */
        /*game.getSpriteBatch().draw(
            game.getCharacterDownAnimation().getKeyFrame(sinusInput, true),
            textX - 96,
            textY - 64,
            64,
            128 );
         */

        game.getSpriteBatch().end();

        // Set up and begin drawing with the sprite batch for the character
        batch.setProjectionMatrix(camera.combined);


        /**
         * from when we had 2 characters but this is now useless
         * we don't need to render character
         * just use characterRegion
         */
        /*batch.begin();
        // Render the character
        character.render(batch);
        batch.end();
        // Move the character based on key input
        character.move();
         */

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

