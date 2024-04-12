import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

import javax.swing.*;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;

public class VmaController {
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class VmaMessage {
        @JsonProperty("Identifier")
        public String identifier;
        @JsonProperty("PushMessage")
        public String pushMessage;
        @JsonProperty("Updated")
        public String updated;
        @JsonProperty("Published")
        public String published;
        @JsonProperty("Headline")
        public String headline;
        @JsonProperty("Preamble")
        public String preamble;
        @JsonProperty("BodyText")
        public String bodyText;
        @JsonProperty("Area")
        public List<Area> areas;
        @JsonProperty("Web")
        public String web;
        @JsonProperty("Language")
        public String language;
        @JsonProperty("Event")
        public String event;
        @JsonProperty("SenderName")
        public String senderName;
        @JsonProperty("BodyLinks")
        public String bodyLinks;
        @JsonProperty("SourceID")
        public int sourceID;
        @JsonProperty("IsTest")
        public boolean isTest;
    }

    public static class Area {
        @JsonProperty("Type")
        public String type;
        @JsonProperty("Description")
        public String description;
        @JsonProperty("Coordinate")
        public String coordinate;
        @JsonProperty("CoordinateObject")
        public CoordinateObject coordinateObject;
    }

    public static class CoordinateObject {
        @JsonProperty("Latitude")
        public String latitude;
        @JsonProperty("Longitude")
        public String longitude;
        @JsonProperty("Altitude")
        public String altitude;
    }

    public static void fetchAndDisplayVmaData(VmaPanel panel) {
        SwingUtilities.invokeLater(() -> {
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

                VmaMessage[] vmaMessages = objectMapper.readValue(response.body(), VmaMessage[].class);

                for (VmaMessage vmaMessage : vmaMessages) {
                    panel.appendText("Identifier: " + vmaMessage.identifier);
                    panel.appendText("Headline: " + vmaMessage.headline);
                    panel.appendText("BodyText: " + vmaMessage.bodyText);
                    panel.appendText("Web: " + vmaMessage.web);
                    if (vmaMessage.areas != null) {
                        for (Area area : vmaMessage.areas) {
                            panel.appendText("Area Type: " + area.type);
                            panel.appendText("Area Description: " + area.description);
                            panel.appendText("Area Coordinate: " + area.coordinate);
                            if (area.coordinateObject != null) {
                                panel.appendText("Latitude: " + area.coordinateObject.latitude);
                                panel.appendText("Longitude: " + area.coordinateObject.longitude);
                                panel.appendText("Altitude: " + area.coordinateObject.altitude);
                            }
                        }
                    }
                    panel.appendText("-----------------------------------");
                }
            } catch (IOException | InterruptedException e) {
                e.printStackTrace();
            }
        });
    }
}

