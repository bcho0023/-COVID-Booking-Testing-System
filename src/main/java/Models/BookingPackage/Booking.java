package Models.BookingPackage;

import Models.TestPackage.Test;

import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.Queue;

public class Booking {
    // Booking information
    private final String bookingId;
    private final String customerId;
    private String siteId;
    private Date startTime;
    private String smsPin;
    private BookingStatus status;
    private ArrayList<Test> covidTests;
    private ArrayList<Symptom> symptoms;
    private Queue<BookingMemento> modifications;
    private String notes;
    private String additionalInfo;

    public Booking(String bookingId, String customerId, String siteId, Date startTime, String smsPin, BookingStatus status, ArrayList<Test> covidTests, String notes, String additionalInfo) {
        this.bookingId = bookingId;
        this.customerId = customerId;
        this.siteId = siteId;
        this.startTime = startTime;
        this.smsPin = smsPin;
        this.status = status;
        this.covidTests = covidTests;
        this.symptoms = new ArrayList<>();
        this.modifications = new LinkedList<>();
        this.notes = notes;
        this.additionalInfo = additionalInfo;
    }

    // Getter
    public String getBookingId() {
        return this.bookingId;
    }

    public String getCustomerId() {
        return customerId;
    }

    public String getSiteId() {
        return siteId;
    }

    public String getSmsPin() {
        return this.smsPin;
    }

    public BookingStatus getStatus() {
        return this.status;
    }

    public Date getStartTime() {
        return startTime;
    }

    public String getAdditionalInfo() {
        return additionalInfo;
    }

    public ArrayList<BookingMemento> getModifications() {
        ArrayList<BookingMemento> list = new ArrayList(modifications);
        return list;
    }

    // Setters
    public void setSiteId(String siteId) {
        this.siteId = siteId;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public void setStatus(BookingStatus status) {
        this.status = status;
    }

    // Mutators
    public void addSymptoms(Symptom symptom) {
        this.symptoms.add(symptom);
    }

    public void addModification(BookingMemento modification) {
        modifications.add(modification);
    }

    // Memento methods
    public void saveModification() {
        while (this.modifications.size() > 2) {
            modifications.poll();
        }
        this.modifications.add(new BookingMemento(this.siteId, this.startTime, new Date()));
    }

    public void revertModification(BookingMemento change) {
        this.modifications.remove(change);
        this.siteId = change.getVenue();
        this.startTime = change.getTiming();
    }

    // Other
    public String toString(){
        return "BookingId = '" + bookingId + '\'' +
                "\nCustomerId = '" + customerId + '\'' +
                "\nBookingTime = '" + startTime + '\'' +
                "\nSmsPin= '" + smsPin + '\'' +
                "\nStatus = '" + status + '\'' +
                "\nCovidTests = '" + covidTests + "'";
    }
}


