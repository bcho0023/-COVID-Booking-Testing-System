package Models.BookingPackage;

import java.util.Date;

public class BookingMemento {
    // Attributes
    private String venue;
    private Date timing;
    private Date modifyTime;

    // Constructor
    public BookingMemento(String venue, Date timing, Date modificationTime) {
        this.venue = venue;
        this.timing = timing;
        this.modifyTime = modificationTime;
    }

    // Getters
    public String getVenue() {
        return venue;
    }

    public Date getTiming() {
        return timing;
    }

    public Date getModificationTime() {
        return modifyTime;
    }

    @Override
    public String toString() {
        java.lang.String str = "";

        if (venue != null) {
            str += "Venue: " + venue + " ";
        }

        if (timing != null) {
            str += "Timing: " + timing + " ";
        }

        if (modifyTime != null) {
            str += "Modified: " + modifyTime;
        }

        return str;
    }
}
