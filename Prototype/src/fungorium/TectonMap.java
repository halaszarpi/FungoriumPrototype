package fungorium;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

public class TectonMap {
    List<Tecton> tectons;
    private final TectonMapView view;
    private Random rand;
    private boolean isTest;
    private TectonView tectonView;

    public TectonMap(List<Tecton> _tectons, Scanner _scanner) {
        this.tectons = _tectons;
        this.view = new TectonMapView(_scanner);
    }

    public TectonMap(File _mapFile, Scanner _scanner, boolean isTest) {
        rand = new Random();
        this.isTest = isTest;
        this.view = new TectonMapView(_scanner);
        List<String> commands = new ArrayList<>();

        try {
            commands = Files.readAllLines(_mapFile.toPath());
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private void addTecton(String[] commandParts) {
        int percentToBreak = isTest ? 100 : rand.nextInt(100) + 1;

        switch (commandParts[2]) {
            case "ORD" :
                tectons.add(new OrdinaryTecton(100, commandParts[1]));
        }
    }

    private void processCommand(String _command) {
        String[] commandParts = _command.split(" ");
        switch (commandParts[0]) {
            case "ADD_TEC":
        }
    }

    private void processAllCommands(List<String> _commands) {
        for (String command : _commands) {
            Strin
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