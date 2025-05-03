package fungorium;

import java.io.File;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

/**
 * Handles the creation, management, and modification of a map consisting of Tectons in the Fungorium world.
 * Supports both normal operation and test mode for easier testing.
 */
public class TectonMap {
    private List<Tecton> tectons;
    private TectonMapView view;
    private Random rand;
    private boolean isTest;
    private final int maxSporeNutrientContent = 5;
    private final int maxSporeEffectDuration = 3;
    private final FungusFarmer testerFarmer;
    private final InsectKeeper testerKeeper;

    /**
     * Constructor to TectonMap with a scanner for input and a flag indicating if it's in test mode.
     *
     * @param _scanner Scanner to use for input
     * @param isTest   whether the map is used for testing purposes
     */
    public TectonMap(Scanner _scanner, boolean isTest) {

        this.view = new TectonMapView(this, _scanner);
        rand = new Random();
        this.isTest = isTest;
        testerFarmer = new FungusFarmer("testerFarmer");
        testerKeeper = new InsectKeeper("testerKeeper");
        if (isTest) {
            testerFarmer.actionPoints = 100;
            testerKeeper.actionPoints = 100;
        }
        tectons = new ArrayList<>();
    }

    /** Adds a Tecton of specified type and name to the map. */
    private void addTecton(String tectonName, String tectonType) throws Exception {
        int percentToBreak = isTest ? 100 : rand.nextInt(10) + 1;
        Tecton tecton = null;

        switch (tectonType) {
            case "ORD" -> tecton = new OrdinaryTecton(percentToBreak, tectonName, this);
            case "NOB" -> tecton = new NoBodyTecton(percentToBreak, tectonName, this);
            case "SIN" -> tecton = new SingleMyceliumTecton(percentToBreak, tectonName, this);
            case "VAN" -> tecton = new MyceliumVanisherTecton(percentToBreak, tectonName, this);
            case "SUS" -> tecton = new MyceliumSustainerTecton(percentToBreak, tectonName, this);
            default -> throw new Exception(view.noSuchTecton(tectonName));
        }
        tectons.add(tecton);
    }

    /** Sets neighboring Tectons for a given Tecton. */
    private void setNeighbour(String[] commandParts) throws Exception {

        Tecton foundTecton = findTecton(commandParts[1]);

        for (int i = 2; i < commandParts.length; ++i){
            Tecton neighbourTecton = findTecton(commandParts[i]);
            foundTecton.addNeighbour(neighbourTecton);
        }
    }

    /** Adds a connection between two Tectons. */
    private void addConnection(String firstTectonName, String secondTectonName) throws Exception {

        Tecton firsTecton = findTecton(firstTectonName);
        Tecton secondTecton = findTecton(secondTectonName);

        firsTecton.addConnection(secondTecton);
    }

    /** Adds a Mycelium to a Tecton. */
    private void addMycelium(String myceliumName, String tectonName) throws Exception {

        Tecton foundTecton = findTecton(tectonName);
        Mycelium newMycelium = new Mycelium(myceliumName, testerFarmer, foundTecton);
        testerFarmer.addMycelium(newMycelium);
        foundTecton.addMycelium(newMycelium);

    }

    /** Grows a body on a Mycelium using a Spore. */
    private void addBody(String myceliumName, String sporeToUseName, String sporeTypeToScatter) throws Exception {

        Mycelium foundMycelium = findMycelium(myceliumName);
        Spore foundSpore = findSpore(sporeToUseName);
        foundMycelium.growBody(foundSpore, sporeTypeToScatter);
    }

    /** Adds an Insect to a Tecton. */
    private void addInsect(String insectName, String tectonName) throws Exception {

        Tecton foundTecton = findTecton(tectonName);
        Insect newInsect = new Insect(insectName, foundTecton, testerKeeper);
        foundTecton.addInsect(newInsect);
        testerKeeper.addInsect(newInsect);

    }

    /** Adds a Spore to a Tecton and gives it to the tester farmer. */
    private void addSpore(String sporeName, String sporeType, String tectonName) throws Exception {

        Tecton foundTecton = findTecton(tectonName);

        int nutrientContent = maxSporeNutrientContent;
        int effectDuration = maxSporeEffectDuration;

        Spore s = null;

        switch (sporeType) {
            case "ANT" -> s = new AntiSeverSpore(testerFarmer, nutrientContent, effectDuration, sporeName);
            case "BST" -> s = new BoosterSpore(testerFarmer, nutrientContent, effectDuration, sporeName);
            case "SLO" -> s = new SlowingSpore(testerFarmer, nutrientContent, effectDuration, sporeName);
            case "DUP" -> s = new InsectDuplicatorSpore(testerFarmer, nutrientContent, effectDuration, sporeName);
            case "STU" -> s = new StunningSpore(testerFarmer, nutrientContent, effectDuration, sporeName);
            case "ORD" -> s = new OrdinarySpore(testerFarmer, nutrientContent, effectDuration, sporeName);
            default -> throw new Exception();
        }

        foundTecton.addSpore(s);
        testerFarmer.addSpore(s);
    }

