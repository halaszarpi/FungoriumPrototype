package fungorium;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * This class is responsible for controlling the game flow, managing players,
 * rounds, and the game map.
 * It orchestrates the initialization of the game, handles the rounds, and ends the game.
 */
public class GameController {
    private final Scanner scanner;
    private final TectonMap tectonMap;
    private List<Player> players;
    private int numberOfRounds;

    /**
     * Constructor for GameController. Initializes the scanner and the game map.
     * Loads the map from a file and creates the tecton map using the commands in the file.
     *
     * @param _scanner the scanner used for input
     */
    public GameController(Scanner _scanner) {
        this.scanner = _scanner;
        this.tectonMap = new TectonMap(scanner, false);
        String workingDir = System.getProperty("user.dir");
        File gameMap = new File(workingDir+ "\\Prototype\\src\\gamemaps\\startingMap.txt");

        try {
            this.tectonMap.processAllMapCreatingCommands(gameMap);
        }
        catch (Exception e) {
            System.out.println("Error while creating map: " + e.getMessage());
            return;
        }

        this.players = new ArrayList<>();
        initializeGame();
    }

    /**
     * Initializes the game by setting up the players, the number of rounds,
     * and the starting tectons for each player. Then it starts the game.
     */
    public void initializeGame() {
        System.out.println("New game started!");
        System.out.println("Please enter the number of players:");
        int numPlayers = scanner.nextInt();
        scanner.nextLine();

        for (int i = 0; i < numPlayers; i++) {
            if(i % 2 == 0){
                System.out.println("Player " + (i + 1) + " is a Fungus Farmer. Please choose a name:");
                String playerName = scanner.nextLine();
                players.add(new FungusFarmer(playerName));
            } else {
                System.out.println("Player " + (i + 1) + " is an Insect Keeper. Please choose a name:");
                String playerName = scanner.nextLine();
                players.add(new InsectKeeper(playerName));
            }
        }

        System.out.println("Please enter the number of rounds:");
        numberOfRounds = scanner.nextInt();
        scanner.nextLine();

        System.out.println("The game map is ready!");
        tectonMap.showMap();

        for (Player player : players) {
            boolean valid = false;
            while (!valid) {
                System.out.println("Player " + player.getName() + " is choosing a starting Tecton:");
                String tectonName = scanner.nextLine();

                Tecton startingTecton;
                try {
                    startingTecton = tectonMap.findTecton(tectonName);
                } catch (Exception e) {
                    System.out.println("Error while choosing starting Tecton: " + e.getMessage());
                    continue;
                }

                try{
                    player.initializePlayer(startingTecton);
                }
                catch (Exception e) {
                    System.out.println("Error while initializing player: " + e.getMessage());
                    continue;
                }

                valid = true;
            }
        }

        System.out.println("Game initialized!");
        runGame();
    }

    /**
     * Runs the game for the given number of rounds. In each round, each player takes their turn.
     * The game state is refreshed after each turn.
     */
    public void runGame() {
        tectonMap.showMap();

        for (int round = 0; round < numberOfRounds; round++) {
            for (Player player : players) {
                if (player.isInGame()) {
                    player.turn(tectonMap, scanner);
                    tectonMap.refreshMap();
                }
            }

            for (Player player : players) {
                player.roundPassed();
            }

            tectonMap.roundPassed(null);

            tectonMap.refreshMap();
            numberOfRounds--;
        }

        endGame();
    }

    /**
     * Ends the game by showing the final map and displaying the scores of all players.
     */
    public void endGame() {
        System.out.println("Game ended!");
        tectonMap.showMap();

        for (Player player : players) {
            System.out.println(player.getScore());
        }
    }
}