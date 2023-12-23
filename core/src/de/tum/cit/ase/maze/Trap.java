package de.tum.cit.ase.maze;

/**
 * Represents obstacles with a fixed position within the maze
 * Traps are static obstacles that can cause the character to lose a life when contact occurs.
 */
public class Trap extends GameObject{
    private boolean isReady;

    /**
     * Constructor for Trap.
     *
     * @param x The initial x-coordinate of the Trap.
     * @param y The initial y-coordinate of the Trap.
     */
    public Trap(int x, int y) {
        super(x, y);
        this.isReady = true; // Traps are ready by default
    }

}
