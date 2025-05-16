package fungorium;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class GPlayer implements IObservable {
    Player player;
    int x;
    int y;
    ArrayList<IObserver> observers = new ArrayList<>();

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

    public JPanel getPanel() {
        JPanel playerPanel = new JPanel();
        playerPanel.setLayout(new GridLayout(2, 1));
        playerPanel.setSize(1080, 360);

        JPanel playerInfoPanel = new JPanel();
        playerInfoPanel.setSize(1080, 180);
        playerInfoPanel.setLayout(new BoxLayout(playerInfoPanel, BoxLayout.Y_AXIS));
        playerInfoPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        playerInfoPanel.setBackground(new Color(250, 235, 215)); // #FAEBD7
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
        playerInfoPanel.setVisible(true);

        JPanel playerActionPanel = new JPanel();
        JComboBox<String> actionBox = new JComboBox<>(player.getActions().toArray(new String[0]));
        actionBox.addItem("Action 1");
        actionBox.setSelectedIndex(0);
        playerActionPanel.add(actionBox);
        playerActionPanel.setSize(1080, 180);
        playerActionPanel.setLayout(new BoxLayout(playerActionPanel, BoxLayout.Y_AXIS));
        playerActionPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        playerActionPanel.setBackground(new Color(250, 235, 215)); // #FAEBD7

        playerPanel.add(playerInfoPanel);
        playerPanel.add(playerActionPanel);
        playerPanel.setVisible(true);

        return playerPanel;
    }

    public void turn(JPanel playerPanel) {
        if(!player.inGame) {
            return;
        }
        //Set up playerPanel
        playerPanel.removeAll();
        playerPanel.add(this.getPanel());
        playerPanel.revalidate();
        playerPanel.repaint();
    }
}
