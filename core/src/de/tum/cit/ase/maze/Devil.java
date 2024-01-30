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
public class Devil extends GameObject {
    private static Animation<TextureRegion> devilStillAnimation;
    private static Animation<TextureRegion> devilLeftAnimation;
    private static Animation<TextureRegion> devilRightAnimation;
    private static Animation<TextureRegion> devilUpAnimation;
    private static Animation<TextureRegion> devilDownAnimation;
    private static TextureRegion devilImageRegion;

    private float stateTime = 0f;
    private float speed;
    private float sinusInput = 0f;

    // Constants to represent enemy states
    private static final int MOVING_LEFT = 1;
    private static final int MOVING_RIGHT = 2;
    private static final int MOVING_UP = 3;
    private static final int MOVING_DOWN = 4;
    private GameScreen gameScreen;
    private Animation<TextureRegion> currentAnimation;

    private int currentDirection;  //


    /**
     * Constructor for Devil.
     *
     * @param x The initial x-coordinate of the Enemy.
     * @param y The initial y-coordinate of the Enemy.
     * sets the speed of the devil
     */

    public Devil(float x, float y) {
        super(x, y);
        this.speed = 120f;
        initialise();
    }

    public void initialise() {
        // Check if the enemy animation is not loaded
        if (devilStillAnimation == null) {
            // Load the enemy animation frames
            loadAnimation();
        }

        // Set the initial frame of the animation
        // Get the frame at time 'sinusInput' and set it as the current enemyRegion
        setDevilImageRegion(devilStillAnimation.getKeyFrame(sinusInput, true));
    }


    public static void loadAnimation() {
        /**
         * grim reaper
         * https://finalbossblues.com/timefantasy/freebies/grim-reaper-sprites/
         * Sprite sheet created by Jason
         */
        Texture devil = new Texture(Gdx.files.internal("reaper_blade_1.png"));


        int frameWidth = 32;
        int frameHeight = 36;


        Array<TextureRegion> devilStillFrames = new Array<>(TextureRegion.class);
        Array<TextureRegion> devilLeftFrames = new Array<>(TextureRegion.class);
        Array<TextureRegion> devilRightFrames = new Array<>(TextureRegion.class);
        Array<TextureRegion> devilUpFrames = new Array<>(TextureRegion.class);
        Array<TextureRegion> devilDownFrames = new Array<>(TextureRegion.class);

        for (int col = 0; col < 3 ; col++) {
            devilStillFrames.add(new TextureRegion(devil,col * frameWidth, 0, frameWidth, frameHeight));
            devilDownFrames.add(new TextureRegion(devil,col * frameWidth, 0, frameWidth, frameHeight));
            devilUpFrames.add(new TextureRegion(devil,col * frameWidth, 3 * frameHeight, frameWidth, frameHeight));
            devilLeftFrames.add(new TextureRegion(devil,col * frameWidth, frameHeight, frameWidth, frameHeight));
            devilRightFrames.add(new TextureRegion(devil,col * frameWidth, 2 * frameHeight, frameWidth, frameHeight));
        }


        devilStillAnimation = new Animation<>(0.3f, devilStillFrames);
        devilLeftAnimation = new Animation<>(0.3f, devilLeftFrames);
        devilRightAnimation = new Animation<>(0.3f, devilRightFrames);
        devilUpAnimation = new Animation<>(0.3f, devilUpFrames);
        devilDownAnimation = new Animation<>(0.3f, devilDownFrames);
    }

    /**
     * moveTowardsCharacter method allows the devil to follow the character
     * This method is used in the GameScreen class while the devil is rendered
     *
     * @param characterX
     * @param characterY
     * @param delta
     */
    public void moveTowardsCharacter(float characterX, float characterY, float delta) {
        updateCurrentAnimation();

        float deltaX = characterX - x;
        float deltaY = characterY - y;

        float absDeltaX = Math.abs(deltaX);
        float absDeltaY = Math.abs(deltaY);


        if (absDeltaX > absDeltaY) {
            x += MathUtils.clamp(deltaX, -speed * delta, speed * delta);
            currentDirection = (deltaX > 0) ? MOVING_RIGHT : MOVING_LEFT;

        } else {
            y += MathUtils.clamp(deltaY, -speed * delta, speed * delta);
            currentDirection = (deltaY > 0) ? MOVING_UP : MOVING_DOWN;
        }
    }

    /**
     *
     */
    private void updateCurrentAnimation() {
        switch (currentDirection) {
            case 1:
                currentAnimation = devilLeftAnimation;
                break;
            case 2:
                currentAnimation = devilRightAnimation;
                break;
            case 3:
                currentAnimation = devilUpAnimation;
                break;
            case 4:
                currentAnimation = devilDownAnimation;
                break;
            default:
                currentAnimation = devilStillAnimation;
        }
    }

    public TextureRegion render(float delta, SpriteBatch batch) {
        // delta = the time elapsed since the last frame or update
        // Increment the state time by the time passed since the last frame
        stateTime += delta;

        // Determine the current animation based on the enemy's state
        currentAnimation = switch (currentDirection) {
            case MOVING_LEFT -> devilLeftAnimation;
            case MOVING_RIGHT -> devilRightAnimation;
            case MOVING_UP -> devilUpAnimation;
            case MOVING_DOWN -> devilDownAnimation;
            default -> devilStillAnimation;
        };

        // Get the current frame of the animation at the updated state time
        // The 'false' parameter indicates no looping of the animation
        devilImageRegion = currentAnimation.getKeyFrame(stateTime, false);
        batch.begin();
        batch.draw(devilImageRegion,getX(),getY(),90,90);
        batch.end();

        // Check if the animation has completed (not looping) and reset the state time
        if (currentAnimation.isAnimationFinished(stateTime)) {
            stateTime = 0f;
        }
        return devilImageRegion;
    }



    //Getters and Setters


    public static TextureRegion getDevilImageRegion() {
        return devilImageRegion;
    }

    public static void setDevilImageRegion(TextureRegion devilImageRegion) {
        Devil.devilImageRegion = devilImageRegion;
    }

    public int getCurrentDirection() {
        return currentDirection;
    }

    public void setCurrentDirection(int currentDirection) {
        this.currentDirection = currentDirection;
    }
}