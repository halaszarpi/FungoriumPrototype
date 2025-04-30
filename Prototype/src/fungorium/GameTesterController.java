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
    private final Scanner scanner;
    private final GameTesterView view;
    private ArrayList<String> mapBuildingCommands;
    private ArrayList<String> playerCommands;
    private ArrayList<String> controllerCommands;
    private boolean saveToFile = false;
    private String testName = "";
    private String inputMapName = "";
    
    public static boolean SHOW_OUTPUT = true;

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
                "RM_SPO",
                "SET_EFF"
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

        printMenuAndWaitForOption();
    }

    public void printMenuAndWaitForOption() {
        while (true) {
            view.listGameTesterOptions();
            String option = scanner.nextLine();
            try {
                switch (option) {
                    case "1" -> createTest();
                    case "2" -> runTest(true);
                    case "3" -> runAllTests();
                    case "4" -> { return; }
                    default -> System.out.println("Invalid option");
                }
            }
            catch(Exception e) {
                view.getMessage(e);
            }
        }
    }

    public void createTest() throws Exception {

        // Letrehozzuk a teszt fajl mappajat
        boolean exitCreateTest = false;
        String filePath = workingDir + "\\Prototype\\src\\tests\\testCases";

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

        ArrayList<File> testTXTs = createTestFiles(filePath);

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

        view.testCreatedMessage();

        // A 3 command listat (epito, bemenet, kimenet) tartalmazo tomb
        ArrayList<String>[] allCommands = new ArrayList[3];
        allCommands[0] = commandsForMapBuilding; // mapbuilding commands
        allCommands[1] = inputCommands; // input commands
        allCommands[2] = outputCommands; // output commands

        validateTestFiles(testTXTs, allCommands);

    }

    public ArrayList<File> createTestFiles(String filePath) {

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

                    String filePath = workingDir + "\\Prototype\\src\\tests\\maps";
                    File file = new File(filePath);

                    if (isMapNameFound(commandParts[2] + ".txt", file.listFiles())) {
                        System.out.println("Map with the name " + commandParts[2] + " already exists!");
                        continue;
                    }
                    saveToFile = true;
                    inputMapName = commandParts[2];
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
                String tmpInputCommands = makeFirstPartUpperCase(commandParts);
                commands.add(tmpInputCommands);
            }
        }

        return commands;

    }

    public boolean isMapNameFound(String mapName, File[] files) {

        for (File f : files) {
            if (f.getName().equals(mapName)) { return true; }
        }

        return false;
    }

    public ArrayList<String> copyExistingMapForNewTestCase() throws Exception {
        String filePath = workingDir + "\\Prototype\\src\\tests\\maps";
        File foundMap = null;

        while (foundMap == null) {

            File file = new File(filePath);
            File[] files = file.listFiles();

            if (files.length == 0) { throw new Exception(view.noMapsToLoad()); }

            view.loadMapMessage();

            for (File f : files) {
                String name = f.getName().substring(0, f.getName().length() - 4);
                System.out.println(name);
            }

            String loadedMapName = scanner.nextLine() + ".txt";

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
        }

        catch (IOException e) {
            e.printStackTrace();
        }

        return new ArrayList<>(copiedMapBuildingCommands);
    }

    public List<String> loadTestMaps(TectonMap[] maps, boolean askForInput) throws Exception {
        String filePath = workingDir + "\\Prototype\\src\\tests\\testCases";
        File file = new File(filePath);
        File inputMap = null;
        File outputMap = null;
        File[] files = new File[3];
        files[0] = file;
        files[1] = inputMap;
        files[2] = outputMap;

        if(askForInput) {
            askForInput(filePath);
        }

        List<String> inputCommands = readMapCommands(filePath, files);

        view.tryingToBuildMaps();

        maps[0] = new TectonMap(scanner, true);
        maps[1] = new TectonMap(scanner, true);

        view.inputMapCommands();

        maps[0].processAllMapCreatingCommands(files[1]);

        view.outputMapCommands();

        maps[1].processAllMapCreatingCommands(files[2]);

        return inputCommands;
    }

    public void askForInput(String filePath) throws Exception {
        boolean foundName = false;
        
        File file = new File(filePath);
        File[] files = file.listFiles();

        while (!foundName) {

            if (files.length == 0) throw new Exception(view.noTestToRun());

            view.chooseTest();

            for (File f : files) {
                System.out.println(f.getName());
            }

            testName = scanner.nextLine();

            // kulon fuggveny: all = minden tesztre

            for (File f : files) {
                String fileName = f.getName();
                if (fileName.equals(testName)) {
                    testName = fileName;
                    foundName = true;
                    break;
                }
            }

            if (!foundName) {
                System.out.println("Test case not found!");
            }
        }
    }

    public List<String> readMapCommands(String filePath, File[] files) {
        List<String> inputCommands = new ArrayList<>();
        filePath += "\\" + testName + "\\" + testName;

        files[1] = new File(filePath + "_map.txt");
        files[2] = new File(filePath + "_output.txt");

        try { inputCommands = Files.readAllLines(new File(filePath + "_input.txt").toPath()); }
        catch(IOException e) { e.printStackTrace(); }

        return inputCommands;
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
                String tmpInputCommands = makeFirstPartUpperCase(inputCommandParts);
                
                returnInputCommands.add(tmpInputCommands);
            }
        }
        return returnInputCommands;
    }

    public String makeFirstPartUpperCase(String[] inputCommandParts) {
        String tmpInputCommands = "";

        for (int i = 0; i < inputCommandParts.length; ++i) {
            String tmpCommand = inputCommandParts[i];
            tmpInputCommands += i == 0 ? tmpCommand.toUpperCase() : tmpCommand;
            if (i != inputCommandParts.length - 1) tmpInputCommands += " ";
        }

        return tmpInputCommands;
    }

    public void validateTestFiles(ArrayList<File> testTXTs, ArrayList<String>[] commands) {
        String filePath = workingDir + "\\Prototype\\src\\tests";
        File testFolder = new File(filePath + "\\testCases", testName);
        File map = null;

        if (saveToFile) {
            map = new File(filePath + "\\maps", inputMapName + ".txt");
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
            if (saveToFile) { Files.write(Paths.get(testTXTs.get(3).getPath()), commands[0]); }
        }

        catch (IOException e) {
            e.printStackTrace();
        }
    }

    public boolean runTest(boolean askForInput) {

        TectonMap[] maps = new TectonMap[2];
        List<String> inputCommands = null;
        boolean wasTestSuccessful = false;

        try {

            inputCommands = loadTestMaps(maps, askForInput);

            view.showInputMapBeforeTest(testName);
            maps[0].showMap();
        }

        catch(Exception e){
            view.getMessage(e);
            e.printStackTrace();
            return false;
        }

        try {

            view.runningInputCommands();
            maps[0].runAllInputCommands(inputCommands);
            wasTestSuccessful = maps[0].mapsAreEqual(maps[1]);
        }

        catch(Exception e) {
            view.getMessage(e);
            view.theGivenMapsAreNotEqual(maps[0], maps[1], testName);
            return false;
        }

        view.theGivenMapsAreEqual(maps[0], maps[1], testName);
        return wasTestSuccessful;
    }

    public void runAllTests() throws Exception{
        String filePath = workingDir + "\\Prototype\\src\\tests\\testCases";
        File file = new File(filePath);
        File[] testFiles = file.listFiles();

        SHOW_OUTPUT = false;

        for (File f : testFiles) {
            testName = f.getName();
            boolean testSuccess = runTest(false);
            view.printTestResultOnly(f.getName(), testSuccess);
            if (!testSuccess) return;
        }

        SHOW_OUTPUT = true;

    }
}