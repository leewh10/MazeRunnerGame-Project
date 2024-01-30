package de.tum.cit.ase.maze;

import com.badlogic.gdx.*;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.ScreenUtils;

import java.awt.*;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;
import java.util.List;

/**
 * The GameScreen class is responsible for rendering the gameplay screen.
 * It handles the game logic and rendering of the game elements.
 */
public class GameScreen implements Screen {

    private static MazeRunnerGame game;
    private OrthographicCamera camera;
    private static float sinusInput = 0f;
    private SpriteBatch batch;
    private static Character character;
    private float textX;
    private float textY;
    private BitmapFont font;

    /**
     * mazeArray converts the data from the .properties file into a two-dimensional array
     */
    private static int[][] mazeArray;

    private static int entryX;
    private static int entryY;
    private static int wallX;
    private static int wallY;
    private static int leverX;
    private static int leverY;
    private static int keyX;
    private static int keyY;
    private static int wallMoveableX;
    private static int wallMoveableY;
    private float enemyX1;
    private float enemyY1;
    private float angelX;
    private float angelY;
    private Devil devil;
    private int devilX;
    private int devilY;


    /**
     * maxX and maxY identify the largest x and y coordination in each .properties file and sets it as the maximum map size
     */
    private static int maxX;
    private static int maxY;

    /**
     * Screen Shaking
     */
    private boolean isScreenShaking = false;
    // how much time has passed during the screen shaking
    private float screenShakeTime = 0f;
    private float screenShakeDuration = 0.5f;
    private float screenShakeIntensity = 2f;
    private Vector2 originalCameraPosition = new Vector2();  // Store the original camera position for restoration


    private GuardianAngel angel;
    private Enemy enemy;
    private static List<Enemy> enemies;
    private static Map<MapCoordinates, Integer> mapData;
    private static boolean isPaused;
    private static long startTime; // Initialize the start time


    /**
     * Constructor for GameScreen. Sets up the camera and font.
     *
     * @param game The main game class, used to access global resources and methods.
     */
    public GameScreen(MazeRunnerGame game) {
        this.game = game;
        mapData = new HashMap<>();

        // Create and configure the camera for the game view
        camera = new OrthographicCamera();
        camera.setToOrtho(true);
        camera.zoom = 0.80f;
        font = game.getSkin().getFont("font");

        Animation<TextureRegion> characterAnimation = Character.getCharacterDownAnimation();

        // Starting position - use the entry coordinates
        /**
         * Resets the character if the game is reset, or if there is no character to begin with
         */
        if (character == null || PauseScreen.isReset()) {
            startTime = System.currentTimeMillis();

            character = new Character(this, game, getEntryX(), getEntryY(), false, false, false,5, characterAnimation);
            camera.position.set(character.getX(), character.getY(), 0);
            camera.update();
        }

        // Play some background music
        // Background sound
        MenuScreen.getMenuScreenMusic().stop();
        Music backgroundMusic = Gdx.audio.newMusic(Gdx.files.internal("background.mp3"));
        backgroundMusic.setLooping(true);
        backgroundMusic.setVolume(0.1f);
        backgroundMusic.play();


        camera.position.set(character.getX(), character.getY(), 0);
        camera.update();

        enemies = initialiseEnemy();
        Enemy.loadAnimation();

        angel = new GuardianAngel(getAngelX(), getAngelY());
        GuardianAngel.loadAnimation();
        devil = new Devil(0,0);
        Devil.loadAnimation();
        /**
         * Load all the required animations for the Game Screen
         */
        Life.animation();
        Exit.animation();
        Trap.animation();
        Key.animation();
        Treasure.animation();
        Wall.animation();
        Lamps.animation();

    }

