package fungorium;

import java.awt.*;
import java.io.File;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import javax.swing.*;
import java.util.Set;
import java.util.concurrent.CountDownLatch;
import java.util.stream.Collectors;


public class GGameController extends JFrame {
    private final TectonMap tectonMap;
    private List<GPlayer> players = new ArrayList<>();
    private int numberOfRounds;
    private JPanel GamePanel;
    private JPanel playerPanel;
    private JPanel mapPanel;
    private final GMap gmap;
    JComboBox<String> TectonChooser;

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

        //The game panel itself Consists of two parts: the mapPanel and the playerPanel
        GamePanel = new JPanel();
        GamePanel.setLayout(new BorderLayout());

        //mapPanel
        mapPanel = createMapPanel();
        GamePanel.add(mapPanel, BorderLayout.CENTER);

        //playerPanel
        playerPanel = new JPanel();
        playerPanel.setLayout(new BorderLayout());
        GamePanel.add(playerPanel, BorderLayout.SOUTH);

        add(GamePanel);
        setVisible(true);

        // Start game logic in a new thread
        new Thread(this::initializeGame).start();
    }

    private JPanel createMapPanel() {
        JPanel mapPanel = new JPanel();
        mapPanel.setLayout(new BorderLayout());

        // Create tecton selection combo box
        List<String> tectonNames = new ArrayList<>();
        for (Tecton tecton : tectonMap.getTectons()) {
            tectonNames.add(tecton.getName());
        }
        TectonChooser = new JComboBox<>(tectonNames.toArray(new String[0]));
        TectonChooser.setSelectedIndex(0);


        // Add components to mapPanel
        mapPanel.add(TectonChooser, BorderLayout.NORTH);
        mapPanel.add(gmap, BorderLayout.CENTER);

        // Initial map draw
        String middleTecton = (String) TectonChooser.getSelectedItem();
        gmap.drawMap(gmap.findGTectonByName(middleTecton));

        // Updating map on tecton selection change
        TectonChooser.addActionListener(e -> {
            String selectedTecton = (String) TectonChooser.getSelectedItem();
            if (selectedTecton != null) {
                gmap.drawMap(gmap.findGTectonByName(selectedTecton));
            }
        });

        return mapPanel;
    }

    private void updateTectonChooser() {
        // Get current items in the combo box
        Set<String> currentItems = new HashSet<>();
        for (int i = 0; i < TectonChooser.getItemCount(); i++) {
            currentItems.add(TectonChooser.getItemAt(i));
        }

        // Get current tecton names from the tectonMap
        Set<String> tectonNames = tectonMap.getTectons().stream()
                .map(Tecton::getName)
                .collect(Collectors.toSet());

        // Remove items not in the tectonMap
        for (String item : new HashSet<>(currentItems)) {
            if (!tectonNames.contains(item)) {
                TectonChooser.removeItem(item);
            }
        }

        // Add items from tectonMap that are not in the combo box
        for (String name : tectonNames) {
            if (!currentItems.contains(name)) {
                TectonChooser.addItem(name);
            }
        }
    }

    private void initializeGame() {
        showInfo("Game is starting!");
        String input;

        // Get number of players (between 2 and 16)
        int numPlayers = 0;
        while (true) {
            input = JOptionPane.showInputDialog(this, "Enter number of players (2–16):");
            try {
                numPlayers = Integer.parseInt(input);
                if (numPlayers >= 2 && numPlayers <= 16) {
                    break;
                } else {
                    showError("Number of players must be between 2 and 16.");
                }
            } catch (NumberFormatException e) {
                showError("Please enter a valid integer.");
            }
        }

        // Get names of players (non-null, unique)
        Set<String> usedNames = new HashSet<>();
        for (int i = 0; i < numPlayers; i++) {
            String role = (i % 2 == 0) ? "Fungus Farmer" : "Insect Keeper";
            String playerName;

            while (true) {
                playerName = JOptionPane.showInputDialog(this,
                        "Enter name for player " + (i + 1) + " (" + role + "):");

                if (playerName == null || playerName.trim().isEmpty()) {
                    showError("Name cannot be empty.");
                } else if (usedNames.contains(playerName)) {
                    showError("This name is already taken. Choose another.");
                } else {
                    usedNames.add(playerName);
                    break;
                }
            }

            if (i % 2 == 0) {
                players.add(new GPlayer(new FungusFarmer(playerName)));
            } else {
                players.add(new GPlayer(new InsectKeeper(playerName)));
            }
        }

        // Get number of rounds (between 0 and 100)
        while (true) {
            input = JOptionPane.showInputDialog(this, "Enter number of rounds (0–100):");
            try {
                numberOfRounds = Integer.parseInt(input);
                if (numberOfRounds >= 0 && numberOfRounds <= 100) {
                    break;
                } else {
                    showError("Number of rounds must be between 0 and 100.");
                }
            } catch (NumberFormatException e) {
                showError("Please enter a valid integer.");
            }
        }

        // Starting tecton selection
        for (GPlayer player : players) {
            boolean validTecton = false;

            List<String> tectonNames = new ArrayList<>();
            for (Tecton tecton : tectonMap.getTectons()) {
                tectonNames.add(tecton.getName());
            }

            while (!validTecton) {
                String tectonChoice = (String) JOptionPane.showInputDialog(
                        this,
                        player.getPlayer().getName() + ", choose a starting tecton:",
                        "Choose Tecton",
                        JOptionPane.PLAIN_MESSAGE,
                        null,
                        tectonNames.toArray(),
                        tectonNames.getFirst()
                );

                try {
                    Tecton startingTecton = tectonMap.findTecton(tectonChoice);
                    player.getPlayer().initializePlayer(startingTecton);
                    validTecton = true;
                } catch (Exception e) {
                    showError("Error initializing player:\n" + e.getMessage());
                }
            }
        }

        showInfo("Game initialized successfully!");
        runGame();
    }

    private void runGame() {
        for (int round = 0; round < numberOfRounds; round++) {
            updateTectonChooser();

            for (GPlayer player : players) {
                CountDownLatch latch = new CountDownLatch(1);


                player.turn(playerPanel, gmap);

                SwingUtilities.invokeLater(() -> {
                    JButton endTurnButton = new JButton("End Turn");
                    endTurnButton.addActionListener(e -> latch.countDown());

                    playerPanel.add(endTurnButton, BorderLayout.NORTH);
                    playerPanel.revalidate();
                    playerPanel.repaint();
                });

                // Wait for the player to press "End Turn"
                try {
                    latch.await();
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    return;
                }
            }

            // Round passed for each player
            for (GPlayer player : players) {
                player.getPlayer().roundPassed();
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
        for (GPlayer player : players) {
            scoreMessage.append(player.getPlayer().getName()).append(": ").append(player.getPlayer().getScore()).append("\n");
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