    /** Removes a Tecton from the map. */
    private void removeTecon(String tectonName) throws Exception {
        Tecton foundTecton = findTecton(tectonName);
        tectons.remove(foundTecton);
    }

    /** Removes the connection between two Tectons. */
    private void removeConnection(String firstTectonName, String secondTectonName) throws Exception {
        Tecton firsTecton = findTecton(firstTectonName);
        Tecton secondTecton = findTecton(secondTectonName);

        firsTecton.removeConnection(secondTecton);
    }

    /** Removes a Mycelium from the map. */
    private void removeMycelium(String myceliumName) throws Exception {

        Mycelium foundMycelium = findMycelium(myceliumName);

        for (Tecton t : tectons) {

            if (t.getMyceliumList().contains(foundMycelium)) {
                t.removeMycelium(foundMycelium);
                testerFarmer.removeMycelium(foundMycelium);
            }
        }
    }

    /** Removes the body of a Mycelium. */
    private void removeBody(String myceliumName) throws Exception {
        Mycelium foundMycelium = findMycelium(myceliumName);
        foundMycelium.bodyDied();
    }

    /** Removes an Insect from the map. */
    private void removeInsect(String insectName) throws Exception {

        Insect foundInsect = findInsect(insectName);

        for (Tecton t : tectons) {

            if (t.getInsectList().contains(foundInsect)) {
                t.removeInsect(foundInsect);
                testerKeeper.removeInsect(foundInsect);
            }
        }
    }

    /** Removes a Spore from the map. */
    private void removeSpore(String sporeName) throws Exception {

        Spore foundSpore = findSpore(sporeName);

        for (Tecton t : tectons) {

            if (t.getSporeList().contains(foundSpore)) { t.removeSpore(foundSpore); }
        }
    }

    /** Finds and returns a Spore by its name. */
    public Spore findSpore(String sporeName) throws Exception {

        for (Tecton t : tectons) {
            List<Spore> sporeList = t.getSporeList();

            for (Spore s : sporeList) {
                if (s.getName().equals(sporeName)) return s;
            }

        }

        throw new Exception(view.couldntFindSpore(sporeName));
    }

    /** Finds and returns an Insect by its name. */
    public Insect findInsect(String insectName) throws Exception {

        for (Tecton t : tectons) {
            List<Insect> myceliumList = t.getInsectList();

            for (Insect i : myceliumList) {
                if (i.getName().equals(insectName)) return i;
            }

        }

        throw new Exception(view.couldntFindInsect(insectName));
    }

    /** Finds and returns a Mycelium by its name. */
    public Mycelium findMycelium(String myceliumName) throws Exception{

        for (Tecton t : tectons) {
            List<Mycelium> myceliumList = t.getMyceliumList();

            for (Mycelium m : myceliumList){
                if (m.getName().equals(myceliumName)) return m;
            }

        }

        throw new Exception(view.couldntFindMycelium(myceliumName));

    }

    /** Finds and returns a Tecton by its name. */
    public Tecton findTecton(String name) throws Exception {

        for (Tecton t : tectons) {
            if (name.equals(t.getName())) return t;
        }

        throw new Exception(view.couldntFindTectont(name));
    }

    /** Sets an effect on an Insect using a specific effect name and duration. */
    private void setEffect(String insectName, String effectName, String duration) throws Exception {
        Insect foundInsect = findInsect(insectName);
        switch (effectName) {
            case "ANT" -> foundInsect.setAntiSeveredForRounds(Integer.parseInt(duration));
            case "BST" -> foundInsect.setBoostedForRounds(Integer.parseInt(duration));
            case "SLO" -> foundInsect.setSlowedForRounds(Integer.parseInt(duration));
            case "STU" -> foundInsect.setStunnedForRounds(Integer.parseInt(duration));
            default -> throw new Exception();
        }
    }

    /** Processes a single command for creating or modifying the map structure. */
    private void processMapCreatingCommand(String command) throws Exception {

        String[] commandParts = command.split(" ");

        switch (commandParts[0]) {
            case "ADD_TEC" -> addTecton(commandParts[1], commandParts[2]);
            case "SET_NGH" -> setNeighbour(commandParts);
            case "ADD_CON" -> addConnection(commandParts[1], commandParts[2]);
            case "ADD_MYC" -> addMycelium(commandParts[1], commandParts[2]);
            case "ADD_BOD" -> addBody(commandParts[1], commandParts[2], commandParts[3]);
            case "ADD_INS" -> addInsect(commandParts[1], commandParts[2]);
            case "ADD_SPO" -> addSpore(commandParts[1], commandParts[2], commandParts[3]);
            case "RM_TEC" -> removeTecon(commandParts[1]);
            case "RM_CON" -> removeConnection(commandParts[1], commandParts[2]);
            case "RM_MYC" -> removeMycelium(commandParts[1]);
            case "RM_BOD" -> removeBody(commandParts[1]);
            case "RM_INS" -> removeInsect(commandParts[1]);
            case "RM_SPO" -> removeSpore(commandParts[1]);
            case "SET_EFF" -> setEffect(commandParts[1], commandParts[2], commandParts[3]);
        }
    }

