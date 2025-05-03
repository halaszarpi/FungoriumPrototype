package fungorium;

import java.util.List;
import java.util.Scanner;

/**
 * The TectonMapView class provides a view of the current state of the tecton map.
 * It is responsible for displaying the state of the map, refreshing it based on user input,
 * and displaying error messages related to tectons, mycelium, insects, spores, etc.
 */
public class TectonMapView {

    private final TectonMap tectonMap;
    private final Scanner scanner;

    /**
     * Constructor to initialize the TectonMapView with a specific TectonMap and Scanner.
     *
     * @param tectonMap The TectonMap this view will represent.
     * @param _scanner The Scanner used for reading user input.
     */
    public TectonMapView(TectonMap tectonMap, Scanner _scanner) {
        this.tectonMap = tectonMap;
        this.scanner = _scanner;
    }

    /**
     * Displays the current state of the tecton map by printing all tectons on the map.
     */
    public void showMap() {
        System.out.println("Current map state:\n");
        for (Tecton tecton : tectonMap.getTectons()) {
            System.out.println(tecton.toString() + "\n");
        }
    }

    /**
     * Asks the user if they want to refresh and view the map after it might have changed.
     * If the user answers "Y", it displays the updated map.
     */
    public void refreshMap() {
        System.out.println("Map might have changed! Do you want to see the full map? (Y/N)");
        String input = scanner.nextLine().toUpperCase();
        if (input.equals("Y")) {
            showMap();
        }
    }

    /**
     * Returns a message indicating that a given tecton type does not exist in the map.
     *
     * @param tectonType The type of the tecton that could not be found.
     * @return A string message indicating the absence of the tecton type.
     */
    public String noSuchTecton(String tectonType) { return "No such tecton type(" + tectonType + ")!"; }

    /**
     * Returns a message indicating that a tecton with the given name was not found in the map.
     *
     * @param name The name of the tecton that could not be found.
     * @return A string message indicating that the tecton could not be found.
     */
    public String couldntFindTectont(String name) { return "There is no tecton on the map with the name " + name + "!"; }

    /**
     * Returns a message indicating that a mycelium with the given name was not found in the map.
     *
     * @param name The name of the mycelium that could not be found.
     * @return A string message indicating that the mycelium could not be found.
     */
    public String couldntFindMycelium(String name) { return "There is no mycelium on the map with the name " + name + "!"; }

    /**
     * Returns a message indicating that an insect with the given name was not found in the map.
     *
     * @param name The name of the insect that could not be found.
     * @return A string message indicating that the insect could not be found.
     */
    public String couldntFindInsect(String name) { return "There is no insect on the map with the name " + name + "!"; }

    /**
     * Returns a message indicating that a spore with the given name was not found in the map.
     *
     * @param name The name of the spore that could not be found.
     * @return A string message indicating that the spore could not be found.
     */
    public String couldntFindSpore(String name) { return "There is no spore on the map with the name " + name + "!"; }

    /**
     * Returns a message indicating that a tecton name is missing in either the input or output map.
     *
     * @param name The name of the missing tecton.
     * @return A string message indicating that the tecton is missing.
     */
    public String noTectonNamePair(String name) { return "There is a missing tecton in the input or output map (" + name + ")!"; }

    /**
     * Returns a message indicating that two tectons are different in the input and output maps.
     *
     * @param t1 The tecton in the input map.
     * @param t2 The tecton in the output map.
     * @return A string message showing the differences between the two tectons.
     */
    public String tectonsAreDifferent(Tecton t1, Tecton t2) { 
        return "\nThe following tectons are different in the input and output maps (name: " + t1.getName() + "):\n\nInput map:" + t1.toString() + "\n\nOutput map: " + t2.toString() + "";
    }
}