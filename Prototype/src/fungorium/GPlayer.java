package fungorium;

import javax.swing.*;
import java.awt.*;

public class GPlayer {
    private Player player;
    private JComboBox<String> actionBox;
    private JComboBox<String> param1box;
    private JComboBox<String> param2box;
    private GMap gmap;

    public GPlayer(Player player, Color playerColor) {
        this.player = player;

        if (player instanceof FungusFarmer fm) { 
            FungusFarmerView fmv = (FungusFarmerView)fm.getView();
            fmv.setColor(playerColor); 
        }

        if (player instanceof InsectKeeper ik) { 
            InsectKeeperView ikv = (InsectKeeperView)ik.getView();
            ikv.setColor(playerColor); 
        }

        param1box = new JComboBox<>();
        param2box = new JComboBox<>();
        
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
        playerActionPanel.add(param1box);
        playerActionPanel.add(param2box);

        // Helper to update param1 and param2
        updateParam1Box();
        updateParam2Box();

        // Listeners
        actionBox.addActionListener(e -> updateParam1Box());
        param1box.addActionListener(e -> updateParam2Box());

        JPanel InfoAndActionPanel = new JPanel();
        InfoAndActionPanel.setLayout(new GridLayout(2, 1));
        InfoAndActionPanel.add(playerInfoPanel);
        InfoAndActionPanel.add(playerActionPanel);

        playerPanel.add(InfoAndActionPanel, BorderLayout.CENTER);

        return playerPanel;
    }

    public void updateParam1Box() {
            // Update param1
            param1box.removeAllItems();
            for (String p1 : player.getParam1ForAction((String) actionBox.getSelectedItem())) {
                param1box.addItem(p1);
            }

            // debug
            param1box.setSelectedIndex(0);

            // Ensure at least one selection
            if (param1box.getItemCount() > 0) {
                param1box.setSelectedIndex(0);
            }
    }

    // Helper function outside getPanel or make it private inside the class
    public void updateParam2Box() {

        String selectedAction = (String) actionBox.getSelectedItem();
        String selectedParam1 = (String) param1box.getSelectedItem();

        if (selectedParam1 == null) return;

        // selectedParam1-et el kell split-elni, mert zarojelbe mogotte van a tekton
        String selectedParam1_splitted = selectedParam1.split(" ")[0];

        if (selectedParam1_splitted  == null) return;

        java.util.List<String> param2Options = player.getParam2ForAction(selectedAction, selectedParam1_splitted, gmap);
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

    public String getFinalCommand() { 
        String param1box_firstPart = param1box.getSelectedItem().toString().split(" ")[0];
        return actionBox.getSelectedItem() + " " + param1box_firstPart + " " + param2box.getSelectedItem(); 
    }

}
