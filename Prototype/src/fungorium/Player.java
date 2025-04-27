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

    protected Tecton findTectonByName(String tectonName, List<Tecton> tectonList) throws Exception{

        for (Tecton tecton : tectonList) {

            if (tecton.getName().equals(tectonName)) return tecton;

        }

        throw new Exception("Tecton not found");
    }

    protected Mycelium findMyceliumByName(String myceliumName, List<Tecton> tectonList) throws Exception{

        for (Tecton tecton : tectonList) {

            for (Mycelium mycelium : tecton.getMyceliumList()) {

                if (mycelium.getName().equals(myceliumName)) return mycelium;

            }
        }

        throw new Exception("Mycelium not found");
    }

    protected Spore findSporeByName(String sporeName, List<Tecton> tectonList) throws Exception{

        for (Tecton tecton : tectonList) {

            for (Spore spore : tecton.getSporeList()) {

                if (spore.getName().equals(sporeName)) return spore;

            }
        }

        throw new Exception("Spore not found");
    }

    protected Insect findInsectByName(String insectName, List<Tecton> tectonList) throws Exception{

        for (Tecton tecton : tectonList) {

            for (Insect insect : tecton.getInsectList()) {

                if (insect.getName().equals(insectName)) return insect;

            }
        }

        throw new Exception("Insect not found");
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
