package fungorium;

import java.awt.Graphics;
import java.awt.Image;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JPanel;

public class GamePanel extends JPanel{
    private Image image = null;
    private String backgroundImage = "Prototype/src/fungorium/Tektonhatter.png";

    public GamePanel() {
        try {
            image = ImageIO.read(new File(backgroundImage));
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(image, 0, 0, getWidth(), getHeight(), this);
    }

}
