package de.tum.cit.ase.maze;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import com.badlogic.gdx.utils.Array;

import java.util.Random;

/**
 * Represents dynamic obstacles in the maze.
 * Enemies can move and cause the player to lose a life on contact.
 */
public class Enemy extends GameObject{
    private static Animation<TextureRegion> enemyStillAnimation;
    private static Animation<TextureRegion> enemyLeftAnimation;
    private static Animation<TextureRegion> enemyRightAnimation;
    private static Animation<TextureRegion> enemyUpAnimation;
    private static Animation<TextureRegion> enemyDownAnimation;
    private static TextureRegion EnemyImageRegion;

    private float stateTime = 0f;
    private float speed;
    private float oscillationDistance;
    private float sinusInput = 0f;
    private TextureRegion enemyRegion;

    // Constants to represent enemy states
    private static final int STILL = 0;
    private static final int MOVING_LEFT = 1;
    private static final int MOVING_RIGHT = 2;
    private static final int MOVING_UP = 3;
    private static final int MOVING_DOWN= 4;
    private float distanceWalked = 0;
    private Animation<TextureRegion> currentAnimation;

    private int currentDirection;


    /**
     * Constructor for Enemy.
     * It sets its speed and oscillation distance
     *
     * @param x The initial x-coordinate of the Enemy.
     * @param y The initial y-coordinate of the Enemy.
     */

    public Enemy(float x, float y) {
        super(x, y);
        this.speed = 80f;
        this.oscillationDistance = 80f; //The maximum distance the enemy travels
        initialise();
    }

    public static void load() {
        Texture extra = new Texture(Gdx.files.internal("mobs.png"));

        int frameWidth = 16;
        int frameHeight = 15;

        EnemyImageRegion = new TextureRegion(extra, 96, 64, frameWidth, frameHeight);
    }

    public static void loadAnimation() {
        Texture enemySheet = new Texture(Gdx.files.internal("mobs.png"));
        int frameWidth = 16;
        int frameHeight = 16;

        Array<TextureRegion> enemyStillFrames = new Array<>(TextureRegion.class);
        Array<TextureRegion> enemyLeftFrames = new Array<>(TextureRegion.class);
        Array<TextureRegion> enemyRightFrames = new Array<>(TextureRegion.class);
        Array<TextureRegion> enemyUpFrames = new Array<>(TextureRegion.class);
        Array<TextureRegion> enemyDownFrames = new Array<>(TextureRegion.class);

        for (int col = 6; col < 9; col++) {
            enemyStillFrames.add(new TextureRegion(enemySheet, col * frameWidth, 4 * frameHeight, frameWidth , frameHeight));
            enemyLeftFrames.add(new TextureRegion(enemySheet, col * frameWidth, 5 * frameHeight, frameWidth, frameHeight));
            enemyRightFrames.add(new TextureRegion(enemySheet, col * frameWidth, 6 * frameWidth, frameWidth, frameHeight));
            enemyUpFrames.add(new TextureRegion(enemySheet, col * frameWidth, 7 * frameHeight, frameWidth, frameHeight));
            enemyDownFrames.add(new TextureRegion(enemySheet, col * frameWidth, 4 * frameHeight, frameWidth, frameHeight));
        }

        enemyStillAnimation = new Animation<>(0.3f, enemyStillFrames);
        enemyLeftAnimation = new Animation<>(0.3f, enemyLeftFrames);
        enemyRightAnimation = new Animation<>(0.3f, enemyRightFrames);
        enemyUpAnimation = new Animation<>(0.3f, enemyUpFrames);
        enemyDownAnimation = new Animation<>(0.3f, enemyDownFrames);
    }


    /**
     * loads the enemy's animation, if there is no animation to begin with.
     * sets the initial frame of the animation
     */
    public void initialise() {
        // Check if the enemy animation is not loaded
        if (enemyStillAnimation == null) {
            // Load the enemy animation frames
            loadAnimation();
        }

        // Get the frame at time 'sinusInput' and set it as the current enemyRegion
        setEnemyRegion(enemyStillAnimation.getKeyFrame(sinusInput, true));
    }

    /**
     * This method allows the enemy to move in all four directions (LEFT, RIGHT, UP, DOWN)
     * @param delta
     */
    public void move(float delta) {
        float movingDistance = speed * delta;
        float newX = x;
        float newY = y;

        distanceWalked += movingDistance;

        if (distanceWalked >= oscillationDistance) {
            /**
             * If the enemy has walked further than the oscillation distance, change directions randomly by
             * generating a random integer.
             */
            Random random = new Random();
            int randomDirection = random.nextInt(5);
            setCurrentDirection(randomDirection); //Set the new direction to be the one randomly generated
            distanceWalked = 0; //Reset the distance walked
        }

        /**
         * Based on the current direction, it updates the enemy's x and y coordinates
         */
        switch (currentDirection) {
            case STILL:
                break;
            case MOVING_UP:
                newY += movingDistance;
                break;
            case MOVING_DOWN:
                newY -= movingDistance;
                break;
            case MOVING_LEFT:
                newX -= movingDistance;
                break;
            case MOVING_RIGHT:
                newX += movingDistance;
                break;
        }

        if (!collidesWithWalls(newX,newY,GameScreen.getMazeArray())) {
            x = newX; // upon collision, the enemy stops moving
            y = newY;
        }
    }

