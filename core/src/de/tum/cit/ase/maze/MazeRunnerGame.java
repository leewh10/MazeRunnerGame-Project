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
    private AngelScreen angelScreen;
    private HeartScreen heartScreen;
    private InstructionScreen instructionScreen;
    private KeyScreen keyScreen;
    private InstructionScreen treasureScreen;
    private TreeScreenEvil treeScreen;
    private InstructionScreen1 instructionScreen1;


    // Sprite Batch for rendering
    private SpriteBatch spriteBatch;

    // UI Skin
    private Skin skin;
    private NativeFileChooser fileChooser;


    /**
     * Constructor for MazeRunnerGame.
     * @param fileChooser The file chooser for the game, typically used in desktop environment.
     */
    public MazeRunnerGame(NativeFileChooser fileChooser) {
        super();
        this.fileChooser = fileChooser;
    }

    /**
     * Opens the fileChooser and specifically goes to the local directory, the folder names "maps"
     */
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
                GameScreen.loadMazeDataFromPropertiesFile(path); //Load the file selected onto the game
                goToGame(); //Go to game after the file is chosen
            }

            @Override
            public void onCancellation() {

            }

            @Override
            public void onError(Exception exception) {
                System.err.println("Error picking maze file: " + exception.getMessage());
                //Print an error message if the file cannot be used
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
        Wall.load();
        Entry.load();
        Exit.load();
        Treasure.load();
        Life.animation();
        Lever.load();
        Enemy.load();
        GuardianAngel.load();
        Tree.load();
        Trap.load();

        // Load character animation
        Character.loadAnimation();

        goToMenu(); // Navigate to the menu screen
    }


    /**
     * Disposes of the Screen and sets it to null to optimize the game
     * @param screen
     */
    public void disposeAndSetNull(Screen screen) {
        if (screen != null) {
            screen.dispose();
            screen = null;
        }
    }

    /**
     * Switches to different Screens.
     */
    public void goToMenu() {
        disposeAndSetNull(pauseScreen);
        disposeAndSetNull(victoryScreen);
        disposeAndSetNull(gameOverScreen);
        disposeAndSetNull(angelScreen);
        disposeAndSetNull(heartScreen);
        disposeAndSetNull(instructionScreen);
        disposeAndSetNull(keyScreen);
        disposeAndSetNull(treasureScreen);
        disposeAndSetNull(treeScreen);
        disposeAndSetNull(instructionScreen1);


        this.setScreen(new MenuScreen(this)); // Set the current screen to MenuScreen
    }
    public void goToTreeEvil() {
        disposeAndSetNull(pauseScreen);
        disposeAndSetNull(victoryScreen);
        disposeAndSetNull(gameOverScreen);
        disposeAndSetNull(angelScreen);
        disposeAndSetNull(heartScreen);
        disposeAndSetNull(instructionScreen);
        disposeAndSetNull(keyScreen);
        disposeAndSetNull(treasureScreen);
        disposeAndSetNull(menuScreen);
        disposeAndSetNull(instructionScreen1);

        this.setScreen(new TreeScreenEvil(this));

    }
    public void goToTreeGood() {
        disposeAndSetNull(pauseScreen);
        disposeAndSetNull(victoryScreen);
        disposeAndSetNull(gameOverScreen);
        disposeAndSetNull(angelScreen);
        disposeAndSetNull(heartScreen);
        disposeAndSetNull(instructionScreen);
        disposeAndSetNull(keyScreen);
        disposeAndSetNull(treasureScreen);
        disposeAndSetNull(menuScreen);
        disposeAndSetNull(instructionScreen1);

        this.setScreen(new TreeScreenGood(this));

    }

    public void goToGame() {
        disposeAndSetNull(menuScreen);
        disposeAndSetNull(pauseScreen);
        disposeAndSetNull(victoryScreen);
        disposeAndSetNull(gameOverScreen);
        disposeAndSetNull(angelScreen);
        disposeAndSetNull(heartScreen);
        disposeAndSetNull(instructionScreen);
        disposeAndSetNull(keyScreen);
        disposeAndSetNull(treasureScreen);
        disposeAndSetNull(treeScreen);
        disposeAndSetNull(instructionScreen1);

        this.setScreen(new GameScreen(this));
    }

    public void goToInstructionScreen() {
        disposeAndSetNull(menuScreen);
        disposeAndSetNull(pauseScreen);
        disposeAndSetNull(victoryScreen);
        disposeAndSetNull(gameOverScreen);
        disposeAndSetNull(angelScreen);
        disposeAndSetNull(heartScreen);
        disposeAndSetNull(keyScreen);
        disposeAndSetNull(treasureScreen);
        disposeAndSetNull(treeScreen);
        disposeAndSetNull(instructionScreen1);

        this.setScreen(new InstructionScreen(this));
    }
    public void goToInstructionScreen1() {
        disposeAndSetNull(menuScreen);
        disposeAndSetNull(pauseScreen);
        disposeAndSetNull(victoryScreen);
        disposeAndSetNull(gameOverScreen);
        disposeAndSetNull(angelScreen);
        disposeAndSetNull(heartScreen);
        disposeAndSetNull(instructionScreen);
        disposeAndSetNull(keyScreen);
        disposeAndSetNull(treasureScreen);
        disposeAndSetNull(treeScreen);


        this.setScreen(new InstructionScreen1(this));
    }

    public void goToGameOverScreen(){
        disposeAndSetNull(menuScreen);
        disposeAndSetNull(pauseScreen);
        disposeAndSetNull(victoryScreen);
        disposeAndSetNull(angelScreen);
        disposeAndSetNull(heartScreen);
        disposeAndSetNull(instructionScreen);
        disposeAndSetNull(keyScreen);
        disposeAndSetNull(treasureScreen);
        disposeAndSetNull(gameScreen);
        disposeAndSetNull(treeScreen);
        disposeAndSetNull(instructionScreen1);

        this.setScreen(new GameOverScreen(this));
    }
    public void goToVictoryScreen(){
        disposeAndSetNull(menuScreen);
        disposeAndSetNull(pauseScreen);
        disposeAndSetNull(gameOverScreen);
        disposeAndSetNull(angelScreen);
        disposeAndSetNull(heartScreen);
        disposeAndSetNull(instructionScreen);
        disposeAndSetNull(keyScreen);
        disposeAndSetNull(treasureScreen);
        disposeAndSetNull(treeScreen);
        disposeAndSetNull(instructionScreen1);

        this.setScreen(new VictoryScreen(this, gameScreen));
    }


    public void goToPauseScreen() {
        disposeAndSetNull(menuScreen);
        disposeAndSetNull(victoryScreen);
        disposeAndSetNull(gameOverScreen);
        disposeAndSetNull(angelScreen);
        disposeAndSetNull(heartScreen);
        disposeAndSetNull(instructionScreen);
        disposeAndSetNull(keyScreen);
        disposeAndSetNull(treasureScreen);
        disposeAndSetNull(gameScreen);
        disposeAndSetNull(treeScreen);
        disposeAndSetNull(instructionScreen1);

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
        disposeAndSetNull(treeScreen);
        disposeAndSetNull(instructionScreen1);

        this.setScreen(new AngelScreen(this,gameScreen));
    }

    public void goToHeartScreen() {
        disposeAndSetNull(menuScreen);
        disposeAndSetNull(pauseScreen);
        disposeAndSetNull(victoryScreen);
        disposeAndSetNull(gameOverScreen);
        disposeAndSetNull(angelScreen);
        disposeAndSetNull(instructionScreen);
        disposeAndSetNull(keyScreen);
        disposeAndSetNull(treasureScreen);
        disposeAndSetNull(gameScreen);
        disposeAndSetNull(treeScreen);
        disposeAndSetNull(instructionScreen1);

        this.setScreen(new HeartScreen(this));
    }
    public void goToTreasureScreen() {
        disposeAndSetNull(menuScreen);
        disposeAndSetNull(pauseScreen);
        disposeAndSetNull(victoryScreen);
        disposeAndSetNull(gameOverScreen);
        disposeAndSetNull(angelScreen);
        disposeAndSetNull(heartScreen);
        disposeAndSetNull(instructionScreen);
        disposeAndSetNull(keyScreen);
        disposeAndSetNull(gameScreen);
        disposeAndSetNull(treeScreen);
        disposeAndSetNull(instructionScreen1);

        this.setScreen(new TreasureScreen(this));
    }
    public void goToKeyScreen() {
        disposeAndSetNull(menuScreen);
        disposeAndSetNull(pauseScreen);
        disposeAndSetNull(victoryScreen);
        disposeAndSetNull(gameOverScreen);
        disposeAndSetNull(angelScreen);
        disposeAndSetNull(heartScreen);
        disposeAndSetNull(instructionScreen);
        disposeAndSetNull(treasureScreen);
        disposeAndSetNull(gameScreen);
        disposeAndSetNull(treeScreen);
        disposeAndSetNull(instructionScreen1);

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
        disposeAndSetNull(angelScreen);
        disposeAndSetNull(heartScreen);
        disposeAndSetNull(instructionScreen);
        disposeAndSetNull(keyScreen);
        disposeAndSetNull(treasureScreen);
        disposeAndSetNull(gameScreen);
        disposeAndSetNull(treeScreen);
        disposeAndSetNull(instructionScreen1);

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
