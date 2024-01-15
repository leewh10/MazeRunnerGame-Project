 package de.tum.cit.ase.maze;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Array;

import javax.swing.*;

public class Character extends GameObject {
    //private float x, y;
    private boolean hasKey;
    private int lives;
    private final Animation<TextureRegion> animation;
    private float stateTime;



    // Character animation downwards
    private static Animation<TextureRegion> characterDownAnimation;
    private static Animation<TextureRegion> characterUpAnimation;
    private static Animation<TextureRegion> characterLeftAnimation;
    private static Animation<TextureRegion> characterRightAnimation;


    private TextureRegion characterRegion;

    private boolean isTextVisible;
    private boolean shouldMove;
    private final OrthographicCamera camera;
    private final MazeRunnerGame game;
    private final BitmapFont font;
    private GameScreen gameScreen;




    //Constructor
    public Character(GameScreen gameScreen, MazeRunnerGame game, float x, float y, boolean hasKey, int lives, Animation<TextureRegion> animation) {
        super(x, y);
        this.game = game; // Store the game instance
        this.hasKey = hasKey;
        this.lives = lives;
        this.animation = animation;
        this.stateTime = 0f;
        this.gameScreen = gameScreen;

        camera = new OrthographicCamera();
        camera.setToOrtho(true);
        font = game.getSkin().getFont("font");
    }

    public Animation<TextureRegion> getAnimation() {
        return animation;
    }

    public boolean isHasKey() {
        return hasKey;
    }

    public void setHasKey(boolean hasKey) {
        this.hasKey = hasKey;
    }

    public int getLives() {
        return lives;
    }

    public void setLives(int lives) {
        this.lives = lives;
    }

    public void update(float delta) {
        stateTime += delta;
    }


    public float getStateTime() {
        return stateTime;
    }

    /**
     * Loads the character animation from the character.png file.
     */
    public static void loadCharacterAnimation() {

        Texture walkSheet = new Texture(Gdx.files.internal("character.png"));

        int frameWidth = 16;
        int frameHeight = 32;
        int animationFrames = 4;

        // libGDX internal Array instead of ArrayList because of performance
        Array<TextureRegion> walkFramesUp = new Array<>(TextureRegion.class);
        Array<TextureRegion> walkFramesDown = new Array<>(TextureRegion.class);
        Array<TextureRegion> walkFramesLeft = new Array<>(TextureRegion.class);
        Array<TextureRegion> walkFramesRight = new Array<>(TextureRegion.class);

        // Add frames to the respective animations
        for (int col = 0; col < animationFrames; col++) {
            walkFramesUp.add(
                    new TextureRegion(walkSheet, col * frameWidth, 2 * frameHeight, frameWidth, frameHeight));
            walkFramesDown.add(
                    new TextureRegion(walkSheet, col * frameWidth, 0, frameWidth, frameHeight));
            walkFramesLeft.add(
                    new TextureRegion(walkSheet, col * frameWidth, 3 * frameHeight, frameWidth, frameHeight));
            walkFramesRight.add(
                    new TextureRegion(walkSheet, col * frameWidth, frameHeight, frameWidth, frameHeight));}

        characterUpAnimation = new Animation<>(0.1f, walkFramesUp);
        characterDownAnimation = new Animation<>(0.1f, walkFramesDown);
        characterLeftAnimation = new Animation<>(0.1f, walkFramesLeft);
        characterRightAnimation = new Animation<>(0.1f, walkFramesRight);
    }


    public static Animation<TextureRegion> getCharacterDownAnimation() {
        return characterDownAnimation;
    }

    public static Animation<TextureRegion> getCharacterUpAnimation() {
        return characterUpAnimation;
    }

    public static Animation<TextureRegion> getCharacterLeftAnimation() {
        return characterLeftAnimation;
    }

    public static Animation<TextureRegion> getCharacterRightAnimation() {
        return characterRightAnimation;
    }


    public void move() {

    /**
     * boolean for the text showing
     */
        if(isTextVisible) {
        font.draw(game.getSpriteBatch(), "Press ESC to go to menu", gameScreen.getTextX(), gameScreen.getTextY());
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

            gameScreen.setTextX(camera.position.x);
            gameScreen.setTextY(camera.position.y);
        isTextVisible = false;
    } else {
        isTextVisible = true;
    }

        if (Gdx.input.isKeyPressed(Input.Keys.UP) || Gdx.input.isKeyPressed(Input.Keys.W)) {
        characterRegion = getCharacterUpAnimation().getKeyFrame(gameScreen.getSinusInput(), true);
        setY((int) getY() + 5);
        shouldMove = true;

    } else if (Gdx.input.isKeyPressed(Input.Keys.DOWN) || Gdx.input.isKeyPressed(Input.Keys.S)) {
        characterRegion = getCharacterDownAnimation().getKeyFrame(gameScreen.getSinusInput(), true);
        setY((int) getY() - 5);
        shouldMove = true;

    } else if (Gdx.input.isKeyPressed(Input.Keys.LEFT) || Gdx.input.isKeyPressed(Input.Keys.A)) {
        characterRegion = getCharacterLeftAnimation().getKeyFrame(gameScreen.getSinusInput(), true);
        setX((int) getX() - 5);
        shouldMove = true;

    } else if (Gdx.input.isKeyPressed(Input.Keys.RIGHT) || Gdx.input.isKeyPressed(Input.Keys.D)) {
        characterRegion = getCharacterRightAnimation().getKeyFrame(gameScreen.getSinusInput(), true);
        setX((int) getX() + 5);
        shouldMove = true;

        }
    }

    public TextureRegion getCharacterRegion() {
        return characterRegion;
    }

    public boolean isTextVisible() {
        return isTextVisible;
    }

    public boolean isShouldMove() {
        return shouldMove;
    }


    public void setTextVisible(boolean textVisible) {
        isTextVisible = textVisible;
    }

    public void setShouldMove(boolean shouldMove) {
        this.shouldMove = shouldMove;
    }

    public void setCharacterRegion(TextureRegion characterRegion) {
        this.characterRegion = characterRegion;
    }




    public void render(SpriteBatch batch) {
        TextureRegion currentFrame = animation.getKeyFrame(stateTime, true);
         float width = 4*currentFrame.getRegionWidth();
         float height = 4* currentFrame.getRegionHeight();

         batch.draw(currentFrame, x,y,width,height);
    }
}

