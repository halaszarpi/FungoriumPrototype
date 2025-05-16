package fungorium;

import java.awt.*;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import javax.swing.*;

public class GGameController extends JFrame {
    private final TectonMap tectonMap;
    private List<Player> players = new ArrayList<>();
    private int numberOfRounds;
    private JPanel GamePanel;
    private final GMap gmap; // Make gmap a field so we can access it in the listener

    public GGameController() {
        setTitle("Fungorium - Game");
        setSize(1080, 720);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        //Loading Map
        tectonMap = new TectonMap(null, false);
        File gameMap = new File(System.getProperty("user.dir") + "\\Prototype\\src\\gamemaps\\startingMap.txt");
        gmap = new GMap(this.tectonMap); // initialize GMap field

        try {
            tectonMap.processAllMapCreatingCommands(gameMap);
        } catch (Exception e) {
            showError("Error creating map:\n" + e.getMessage());
            return;
        }

        GamePanel = new JPanel();
        GamePanel.setLayout(new GridLayout(2, 1));

        JPanel MapPanel = new JPanel();
        MapPanel.setLayout(new BorderLayout()); // Use BorderLayout for flexible sizing

        // Create tecton selection combo box
        List<String> tectonNames = new ArrayList<>();
        for (Tecton tecton : tectonMap.getTectons()) {
            tectonNames.add(tecton.getName());
        }

        JComboBox<String> TectonChooser = new JComboBox<>(tectonNames.toArray(new String[0]));
        TectonChooser.setSelectedIndex(0); // Select first by default
        TectonChooser.setPreferredSize(new Dimension(150, 30));

        // Panel for controls (top of map panel)
        JPanel controlPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        controlPanel.add(new JLabel("Select Tecton:"));
        controlPanel.add(TectonChooser);

        // Add components to MapPanel
        MapPanel.add(controlPanel, BorderLayout.NORTH);
        MapPanel.add(gmap, BorderLayout.CENTER);
        GamePanel.add(MapPanel);

        // Initial map draw
        String middleTecton = (String) TectonChooser.getSelectedItem();
        gmap.drawMap(gmap.findGTectonByName(middleTecton));

        // Add listener to update map on tecton selection change
        TectonChooser.addActionListener(e -> {
            String selectedTecton = (String) TectonChooser.getSelectedItem();
            if (selectedTecton != null) {
                gmap.drawMap(gmap.findGTectonByName(selectedTecton));
                gmap.repaint();
            }
        });

        // Middle panel: player info and command input
        JPanel playerPanel = new JPanel();
        playerPanel.setLayout(new GridLayout(2, 1));
        playerPanel.setSize(1080, 720);

        playerPanel.add(new GPlayerInfo());
        playerPanel.add(new GPlayerCommand());

        GamePanel.add(playerPanel);
        add(GamePanel);
        setVisible(true);

        // Start game logic in a new thread
        new Thread(this::initializeGame).start();
    }

    private void initializeGame() {
        showInfo("Game is starting!");
        String input;

        // Get number of players
        boolean validnum = false;
        int numPlayers = 0;
        while(!validnum) {
            input = JOptionPane.showInputDialog(this, "Enter number of players:");
            try {
                numPlayers = Integer.parseInt(input);
                if (2 <= numPlayers && numPlayers <= 16) {
                    validnum = true;
                } else {
                    showError("Number of players must be between 2 and 16.");
                }
            } catch (NumberFormatException e) {
                showError("Please enter a valid number.");
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
    }

    private void runGame() {
        GPlayerInfo info = new GPlayerInfo();
        for (int round = 0; round < numberOfRounds; round++) {
            for (Player player : players) {
                if (player.isInGame()) {
                    info.updatePersonInfo(player);
                    player.turn(tectonMap, new Scanner(System.in));
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
