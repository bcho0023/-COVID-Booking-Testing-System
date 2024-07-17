package Screens.BookingScreens;

import Models.BookingPackage.Booking;
import Models.BookingPackage.BookingStatus;
import Models.BookingPackage.Symptom;
import Models.BookingPackage.BookingMemento;

import java.util.ArrayList;

public abstract class BookingView {
    // Attributes
    protected Booking model;

    // Input attributes
    protected String customerId = null;
    protected String patientId = null;
    protected String siteId = null;
    protected String bookingId = null;
    protected String pin = null;
    protected String dateString = null;
    protected RatObtainLocation ratObtainLocation = null;
    protected BookingStatus status = null;
    protected ArrayList<Symptom> symptoms = null;
    protected BookingMemento reversion = null;

    // Constructor
    public BookingView(Booking model) {
        this.model = model;
    }

    // Getters
    public Booking getModel() {
        return model;
    }

    public String getCustomerId() {
        return customerId;
    }

    public String getPatientId() {
        return patientId;
    }

    public String getSiteId() {
        return siteId;
    }

    public String getBookingId() {
        return bookingId;
    }

    public String getPin() {
        return pin;
    }

    public String getDateString() {
        return dateString;
    }

    public RatObtainLocation getRatObtainLocation() {
        return ratObtainLocation;
    }

    public BookingStatus getStatus() {
        return status;
    }

    public ArrayList<Symptom> getSymptoms() {
        return symptoms;
    }

    public BookingMemento getReversion() {
        return reversion;
    }

    // General
    public abstract void title(String prompt);

    public abstract void message(String prompt);

    // Display booking
    public abstract void displayBooking();

    // Search booking methods
    public abstract void search();

    // Create booking methods
    public abstract void createOnSite();

    public abstract void createSystem();

    // Survey methods
    public abstract void survey();

    // Modify methods
    public abstract void modifySystem();

    public abstract void modifyPhone();

    // Delete methods
    public abstract void delete();

    // Other methods
    public abstract void verifyUser();


}
