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
    private GameControllerView view;

    /**
     * Constructor for GameController. Initializes the scanner and the game map.
     * Loads the map from a file and creates the tecton map using the commands in the file.
     *
     * @param _scanner the scanner used for input
     */
    public GameController(Scanner _scanner) {
        this.scanner = _scanner;
        this.tectonMap = new TectonMap(scanner, false);
        this.view = new GameControllerView();
        String workingDir = System.getProperty("user.dir");
        File gameMap = new File(workingDir+ "\\Prototype\\src\\gamemaps\\startingMap.txt");

        try {
            this.tectonMap.processAllMapCreatingCommands(gameMap);
        }
        catch (Exception e) {
            view.ErrorCreatingMap(e);
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
        view.Startedprint();
        int numPlayers = scanner.nextInt();
        scanner.nextLine();

        for (int i = 0; i < numPlayers; i++) {
            if(i % 2 == 0){
                view.PlayerisFungusFarmer(i);
                String playerName = scanner.nextLine();
                players.add(new FungusFarmer(playerName));
            } else {
                view.PlayerisInsectKeeper(i);
                String playerName = scanner.nextLine();
                players.add(new InsectKeeper(playerName));
            }
        }

        view.NumberofRounds();
        numberOfRounds = scanner.nextInt();
        scanner.nextLine();

        view.MapisReady();
        tectonMap.showMap();

        for (Player player : players) {
            boolean valid = false;
            while (!valid) {
                view.ChoosingstartTecton(player.getName());
                String tectonName = scanner.nextLine();

                Tecton startingTecton;
                try {
                    startingTecton = tectonMap.findTecton(tectonName);
                } catch (Exception e) {
                    view.ErrorChoosingstartTecton(e);
                    continue;
                }

                try{
                    player.initializePlayer(startingTecton);
                }
                catch (Exception e) {
                    view.ErrorInitializePlayer(e);
                    continue;
                }

                valid = true;
            }
        }

        view.Initializesuccessfull();
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
        view.EndGame();
        tectonMap.showMap();

        for (Player player : players) {
            System.out.println(player.getScore());
        }
    }
}