    /**
     * All the mazeArray values with which the Enemy should collide
     * 0 = a regular wall
     * 1 = entry after the character starts the game
     * 2 = exit before a key is acquired
     * 8 = the shadow of a wall
     * 10 = a movable wall
     */
    private boolean collidesWithWalls(float newX, float newY, int[][] mazeArray) {
        float marginTop = 0.5f * 50f;
        float marginBottomLeft = 0.5f * 50f;

        // Shadow Wall
        if (collidesWithMargin(newX, newY, marginTop, marginBottomLeft, 8, mazeArray)) {
            return true;
        }

        // Movable Wall
        if (collidesWithMargin(newX, newY, 0.45f * 50f, 0.45f * 50f, 10, mazeArray)) {
            return true;
        }

        // Normal Wall
        if (collidesWithMargin(newX, newY, 0.4f * 50f, 0.5f * 50f, 0, mazeArray)) {
            return true;
        }

        // Entry, Exit, Trap, Angel
        if (collidesWithMargin(newX, newY, 0.5f * 50, 0.05f * 50, 1, mazeArray) ||
                collidesWithMargin(newX, newY, 0.5f * 50, 0.05f * 50, 2, mazeArray) ||
                collidesWithMargin(newX, newY, 0.5f * 50, 0.05f * 50, 3, mazeArray) ||
                collidesWithMargin(newX, newY, 0.5f * 50, 0.05f * 50, 7, mazeArray)) {
            return true;
        }

        return false;
    }

    private boolean collidesWithMargin(float x, float y, float top, float bottomLeft, int value, int[][] mazeArray) {
        float xTop = x + top;
        float yTop = y + top;
        float xBottomLeft = x + bottomLeft;
        float yBottomLeft = y + bottomLeft;

        int cellXTop = (int) (xTop / 50);
        int cellYTop = (int) (yTop / 50);

        int cellXBottomLeft = (int) (xBottomLeft / 50);
        int cellYBottomLeft = (int) (yBottomLeft / 50);

        if (cellXTop < 0 || cellXTop >= mazeArray.length || cellYTop < 0 || cellYTop >= mazeArray[0].length ||
                cellXBottomLeft < 0 || cellXBottomLeft >= mazeArray.length || cellYBottomLeft < 0 || cellYBottomLeft >= mazeArray[0].length) {
            return true;  // Outside maze boundaries
        }

        return mazeArray[cellXTop][cellYTop] == value || mazeArray[cellXBottomLeft][cellYBottomLeft] == value;
    }

    /**
     * This method is used, when the enemy is rendered in the GameScreen class.
     * It updates the enemy's current animation based on its current direction.
     * It draws the enemy with its width and height
     * @param delta
     * @param batch
     * @return
     */
    public TextureRegion render(float delta,SpriteBatch batch) {
        // delta = the time elapsed since the last frame or update
        // Increment the state time by the time passed since the last frame
        stateTime += delta;

        // Determine the current animation based on the enemy's state
        Animation<TextureRegion> currentAnimation = switch (currentDirection) {
            case MOVING_LEFT -> enemyLeftAnimation;
            case MOVING_RIGHT -> enemyRightAnimation;
            case MOVING_UP -> enemyUpAnimation;
            case MOVING_DOWN -> enemyDownAnimation;
            default -> enemyStillAnimation;
        };

        // Get the current frame of the animation at the updated state time
        // The 'false' parameter indicates no looping of the animation
        enemyRegion = currentAnimation.getKeyFrame(stateTime, false);

        batch.begin();
        batch.draw(enemyRegion,getX(),getY(),50,50);
        batch.end();

        // Check if the animation has completed (not looping) and reset the state time
        if (currentAnimation.isAnimationFinished(stateTime)) {
            stateTime = 0f;
        }

        return enemyRegion;
    }

    public static void renderEnemies(SpriteBatch spriteBatch, float viewportWidth, float viewportHeight, int characterEnemies) {
        float spacing = 20;
        float enemyWidth = 40;
        float enemyHeight = 40;

        float enemyX = viewportWidth - 50 ;
        float enemyY = viewportHeight + 100;

        for (int i = 0; i < characterEnemies; i++) {
            TextureRegion currentEnemyFrame = Enemy.getEnemyImageRegion();
            spriteBatch.draw(currentEnemyFrame, enemyX - i * spacing,enemyY, enemyWidth, enemyHeight);
        }
    }

    public static TextureRegion getEnemyImageRegion() {
        return EnemyImageRegion;
    }
    public void setEnemyRegion(TextureRegion enemyRegion) {
        this.enemyRegion = enemyRegion;
    }

    public void setCurrentDirection(int currentDirection) {
        this.currentDirection = currentDirection;
    }
}