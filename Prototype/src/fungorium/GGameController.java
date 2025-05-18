package fungorium;

import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import javax.swing.*;

import fungorium.GMainMenu.BackgroundPanel;

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

        //Menu items
        JButton colorButton = new JButton("Colors");
        colorButton.addActionListener(e -> {
            JPanel panel = new JPanel();
            panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

            for (GPlayer player : players) {
                JPanel row = new JPanel(new FlowLayout(FlowLayout.LEFT));
                JLabel nameLabel = new JLabel(player.getPlayer().getName() + ": ");
                nameLabel.setPreferredSize(new Dimension(100, 20));

                JPanel colorBox = new JPanel();
                colorBox.setBackground(player.getColor());
                colorBox.setPreferredSize(new Dimension(40, 20));
                colorBox.setBorder(BorderFactory.createLineBorder(Color.BLACK));

                row.add(nameLabel);
                row.add(colorBox);
                panel.add(row);
            }

            JScrollPane scrollPane = new JScrollPane(panel);
            scrollPane.setPreferredSize(new Dimension(300, 200));
            JOptionPane.showMessageDialog(this, scrollPane, "Player Colors", JOptionPane.INFORMATION_MESSAGE);
        });


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

        menuBar.add(colorButton);
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
        mapPanel = new JPanel();
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
        mapPanel.setVisible(false);

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

        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                revalidate();
                gmap.drawMap(gmap.findGTectonByName((String) TectonChooser.getSelectedItem()));
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
        comboBox.setRenderer(new ListCellRenderer<Color>() {
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
        int numPlayers;

        while (true) {
            JSpinner spinner = new JSpinner(new SpinnerNumberModel(2, 2, 8, 1)); // start=2, min=2, max=8
            JComponent editor = spinner.getEditor();
            ((JSpinner.DefaultEditor) editor).getTextField().setColumns(2);

            int option = JOptionPane.showOptionDialog(
                    this,
                    spinner,
                    "Select number of players",
                    JOptionPane.OK_CANCEL_OPTION,
                    JOptionPane.QUESTION_MESSAGE,
                    null,
                    null,
                    null
            );

            if (option == JOptionPane.OK_OPTION) {
                numPlayers = (int) spinner.getValue();
                break;
            } else {
                // Optionally ask if the user wants to cancel or continue
                int retry = JOptionPane.showConfirmDialog(
                        this,
                        "You must select a number of players to continue.\nDo you want to try again?",
                        "Input Required",
                        JOptionPane.YES_NO_OPTION
                );

                if (retry != JOptionPane.YES_OPTION) {
                    showError("Player selection was cancelled.");
                    dispose();
                    SwingUtilities.invokeLater(GMainMenu::new);
                    return;
                }
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
            JSpinner spinner = new JSpinner(new SpinnerNumberModel(10, 1, 100, 1));
            JComponent editor = spinner.getEditor();
            ((JSpinner.DefaultEditor) editor).getTextField().setColumns(3);

            int option = JOptionPane.showOptionDialog(
                    this,
                    spinner,
                    "Select number of rounds",
                    JOptionPane.DEFAULT_OPTION,
                    JOptionPane.QUESTION_MESSAGE,
                    null,
                    new Object[]{"OK"},
                    "OK"
            );

            if (option == 0) {
                numberOfRounds = (int) spinner.getValue();
                break;
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

        mapPanel.setVisible(true);

        for (int round = 0; round < numberOfRounds; round++) {
            updateTectonChooser();

            for (GPlayer player : players) {
                CountDownLatch latch = new CountDownLatch(1);  

                player.turn(playerPanel, gmap);

                SwingUtilities.invokeLater(() -> {
                    gmap.drawMap(gmap.findGTectonByName((String) TectonChooser.getSelectedItem()));
                    JButton okButton = new JButton("OK");
                    okButton.addActionListener(e -> {
                        player.getPlayer().doAction(tectonMap, player.getFinalCommand());
                        player.updateParam1Box();
                        player.updateParam2Box();
                        player.setActionPoints();
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

            repaint();
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
