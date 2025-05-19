package fungorium;

import java.awt.*;
import javax.swing.JOptionPane;

public class InsectKeeperView implements IObserver, IColorAble {
    InsectKeeper insectKeeper;
    private Color drawingColor;

    public InsectKeeperView(InsectKeeper insectKeeper) {
        this.insectKeeper = insectKeeper;
    }

    /**
     * Displays a message to the player indicating that they don't have enough action
     * points to perform an action.
     */
    public void notEnoughActionPoints() {
        String errorMessage = "You don't have enough action points!";
        JOptionPane.showMessageDialog(null, errorMessage);
    }

    /**
     * Setting the color of the InsectKeeper
     * @param drawingColor the color of the 'Keeper
     */
    public void setColor(Color drawingColor) {
        this.drawingColor = drawingColor;
    }

    /**
     * Returns the drawing color of the InsectKeeper
     */
    public Color getColor() {
        return drawingColor;
    }

    @Override
    public void draw(Graphics g, Point coords) {}

}