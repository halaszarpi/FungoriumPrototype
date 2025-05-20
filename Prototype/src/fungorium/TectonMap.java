package fungorium;

import java.io.File;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;


public class TectonMap {
    private List<Tecton> tectons;
    private TectonMapView view;
    private Random rand;
    public TectonMap() {
        this.view = new TectonMapView(this);
        rand = new Random();
        tectons = new ArrayList<>();
    }

    public void addTecton(Tecton t){
        tectons.add(t);
    }

    private void addNewTecton(String tectonName, String tectonType) throws Exception {
        Tecton tecton;
        switch (tectonType) {
            case "ORD" -> tecton = new OrdinaryTecton(tectonName, this);
            case "NOB" -> tecton = new NoBodyTecton(tectonName, this);
            case "SIN" -> tecton = new SingleMyceliumTecton(tectonName, this);
            case "VAN" -> tecton = new MyceliumVanisherTecton(tectonName, this);
            case "SUS" -> tecton = new MyceliumSustainerTecton(tectonName, this);
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

    private void processMapCreatingCommand(String command) throws Exception {

        String[] commandParts = command.split(" ");

        switch (commandParts[0]) {
            case "ADD_TEC" -> addNewTecton(commandParts[1], commandParts[2]);
            case "SET_NGH" -> setNeighbour(commandParts);
        }
    }

    public void processAllMapCreatingCommands(File mapFile) throws Exception {

        List<String> commands = new ArrayList<>();
        commands = Files.readAllLines(mapFile.toPath());

        for (String command : commands) {
            processMapCreatingCommand(command);
        }
    }

    public void roundPassed() {

        int initialTectonsSize = tectons.size();

        for (int i = 0; i < initialTectonsSize; i++){
            Tecton t = tectons.get(i);
            t.roundPassed();
        }
    }

    public List<Tecton> getTectons() {
        return tectons;
    }

    public List<Spore> getSpores() {
        List<Spore> spores = new ArrayList<>();
        for (Tecton t : tectons) {
            spores.addAll(t.getSporeList());
        }
        return spores;
    }

    public List<Insect> getInsects() {
        List<Insect> insects = new ArrayList<>();
        for (Tecton t : tectons) {
            insects.addAll(t.getInsectList());
        }
        return insects;
    }

    public Mycelium[] getMyceliums() {
        List<Mycelium> myceliums = new ArrayList<>();
        for (Tecton t : tectons) {
            myceliums.addAll(t.getMyceliumList());
        }
        return myceliums.toArray(new Mycelium[0]);
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
            List<Insect> insectList = t.getInsectList();

            for (Insect i : insectList) {
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
}