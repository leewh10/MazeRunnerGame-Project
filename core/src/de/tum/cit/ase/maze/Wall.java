package de.tum.cit.ase.maze;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class Wall {
    private static TextureRegion WallImageRegion;

    public static void loadWall() {
        int frameWidth = 16;
        int frameHeight = 15;
        Texture map = new Texture(Gdx.files.internal("basictiles.png"));

        WallImageRegion = new TextureRegion(map, 16, 0, frameWidth, frameHeight);
    }
    public static TextureRegion getWallImageRegion() {
        return WallImageRegion;
    }

}