    /**
     * Reads the file provided line by line and separates the lines into first, before and after an = sign,
     * then before and after a comma sign.
     * It then adds this to a two-dimensional array in which before the comma is x, after the comma is y, after the = is value
     * @param filePath
     */
    public static void loadMazeDataFromPropertiesFile(String filePath) {
        try {
            mapData = new HashMap<>();
            /**
             *  Read the file line by line
             */
            try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    // Split the line into two parts using "=" as a delimiter
                    String[] parts = line.split("=");

                    // Extract the coordinates from the first part using "," as a delimiter
                    String[] coordinates = parts[0].split(",");
                    int x = Integer.parseInt(coordinates[0]);
                    int y = Integer.parseInt(coordinates[1]);

                    // Parse the value from the second part of the line
                    int value = Integer.parseInt(parts[1]);

                    // Create a new MapCoordinates object with the parsed coordinates
                    MapCoordinates coordinatesObject = new MapCoordinates(x, y);

                    // Put the MapCoordinates object and the parsed value into the mapData map
                    mapData.put(coordinatesObject, value);
                }
            }

            maxX = mapData.keySet().stream().mapToInt(MapCoordinates::getX).max().orElse(0);
            maxY = mapData.keySet().stream().mapToInt(MapCoordinates::getY).max().orElse(0);

            /**
             * Initialize the mazeArray with maxX and maxY
             */
            mazeArray = new int[maxX + 1][maxY + 1];

            /**
             * Add a standard background that appears in an unspecified case
             */
            int defaultValue = -1;
            // Populate the mazeArray with values from the mapData, using defaultValue for unspecified cells
            for (int i = 0; i <= maxX; i++) {
                for (int j = 0; j <= maxY; j++) {
                    mazeArray[i][j] = mapData.getOrDefault(new MapCoordinates(i, j), defaultValue);
                }
            }

            /**
             * Populate the mazeArray with values from the mapData
             */
            mapData.forEach((coordinates, value) -> mazeArray[coordinates.getX()][coordinates.getY()] = value);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Created a private class here for simplicity and easier access to the x and y coordinates
     * Otherwise, everytime a coordinate needed to me called, a class would have to be called first
     */
    private static class MapCoordinates {
        private final int x;
        private final int y;

        public MapCoordinates(int x, int y) {
            this.x = x;
            this.y = y;
        }

        public int getX() {
            return x;
        }

        public int getY() {
            return y;
        }

    }

    /**
     * Initialize the enemy by adding each individual Enemy from the mazeArray to an ArrayList
     * This allows for each individual enemy to act independent of the other
     * @return
     */
    public List<Enemy> initialiseEnemy() {
        if (enemies == null) {
            enemies = new ArrayList<>();

            for (int x = 0; x <= maxX; x++) {
                for (int y = 0; y <= maxY; y++) {
                    int variable = mazeArray[x][y];

                    if (variable == 4) {
                        enemies.add(new Enemy(x * 50, y * 50));
                    }
                }
            }
        }
        return enemies;
    }

