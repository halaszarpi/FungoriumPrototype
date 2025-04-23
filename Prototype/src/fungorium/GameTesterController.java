package fungorium;

import java.io.File;
import java.util.Scanner;

public class GameTesterController {

    private Scanner scanner;
    private GameTesterView view;

    public GameTesterController(Scanner scanner) {
        this.scanner = scanner;
        view = new GameTesterView();
    }

    public void printMenuAndWaitForOption() {
        view.listGameTesterOptions();
        String option = scanner.nextLine();
        switch(option) {
            case "1" -> createTest();
            case "2" -> runTest();
            case "3" -> {return;}
            default -> System.out.println("Invalid option");
        }
    }

    public void createTest() {
        view.enterTestName();
        String testName = scanner.nextLine();
        File filePath = new File(System.getProperty("user.dir") + "\\Prototype\\src\\tests");
        System.out.println(filePath);
        File testFile = new File(filePath, testName);
        testFile.mkdirs();
    }

    public void runTest() {}
}