    /**
     * Reads a file containing multiple map creating commands and processes them.
     *
     * @param mapFile file containing the commands
     */
    public void processAllMapCreatingCommands(File mapFile) throws Exception {

        List<String> commands = new ArrayList<>();
        commands = Files.readAllLines(mapFile.toPath());

        for (String command : commands) {
            processMapCreatingCommand(command);
        }
    }

    /** Breaks a Tecton into smaller parts (according to its behavior). */
    private void breakTecton(String tectonName, String oneNeighbourNameOfTecton) throws Exception {
        Tecton foundTecton = findTecton(tectonName);

        foundTecton.breakTecton(oneNeighbourNameOfTecton);
    }

    /** Vanishes all Myceliums from a Tecton. */
    private void vanishMycelium(String tectonName) throws Exception {
        Tecton foundTecton = findTecton(tectonName);

        foundTecton.vanishMycelium();
    }

    /** Simulates a round passing for all Tectons. */
    public void roundPassed(String allTectonBreakChance) {

        int initialTectonsSize = tectons.size();

        for (int i = 0; i < initialTectonsSize; i++){
            Tecton t = tectons.get(i);
            if (allTectonBreakChance != null) { t.setBreakPercent(Integer.parseInt(allTectonBreakChance)); }
            t.roundPassed();
            if (allTectonBreakChance != null) { t.setBreakPercent(100); }
        }

        testerFarmer.roundPassed();
        testerKeeper.roundPassed();
    }

    /** Processes a single input command related to game actions. */
    private void processInputCommand(String command) throws Exception {
        String[] commandParts = command.split(" ");
        switch (commandParts[0]) {
            case "GROWMYC" -> testerFarmer.changeMapBasedOnCommands(this, commandParts);
            case "GROWBOD" -> testerFarmer.changeMapBasedOnCommands(this, commandParts);
            case "EATINS" -> testerFarmer.changeMapBasedOnCommands(this, commandParts);
            case "SCATTERSP" -> testerFarmer.changeMapBasedOnCommands(this, commandParts);
            case "MOVETOTECTON" -> testerKeeper.changeMapBasedOnCommands(this, commandParts);
            case "CUTMYC" -> testerKeeper.changeMapBasedOnCommands(this, commandParts);
            case "EATSPORE" -> testerKeeper.changeMapBasedOnCommands(this, commandParts);
            case "BREAKTEC" -> breakTecton(commandParts[1], commandParts[2]);
            case "VANISHMYC" -> vanishMycelium(commandParts[1]);
            case "ROUND" -> roundPassed(commandParts[1]);
        }
    }

    /**
     * Processes multiple input commands, such as growing myceliums, moving insects, etc.
     *
     * @param commands list of input commands
     */
    public void runAllInputCommands(List<String> commands) throws Exception{
        for (String command : commands){
            processInputCommand(command);
        }
    }

    /**
     * Checks if two TectonMaps are equivalent by comparing their Tectons.
     *
     * @param outputMap the map to compare against
     * @return true if the maps are equal
     */
    public boolean mapsAreEqual(TectonMap outputMap) throws Exception {

        TectonMap map = outputMap.getTectons().size() > tectons.size() ? outputMap : this;
        TectonMap otherMap = outputMap == map ? this : outputMap;
        map.testerFarmer.actionPoints = map.testerFarmer.score = map.testerKeeper.actionPoints = map.testerKeeper.score = 100;
        otherMap.testerFarmer.actionPoints = otherMap.testerKeeper.actionPoints = otherMap.testerKeeper.score = otherMap.testerFarmer.score = 100;

        for (Tecton t1 : map.getTectons()) {
            boolean foundName = false;

            for (Tecton t2 : otherMap.getTectons()) {

                if (t1.getName().equals(t2.getName())) {
                    foundName = true;

                    if (!t1.isEqual(t2)) { throw new Exception(view.tectonsAreDifferent(t1, t2)); }
                }
            }

            if (!foundName) { throw new Exception(view.noTectonNamePair(t1.getName())); }
        }

        return true;
    }

    /** Returns the list of Tectons in the map. */
    public List<Tecton> map() {
        return tectons;
    }

    /** Displays the current map state. */
    public void showMap() {
        if (!GameTesterController.SHOW_OUTPUT) return;
        view.showMap();
    }

    /** Asks if the user wants to refresh the displayed map and shows it if yes. */
    public void refreshMap() {
        view.refreshMap();
    }

    /** Getter for the list of Tectons. */
    public List<Tecton> getTectons() { return tectons; }

    /** Adds a Tecton to the map. */
    public void add(Tecton t) { tectons.add(t); }

}