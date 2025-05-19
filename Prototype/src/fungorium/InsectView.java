package fungorium;

import java.awt.Graphics;
import java.awt.Point;
import java.awt.Polygon;

import javax.swing.JOptionPane;

/**
 * Represents the view for an Insect in the Fungorium game.
 *
 * This class is responsible for providing user-friendly messages related to an Insect's actions,
 * such as when it cannot step, eat or cut mycelium.
 */
public class InsectView implements IObserver {

    Insect insect;
    private final int xOffset = 5;
    private final int yOffset = 5;

    public InsectView(Insect insect) {
        this.insect = insect;
    }

    /**
     * Message that notifies the insectkeeper that its insect is stunned.
     * @return the message
     */
    public String cannotEatSporeBecauseStunnedMessage() {
        String message = "Insect (" + insect.getName() + ") cannot eat spore because it is stunned!";
        JOptionPane.showMessageDialog(null, message);
        return message;
    }

    /**
     * Message that notifies the insectkeeper that its insect is not able to step to an other tecton.
     * @return the message
     */
    public String insectNotAbleToStep(String insectName, String targetTectonName) {
        String message = "Insect " + insectName + " is not able to step to tecton (" + targetTectonName + ")!";
        JOptionPane.showMessageDialog(null, message);
        return message;
    }

    public void insectDuplicated(Insect duplicatedInsect) {
        //if (!GameTesterController.SHOW_OUTPUT) { return; }
        duplicatedInsect.getTecton().getGTecton().attach(duplicatedInsect.getView());

    }

    public Insect getInsect() {
        return insect;
    }

    /**
     * Message that notifies the insectkeeper that its insect is not able to cut a mycelium.
     * @return the message
     */
    public String cannotCutMyceliumMessage() {
        String message = "Insect (" + insect.getName() + ") cannot cut mycelium because it's anti-severed (or stunned)!";
        JOptionPane.showMessageDialog(null, message);
        return message;
    }

    /**
     * The graphical representation of an insect which is a filled triangle with the color of the InsectKeeper.
     * @param coords the relative middle point of the insect.
     */
    @Override
    public void draw(Graphics g, Point coords) {
        // a pont amit itt megkap a tekton kp.-ja
        // ez csak valami random egyelore
        InsectKeeperView ikv = (InsectKeeperView)insect.getOwner().getView();
        g.setColor(ikv.getColor());
        Polygon p = new Polygon(
            new int[] { (coords.x), (coords.x + xOffset), (coords.x - xOffset)},
            new int[] { (coords.y), (coords.y + yOffset), (coords.y + yOffset)},
            3);
        g.fillPolygon(p);
        g.drawPolygon(p);
    }
}