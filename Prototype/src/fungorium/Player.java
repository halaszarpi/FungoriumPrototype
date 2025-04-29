package fungorium;

import java.util.List;
import java.util.Scanner;

/**
 * Abstract class representing a player in the fungorium game.
 * This class contains basic attributes for players such as action points, score, and methods to handle actions during a turn.
 */
public abstract class Player {
    protected String name;
    protected int actionPoints;
    protected int score;
    protected boolean inGame;

    /**
     * Constructor to initialize a player with a given name.
     * The player starts with 4 action points, 0 score, and is considered "in-game".
     *
     * @param name The name of the player.
     */
    protected Player(String name) {
        this.name = name;
        this.actionPoints = 4;
        this.score = 0;
        this.inGame = true;
    }

    /**
     * Abstract method to be implemented by subclasses. Represents the player's turn.
     * The specifics of the player's actions during a turn are determined by the subclass.
     *
     * @param map The map representing the game state.
     * @param in Scanner to read player input during their turn.
     */
    public abstract void turn(TectonMap map, Scanner in);

    /**
     * Abstract method to be implemented by subclasses. Represents the actions to be taken when a round passes.
     * This may include updating scores, checking game status, or other game logic.
     */
    public abstract void roundPassed();

    /**
     * Abstract method to be implemented by subclasses. Initializes the player with a starting tecton.
     *
     * @param startingTecton The tecton from which the player begins their game.
     * @throws Exception If there's an issue initializing the player (such as invalid tecton).
     */
    public abstract void initializePlayer(Tecton startingTecton) throws Exception;

    /**
     * Uses a given number of action points.
     *
     * @param amount The amount of action points to be used.
     */
    public void useActionPoints(int amount) {
        this.actionPoints -= amount;
    }

    /**
     * Gets the current number of action points the player has available.
     *
     * @return The current number of action points.
     */
    public int getActionPoints() {
        return actionPoints;
    }

    /**
     * Gets the player's current score.
     *
     * @return The player's score.
     */
    public int getScore() {
        return score;
    }

    /**
     * Increases the player's score by a specified amount.
     *
     * @param amount The amount to increase the score by.
     */
    public void increaseScore(int amount) {
        this.score += amount;
    }

    /**
     * Checks if the player is still in the game.
     *
     * @return True if the player is in the game, otherwise false.
     */
    public boolean isInGame() {
        return inGame;
    }

    /**
     * Gets the player's name.
     *
     * @return The player's name.
     */
    public String getName() {
        return name;
    }

    /**
     * Returns a string representation of the player's details.
     *
     * @return A string containing the player's name, action points, and score.
     */
    public String toString() {
        String returnString = "\nName: " + this.name;
        returnString += "\nActionPoints: " + this.actionPoints;
        returnString += "\nScore: " + this.score + "\n";
        return returnString;
    }
}
