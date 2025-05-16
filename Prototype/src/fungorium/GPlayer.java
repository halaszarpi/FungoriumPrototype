package fungorium;

import javax.swing.*;
import java.util.*;
import java.util.List;
import java.awt.*;

public class GPlayer implements IObservable {
    Player player;
    int x;
    int y;
    ArrayList<IObserver> observers = new ArrayList<>();
    private String finalCommand = null;

    public GPlayer(Player player) {
        this.player = player;
        attach(player.getView());
    }

    @Override
    public void attach(IObserver o) { observers.add(o); }

    @Override
    public void detach(IObserver o) { observers.remove(o); }

    @Override
    public void update(Graphics g) {
        for(IObserver o : observers) {
            o.draw(g, new Point(x, y));
        }
    }

    @Override
    public IObserver getObserver() { return observers.getFirst(); }

    public void setCoordinates(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public Player getPlayer() { return player; }

    public JPanel getPanel(GMap map) {
        JPanel playerPanel = new JPanel(new GridLayout(2, 1));
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
        JComboBox<String> actionBox = new JComboBox<>(player.getActions().toArray(new String[0]));
        actionBox.setSelectedIndex(0);
        playerActionPanel.add(actionBox);

        // Param1 and Param2 ComboBoxes
        JComboBox<String> param1box = new JComboBox<>();
        JComboBox<String> param2box = new JComboBox<>();

        playerActionPanel.add(param1box);
        playerActionPanel.add(param2box);

        // Helper to update param1 and param2
        Runnable updateParamBoxes = () -> {
            // Update param1
            param1box.removeAllItems();
            for (String p1 : player.getParam1ForAction()) {
                param1box.addItem(p1);
            }

            // Ensure at least one selection
            if (param1box.getItemCount() > 0) {
                param1box.setSelectedIndex(0);
            }

            // Now update param2 based on action + selected param1
            updateParam2Box(actionBox, param1box, param2box, map);
        };

        // Helper to update param2
        Runnable updateParam2 = () -> {
            updateParam2Box(actionBox, param1box, param2box, map);
        };

        // Listeners
        actionBox.addActionListener(e -> updateParamBoxes.run());
        param1box.addActionListener(e -> updateParam2.run());

        // Initial population
        updateParamBoxes.run();

        JButton okButton = new JButton("OK");
        // osszeallitott command

        okButton.addActionListener(e ->
            finalCommand = actionBox.getSelectedItem() + " " + param1box.getSelectedItem() + " " + param2box.getSelectedItem()
        );

        playerPanel.add(playerInfoPanel);
        playerPanel.add(playerActionPanel);
        playerPanel.add(okButton);

        return playerPanel;
    }

    // Helper function outside getPanel or make it private inside the class
    private void updateParam2Box(JComboBox<String> actionBox, JComboBox<String> param1box, JComboBox<String> param2box, GMap map) {
        String selectedAction = (String) actionBox.getSelectedItem();
        String selectedParam1 = (String) param1box.getSelectedItem();

        java.util.List<String> param2Options = player.getParam2ForAction(selectedAction, selectedParam1, map);
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
        playerPanel.add(this.getPanel(map));
        playerPanel.revalidate();
        playerPanel.repaint();
    }
}
