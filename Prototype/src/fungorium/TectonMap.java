package fungorium;

import java.io.File;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

public class TectonMap {
    private List<Tecton> tectons;
    private List<Tecton> tmpTectons;
    private TectonMapView view;
    private Random rand;
    private boolean isTest;
    private final int maxSporeNutrientContent = 5;
    private final int maxSporeEffectDuration = 3;
    private final FungusFarmer testerFarmer;
    private final InsectKeeper testerKeeper;

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
        tmpTectons = new ArrayList<>();
    }

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

    // ADD_MYC [mycelium_name] [tecton_name]
    private void addMycelium(String myceliumName, String tectonName) throws Exception {

        Tecton foundTecton = findTecton(tectonName);
        Mycelium newMycelium = new Mycelium(myceliumName, testerFarmer, foundTecton);
        foundTecton.addMycelium(newMycelium);

    }

    // ADD_BOD [mycelium_name] [spore_to_use_name]
    private void addBody(String myceliumName, String sporeToUseName) throws Exception {

        Mycelium foundMycelium = findMycelium(myceliumName);
        Spore foundSpore = findSpore(sporeToUseName);
        foundMycelium.growBody(foundSpore);
    }

    private void addInsect(String insectName, String tectonName) throws Exception {

        Tecton foundTecton = findTecton(tectonName);
        Insect newInsect = new Insect(insectName, foundTecton, testerKeeper);
        foundTecton.addInsect(newInsect);

    }

    private void addSpore(String sporeName, String sporeType, String tectonName) throws Exception {

        Tecton foundTecton = findTecton(tectonName);

        int nutrientContent = maxSporeNutrientContent;
        int effectDuration = maxSporeEffectDuration;

        switch (sporeType) {
            case "ANT" -> foundTecton.addSpore(new AntiSeverSpore(testerFarmer, nutrientContent, effectDuration, sporeName));
            case "BST" -> foundTecton.addSpore(new BoosterSpore(testerFarmer, nutrientContent, effectDuration, sporeName));
            case "SLO" -> foundTecton.addSpore(new SlowingSpore(testerFarmer, nutrientContent, effectDuration, sporeName));
            case "DUP" -> foundTecton.addSpore(new InsectDuplicatorSpore(testerFarmer, nutrientContent, effectDuration, sporeName));
            case "STU" -> foundTecton.addSpore(new StunningSpore(testerFarmer, nutrientContent, effectDuration, sporeName));
            case "ORD" -> foundTecton.addSpore(new OrdinarySpore(testerFarmer, nutrientContent, effectDuration, sporeName));
            default -> throw new Exception();
        }
    }

    private void removeTecon(String tectonName) throws Exception {
        Tecton foundTecton = findTecton(tectonName);
        tectons.remove(foundTecton);
    }

    private void removeConnection(String firstTectonName, String secondTectonName) throws Exception {
        Tecton firsTecton = findTecton(firstTectonName);
        Tecton secondTecton = findTecton(secondTectonName);

        firsTecton.removeConnection(secondTecton);
    }

    private void removeMycelium(String myceliumName) throws Exception {

        Mycelium foundMycelium = findMycelium(myceliumName);

        for (Tecton t : tectons) {

            if (t.getMyceliumList().contains(foundMycelium)) {
                t.removeMycelium(foundMycelium);
            }
        }
    }

    private void removeBody(String myceliumName) throws Exception {
        Mycelium foundMycelium = findMycelium(myceliumName);
        foundMycelium.bodyDied();
    }

    private void removeInsect(String insectName) throws Exception {

        Insect foundInsect = findInsect(insectName);

        for (Tecton t : tectons) {

            if (t.getInsectList().contains(foundInsect)) {
                t.removeInsect(foundInsect);
            }
        }
    }

    private void removeSpore(String sporeName) throws Exception {

        Spore foundSpore = findSpore(sporeName);

        for (Tecton t : tectons) {

            if (t.getSporeList().contains(foundSpore)) { t.removeSpore(foundSpore); }
        }
    }

    public Spore findSpore(String sporeName) throws Exception {

        for (Tecton t : tectons) {
            List<Spore> sporeList = t.getSporeList();

            for (Spore s : sporeList) {
                if (s.getName().equals(sporeName)) return s;
            }

        }

        throw new Exception(view.couldntFindSpore(sporeName));
    }

    public Insect findInsect(String insectName) throws Exception {

        for (Tecton t : tectons) {
            List<Insect> myceliumList = t.getInsectList();

            for (Insect i : myceliumList) {
                if (i.getName().equals(insectName)) return i;
            }

        }

        throw new Exception(view.couldntFindInsect(insectName));
    }

    public Mycelium findMycelium(String myceliumName) throws Exception{

        for (Tecton t : tectons) {
            List<Mycelium> myceliumList = t.getMyceliumList();

            for (Mycelium m : myceliumList){
                if (m.getName().equals(myceliumName)) return m;
            }

        }

        throw new Exception(view.couldntFindMycelium(myceliumName));

    }

    public Tecton findTecton(String name) throws Exception {

        for (Tecton t : tectons) {
            if (name.equals(t.getName())) return t;
        }

        throw new Exception(view.couldntFindTectont(name));
    }

    // DUP spora nem allit allapotot a rovaron, csak meghivja az owner duplicateInsect metodusat.
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

    private void processMapCreatingCommand(String command) throws Exception {

        String[] commandParts = command.split(" ");

        switch (commandParts[0]) {
            case "ADD_TEC" -> addTecton(commandParts[1], commandParts[2]);
            case "SET_NGH" -> setNeighbour(commandParts);
            case "ADD_CON" -> addConnection(commandParts[1], commandParts[2]);
            case "ADD_MYC" -> addMycelium(commandParts[1], commandParts[2]);
            case "ADD_BOD" -> addBody(commandParts[1], commandParts[2]);
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

    public void processAllMapCreatingCommands(File mapFile) throws Exception {

        List<String> commands = new ArrayList<>();
        commands = Files.readAllLines(mapFile.toPath());

        for (String command : commands) {
            processMapCreatingCommand(command);
        }
    }

    private void breakTecton(String tectonName) throws Exception {
        Tecton foundTecton = findTecton(tectonName);

        // ezt le kellene tarolni, ami letrejott tekton, majd berakni a map tektonlistajaba
        foundTecton.breakTecton();
        refreshMapAfterRoundPassed();
    }

    private void vanishMycelium(String tectonName) throws Exception {
        Tecton foundTecton = findTecton(tectonName);

        foundTecton.vanishMycelium();
    }

    public void roundPassed() {
        for (Tecton t : tectons){
            t.roundPassed();
        }
        refreshMapAfterRoundPassed();
    }

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
            case "BREAKTEC" -> breakTecton(commandParts[1]);
            case "VANISHMYC" -> vanishMycelium(commandParts[1]);
            case "ROUND" -> roundPassed();
        }
    }

    public void runAllInputCommands(List<String> commands) throws Exception{
        for (String command : commands){
            processInputCommand(command);
        }
    }

    public boolean mapsAreEqual(TectonMap outputMap) throws Exception {

        TectonMap map = outputMap.getTectons().size() > tectons.size() ? outputMap : this;
        TectonMap otherMap = outputMap == map ? this : outputMap;
        map.testerFarmer.actionPoints = map.testerKeeper.actionPoints = 100;
        otherMap.testerFarmer.actionPoints = map.testerKeeper.actionPoints = 100;

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

    public List<Tecton> map() {
        return tectons;
    }

    public void showMap() {
        view.showMap();
    }

    public void refreshMap() {
        view.refreshMap();
    }

    public List<Tecton> getTectons() { return tectons; }

    public void add(Tecton t) { tmpTectons.add(t); }

    public void refreshMapAfterRoundPassed() { 
        tectons.addAll(tmpTectons); 
        tmpTectons.clear();
    }

}