package fungorium;

import javax.swing.*;
import java.util.*;
import java.util.List;
import java.awt.*;

public class GPlayer {
    private Player player;
    private String finalCommand = null;
    private JComboBox<String> param1box;
    private JComboBox<String> param2box;
    private JComboBox<String> actionBox;
    private GMap gmap;

    public GPlayer(Player player) {
        this.player = player;
    }

    public Player getPlayer() { return player; }

    public JPanel getPanel(GMap map) {

        this.gmap = map;
        JPanel playerPanel = new JPanel(new BorderLayout());
        playerPanel.setSize(1080, 360);

        // --- Top Info Panel ---
        JPanel playerInfoPanel = new JPanel();
        playerInfoPanel.setLayout(new BoxLayout(playerInfoPanel, BoxLayout.Y_AXIS));
        playerInfoPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        playerInfoPanel.setBackground(new Color(250, 235, 215));
        Font infoFont = new Font("SansSerif", Font.BOLD, 16);

        JLabel playerNameLabel = new JLabel("Player: " + player.getName());
        playerNameLabel.setFont(infoFont);
        JLabel actionPointsLabel = new JLabel("Action Points: " + player.getActionPoints());
        actionPointsLabel.setFont(infoFont);
        JLabel scoreLabel = new JLabel("Score: " + player.getScore());
        scoreLabel.setFont(infoFont);

        playerInfoPanel.add(playerNameLabel);
        playerInfoPanel.add(actionPointsLabel);
        playerInfoPanel.add(scoreLabel);

        // --- Bottom Action Panel ---
        JPanel playerActionPanel = new JPanel();
        playerActionPanel.setLayout(new BoxLayout(playerActionPanel, BoxLayout.Y_AXIS));
        playerActionPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        playerActionPanel.setBackground(new Color(250, 235, 215));

        // Action ComboBox
        actionBox = new JComboBox<>(player.getActions().toArray(new String[0]));
        actionBox.setSelectedIndex(0);
        playerActionPanel.add(actionBox);

        // Param1 and Param2 ComboBoxes
        param1box = new JComboBox<>();
        param2box = new JComboBox<>();

        playerActionPanel.add(param1box);
        playerActionPanel.add(param2box);

        // Helper to update param1 and param2
        Runnable updateParamBoxes = () -> {
            kurvaanyad();
            // Now update param2 based on action + selected param1
            updateParam2Box();
        };

        // Helper to update param2
        Runnable updateParam2 = () -> {
            updateParam2Box();
        };

        // Listeners
        actionBox.addActionListener(e -> updateParamBoxes.run());
        param1box.addActionListener(e -> updateParam2.run());

        // Initial population
        updateParamBoxes.run();

        // osszeallitott command
        finalCommand = actionBox.getSelectedItem() + " " + param1box.getSelectedItem() + " " + param2box.getSelectedItem();

        JPanel InfoAndActionPanel = new JPanel();
        InfoAndActionPanel.setLayout(new GridLayout(2, 1));
        InfoAndActionPanel.add(playerInfoPanel);
        InfoAndActionPanel.add(playerActionPanel);

        playerPanel.add(InfoAndActionPanel, BorderLayout.CENTER);

        return playerPanel;
    }

    public void kurvaanyad() {
            // Update param1
            param1box.removeAllItems();
            for (String p1 : player.getParam1ForAction()) {
                param1box.addItem(p1);
            }

            // Ensure at least one selection
            if (param1box.getItemCount() > 0) {
                param1box.setSelectedIndex(0);
            }
    }

    // Helper function outside getPanel or make it private inside the class
    public void updateParam2Box() {
        String selectedAction = (String) actionBox.getSelectedItem();
        String selectedParam1 = (String) param1box.getSelectedItem();

        java.util.List<String> param2Options = player.getParam2ForAction(selectedAction, selectedParam1, gmap);
        param2box.removeAllItems();
        for (String p2 : param2Options) {
            param2box.addItem(p2);
        }
        if (param2box.getItemCount() > 0) {
            param2box.setSelectedIndex(0);
        }
    }

    public void turn(JPanel playerPanel, GMap map) {
        if(!player.inGame) {
            return;
        }
        //Set up playerPanel
        playerPanel.removeAll();
        playerPanel.add(this.getPanel(map), BorderLayout.CENTER);
        playerPanel.revalidate();
        playerPanel.repaint();
    }

    public String getFinalCommand() { return finalCommand; }

    public JComboBox<String> getParam1Box() { return this.param1box; }

    public JComboBox<String> getParam2Box() { return this.param2box; }

}
