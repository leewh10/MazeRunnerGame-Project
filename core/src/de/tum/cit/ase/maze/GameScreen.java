
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

        import java.io.FileInputStream;
        import java.io.IOException;
        import java.util.Properties;

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

    private float textX;
    private float textY;

    private Map map;
    private static int[][] mazeData;



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

        Animation<TextureRegion> characterAnimation = Character.getCharacterDownAnimation();

        //starting position
        this.character = new Character(this,game,1, 1, false, 3, characterAnimation);

        loadMazeDataFromPropertiesFile();

    }
    public static void loadMazeDataFromPropertiesFile() {
        Properties properties = new Properties();

        try (FileInputStream input = new FileInputStream("C:\\Users\\eshal\\IdeaProjects\\itp2324itp2324projectwork-fri2mu1nootnoot\\maps\\level-1.properties")) {
            properties.load(input);

            // Assuming you have a fixed-size maze, adjust as needed
            int rows = 15;
            int cols = 15;
            mazeData = new int[rows * cols][3];

            // Populate mazeData array from properties
            for (int i = 0; i < rows; i++) {
                for (int j = 0; j < cols; j++) {
                    String key = i + "," + j;  // The key format should be i,j
                    int value = Integer.parseInt(properties.getProperty(key, "6"));
                    mazeData[i * cols + j] = new int[]{i, j, value};
                }
            }

            mazeData = mazeData;

        } catch (IOException e) {
            e.printStackTrace();
        }
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







        for (int i = 0; i < mazeData.length; i++) {
            int x = mazeData[i][0];
            int y = mazeData[i][1];
            int variable = mazeData[i][2];


            // Calculate the actual position on the screen
            float mazeX = x * 50;
            float mazeY = y * 50;

            switch (variable) {
                case 0:
                    // Wall

                    game.getSpriteBatch().draw(Map.getWallImageRegion(), mazeX, mazeY, 50, 50);
                    break;
                case 1:
                    // Entry point
                    game.getSpriteBatch().draw(Map.getEntryPointImageRegion(), mazeX, mazeY, 50, 50);
                    break;
                case 2:
                    // Exit
                    game.getSpriteBatch().draw(Map.getExitPointImageRegion(), mazeX, mazeY, 50, 50);
                    break;
                case 3:
                    // Trap (static obstacle)
                    game.getSpriteBatch().draw(Map.getTrapImageRegion(), mazeX, mazeY, 50, 50);
                    break;
                case 4:
                    // Enemy (dynamic obstacle)
                    game.getSpriteBatch().draw(Map.getEnemyImageRegion(), mazeX, mazeY, 50, 50);
                    break;
                case 5:
                    // Key
                    game.getSpriteBatch().draw(Map.getKeyImageRegion(), mazeX, mazeY, 50, 50);
                    break;
                case 6:
                    // Key
                    game.getSpriteBatch().draw(Map.getFloorImageRegion(), mazeX, mazeY, 50, 50);
                    break;
            }
        }











        game.getSpriteBatch().end();


        camera.update(); // Update the camera position

        // Move text in a circular path to have an example of a moving object
        sinusInput += delta;
        textX = (float) (camera.position.x + Math.sin(sinusInput) * 100);
        textY = (float) (camera.position.y + Math.cos(sinusInput) * 100);


        /**
         * moves camera according to camera movement
         */
        camera.position.set(character.getX(), character.getY(), 0);


        // Set up and begin drawing with the sprite batch
        game.getSpriteBatch().setProjectionMatrix(camera.combined);
        game.getSpriteBatch().begin(); // Important to call this before drawing anything


        /**
         * character movement commands
         */
        character.move();


        /**
         * For the time between moving
         * After the game starts
         */

        if (!character.isShouldMove()){
            // Use a default frame when not moving, but before game starts
            character.setCharacterRegion(Character.getCharacterDownAnimation().getKeyFrame(sinusInput, true));
            character.setTextVisible(true);
        }
        else{
            // Use a default frame when not moving, after game starts
            //character.setCharacterRegion(Character.getCharacterDownAnimation().getKeyFrame(0, false));
            textX = camera.position.x;   //makes te character stop moving in circles, refer to line 93
            textY = camera.position.y;   //makes te character stop moving in circles, refer to line 94
            character.setTextVisible(false);
        }

        /**
         * creates the character
         */
        game.getSpriteBatch().draw(
                character.getCharacterRegion(),
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
            character.getCharacterDownAnimation().getKeyFrame(sinusInput, true),
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



    public float getSinusInput() {
        return sinusInput;
    }

    public float getTextX() {
        return textX;
    }

    public float getTextY() {
        return textY;
    }

    public void setTextX(float textX) {
        this.textX = textX;
    }

    public void setTextY(float textY) {
        this.textY = textY;
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

