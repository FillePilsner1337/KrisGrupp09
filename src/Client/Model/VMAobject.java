package Client.Model;

import org.jxmapviewer.viewer.GeoPosition;

public class VMAobject {
    private String headline;
    private String published;
    private String bodyText;
    private String description;

    private GeoPosition position;

    public VMAobject(String headline, String published, String bodyText, String description, String lon, String lat) {
        this.headline = headline;
        this.published = published;
        this.bodyText = bodyText;
        this.description = description;
        this.position = new GeoPosition(Double.parseDouble(lat),Double.parseDouble(lon));
    }

    public String getHeadline() {
        return headline;
    }

    public void setHeadline(String headline) {
        this.headline = headline;
    }

    public String getPublished() {
        return published;
    }

    public void setPublished(String published) {
        this.published = published;
    }

    public String getBodyText() {
        return bodyText;
    }

    public void setBodyText(String bodyText) {
        this.bodyText = bodyText;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public GeoPosition getPosition() {
        return position;
    }

    public void setPosition(GeoPosition position) {
        this.position = position;
    }
    public String toString(){
        return  "Nytt VMA i " + description;
    }


    public String getDetailedInfo() {
        String date = published.substring(0,10);
        String time = published.substring(11,16);
        return "Datum: " + date + "\n" +
                "Tidpunkt: " + time + "\n\n"+
                headline + "\n\n" +
                bodyText + "Dummy text eftersom test VMA inte ger någon text. Dummy text eftersom test VMA inte ger någon text. Dummy text eftersom test VMA inte ger någon text. ";
    }

    public GeoPosition getLocation() {
        return position;
    }
}
