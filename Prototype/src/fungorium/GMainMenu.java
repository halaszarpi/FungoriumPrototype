package fungorium;

import java.awt.*;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List; 
import javax.swing.*; 

public class GMainMenu extends JFrame {

    public GMainMenu() {
        setTitle("Fungorium - Main Menu");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1080, 720);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        JLabel titleLabel = new JLabel("Fungorium", JLabel.CENTER);
        titleLabel.setFont(new Font("Serif", Font.BOLD, 100));
        add(titleLabel, BorderLayout.NORTH);

        JPanel buttonPanel = new JPanel(new GridLayout(3, 1, 10, 10));

        JButton newGameButton = new JButton("New Game");
        JButton rulesButton = new JButton("Rules");
        JButton exitButton = new JButton("Exit");

        buttonPanel.add(newGameButton);
        buttonPanel.add(rulesButton);
        buttonPanel.add(exitButton);

        add(buttonPanel, BorderLayout.CENTER);

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

        JTextArea textArea = new JTextArea(rules.toString());
        textArea.setEditable(false);
        textArea.setLineWrap(true);
        textArea.setWrapStyleWord(true);

        JScrollPane scrollPane = new JScrollPane(textArea);
        scrollPane.setPreferredSize(new Dimension(600, 500)); 

        JOptionPane.showMessageDialog(this, scrollPane, "Game Rules", JOptionPane.INFORMATION_MESSAGE);

    } catch (IOException ex) {
        JOptionPane.showMessageDialog(this, "Failed to load rules file.", "Error", JOptionPane.ERROR_MESSAGE);
        ex.printStackTrace();
    }
});



        exitButton.addActionListener(e -> {
            JOptionPane.showMessageDialog(this, "Goodbye!");
            System.exit(0);
        });

        setVisible(true);
    }
}
