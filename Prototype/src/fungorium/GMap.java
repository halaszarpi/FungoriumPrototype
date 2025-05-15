package fungorium;

import java.awt.Graphics;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JPanel;

public class GMap extends JPanel {
    
    private TectonMap gameMap;
    private ArrayList<GTecton> gTectons;
    private GTecton chosenTecton;
    private List<GTecton> chosenTectonNeighbours;
    private final int radius = 200; // pixelben

    public GMap(TectonMap map) {
        chosenTecton = null;
        gameMap = map;
        gTectons = new ArrayList<>();
        chosenTectonNeighbours = new ArrayList<>();
        setVisible(true);
    }

    private void updateGTectons() {

        List<Tecton> currentTectons = gameMap.getTectons();
        int gameMapSize = currentTectons.size();
        int gTectonsSize = gTectons.size();

        if (gameMapSize == gTectonsSize) { return; }

        int tectonAmountDifference = gameMapSize - gTectonsSize;

        int startIndex = gameMapSize - tectonAmountDifference;

        for (int i = startIndex; i < gameMapSize; i++) {

            GTecton newGTecton = new GTecton(currentTectons.get(i));
            gTectons.add(newGTecton);

        }
    }

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

    public void setNeigboursCoordinates() {

        int chosenTectonX = this.getWidth() / 2;
        int chosenTectonY = this.getHeight() / 2;

        double startingAngle = 0;
        double phi = 360.0 / chosenTectonNeighbours.size(); // elfordulas

        for (GTecton gt : gTectons) {
            gt.setCoordinates((int)(chosenTectonX + radius * Math.sin(startingAngle)), (int)(chosenTectonY + radius * Math.cos(startingAngle)));
            startingAngle += phi;
        }

    }

    public void drawMap() {
        updateGTectons();
        chosenTecton = gTectons.getFirst();
        setChosenTectonsNeighbourGTectons();
        setNeigboursCoordinates();
        repaint();
    }

    @Override
    public void paintComponent(Graphics g) {
        for (GTecton aNeighbour : chosenTectonNeighbours) {
            aNeighbour.update(g);
        }
    }

}
