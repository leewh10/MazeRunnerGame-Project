package de.tum.cit.ase.maze;

/**
 * Represents walls in the maze.
 * Walls are static obstacles that restrict the player's movement.
 * Characters cannot walk through the walls.
 */
public class Wall extends GameObject{
    /**
     * Constructor for Wall.
     *
     * @param x The initial x-coordinate of the Wall.
     * @param y The initial y-coordinate of the Wall.
     */
    public Wall(int x, int y) {
        super(x, y);
    }

}
