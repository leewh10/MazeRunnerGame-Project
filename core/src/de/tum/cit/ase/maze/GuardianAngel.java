package de.tum.cit.ase.maze;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.Array;

public class GuardianAngel extends GameObject {
    /**
     * A dynamic Non Player Character that, upon Collision, gives the Character a Life
     */
    private static Animation<TextureRegion> angelDownAnimation;
    private static float angelStateTime;
    private static TextureRegion angelImageRegion;
    private static TextureRegion defaultAngelImageRegion;



    public GuardianAngel(float x, float y) {
        super(x, y);
    }

    public static void loadAnimation() {
        /**
         * https://opengameart.org/node/83286
         * Sprite sheet created by AntumDeluge
         */
        Texture angel = new Texture(Gdx.files.internal("angel-f-001.png"));

        int frameWidth = 48;
        int frameHeight = 65;

        Array<TextureRegion> angelDownFrames = new Array<>(TextureRegion.class);

        for (int col = 0; col < 3 ; col++) {
            angelDownFrames.add(new TextureRegion(angel,col * frameWidth, 2 * frameHeight, frameWidth, frameHeight));
        }

        angelDownAnimation = new Animation<>(0.5f, angelDownFrames);
    }

    public TextureRegion render(float delta) {
        angelStateTime += delta;

        defaultAngelImageRegion = angelDownAnimation.getKeyFrame(angelStateTime,true);
        return defaultAngelImageRegion;
    }


    // Initialize the texture region in a static block
    public static void load(){
        Texture angel = new Texture(Gdx.files.internal("angel-f-001.png"));

        int frameWidth = 48;
        int frameHeight = 65;

        // Create a TextureRegion for the image
        angelImageRegion = new TextureRegion(angel, 50, 130, frameWidth, frameHeight);
    }

    public static TextureRegion getAngelImageRegion() {
        return angelImageRegion;
    }

    public static TextureRegion getDefaultAngelImageRegion() {
        return defaultAngelImageRegion;
    }

}
