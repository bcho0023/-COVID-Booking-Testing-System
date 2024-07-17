package Models.SitePackage;

import java.time.LocalTime;

public class OperatingHour {
    private final LocalTime openingHour;
    private final LocalTime closingHour;

    public OperatingHour(String openingHour, String closingHour){
        this.openingHour = LocalTime.parse(openingHour);
        this.closingHour = LocalTime.parse(closingHour);
    }

    public String toString(){
        return this.openingHour.toString() + "-" + this.closingHour.toString();
    }
}
