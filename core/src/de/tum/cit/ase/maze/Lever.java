package de.tum.cit.ase.maze;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Array;

public class Lever {
    private static TextureRegion leverPointImageRegion;
    private static TextureRegion leverEndPointImageRegion;

    private static TextureRegion leverRegion;
    private static Animation<TextureRegion> leverAnimation;
    private static float leverStateTime;

    public static void loadLever() {
        Texture things = new Texture(Gdx.files.internal("things.png"));

        int frameWidth = 16;
        int frameHeight = 15;

        leverPointImageRegion = new TextureRegion(things, 3 * 16, 4 * 16, frameWidth, frameHeight);
        leverEndPointImageRegion = new TextureRegion(things, 5 * 16, 4 * 16, frameWidth, frameHeight);
    }

    public static void leverImageAnimation() {
        Texture things = new Texture(Gdx.files.internal("things.png"));
        int frameWidth = 16;
        int frameHeight = 16;
        int animationFrames = 3;

        Array<TextureRegion> leverFrames = new Array<>(TextureRegion.class);
        for (int col = 0; col < animationFrames; col++) {
            leverFrames.add(new TextureRegion(things, 3 * 16 + col * frameWidth, 4 * frameHeight, frameWidth, frameHeight));
        }
        leverAnimation = new Animation<>(0.3f, leverFrames);
    }

    public static TextureRegion renderLever() {
        if (leverAnimation == null) {
            leverImageAnimation(); // Initialize leverAnimation if not already initialized
        }

        leverStateTime += Gdx.graphics.getDeltaTime();
        leverRegion = leverAnimation.getKeyFrame(leverStateTime, false);

        return leverRegion;
    }

    public static TextureRegion getLeverPointImageRegion() {
        return leverPointImageRegion;
    }

    public static TextureRegion getLeverEndPointImageRegion() {
        return leverEndPointImageRegion;
    }

    public static void setLeverEndPointImageRegion(TextureRegion leverEndPointImageRegion) {
        Lever.leverEndPointImageRegion = leverEndPointImageRegion;
    }

    public static void setLeverPointImageRegion(TextureRegion leverPointImageRegion) {
        Lever.leverPointImageRegion = leverPointImageRegion;
    }

    public static TextureRegion getLeverRegion() {
        return leverRegion;
    }

    public static void setLeverRegion(TextureRegion leverRegion) {
        Lever.leverRegion = leverRegion;
    }

    public static Animation<TextureRegion> getLeverAnimation() {
        return leverAnimation;
    }

    public static void setLeverAnimation(Animation<TextureRegion> leverAnimation) {
        Lever.leverAnimation = leverAnimation;
    }

    public static float getLeverStateTime() {
        return leverStateTime;
    }

    public static void setLeverStateTime(float leverStateTime) {
        Lever.leverStateTime = leverStateTime;
    }
}
