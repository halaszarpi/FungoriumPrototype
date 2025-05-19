package fungorium;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.imageio.ImageIO;
import javax.swing.JPanel;

/**
 * A JPanel which visualizes the game itself.
 */
public class GMap extends JPanel {
    
    private Image image = null;
    private TectonMap gameMap;
    private ArrayList<GTecton> gTectons;
    private GTecton chosenTecton;
    private List<GTecton> chosenTectonNeighbours;
    private final int radius = 200;

    public GMap(TectonMap map) {

        try {
            image = ImageIO.read(new File("Prototype/src/fungorium/Tektonhatter.png"));
        }
        catch (IOException e) {
            e.printStackTrace();
        }

        chosenTecton = null;
        gameMap = map;
        gTectons = new ArrayList<>();
        chosenTectonNeighbours = new ArrayList<>();
    }

    /**
     * Updates and creates GTectons for every existing tecton on the map.
     */
    private void updateGTectons() {

        List<Tecton> currentTectons = gameMap.getTectons();
        int gameMapSize = currentTectons.size();
        int gTectonsSize = gTectons.size();

        if (gameMapSize == gTectonsSize) { return; }

        int tectonAmountDifference = gameMapSize - gTectonsSize;

        int startIndex = gameMapSize - tectonAmountDifference;

        for (int i = startIndex; i < gameMapSize; i++) {

            GTecton newGTecton = new GTecton(currentTectons.get(i), this);
            currentTectons.get(i).setGTecton(newGTecton);
            gTectons.add(newGTecton);

        }
    }

    /**
     * Sets the chosen tecton's neighbour's GTectons.
     */
    private void setChosenTectonsNeighbourGTectons() {
        if (chosenTecton == null) {
            return;
        }

        List<Tecton> neighbours = new ArrayList<>(chosenTecton.getTecton().getNeighbourMap().keySet());
        List<GTecton> gNeighbours = new ArrayList<>();
        gNeighbours.add(chosenTecton);

        for (GTecton gt : gTectons) {
            if (neighbours.contains(gt.getTecton())) {
                gNeighbours.add(gt);
            }
        }

        chosenTectonNeighbours = gNeighbours;
    }

    /**
     * Specifies the coordinates of the neigbours of the chosen tecton according to the
     * number of neighbours.
     */
    public void setNeigboursCoordinates() {

        int chosenTectonX = this.getWidth() / 2;
        int chosenTectonY = this.getHeight() / 2;
        chosenTecton.setCoordinates(chosenTectonX, chosenTectonY);

        double startingAngle = 0;
        double phi = 360.0 / (chosenTectonNeighbours.size()-1); // elfordulas

        List<GTecton> nghb = new ArrayList<>(chosenTectonNeighbours);
        nghb.remove(chosenTecton);

        for (GTecton gt : nghb) {
            double xChange = Math.sin(startingAngle * Math.PI / 180);
            double yChange = Math.cos(startingAngle * Math.PI / 180);
            gt.setCoordinates((int)(chosenTectonX + radius * xChange), (int)(chosenTectonY + radius * yChange));
            startingAngle += phi;
        }

    }

    /**
     * GTecton search by Tecton.
     * @param t the tecton to be searched
     * @return GTecton of the searched tecton
     */
    public GTecton findGTectonByTecton(Tecton t) {

        for (GTecton gt : gTectons) {
            if (gt.getTecton() == t) return gt;
        }

        return null;
    }

    /**
     * GTecton search by Tecton's name.
     * @param name the tecton's name
     * @return GTecton of the searched tecton
     */
    public GTecton findGTectonByName(String name){
        updateGTectons();
        for (GTecton gt : gTectons) {
            if (gt.getTecton().getName().equals(name)) {
                return gt;
            }
        }
        return null;
    }

    /**
     * Updates the GTectons, sets the chosen tecton to the middle tecton (middle of the screen),
     * sets the chosen's neighbours and their coordinates and repaints the screen.
     * @param middleTecton the tecton in the middle of the screen
     */
    public void drawMap(GTecton middleTecton) {
        updateGTectons();
        chosenTecton = middleTecton;
        setChosenTectonsNeighbourGTectons();
        setNeigboursCoordinates();
        repaint();
    }

    @Override
    public void paintComponent(Graphics g) {

        g.drawImage(image, 0, 0, getWidth(), getHeight(), this);

        TectonView tv = (TectonView)chosenTecton.getObserver();
        tv.drawLine(g, getConnectedGTectonts(chosenTecton));

        for (GTecton aNeighbour : chosenTectonNeighbours) {
            aNeighbour.update(g);
        }
    }

    /**
     * Returns the list of tectons on the game map.
     * @return the tectons
     */
    public List<Tecton> getTectons() {
        return gameMap.getTectons();
    }

    /**
     * Returns the list of spores on the game map.
     * @return the spores
     */
    public List<Spore> getSpores() {
        return gameMap.getSpores();
    }

    /**
     * Returns the list of insects on the game map.
     * @return the insects.
     */
    public List<Insect> getInsects() {
        return gameMap.getInsects();
    }

    /**
     * Insect search by insect name.
     * @param name the insect's name
     * @return the Insect
     */
    public Insect findInsectByName(String name) {
        for (Insect insect : gameMap.getInsects()) {
            if (insect.getName().equals(name)) {
                return insect;
            }
        }
        return null;
    }

    /**
     * Mycelium search by name.
     * @param name the mycelium's name
     * @return the Mycelium
     */
    public Mycelium findMyceliumByName(String name){
        for (Mycelium mycelium : gameMap.getMyceliums()) {
            if (mycelium.getName().equals(name)) {
                return mycelium;
            }
        }
        return null;
    }

    /**
     * Gets the connected GTectons for the middle tecton.
     * @param gt the tecton in the middle of the screen
     * @return the GTectons
     */
    public List<GTecton> getConnectedGTectonts(GTecton gt) {
        ArrayList<GTecton> gtectons = new ArrayList<>();
        Tecton middleTecton = gt.getTecton();
        Map<Tecton, List<FungusFarmer>> neighbourMap = middleTecton.getNeighbourMap();
        List<Tecton> neighbours = new ArrayList<>(neighbourMap.keySet());

        for (Tecton t : neighbours) {
            if (!neighbourMap.get(t).isEmpty()) gtectons.add(findGTectonByTecton(t));
        }

        return gtectons;
    }

    /**
     * Returns the game map.
     * @return the game map
     */
    public TectonMap getGameMap() { return gameMap; }

    /**
     * Calls TectonMaps roundPassed function which notifies each tecton of the map that a round has passed by.
     */
    public void roundPassed() {
        gameMap.roundPassed();
    }

}
