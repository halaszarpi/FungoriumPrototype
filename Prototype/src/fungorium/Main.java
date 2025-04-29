package fungorium;

import java.util.Scanner;
import javax.swing.text.View;

public class Main {
    public static Scanner scanner;
    public static MainView view = new MainView();

    public static void main(String[] args) {

        scanner = new Scanner(System.in);
        boolean exit = false;
        String input;

        while(!exit){
            view.print_mainMenu();

            input = scanner.nextLine().toUpperCase();

            switch (input) {
                case "1" -> newGame(scanner);
                case "2" -> gameTester(scanner);
                case "E" -> {view.Goodbye();exit = true;}
                default -> view.InvalidInput();
            }
        }
        scanner.close();
    }

    private static void newGame(Scanner scanner) {
        GameController gamecontroller = new GameController(scanner);
    }

    private static void gameTester(Scanner scanner) {
        GameTesterController testercontroller = new GameTesterController(scanner);
    }



    private static void print_mainMenu() {
        view.clearScreen();
        view.print_Logo();
        System.out.println("Main Menu:");
        System.out.println("1 - New Game");
        System.out.println("2 - Game Tester");
        System.out.println("E - Exit");
    }
}