package fungorium;

import java.awt.*;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import javax.swing.*;
import java.util.concurrent.CountDownLatch;


public class GGameController extends JFrame {
    private final TectonMap tectonMap;
    private List<GPlayer> players = new ArrayList<>();
    private int numberOfRounds;
    private JPanel GamePanel;
    private JPanel playerPanel;
    private JPanel mapPanel;
    private final GMap gmap; // Make gmap a field so we can access it in the listener
    private GPlayer currentPlayer = null;

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
        GamePanel.setLayout(new GridLayout(2, 1));

        //mapPanel
        mapPanel = createMapPanel();
        GamePanel.add(mapPanel);

        //playerPanel
        playerPanel = new JPanel();
        playerPanel.setSize(1080,360);
        GamePanel.add(playerPanel);

        add(GamePanel);
        setVisible(true);

        // Start game logic in a new thread
        new Thread(this::initializeGame).start();
    }

    private JPanel createMapPanel() {
        JPanel mapPanel = new JPanel();
        mapPanel.setLayout(new BorderLayout()); // Use BorderLayout for flexible sizing

        // Create tecton selection combo box
        List<String> tectonNames = new ArrayList<>();
        for (Tecton tecton : tectonMap.getTectons()) {
            tectonNames.add(tecton.getName());
        }

        JComboBox<String> TectonChooser = new JComboBox<>(tectonNames.toArray(new String[0]));
        TectonChooser.setSelectedIndex(0);

        // Panel for controls (top of map panel)
        JPanel chooserPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        chooserPanel.add(new JLabel("Select Tecton:"));
        chooserPanel.add(TectonChooser);

        // Add components to mapPanel
        mapPanel.add(chooserPanel, BorderLayout.NORTH);
        mapPanel.add(gmap, BorderLayout.CENTER);

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

        return mapPanel;
    }

    private void initializeGame() {
        showInfo("Game is starting!");
        String input;

        // Get the number of players
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

        //Get name and role of players
        for (int i = 0; i < numPlayers; i++) {
            String role = (i % 2 == 0) ? "Fungus Farmer" : "Insect Keeper";
            String playerName = JOptionPane.showInputDialog(this,
                    "Enter name for player " + (i + 1) + " (" + role + "):");

            if (i % 2 == 0) {
                players.add(new GPlayer(new FungusFarmer(playerName)));
            } else {
                players.add(new GPlayer(new InsectKeeper(playerName)));
            }
        }

        // Get number of rounds
        input = JOptionPane.showInputDialog(this, "Enter number of rounds:");
        numberOfRounds = Integer.parseInt(input);

        // Starting tecton selection
        for (GPlayer player : players) {
            boolean validtecton = false;

            List<String> tectonNames = new ArrayList<>();
            for (Tecton tecton : tectonMap.getTectons()) {
                tectonNames.add(tecton.getName());
            }

            while (!validtecton) {
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
        for (int round = 0; round < numberOfRounds; round++) {
            for (GPlayer player : players) {
                CountDownLatch latch = new CountDownLatch(1);

                // Setup the player's panel
                player.turn(playerPanel, gmap);

                // Add "End Turn" button after player UI is set up
                SwingUtilities.invokeLater(() -> {
                    JButton endTurnButton = new JButton("End Turn");
                    endTurnButton.addActionListener(e -> latch.countDown());

                    JPanel buttonPanel = new JPanel();
                    buttonPanel.add(endTurnButton);

                    playerPanel.add(buttonPanel, BorderLayout.SOUTH);
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

                // Optionally clear panel or prepare for next player
                SwingUtilities.invokeLater(() -> {
                    playerPanel.removeAll();
                    playerPanel.revalidate();
                    playerPanel.repaint();
                });
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
