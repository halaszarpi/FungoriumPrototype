package fungorium;

import java.awt.*;

public class InsectKeeperView implements IObserver, IColorAble {
    InsectKeeper insectKeeper;
    private Color drawingColor;

    public InsectKeeperView(InsectKeeper insectKeeper) {
        this.insectKeeper = insectKeeper;
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