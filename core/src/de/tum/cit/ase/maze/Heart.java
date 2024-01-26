package de.tum.cit.ase.maze;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Array;

public class Heart {
    private static Animation<TextureRegion> heartAnimation;
    private static float heartStateTime;

    public static void loadHeartAnimation() {
        Texture heart1 = new Texture(Gdx.files.internal("heart-1.png"));
        Texture heart2 = new Texture(Gdx.files.internal("heart-2.png"));
        Texture heart3 = new Texture(Gdx.files.internal("heart-3.png"));
        Texture heart4 = new Texture(Gdx.files.internal("heart-4.png"));
        Texture heart5 = new Texture(Gdx.files.internal("heart-5.png"));

        Array<TextureRegion> heartFrames = new Array<>(TextureRegion.class);
        heartFrames.add(new TextureRegion(heart1, 0, 0, 16, 16));
        heartFrames.add(new TextureRegion(heart2, 0, 0, 32, 32));
        heartFrames.add(new TextureRegion(heart3, 0, 0, 48, 48));
        heartFrames.add(new TextureRegion(heart4, 0, 0, 64, 64));
        heartFrames.add(new TextureRegion(heart5, 0, 0, 254, 254));

        heartAnimation = new Animation<>(0.2f, heartFrames, Animation.PlayMode.LOOP);
    }

    public static TextureRegion renderHeart() {
        heartStateTime += Gdx.graphics.getDeltaTime();
        return heartAnimation.getKeyFrame(heartStateTime, true);
    }
}
