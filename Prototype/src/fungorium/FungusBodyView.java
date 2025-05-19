package fungorium;

import java.awt.*;

import javax.swing.JOptionPane;

/**
 * Represents the view for a FungusBody in the Fungorium game.
 *
 * This class is responsible for providing user-friendly messages related to a FungusBody's actions,
 * such as when it cannot scatter spores
 */
public class FungusBodyView implements IObserver{

    /** The FungusBody associated with this view. */
    private FungusBody fungusBody;
    private final int RADIUS = 15;

    /**
     * Constructs a new FungusBodyView for a given FungusBody.
     *
     * @param fungusBody the FungusBody this view represents
     */
    public FungusBodyView(FungusBody fungusBody) {
        this.fungusBody = fungusBody;
    }

    /**
     * Notifies the player in a pop-up windiw that there are no spores to scatter.
     * Returns an error message indicating that no spores are available for scattering.
     *
     * @return a string message about no available spores
     */
    public String noAvailableSpore() {
        String message = "Fungus body on (" + fungusBody.getMycelium().getName() + ") has no spore to scatter!";
        JOptionPane.showMessageDialog(null, message);
        return message;
    }

    /**
     * The drawing of the FungusBody. The function draws a blank circle
     * with the color of the FungusFarmer.
     */
    @Override
    public void draw(Graphics g, Point coords) {
        FungusFarmerView fmv = (FungusFarmerView)fungusBody.getOwner().getView();
        g.setColor(fmv.getColor());
        Graphics2D g2d = (Graphics2D)g;
        g2d.setStroke(new BasicStroke(3));
        g.drawOval(coords.x - RADIUS / 2, coords.y - RADIUS / 2, RADIUS, RADIUS);
    }
}