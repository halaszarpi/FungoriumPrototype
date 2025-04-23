package fungorium;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Scanner;

public class GameTesterController {

    private Scanner scanner;
    private GameTesterView view;
    private ArrayList<String> mapBuildingCommands;

    private void initializeMapBuildingCommands() {
        this.mapBuildingCommands = new ArrayList<>(){{
           add("ADD_TEC");
           add("SET_NGH");
           add("ADD_CON");
           add("ADD_MYC");
           add("ADD_BOD");
           add("ADD_INS");
           add("ADD_SPO");
           add("RM_TEC");
           add("RM_CON");
           add("RM_MYC");
           add("RM_BOD");
           add("RM_INS");
           add("RM_SPO");
        }};
    }

    public GameTesterController(Scanner scanner) {
        this.scanner = scanner;
        this.initializeMapBuildingCommands();
        view = new GameTesterView();
    }

    public void printMenuAndWaitForOption() {
        view.listGameTesterOptions();
        String option = scanner.nextLine();
        switch (option) {
            case "1" -> createTest();
            case "2" -> runTest();
            case "3" -> {return;}
            default -> System.out.println("Invalid option");
        }
    }

    public void createTest() {

        // Letrehozzuk a teszt fajl mappajat
        boolean exitCreateTest = false;
        String filePath = System.getProperty("user.dir") + "\\Prototype\\src\\tests";
        String testName = null;

        while (!exitCreateTest) {

            view.enterTestName();
            testName = scanner.nextLine();
            File file = new File(filePath);
            File[] files = file.listFiles();

            boolean matchingTestName = false;
            for (File f : files) {
                if (f.getName().equals(testName)) {
                    System.out.println("Test " + testName + " already exists!");
                    matchingTestName = true;
                    break;
                }
            }

            if (!matchingTestName) {
                exitCreateTest = true;
            }
        }

        // Letrehozzuk a 3 db txt-t a teszthez
        ArrayList<File> testTXTs = createTestFiles(filePath, testName);

        boolean validOption = false;
        while (!validOption) {
            view.listMapOptions();
            String mapOption = scanner.nextLine();
            switch (mapOption) {
                case "1": createNewMap(testTXTs.getFirst()); validOption = true; break;
                case "2": loadMap(testTXTs.getFirst()); validOption = true; break;
                default: System.out.println("Invalid option");
            }
        }

    }

    public ArrayList<File> createTestFiles(String filePath, String testName) {

        File testFile = new File(filePath, testName);
        testFile.mkdirs();

        // Letrehozzuk a fajlokat
        filePath += "\\" + testName;
        File testFileMap = new File(filePath, testName + "_map.txt");
        File testFileInput = new File(filePath, testName + "_input.txt");
        File testFileOutput = new File(filePath, testName + "_output.txt");

        try {
            testFileMap.createNewFile();
            testFileInput.createNewFile();
            testFileOutput.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return new ArrayList<File>(){{
            add(testFileMap);
            add(testFileInput);
            add(testFileOutput);
        }};
    }

    public void createNewMap(File mapFile) {

        view.enterMapBuildingCommands();
        ArrayList<String> commands = new ArrayList<>();
        String command = "";
        while (!command.equals("INIT_END")) {
            command = scanner.nextLine().toUpperCase().trim();
            if (command.equals("INIT_END")) { break; }
            String[] commandParts = command.split(" ");
            if (command.equals("CLEAR")) {
                System.out.println("Map cleared! Start over!");
                commands.clear();
            } else if (!mapBuildingCommands.contains(commandParts[0])) {
                System.out.println("Invalid command!");
            } else {
                commands.add(command);
            }
        }
        try {
            Files.write(Paths.get(mapFile.getPath()), commands);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void loadMap(File mapFile) {
        System.out.println("Loading map");
    }

    public void runTest() {}
}