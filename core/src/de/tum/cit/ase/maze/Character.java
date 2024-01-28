 package de.tum.cit.ase.maze;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Array;


 public class Character extends GameObject {

     private boolean hasKey;
     private static boolean isCollidedTree;
     private int lives;
     private static int enemiesKilled;
     private final Animation<TextureRegion> animation;
     private static float stateTime;
     private static boolean isImmuneToFire;
     private boolean isTextVisible;
     private boolean shouldMove;
     private boolean isLeverPulled;
     private boolean isKeyPressed;
     private boolean isTreasureOpened;


     // Character animation downwards
     private static Animation<TextureRegion> characterDownAnimation;
     private static Animation<TextureRegion> characterUpAnimation;
     private static Animation<TextureRegion> characterLeftAnimation;
     private static Animation<TextureRegion> characterRightAnimation;
     private static Animation<TextureRegion> characterFightRightAnimation;
     private static Animation<TextureRegion> characterFightLeftAnimation;
     private static Animation<TextureRegion> characterFightUpAnimation;
     private static Animation<TextureRegion> characterFightDownAnimation;

     private TextureRegion characterRegion;


     private final OrthographicCamera camera;
     private final MazeRunnerGame game;
     private final BitmapFont font;
     private final GameScreen gameScreen;

     //Collision cool down
     private long lastCollisionTime;
     private static final long COLLISION_COOLDOWN = 3000; // for 3 seconds, the character doesn't lose its lives
     private static final long COLLISION_COOLDOWN1 = 1000;



     private static Animation<TextureRegion> currentAnimation;
     private static TextureRegion collisionImageRegion;
     private static TextureRegion angelMeetingImageRegion;
     private static TextureRegion heartImageRegion;
     private static TextureRegion keyImageRegion;


     /**
      * Constructor for Character
      * @param gameScreen
      * @param game
      * @param x
      * @param y
      * @param hasKey
      * @param isLeverPulled
      * @param isTreasureOpened
      * @param lives
      * @param animation
      */
     public Character(GameScreen gameScreen, MazeRunnerGame game, float x, float y, boolean hasKey, boolean isLeverPulled, boolean isTreasureOpened, int lives,  Animation<TextureRegion> animation) {
         super(x, y);
         this.game = game; // Store the game instance
         this.hasKey = hasKey;
         this.lives = lives;
         this.animation = animation;
         stateTime = 0f;
         this.gameScreen = gameScreen;
         this.isLeverPulled = isLeverPulled;
         this.isTreasureOpened=isTreasureOpened;
         isImmuneToFire = false;
         isCollidedTree = false;

         camera = new OrthographicCamera();
         camera.setToOrtho(true);
         font = game.getSkin().getFont("font");

         currentAnimation = getCharacterDownAnimation();
     }


     /**
      * Loads the character animation from the character.png file.
      */
     public static void loadAnimation() {

         Texture walkSheet = new Texture(Gdx.files.internal("character.png"));

         int frameWidth = 16;
         int frameHeight = 32;
         int animationFrames = 4;

         // libGDX internal Array instead of ArrayList because of performance
         Array<TextureRegion> walkFramesUp = new Array<>(TextureRegion.class);
         Array<TextureRegion> walkFramesDown = new Array<>(TextureRegion.class);
         Array<TextureRegion> walkFramesLeft = new Array<>(TextureRegion.class);
         Array<TextureRegion> walkFramesRight = new Array<>(TextureRegion.class);

         Array<TextureRegion> walkFramesFightRight = new Array<>(TextureRegion.class);
         Array<TextureRegion> walkFramesFightLeft = new Array<>(TextureRegion.class);
         Array<TextureRegion> walkFramesFightUp = new Array<>(TextureRegion.class);
         Array<TextureRegion> walkFramesFightDown = new Array<>(TextureRegion.class);

         // Add frames to the respective animations
         for (int col = 0; col < animationFrames; col++) {
             walkFramesUp.add(
                     new TextureRegion(walkSheet, col * frameWidth, 2 * frameHeight, frameWidth, frameHeight));
             walkFramesDown.add(
                     new TextureRegion(walkSheet, col * frameWidth, 0, frameWidth, frameHeight));
             walkFramesLeft.add(
                     new TextureRegion(walkSheet, col * frameWidth, 3 * frameHeight, frameWidth, frameHeight));
             walkFramesRight.add(
                     new TextureRegion(walkSheet, col * frameWidth, frameHeight, frameWidth, frameHeight));


             walkFramesFightRight.add(
                    new TextureRegion(walkSheet, 8, 6* frameHeight, frameWidth, frameHeight));
             walkFramesFightRight.add(
                     new TextureRegion(walkSheet, 40, 6* frameHeight, frameWidth, frameHeight));
             walkFramesFightRight.add(
                     new TextureRegion(walkSheet, 72, 6* frameHeight, frameWidth, frameHeight));
             walkFramesFightRight.add(
                     new TextureRegion(walkSheet, 104, 6* frameHeight, frameWidth, frameHeight));


             walkFramesFightLeft.add(
                     new TextureRegion(walkSheet, 8, 7* frameHeight, frameWidth, frameHeight));
             walkFramesFightLeft.add(
                     new TextureRegion(walkSheet, 40, 7* frameHeight, frameWidth, frameHeight));
             walkFramesFightLeft.add(
                     new TextureRegion(walkSheet, 72, 7* frameHeight, frameWidth, frameHeight));
             walkFramesFightLeft.add(
                     new TextureRegion(walkSheet, 104, 7* frameHeight, frameWidth, frameHeight));


             walkFramesFightUp.add(
                     new TextureRegion(walkSheet, 8, 5* frameHeight, frameWidth, frameHeight));
             walkFramesFightUp.add(
                     new TextureRegion(walkSheet, 40, 5* frameHeight, frameWidth, frameHeight));
             walkFramesFightUp.add(
                     new TextureRegion(walkSheet, 72, 5* frameHeight, frameWidth, frameHeight));
             walkFramesFightUp.add(
                     new TextureRegion(walkSheet, 104, 5* frameHeight, frameWidth, frameHeight));

             walkFramesFightDown.add(
                     new TextureRegion(walkSheet, 40, 4* frameHeight, frameWidth, frameHeight));
             walkFramesFightDown.add(
                     new TextureRegion(walkSheet, 40, 4* frameHeight, frameWidth, frameHeight));
             walkFramesFightDown.add(
                     new TextureRegion(walkSheet, 72, 4* frameHeight, frameWidth, frameHeight));
             walkFramesFightDown.add(
                     new TextureRegion(walkSheet, 104, 4* frameHeight, frameWidth, frameHeight));
     }



         characterFightRightAnimation = new Animation<>(0.1f, walkFramesFightRight);
         characterFightLeftAnimation = new Animation<>(0.1f, walkFramesFightLeft);
         characterFightUpAnimation = new Animation<>(0.1f, walkFramesFightUp);
         characterFightDownAnimation = new Animation<>(0.1f, walkFramesFightDown);


         characterUpAnimation = new Animation<>(0.1f, walkFramesUp);
         characterDownAnimation = new Animation<>(0.1f, walkFramesDown);
         characterLeftAnimation = new Animation<>(0.1f, walkFramesLeft);
         characterRightAnimation = new Animation<>(0.1f, walkFramesRight);
     }



     public void move() {
         stateTime += Gdx.graphics.getDeltaTime();

         /**
          * Boolean for the text showing
          */
         if (isTextVisible) {
             font.draw(game.getSpriteBatch(), "Press ESC to Pause", gameScreen.getTextX(), gameScreen.getTextY());
         }

         //All key controls to move the character
         /**
          * Before the game starts (before any key is pressed), the character is moving in circles
          */
         if (Gdx.input.isKeyPressed(Input.Keys.UP)
                 || Gdx.input.isKeyPressed(Input.Keys.W)
                 || Gdx.input.isKeyPressed(Input.Keys.DOWN)
                 || Gdx.input.isKeyPressed(Input.Keys.S)
                 || Gdx.input.isKeyPressed(Input.Keys.LEFT)
                 || Gdx.input.isKeyPressed(Input.Keys.A)
                 || Gdx.input.isKeyPressed(Input.Keys.RIGHT)
                 || Gdx.input.isKeyPressed(Input.Keys.D)) {

             gameScreen.setTextX(camera.position.x);
             gameScreen.setTextY(camera.position.y);
             isTextVisible = false;
         } else {
             isTextVisible = true;
         }

         /**
          * If any of the arrow keys are pressed, the character moves in the specified direction by 5 units
          * If the SPACE bar is pressed at the same time, it can kill enemies
          * The character will only change location by 5 units if it will not collide with a wall (view collidesWithWall() method)
          * While the key is pressed, the character will animate in the direction indicated
          * Once the key is let go of, the Character stops animating and remains stationary facing the direction last specified
          */
         if (Gdx.input.isKeyPressed(Input.Keys.UP) || Gdx.input.isKeyPressed(Input.Keys.W)) {
             if(Gdx.input.isKeyPressed(Input.Keys.SPACE)) {
                 characterRegion = getCharacterFightUpAnimation().getKeyFrame(gameScreen.getSinusInput(), true);
                 currentAnimation = getCharacterFightUpAnimation();
             }
             else{
                 currentAnimation = getCharacterUpAnimation();
                 characterRegion = getCharacterUpAnimation().getKeyFrame(gameScreen.getSinusInput(), true);
             }

             if (!collidesWithWall(getX(), getY() + 5, GameScreen.getMazeArray())) {
                 setY((int) (getY() + 5));
             }
             shouldMove = true;
             isKeyPressed = true;


         } else if (Gdx.input.isKeyPressed(Input.Keys.DOWN) || Gdx.input.isKeyPressed(Input.Keys.S)) {
             if(Gdx.input.isKeyPressed(Input.Keys.SPACE)) {
                 characterRegion = getCharacterFightDownAnimation().getKeyFrame(gameScreen.getSinusInput(), true);
                 currentAnimation = getCharacterFightDownAnimation();
             }
             else{
                 characterRegion = getCharacterDownAnimation().getKeyFrame(gameScreen.getSinusInput(), true);
                 currentAnimation = getCharacterDownAnimation();
             }
             if (!collidesWithWall(getX(), getY() - 5, GameScreen.getMazeArray())) {
                 setY((int) (getY() - 5));
             }

             shouldMove = true;
             isKeyPressed = true;


         } else if (Gdx.input.isKeyPressed(Input.Keys.LEFT) || Gdx.input.isKeyPressed(Input.Keys.A)) {
             if(Gdx.input.isKeyPressed(Input.Keys.SPACE)) {
                 characterRegion = getCharacterFightLeftAnimation().getKeyFrame(gameScreen.getSinusInput(), true);
                 currentAnimation = getCharacterFightLeftAnimation();
             }
             else{
                 characterRegion = getCharacterLeftAnimation().getKeyFrame(gameScreen.getSinusInput(), true);
                 currentAnimation = getCharacterLeftAnimation();
             }
             if (!collidesWithWall(getX() - 5, getY(), GameScreen.getMazeArray())) {
                 setX((int) (getX() - 5));
             }


             shouldMove = true;
             isKeyPressed = true;
         } else if (Gdx.input.isKeyPressed(Input.Keys.RIGHT) || Gdx.input.isKeyPressed(Input.Keys.D)) {
             if(Gdx.input.isKeyPressed(Input.Keys.SPACE)) {
                 characterRegion = getCharacterFightRightAnimation().getKeyFrame(gameScreen.getSinusInput(), true);
                 currentAnimation = getCharacterFightRightAnimation();
             }
             else{
                 characterRegion = getCharacterRightAnimation().getKeyFrame(gameScreen.getSinusInput(), true);
                 currentAnimation = getCharacterRightAnimation();
             }

             if (!collidesWithWall(getX() + 5, getY(), GameScreen.getMazeArray())) {
                 setX((int) (getX() + 5));
             }

             shouldMove = true;
             isKeyPressed = true;

         } else {
             isKeyPressed = false;
         }

     }

     /**
      *
      * @param newX the x coordinate that is being analysed to see if it will result in a collision
      * @param newY the y coordinate that is being analysed to see if it will result in a collision
      * @param mazeArray the array that contains the coordinates of all map and game objects
      * @return whether the character will collide with a wall
      */
     public boolean collidesWithWall(float newX, float newY, int[][] mazeArray) {
         // Allowing the character to go through the wall a little bit to avoid getting stuck
         float collisionMarginTopRight = 0.8f * 50;
         float collisionMarginBottomLeft = 0.3f * 50;

         // Adjust the newX and newY with the collisionMarginTopRight
         float characterX = newX + collisionMarginBottomLeft;
         float characterY = newY + collisionMarginBottomLeft;
         float adjustedX = newX + collisionMarginTopRight;
         float adjustedY = newY + collisionMarginTopRight;

         // Calculate the cell coordinates for adjusted position
         int cellXForBottomLeft = (int) (characterX / 50);
         int cellYForBottomLeft = (int) (characterY / 50);
         int cellXForTopRight = (int) (adjustedX / 50);
         int cellYForTopRight = (int) (adjustedY / 50);


         if (cellXForTopRight < 0 || cellXForTopRight >= mazeArray.length || cellYForTopRight < 0 || cellYForTopRight >= mazeArray[0].length) {
             return true;
         }
         if (cellXForBottomLeft < 0 || cellXForBottomLeft >= mazeArray.length || cellYForBottomLeft < 0 || cellYForBottomLeft >= mazeArray[0].length) {
             return true;
         }


         /**
          * All the mazeArray values through which a character should not go through
          * 0 = a regular wall
          * 8 = the shadow of a wall
          * 10 = a moveable wall
          * 2 = exit before a key is acquired
          * 1 = entry after the character starts the game
          */
         boolean collisionTop = mazeArray[cellXForTopRight][cellYForTopRight] == 0; //
         boolean collisionBottom = mazeArray[cellXForBottomLeft][cellYForBottomLeft] == 0;
         boolean collisionLeft = mazeArray[cellXForBottomLeft][cellYForBottomLeft] == 0;
         boolean collisionRight = mazeArray[cellXForTopRight][cellYForTopRight] == 0;

         boolean collisionTopShadow = mazeArray[cellXForTopRight][cellYForTopRight] == 8; //
         boolean collisionBottomShadow = mazeArray[cellXForBottomLeft][cellYForBottomLeft] == 8;
         boolean collisionLeftShadow = mazeArray[cellXForBottomLeft][cellYForBottomLeft] == 8;
         boolean collisionRightShadow = mazeArray[cellXForTopRight][cellYForTopRight] == 8;

         boolean collisionTopMove = mazeArray[cellXForTopRight][cellYForTopRight] == 10; //
         boolean collisionBottomMove = mazeArray[cellXForBottomLeft][cellYForBottomLeft] == 10;
         boolean collisionLeftMove = mazeArray[cellXForBottomLeft][cellYForBottomLeft] == 10;
         boolean collisionRightMove = mazeArray[cellXForTopRight][cellYForTopRight] == 10;

         boolean collisionTopExit = mazeArray[cellXForTopRight][cellYForTopRight] == 2; //
         boolean collisionBottomExit = mazeArray[cellXForBottomLeft][cellYForBottomLeft] == 2;
         boolean collisionLeftExit = mazeArray[cellXForBottomLeft][cellYForBottomLeft] == 2;
         boolean collisionRightExit = mazeArray[cellXForTopRight][cellYForTopRight] == 2;

         boolean collisionTopEntry = mazeArray[cellXForTopRight][cellYForTopRight] == 1; //
         boolean collisionBottomEntry = mazeArray[cellXForBottomLeft][cellYForBottomLeft] == 1;
         boolean collisionLeftEntry = mazeArray[cellXForBottomLeft][cellYForBottomLeft] == 1;
         boolean collisionRightEntry = mazeArray[cellXForTopRight][cellYForTopRight] == 1;

         return collisionTop || collisionBottom || collisionLeft || collisionRight
                 || collisionBottomShadow || collisionLeftShadow || collisionRightShadow|| collisionTopShadow
                 || collisionBottomMove || collisionLeftMove || collisionRightMove|| collisionTopMove
                 || collisionBottomExit || collisionLeftExit || collisionRightExit|| collisionTopExit
                 || collisionBottomEntry || collisionLeftEntry || collisionRightEntry|| collisionTopEntry;
     }

     /**
      * The collidesWith methods allow for the character to collide with various maze objects and have something happen as a result of this collision
      * These methods are primarily used in the GameScreen class during the switch-case statements
      * @param enemyX1 the x coordinate of the game object in question (in this example this is the enemy)
      * @param enemyY1 the y coordinate of the game object in question (in this example this is the enemy)
      * @return whether the character has collided with the game object
      */
     public boolean collidesWithEnemy(float enemyX1, float enemyY1) {
         //retrieves the current time in milliseconds.
         long currentTime = System.currentTimeMillis();

         // Check if enough time has passed since the last collision
         if (currentTime - lastCollisionTime >= COLLISION_COOLDOWN) {
             float characterX = getX();
             float characterY = getY();
             float characterWidth = 36;
             float characterHeight = 62;

             float keyWidth = 50;
             float keyHeight = 50;

             // Check for collision between character and enemy
             if (characterX < enemyX1 + keyWidth &&
                     characterX + characterWidth > enemyX1 &&
                     characterY < enemyY1 + keyHeight &&
                     characterY + characterHeight > enemyY1) {

                 if(!Gdx.input.isKeyPressed(Input.Keys.SPACE)) {
                     // Set the last collision time for collision cool-down time
                     lastCollisionTime = currentTime;
                     //visual to help the character know if their cool-down time is up
                     loadCollisionImage();
                 }
                 return true;
             }
         }
         return false;
     }

     public boolean collidesWithKey(float keyX, float keyY) {
         float characterX = getX();
         float characterY = getY();
         float characterWidth = 36;
         float characterHeight = 62;

         float keyWidth = 50;
         float keyHeight = 50;

         return characterX < keyX + keyWidth &&
                 characterX + characterWidth > keyX &&
                 characterY < keyY + keyHeight &&
                 characterY + characterHeight > keyY;
     }

     public boolean collidesWithAngel(float angelX, float angelY) {
         float characterX = getX();
         float characterY = getY();
         float characterWidth = 36;
         float characterHeight = 62;

         float angelWidth = 50;
         float angelHeight = 50;


         if (characterX < angelX + angelWidth + 20 &&
                  characterX + characterWidth + 20 > angelX &&
                  characterY < angelY + angelHeight + 20 &&
                  characterY + characterHeight + 20 > angelY) {
             loadAngelMeetingImage();
             return true;
         }
         return false;
     }

     public boolean collidesWithLever(float leverX, float leverY) {
         float characterX = getX();
         float characterY = getY();

         return characterX < leverX + 50 && characterX + 36 > leverX  &&
                 characterY <leverY + 50 && characterY + 31 > leverY;
     }

     public boolean collidesWithTree(float treeX, float treeY) {
         float characterX = getX();
         float characterY = getY();

         return characterX < treeX + 50 && characterX + 36 > treeX  &&
                 characterY < treeY + 50 && characterY + 31 > treeY;
     }

     public boolean collidesWithTreasure(float treasureX, float treasureY) {
         float characterX = getX();
         float characterY = getY();
         float characterWidth = 36;
         float characterHeight = 62;

         float treasureWidth = 50;
         float treasureHeight = 50;

         if(characterX < treasureX + treasureWidth &&
                 characterX + characterWidth > treasureX &&
                 characterY < treasureY + treasureHeight &&
                 characterY + characterHeight > treasureY) {
             return true;
         }
         return false;
     }
     public boolean collidesWithTrap(float trapX, float trapY) {
         //retrieves the current time in milliseconds.
         long currentTime = System.currentTimeMillis();

         // Check if enough time has passed since the last collision
         if (currentTime - lastCollisionTime >= COLLISION_COOLDOWN) {
             float characterX = getX();
             float characterY = getY();
             float characterWidth = 36;
             float characterHeight = 62;

             float trapWidth = 50;
             float trapHeight = 50;

             // Check for collision between character and enemy
             if (characterX < trapX + trapWidth &&
                     characterX + characterWidth > trapX &&
                     characterY + 25< trapY + trapHeight &&
                     characterY + characterHeight > trapY) {

                 if(!isIsImmuneToFire()) {
                     // Set the last collision time for collision cool-down time
                     lastCollisionTime = currentTime;
                     //visual to help the character know if their cool-down time is up
                     loadCollisionImage();
                 }
                 return true;
             }
         }
         return false;
     }

     /**
      * Images that are loaded to inform the player that they have or will collide with a GameObject
      */
     private void loadCollisionImage() {
         Texture collisionMark = new Texture(Gdx.files.internal("objects.png"));

         int frameWidth = 16;
         int frameHeight = 16;

         collisionImageRegion = new TextureRegion(collisionMark,2 * frameWidth,8 * frameHeight,frameWidth,frameHeight);
         // Set the time when the cooldown image is set
         lastCollisionTime = System.currentTimeMillis();
     }

     private void loadAngelMeetingImage() {
         Texture angelMeetingMark = new Texture(Gdx.files.internal("objects.png"));

         int frameWidth = 16;
         int frameHeight = 16;

         angelMeetingImageRegion = new TextureRegion(angelMeetingMark,3 * frameWidth,8 * frameHeight,frameWidth,frameHeight);
         lastCollisionTime = System.currentTimeMillis();
     }


     /**
      * Images that are rendered to inform the player that they have collided with an Enemy of Trap
      * They appear temporarily then disappear after some time (specifically after the cool-down time is up)
      * @param batch
      * @param viewportWidth
      * @param viewportHeight
      */
     public void renderCollision(SpriteBatch batch, float viewportWidth, float viewportHeight) {
         float collisionMarkWidth = 40;
         float collisionMarkHeight = 40;

         float collisionImageX = viewportWidth + 20;
         float collisionImageY = viewportHeight + 100;

         if (collisionImageRegion != null) {
             batch.draw(collisionImageRegion,collisionImageX,collisionImageY,collisionMarkWidth,collisionMarkHeight);
         }

         if (System.currentTimeMillis() - lastCollisionTime >= COLLISION_COOLDOWN) {
             // Reset the cooldown image
             collisionImageRegion = null;
         }
     }

     /**
      * Informs the character that they are near a GuardianAngel
      * @param batch
      * @param viewportWidth
      * @param viewportHeight
      */
     public void renderAngelMeeting(SpriteBatch batch, float viewportWidth, float viewportHeight) {
        float angelMeetingMarkWidth = 40;
        float angelMeetingMarkHeight = 40;

        float angelMeetingImageX = viewportWidth + 20;
        float angelMeetingImageY = viewportHeight + 100;

        if (angelMeetingImageRegion != null) {
            batch.draw(angelMeetingImageRegion,angelMeetingImageX,angelMeetingImageY,angelMeetingMarkWidth,angelMeetingMarkHeight);
        }

         if (System.currentTimeMillis() - lastCollisionTime >= COLLISION_COOLDOWN1) {
             // Reset the cooldown image
             angelMeetingImageRegion = null;
         }
     }
     public boolean seesTheAngel(float angelX, float angelY) {
         long currentTime = System.currentTimeMillis();
         if (currentTime - lastCollisionTime >= COLLISION_COOLDOWN1) {

             float characterX = getX();
             float characterY = getY();
             float characterWidth = 36;
             float characterHeight = 62;

             float angelWidth = 50;
             float angelHeight = 50;


             if (characterX < angelX + angelWidth + 100 &&
                     characterX + characterWidth + 100 > angelX &&
                     characterY < angelY + angelHeight + 100 &&
                     characterY + characterHeight + 100 > angelY) {
                 loadAngelMeetingImage();
                 return true;
             }
         }
         return false;
     }

     /**
      * Getters and Setters
      * @return
      */
     public static boolean isIsImmuneToFire() {
         return isImmuneToFire;
     }

     public static void setIsImmuneToFire(boolean isImmuneToFire) {
         Character.isImmuneToFire = isImmuneToFire;
     }

     public static Animation<TextureRegion> getCharacterDownAnimation() {
        return characterDownAnimation;
    }

    public static Animation<TextureRegion> getCharacterUpAnimation() {
        return characterUpAnimation;
    }

     public static boolean isCollidedTree() {
         return isCollidedTree;
     }

     public static void setCollidedTree(boolean collidedTree) {
         isCollidedTree = collidedTree;
     }

     public static Animation<TextureRegion> getCharacterLeftAnimation() {
        return characterLeftAnimation;
    }

    public static Animation<TextureRegion> getCharacterRightAnimation() {
        return characterRightAnimation;
    }

     public static Animation<TextureRegion> getCharacterFightRightAnimation() {
         return characterFightRightAnimation;
     }

     public static Animation<TextureRegion> getCharacterFightLeftAnimation() {
         return characterFightLeftAnimation;
     }

     public static Animation<TextureRegion> getCharacterFightUpAnimation() {
         return characterFightUpAnimation;
     }

     public static Animation<TextureRegion> getCharacterFightDownAnimation() {
         return characterFightDownAnimation;
     }

     public static int getEnemiesKilled() {
         return enemiesKilled;
     }

     public static void setEnemiesKilled(int enemiesKilled) {
         Character.enemiesKilled = enemiesKilled;
     }

     public boolean isTreasureOpened() {
         return isTreasureOpened;
     }

     public void setTreasureOpened(boolean treasureOpened) {
         isTreasureOpened = treasureOpened;
     }

     public TextureRegion getCharacterRegion() {
        return characterRegion;
    }

     public static Animation<TextureRegion> getCurrentAnimation() {
         return currentAnimation;
     }

     public int getLives() {
         return lives;
     }

     public boolean isHasKey() {
         return hasKey;
     }

     public boolean isShouldMove() {
        return shouldMove;
    }

     public boolean isKeyPressed() {
         return isKeyPressed;
     }

     public boolean isLeverPulled() {
         return isLeverPulled;
     }

     public void setLeverPulled(boolean leverPulled) {
         isLeverPulled = leverPulled;
     }

     public void setTextVisible(boolean textVisible) {
        isTextVisible = textVisible;
    }

    public void setCharacterRegion(TextureRegion characterRegion) {
        this.characterRegion = characterRegion;
    }

     public static void setCurrentAnimation(Animation<TextureRegion> currentAnimation) {
         Character.currentAnimation = currentAnimation;
     }

     public void setHasKey(boolean hasKey) {
         this.hasKey = hasKey;
     }

     public void setLives(int lives) {
         this.lives = lives;
     }

     /**
      * Renders the character
      * @param batch
      */
     public void render(SpriteBatch batch) {
         TextureRegion currentFrame = animation.getKeyFrame(stateTime, true);
         float width = 4*currentFrame.getRegionWidth();
         float height = 4* currentFrame.getRegionHeight();

         batch.draw(currentFrame, x,y,width,height);
     }
}

