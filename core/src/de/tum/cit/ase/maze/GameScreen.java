
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

    private float sinusInput = 0f;
    private SpriteBatch batch;
    private static Character character;

    private float textX;
    private float textY;

    private int entryX;
    private int entryY;

    private static int wallX;
    private static int wallY;

    private static int[][] mazeData;
    private final BitmapFont font;
    private Enemy enemy;
    private float enemyX;
    private float enemyY;




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
        font = game.getSkin().getFont("font");


        Animation<TextureRegion> characterAnimation = Character.getCharacterDownAnimation();

        // Starting position - use the entry coordinates

        character = new Character(this,
                game,
                getEntryX(),
                getEntryY(),
                false,
                3,
                characterAnimation);
        camera.position.set(character.getX(), character.getY(), 0);
        camera.update();

        enemy = new Enemy(getEnemyX(),getEnemyY(),20f,1000,this);
        Enemy.loadEnemyAnimation();
    }


    public static void loadMazeDataFromPropertiesFile(int level) {

        Properties properties = new Properties();

        String filePath = "C:\\Users\\wonhy\\IdeaProjects\\itp2324itp2324projectwork-fri2mu1nootnoot\\maps\\level-" + level + ".properties";

        try (FileInputStream input = new FileInputStream(filePath)) {
            properties.load(input);

            // Assuming you have a fixed-size maze, adjust as needed
            int rows = 300;
            int cols = 300;
            mazeData = new int[rows * cols][3];

            // Populate mazeData array from properties
            for (int i = 0; i < rows; i++) {
                for (int j = 0; j < cols; j++) {
                    String key = i + "," + j;  // The key format should be i,j
                    int value = Integer.parseInt(properties.getProperty(key, "6"));
                    mazeData[i * cols + j] = new int[]{i, j, value};
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    // Screen interface methods with necessary functionality
    @Override
    public void render(float delta) {
        // Check for escape key press to go back to the menu
        if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
            game.goToMenu();
        }

        ScreenUtils.clear(0, 0, 0, 1); // Clear the screen

        game.getSpriteBatch().setProjectionMatrix(camera.combined);
        game.getSpriteBatch().begin();


        font.draw(game.getSpriteBatch(), "LIVES", camera.viewportWidth -5, camera.viewportHeight-5);


        ///////////////////////////////////////////////////////////////////////////////////////////////////////////////

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
                    wallX = (int) mazeX;
                    wallY = (int) mazeY;
                    game.getSpriteBatch().draw(Map.getWallImageRegion(), mazeX, mazeY, 50, 50);
                    break;
                case 1:
                    // Entry point
                    entryX = (int) mazeX;
                    entryY = (int) mazeY;
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
//                    game.getSpriteBatch().draw(Map.getEnemyImageRegion(), mazeX, mazeY, 50, 50);
//                    enemy.move(delta);
//                    game.getSpriteBatch().draw(
//                            enemy.render(delta),
//                            mazeX,
//                            mazeY,
//                            50,
//                            50
//                    );
//                    enemy.setX((int) mazeX);
//                    enemy.setY((int) mazeY);
//                    game.getSpriteBatch().draw(
//                            enemy.render(delta),
//                            enemy.getX(),
//                            enemy.getY(),
//                            50,
//                            50
//                    );
//                    enemy.move(delta);
//                    game.getSpriteBatch().draw(
//                            enemy.render(delta),
//                            getEnemyX(),
//                            getEnemyY(),
//                            50,
//                            50
//                    );
//                    enemy.move(delta);
////
//                    game.getSpriteBatch().draw(
//                            enemy.render(delta),
//                            mazeX,
//                            mazeY,
//                            50,
//                            50
//                    );
//                    enemy.move(delta);
//
//                    //ENEMY MOVES WITH THE CODE BELOW (DOESN'T START AT THE RIGHT POSITIONS THOUGH)
                    game.getSpriteBatch().draw(
                            enemy.render(delta),
                            enemy.getX() - 96,
                            enemy.getY() - 64,
                            50,
                            50
                    );
                    break;
                case 5:
                    // Key
                    game.getSpriteBatch().draw(Map.getKeyImageRegion(), mazeX, mazeY, 50, 50);
                    break;
                case 6:
                    // Floor
                    game.getSpriteBatch().draw(Map.getFloorImageRegion(), mazeX, mazeY, 50, 50);
                    break;
            }
        }

        //////////////////////////////////////////////////////////////////////////////////////////////////////////////

        game.getSpriteBatch().end();
        camera.update(); // Update the camera position

        // Move text in a circular path to have an example of a moving object
        sinusInput += delta;
        textX = (float) (entryX + 50 + Math.sin(sinusInput) * 100);
        textY = (float) (entryY + 50 + Math.cos(sinusInput) * 100);


        /**
         * moves camera according to camera movement
         */
        camera.position.set(character.getX(), character.getY(), 0);


        // Set up and begin drawing with the sprite batch
        game.getSpriteBatch().setProjectionMatrix(camera.combined);
        game.getSpriteBatch().begin(); // Important to call this before drawing anything

    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////

        /**
         * character movement commands
         */
        character.move();

        if (!character.isShouldMove()){
            // Use a default frame when not moving, but before game starts
            character.setX(entryX);
            character.setY(entryY);
            character.setCharacterRegion(Character.getCharacterDownAnimation().getKeyFrame(sinusInput, true));
            character.setTextVisible(true);
        }
        else{
            // Use a default frame when not moving, after game starts
            textX = camera.position.x;   //makes te character stop moving in circles, refer to line 93
            textY = camera.position.y;   //makes te character stop moving in circles, refer to line 94
            character.setTextVisible(false);
        }
        /**
         * creates the character
         */
        game.getSpriteBatch().draw(
                character.getCharacterRegion(), //character.getCharacterDownAnimation().getKeyFrame(sinusInput, true)
                textX + 50,   // -96
                textY,   // -64
                64,
                128
        );

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////


        game.getSpriteBatch().end();
        batch.setProjectionMatrix(camera.combined);

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

    public float getEntryX() {
        return entryX;
    }

    public float getEntryY() {
        return entryY;
    }

    public static int getWallX() {
        return wallX;
    }

    public static int getWallY() {
        return wallY;
    }

    public Enemy getEnemy() {
        return enemy;
    }

    public void setEnemy(Enemy enemy) {
        this.enemy = enemy;
    }

    public float getEnemyX() {
        return enemyX;
    }

    public void setEnemyX(float enemyX) {
        this.enemyX = enemyX;
    }

    public float getEnemyY() {
        return enemyY;
    }

    public void setEnemyY(float enemyY) {
        this.enemyY = enemyY;
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

