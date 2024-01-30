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
        updateCurrentAnimation(); //updates the animation to match the direction of movement

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
            x = newX; //upon collision, the enemy stops moving
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

        //Shadow Wall
        float shadowWallCollisionMarginTop = 0.3f * 50f;
        float shadowWallCollisionMarginBottomLeft = 0.5f * 50f;

        float xShadowWallTR = newX + shadowWallCollisionMarginTop;
        float yShadowWallTR = newY + shadowWallCollisionMarginTop;
        float xShadowWallBL = newX + shadowWallCollisionMarginBottomLeft;
        float yShadowWallBL = newY + shadowWallCollisionMarginBottomLeft;

        int shadowWallCellXTR = (int) (xShadowWallTR / 50) ;
        int shadowWallCellYTR = (int) (yShadowWallTR / 50) ;
        if (shadowWallCellXTR < 0 || shadowWallCellXTR >= mazeArray.length || shadowWallCellYTR < 0 || shadowWallCellYTR >= mazeArray[0].length) {
            return true;  // Outside maze boundaries
        }

        int shadowWallCellXBL = (int) (xShadowWallBL / 50) ;
        int shadowWallCellYBL = (int) (yShadowWallBL / 50) ;
        if (shadowWallCellXBL < 0 || shadowWallCellXBL >= mazeArray.length || shadowWallCellYBL < 0 || shadowWallCellYBL >= mazeArray[0].length) {
            return true;  // Outside maze boundaries
        }

        boolean collisionTopShadow = mazeArray[shadowWallCellXTR][shadowWallCellYTR] == 8; //
        boolean collisionBottomShadow = mazeArray[shadowWallCellXBL][shadowWallCellYBL] == 8;
        boolean collisionLeftShadow = mazeArray[shadowWallCellXBL][shadowWallCellYBL] == 8;
        boolean collisionRightShadow = mazeArray[shadowWallCellXTR][shadowWallCellYTR] == 8;

        //Movable Wall
        float movableWallCollisionMargin = 0.45f * 50f;
        float xMovWall = newX + movableWallCollisionMargin;
        float yMovWall = newY + movableWallCollisionMargin;

        int cellX1 = (int) (xMovWall / 50);
        int cellY1 = (int) (yMovWall / 50);
        if (cellX1 < 0 || cellX1 >= mazeArray.length || cellY1 < 0 || cellY1 >= mazeArray[0].length) {
            return true;
        }
        boolean collisionTopMove = mazeArray[cellX1][cellY1] == 10; //
        boolean collisionBottomMove = mazeArray[cellX1][cellY1] == 10;
        boolean collisionLeftMove = mazeArray[cellX1][cellY1] == 10;
        boolean collisionRightMove = mazeArray[cellX1][cellY1] == 10;


        //Normal Wall
        float normalWallCollisionMarginTop = 0.4f * 50f;
        float normalWallCollisionMarginBottomLeft = 0.5f * 50f;

        float xNormalWallTR = newX + normalWallCollisionMarginTop;
        float yNormalWallTR = newY + normalWallCollisionMarginTop;
        float xNormalWallBL = newX + normalWallCollisionMarginBottomLeft;
        float yNormalWallBL = newY + normalWallCollisionMarginBottomLeft;

        int normalWallCellXTR = (int) (xNormalWallTR / 50) ;
        int normalWallCellYTR = (int) (yNormalWallTR / 50) ;
        if (normalWallCellXTR < 0 || normalWallCellXTR >= mazeArray.length || normalWallCellYTR < 0 || normalWallCellYTR >= mazeArray[0].length) {
            return true;  // Outside maze boundaries
        }

        int normalWallCellXBL = (int) (xNormalWallBL / 50) ;
        int normalWallCellYBL = (int) (yNormalWallBL / 50) ;
        if (normalWallCellXBL < 0 || normalWallCellXBL >= mazeArray.length || normalWallCellYBL < 0 || normalWallCellYBL >= mazeArray[0].length) {
            return true;  // Outside maze boundaries
        }

        boolean collisionTop = mazeArray[normalWallCellXTR][normalWallCellYTR] == 0;
        boolean collisionBottom = mazeArray[normalWallCellXBL][normalWallCellYBL] == 0;
        boolean collisionLeft = mazeArray[normalWallCellXBL][normalWallCellYBL] == 0;
        boolean collisionRight = mazeArray[normalWallCellXTR][normalWallCellYTR] == 0;


        //////////////////////////////////////////////////

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

        boolean collisionTopAngel = mazeArray[cellX][cellY] == 7;
        boolean collisionBottomAngel = mazeArray[cellX][cellY] == 7;
        boolean collisionLeftAngel = mazeArray[cellX][cellY] == 7;
        boolean collisionRightAngel = mazeArray[cellX][cellY] == 7;


        return collisionTop || collisionBottom || collisionLeft || collisionRight
                || collisionBottomEntry || collisionLeftEntry || collisionRightEntry|| collisionTopEntry
                || collisionBottomExit || collisionLeftExit || collisionRightExit|| collisionTopExit
                || collisionBottomTrap || collisionLeftTrap || collisionRightTrap|| collisionTopTrap
                || collisionBottomAngel || collisionLeftAngel || collisionRightAngel|| collisionTopAngel
                || collisionBottomShadow || collisionLeftShadow || collisionRightShadow|| collisionTopShadow
                || collisionBottomMove || collisionLeftMove || collisionRightMove|| collisionTopMove;
    }

    private void updateCurrentAnimation() {
        switch (currentDirection) {
            case 1:
                currentAnimation = enemyLeftAnimation; //Set the current animation to match the direction of movement
                break;
            case 2:
                currentAnimation = enemyRightAnimation;
                break;
            case 3:
                currentAnimation = enemyUpAnimation;
                break;
            case 4:
                currentAnimation = enemyDownAnimation;
                break;
            default:
                currentAnimation = enemyStillAnimation;
        }
    }

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