package fungorium;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import javax.imageio.ImageIO;
import javax.swing.*;

public class GMainMenu extends JFrame {

    public GMainMenu() {
        setTitle("Fungorium - Main Menu");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1080, 720);
        setLocationRelativeTo(null);

        BackgroundPanel backgroundPanel = new BackgroundPanel("Prototype/src/fungorium/Menu.png");
        backgroundPanel.setLayout(null);

    
        JButton newGameButton = createInvisibleButton(350, 220, 380, 80);
        JButton rulesButton = createInvisibleButton(350, 330, 380, 80);
        JButton exitButton = createInvisibleButton(350, 500, 380, 80);

       
        newGameButton.addActionListener(e -> {
            dispose();
            SwingUtilities.invokeLater(GGameController::new);
        });

       rulesButton.addActionListener(e -> {
try {
    List<String> lines = Files.readAllLines(Paths.get("Prototype/src/fungorium/Rules.txt"));
    StringBuilder rules = new StringBuilder();
    for (String line : lines) {
        rules.append(line).append("\n");
    }

    JDialog dialog = new JDialog(this, "Game Rules", true);
    dialog.setSize(900, 700);
    dialog.setLocationRelativeTo(this);

   
    BackgroundPanel panel = new BackgroundPanel("Prototype/src/fungorium/rules.png");
    panel.setLayout(null); 
   
    JTextArea textArea = new JTextArea(rules.toString());
    textArea.setEditable(false);
    textArea.setLineWrap(true);
    textArea.setWrapStyleWord(true);
    textArea.setOpaque(false);
    textArea.setForeground(Color.black);
    textArea.setFont(new Font("Serif", Font.PLAIN, 18));
  
    textArea.setMargin(new Insets(20, 20, 20, 20));

   
    JScrollPane scrollPane = new JScrollPane(textArea);
    scrollPane.setOpaque(false);
    scrollPane.getViewport().setOpaque(false);
    scrollPane.setBorder(BorderFactory.createEmptyBorder());
   scrollPane.setBounds(110, 80, 660, 500);

    scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_NEVER);
    scrollPane.getVerticalScrollBar().setPreferredSize(new Dimension(0, 0));

    panel.add(scrollPane);

    dialog.setContentPane(panel);
    dialog.setVisible(true);

} catch (IOException ex) {
    JOptionPane.showMessageDialog(this, "Failed to load rules file.", "Error", JOptionPane.ERROR_MESSAGE);
    ex.printStackTrace();
}



});



exitButton.addActionListener(e -> {

    JDialog dialog = new JDialog(this, "Exit", true);
    dialog.setSize(400, 300);
    dialog.setLocationRelativeTo(this);

    BackgroundPanel panel = new BackgroundPanel("Prototype/src/fungorium/GoodBye.png");
    panel.setLayout(null);



    JButton okButton = createInvisibleButton(350, 220, 380, 80);
    okButton.setBounds(110, 150, 80, 40);
    okButton.setOpaque(false);
    okButton.setContentAreaFilled(false);
    okButton.setBorderPainted(false);
    okButton.setFocusPainted(false);
    okButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
    okButton.addActionListener(ev -> dialog.dispose());
    panel.add(okButton);

    dialog.setContentPane(panel);
    dialog.setVisible(true);
});

    
        backgroundPanel.add(newGameButton);
        backgroundPanel.add(rulesButton);
        backgroundPanel.add(exitButton);

        setContentPane(backgroundPanel);
        setVisible(true);
    }

  
    private JButton createInvisibleButton(int x, int y, int width, int height) {
        JButton button = new JButton();
        button.setBounds(x, y, width, height);
        button.setOpaque(false);
        button.setContentAreaFilled(false);
        button.setBorderPainted(false);
        button.setFocusPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return button;
    }

    
    static class BackgroundPanel extends JPanel {
        private Image backgroundImage;

        public BackgroundPanel(String imagePath) {
            try {
                backgroundImage = ImageIO.read(new File(imagePath));
            } catch (IOException e) {
                System.err.println("Background image not found: " + imagePath);
            }
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            if (backgroundImage != null) {
                g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
            }
        }
    }
}
