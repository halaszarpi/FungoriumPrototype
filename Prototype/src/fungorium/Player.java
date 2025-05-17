package fungorium;

import java.util.Collection;
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

    public abstract void roundPassed();

    public abstract void initializePlayer(Tecton startingTecton) throws Exception;

    public void useActionPoints(int amount) {
        this.actionPoints -= amount;
    }

    public int getActionPoints() {
        return actionPoints;
    }

    public int getScore() {
        return score;
    }

    public void increaseScore(int amount) {
        this.score += amount;
    }

    public boolean isInGame() {
        return inGame;
    }

    public String getName() {
        return name;
    }

    public abstract IObserver getView();

    public abstract List<String> getActions();

    public abstract List<String> getParam1ForAction();

    public abstract List<String> getParam2ForAction(String action, String param1, GMap map);

    public abstract void doAction(GMap map, String commandToRun);
}
