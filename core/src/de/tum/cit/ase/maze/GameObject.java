package de.tum.cit.ase.maze;

/**
 * The GameObject is responsible for the functionalities and properties shared by moving object types in the game.
 * This category includes the Guardian Angel, the Enemy and the main player Character
 * It is the common superclass for the moving object types in the game.
 * It stores the x and y coordinates, indicating the location of the game object in the two-dimensional game world.
 */
public abstract class GameObject {
    /** The x-coordinate of the GameObject */
    protected float x;
    /** The y-coordinate of the GameObject */
    protected float y;

    /**
     * Constructor for GameObject.
     *
     * @param x The initial x-coordinate of the GameObject.
     * @param y The initial y-coordinate of the GameObject.
     */
    public GameObject(float x, float y) {
        this.x = x;
        this.y = y;
    }

    /**
     * Loads the animation of the GameObject so that it can be animated on the GameScreen
     */
    public static void loadAnimation() {}

    /**
     * Allows the GameObject to change positions usually by changing the x and y coordinates
     * @param delta
     */
    public void move(float delta) {}


    //Getter and Setter
    public float getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public float getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }
}
