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
}