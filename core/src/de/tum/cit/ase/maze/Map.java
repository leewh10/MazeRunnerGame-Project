package de.tum.cit.ase.maze;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class Map {


    private static TextureRegion WallImageRegion;
    private static TextureRegion EntryPointImageRegion;
    private static TextureRegion ExitPointImageRegion;
    private static TextureRegion TrapImageRegion;
    private static TextureRegion EnemyImageRegion;
    private static TextureRegion KeyImageRegion;
    private static TextureRegion FloorImageRegion;


    public static void loadBackground() {
        Texture map = new Texture(Gdx.files.internal("basictiles.png"));
        Texture extra = new Texture(Gdx.files.internal("mobs.png"));



        int frameWidth = 16;
        int frameHeight = 15;

        // Create a TextureRegion for the first image
        WallImageRegion = new TextureRegion(map, 16, 0, frameWidth, frameHeight);
        EntryPointImageRegion = new TextureRegion(map, 0, 96, frameWidth, frameHeight);
        ExitPointImageRegion = new TextureRegion(map,32, 96, frameWidth, frameHeight);
        TrapImageRegion = new TextureRegion(map, 16, 96, frameWidth, frameHeight);
        EnemyImageRegion = new TextureRegion(extra, 96, 64, frameWidth, frameHeight);
        KeyImageRegion = new TextureRegion(extra, 0, 0, frameWidth, frameHeight);
        FloorImageRegion = new TextureRegion(map, 1000, 1000, frameWidth, frameHeight);
    }

    public static TextureRegion getFloorImageRegion() {
        return FloorImageRegion;
    }

    public static TextureRegion getWallImageRegion() {
        return WallImageRegion;
    }

    public static TextureRegion getEntryPointImageRegion() {
        return EntryPointImageRegion;
    }

    public static TextureRegion getExitPointImageRegion() {
        return ExitPointImageRegion;
    }

    public static TextureRegion getTrapImageRegion() {
        return TrapImageRegion;
    }

    public static TextureRegion getEnemyImageRegion() {
        return EnemyImageRegion;
    }

    public static TextureRegion getKeyImageRegion() {
        return KeyImageRegion;
    }

}
