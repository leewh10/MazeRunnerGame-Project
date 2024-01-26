package de.tum.cit.ase.maze;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Array;

public class Treasure {
    private static TextureRegion TreasurePointImageRegion;
    private static TextureRegion treasureRegion;
    private static Animation<TextureRegion> treasureAnimation;
    private static float treasureStateTime;
    public static void loadTreasure() {
        Texture things = new Texture(Gdx.files.internal("things.png"));

        int frameWidth = 16;
        int frameHeight = 15;

        TreasurePointImageRegion = new TextureRegion(things, 6*16,0 , frameWidth, frameHeight);
    }
    public static void treasureImageAnimation() {
        Texture treasure = new Texture(Gdx.files.internal("things.png"));
        int frameWidth = 16;
        int frameHeight = 16;
        int animationFrames = 4;

        Array<TextureRegion> treasureFrames = new Array<>(TextureRegion.class);
        for (int row = 0; row < animationFrames; row++) {
            treasureFrames.add(new TextureRegion(treasure, 6*frameWidth,row * frameHeight, frameWidth, frameHeight));
        }
        treasureAnimation = new Animation<>(0.3f, treasureFrames);
    }
    public static TextureRegion renderTreasure() {
        treasureStateTime += Gdx.graphics.getDeltaTime();
        treasureRegion = treasureAnimation.getKeyFrame(treasureStateTime,true);
        return treasureRegion;
    }

    public static TextureRegion getTreasureRegion() {
        return treasureRegion;
    } public static TextureRegion getTreasurePointImageRegion() {
        return TreasurePointImageRegion;
    }
}
