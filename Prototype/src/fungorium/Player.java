package fungorium;

import java.util.List;
import java.util.Scanner;

public abstract class Player {
    protected String name;
    protected int actionPoints;
    protected int score;
    protected boolean inGame;

    protected Player(String name) {
        this.name = name;
        this.actionPoints = 4;
        this.score = 0;
        this.inGame = true;
    }

    public abstract void turn(TectonMap map, Scanner in);

    public abstract void roundPassed();

    public abstract void initializePlayer(Tecton startingTecton);

    public void useActionPoints(int amount) {
        this.actionPoints -= amount;
    }

    public int getActionPoints() {
        return actionPoints;
    }

    public int getScore() {
        return score;
    }

    public boolean isInGame() {
        return inGame;
    }

    public String getName() {
        return name;
    }

    public String toString() {
        String returnString = "\nName: " + this.name;
        returnString += "\nActionPoints: " + this.actionPoints;
        returnString += "\nScore: " + this.score + "\n";
        return returnString;
    }
}
