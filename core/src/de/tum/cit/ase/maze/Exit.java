package de.tum.cit.ase.maze;

/**
 * Represents the exit in the maze.
 * The exit point is where the player should reach to win the game.
 */
public class Exit extends GameObject {
    /**
     * Constructor for Exit.
     *
     * @param x The initial x-coordinate of the Exit.
     * @param y The initial y-coordinate of the Exit.
     */
    public Exit(int x, int y) {
        super(x, y);
    }

    /**
     * Check if the character has a key to exit the maze.
     * The exit is only opened if the character have collected a key.
     * @param character The player character
     * @return True if the character has collected a key and hence can exit, otherwise false.
     */
    public boolean canExit(Character character) {
        return character.hasKey();
    }
}
