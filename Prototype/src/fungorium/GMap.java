package fungorium;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JPanel;

public class GMap extends JPanel {
    
    private TectonMap gameMap;
    private ArrayList<GTecton> gTectons;
    private GTecton chosenTecton;
    private List<GTecton> chosenTectonNeighbours;
    private final int radius = 100; // pixelben

    public GMap(TectonMap map) {
        chosenTecton = null;
        gameMap = map;
        gTectons = new ArrayList<>();
        chosenTectonNeighbours = new ArrayList<>();
        setSize(1080,360);
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

    public GTecton findGTectonByName(String name){
        updateGTectons();
        for (GTecton gt : gTectons) {
            if (gt.getTecton().getName().equals(name)) {
                return gt;
            }
        }
        return null;
    }

    public void drawMap(GTecton middleTecton) {
        updateGTectons();
        chosenTecton = middleTecton;
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
