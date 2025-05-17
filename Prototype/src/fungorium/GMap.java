package fungorium;

import java.awt.*;
import java.io.File;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;

import javax.swing.JPanel;

public class GMap extends JPanel {
    
    private ArrayList<GTecton> gTectons;
    private GTecton chosenTecton;
    private List<GTecton> chosenTectonNeighbours;
    private Random rand;
    private final int radius = 100; // pixelben

    public GMap() {
        chosenTecton = null;
        gTectons = new ArrayList<>();
        chosenTectonNeighbours = new ArrayList<>();
        rand = new Random();
        setSize(1080,360);
        setVisible(true);
    }

    public void addTecton(Tecton t){
        GTecton gt = new GTecton(t, this);
        gTectons.add(gt);
    }

    private void addNewTecton(String tectonName, String tectonType) throws Exception {
        int percentToBreak = rand.nextInt(10) + 1;

        Tecton tecton;
        switch (tectonType) {
            case "ORD" -> tecton = new OrdinaryTecton(percentToBreak, tectonName);
            case "NOB" -> tecton = new NoBodyTecton(percentToBreak, tectonName);
            case "SIN" -> tecton = new SingleMyceliumTecton(percentToBreak, tectonName);
            case "VAN" -> tecton = new MyceliumVanisherTecton(percentToBreak, tectonName);
            case "SUS" -> tecton = new MyceliumSustainerTecton(percentToBreak, tectonNamem);
            default -> throw new Exception("No such tecton type(" + tectonType + ")!");
        }
        gTectons.add(new GTecton(tecton, this));
    }

    private void setNeighbour(String[] commandParts) throws Exception {
        GTecton foundTecton = findGTectonByName(commandParts[1]);
        for (int i = 2; i < commandParts.length; ++i){
            GTecton neighbourTecton = findGTectonByName(commandParts[i]);
            foundTecton.getTecton().addNeighbour(neighbourTecton.getTecton());
        }
    }

    private void processMapCreatingCommand(String command) throws Exception {

        String[] commandParts = command.split(" ");

        switch (commandParts[0]) {
            case "ADD_TEC" -> addNewTecton(commandParts[1], commandParts[2]);
            case "SET_NGH" -> setNeighbour(commandParts);
        }
    }

    public void processAllMapCreatingCommands(File mapFile) throws Exception {

        List<String> commands = Files.readAllLines(mapFile.toPath());

        for (String command : commands) {
            processMapCreatingCommand(command);
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

    public void drawMap(GTecton middleTecton) {
        chosenTecton = middleTecton;
        setChosenTectonsNeighbourGTectons();
        setNeigboursCoordinates();
        repaint();
    }

    @Override
    public void paintComponent(Graphics g) {

        TectonView tv = (TectonView)chosenTecton.getObserver();
        try {
            tv.drawLine(g, chosenTecton.getCoords(), getConnectedGTectonts(chosenTecton));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        for (GTecton aNeighbour : chosenTectonNeighbours) {
            aNeighbour.update(g);
        }
    }

    public GTecton findGTectonByTecton(Tecton t) throws Exception {

        for (GTecton gt : gTectons) {
            if (gt.getTecton() == t) return gt;
        }
        throw new Exception("GTecton not found for tecton: " + t.getName());
    }

    public GTecton findGTectonByName(String name){
        for (GTecton gt : gTectons) {
            if (gt.getTecton().getName().equals(name)) {
                return gt;
            }
        }
        throw new RuntimeException("GTecton not found: " + name);
    }

    public Insect findInsectByName(String name) throws Exception {
        for (GTecton gt : gTectons) {
            List<Insect> insects = gt.getTecton().getInsectList();
            for (Insect insect : insects) {
                if (insect.getName().equals(name)) {
                    return insect;
                }
            }
        }
        throw new Exception("Insect not found: " + name);
    }

    public Mycelium findMyceliumByName(String name) throws Exception {
        for (GTecton gt : gTectons) {
            List<Mycelium> myceliums = gt.getTecton().getMyceliumList();
            for (Mycelium mycelium : myceliums) {
                if (mycelium.getName().equals(name)) {
                    return mycelium;
                }
            }
        }
        throw new Exception("Mycelium not found: " + name);
    }

    public Spore findSporeByName(String name) throws Exception {
        for (GTecton gt : gTectons) {
            List<Spore> spores = gt.getTecton().getSporeList();
            for (Spore spore : spores) {
                if (spore.getName().equals(name)) {
                    return spore;
                }
            }
        }
        throw new Exception("Spore not found: " + name);
    }

    public List<GTecton> getConnectedGTectonts(GTecton gt) throws Exception {

        ArrayList<GTecton> gtectons = new ArrayList<>();
        Tecton middleTecton = gt.getTecton();
        Map<Tecton, Boolean> neighbourMap = middleTecton.getNeighbourMap();
        List<Tecton> neighbours = new ArrayList<>(neighbourMap.keySet());

        for (Tecton t : neighbours) {
            if (neighbourMap.get(t)) gtectons.add(findGTectonByTecton(t));
        }

        return gtectons;
    }

    public void roundPassed() {
        int percentToBreak = rand.nextInt(10) + 1;

        int initialTectonsSize = gTectons.size();

        for (int i = 0; i < initialTectonsSize; i++){
            GTecton t = gTectons.get(i);
            t.getTecton().setBreakPercent(percentToBreak);
            t.getTecton().roundPassed();
        }
    }

}
