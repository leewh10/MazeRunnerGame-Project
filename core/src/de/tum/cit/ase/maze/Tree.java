package de.tum.cit.ase.maze;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class Tree extends MapObject{
    private static TextureRegion TreeImageRegion;
    private static TextureRegion TreeTopImageRegion;

    public static void load() {
        int frameWidth = 16;
        int frameHeight = 16;
        Texture map = new Texture(Gdx.files.internal("basictiles.png"));

        TreeImageRegion = new TextureRegion(map, 4*frameWidth, 9*frameHeight, frameWidth, frameHeight);
        TreeTopImageRegion = new TextureRegion(map, 4*frameWidth, 11*frameHeight, frameWidth, frameHeight);
    }

    public static TextureRegion getTreeImageRegion() {
        return TreeImageRegion;
    }

    public static TextureRegion getTreeTopImageRegion() {
        return TreeTopImageRegion;
    }
}

