package fungorium;

import java.awt.*;

public class InsectKeeperView implements IObserver, IColorAble {
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
    public void draw(Graphics g, Point coords) {}

}