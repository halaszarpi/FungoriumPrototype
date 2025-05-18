package fungorium;

import java.awt.Graphics;
import java.awt.Image;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JPanel;

public class GamePanel extends JPanel {

    private Image image = null;

    public GamePanel() {
        try {
            image = ImageIO.read(new File("Prototype/src/fungorium/hatter.png"));
        }
        catch (IOException e) {

        }
    }

    @Override
    public void paintComponent(Graphics g) {
        g.drawImage(image, 0, 0, getWidth(), getHeight(), this);
    }
}
