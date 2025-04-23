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
        int option = scanner.nextInt();
        switch(option) {
            case 1 -> createTest();
            case 2 -> runTest();
            case 3 -> {return;}
            default -> System.out.println("Invalid option");
        }
    }

    public void createTest() {
        view.enterTestName();
        String testName = scanner.nextLine();
        File filePath = new File(System.getProperty("user.dir") + "\\Prototype\\src\\tests");
        File testFile = new File(filePath, testName + ".txt");
    }

    public void runTest() {}
}