    /**
     * Initialising the devil's position based on the level properties files.
     */
    public void initialiseDevil() {
        for (int x = 0; x <= maxX; x++) {
            for (int y = 0; y <= maxY; y++) {
                int variable = mazeArray[x][y];

                if (variable == 25) {
                    devil = new Devil(x * 50, y * 50);
                }
            }
        }
    }

    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // Screen interface methods with necessary functionality
    @Override
    public void render(float delta) {
        if (!isPaused) {

        if(Gdx.input.isKeyPressed(Input.Keys.PERIOD)) {
            camera.zoom += 0.01f;
        }
        if(Gdx.input.isKeyPressed(Input.Keys.COMMA)) {
            camera.zoom -= 0.01f;
        }

        /**
         * Gdx.input.isKeyJustPressed() as opposed to Gdx.input.isKeyPressed() allows the key to just be pressed once
         */
        if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
            setPaused(true);
            game.goToPauseScreen();
        }

        /**
         * If the character kills 5 Enemies, it becomes one Life
         */
        if (Character.getEnemiesKilled() >= 5) {
            Character.setEnemiesKilled(0);  // Reset the count of enemies killed
            character.setLives(character.getLives() + 1);  // Increase the number of lives
        }

        /**
         *
         */
        if(character.getLives() <= 0) {
            game.goToGameOverScreen();
            /**
             * https://www.youtube.com/watch?v=BVQ_JHmvhCM&ab_channel=SuperMarioBroz.
             * SUPER MARIO - game over - sound effect by Super Mario Broz. on YouTube
             */
            Music backgroundMusic = Gdx.audio.newMusic(Gdx.files.internal("SuperMarioGameOverSound.mp3"));
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
        for (int x = 0; x <= maxX; x++) {
            for (int y = 0; y <= maxY; y++) {
                int variable = mazeArray[x][y];

            // Calculate the actual position on the screen
            float mazeX = x * 50;
            float mazeY = y * 50;

            switch (variable) {
                case 0:
                    // Wall
                    wallX = (int) mazeX;
                    wallY = (int) mazeY;
                    game.getSpriteBatch().draw(Wall.getWallImageRegion(), mazeX, mazeY, 50, 50);
                    break;
                case 1:
                    // Entry point
                    entryX = (int) mazeX;
                    entryY = (int) mazeY;
                    game.getSpriteBatch().draw(Entry.getEntryPointImageRegion(), mazeX, mazeY, 50, 50);
                    break;
                case 2:
                    //Exit
                    if (character.isHasKey()) {
                        removeFromMazeData(mazeX, mazeY,2,22);
                    }
                    else {
                        game.getSpriteBatch().draw(Exit.getExitPointImageRegion(), mazeX, mazeY, 50, 50);
                    }
                    break;

                case 3:
                    // Trap (static obstacle)
                    float trapX = mazeX;
                    float trapY = mazeY;
                    game.getSpriteBatch().draw(Trap.renderTexture(),mazeX,mazeY,50,50);

                    /**
                     * Immunity to fire
                     */
                    if (character.collidesWithTrap(trapX, trapY) && !Character.isIsImmuneToFire()) {
                        /**
                         * https://www.youtube.com/watch?v=5kl8-IwhQQI&ab_channel=OverknightSounds
                         * Wrong Buzzer Sound Effect by Overknight Sounds on YouTube
                         */
                        Music backgroundMusic = Gdx.audio.newMusic(Gdx.files.internal("LoseLife.mp3"));
                        backgroundMusic.setLooping(false);
                        backgroundMusic.play();
                        character.setLives(character.getLives() - 1);
                        shakeTheScreen();
                    }
                    game.getSpriteBatch().draw(Trap.getTrapImageRegion(), mazeX, mazeY, 50, 50);
                    break;
                case 5:
                    // Key
                    keyX = (int) mazeX;
                    keyY = (int) mazeY;

                    if(character.isTreasureOpened()) {

                        game.getSpriteBatch().draw(Key.renderTexture(),mazeX, mazeY, 60, 60);
                        if (character.collidesWithKey(keyX, keyY)) {
                            character.setHasKey(true);
                            /**
                             * https://www.youtube.com/watch?v=c9Yb_a4R7ts&ab_channel=SFXWARD
                             * Level Up Sound Effect by SFX WARD on YouTube
                             */
                            Music backgroundMusic = Gdx.audio.newMusic(Gdx.files.internal("Key.mp3"));
                            backgroundMusic.setLooping(false);
                            backgroundMusic.play();
                            removeFromMazeData(keyX, keyY,5,15);
                        }
                    }

                    break;
                case 6:
                    //Treasure
                    /**
                     * If the character is near the treasure, animate the treasure opening
                     */
                    if (character.getX() < mazeX + 100 && character.getX() + 36 > mazeX - 50 &&
                            character.getY() < mazeY + 100 && character.getY() + 31 > mazeY - 50) {

                        game.getSpriteBatch().draw(Treasure.renderTexture(),mazeX, mazeY, 60, 60);

                        if (character.collidesWithTreasure(mazeX, mazeY)) {
                            character.setTreasureOpened(true);
                            character.setHasKey(false);
                            game.goToTreasureScreen();
                            removeFromMazeData(mazeX, mazeY,6,16);
                        }
                    }
                    else {
                        game.getSpriteBatch().draw(Treasure.getTreasurePointImageRegion(), mazeX, mazeY, 60, 60);
                    }
                    break;
                case 7:
                    // Guardian Angel
                    float angelX = mazeX + angel.getX();
                    float angelY = mazeY + angel.getY();
                    game.getSpriteBatch().draw(
                            angel.render(delta),
                            angelX,
                            angelY,
                            55,
                            55
                    );
                    if (character.seesTheAngel(angelX, angelY)) {
                        if (character.collidesWithAngel(angelX, angelY)) {
                            game.goToNpcDialogScreen1();
                            removeFromMazeData(angelX, angelY,7,17);
                        }
                    }
                    break;
                case 8:
                    // WallShadow
                    wallX = (int) mazeX;
                    wallY = (int) mazeY;
                    game.getSpriteBatch().draw(Wall.getWallShadowImageRegion(), mazeX, mazeY, 50, 50);
                    break;
                case 9:
                    //Lamps
                    game.getSpriteBatch().draw(Lamps.renderTexture(), mazeX, mazeY, 50, 50);
                    break;
                case 10:
                    // WallMoveable
                    wallMoveableX = (int) mazeX;
                    wallMoveableY = (int) mazeY;
                    game.getSpriteBatch().draw(Wall.getMoveableWallImageRegion(), mazeX, mazeY, 50, 50);

                    /**
                     * If the character colllides with the lever, the wall is removed
                     */
                    if (character.getX() < leverX + 50 && character.getX() + 36 > leverX  &&
                            character.getY() <leverY + 50 && character.getY() + 31 > leverY) {
                        removeFromMazeData(mazeX, mazeY,10,20);
                    }
                    break;
                case 11:
                    //Lever
                    leverX = (int) mazeX;
                    leverY = (int) mazeY;
                    if (character.collidesWithLever(mazeX,mazeY)) {
                        game.getSpriteBatch().draw(
                                Lever.renderTexture(),
                                mazeX,
                                mazeY,
                                50,
                                50
                        );
                    }

                    if (character.collidesWithLever(mazeX,mazeY) && !character.isLeverPulled()) {
                        /**
                         * https://www.youtube.com/watch?v=oRwHxQnu-gs&ab_channel=catasorusanims
                         * rock wall smash sound effect fixed. by catasorus anims on YouTube
                         */
                        Music backgroundMusic = Gdx.audio.newMusic(Gdx.files.internal("WallCrumbleSound.mp3"));
                        backgroundMusic.setLooping(false);
                        backgroundMusic.play();
                        character.setLeverPulled(true);
                    }

                    if (!character.collidesWithLever(mazeX,mazeY) && !character.isLeverPulled()) {
                        game.getSpriteBatch().draw(Lever.getLeverPointImageRegion(), mazeX, mazeY, 50, 50);
                    }
                    else if (!character.collidesWithLever(mazeX,mazeY) && character.isLeverPulled()) {
                        game.getSpriteBatch().draw(Lever.getLeverEndPointImageRegion(), mazeX, mazeY, 50, 50);
                    }
                    break;
                case 12:
                    //Tree top
                    game.getSpriteBatch().draw(Tree.getTreeTopImageRegion(), mazeX, mazeY, 50, 50);
                    break;
                case 13:
                    //Tree of Good and Evil
                    game.getSpriteBatch().draw(Tree.getTreeImageRegion(), mazeX, mazeY, 50, 50);
                    if(character.collidesWithTree(mazeX,mazeY)) {
                        /**
                         * MathUtils.randomBoolean generates a random boolean and if its true, then we set collided tree as true
                         * else we set it as false
                         * The opposite is done for fire immunity
                         */
                        if (MathUtils.randomBoolean()) {
                            Character.setCollidedTree(true);
                            Character.setIsImmuneToFire(false); // Set the other property to false
                        } else {
                            Character.setCollidedTree(false); // Set the other property to false
                            Character.setIsImmuneToFire(true);
                        }


                        removeFromMazeData(mazeX, mazeY,13,23);

                        /**
                         * Depending on the result of the random boolean, the corresponding screen is called
                         */
                        if(Character.isCollidedTree()) {
                            game.goToTreeEvil();
                        }
                        else{
                            game.goToTreeGood();
                        }
                    }
                    break;
                case 20:
                    //Moveable Wall dissapearing
                    if (character.isLeverPulled()) {
                        game.getSpriteBatch().draw(Wall.renderTexture(), mazeX, mazeY, 50, 50);
                    }
                    break;
                case 22:
                    //Exit after being removed
                    if (character.isHasKey()) {
                        if (character.getX() < mazeX + 50 && character.getX() + 36 > mazeX - 0 &&
                                character.getY() < mazeY + 50 && character.getY() + 31 > mazeY - 0) {
                            game.getSpriteBatch().draw(
                                    Exit.renderTexture(),
                                    mazeX,
                                    mazeY,
                                    50,
                                    50
                            );{

                                game.goToVictoryScreen();

                                /**
                                 * https://www.youtube.com/watch?v=teUWsONJkk8&ab_channel=SoundEffectsFree
                                 * Victory Sound Effect by Sound Effects Free on YouTube
                                 */
                                Music backgroundMusic = Gdx.audio.newMusic(Gdx.files.internal("Victory.mp3"));
                                backgroundMusic.setLooping(false);
                                backgroundMusic.play();
                            }

                        } else if (character.getX() < mazeX + 250 && character.getX() + 36 > mazeX - 200 &&
                                character.getY() < mazeY + 250 && character.getY() + 31 > mazeY - 200) {
                            game.getSpriteBatch().draw(
                                    Exit.renderTexture(),
                                    mazeX,
                                    mazeY,
                                    50,
                                    50
                            );
                        } else {
                            game.getSpriteBatch().draw(Exit.getExitPointImageRegion(), mazeX, mazeY, 50, 50);
                        }
                    }else {
                        game.getSpriteBatch().draw(Exit.getExitPointImageRegion(), mazeX, mazeY, 50, 50);
                    }
                    break;
                default:
                    break;
            }
        }
        }

        //////////////////////////////////////////////////////////////////////////////////////////////////////////////
        /**
         * * render screen shake
         * */
        if (isScreenShaking) {
            applyScreenShakeEffect(delta);
        }
        game.getSpriteBatch().end();

            /**
             * render Enemies
             */
            for (Iterator<Enemy> iterator = enemies.iterator(); iterator.hasNext(); ) {
                //Iterate through every enemy separately and render accordingly
                Enemy enemy = iterator.next();
                //Vector distance creates a 2-dimensional range with the distance
                float distance = Vector2.dst(character.getX(), character.getY(), enemy.getX(), enemy.getY());
                boolean isEnemyVisible = distance < 150;
                /**
                 * This if statement ensure that the enemy only appears when the Character is within the range of vision
                 * The range of vision is restricted is the Tree of Good and Evil only allows the Character to see the enemy when they are near
                 */
                if ((Character.isCollidedTree() && isEnemyVisible) || !Character.isCollidedTree()) {
                    enemy.move(delta);
                    enemy.render(delta, game.getSpriteBatch());
                    if (character.collidesWithEnemy(enemy.getX(), enemy.getY())) {
                        if (Gdx.input.isKeyPressed(Input.Keys.SPACE)) { //The condition for killing enemies
                            Music backgroundMusic = Gdx.audio.newMusic(Gdx.files.internal("Slice.mp3"));
                            backgroundMusic.setLooping(false);
                            backgroundMusic.play();

                            // Change the value of the Enemy to have it disappear from the screen
                            removeEnemyFromMazeData(enemy.getX(), enemy.getY());
                            iterator.remove(); //remove the enemy from the list

                            character.setEnemiesKilled(character.getEnemiesKilled() + 1);
                        } else {
                            Music backgroundMusic = Gdx.audio.newMusic(Gdx.files.internal("LoseLife.mp3"));
                            backgroundMusic.setLooping(false);
                            backgroundMusic.play();
                            character.setLives(character.getLives() - 1);
                            shakeTheScreen();
                        }
                    }
                }
            }

            /**
             * render Devil
             */

            devil.render(delta,game.getSpriteBatch());
            devil.moveTowardsCharacter(character.getX(), character.getY(), delta);
            if (character.collidesWithDevil(devil.getX(),devil.getY())) {
                Music backgroundMusic = Gdx.audio.newMusic(Gdx.files.internal("LoseLife.mp3"));
                backgroundMusic.setLooping(false);
                backgroundMusic.play();
                character.setLives(character.getLives() - 1);
                shakeTheScreen();
            }

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
         * Character's Lives HUD
         */
        Life.render(game.getSpriteBatch(), delta, camera.position.x, camera.position.y, character.getLives());
        /**
         * Character's Key HUD
         */
        Key.render(game.getSpriteBatch(), delta, camera.position.x, camera.position.y, character.isHasKey());
        /**
         * Character's Enemies HDU
         */
        Enemy.renderEnemies(game.getSpriteBatch(), delta, camera.position.x, camera.position.y, Character.getEnemiesKilled());


        /**
         * collision image with trap/enemy (!)
         */
        float collisionX = camera.position.x - 10;
        float collisionY = camera.position.y;
        character.renderCollision(game.getSpriteBatch(),collisionX,collisionY);

        /**
         * angel meeting image (?)
         */
        float angelMeetingX = camera.position.x - 10;
        float angelMeetingY = camera.position.y;
        character.renderAngelMeeting(game.getSpriteBatch(),angelMeetingX,angelMeetingY);


        /**
         * character movement commands
         */
        character.move();
        if (!character.isShouldMove()){
            // Use a default frame when not moving, but before game starts
            character.setX(entryX + 100);
            character.setY(entryY);
            character.setCharacterRegion(Character.getCharacterDownAnimation().getKeyFrame(sinusInput, true));
            character.setLeverPulled(false);
            character.setTextVisible(true);
        } else{
            // Use a default frame when not moving, after game starts
            textX = camera.position.x; //Character and Camera are in sync
            textY = camera.position.y;
            character.setTextVisible(false);
        }

        /**
         * creates the stationary character
         */
        if(character.isKeyPressed()){
            game.getSpriteBatch().draw(
                    //Move the character with the animation that matches the direction of movement
                    Character.getCurrentAnimation().getKeyFrame(sinusInput, true),
                    textX,
                    textY,
                    64,
                    128
            );
        }
        else if (!character.isKeyPressed()){
            game.getSpriteBatch().draw(
                    //Stop moving and face the direction of the key last pressed when the move key is let go of
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
    }


    /**
     * removeMazeFromMazeData() allows for the object to be removed by changing the value of the object.
     * This way, even after the object is removed, we can work with it. An example is the moveable wall crumbling after it has been removed.
     *
     * @param x the x coordinate of the object being removed
     * @param y the y coordinate of the object being removed
     * @param fromWhere the original value of the object being removed as in switch case statement
     * @param toWhere the new value of the object being removed as in switch case statement
     */
    private void removeFromMazeData(float x, float y, int fromWhere, int toWhere) {
        for (int i = 0; i <= maxX; i++) {
            for (int j = 0; j <= maxY; j++) {
                if (mazeArray[i][j] == fromWhere) {
                    mazeArray[i][j] = toWhere;
                    break;
                }
            }
        }
    }

    /**
     * removeEnemyFromMazeData() is seperate because this method allows for individual enemies to be removed as opposed to all of them at once
     * The ones that are removed are specifically the ones that are collided with
     * @param x
     * @param y
     */
    private void removeEnemyFromMazeData(float x, float y) {
        for (int i = 0; i <= maxX; i++) {
            for (int j = 0; j <= maxY; j++) {
                      if (mazeArray[i][j] == 4 && i == (int) (x / 50) && j == (int) (y / 50)) {
                    mazeArray[i][j] = 14;
                    break;
                }
            }
        }
    }


    /**
     * reset() puts all the removed mazeData back in its original positions by having the values reset.
     * By using this method of removing objects from the maze, we are able to give them a different appearance once they are removed
     * for example when the wall is "removed", it shows a crumbling animation
     */
    public static void reset() {
        Wall.setWallBreakingStateTime(0);
        Character.setEnemiesKilled(0);
        Character.setCollidedTree(false);
        Character.setIsImmuneToFire(false);
        startTime = 0;
        enemies = null;

        //Key
        for (int i = 0; i <= maxX; i++) {
            for (int j = 0; j <= maxY; j++) {
                if (mazeArray[i][j] == 15) {
                    mazeArray[i][j] = 5;
                    break;
                }
            }
        }

        //Angel
        for (int i = 0; i <= maxX; i++) {
            for (int j = 0; j <= maxY; j++) {
                if (mazeArray[i][j] == 17) {
                    mazeArray[i][j] = 7;
                    break;
                }
            }
        }
        //Treasure
        for (int i = 0; i <= maxX; i++) {
            for (int j = 0; j <= maxY; j++) {
                if (mazeArray[i][j] == 16) {
                    mazeArray[i][j] = 6;  // Set variable to 0 to remove key from rendering
                    break;
                }
            }
        }
        //Moveable Wall
        for (int i = 0; i <= maxX; i++) {
            for (int j = 0; j <= maxY; j++) {
                if (mazeArray[i][j] == 20) {
                    mazeArray[i][j] = 10;  // Set variable to 0 to remove key from rendering

                }
            }
        }
        //Enemy
        for (int i = 0; i <= maxX; i++) {
            for (int j = 0; j <= maxY; j++) {
                if (mazeArray[i][j] == 14) {
                    mazeArray[i][j] = 4;  // Set variable to 0 to remove key from rendering

                }
            }
        }
        //Tree
        for (int i = 0; i <= maxX; i++) {
            for (int j = 0; j <= maxY; j++) {
                if (mazeArray[i][j] == 23) {
                    mazeArray[i][j] = 13;  // Set variable to 0 to remove key from rendering

                }
            }
        }
        //Exit
        for (int i = 0; i <= maxX; i++) {
            for (int j = 0; j <= maxY; j++) {
                if (mazeArray[i][j] == 22) {
                    mazeArray[i][j] = 2;  // Set variable to 0 to remove key from rendering

                }
            }
        }
    }


    /**
     * methods for shaking the screen.
     * @return
     */

    private void shakeTheScreen() {
        isScreenShaking = true;
        screenShakeTime = 0f;
        //stores the original camera position in originalCameraPosition
        originalCameraPosition.set(camera.position.x, camera.position.y);
    }

    private void applyScreenShakeEffect(float delta) {
        if (isScreenShaking) {
            // increments screenShakeTime by the time elapsed since the last frame which is 'delta'
            screenShakeTime += delta;

            //If the total shake time exceeds the defined duration (screenShakeDuration), it stops the screen shake.
            if (screenShakeTime >= screenShakeDuration) {
                isScreenShaking = false;
                // brings back the original camera position
                camera.position.x = originalCameraPosition.x;
                camera.position.y = originalCameraPosition.y;
            } else {
                // generates random values 'shakeAmountX' and 'shakeAmountY' within screenShakeIntensity
                float shakeAmountX = MathUtils.random(-screenShakeIntensity, screenShakeIntensity);

                float shakeAmountY = MathUtils.random(-screenShakeIntensity, screenShakeIntensity);

                camera.position.x += shakeAmountX;
                camera.position.y += shakeAmountY;

                camera.update();
            }
        }
    }


    /**
     * Getters and Setters
     */

    public float getSinusInput() {
        return sinusInput;
    }

    public static boolean isPaused() {
        return isPaused;
    }

    public static void setPaused(boolean paused) {
        isPaused = paused;
    }
    public static int[][] getMazeArray() {
        return mazeArray;
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

    public float getAngelX() {
        return angelX;
    }

    public float getAngelY() {
        return angelY;
    }
    public int getDevilX() {
        return devilX;
    }

    public int getDevilY() {
        return devilY;
    }
    public static long getStartTime() {
        return startTime;
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
        batch.dispose();
    }
    // Additional methods and logic can be added as needed for the game screen
}