package de.tum.cit.ase.maze;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Array;

public class Wall extends MapObject{
    /**
     * The Wall restricts the character movement by rerouting the characters motion
     * They are set the borders for the maze and throughout the maze
     */
    private static TextureRegion WallImageRegion;
    private static TextureRegion WallShadowImageRegion;
    private static TextureRegion MovableWallImageRegion;

    public static void load() {
    int frameWidth = 16;
    int frameHeight = 16;
    Texture map = new Texture(Gdx.files.internal("basictiles.png"));

    WallImageRegion = new TextureRegion(map, 16, 0, frameWidth, frameHeight);
    WallShadowImageRegion = new TextureRegion(map, 16*2, 0, frameWidth, frameHeight);
    MovableWallImageRegion = new TextureRegion(map,16*6,16, frameWidth,frameHeight);
    }
    public static TextureRegion getWallImageRegion() {
        return WallImageRegion;
    }

    public static TextureRegion getWallShadowImageRegion() {
        return WallShadowImageRegion;
    }

    public static TextureRegion getMovableWallImageRegion() {
        return MovableWallImageRegion;
    }



    /**
     * The Movable Wall restricts the character movement by acting as a regular Wall until the Lever is pulled
     * After the Lever is pulled, the character can access the area that was previously restricted
     */
    private static TextureRegion wallBreakingRegion;
    private static Animation<TextureRegion> wallBreakingAnimation;
    private static float wallBreakingStateTime;

    public static void animation() {
        Texture wallBreaking = new Texture(Gdx.files.internal("objects.png"));
        int frameWidth = 33;
        int frameHeight = 33;
        int animationFrames = 6;

        Array<TextureRegion> treasureFrames = new Array<>(TextureRegion.class);
        for (int col = 0; col < animationFrames; col++) {
            treasureFrames.add(new TextureRegion(wallBreaking, 5*frameWidth + col*frameWidth, 3*frameHeight, frameWidth, frameHeight));
        }
        wallBreakingAnimation = new Animation<>(1, treasureFrames);
    }
    public static TextureRegion renderTexture() {
        wallBreakingStateTime += Gdx.graphics.getDeltaTime();
        wallBreakingRegion = wallBreakingAnimation.getKeyFrame(wallBreakingStateTime,false);
        return wallBreakingRegion;
    }
    public static void setWallBreakingStateTime(float wallBreakingStateTime) {
        Wall.wallBreakingStateTime = wallBreakingStateTime;
    }

}
