package fungorium;

import java.awt.*;

public class InsectKeeperView implements IObserver {
    InsectKeeper insectKeeper;
    private Color drawingColor;

    public InsectKeeperView(InsectKeeper insectKeeper) {
        this.insectKeeper = insectKeeper;
    }

    public void chooseAction() {
        System.out.println(insectKeeper.toString());
        System.out.println(
                "Enter command: \n\tMOVETOTECTON(1-3) [INSNAME] [TECNAME]\n\t CUTMYC(1) [INSNAME] [TECNAME]\n\t EATSPORE(1) [INSNAME] [SPONAME]\n\t INFO\n\tSHOWMAP\n\t SKIP");
    }

    public void invalidActionMessage() {
        System.out.println("Invalid action!");
    }

    public void info() {
        System.out.println("INSECTKEEPER INFO:");
        System.out.println("-------------------");
        System.out.println("\tNAME: " + insectKeeper.getName());
        System.out.println("\n\tACTIONPOINTS: " + insectKeeper.getActionPoints());
        System.out.println("\n\tSCORE: " + insectKeeper.getScore());
        System.out.println("\nINSECTS ");
        for (Insect insect : insectKeeper.getInsects()) {
            System.out.println("\t" + insect.getName() + " on tecton (" + insect.getTecton().getName() + ")");
            if (insect.getBoostedForRounds() != 0) {
                System.out.println("\t\tBoosted for " + insect.getBoostedForRounds() + " rounds");
            }
            if (insect.getSlowedForRounds() != 0) {
                System.out.println("\t\tSlowed for " + insect.getSlowedForRounds() + " rounds");
            }
            if (insect.getStunnedForRounds() != 0) {
                System.out.println("\t\tStunned for " + insect.getStunnedForRounds() + " rounds");
            }
            if (insect.getAntiSeveredForRounds() != 0) {
                System.out.println("\t\tAnti severed for " + insect.getAntiSeveredForRounds() + " rounds");
            }
        }
        System.out.println("-------------------");
    }

    public void notEnoughActionPoints() {
        System.out.println("Nincs elég akciópontod ehhez a művelethez.");
    }

    public void setColor(Color drawingColor) {
        this.drawingColor = drawingColor;
    }

    public Color getColor() {
        return drawingColor;
    }

    @Override
    public void draw(Graphics g, Point coords) {

    }

}