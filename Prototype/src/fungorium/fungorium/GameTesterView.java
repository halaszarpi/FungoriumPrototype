package fungorium;

public class GameTesterView {

    public void listGameTesterOptions() {
        System.out.println("1. CREATE TEST");
        System.out.println("2. RUN TEST");
        System.out.println("3. BACK TO MAIN MENU");
    }

    // CREATE es RUN eseten
    public void enterTestName() {
        System.out.println("Please enter a test name: ");
    }

    public void listMapOptions() {
        System.out.println("1. NEW MAP");
        System.out.println("2. LOAD MAP");
    }

    public void loadMapMessage() {
        System.out.println("Please enter a map name that you want to load: ");
    }

    public void mapLoadedMessage(String mapName) {
        System.out.println("Loaded map: " + mapName);
        System.out.println("(You can modify this map or save it as it is for the test!)");
    }

    public void enterMapBuildingCommands() {
        System.out.println("Please enter the map building commands using the map building language:");
    }

    public void enterInputCommands() {
        System.out.println("Please enter the input commands using the input language:");
    }

    public void enterOutputCommands() {
        System.out.println("Please enter the output commands using the output language:");
    }

    public void testCreatedMessage() {
        System.out.println("All files created for the test.");
        System.out.println("You can run the test in the RUN TEST menu!");
    }

    public void chooseTest() {
        System.out.println("Plese enter the test name you want to run: ");
    }

    public void showInputMapBeforeTest(String testMapName) {
        System.out.println("\nMap: " + testMapName + "\nBefore test:");
    }

    // Ez a teszt legvegen kiirodik
    public void afterTestNotification() {
        System.out.println("(This map did not change ultimately, you can rerun the test and get the same result.)");
    }

    public void invalidInputMessage() { System.out.println("Invalid input!"); }

    public void theGivenMapsAreEqual(TectonMap inputMap, TectonMap outputMap, String testName) { 
        System.out.println("\nThe input and output maps are equal: "); 
        printMaps(inputMap, outputMap, testName);
        System.out.println("\nTest successful!\n"); 
    }

    public void theGivenMapsAreNotEqual(TectonMap inputMap, TectonMap outputMap, String testName) { 
        System.out.println("\nThe input and output maps are not equal: "); 
        printMaps(inputMap, outputMap, testName);
        System.out.println("\nTest unsuccessful!\n"); 
    }

    public void printMaps(TectonMap inputMap, TectonMap outputMap, String testName) {
        System.out.println("\nInput map (" + testName + "):");
        inputMap.showMap();
        System.out.println("Output map (" + testName + "):");
        outputMap.showMap();
    }

    public void getMessage(Exception e) { System.out.println(e.getMessage()); }

    public void runnungInputCommands() { System.out.println("Running input commands:\n"); }

    public void tryingToBuildMaps() { System.out.println("\nIf all the inputs and logics were correct the map is built without getting exceptions:\n"); }

}