package fungorium;

import java.awt.*;
import javax.swing.JOptionPane;

/**
 * This class represents the view for the FungusFarmer player in the game.
 *
 * It provides methods to display various information and options to the player,
 * such as available actions, mycelium initialization, invalid actions, and
 * detailed player info.
 */
public class FungusFarmerView implements IObserver, IColorAble {

    private FungusFarmer fungusFarmer;
    private Color drawingColor;

    /**
     * Constructs a FungusFarmerView for the given FungusFarmer.
     *
     * @param fungusFarmer the FungusFarmer this view belongs to
     */
    public FungusFarmerView(FungusFarmer fungusFarmer) {
        this.fungusFarmer = fungusFarmer;
    }


    /**
     * Informs the player that their mycelium has been initialized on the specified
     * tecton.
     *
     * @param startingTecton the tecton where the mycelium is initialized
     */
    public void myceliumInitialized(Tecton startingTecton) {
        System.out.println("Starting mycelium for (" + fungusFarmer.getName() + ") has been initialized on tecton ("
                + startingTecton.getName() + ") !");
    }

    /**
     * Displays an invalid action message to the player.
     * This is shown when the player enters a command that is not recognized or
     * invalid.
     */
    public void invalidActionMessage() {
        System.out.println("Invalid action!");
    }

    /**
     * Displays a message to the player indicating that they don't have enough action
     * points to perform an action.
     */
    public void notEnoughActionPoints() {
        String errorMessage = "You don't have enough action points!";
        JOptionPane.showMessageDialog(null, errorMessage);
    }

    @Override
    public void draw(Graphics g, Point coords) {}

    /**
     * Sets the drawing color of the FungusFarmer.
     * @param drawingColor the specific color
     */
    public void setColor(Color drawingcolor) {
        this.drawingColor = drawingcolor;
    }

    /*
     * Returns the drawingColor of the FungusFarmer.
     */
    public Color getColor() {
        return drawingColor;
    }
}