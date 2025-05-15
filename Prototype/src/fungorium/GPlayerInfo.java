package fungorium;

import java.awt.*;
import javax.swing.*;

public class GPlayerInfo extends JPanel {

    public GPlayerInfo() {
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Világos krém háttér (pl. antikfehér)
        setBackground(new Color(250, 235, 215)); // #FAEBD7

        Font infoFont = new Font("SansSerif", Font.BOLD, 16);

        JLabel nameLabel = new JLabel("Player:");
        nameLabel.setFont(infoFont);

        JLabel colorLabel = new JLabel("Color: green");
        colorLabel.setFont(infoFont);

        JLabel scoreLabel = new JLabel("Score: 5");
        scoreLabel.setFont(infoFont);

        add(nameLabel);
        add(colorLabel);
        add(scoreLabel);
    }
}
