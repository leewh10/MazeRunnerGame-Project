package de.tum.cit.ase.maze;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import com.badlogic.gdx.utils.Array;

/**
 * Represents dynamic obstacles in the maze.
 * Enemies can move and cause the player to lose a life on contact.
 */
public class Enemy extends GameObject{
    private static Animation<TextureRegion> enemyStillAnimation;
    private static Animation<TextureRegion> enemyLeftAnimation;
    private static Animation<TextureRegion> enemyRightAnimation;
    private float stateTime = 0f;
    private float speed;
    private boolean movingRight;
    private float oscillationDistance;
    private float initialX;
    private float sinusInput = 0f;
    private TextureRegion enemyRegion;

    // Constants to represent enemy states
    private static final int STILL = 0;
    private static final int MOVING_LEFT = 1;
    private static final int MOVING_RIGHT = 2;

    private int currentState = STILL;
    private GameScreen gameScreen;


    /**
     * Constructor for Enemy.
     *
     * @param x The initial x-coordinate of the Enemy.
     * @param y The initial y-coordinate of the Enemy.
     */

    public Enemy(float x, float y, float speed, float oscillationDistance, GameScreen gameScreen) {
        super(x, y);
        this.speed = speed;
        this.movingRight = true;
        this.oscillationDistance = oscillationDistance;
        this.initialX = gameScreen.getEnemyX();
        this.gameScreen = gameScreen;
        initialise();
    }

    public static void loadEnemyAnimation() {
        Texture enemySheet = new Texture(Gdx.files.internal("mobs.png"));
        int frameWidth = 16;
        int frameHeight = 16;

        Array<TextureRegion> enemyStillFrames = new Array<>(TextureRegion.class);
        Array<TextureRegion> enemyLeftFrames = new Array<>(TextureRegion.class);
        Array<TextureRegion> enemyRightFrames = new Array<>(TextureRegion.class);
        for (int col = 6; col < 9; col++) {
            enemyStillFrames.add(new TextureRegion(enemySheet, col * frameWidth, 4 * frameHeight, frameWidth , frameHeight));
            enemyLeftFrames.add(new TextureRegion(enemySheet, col * frameWidth, 5 * frameHeight, frameWidth, frameHeight));
            enemyRightFrames.add(new TextureRegion(enemySheet, col * frameWidth, 6 * frameWidth, frameWidth, frameHeight));
        }

        enemyStillAnimation = new Animation<>(0.5f, enemyStillFrames);
        enemyLeftAnimation = new Animation<>(0.5f, enemyLeftFrames);
        enemyRightAnimation = new Animation<>(0.5f, enemyRightFrames);
    }

    public void initialise() {
        // Check if the enemy animation is not loaded
        if (enemyStillAnimation == null) {
            // Load the enemy animation frames
            loadEnemyAnimation();
        }

        // Set the initial frame of the animation
        // Get the frame at time 'sinusInput' and set it as the current enemyRegion
        setEnemyRegion(enemyStillAnimation.getKeyFrame(sinusInput, true));
    }


    public void move(float delta) {
        // delta = the time elapsed since the last frame or update
        // deltaX = the horizontal distance the enemy should move in the current frame
        // Move the enemy horizontally (right and left)
        float deltaX = speed * (movingRight ? 1 : -1);
        x += deltaX * delta;

        // Update the enemy state based on movement direction
        currentState = (deltaX > 0) ? MOVING_RIGHT : MOVING_LEFT;

        if (getX() <= gameScreen.getEnemyX() - oscillationDistance || getX() >= gameScreen.getEnemyX() + oscillationDistance) {
            movingRight = !movingRight;
        }

//        if (gameScreen.getEnemyX() <= initialX - oscillationDistance || gameScreen.getEnemyX() >= initialX + oscillationDistance) {
//            movingRight = !movingRight;
//        }
    }

    public TextureRegion render(float delta) {
        // delta = the time elapsed since the last frame or update
        // Increment the state time by the time passed since the last frame
        stateTime += delta;

        // Determine the current animation based on the enemy's state
        Animation<TextureRegion> currentAnimation = switch (currentState) {
            case MOVING_LEFT -> enemyLeftAnimation;
            case MOVING_RIGHT -> enemyRightAnimation;
            default -> enemyStillAnimation;
        };

        // Get the current frame of the animation at the updated state time
        // The 'true' parameter indicates looping of the animation
        enemyRegion = currentAnimation.getKeyFrame(stateTime,true);
        return enemyRegion;
    }


    //Getters
    public static Animation<TextureRegion> getEnemyStillAnimation() {
        return enemyStillAnimation;
    }

    public static Animation<TextureRegion> getEnemyLeftAnimation() {
        return enemyLeftAnimation;
    }

    public static Animation<TextureRegion> getEnemyRightAnimation() {
        return enemyRightAnimation;
    }

    public float getStateTime() {
        return stateTime;
    }

    public float getSpeed() {
        return speed;
    }

    public boolean isMovingRight() {
        return movingRight;
    }

    public float getOscillationDistance() {
        return oscillationDistance;
    }

    public float getInitialX() {
        return initialX;
    }

    public TextureRegion getEnemyRegion() {
        return enemyRegion;
    }

    public void setEnemyRegion(TextureRegion enemyRegion) {
        this.enemyRegion = enemyRegion;
    }
}
