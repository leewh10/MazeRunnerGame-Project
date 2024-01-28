package de.tum.cit.ase.maze;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Array;

public class Lamps extends MapObject{
    /**
     * Purely aesthetic attribute of the Maze stationed on either side of the Exit
     */
    private static TextureRegion LampImageRegion;
    private static Animation<TextureRegion> lampAnimation;
    private static float lampStateTime;

    public static void animation() {

        Texture lamp = new Texture(Gdx.files.internal("basictiles.png"));
            int frameWidth = 16;
            int frameHeight = 16;
            int animationFrames = 2;

            Array<TextureRegion> lampFrames = new Array<>(TextureRegion.class);
            for (int col = 0; col < animationFrames; col++) {
                lampFrames.add(new TextureRegion(lamp, 4*frameWidth + col*frameWidth,7*frameHeight, frameWidth, frameHeight));
            }
            lampAnimation = new Animation<>(0.2f, lampFrames);
        }

        public static TextureRegion renderTexture() {
            lampStateTime += Gdx.graphics.getDeltaTime();
            LampImageRegion = lampAnimation.getKeyFrame(lampStateTime,true);
            return LampImageRegion;
        }



    public static TextureRegion getLampImageRegion() {
            return LampImageRegion;
        }
    }
