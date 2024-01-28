package de.tum.cit.ase.maze;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import com.badlogic.gdx.math.MathUtils;
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

    private int currentState = STILL;
    private GameScreen gameScreen;
    private float distanceWalked = 0;
    private Animation<TextureRegion> currentAnimation;

    private int currentDirection;  // 0: Still 1: Up


    /**
     * Constructor for Enemy.
     *
     * @param x The initial x-coordinate of the Enemy.
     * @param y The initial y-coordinate of the Enemy.
     */

    public Enemy(float x, float y) {
        super(x, y);
        this.speed = 50f;
        this.oscillationDistance = 70f;
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

        for (int col = 0; col < 3; col++) {
            enemyStillFrames.add(new TextureRegion(enemySheet, col * frameWidth, 4 * frameHeight, frameWidth , frameHeight));
            enemyLeftFrames.add(new TextureRegion(enemySheet, col * frameWidth, 5 * frameHeight, frameWidth, frameHeight));
            enemyRightFrames.add(new TextureRegion(enemySheet, col * frameWidth, 6 * frameWidth, frameWidth, frameHeight));
            enemyUpFrames.add(new TextureRegion(enemySheet, col * frameWidth, 7 * frameHeight, frameWidth, frameHeight));
            enemyDownFrames.add(new TextureRegion(enemySheet, col * frameWidth, 8 * frameHeight, frameWidth, frameHeight));
        }

        enemyStillAnimation = new Animation<>(0.3f, enemyStillFrames);
        enemyLeftAnimation = new Animation<>(0.3f, enemyLeftFrames);
        enemyRightAnimation = new Animation<>(0.3f, enemyRightFrames);
        enemyUpAnimation = new Animation<>(0.3f, enemyUpFrames);
        enemyDownAnimation = new Animation<>(0.3f, enemyDownFrames);
    }


    public void initialise() {
        // Check if the enemy animation is not loaded
        if (enemyStillAnimation == null) {
            // Load the enemy animation frames
            loadAnimation();
        }

        // Set the initial frame of the animation
        // Get the frame at time 'sinusInput' and set it as the current enemyRegion
        setEnemyRegion(enemyStillAnimation.getKeyFrame(sinusInput, true));
    }

    public void move(float delta) {
        updateCurrentAnimation();

        float newX = x;
        float newY = y;

        float movingDistance = speed * delta;

        distanceWalked += movingDistance;

        if (distanceWalked >= oscillationDistance) {
            Random random = new Random();
            int randomDirection = random.nextInt(4); //
            setCurrentDirection(randomDirection);
            distanceWalked = 0;
        }

        switch (currentDirection) {
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
            x = newX;
            y = newY;
        }
    }

    private boolean collidesWithWalls(float newX, float newY, int[][] mazeArray) {
        float collisionMarginTopRight = 0.5f * 50;
        float collisionMarginDownLeft = 0.05f * 50;

        float xWithCollisionMargin = newX + collisionMarginTopRight;
        float yWithCollisionMargin = newY + collisionMarginDownLeft;

        int cellX = (int) (xWithCollisionMargin / 50);
        int cellY = (int) (yWithCollisionMargin / 50);


        // Check if the adjusted position is within the maze boundaries
        if (cellX < 0 || cellX >= mazeArray.length || cellY < 0 || cellY >= mazeArray[0].length) {
            return true;  // Outside maze boundaries
        }

        boolean collisionTop = mazeArray[cellX][cellY] == 0;
        boolean collisionBottom = mazeArray[cellX][cellY] == 0;
        boolean collisionLeft = mazeArray[cellX][cellY] == 0;
        boolean collisionRight = mazeArray[cellX][cellY] == 0;

        boolean collisionTopEntry = mazeArray[cellX][cellY] == 1;
        boolean collisionBottomEntry = mazeArray[cellX][cellY] == 1;
        boolean collisionLeftEntry = mazeArray[cellX][cellY] == 1;
        boolean collisionRightEntry = mazeArray[cellX][cellY] == 1;

        boolean collisionTopExit = mazeArray[cellX][cellY] == 2;
        boolean collisionBottomExit = mazeArray[cellX][cellY] == 2;
        boolean collisionLeftExit = mazeArray[cellX][cellY] == 2;
        boolean collisionRightExit = mazeArray[cellX][cellY] == 2;

        boolean collisionTopTrap = mazeArray[cellX][cellY] == 3;
        boolean collisionBottomTrap = mazeArray[cellX][cellY] == 3;
        boolean collisionLeftTrap = mazeArray[cellX][cellY] == 3;
        boolean collisionRightTrap = mazeArray[cellX][cellY] == 3;

        boolean collisionTopShadow = mazeArray[cellX][cellY] == 8; //
        boolean collisionBottomShadow = mazeArray[cellX][cellY] == 8;
        boolean collisionLeftShadow = mazeArray[cellX][cellY] == 8;
        boolean collisionRightShadow = mazeArray[cellX][cellY] == 8;

        boolean collisionTopMove = mazeArray[cellX][cellY] == 10; //
        boolean collisionBottomMove = mazeArray[cellX][cellY] == 10;
        boolean collisionLeftMove = mazeArray[cellX][cellY] == 10;
        boolean collisionRightMove = mazeArray[cellX][cellY] == 10;

        return collisionTop || collisionBottom || collisionLeft || collisionRight
                || collisionBottomEntry || collisionLeftEntry || collisionRightEntry|| collisionTopEntry
                || collisionBottomExit || collisionLeftExit || collisionRightExit|| collisionTopExit
                || collisionBottomTrap || collisionLeftTrap || collisionRightTrap|| collisionTopTrap
                || collisionBottomShadow || collisionLeftShadow || collisionRightShadow|| collisionTopShadow
                || collisionBottomMove || collisionLeftMove || collisionRightMove|| collisionTopMove ;
    }
    private void updateCurrentAnimation() {
        switch (currentDirection) {
            case 0:
                currentAnimation = enemyUpAnimation;
                break;
            case 2:
                currentAnimation = enemyLeftAnimation;
                break;
            case 3:
                currentAnimation = enemyRightAnimation;
                break;
            default:
                currentAnimation = enemyDownAnimation;
        }
    }

    public TextureRegion render(float delta,SpriteBatch batch) {


        // delta = the time elapsed since the last frame or update
        // Increment the state time by the time passed since the last frame
        stateTime += delta;

        // Determine the current animation based on the enemy's state
        Animation<TextureRegion> currentAnimation = switch (currentState) {
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

    public static void renderEnemies(SpriteBatch spriteBatch, float delta, float viewportWidth, float viewportHeight, int characterEnemies) {
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