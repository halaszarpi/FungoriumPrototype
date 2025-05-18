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
     * Provides a message indicating that two tectons are not neighbours.
     *
     * @param t2 The other Tecton being compared.
     * @return A string message indicating the two tectons are not neighbours.
     */
    public String notNeighbour(Tecton t2) { return "The tectons ("+ t.getName() + ", " + t2.getName() + ") are not neighbours!"; }

    /**
     * Provides a message indicating that two tectons are not connected by mycelium.
     *
     * @param t2 The other Tecton being compared.
     * @return A string message indicating the tectons are not connected by mycelium.
     */
    public String notConnectedByMycelium(Tecton t2) { return "The tectons ("+ t.getName() + ", " + t2.getName() + ") are not connected by mycelium!"; }

    /**
     * Provides a message indicating that a SingleMyceliumTecton already has mycelium.
     *
     * @return A string message indicating that the tecton already has mycelium.
     */
    public String singleMyceliumTectonAlreadyHasMycelium() { return "The single mycelium tecton (" + t.getName() + ") has already a mycelium on it!"; }

    /**
     * Provides a message indicating that the Tecton cannot vanish mycelium.
     *
     * @return A string message indicating that the tecton cannot vanish mycelium.
     */
    public String notMyceliumVanisherTecton() { return "Not Mycelium Vanisher Tecton (" + t.getName() + "), therefor cant vanish mycelium"; }

    /**
     * Prints a message indicating that a Tecton has been created.
     */
    public void tectonCreated() {
        //if (!GameTesterController.SHOW_OUTPUT) { return; }
    }

    /**
     * Prints a message indicating that a Tecton has broken into two,
     * with the name of the new tecton created.
     *
     * @param newTecton The new Tecton created after the break.
     */
    public void tectonBreaks(Tecton newTecton) {
        //if (!GameTesterController.SHOW_OUTPUT) { return; }
    }

    /**
     * Prints a message indicating that a new neighbour has been added to the Tecton.
     *
     * @param newTecton The new Tecton that is now a neighbour.
     */
    public void neighbourAdded(Tecton newTecton) {
        //if (!GameTesterController.SHOW_OUTPUT) { return; }
    }

    /**
     * Prints a message indicating that a new connection has been added between two tectons.
     *
     * @param newTecton The Tecton that is now connected to this Tecton.
     */
    public void connectionAdded(Tecton newTecton) {
        //if (!GameTesterController.SHOW_OUTPUT) { return; }
    }

    /**
     * Prints a message indicating that mycelium has been added to the Tecton.
     *
     * @param m The Mycelium added to the Tecton.
     */
    public void myceliumAdded(Mycelium m) {
        //if (!GameTesterController.SHOW_OUTPUT) { return; }
        t.getGTecton().attach(m.getView());
    }

    /**
     * Prints a message indicating that a spore has been added to the Tecton.
     *
     * @param s The Spore added to the Tecton.
     */
    public void sporeAdded(Spore s) {
        //if (!GameTesterController.SHOW_OUTPUT) { return; }
        t.getGTecton().attach(s.getView());
    }

    /**
     * Prints a message indicating that an insect has been added to the Tecton.
     *
     * @param i The Insect added to the Tecton.
     */
    public void insectAdded(Insect i) {
        //if (!GameTesterController.SHOW_OUTPUT) { return; }
        t.getGTecton().attach(i.getView());
    }

    /**
     * Prints a message indicating that a neighbour has been removed from the Tecton.
     *
     * @param newTecton The Tecton that is no longer a neighbour.
     */
    public void neighbourRemoved(Tecton newTecton) {
        //if (!GameTesterController.SHOW_OUTPUT) { return; }
    }

    /**
     * Prints a message indicating that mycelium has been removed from the Tecton.
     *
     * @param m The Mycelium removed from the Tecton.
     */
    public void myceliumRemoved(Mycelium m) {
        //if (!GameTesterController.SHOW_OUTPUT) { return; }
        t.getGTecton().detach(m.getView());

    }

    /**
     * Prints a message indicating that an insect has been removed from the Tecton.
     *
     * @param i The Insect removed from the Tecton.
     */
    public void insectRemoved(Insect i) {
        //if (!GameTesterController.SHOW_OUTPUT) { return; }
       t.getGTecton().detach(i.getView());
    }

    /**
     * Prints a message indicating that a spore has been removed from the Tecton.
     *
     * @param s The Spore removed from the Tecton.
     */
    public void sporeRemoved(Spore s) {
        //if (!GameTesterController.SHOW_OUTPUT) { return; }
        t.getGTecton().detach(s.getView());
    }

    /**
     * Prints a message indicating that the connection between two tectons has been removed.
     *
     * @param newTecton The Tecton that is no longer connected to this Tecton.
     */
    public void removeConnection(Tecton newTecton) {
        //if (!GameTesterController.SHOW_OUTPUT) { return; }
    }

    // Újítás, kell Graphics g paraméterben
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