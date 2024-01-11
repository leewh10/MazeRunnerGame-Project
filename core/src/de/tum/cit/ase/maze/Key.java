package de.tum.cit.ase.maze;
/**
 * Represents Keys in the maze.
 * Character should have keys in order to open the exit. Without keys, the character cannot exit the maze.
 * If the character tries to open the exit without the key, the wall should work as a wall.
 */
public class Key extends GameObject{
    /**
     * Constructor for Key.
     *
     * @param x The initial x-coordinate of the Key.
     * @param y The initial y-coordinate of the Key.
     */
    public Key(float x, float y) {
        super(x, y);
    }
}
