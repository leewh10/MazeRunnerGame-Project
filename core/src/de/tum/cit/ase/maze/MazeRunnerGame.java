package de.tum.cit.ase.maze;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import games.spooky.gdx.nativefilechooser.NativeFileChooser;

/**
 * The MazeRunnerGame class represents the core of the Maze Runner game.
 * It manages the screens and global resources like SpriteBatch and Skin.
 */
public class MazeRunnerGame extends Game {
    // Screens
    private MenuScreen menuScreen;
    private GameScreen gameScreen;
    private PauseScreen pauseScreen;

    // Sprite Batch for rendering
    private SpriteBatch spriteBatch;

    // UI Skin
    private Skin skin;


    /**
     * Constructor for MazeRunnerGame.
     *
     * @param fileChooser The file chooser for the game, typically used in desktop environment.
     */
    public MazeRunnerGame(NativeFileChooser fileChooser) {
        super();
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
        GameMap.loadBackground();

        // Load character's life animation
        GameMap.lifeImageAnimation();


        // Load character animation
        Character.loadCharacterAnimation();

        // Play some background music
        // Background sound


        Music backgroundMusic = Gdx.audio.newMusic(Gdx.files.internal("background.mp3"));
        backgroundMusic.setLooping(true);
        backgroundMusic.play();

        goToMenu(); // Navigate to the menu screen

    }


    /**
     * Switches to the menu screen.
     */
    public void goToMenu() {
        this.setScreen(new MenuScreen(this)); // Set the current screen to MenuScreen
        if (gameScreen != null) {
            gameScreen.dispose(); // Dispose the game screen if it exists
            gameScreen = null;
        }
    }


    /**
     * Switches to the game screen.
     */
    public void goToGame() {
        this.setScreen(new GameScreen(this));
        if (menuScreen != null) {
            menuScreen.dispose();
            menuScreen = null;
        }
    }

    public void goToGameOverScreen(){
        this.setScreen(new GameOverScreen(this));
        if (gameScreen != null) {
            gameScreen.dispose();
            gameScreen = null;
        }
    }
    public void goToVictoryScreen(){
        this.setScreen(new VictoryScreen(this, gameScreen));
        if (gameScreen != null) {
            gameScreen.dispose();
            gameScreen = null;
        }
    }


    public void goToPauseScreen() {
        this.setScreen(new PauseScreen(this,gameScreen));
        if (gameScreen != null) {
            gameScreen.dispose();
            gameScreen = null;
        }
    }

    public void resumeGame() {
        Character.loadCharacterAnimation();
        Character.resetAnimation();

        if (gameScreen == null) {
            gameScreen = new GameScreen(this); // Create a new GameScreen instance if not already created
            this.setScreen(gameScreen); // Set the current screen to the new GameScreen instance
        }

        if (pauseScreen != null) {
            pauseScreen.dispose();
            pauseScreen = null;
        }

        Character.setCurrentAnimation(Character.getCharacterDownAnimation());

        //Gdx.input.setInputProcessor(gameScreen.stage);
        goToGame();
    }




    /**
     * Cleans up resources when the game is disposed.
     */
    @Override
    public void dispose() {
        if (gameScreen != null) {
            gameScreen.dispose(); // Dispose the game screen if it exists
            gameScreen = null;
        }
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
