package de.tum.cit.ase.maze;

import com.badlogic.gdx.*;
import com.badlogic.gdx.audio.Music;
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

    private static MazeRunnerGame game;
    private OrthographicCamera camera;
    public InputProcessor stage;

    private static float sinusInput = 0f;
    private SpriteBatch batch;
    private static Character character;

    private float textX;
    private float textY;

    private static int entryX;
    private static int entryY;

    private static int wallX;
    private static int wallY;

    private static int[][] mazeData;
    private BitmapFont font;
    private Enemy enemy;
    private float enemyX;
    private float enemyY;
    private long exitTime;
    private boolean showVictoryScreen = false;
    private float exitAnimationDuration = 5f; //


    private int currentLevel;

    public int getCurrentLevel() {
        return currentLevel;
    }


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
        camera.zoom = 0.80f;
        font = game.getSkin().getFont("font");


        Animation<TextureRegion> characterAnimation = Character.getCharacterDownAnimation();

        // Starting position - use the entry coordinates
        if (character == null || PauseScreen.isReset()) {
            character = new Character(this, game, getEntryX(), getEntryY(), false, 5, characterAnimation);
            camera.position.set(character.getX(), character.getY(), 0);
            camera.update();
        }

        camera.position.set(character.getX(), character.getY(), 0);
        camera.update();

        enemy = new Enemy(getEnemyX(),getEnemyY(),5f,50,this);
        Enemy.loadEnemyAnimation(getCurrentLevel());

        GameMap.lifeImageAnimation();
        GameMap.exitImageAnimation();
        GameMap.trapImageAnimation();
    }


    public static void loadMazeDataFromPropertiesFile(String filePath) {

        Properties properties = new Properties();

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
        if(Gdx.input.isKeyPressed(Input.Keys.PERIOD)) {
            camera.zoom += 0.01f;
        }
        if(Gdx.input.isKeyPressed(Input.Keys.COMMA)) {
            camera.zoom -= 0.01f;
        }

        // Check for escape key press to go back to the menu
        if (Gdx.input.isKeyJustPressed(Input.Keys.SPACE) || Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
            game.goToPauseScreen();
        }

        if(character.getLives() <= 0) {
            game.goToGameOverScreen();
            Music backgroundMusic = Gdx.audio.newMusic(Gdx.files.internal("SpongeBobFail.mp3"));
            backgroundMusic.setLooping(false);
            backgroundMusic.play();
        }

        /**
         * checks for the key then allows the character to leave the game;
         */


        ScreenUtils.clear(0, 0, 0, 1); // Clear the screen

        game.getSpriteBatch().setProjectionMatrix(camera.combined);
        game.getSpriteBatch().begin();


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
                    game.getSpriteBatch().draw(GameMap.getWallImageRegion(), mazeX, mazeY, 50, 50);
                    break;
                case 1:
                    // Entry point
                    entryX = (int) mazeX;
                    entryY = (int) mazeY;
                    game.getSpriteBatch().draw(GameMap.getEntryPointImageRegion(), mazeX, mazeY, 50, 50);
                    break;
                case 2:
                    if (character.isHasKey()) {
                        if (character.getX() < mazeX + 25
                                && character.getX() + 36 > mazeX
                                && character.getY() < mazeY + 25
                                && character.getY() + 31 > mazeY) {

                            game.goToVictoryScreen();

                        } else if (character.getX() < mazeX + 250 && character.getX() + 36 > mazeX - 200 &&
                                character.getY() < mazeY + 250 && character.getY() + 31 > mazeY - 200) {
                            game.getSpriteBatch().draw(
                                    GameMap.renderExit(),
                                    mazeX,
                                    mazeY,
                                    50,
                                    50
                            );
                        } else {
                        // Draw the static exit point image when the character is not at the exit
                        game.getSpriteBatch().draw(GameMap.getExitPointImageRegion(), mazeX, mazeY, 50, 50);
                    }
                    }else {
                        // Draw the static exit point image when the character is not at the exit
                        game.getSpriteBatch().draw(GameMap.getExitPointImageRegion(), mazeX, mazeY, 50, 50);
                    }

                    break;
                case 3:
                    // Trap (static obstacle)
                    float trapX = mazeX;
                    float trapY = mazeY;
                    game.getSpriteBatch().draw(GameMap.renderTrap(),mazeX,mazeY,50,50);

                    if (character.collidesWithTrap(trapX, trapY)) {
                        Music backgroundMusic = Gdx.audio.newMusic(Gdx.files.internal("LoseLife.mp3"));
                        backgroundMusic.setLooping(false);
                        backgroundMusic.play();
                        character.setLives(character.getLives() - 1);

                    }
                    game.getSpriteBatch().draw(GameMap.getTrapImageRegion(), mazeX, mazeY, 50, 50);

                    break;
                case 4:
                    // Enemy (dynamic obstacle)
                    enemy.move(delta);
                    float enemyX1 = mazeX + enemy.getX();
                    float enemyY1 = mazeY + enemy.getY();

                        game.getSpriteBatch().draw(
                                enemy.render(delta),
                                enemyX1,
                                enemyY1,
                                50,
                                50
                        );


                    if (character.collidesWithEnemy(enemyX1, enemyY1)) {
                        Music backgroundMusic = Gdx.audio.newMusic(Gdx.files.internal("LoseLife.mp3"));
                        backgroundMusic.setLooping(false);
                        backgroundMusic.play();
                        character.setLives(character.getLives() - 1);
                    }
                    break;
                case 5:
                    // Key
                    float keyX = mazeX;
                    float keyY = mazeY;

                    // Check for collision between character and key
                    if (character.collidesWithKey(keyX, keyY)) {
                        character.setHasKey(true);  // Update character's hasKey status
                        // Remove key from maze data
                        removeKeyFromMazeData(keyX, keyY);
                    } else {
                        game.getSpriteBatch().draw(GameMap.getKeyImageRegion(), mazeX, mazeY, 50, 50);
                    }
                    break;
                default:
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
         * Character's Lives
         */
        float lifeTextX = camera.position.x - camera.viewportWidth / 10 - 500;
        float lifeTextY = camera.position.y + camera.viewportHeight / 10 + 300;
//        Map.renderLives(game.getSpriteBatch(), delta, camera.position.x + 140, camera.position.y + 70, character.getLives());
        GameMap.renderLives(game.getSpriteBatch(), delta, camera.position.x, camera.position.y, character.getLives());
        font.draw(game.getSpriteBatch(), "Lives: " + character.getLives(), lifeTextX, lifeTextY);

//        Map.lifeImageAnimation();

        float keyTextX = camera.position.x - camera.viewportWidth / 10 - 550;
        float keyTextY = camera.position.y + camera.viewportHeight / 10 + 340;

        if (character.isHasKey()) {
            font.draw(game.getSpriteBatch(), "Key Collected", keyTextX, keyTextY);
        } else {
            font.draw(game.getSpriteBatch(), "No Key", keyTextX, keyTextY);
        }

        /**
         * character movement commands
         */
        character.move();

        if (!character.isShouldMove()){
            // Use a default frame when not moving, but before game starts
            character.setX(entryX + 100);
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
        if(character.isKeyPressed()){
            game.getSpriteBatch().draw(
                    Character.getCurrentAnimation().getKeyFrame(sinusInput, true),
                    textX,   // -96
                    textY,   // -64
                    64,
                    128
            );
        }
        else if (!character.isKeyPressed()){
            game.getSpriteBatch().draw(
                    Character.getCurrentAnimation().getKeyFrame(3, true),
                    textX,   // -96
                    textY,   // -64
                    64,
                    128
            );
        }

        ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////



        game.getSpriteBatch().end();
        batch.setProjectionMatrix(camera.combined);

    }
    private void removeKeyFromMazeData(float x, float y) {
        for (int i = 0; i < mazeData.length; i++) {
            if (mazeData[i][2] == 5) {
                mazeData[i][2] = 7;  // Set variable to 0 to remove key from rendering
                break;
            }
        }
    }

    public static void resetKeyInMazeData() {
        for (int i = 0; i < mazeData.length; i++) {
            if (mazeData[i][2] == 7) {
                mazeData[i][2] = 5; // Reset key value to 5
                break;
            }
        }
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

    public static float getEntryX() {
        return entryX;
    }

    public static float getEntryY() {
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

    public static int[][] getMazeData() {
        return mazeData;
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