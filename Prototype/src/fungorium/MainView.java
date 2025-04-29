package fungorium;

public class MainView {
    public static void print_Logo() {
        final String GREEN = "\u001B[32m";
        final String RESET = "\u001B[0m";
    
        System.out.println(GREEN +
            "---------------------------------------------------------------\n" +
            "|  #####  #   #  #   #  #####  #####  #####  #  #   #  #   #  |\n" +
            "|  #      #   #  ##  #  #      #   #  #   #  #  #   #  ## ##  |\n" +
            "|  ####   #   #  # # #  #  ##  #   #  ###    #  #   #  # # #  |\n" +
            "|  #      #   #  #  ##  #   #  #   #  #  ##  #  #   #  #   #  |\n" +
            "|  #      #####  #   #  #####  #####  #   #  #  #####  #   #  |\n" +
            "---------------------------------------------------------------" + RESET + "\n"
        );
    }
    
    public static void print_mainMenu() {
        clearScreen();
        print_Logo();
        System.out.println("Main Menu:");
        System.out.println("1 - New Game");
        System.out.println("2 - Game Tester");
        System.out.println("E - Exit");
    } 

    public static void clearScreen() {
        System.out.print("\033[H\033[2J");
        System.out.flush();
    }

    public void Goodbye(){
        System.out.println("Goodbye!"); 
    }

    public void InvalidInput(){
        System.out.println("Invalid input!");
    }

}
