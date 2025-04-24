package fungorium;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

/*
public class TectonMap {
    List<Tecton> tectons;
    private TectonMapView view;
    private Random rand;
    private boolean isTest;
    private TectonView tectonView;
    private InsectView insectView; 
    private MyceliumView myceliumView;
    private FungusBodyView fungusBodyView;
    private ArrayList<String> playerCommands;
    private ArrayList<String> controllerCommands;
    private final int maxSporeNutrientContent = 5;
    private final int maxSporeEffectDuration = 3;

    public TectonMap(File _mapFile, Scanner _scanner, boolean isTest) {

        initViews(_scanner);

        rand = new Random();
        this.isTest = isTest;

        List<String> commands = new ArrayList<>();
        try {
            commands = Files.readAllLines(_mapFile.toPath());
        } catch (IOException e) {
            e.printStackTrace();
        }

        processAllMapCreatingCommands(commands);

    }

    private void initViews(Scanner _scanner) {

        this.view = new TectonMapView(_scanner);
        tectonView = new TectonView();
        insectView = new InsectView(); 
        myceliumView = new MyceliumView();
        fungusBodyView = new FungusBodyView();
    }

    private void addTecton(String tectonName, String tectonType) throws Exception {
        int percentToBreak = isTest ? 100 : rand.nextInt(100) + 1;

        switch (tectonType) {
            case "ORD" -> tectons.add(new OrdinaryTecton(100, tectonName, tectonView));
            case "NOB" -> tectons.add(new NoBodyTecton(percentToBreak, tectonName, tectonView));
            case "SIN" -> tectons.add(new SingleMyceliumTecton(percentToBreak, tectonName, tectonView));
            case "VAN" -> {
                int percentToVanish = isTest ? 100 : rand.nextInt(100) + 1;
                tectons.add(new MyceliumVanisherTecton(percentToBreak, percentToVanish, tectonName, tectonView));
            }
            case "SUS" -> tectons.add(new MyceliumSustainerTecton(percentToBreak, tectonName, tectonView));
            default -> throw new Exception(view.noSuchTecton(tectonName));
        }
    }

    private void setNeighbour(String[] commandParts) throws Exception {

        Tecton foundTecton = findTecton(commandParts[1]);

        for (int i = 2; i < commandParts.length; ++i){
            Tecton neighbourTecton = findTecton(commandParts[i]);
            foundTecton.addNeighbour(neighbourTecton);
        }
    }

    private void addConnection(String firstTectonName, String secondTectonName) throws Exception {

        Tecton firsTecton = findTecton(firstTectonName);
        Tecton secondTecton = findTecton(secondTectonName);

        firsTecton.addConnection(secondTecton);
    }

    private void addMycelium(String myceliumName, String tectonName) throws Exception {

        Tecton foundTecton = findTecton(tectonName);
        foundTecton.addMycelium(new Mycelium(foundTecton, myceliumName, myceliumView));

    }

    private void addBody(String myceliumName) throws Exception {

        Mycelium foundMycelium = findMycelium(myceliumName);
        foundMycelium.growBody(fungusBodyView);
    }

    private void addInsect(String insectName, String tectonName) throws Exception {

        Tecton foundTecton = findTecton(insectName);
        foundTecton.addInsect(new Insect(foundTecton, tectonName, insectView));

    }

    private void addSpore(String sporeName, String sporeType, String tectonName) throws Exception {

        Tecton foundTecton = findTecton(tectonName);

        int nutrientContent = rand.nextInt(maxSporeNutrientContent) + 1;
        int effectDuration = rand.nextInt(maxSporeEffectDuration) + 1;

        switch (sporeType) {
            case "ANT" -> foundTecton.addSpore(new AntiSeverSpore(nutrientContent, effectDuration, sporeName));
            case "BST" -> foundTecton.addSpore(new BoosterSpore(nutrientContent, effectDuration, sporeName));
            case "SLO" -> foundTecton.addSpore(new SlowingSpore(nutrientContent, effectDuration, sporeName));
            case "DUP" -> foundTecton.addSpore(new InsectDuplicatorSpore(nutrientContent, effectDuration, sporeName));
            case "STU" -> foundTecton.addSpore(new StunningSpore(nutrientContent, effectDuration, sporeName));
            case "ORD" -> foundTecton.addSpore(new OrdinarySpore(nutrientContent, effectDuration, sporeName));
            default -> throw new Exception();
        }
    }

    private void removeTecon(String tectonName) throws Exception {
        Tecton foundTecton = findTecton(tectonName);
        tectons.remove(foundTecton);
    }

    private void removeConnection(String firstTectonName, String secondTectonName) {
        Tecton firsTecton = findTecton(firstTectonName);
        Tecton secondTecton = findTecton(secondTectonName);

        firsTecton.removeConnection(secondTecton);
    }

    private void removeMycelium(String myceliumName) {
        for (Tecton t : tectons) {
            List<Mycelium> myceliumList = t.getMyceliumList();

            for (Mycelium m : myceliumList){
                if (m.getName().equals(myceliumName)) t.removeMycelium(m);
            }

        }
    }

    private void removeBody(String myceliumName) {
        Mycelium foundMycelium = findMycelium(myceliumName);
        foundMycelium.bodyDied();
    }

    private void removeInsect(String insectName) {
        for (Tecton t : tectons) {
            List<Insect> insectList = t.getInsectList();

            for (Insect i : insectList){
                if (i.getName().equals(insectName)) t.removeInsect(i);
            }

        }
    }

    private void removeSpore(String sporeName){
        for (Tecton t : tectons) {
            List<Spore> sporeList = t.getSporeList();

            for (Spore s : sporeList) {
                if (s.getName().equals(sporeName)) t.removeSpore(s);
            }

        }
    }

    private Mycelium findMycelium(String myceliumName) throws Exception{

        for (Tecton t : tectons) {
            List<Mycelium> myceliumList = t.getMyceliumList();

            for (Mycelium m : myceliumList){
                if (m.getName().equals(myceliumName)) return m;
            }

        }

        throw new Exception(view.couldntFindMycelium(myceliumName));

    }

    private Tecton findTecton(String name) throws Exception {

        for (Tecton t : tectons) {
            if (name.equals(t.getName())) return t;
        }

        throw new Exception(view.couldntFindTectont(name));
    }

    private void processCommand(String command) {

        String[] commandParts = command.split(" ");
        try {

            switch (commandParts[0]) {
                case "ADD_TEC" -> addTecton(commandParts[1], commandParts[2]);
                case "SET_NGH" -> setNeighbour(commandParts);
                case "ADD_CON" -> addConnection(commandParts[1], commandParts[2]);
                case "ADD_MYC" -> addMycelium(commandParts[1], commandParts[2]);
                case "ADD_BOD" -> addBody(commandParts[1]);
                case "ADD_INS" -> addInsect(commandParts[1], commandParts[2]);
                case "ADD_SPO" -> addSpore(commandParts[1], commandParts[2], commandParts[3]);
                case "RM_TEC" -> removeTecon(commandParts[1]);
                case "RM_CON" -> removeConnection(commandParts[1], commandParts[2]);
                case "RM_MYC" -> removeMycelium(commandParts[1]);
                case "RM_BOD" -> removeBody(commandParts[1]);
                case "RM_INS" -> removeInsect(commandParts[1]);
                case "RM_SPO" -> removeSpore(commandParts[1]);
            }
        }
        catch(Exception e) {
            e.printStackTrace();
        }
    }

    private void processAllMapCreatingCommands(List<String> commands) {
        for (String command : commands) {
            processCommand(command);
        }
    }

    public List<Tecton> map(){
        return tectons;
    }

    public void showMap() {
        view.showMap(this);
    }

    public void refreshMap() {
        view.refreshMap(this);
    }

}

 */