package de.tum.cit.ase.maze;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import games.spooky.gdx.nativefilechooser.NativeFileChooser;
import games.spooky.gdx.nativefilechooser.NativeFileChooserCallback;
import games.spooky.gdx.nativefilechooser.NativeFileChooserConfiguration;
import games.spooky.gdx.nativefilechooser.NativeFileChooserIntent;

/**
 * The MazeRunnerGame class represents the core of the Maze Runner game.
 * It manages the screens and global resources like SpriteBatch and Skin.
 */
public class MazeRunnerGame extends Game {
    // Screens
    private MenuScreen menuScreen;
    private GameScreen gameScreen;
    private PauseScreen pauseScreen;
    private VictoryScreen victoryScreen;
    private GameOverScreen gameOverScreen;
    private NpcDialogScreen1 npcDialogScreen1;
    private HeartScreen heartScreen;
    private InstructionScreen instructionScreen;
    private KeyScreen keyScreen;
    private InstructionScreen treasureScreen;

    // Sprite Batch for rendering
    private SpriteBatch spriteBatch;

    // UI Skin
    private Skin skin;
    private NativeFileChooser fileChooser;


    /**
     * Constructor for MazeRunnerGame.
     *
     * @param fileChooser The file chooser for the game, typically used in desktop environment.
     */
    public MazeRunnerGame(NativeFileChooser fileChooser) {
        super();
        this.fileChooser = fileChooser;
    }

    public void openFileChooser() {
        var fileChooserConfig = new NativeFileChooserConfiguration();
        fileChooserConfig.title = "Load Map";
        fileChooserConfig.intent = NativeFileChooserIntent.OPEN;
        fileChooserConfig.nameFilter = (file,name) -> name.endsWith("properties");
        fileChooserConfig.directory = Gdx.files.local("maps");

        fileChooser.chooseFile(fileChooserConfig, new NativeFileChooserCallback() {
            @Override
            public void onFileChosen(FileHandle fileHandle) {
                String path = fileHandle.path();
                GameScreen.loadMazeDataFromPropertiesFile(path);
                goToGame();
            }

            @Override
            public void onCancellation() {

            }

            @Override
            public void onError(Exception exception) {
                System.err.println("Error picking maze file: " + exception.getMessage());
            }
        });
    }

    /**
     * Called when the game is created. Initializes the SpriteBatch and Skin.
     */
    @Override
    public void create() {
        spriteBatch = new SpriteBatch(); // Create SpriteBatch

        // Load UI skin
        skin = new Skin(Gdx.files.internal("craft/craftacular-ui.json"));


        // Load game background
        Wall.loadWall();
        Entry.loadEntry();
        Exit.loadExit();
        Treasure.loadTreasure();
        Life.lifeImageAnimation();
        Lever.loadLever();
        Enemy.loadEnemy();
        GuardianAngel.loadAngel();


        // Load character animation
        Character.loadCharacterAnimation();
        // Play some background music
        // Background sound


        Music backgroundMusic = Gdx.audio.newMusic(Gdx.files.internal("background.mp3"));
        backgroundMusic.setLooping(true);
        //backgroundMusic.play();

        goToMenu(); // Navigate to the menu screen

    }



    /**
     * Switches to the menu screen.
     */
    public void goToMenu() {
        disposeAndSetNull(pauseScreen);
        disposeAndSetNull(victoryScreen);
        disposeAndSetNull(gameOverScreen);
        disposeAndSetNull(npcDialogScreen1);
        disposeAndSetNull(heartScreen);
        disposeAndSetNull(instructionScreen);
        disposeAndSetNull(keyScreen);
        disposeAndSetNull(treasureScreen);

        this.setScreen(new MenuScreen(this)); // Set the current screen to MenuScreen

    }


    /**
     * Switches to the game screen.
     */
    public void disposeAndSetNull(Screen screen) {
        if (screen != null) {
            screen.dispose();
            screen = null;
        }
    }

    public void goToGame() {
        disposeAndSetNull(menuScreen);
        disposeAndSetNull(pauseScreen);
        disposeAndSetNull(victoryScreen);
        disposeAndSetNull(gameOverScreen);
        disposeAndSetNull(npcDialogScreen1);
        disposeAndSetNull(heartScreen);
        disposeAndSetNull(instructionScreen);
        disposeAndSetNull(keyScreen);
        disposeAndSetNull(treasureScreen);

        // Set the game screen
        this.setScreen(new GameScreen(this));
    }

    public void goToInstructionScreen() {
        disposeAndSetNull(menuScreen);
        disposeAndSetNull(pauseScreen);
        disposeAndSetNull(victoryScreen);
        disposeAndSetNull(gameOverScreen);
        disposeAndSetNull(npcDialogScreen1);
        disposeAndSetNull(heartScreen);
        disposeAndSetNull(instructionScreen);
        disposeAndSetNull(keyScreen);
        disposeAndSetNull(treasureScreen);

        // Set the game screen
        this.setScreen(new InstructionScreen(this));
    }

