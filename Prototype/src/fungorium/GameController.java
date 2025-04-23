package fungorium;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class GameController {
    private final Scanner scanner;
    private final TectonMap tectonMap;
    private final List<Player> players;
    private int numberOfRounds;

    private final TectonMapView gameView;

    public GameController(Scanner _scanner) {
        this.scanner = _scanner;
        this.gameView = new TectonMapView(_scanner);

        /// TODO
        //Load map from file "map.txt"
        //For now:
        this.tectonMap = TectonMap("map.txt", scanner);

        this.players = new ArrayList<Player>();

        startGame();
    }

    public void startGame() {
        System.out.println("New game started!");
        System.out.println("Please enter the number of players:");
        int numPlayers = scanner.nextInt();
        scanner.nextLine(); // Consume newline

        for (int i = 0; i < numPlayers; i++) {
            if(i%2 == 0){
                System.out.println("Player " + (i + 1) + " is a Fungus Farmer. Please choose a name:");
                String playerName = scanner.nextLine();
                players.add(new FungusFarmer(playerName));
            } else {
                System.out.println("Player " + (i + 1) + " is a Insect Keeper. Please choose a name:");
                String playerName = scanner.nextLine();
                players.add(new InsectKeeper(playerName));
            }
        }
        System.out.println("Game initialized!");

        System.out.println("Please enter the number of rounds:");
        numberOfRounds = scanner.nextInt();
        scanner.nextLine();

        runGame();
    }

    public void runGame() {
        tectonMap.showMap();

        for (int round = 0; round < numberOfRounds; round++) {
            for (Player player : players) {
                if (player.isInGame()) {
                    player.step(tectonMap.map());
                    tectonMap.refreshMap();
                }
            }

            for (Player player : players) {
                player.roundPassed();
            }

            for (Tecton tecton : tectonMap.map()) {
                tecton.roundPassed();
            }

            tectonMap.refreshMap();
            numberOfRounds--;
        }

        endGame();
    }

    public void endGame() {
        System.out.println("Game ended!");
        tectonMap.showMap();

        for (Player player : players) {
            System.out.println(player.getScore());
        }
    }
}
