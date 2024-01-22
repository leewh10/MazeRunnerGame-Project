package de.tum.cit.ase.maze;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class GuardianAngel {
    private static TextureRegion angelImageRegion;

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


}
