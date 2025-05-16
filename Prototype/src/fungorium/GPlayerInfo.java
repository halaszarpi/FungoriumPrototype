package fungorium;

import java.awt.*;
import javax.swing.*;

public class GPlayerInfo extends JPanel {



    private JLabel nameLabel;
    private JLabel actionPointsLabel;
    private JLabel scoreLabel;

    public GPlayerInfo() {
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        setBackground(new Color(250, 235, 215)); // #FAEBD7

        Font infoFont = new Font("SansSerif", Font.BOLD, 16);

        nameLabel = new JLabel("Player: ");
        nameLabel.setFont(infoFont);

        actionPointsLabel = new JLabel("ActionPoints: ");
        actionPointsLabel.setFont(infoFont);

        scoreLabel = new JLabel("Score: ");
        scoreLabel.setFont(infoFont);

        

        add(nameLabel);
        add(actionPointsLabel);
        add(scoreLabel);
    }

    public void updatePersonInfo(Player player) {
        nameLabel.setText("Player: " + player.getName());
        actionPointsLabel.setText("ActionPoints: " + player.getActionPoints());
        scoreLabel.setText("Score: " + player.getScore());
        
    

    }
}