    public void goToGameOverScreen(){
        disposeAndSetNull(menuScreen);
        disposeAndSetNull(pauseScreen);
        disposeAndSetNull(victoryScreen);
        disposeAndSetNull(npcDialogScreen1);
        disposeAndSetNull(heartScreen);
        disposeAndSetNull(instructionScreen);
        disposeAndSetNull(keyScreen);
        disposeAndSetNull(treasureScreen);
        disposeAndSetNull(gameScreen);

        this.setScreen(new GameOverScreen(this));
    }
    public void goToVictoryScreen(){
        disposeAndSetNull(menuScreen);
        disposeAndSetNull(pauseScreen);
        disposeAndSetNull(gameOverScreen);
        disposeAndSetNull(npcDialogScreen1);
        disposeAndSetNull(heartScreen);
        disposeAndSetNull(instructionScreen);
        disposeAndSetNull(keyScreen);
        disposeAndSetNull(treasureScreen);
        disposeAndSetNull(gameScreen);

        this.setScreen(new VictoryScreen(this, gameScreen));
    }


    public void goToPauseScreen() {
        disposeAndSetNull(menuScreen);
        disposeAndSetNull(victoryScreen);
        disposeAndSetNull(gameOverScreen);
        disposeAndSetNull(npcDialogScreen1);
        disposeAndSetNull(heartScreen);
        disposeAndSetNull(instructionScreen);
        disposeAndSetNull(keyScreen);
        disposeAndSetNull(treasureScreen);
        disposeAndSetNull(gameScreen);

        this.setScreen(new PauseScreen(this,gameScreen));

    }
    public void goToNpcDialogScreen1() {
        disposeAndSetNull(menuScreen);
        disposeAndSetNull(pauseScreen);
        disposeAndSetNull(victoryScreen);
        disposeAndSetNull(gameOverScreen);
        disposeAndSetNull(heartScreen);
        disposeAndSetNull(instructionScreen);
        disposeAndSetNull(keyScreen);
        disposeAndSetNull(treasureScreen);
        disposeAndSetNull(gameScreen);

        this.setScreen(new NpcDialogScreen1(this,gameScreen));
    }

    public void goToHeartScreen() {
        disposeAndSetNull(menuScreen);
        disposeAndSetNull(pauseScreen);
        disposeAndSetNull(victoryScreen);
        disposeAndSetNull(gameOverScreen);
        disposeAndSetNull(npcDialogScreen1);
        disposeAndSetNull(instructionScreen);
        disposeAndSetNull(keyScreen);
        disposeAndSetNull(treasureScreen);
        disposeAndSetNull(gameScreen);

        this.setScreen(new HeartScreen(this));
    }
    public void goToTreasureScreen() {
        disposeAndSetNull(menuScreen);
        disposeAndSetNull(pauseScreen);
        disposeAndSetNull(victoryScreen);
        disposeAndSetNull(gameOverScreen);
        disposeAndSetNull(npcDialogScreen1);
        disposeAndSetNull(heartScreen);
        disposeAndSetNull(instructionScreen);
        disposeAndSetNull(keyScreen);
        disposeAndSetNull(gameScreen);

        this.setScreen(new TreasureScreen(this));
    }
    public void goToKeyScreen() {
        disposeAndSetNull(menuScreen);
        disposeAndSetNull(pauseScreen);
        disposeAndSetNull(victoryScreen);
        disposeAndSetNull(gameOverScreen);
        disposeAndSetNull(npcDialogScreen1);
        disposeAndSetNull(heartScreen);
        disposeAndSetNull(instructionScreen);
        disposeAndSetNull(treasureScreen);
        disposeAndSetNull(gameScreen);

        this.setScreen(new KeyScreen(this));
    }



    /**
     * Cleans up resources when the game is disposed.
     */
    @Override
    public void dispose() {
        disposeAndSetNull(menuScreen);
        disposeAndSetNull(pauseScreen);
        disposeAndSetNull(victoryScreen);
        disposeAndSetNull(gameOverScreen);
        disposeAndSetNull(npcDialogScreen1);
        disposeAndSetNull(heartScreen);
        disposeAndSetNull(instructionScreen);
        disposeAndSetNull(keyScreen);
        disposeAndSetNull(treasureScreen);
        disposeAndSetNull(gameScreen);
        spriteBatch.dispose(); // Dispose the spriteBatch
        skin.dispose(); // Dispose the skin
        super.dispose(); // Make sure to call the superclass method
    }


    // Getter methods
    public Skin getSkin() {
        return skin;
    }


    public SpriteBatch getSpriteBatch() {
        return spriteBatch;
    }


}
