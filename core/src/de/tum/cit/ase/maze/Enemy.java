package de.tum.cit.ase.maze;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.Array;

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
    private boolean movingRight;
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
        this.gameScreen = gameScreen;
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
        speed = 5;
        // delta = the time elapsed since the last frame or update

        // Keep track of previous position
        float prevX = x;
        float prevY = y;

        // Generate a random direction (0 = left, 1 = right, 2 = up, 3 = down)
        int randomDirection = MathUtils.random(0, 3);

        // Update the enemy's position based on the random direction
        switch (randomDirection) {
            case 0: // Move left
                x -= speed * delta;
                break;
            case 1: // Move right
                x += speed * delta;
                break;
            case 2: // Move up
                y += speed * delta;
                break;
            case 3: // Move down
                y -= speed * delta;
                break;
        }

        // Check if the new position is within the specified area
        if (!isWithinBounds()) {
            // If out of bounds, revert to the previous position
            x = prevX;
            y = prevY;
        }

        // Update the enemy state based on movement direction
        currentState = determineState(x - prevX, y - prevY);
    }

    private int determineState(float deltaX, float deltaY) {
        if (Math.abs(deltaX) > Math.abs(deltaY)) {
            // Moving horizontally
            return (deltaX > 0) ? MOVING_RIGHT : MOVING_LEFT;
        } else {
            // Moving vertically
            return (deltaY > 0) ? MOVING_UP : MOVING_DOWN;
        }
    }

    private boolean isWithinBounds() {
        // Check if the new position is within the specified 200 by 200 range
        float minX = gameScreen.getEnemyX1();
        float maxX = gameScreen.getEnemyX1()+200;
        float minY = gameScreen.getEnemyY1();
        float maxY = gameScreen.getEnemyY1()+200;

        return x >= minX && x <= maxX && y >= minY && y <= maxY;
    }



    public TextureRegion render(float delta) {
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
}
