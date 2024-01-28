package de.tum.cit.ase.maze;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Array;

public class Trap extends MapObject{
    /**
     * A stationary Obstacle that causes the Character to lose a Life upon Collision
     */
    private static TextureRegion TrapImageRegion;
    private static Animation<TextureRegion> trapAnimation;
    private static float trapStateTime;

    public static void animation() {
        Texture trap = new Texture(Gdx.files.internal("objects.png"));
        int frameWidth = 16;
        int frameHeight = 16;
        int animationFrames = 7;

        Array<TextureRegion> trapFrames = new Array<>(TextureRegion.class);
        for (int col = 4; col < animationFrames; col++) {
            trapFrames.add(new TextureRegion(trap, col * frameWidth,3 * frameHeight, frameWidth, frameHeight));
        }
        trapAnimation = new Animation<>(0.8f, trapFrames);
    }
    public static TextureRegion renderTexture() {
        trapStateTime += Gdx.graphics.getDeltaTime();
        TrapImageRegion = trapAnimation.getKeyFrame(trapStateTime,true);
        return TrapImageRegion;
    }

    public static TextureRegion getTrapImageRegion() {
        return TrapImageRegion;
    }

}

