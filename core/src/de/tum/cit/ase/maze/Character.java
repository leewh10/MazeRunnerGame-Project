package de.tum.cit.ase.maze;

/**
 * Represents the player character in the maze.
 */
public class Character extends GameObject {
    private int lives; // Number of lives that the character has
    private boolean hasKey; // Whether the character has a key or not


    /**
     * Constructor for Character.
     *
     * @param x The initial x-coordinate of the Character.
     * @param y The initial y-coordinate of the Character.
     * @param lives the initial number of lives of the character
     */
    public Character(int x, int y, int lives) {
        super(x, y);
        this.lives = lives;
        this.hasKey = false; // The character starts the game without possessing a key.
    }

    /**
     * Getter for the attribute 'lives'.
     * Get the remaining number of lives that the character has.
     * @return The number of lives.
     */
    public int getLives() {
        return lives;
    }

    /**
     * Method to decrease the number of lives that the character has.
     * Decreases the character's remaining lives by 1.
     */
    public void loseLives() {
        lives --;
    }

    /**
     * Check if the character has a key or not.
     * @return true if the character has a key, otherwise false.
     */
    public boolean hasKey() {
        return hasKey;
    }

    /**
     * Updates the character's status when the character collects a key.
     */
    public void collectKey() {
        hasKey = true;
    }

    /**
     * Updates the character's key status when the character uses a key.
     */
    public void useKey() {
        hasKey = false;
    }
}
