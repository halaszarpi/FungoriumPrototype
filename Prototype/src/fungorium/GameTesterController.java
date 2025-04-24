package fungorium;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class GameTesterController {

    private static final String workingDir = System.getProperty("user.dir");
    private Scanner scanner;
    private GameTesterView view;
    private ArrayList<String> mapBuildingCommands;
    private ArrayList<String> playerCommands;
    private ArrayList<String> controllerCommands;
    private boolean saveToFile = false;
    private String mapName = "";
    //private TectonMap loadedMap = null;

    private void initializeCommands() {

        this.mapBuildingCommands = new ArrayList<>(Arrays.asList(
                "ADD_TEC",
                "SET_NGH",
                "ADD_CON",
                "ADD_MYC",
                "ADD_BOD",
                "ADD_INS",
                "ADD_SPO",
                "RM_TEC",
                "RM_CON",
                "RM_MYC",
                "RM_BOD",
                "RM_INS",
                "RM_SPO"
        ));

        this.playerCommands = new ArrayList<>(Arrays.asList(
                "GROWMYC",
                "GROWBOD",
                "EATINS",
                "SCATTERSP",
                "MOVETOTECTON",
                "CUTMYC",
                "EATSPORE"
        ));

        this.controllerCommands = new ArrayList<>(Arrays.asList(
                "BREAKTEC",
                "VANISHMYC",
                "ROUND"
        ));
    }

    public GameTesterController(Scanner scanner) {
        this.scanner = scanner;
        this.initializeCommands();
        view = new GameTesterView();
    }

    public void printMenuAndWaitForOption() {
        view.listGameTesterOptions();
        String option = scanner.nextLine();
        switch (option) {
            case "1" -> createTest();
            case "2" -> runTest();
            case "3" -> { return; }
            default -> System.out.println("Invalid option");
        }
    }

    public void createTest() {

        // Letrehozzuk a teszt fajl mappajat
        boolean exitCreateTest = false;
        String filePath = workingDir + "\\Prototype\\src\\tests\\testCases";
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
        ArrayList<String> commandsForMapBuilding = new ArrayList<>();

        while (!validOption) {

            view.listMapOptions();
            String mapOption = scanner.nextLine();

            switch (mapOption) {
                case "1": commandsForMapBuilding = createNewMap(true); validOption = true; break;
                case "2": commandsForMapBuilding = copyExistingMapForNewTestCase(); validOption = true; break;
                default: System.out.println("Invalid option");
            }
        }

        // Ide kell egy fgv., amely kezeli a bemeneti nyelvet
        view.enterInputCommands();
        ArrayList<String> inputCommands = handleInputCommands();

        // Ide jon a kimeneti nyelvet kero fgv.
        ArrayList<String> outputCommands = createNewMap(false);

        // A 3 command listat (epito, bemenet, kimenet) tartalmazo tomb
        ArrayList<String>[] allCommands = new ArrayList[3];
        allCommands[0] = commandsForMapBuilding; // mapbuilding commands
        allCommands[1] = inputCommands; // input commands
        allCommands[2] = outputCommands; // output commands

        validateTestFiles(testName, testTXTs, allCommands);

    }

    public ArrayList<File> createTestFiles(String filePath, String testName) {

        // Letrehozzuk a fajlokat
        filePath += "\\" + testName;
        File testFileMap = new File(filePath, testName + "_map.txt");
        File testFileInput = new File(filePath, testName + "_input.txt");
        File testFileOutput = new File(filePath, testName + "_output.txt");

        return new ArrayList<>(
            Arrays.asList(
                    testFileMap,
                    testFileInput,
                    testFileOutput
            )
        );
    }

    // Ez kezeli a palyaepitest a palyaepitesnel es kimeneti nyelvnel is
    public ArrayList<String> createNewMap(boolean isCreatingMap) {

        if (isCreatingMap) {
            view.enterMapBuildingCommands();
        } else {
            view.enterOutputCommands();
        }
        
        ArrayList<String> commands = new ArrayList<>();
        String command = "";

        while (!command.equals("INIT_END")) {
            command = scanner.nextLine().trim();
            String[] commandParts = command.split(" ");
            commandParts[0] = commandParts[0].toUpperCase();

            if (commandParts[0].equals("INIT_END")) {

                if (commandParts.length == 3 && commandParts[1].equalsIgnoreCase("TRUE") && isCreatingMap) {
                    saveToFile = true;
                    mapName = commandParts[2];
                }

                break;
            }

            if (command.equals("CLEAR")) {
                System.out.println("Map cleared! Start over!");
                commands.clear();
            }

            else if (!mapBuildingCommands.contains(commandParts[0])) {
                System.out.println("Invalid command!");
            }

            else {
                commands.add(command);
            }
        }

        return commands;

    }

    public ArrayList<String> copyExistingMapForNewTestCase() {
        String filePath = workingDir + "\\Prototype\\src\\tests\\maps";
        File foundMap = null;

        while (foundMap == null) {
            view.loadMapMessage();
            String loadedMapName = scanner.nextLine() + ".txt";
            File file = new File(filePath);
            File[] files = file.listFiles();

            for (File f : files) {
                if (f.getName().equals(loadedMapName)) {
                    foundMap = f;
                    break;
                }
            }

            if (foundMap == null) {
                System.out.println("Map not found!");
            }
        }

        List<String> copiedMapBuildingCommands = new ArrayList<String>();
        try {
            copiedMapBuildingCommands = Files.readAllLines(foundMap.toPath());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new ArrayList<>(copiedMapBuildingCommands);
    }

    public void loadMap() {
        String filePath = workingDir + "\\Prototype\\src\\tests\\maps";
        File foundMap = null;

        while (foundMap == null) {
            view.loadMapMessage();
            String loadedMapName = scanner.nextLine();
            File file = new File(filePath);
            File[] files = file.listFiles();

            for (File f : files) {
                if (f.getName().equals(loadedMapName)) {
                    foundMap = f;
                    break;
                }
            }

            if (foundMap == null) {
                System.out.println("Map not found!");
            }
        }

        //loadedMap = new TectonMap(foundMap, scanner, true);
    }

    public ArrayList<String> handleInputCommands() {
        String inputCommand = "";
        ArrayList<String> returnInputCommands = new ArrayList<>();
        while (!inputCommand.equals("SAVE")) {
            inputCommand = scanner.nextLine();
            String[] inputCommandParts = inputCommand.split(" ");
            inputCommandParts[0] = inputCommandParts[0].toUpperCase();
            if (inputCommandParts[0].equals("SAVE")) { break; }
            else if (!playerCommands.contains(inputCommandParts[0]) && !controllerCommands.contains(inputCommandParts[0])) {
                view.invalidInputMessage();
            }
            else {
                returnInputCommands.add(inputCommand);
            }
        }
        return returnInputCommands;
    }

    public void validateTestFiles(String testName, ArrayList<File> testTXTs, ArrayList<String>[] commands) {
        String filePath = workingDir + "\\Prototype\\src\\tests";
        File testFolder = new File(filePath + "\\testCases", testName);
        File map = null;

        if (saveToFile) {
            map = new File(filePath + "\\maps", mapName + ".txt");
            testTXTs.add(map);
        }

        testFolder.mkdirs();

        try{

            for (File f : testTXTs) {
                f.createNewFile();
            }
            Files.write(Paths.get(testTXTs.get(0).getPath()), commands[0]);
            Files.write(Paths.get(testTXTs.get(1).getPath()), commands[1]);
            Files.write(Paths.get(testTXTs.get(2).getPath()), commands[2]);
            if (saveToFile) {
                Files.write(Paths.get(testTXTs.get(3).getPath()), commands[0]);
            }
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void runTest() {

    }
}