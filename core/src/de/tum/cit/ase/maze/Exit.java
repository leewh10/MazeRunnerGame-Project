package de.tum.cit.ase.maze;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Array;

public class Exit extends MapObject{
    /**
     * The point through which the Character exits the Game and wins
     * Only accessible with a Key
     * Otherwise serves as a Wall
     */
    private static TextureRegion ExitPointImageRegion;
    private static TextureRegion ExitRegion;
    private static Animation<TextureRegion> exitAnimation;
    private static float exitStateTime;


    public static void load() {
        Texture map = new Texture(Gdx.files.internal("basictiles.png"));

        int frameWidth = 16;
        int frameHeight = 15;

        ExitPointImageRegion = new TextureRegion(map, 0, 96, frameWidth, frameHeight);
    }


    public static void animation() {
        Texture exit = new Texture(Gdx.files.internal("things.png"));
        int frameWidth = 16;
        int frameHeight = 16;
        int animationFrames = 4;

        Array<TextureRegion> exitFrames = new Array<>(TextureRegion.class);
        for (int row = 0; row < animationFrames; row++) {
            exitFrames.add(new TextureRegion(exit, 0,row * frameHeight, frameWidth, frameHeight));
        }
        exitAnimation = new Animation<>(0.3f, exitFrames);
    }


    public static TextureRegion renderTexture() {
        exitStateTime += Gdx.graphics.getDeltaTime();
        ExitRegion = exitAnimation.getKeyFrame(exitStateTime,true);
        return ExitRegion;
    }


    public static TextureRegion getExitPointImageRegion() {
        return ExitPointImageRegion;
    }
    public static Animation<TextureRegion> getExitAnimation() {
        return exitAnimation;
    }

    public static TextureRegion getExitRegion() {
        return ExitRegion;
    }

    public static float getExitStateTime() {
        return exitStateTime;
    }

    public static void setExitStateTime(float exitStateTime) {
        Exit.exitStateTime = exitStateTime;
    }

}
