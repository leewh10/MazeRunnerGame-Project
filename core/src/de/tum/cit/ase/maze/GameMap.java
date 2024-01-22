package de.tum.cit.ase.maze;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Array;

public class GameMap {

    private static TextureRegion WallImageRegion;
    private static TextureRegion EntryPointImageRegion;
    private static TextureRegion ExitPointImageRegion;
    private static TextureRegion TreasurePointImageRegion;
    private static TextureRegion TrapImageRegion;
    private static TextureRegion EnemyImageRegion;
    private static TextureRegion KeyImageRegion;
    private static TextureRegion ExitRegion;
    private static TextureRegion treasureRegion;



    //Life Image
    private static Animation<TextureRegion> keyAnimation;
    private static Animation<TextureRegion> lifeAnimation;
    private static Animation<TextureRegion> exitAnimation;
    private static Animation<TextureRegion> trapAnimation;
    private static Animation<TextureRegion> treasureAnimation;

    private static float lifeStateTime;
    private static float exitStateTime;
    private static float trapStateTime;
    private static float treasureStateTime;
    private static float keyStateTime;

    public static void loadBackground() {
        Texture map = new Texture(Gdx.files.internal("basictiles.png"));
        Texture extra = new Texture(Gdx.files.internal("mobs.png"));
        Texture things = new Texture(Gdx.files.internal("things.png"));


        int frameWidth = 16;
        int frameHeight = 15;

        // Create a TextureRegion for the first image
        WallImageRegion = new TextureRegion(map, 16, 0, frameWidth, frameHeight);
        EntryPointImageRegion = new TextureRegion(map,32, 96, frameWidth, frameHeight);
        ExitPointImageRegion = new TextureRegion(map, 0, 96, frameWidth, frameHeight);
        EnemyImageRegion = new TextureRegion(extra, 96, 64, frameWidth, frameHeight);
        TreasurePointImageRegion = new TextureRegion(things, 6*16,0 , frameWidth, frameHeight);

    }

    public static void keyImageAnimation() {
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

    public static TextureRegion renderKey() {
        keyStateTime += Gdx.graphics.getDeltaTime();
        KeyImageRegion = keyAnimation.getKeyFrame(keyStateTime,true);
        return KeyImageRegion;
    }


    public static void lifeImageAnimation() {
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

    public static void renderLives(SpriteBatch spriteBatch, float delta, float viewportWidth, float viewportHeight, int characterLives) {
        float spacing = 20;
        float lifeWidth = 40;
        float lifeHeight = 40;

        float lifeX = viewportWidth + 20 + (characterLives * spacing) / 2;
        float lifeY = viewportHeight + 100;

        lifeStateTime += Gdx.graphics.getDeltaTime(); // Update animation time

        for (int i = 0; i < characterLives; i++) {
            TextureRegion currentLifeFrame = GameMap.getLifeAnimation().getKeyFrame(lifeStateTime, true);
            spriteBatch.draw(currentLifeFrame, lifeX + i * spacing, lifeY, lifeWidth, lifeHeight);
        }
    }
    public static void renderKeys(SpriteBatch spriteBatch, float delta, float viewportWidth, float viewportHeight, boolean characterKey) {
        float keyWidth = 30;
        float keyHeight = 30;

        float keyX = viewportWidth + 20;
        float keyY = viewportHeight + 80;

        keyStateTime += Gdx.graphics.getDeltaTime(); // Update animation time

        if(characterKey) {
            TextureRegion currentKeyFrame = GameMap.getKeyAnimation().getKeyFrame(keyStateTime, true);
            spriteBatch.draw(currentKeyFrame, keyX + 65, keyY - 10, keyWidth, keyHeight);
        }
    }
    public static void exitImageAnimation() {
        Texture exit = new Texture(Gdx.files.internal("things.png"));
        int frameWidth = 16;
        int frameHeight = 16;
        int animationFrames = 4;

        Array<TextureRegion> exitFrames = new Array<>(TextureRegion.class);
        for (int row = 0; row < animationFrames; row++) {
            exitFrames.add(new TextureRegion(exit, 0,row * frameHeight, frameWidth, frameHeight));
        }
        exitAnimation = new Animation<>(0.3f, exitFrames);
    }

    public static TextureRegion renderExit() {
        exitStateTime += Gdx.graphics.getDeltaTime();
        ExitRegion = exitAnimation.getKeyFrame(exitStateTime,true);
        return ExitRegion;
    }

    public static void trapImageAnimation() {
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
    public static TextureRegion renderTrap() {
        trapStateTime += Gdx.graphics.getDeltaTime();
        TrapImageRegion = trapAnimation.getKeyFrame(trapStateTime,true);
        return TrapImageRegion;
    }

    public static void treasureImageAnimation() {
        Texture exit = new Texture(Gdx.files.internal("things.png"));
        int frameWidth = 16;
        int frameHeight = 16;
        int animationFrames = 4;

        Array<TextureRegion> treasureFrames = new Array<>(TextureRegion.class);
        for (int row = 0; row < animationFrames; row++) {
            treasureFrames.add(new TextureRegion(exit, 6*frameWidth,row * frameHeight, frameWidth, frameHeight));
        }
        treasureAnimation = new Animation<>(0.3f, treasureFrames);
    }

    public static TextureRegion renderTreasure() {
        treasureStateTime += Gdx.graphics.getDeltaTime();
        treasureRegion = treasureAnimation.getKeyFrame(treasureStateTime,true);
        return treasureRegion;
    }



    public static TextureRegion getTreasureRegion() {
        return treasureRegion;
    }

    public static TextureRegion getWallImageRegion() {
        return WallImageRegion;
    }

    public static TextureRegion getEntryPointImageRegion() {
        return EntryPointImageRegion;
    }

    public static TextureRegion getExitPointImageRegion() {
        return ExitPointImageRegion;
    }

    public static TextureRegion getTrapImageRegion() {
        return TrapImageRegion;
    }

    public static TextureRegion getTreasurePointImageRegion() {
        return TreasurePointImageRegion;
    }

    public static TextureRegion getEnemyImageRegion() {
        return EnemyImageRegion;
    }

    public static TextureRegion getKeyImageRegion() {
        return KeyImageRegion;
    }

    public static Animation<TextureRegion> getKeyAnimation() {
        return keyAnimation;
    }

    public static Animation<TextureRegion> getLifeAnimation() {
        return lifeAnimation;
    }

    public static float getLifeStateTime() {
        return lifeStateTime;
    }
    public static Animation<TextureRegion> getExitAnimation() {
        return exitAnimation;
    }

    public static TextureRegion getExitRegion() {
        return ExitRegion;
    }

    public static float getExitStateTime() {
        return exitStateTime;
    }

    public static void setExitStateTime(float exitStateTime) {
        GameMap.exitStateTime = exitStateTime;
    }

    public static Animation<TextureRegion> getTrapAnimation() {
        return trapAnimation;
    }

    public static void setTrapAnimation(Animation<TextureRegion> trapAnimation) {
        GameMap.trapAnimation = trapAnimation;
    }

    public static float getTrapStateTime() {
        return trapStateTime;
    }

    public static void setTrapStateTime(float trapStateTime) {
        GameMap.trapStateTime = trapStateTime;
    }
}