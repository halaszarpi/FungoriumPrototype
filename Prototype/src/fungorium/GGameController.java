package fungorium;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class GGameController extends JFrame {
    private final TectonMap tectonMap;
    private List<Player> players = new ArrayList<>();
    private int numberOfRounds;
    private Scanner scanner = new Scanner(System.in);
    private JPanel gridPanel;

    public GGameController() {
        setTitle("Fungorium - Game");
        setSize(1080, 720);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        tectonMap = new TectonMap(null, false);
        File gameMap = new File(System.getProperty("user.dir") + "\\Prototype\\src\\gamemaps\\startingMap.txt");

        try {
            tectonMap.processAllMapCreatingCommands(gameMap);
        } catch (Exception e) {
            showError("Error creating map:\n" + e.getMessage());
            return;
        }

        setVisible(true);

        initializeGame();
    }

    private void initializeGame() {
        showInfo("Game is starting!");
        String input;

        // Get number of players
        boolean validnum = false;
        int numPlayers = 0;
        while(!validnum) {
            input = JOptionPane.showInputDialog(this, "Enter number of players:");
            numPlayers = Integer.parseInt(input);
            if (2 <= numPlayers && numPlayers <= 16) {
                validnum = true;
            }
            else{
                showError("Number of players must be between 2 and 16.");
            }
        }

        for (int i = 0; i < numPlayers; i++) {
            String role = (i % 2 == 0) ? "Fungus Farmer" : "Insect Keeper";
            String playerName = JOptionPane.showInputDialog(this,
                    "Enter name for player " + (i + 1) + " (" + role + "):");

            if (i % 2 == 0) {
                players.add(new FungusFarmer(playerName));
            } else {
                players.add(new InsectKeeper(playerName));
            }
        }

        input = JOptionPane.showInputDialog(this, "Enter number of rounds:");
        numberOfRounds = Integer.parseInt(input);

        // Starting tecton selection
        for (Player player : players) {
            boolean validtecton = false;

            List<String> tectonNames = new ArrayList<>();
            for (Tecton tecton : tectonMap.getTectons()) {
                tectonNames.add(tecton.getName());
            }

            while (!validtecton) {
                String tectonChoice = (String) JOptionPane.showInputDialog(
                        this,
                        player.getName() + ", choose a starting tecton:",
                        "Choose Tecton",
                        JOptionPane.PLAIN_MESSAGE,
                        null,
                        tectonNames.toArray(),
                        tectonNames.getFirst()
                );

                try {
                    Tecton startingTecton = tectonMap.findTecton(tectonChoice);
                    player.initializePlayer(startingTecton);
                    validtecton = true;
                } catch (Exception e) {
                    showError("Error initializing player:\n" + e.getMessage());
                }
            }
        }

        showInfo("Game initialized successfully!");
        runGame();

        gridPanel = new JPanel(new GridLayout(3, 1));
        
        // A legfelso panel, amin a jatek latszik
        //gridPanel.add(new GMap(this.tectonMap));

        // A kozepso panel, amin az aktualis jatekos adatai latszanak
        //gridPanel.add(new GPlayerInfo());

        // Az also panel, amin a commandokat lehet kiadni
        //gridPanel.add(new GPlayerCommand());

        gridPanel.setBackground(Color.BLUE);
        gridPanel.setOpaque(true);
        gridPanel.setVisible(true);

        add(gridPanel);

    }

    private void runGame() {
        for (int round = 0; round < numberOfRounds; round++) {
            for (Player player : players) {
                if (player.isInGame()) {
                    player.turn(tectonMap,scanner);
                    tectonMap.refreshMap();
                    tectonMap.showMap();
                }
            }

            for (Player player : players) {
                player.roundPassed();
            }

            tectonMap.roundPassed(null);
            tectonMap.refreshMap();
        }

        endGame();
    }

    private void endGame() {
        showInfo("Game Over!");
        tectonMap.showMap();

        StringBuilder scoreMessage = new StringBuilder("Final Scores:\n");
        for (Player player : players) {
            scoreMessage.append(player.getName()).append(": ").append(player.getScore()).append("\n");
        }

        JOptionPane.showMessageDialog(this, scoreMessage.toString(), "Game Over", JOptionPane.INFORMATION_MESSAGE);


        dispose();
        SwingUtilities.invokeLater(GMainMenu::new);
    }


    private void showError(String message) {
        JOptionPane.showMessageDialog(this, message, "Error", JOptionPane.ERROR_MESSAGE);
    }

    private void showInfo(String message) {
        JOptionPane.showMessageDialog(this, message, "Info", JOptionPane.INFORMATION_MESSAGE);
    }
}
