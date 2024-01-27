package de.tum.cit.ase.maze;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class Entry extends MapObject{
    private static TextureRegion EntryPointImageRegion;
    public static void load() {
        int frameWidth = 16;
        int frameHeight = 15;
        Texture map = new Texture(Gdx.files.internal("basictiles.png"));

        EntryPointImageRegion = new TextureRegion(map,32, 96, frameWidth, frameHeight);
    }
    public static TextureRegion getEntryPointImageRegion() {
        return EntryPointImageRegion;
    }

}
