package de.tum.cit.ase.maze;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Array;

public class Key extends MapObject{
    /**
     * The Key requires to open the Exit
     * Only appears on the Maze after it is unlocked by the Treasure
     */
    private static TextureRegion KeyImageRegion;
    private static Animation<TextureRegion> keyAnimation;
    private static float keyStateTime;



    public static void animation() {
        /**
         *https://opengameart.org/content/locks-and-keys
         * Sprite sheet created by Kelvin Shadewing
         */
        Texture key = new Texture(Gdx.files.internal("key-gold.png"));

        int frameWidth = 16;
        int frameHeight = 16;
        int animationFrames = 4;

        Array<TextureRegion> keyFrames = new Array<>(TextureRegion.class);
        for (int col = 0; col < animationFrames; col++) {
            keyFrames.add(new TextureRegion(key, col * frameWidth,frameHeight, frameWidth, frameHeight));
        }
        keyAnimation = new Animation<>(0.2f, keyFrames);
    }


    public static TextureRegion renderTexture() {
        keyStateTime += Gdx.graphics.getDeltaTime();
        KeyImageRegion = keyAnimation.getKeyFrame(keyStateTime,true);
        return KeyImageRegion;
    }



    public static void render(SpriteBatch spriteBatch, float delta, float viewportWidth, float viewportHeight, boolean characterKey) {
        float keyWidth = 30;
        float keyHeight = 30;

        float keyX = viewportWidth + 10;
        float keyY = viewportHeight + 80;

        keyStateTime += Gdx.graphics.getDeltaTime(); // Update animation time

        if(characterKey) {
            TextureRegion currentKeyFrame = Key.getKeyAnimation().getKeyFrame(keyStateTime, true);
            spriteBatch.draw(currentKeyFrame, keyX + 65, keyY - 10, keyWidth, keyHeight);
        }
    }



    public static Animation<TextureRegion> getKeyAnimation() {
        return keyAnimation;
    }
}
