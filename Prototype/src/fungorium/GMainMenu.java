package fungorium;

import javax.swing.*;
import java.awt.*;

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
            JOptionPane.showMessageDialog(this, "Rules are not implemented yet.");
            dispose();
        });

        exitButton.addActionListener(e -> {
            JOptionPane.showMessageDialog(this, "Goodbye!");
            System.exit(0);
        });

        setVisible(true);
    }
}
