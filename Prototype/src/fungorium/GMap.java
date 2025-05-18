package fungorium;

import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.imageio.ImageIO;
import javax.swing.JPanel;

public class GMap extends JPanel {
    
    private Image tempImage = null;
    private TectonMap gameMap;
    private ArrayList<GTecton> gTectons;
    private GTecton chosenTecton;
    private List<GTecton> chosenTectonNeighbours;
    private final int radius = 200;

    public GMap(TectonMap map) {

        try {
            tempImage = ImageIO.read(new File("Prototype/src/fungorium/hatter.png"));
        }
        catch (IOException e) {

        }

        chosenTecton = null;
        gameMap = map;
        gTectons = new ArrayList<>();
        chosenTectonNeighbours = new ArrayList<>();
    }

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

    public GTecton findGTectonByTecton(Tecton t) {

        for (GTecton gt : gTectons) {
            if (gt.getTecton() == t) return gt;
        }

        return null;
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

        g.drawImage(tempImage, 0, 0, getWidth(), getHeight(), this);

        TectonView tv = (TectonView)chosenTecton.getObserver();
        tv.drawLine(g, getConnectedGTectonts(chosenTecton));

        for (GTecton aNeighbour : chosenTectonNeighbours) {
            aNeighbour.update(g);
        }
    }


    public List<Tecton> getTectons() {
        return gameMap.getTectons();
    }

    public List<Spore> getSpores() {
        return gameMap.getSpores();
    }

    public List<Insect> getInsects() {
        return gameMap.getInsects();
    }

    public Insect findInsectByName(String name) {
        for (Insect insect : gameMap.getInsects()) {
            if (insect.getName().equals(name)) {
                return insect;
            }
        }
        return null;
    }

    public Mycelium findMyceliumByName(String name){
        for (Mycelium mycelium : gameMap.getMyceliums()) {
            if (mycelium.getName().equals(name)) {
                return mycelium;
            }
        }
        return null;
    }

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

    public TectonMap getGameMap() { return gameMap; }

    public void roundPassed() {
        gameMap.roundPassed();
    }

}
