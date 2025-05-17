package fungorium;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import javax.swing.*;
import java.util.Set;
import java.util.concurrent.CountDownLatch;
import java.util.stream.Collectors;

public class GGameController extends JFrame {
    private final TectonMap tectonMap;
    private List<GPlayer> players;
    private int numberOfRounds;
    private JPanel GamePanel;
    private JPanel playerPanel;
    private JPanel mapPanel;
    private final GMap gmap;
    JComboBox<String> TectonChooser;
    private ArrayList<Color> allColors = new ArrayList<>(
        Arrays.asList(
            Color.RED, 
            Color.GREEN, 
            Color.CYAN, 
            Color.YELLOW, 
            Color.GRAY, 
            Color.PINK, 
            Color.MAGENTA, 
            Color.ORANGE
    ));

    public GGameController() {
        setTitle("Fungorium - Game");
        setSize(1080, 720);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        //Loading Map
        tectonMap = new TectonMap();
        File gameMap = new File(System.getProperty("user.dir") + "\\Prototype\\src\\gamemaps\\startingMap.txt");
        gmap = new GMap(this.tectonMap); // initialize GMap field
        try {
            tectonMap.processAllMapCreatingCommands(gameMap);
        } catch (Exception e) {
            showError("Error creating map:\n" + e.getMessage());
            return;
        }

        //Menubar
        JMenuBar menuBar = new JMenuBar();
        menuBar.setVisible(true);
        menuBar.setBorderPainted(true);
        menuBar.setOpaque(true);
        menuBar.setBackground(Color.LIGHT_GRAY);
        menuBar.setPreferredSize(new Dimension(1080, 30));

        JButton mainMenuItem = new JButton("Back to Main Menu");
        mainMenuItem.addActionListener(e -> {
            dispose();
            SwingUtilities.invokeLater(GMainMenu::new);
        });

        JButton exitMenuItem = new JButton("Exit game");
        exitMenuItem.addActionListener(e -> {
            dispose();
            System.exit(0);
        });

        JButton rulesMenuItem = new JButton("Rules");
        rulesMenuItem.addActionListener(e -> {
            try {
                List<String> lines = Files.readAllLines(Paths.get("Prototype/src/fungorium/Rules.txt"));
                StringBuilder rulesText = new StringBuilder();
                for (String line : lines) {
                    rulesText.append(line).append("\n");
                }
                JTextArea textArea = new JTextArea(rulesText.toString());
                textArea.setEditable(false);
                textArea.setLineWrap(true);
                JScrollPane scrollPane = new JScrollPane(textArea);
                scrollPane.setPreferredSize(new Dimension(600, 400));
                JOptionPane.showMessageDialog(this, scrollPane, "Game Rules", JOptionPane.INFORMATION_MESSAGE);
            } catch (IOException ex) {
                showError("Error loading rules: " + ex.getMessage());
            }
        });

        menuBar.add(rulesMenuItem);
        menuBar.add(mainMenuItem);
        menuBar.add(exitMenuItem);
        setJMenuBar(menuBar);

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

    private Color chooseColor() {
        // Létrehozunk egy JComboBox-ot Color objektumokkal
        JComboBox<Color> comboBox = new JComboBox<>(allColors.toArray(new Color[0]));

        // Renderer: minden elem egy kis színmintát jelenít meg
        comboBox.setRenderer(new ListCellRenderer<>() {
            private final JPanel panel = new JPanel();

            @Override
            public Component getListCellRendererComponent(
                JList<? extends Color> list,
                Color value,
                int index,
                boolean isSelected,
                boolean cellHasFocus) {

                panel.setBackground(value);

                if (isSelected) {
                    panel.setBorder(BorderFactory.createLineBorder(Color.WHITE, 2));
                } else {
                    panel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
                }

                panel.setPreferredSize(new Dimension(100, 20));

                return panel;
            }
        });

        // Megjelenítjük egy JOptionPane-ben
        int result = JOptionPane.showConfirmDialog(
                null,
                comboBox,
                "Choose a color!",
                JOptionPane.OK_CANCEL_OPTION,
                JOptionPane.PLAIN_MESSAGE
        );
        
        if (result == JOptionPane.OK_OPTION) {
            return (Color) comboBox.getSelectedItem();
        } else {
            return null;
        }
        
    }

    private void initializeGame() {
        showInfo("Game is starting!");
        String input;

        // Get number of players (between 2 and 16)
        int numPlayers = 0;
        while (true) {
            input = JOptionPane.showInputDialog(this, "Enter number of players (2–8):");
            try {
                numPlayers = Integer.parseInt(input);
                if (numPlayers >= 2 && numPlayers <= 8) {
                    break;
                } else {
                    showError("Number of players must be between 2 and 8.");
                }
            } catch (NumberFormatException e) {
                showError("Please enter a valid integer.");
            }
        }

        // Get names of players (non-null, unique)
        players = new ArrayList<>();
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

            Color chosenColor = chooseColor();
            allColors.remove(chosenColor);

            if (i % 2 == 0) {
                players.add(new GPlayer(new FungusFarmer(playerName), chosenColor));
            } else {
                players.add(new GPlayer(new InsectKeeper(playerName), chosenColor));
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
                    JButton okButton = new JButton("OK");
                    okButton.addActionListener(e -> {
                        player.getPlayer().doAction(tectonMap, player.getFinalCommand());
                        player.updateParam1Box();
                        player.updateParam2Box();
                        repaint();
                    });

                    JButton endTurnButton = new JButton("End Turn");
                    endTurnButton.addActionListener(e -> latch.countDown());

                    playerPanel.add(endTurnButton, BorderLayout.NORTH);
                    playerPanel.add(okButton, BorderLayout.SOUTH);
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

            gmap.roundPassed();
        }

        endGame();
    }

    private void endGame() {
        showInfo("Game Over!");

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
