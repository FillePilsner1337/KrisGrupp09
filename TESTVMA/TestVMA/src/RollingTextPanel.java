import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

public class RollingTextPanel extends JPanel implements ActionListener {
    private String text = "";
    private int textPosition = 0;
    private Timer timer;

    public RollingTextPanel(){
        timer = new Timer(50, this);
        timer.start();
        setPreferredSize(new Dimension(800, 400));
        setBackground(Color.RED);

        fetchVmaMessages();
    }

    private void fetchVmaMessages() {
        String url = "https://api.krisinformation.se/v3/testvmas";

        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .header("Accept", "application/json")
                .build();

        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
            objectMapper.configure(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY, true);

            ApiExample.VmaMessage[] vmaMessages = objectMapper.readValue(response.body(), ApiExample.VmaMessage[].class);

            StringBuilder stringBuilder = new StringBuilder();
            for (ApiExample.VmaMessage vmaMessage : vmaMessages) {
                stringBuilder.append(vmaMessage.headline).append(" - ").append(vmaMessage.bodyText).append(" ");
            }

            text = stringBuilder.toString();
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        g.setColor(Color.BLACK);
        g.drawString(text, textPosition, 20);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        textPosition--;
        if (textPosition < -getWidth()) {
            textPosition = getWidth();
        }
        repaint();
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("Rullande text");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel Panel = new JPanel();
        Panel.setLayout(new BorderLayout());

        RollingTextPanel rollingTextPanel = new RollingTextPanel();
        Panel.add(rollingTextPanel, BorderLayout.NORTH);

        frame.add(Panel);

        frame.pack();
        frame.setVisible(true);
    }
}
