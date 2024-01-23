package de.tum.cit.ase.maze;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.Array;

public class GuardianAngel extends GameObject{
    private static Animation<TextureRegion> angelDownAnimation;
    private static Animation<TextureRegion> angelUpAnimation;
    private static Animation<TextureRegion> angelLeftAnimation;
    private static Animation<TextureRegion> angelRightAnimation;
    private static float angelStateTime;
    private float speed;
    private float oscillationDistance;
    private static TextureRegion angelImageRegion;
    private static TextureRegion defaultAngelImageRegion;
    private static final int STILL = 0;
    private static final int MOVING_LEFT = 1;
    private static final int MOVING_RIGHT = 2;
    private static final int MOVING_UP = 3;
    private static final int MOVING_DOWN = 4;
    private boolean movingRight;
    private boolean movingUp;
    private int currentState = STILL;
    private GameScreen gameScreen;
    private static final int RANDOM_DIRECTION = 0;
    private int currentDirection;


    public GuardianAngel(float x, float y, float speed, float oscillationDistance, GameScreen gameScreen) {
        super(x, y);
        this.speed = speed;
        this.movingRight = true;
        this.oscillationDistance = oscillationDistance;
        this.gameScreen = gameScreen;
        initialise();
    }

    public static void loadAngelAnimation() {
        Texture angel = new Texture(Gdx.files.internal("angel-f-001.png"));

        int frameWidth = 48;
        int frameHeight = 65;

        Array<TextureRegion> angelDownFrames = new Array<>(TextureRegion.class);
        Array<TextureRegion> angelUpFrames = new Array<>(TextureRegion.class);
        Array<TextureRegion> angelLeftFrames = new Array<>(TextureRegion.class);
        Array<TextureRegion> angelRightFrames = new Array<>(TextureRegion.class);

        for (int col = 0; col < 3 ; col++) {
            angelDownFrames.add(new TextureRegion(angel,col * frameWidth, 2 * frameHeight, frameWidth, frameHeight));
            angelUpFrames.add(new TextureRegion(angel,col * frameWidth, 0, frameWidth, frameHeight));
            angelLeftFrames.add(new TextureRegion(angel,col * frameWidth, 3 * frameHeight, frameWidth, frameHeight));
            angelRightFrames.add(new TextureRegion(angel,col * frameWidth, frameHeight, frameWidth, frameHeight));
        }

        angelDownAnimation = new Animation<>(0.5f, angelDownFrames);
        angelUpAnimation = new Animation<>(0.5f, angelUpFrames);
        angelLeftAnimation = new Animation<>(0.5f, angelLeftFrames);
        angelRightAnimation = new Animation<>(0.5f, angelRightFrames);
    }

    public void initialise() {
        if (angelDownAnimation == null) {
            loadAngelAnimation();
        }
        setAngelImageRegion(angelDownAnimation.getKeyFrame(angelStateTime,true));
    }

    public TextureRegion render(float delta) {
        angelStateTime += delta;
        moveRandomDirection();

        Animation<TextureRegion> currentAnimation = switch (currentState) {
            case MOVING_RIGHT -> angelRightAnimation;
            case MOVING_LEFT -> angelLeftAnimation;
            case MOVING_UP -> angelUpAnimation;
            default -> angelDownAnimation;
        };
        angelImageRegion = currentAnimation.getKeyFrame(angelStateTime,true);
        defaultAngelImageRegion = angelDownAnimation.getKeyFrame(angelStateTime,true);
        return angelImageRegion;
    }

    public void move(float delta) {
        float deltaX = speed * (movingRight ? 1 : -1);
        float deltaY = speed * (movingUp ? 1 : -1);

        x += deltaX * delta;
        y += deltaY * delta;

        getCurrentFrame();

        if (getX() <= gameScreen.getAngelX()|| getX() >= gameScreen.getAngelX() + oscillationDistance) {
            movingRight = !movingRight;
        }
        if (getY() <= gameScreen.getAngelY()) {
            movingUp = true;
        } else if (getY() >= gameScreen.getAngelY() + oscillationDistance) {
            movingUp = false;
        }

        int randomDirection = MathUtils.random(1,4);
        currentDirection = MOVING_DOWN;

        switch (randomDirection) {
            //1: left, 2: right, 3: up, 4: down
            case 1:
                currentDirection = MOVING_LEFT;
                break;
            case 2:
                currentDirection = MOVING_RIGHT;
                break;
            case 3:
                currentDirection = MOVING_UP;
                break;
            case 4:
                currentDirection = MOVING_DOWN;
                break;
        }
    }
    private void moveRandomDirection() {
        // Your existing switch statement for setting the movingRight and movingUp variables remains unchanged
        int randomDirection = MathUtils.random(1,4);
        switch (randomDirection) {
            //1: left, 2: right, 3: up, 4: down
            case 1:
                currentDirection = MOVING_LEFT;
                break;
            case 2:
                currentDirection = MOVING_RIGHT;
                break;
            case 3:
                currentDirection = MOVING_UP;
                break;
            case 4:
                currentDirection = MOVING_DOWN;
                break;
        }
    }

    public TextureRegion getCurrentFrame() {
        defaultAngelImageRegion = angelDownAnimation.getKeyFrame(angelStateTime,true);
        return switch (currentDirection) {
            case MOVING_UP -> angelUpAnimation.getKeyFrame(angelStateTime, true);
            case MOVING_LEFT -> angelLeftAnimation.getKeyFrame(angelStateTime, true);
            case MOVING_RIGHT -> angelRightAnimation.getKeyFrame(angelStateTime, true);
            default -> angelDownAnimation.getKeyFrame(angelStateTime, true);
        };
    }

    public void updateAnimation(float delta) {
        angelStateTime += delta;
    }

    public void moveLeftRight(float delta) {
        float deltaX = speed * (movingRight ? 1 : -1);
        x += deltaX * delta;

        currentState = (deltaX > 0) ? MOVING_RIGHT : MOVING_LEFT;

        if (getX() <= gameScreen.getAngelX()|| getX() >= gameScreen.getAngelX() + oscillationDistance) {
            movingRight = !movingRight;
        }
    }

    public void moveUpDown(float delta) {
        float deltaY = speed * (movingUp ? 1 : -1);
        y += deltaY * delta;

        currentState = (deltaY > 0) ? MOVING_UP : MOVING_DOWN;

        if (getY() <= gameScreen.getAngelY()) {
            movingUp = true;
        } else if (getY() >= gameScreen.getAngelY() + oscillationDistance) {
            movingUp = false;
        }
    }


    // Initialize the texture region in a static block
    static {
        Texture angel = new Texture(Gdx.files.internal("angel-f-001.png"));

        int frameWidth = 48;
        int frameHeight = 65;

        // Create a TextureRegion for the image
        angelImageRegion = new TextureRegion(angel, 50, 130, frameWidth, frameHeight);
    }

    public static TextureRegion getAngelImageRegion() {
        return angelImageRegion;
    }

    public static Animation<TextureRegion> getAngelDownAnimation() {
        return angelDownAnimation;
    }

    public static void setAngelImageRegion(TextureRegion angelImageRegion) {
        GuardianAngel.angelImageRegion = angelImageRegion;
    }

    public static TextureRegion getDefaultAngelImageRegion() {
        return defaultAngelImageRegion;
    }

}
