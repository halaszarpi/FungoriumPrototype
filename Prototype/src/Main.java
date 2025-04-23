import java.util.Scanner;

public class Main {
    public static Scanner scanner;

    public static void main(String[] args) {

        scanner = new Scanner(System.in);
        boolean exit = false;
        String input;

        while(!exit){
            print_mainMenu();

            input = scanner.nextLine().toUpperCase();

            switch (input) {
                case "1" -> newGame(scanner);
                case "2" -> gameTester(scanner);
                case "E" -> {System.out.println("Goodbye!"); exit = true;}
                default -> System.out.println("Invalid input!");
            }
        }
        scanner.close();
    }

    private static void newGame(Scanner scanner) {
        GameController gamecontroller = new GameController(scanner);
    }

    /// TODO
    private static void gameTester(Scanner scanner) {
       TesterController testercontroller = new TesterController(scanner);
    }

    private static void clearScreen() {
        System.out.print("\033[H\033[2J");
        System.out.flush();
    }

    private static void print_Logo(){
        System.out.println("---------------------------------------------------------------");
        System.out.println("|  #####  #   #  #   #  #####  #####  #####  #  #   #  #   #  |");
        System.out.println("|  #      #   #  ##  #  #      #   #  #   #  #  #   #  ## ##  |");
        System.out.println("|  ####   #   #  # # #  #  ##  #   #  ###    #  #   #  # # #  |");
        System.out.println("|  #      #   #  #  ##  #   #  #   #  #  ##  #  #   #  #   #  |");
        System.out.println("|  #      #####  #   #  #####  #####  #   #  #  #####  #   #  |");
        System.out.println("---------------------------------------------------------------");
        System.out.println("\n");
    }

    private static void print_mainMenu() {
        clearScreen();
        print_Logo();
        System.out.println("Main Menu:");
        System.out.println("1 - New Game");
        System.out.println("2 - Game Tester");
        System.out.println("E - Exit");
    }

    private static void print_testMenu(){
        clearScreen();
        System.out.println("Game Tester Menu:");
        System.out.println("1 - Create Test");
        System.out.println("2 - Run Test");
        System.out.println("E - Back to Main Menu");
    }
}