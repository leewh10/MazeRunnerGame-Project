package de.tum.cit.ase.maze;

import com.badlogic.gdx.graphics.g2d.TextureRegion;

public abstract class MapObject {
    /**
     * Loads the static texture for the MapObject from a .png
     */
    public static void load() {};

    /**
     * Loads the dynamic texture for moving MapObjects from one or many .pngs
     */
    public static void animation() {};

    /**
     * Used to render the previously created textures at the specified location on the gameScreen
     * @return
     */
    public static TextureRegion renderTexture() {
        return null;
    }

    /**
     * Primarily used to render the HUD of the specified texture at a specified location
     *
     */
    public static void render() {}
}
