package de.tum.cit.ase.maze;

/**
 * Represents dynamic obstacles in the maze.
 * Enemies can move and cause the player to lose a life on contact.
 */
public class Enemy extends GameObject{
    /**
     * Constructor for Enemy.
     *
     * @param x The initial x-coordinate of the Enemy.
     * @param y The initial y-coordinate of the Enemy.
     */
    public Enemy(int x, int y) {
        super(x, y);
    }
}
