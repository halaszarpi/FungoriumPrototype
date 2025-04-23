package fungorium;

import java.util.List;
import java.util.Scanner;

public class TectonMapView {

    private final Scanner scanner;
    public TectonMapView(Scanner _scanner) {
        this.scanner = _scanner;
    }

    public void showMap(TectonMap map) {
        System.out.println("Current map state:");
        for (Tecton tecton : map.tectons) {
            System.out.println(tecton.ToString());
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
}