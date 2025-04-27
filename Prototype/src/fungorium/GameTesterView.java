package fungorium;

/**
 * This class provides the view for the GameTester. It contains methods that display various messages,
 * prompts, and feedback to the user during the test creation and test execution process.
 */
public class GameTesterView {

    /**
     * Returns a message when there are no tests available to run.
     *
     * @return a message indicating no tests are available
     */
    public String noTestToRun() {
        return "There are no tests to run!";
    }

    /**
     * Returns a message when there are no maps available to load.
     *
     * @return a message indicating no maps are available
     */
    public String noMapsToLoad() {
        return "There are no maps to load!";
    }

    /**
     * Displays the main menu options for the GameTester.
     */
    public void listGameTesterOptions() {
        System.out.println("\n1. CREATE TEST");
        System.out.println("2. RUN TEST");
        System.out.println("3. BACK TO MAIN MENU");
    }

    /**
     * Prompts the user to enter a name for the test.
     */
    public void enterTestName() {
        System.out.println("Please enter a test name: ");
    }

    /**
     * Displays the options for map handling in the test creation process.
     */
    public void listMapOptions() {
        System.out.println("1. NEW MAP");
        System.out.println("2. LOAD MAP");
    }

    /**
     * Prompts the user to enter the name of a map they want to load.
     */
    public void loadMapMessage() {
        System.out.println("Please enter a map name that you want to load: ");
    }

    /**
     * Displays a message after a map is successfully loaded.
     *
     * @param mapName the name of the loaded map
     */
    public void mapLoadedMessage(String mapName) {
        System.out.println("Loaded map: " + mapName);
        System.out.println("(You can modify this map or save it as it is for the test!)");
    }

    /**
     * Prompts the user to enter map-building commands using a specified language.
     */
    public void enterMapBuildingCommands() {
        System.out.println("Please enter the map building commands using the map building language:");
    }

    /**
     * Prompts the user to enter input commands using a specified language.
     */
    public void enterInputCommands() {
        System.out.println("Please enter the input commands using the input language:");
    }

    /**
     * Prompts the user to enter output commands using a specified language.
     */
    public void enterOutputCommands() {
        System.out.println("Please enter the output commands using the output language:");
    }

    /**
     * Displays a message indicating that all files required for the test have been created.
     */
    public void testCreatedMessage() {
        System.out.println("All files created for the test.");
        System.out.println("You can run the test in the RUN TEST menu!");
    }

    /**
     * Prompts the user to choose the test they want to run.
     */
    public void chooseTest() {
        System.out.println("Please enter the test name you want to run: ");
    }

    /**
     * Displays the input map before the test begins.
     *
     * @param testMapName the name of the test map
     */
    public void showInputMapBeforeTest(String testMapName) {
        System.out.println("\nMap: " + testMapName + "\nBefore test:");
    }

    /**
     * Displays a notification after the test has completed, indicating that the map has not changed.
     */
    public void afterTestNotification() {
        System.out.println("(This map did not change ultimately, you can rerun the test and get the same result.)");
    }

    /**
     * Displays a message indicating invalid input.
     */
    public void invalidInputMessage() {
        System.out.println("Invalid input!");
    }

    /**
     * Displays the comparison between the input and output maps, indicating that they are equal.
     *
     * @param inputMap the input map
     * @param outputMap the output map
     * @param testName the name of the test
     */
    public void theGivenMapsAreEqual(TectonMap inputMap, TectonMap outputMap, String testName) {
        System.out.println("\nThe input and output maps are equal: ");
        printMaps(inputMap, outputMap, testName);
        System.out.println("\nTest successful!\n");
    }

    /**
     * Displays the comparison between the input and output maps, indicating that they are not equal.
     *
     * @param inputMap the input map
     * @param outputMap the output map
     * @param testName the name of the test
     */
    public void theGivenMapsAreNotEqual(TectonMap inputMap, TectonMap outputMap, String testName) {
        System.out.println("\nThe input and output maps are not equal: ");
        printMaps(inputMap, outputMap, testName);
        System.out.println("\nTest unsuccessful!\n");
    }

    /**
     * Prints the details of the input and output maps for comparison.
     *
     * @param inputMap the input map
     * @param outputMap the output map
     * @param testName the name of the test
     */
    public void printMaps(TectonMap inputMap, TectonMap outputMap, String testName) {
        System.out.println("\nInput map (" + testName + "):");
        inputMap.showMap();
        System.out.println("Output map (" + testName + "):");
        outputMap.showMap();
    }

    /**
     * Displays the message from an exception.
     *
     * @param e the exception to display
     */
    public void getMessage(Exception e) {
        System.out.println(e.getMessage());
    }

    /**
     * Indicates that input commands are being executed.
     */
    public void runningInputCommands() {
        System.out.println("Running input commands:\n");
    }

    /**
     * Indicates the process of building maps based on user inputs.
     */
    public void tryingToBuildMaps() {
        System.out.println("\nIf all the inputs and logics were correct the map is built without getting exceptions:\n");
    }

    /**
     * Displays the message for input map commands.
     */
    public void inputMapCommands() {
        System.out.println("Input map commands:\n");
    }

    /**
     * Displays the message for output map commands.
     */
    public void outputMapCommands() {
        System.out.println("\nOutput map commands:\n");
    }
}