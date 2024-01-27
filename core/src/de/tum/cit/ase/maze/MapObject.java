package de.tum.cit.ase.maze;

import com.badlogic.gdx.graphics.g2d.TextureRegion;

public abstract class MapObject {
    public static void load() {
    };
    public static void animation() {
    };
    public static TextureRegion renderTexture() {
        return null;
    }
    public static void render() {
    }
}
