package fungorium;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.util.ArrayList;
import java.util.Map;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JPanel;

public class GPlayerCommand extends JPanel {
    private JComboBox<String> commandBox;
    private JButton okButton;
    private JPanel parametersPanel;
    private ArrayList<JComboBox<String>> parameterBoxes;

    private final String[] allCommands = {
            "GROWMYC",
            "GROWBOD",
            "EATINS",
            "SCATTERSP",
            "MOVETOTECTON",
            "CUTMYC",
            "EATSPORE"
    };
    private final Map<String, Integer> commandsValidParamCounts = Map.of(
            "GROWMYC", 2,
            "GROWBOD", 2,
            "EATINS", 2,
            "SCATTERSP", 2,
            "MOVETOTECTON", 2,
            "CUTMYC", 2,
            "EATSPORE", 2
    );

    // Ez a fuggveny arra van, hogyha mas parancsot valasztunk, akkor frissul a lehetseges parameterek tartalma
    // Pl.: Tektonra lepesnel ne lehessen mycelium-ot valasztani...
    private void updateChosenCommandsParameters() {
        parametersPanel.removeAll();

        String currentCommand = commandBox.getSelectedItem().toString();
        int currentCommandParamCount = commandsValidParamCounts.getOrDefault(currentCommand, 0);

        // Ide majd a valid paraméterek kellenek, egyelore csak valami random
        String[] dummyParams = {"value1", "value2", "value3"};

        for (int i = 0; i < currentCommandParamCount; i++) {
            parametersPanel.add(new JComboBox<>(dummyParams));
        }

        parametersPanel.revalidate();
        parametersPanel.repaint();
    }

    public GPlayerCommand() {
        commandBox = new JComboBox<>(allCommands);
        commandBox.setSelectedIndex(0);
        commandBox.addActionListener(e -> {
            updateChosenCommandsParameters();
        });

        okButton = new JButton("OK");
        // okButton-nak kell egy actionListener ide

        parametersPanel = new JPanel();
        parametersPanel.setLayout(new FlowLayout());
        parametersPanel.setVisible(true);

        this.setLayout(new BorderLayout());
        this.add(commandBox, BorderLayout.NORTH);
        this.add(parametersPanel, BorderLayout.CENTER);
        this.add(okButton, BorderLayout.SOUTH);

        updateChosenCommandsParameters();
    }
}