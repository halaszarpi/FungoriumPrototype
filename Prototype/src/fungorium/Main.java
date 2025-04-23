package fungorium;

import java.util.Scanner;

public class Main {
    public static Scanner scanner;

    public static void main(String[] args) {

        scanner = new Scanner(System.in);

        GameTesterController gtc = new GameTesterController(scanner);
        gtc.printMenuAndWaitForOption();

        scanner.close();
    }

}