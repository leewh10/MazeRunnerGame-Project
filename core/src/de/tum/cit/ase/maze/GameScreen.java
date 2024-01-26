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

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

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
    private static int[][] mazeArray;
    private BitmapFont font;
    private Enemy enemy;
    private float enemyX1;
    private float enemyY1;

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
    private float angelX;
    private float angelY;

    public float getAngelX() {
        return angelX;
    }

    public float getAngelY() {
        return angelY;
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
            character = new Character(this, game, getEntryX(), getEntryY(), false, false, false, 5, characterAnimation);
            camera.position.set(character.getX(), character.getY(), 0);
            camera.update();
        }

        camera.position.set(character.getX(), character.getY(), 0);
        camera.update();

        enemy = new Enemy(getEnemyX1(), getEnemyY1(), 5f, 50, this);
        Enemy.loadEnemyAnimation();

        angel = new GuardianAngel(getAngelX(), getAngelY(), 20f, 80, this);
        GuardianAngel.loadAngelAnimation();

        Tree.loadWall();
        Life.lifeImageAnimation();
        Exit.exitImageAnimation();
        Trap.trapImageAnimation();
        Key.keyImageAnimation();
        Treasure.treasureImageAnimation();
        Wall.wallBreakingImageAnimation();
        Lamps.lampImageAnimation();
    }

    public static void loadMazeDataFromPropertiesFile(String filePath) {
        try {
            Map<MapCoordinates, Integer> mapData = new HashMap<>();

            /**
             *  Read the file line by line.
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
             * we have to choose wall for this. so we need to add floor in the switch-case statement.
             */
            int defaultValue = -1; // (We have to choose Floor for this)
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


    public static int getMaxX() {
        return maxX;
    }

    public static int getMaxY() {
        return maxY;
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
        if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
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
                    // Exit point
                    if (character.isHasKey()) {
                        if (character.getX() < mazeX + 50 && character.getX() + 36 > mazeX - 0 &&
                                character.getY() < mazeY + 50 && character.getY() + 31 > mazeY - 0) {
                            game.getSpriteBatch().draw(
                                    Exit.renderExit(),
                                    mazeX,
                                    mazeY,
                                    50,
                                    50
                            );
                            {

                                game.goToVictoryScreen();

                            }

                        } else if (character.getX() < mazeX + 250 && character.getX() + 36 > mazeX - 200 &&
                                character.getY() < mazeY + 250 && character.getY() + 31 > mazeY - 200) {
                            game.getSpriteBatch().draw(
                                    Exit.renderExit(),
                                    mazeX,
                                    mazeY,
                                    50,
                                    50
                            );
                        } else {
                            // Draw the static exit point image when the character is not at the exit
                            game.getSpriteBatch().draw(Exit.getExitPointImageRegion(), mazeX, mazeY, 50, 50);
                        }
                    }else {
                        // Draw the static exit point image when the character is not at the exit
                        game.getSpriteBatch().draw(Exit.getExitPointImageRegion(), mazeX, mazeY, 50, 50);
                    }

                    break;

                case 3:
                    // Trap (static obstacle)
                    float trapX = mazeX;
                    float trapY = mazeY;
                    game.getSpriteBatch().draw(Trap.renderTrap(),mazeX,mazeY,50,50);

                    if (character.collidesWithTrap(trapX, trapY)) {
                        Music backgroundMusic = Gdx.audio.newMusic(Gdx.files.internal("LoseLife.mp3"));
                        backgroundMusic.setLooping(false);
                        backgroundMusic.play();
                        character.setLives(character.getLives() - 1);
                        shakeTheScreen();
                    }
                    game.getSpriteBatch().draw(Trap.getTrapImageRegion(), mazeX, mazeY, 50, 50);
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
                        if(Gdx.input.isKeyPressed(Input.Keys.SPACE)) {
                            removeEnemyFromMazeData(enemyX1,enemyY1);
                        }
                        else{
                            Music backgroundMusic = Gdx.audio.newMusic(Gdx.files.internal("LoseLife.mp3"));
                            backgroundMusic.setLooping(false);
                            backgroundMusic.play();
                            character.setLives(character.getLives() - 1);
                            shakeTheScreen();
                        }
                    }
                    break;
                case 5:
                    // Key
                    keyX = (int) mazeX;
                    keyY = (int) mazeY;

                    if(character.isTreasureOpened()) {
                        game.getSpriteBatch().draw(Key.renderKey(),mazeX, mazeY, 60, 60);
                        if (character.collidesWithKey(keyX, keyY)) {
                            character.setHasKey(true);
                            removeKeyFromMazeData(keyX, keyY);
                        }
                    }

                    break;
                case 6:
                    //Treasure
                    if (character.getX() < mazeX + 100 && character.getX() + 36 > mazeX - 50 &&
                            character.getY() < mazeY + 100 && character.getY() + 31 > mazeY - 50) {
                        game.getSpriteBatch().draw(Treasure.renderTreasure(),mazeX, mazeY, 60, 60);
                        if (character.collidesWithTreasure(mazeX, mazeY)) {
                            character.setTreasureOpened(true);
                            character.setHasKey(false);
                            game.goToTreasureScreen();
                            removeTreasureFromMazeData(mazeX, mazeY);
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
                            removeAngelFromMazeData(angelX, angelY);
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
                    game.getSpriteBatch().draw(Lamps.renderLamp(), mazeX, mazeY, 50, 50);
                    break;
                case 10:
                    // WallMoveable
                    wallMoveableX = (int) mazeX;
                    wallMoveableY = (int) mazeY;
                    game.getSpriteBatch().draw(Wall.getMoveableWallImageRegion(), mazeX, mazeY, 50, 50);
                    if (character.getX() < leverX + 50 && character.getX() + 36 > leverX  &&
                            character.getY() <leverY + 50 && character.getY() + 31 > leverY) {
                        removeWallMoveableFromMazeData(mazeX, mazeY);
                    }

                    break;
                case 11:
                    //Lever
                    leverX = (int) mazeX;
                    leverY = (int) mazeY;
                    if (character.collidesWithLever(mazeX,mazeY)) {
                        game.getSpriteBatch().draw(
                                Lever.renderLever(),
                                mazeX,
                                mazeY,
                                50,
                                50
                        );
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
                    //Tree
                    game.getSpriteBatch().draw(Tree.getTreeImageRegion(), mazeX, mazeY, 50, 50);
                    break;
                case 20:
                    //Moveable Wall dissapearing
                    if (character.isLeverPulled()) {
                        game.getSpriteBatch().draw(Wall.renderWallBreaking(), mazeX, mazeY, 50, 50);
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
        Life.renderLives(game.getSpriteBatch(), delta, camera.position.x, camera.position.y, character.getLives());
        font.draw(game.getSpriteBatch(), "Lives: " + character.getLives(), lifeTextX, lifeTextY);

        /**
         * Character's Key
         */
        Key.renderKeys(game.getSpriteBatch(), delta, camera.position.x, camera.position.y, character.isHasKey());


        float keyTextX = camera.position.x - camera.viewportWidth / 10 - 550;
        float keyTextY = camera.position.y + camera.viewportHeight / 10 + 340;

        if (character.isHasKey()) {
            font.draw(game.getSpriteBatch(), "Key Collected", keyTextX, keyTextY);
        } else {
            font.draw(game.getSpriteBatch(), "No Key", keyTextX, keyTextY);
        }

        /**
         * collision image
         */
        float collisionX = camera.position.x - 10;
        float collisionY = camera.position.y;
        character.renderCollision(game.getSpriteBatch(),collisionX,collisionY);

        /**
         * angel meeting image
         */
        float angelMeetingX = camera.position.x - 10;
        float angelMeetingY = camera.position.y;
        character.renderAngelMeeting(game.getSpriteBatch(),angelMeetingX,angelMeetingY);



        /**
         * key image
         */
        float keyX = camera.position.x - 50;
        float keyY = camera.position.y;
        character.renderKeyImage(game.getSpriteBatch(),keyX,keyY);

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
        for (int i = 0; i <= maxX; i++) {
            for (int j = 0; j <= maxY; j++) {
                if (mazeArray[i][j] == 5) {
                    mazeArray[i][j] = 15;  // Set variable to 0 to remove key from rendering
                    break;
                }
            }
        }
    }
    private void removeAngelFromMazeData(float x, float y) {
        for (int i = 0; i <= maxX; i++) {
            for (int j = 0; j <= maxY; j++) {
                if (mazeArray[i][j] == 7) {
                    mazeArray[i][j] = 17;  // Set variable to 0 to remove key from rendering
                    break;
                }
            }
        }
    }
    private void removeWallMoveableFromMazeData(float x, float y) {
        for (int i = 0; i <= maxX; i++) {
            for (int j = 0; j <= maxY; j++) {
                if (mazeArray[i][j] == 10) {
                    mazeArray[i][j] = 20;  // Set variable to 0 to remove key from rendering
                    break;
                }
            }
        }
    }

    private void removeTreasureFromMazeData(float x, float y) {
        for (int i = 0; i <= maxX; i++) {
         for (int j = 0; j <= maxY; j++) {
            if (mazeArray[i][j] == 6) {
                mazeArray[i][j] = 16;  // Set variable to 0 to remove key from rendering
                break;
            }
        }
     }
    }
    private void removeEnemyFromMazeData(float x, float y) {
        for (int i = 0; i <= maxX; i++) {
            for (int j = 0; j <= maxY; j++) {
                      if (mazeArray[i][j] == 4 && i == (int) (x / 50) && j == (int) (y / 50)) {
                    mazeArray[i][j] = 14;  // Set variable to 0 to remove key from rendering
                    break;
                }
            }
        }
    }

    public static void reset() {
        Wall.setWallBreakingStateTime(0);
        //Key
        for (int i = 0; i <= maxX; i++) {
            for (int j = 0; j <= maxY; j++) {
                if (mazeArray[i][j] == 15) {
                    mazeArray[i][j] = 5;  // Set variable to 0 to remove key from rendering
                    break;
                }
            }
        }


        //Angel
        for (int i = 0; i <= maxX; i++) {
            for (int j = 0; j <= maxY; j++) {
                if (mazeArray[i][j] == 17) {
                    mazeArray[i][j] = 7;  // Set variable to 0 to remove key from rendering
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
                    break;
                }
            }
        }
        //Enemy
        for (int i = 0; i <= maxX; i++) {
            for (int j = 0; j <= maxY; j++) {
                if (mazeArray[i][j] == 14) {
                    mazeArray[i][j] = 4;  // Set variable to 0 to remove key from rendering
                    break;
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
                // generates random values 'shakeAmountX' and shakeAmountY' within screenShakeIntensity
                float shakeAmountX = MathUtils.random(-screenShakeIntensity, screenShakeIntensity);
                float shakeAmountY = MathUtils.random(-screenShakeIntensity, screenShakeIntensity);

                camera.position.x += shakeAmountX;
                camera.position.y += shakeAmountY;

                camera.update();
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

    public float getEnemyX1() {
        return enemyX1;
    }

    public float getEnemyY1() {
        return enemyY1;
    }
    public static int[][] getMazeArray() {
        return mazeArray;
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