package fungorium;

import java.util.ArrayList;
import java.util.Scanner;

public class GameController {
    private Scanner scanner;
    private List<Tecton> map;
    private List<Player> players;
    private int numberOfRounds;

    private GameView gameView;

    public GameController(Scanner _scanner) {
        this.scanner = _scanner;
        this.gameView = _gameView;

        /// TODO
        //Load map from file "map.txt"
        //For now:
        this.map = new ArrayList<Tecton>();

        this.players = new ArrayList<Player>();
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
                players.add(new FungusFarmer(playerName, map));
            } else {
                System.out.println("Player " + (i + 1) + " is a Insect Keeper. Please choose a name:");
                String playerName = scanner.nextLine();
                players.add(new InsectKeeper(playerName, map));
            }
        }
        System.out.println("Game initialized!");

        System.out.println("Please enter the number of rounds:");
        numberOfRounds = scanner.nextInt();
        scanner.nextLine(); // Consume newline
    }

    public void runGame() {
        gameView.showMap(map);

        for (int round = 0; round < numberOfRounds; round++) {
            for (Player player : players) {
                if (player.isInGame()) {
                    player.step();
                    gameView.refreshMap(map);
                }
            }

            for (Player player : players) {
                player.roundPassed();
            }

            for (Tecton tecton : map) {
                tecton.roundPassed();
            }

            gameView.refreshMap(map);
            numberOfRounds--;
        }
    }

    public void endGame() {
        System.out.println("Game ended!");
        gameView.showMap(map);

        for (Player player : players) {
            System.out.println(player.getScore());
        }
    }
}
