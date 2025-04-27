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

    protected Tecton findTectonByName(String tectonName, List<Tecton> tectonList) {

        for (Tecton tecton : tectonList) {

            if (tecton.getName().equals(tectonName)) return tecton;

        }

        return null;
    }

    protected Mycelium findMyceliumByName(String myceliumName, List<Tecton> tectonList) {

        for (Tecton tecton : tectonList) {

            for (Mycelium mycelium : tecton.getMyceliumList()) {

                if (mycelium.getName().equals(myceliumName)) return mycelium;

            }
        }

        return null;
    }

    protected Spore findSporeByName(String sporeName, List<Tecton> tectonList) {

        for (Tecton tecton : tectonList) {

            for (Spore spore : tecton.getSporeList()) {

                if (spore.getName().equals(sporeName)) return spore;

            }
        }

        return null;
    }

    protected Insect findInsectByName(String insectName, List<Tecton> tectonList) {

        for (Tecton tecton : tectonList) {

            for (Insect insect : tecton.getInsectList()) {

                if (insect.getName().equals(insectName)) return insect;

            }
        }

        return null;
    }

    public abstract void turn(List<Tecton> map, Scanner in);
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
