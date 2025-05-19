package fungorium;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.swing.JOptionPane;

/**
 * The TectonView class is responsible for providing messages and status updates
 * related to the state and interactions of a Tecton. It communicates important
 * events such as tecton creation, breaking, mycelium additions/removals,
 * neighbour connections, and various error or status messages related to the
 * tecton and its interactions with other game elements.
 */
public class TectonView implements IObserver {

    Tecton t;
    private final int tectonWidth = 150;
    private final int tectonHeight = 150;

    /**
     * Constructor to initialize the TectonView with a specific Tecton.
     *
     * @param tecton The Tecton that this view will represent.
     */
    public TectonView(Tecton tecton) {
        this.t = tecton;
    }

    /**
     * Prints a message indicating that mycelium has been added to the Tecton.
     *
     * @param m The Mycelium added to the Tecton.
     */
    public void myceliumAdded(Mycelium m) {
        t.getGTecton().attach(m.getView());
    }

    /**
     * Prints a message indicating that a spore has been added to the Tecton.
     *
     * @param s The Spore added to the Tecton.
     */
    public void sporeAdded(Spore s) {
        t.getGTecton().attach(s.getView());
    }

    /**
     * Prints a message indicating that an insect has been added to the Tecton.
     *
     * @param i The Insect added to the Tecton.
     */
    public void insectAdded(Insect i) {
        t.getGTecton().attach(i.getView());
    }

    /**
     * Prints a message indicating that mycelium has been removed from the Tecton.
     *
     * @param m The Mycelium removed from the Tecton.
     */
    public void myceliumRemoved(Mycelium m) {
        t.getGTecton().detach(m.getView());
    }

    /**
     * Prints a message indicating that an insect has been removed from the Tecton.
     *
     * @param i The Insect removed from the Tecton.
     */
    public void insectRemoved(Insect i) {
       t.getGTecton().detach(i.getView());
    }

    /**
     * Prints a message indicating that a spore has been removed from the Tecton.
     *
     * @param s The Spore removed from the Tecton.
     */
    public void sporeRemoved(Spore s) {
        t.getGTecton().detach(s.getView());
    }

    /**
     * Error message indicating the lack of neighbour connection with the parameter tecton.
     * @param otherTecton
     * @return message
     */
    public String notNeighbour(Tecton otherTecton) {
        String message = "Tecton (" + t.getName() + ") has no neighbour tecton (" + otherTecton.getName() + ")!";
        JOptionPane.showMessageDialog(null, message);
        return message;
    }

    /**
     * Error message indicating the lack of mycelium connection with the parameter tecton.
     * @param otherTecton
     * @return message
     */
    public String notConnectedByMycelium(Tecton otherTecton) {
        String message = "Tecton (" + t.getName() + ") is not connected with tecton (" + otherTecton.getName() + ")!";
        JOptionPane.showMessageDialog(null, message);
        return message;
    }

    /**
     * Error message indicating the that the SingleMyceliumTecton already has a mycelium.
     * @param otherTecton
     * @return message
     */
    public String singleMyceliumTectonAlreadyHasMycelium() {
        String message = "Tecton (" + t.getName() + ") already has a mycelium!";
        JOptionPane.showMessageDialog(null, message);
        return message;
    }

    /**
     * The graphical representation of the tecton itself, it consists if a circle filled black with tecton's name on it and mycelium connections
     * around it with the right color of the mycelium's FungusFarmer
     */
    @Override
    public void draw(Graphics g, Point coords) {
        Graphics2D g2d = (Graphics2D) g;
        g2d.setColor(Color.BLACK);
        g2d.fillOval(coords.x - tectonWidth / 2, coords.y - tectonHeight / 2, tectonWidth, tectonHeight);
        g2d.setColor(Color.WHITE);
        g2d.setFont(new Font("Arial", Font.BOLD, 14));
        // kicsit eltolva mindig
        g2d.drawString(t.getName(), coords.x - 5, coords.y + 5);
    }

    /**
     * Function that supports mycelium connection drawing during tecton drawings. 
     * @param g
     * @param connectedByMycelium
     */
    public void drawLine(Graphics g, List<GTecton> connectedByMycelium) {

        Graphics2D g2d = (Graphics2D)g;

        final int xOffset = 10;
        final int yOffset = 10;

        Point middleTectonCoords = t.getGTecton().getCoords();
        Map<Tecton, List<FungusFarmer>> neighbourMap = t.getNeighbourMap();
        List<Tecton> neighbourTectonList = new ArrayList<>(neighbourMap.keySet());

        for (GTecton connectedByMyceliaTecton : connectedByMycelium) {
            
            for (Tecton t1: neighbourTectonList) {

                if (t1 != connectedByMyceliaTecton.getTecton()) continue;

                List<FungusFarmer> fungusFarmerList = neighbourMap.get(t1);

                int startX = middleTectonCoords.x - ((fungusFarmerList.size() - 1) * xOffset) / 2;
                int startY = middleTectonCoords.y - ((fungusFarmerList.size() - 1) * yOffset) / 2;

                Point CBMCoord = connectedByMyceliaTecton.getCoords();

                int endX = CBMCoord.x - ((fungusFarmerList.size() - 1) * xOffset) / 2;
                int endY = CBMCoord.y - ((fungusFarmerList.size() - 1) * yOffset) / 2;

                for (FungusFarmer f : fungusFarmerList) {

                    FungusFarmerView ffv = (FungusFarmerView)f.getView();
                    g2d.setStroke(new BasicStroke(2));
                    g2d.setColor(ffv.getColor());
                    g2d.drawLine(startX, startY, endX, endY);

                    startX += xOffset; startY += yOffset;
                    endX += xOffset; endY += yOffset;

                }
            }
        }
    }
}