package de.tum.cit.ase.maze;

/**
 * The GameObject is responsible for the functionalities and properties shared by various object types in the game.
 * It is the common superclass for the object types in the game.
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

    public static void loadAnimation() {
    }

    public void move(float delta) {
    }
}
