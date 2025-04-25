package fungorium;

import java.util.List;
import java.util.Scanner;
public class TectonMapView {

    private final Scanner scanner;
    public TectonMapView(Scanner _scanner) {
        this.scanner = _scanner;
    }

    public void showMap(TectonMap map) {
        System.out.println("Current map state:\n");
        for (Tecton tecton : map.tectons) {
            System.out.println(tecton.toString() + "\n");
        }
    }

    public void refreshMap(TectonMap map) {
        System.out.println("Map might have changed! Do you want to see the full map? (Y/N)");
        String input = scanner.nextLine().toUpperCase();
        if (input.equals("Y")) {
            showMap(map);
        }
    }

    public String noSuchTecton(String tectonType) { return "No such tecton type(" + tectonType + ")!"; }

    public String couldntFindTectont(String name) { return "There is no tecton on the map with the name " + name + "!"; }

    public String couldntFindMycelium(String name) { return "There is no mycelium on the map with the name " + name + "!"; }

    public String couldntFindInsect(String name) { return "There is no insect on the map with the name " + name + "!"; }

    public String couldntFindSpore(String name) { return "There is no spore on the map with the name " + name + "!"; }

    public String noTectonNamePair(String name) { return "There is a missing tecton in the input or output map (" + name + ")!"; }

    public String tectonsAreDifferent(Tecton t1, Tecton t2) { 
        return "\nThe following tectons are different in the input and output maps (name: " + t1.getName() + "):\n\nInput map:" + t1.toString() + "\n\nOutput map: " + t2.toString() + "";
    }
    
    public void getMessage(Exception e) { System.out.println(e.getMessage()); }
}