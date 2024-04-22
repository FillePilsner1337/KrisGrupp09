package Client.View;

import javax.swing.*;
import java.awt.*;

public class VmaPanel extends JPanel {
    private JTextArea textArea;

    public VmaPanel() {
        setPreferredSize(new Dimension(800, 600));
        textArea = new JTextArea(35, 70);
        textArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(textArea);
        this.setLayout(new BorderLayout());
        this.add(scrollPane, BorderLayout.CENTER);
    }
    public void appendText(String text) {
        textArea.append(text + "\n");
    }
}
