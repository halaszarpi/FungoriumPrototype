package fungorium;

import java.awt.*;
import javax.swing.*;

public class GPlayerInfo extends JPanel {

    private GPlayer player;
    private JLabel nameLabel;
    private JLabel actionPointsLabel;
    private JLabel scoreLabel;

    public GPlayerInfo(GPlayer player) {
        this.player = player;

        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        setBackground(new Color(250, 235, 215)); // #FAEBD7

        Font infoFont = new Font("SansSerif", Font.BOLD, 16);

        nameLabel = new JLabel("Player: " + player.getPlayer().getName());
        nameLabel.setFont(infoFont);

        actionPointsLabel = new JLabel("ActionPoints: " + player.getPlayer().getActionPoints());
        actionPointsLabel.setFont(infoFont);

        scoreLabel = new JLabel("Score: " + player.getPlayer().getScore());
        scoreLabel.setFont(infoFont);

        add(nameLabel);
        add(actionPointsLabel);
        add(scoreLabel);
    }

    public void updatePersonInfo() {
        nameLabel.setText("Player: " + player.getPlayer().getName());
        actionPointsLabel.setText("ActionPoints: " + player.getPlayer().getActionPoints());
        scoreLabel.setText("Score: " + player.getPlayer().getScore());
    }
}
