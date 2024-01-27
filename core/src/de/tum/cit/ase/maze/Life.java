package de.tum.cit.ase.maze;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Array;

public class Life extends MapObject{
    private static Animation<TextureRegion> lifeAnimation;
    private static float lifeStateTime;

    public static void animation() {
        Texture life = new Texture(Gdx.files.internal("objects.png"));
        int frameWidth = 16;
        int frameHeight = 16;
        int animationFrames = 4;

        Array<TextureRegion> lifeFrames = new Array<>(TextureRegion.class);
        for (int col = 0; col < animationFrames; col++) {
            lifeFrames.add(new TextureRegion(life, col * frameWidth,3 * frameHeight, frameWidth, frameHeight));
        }
        lifeAnimation = new Animation<>(0.2f, lifeFrames);
    }

    public static void render(SpriteBatch spriteBatch, float delta, float viewportWidth, float viewportHeight, int characterLives) {
        float spacing = 20;
        float lifeWidth = 40;
        float lifeHeight = 40;

        float lifeX = viewportWidth + 20 + (characterLives * spacing) / 2;
        float lifeY = viewportHeight + 100;

        lifeStateTime += Gdx.graphics.getDeltaTime(); // Update animation time

        for (int i = 0; i < characterLives; i++) {
            TextureRegion currentLifeFrame = Life.getLifeAnimation().getKeyFrame(lifeStateTime, true);
            spriteBatch.draw(currentLifeFrame, lifeX + i * spacing, lifeY, lifeWidth, lifeHeight);
        }
    }



    public static Animation<TextureRegion> getLifeAnimation() {
        return lifeAnimation;
    }

    public static float getLifeStateTime() {
        return lifeStateTime;
    }
}
