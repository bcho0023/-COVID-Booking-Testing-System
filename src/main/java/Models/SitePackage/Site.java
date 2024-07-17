package Models.SitePackage;

import java.time.DayOfWeek;
import Models.BookingPackage.Booking;
import Models.BookingPackage.BookingStatus;

import java.util.ArrayList;
import java.util.Date;
import java.util.Objects;

public class Site {
    // Site information
    private final String id;
    private final String name;
    private final String description;
    private final String websiteUrl;
    private final String phoneNumber;
    private final Address address;
    private ArrayList<Booking> bookings;
    private ArrayList<SiteType> siteTypes;
    private OperatingHour[] operatingHours;
    private String additionalInfo;

    // Constructor
    public Site(String id, String name, String description, String websiteUrl, String phoneNumber, Address address, String additionalInfo) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.websiteUrl = websiteUrl;
        this.phoneNumber = phoneNumber;
        this.address = address;
        this.siteTypes = new ArrayList<>();
        this.operatingHours = new OperatingHour[7];
        this.additionalInfo = additionalInfo;
    }

    // Getter
    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Address getAddress() {
        return this.address;
    }

    public ArrayList<Booking> getBookings() {
        return this.bookings;
    }

    public ArrayList<SiteType> getSiteTypes() {
        return this.siteTypes;
    }

    public OperatingHour[] getOperatingHours() {
        return this.operatingHours;
    }

    public Booking getBookingByPin(String pin) {
        for (Booking booking : bookings){
            // Get booking PIN
            String smsPin = booking.getSmsPin();

            // Match input against booking
            if (Objects.equals(smsPin, pin)) {
                return booking;
            }
        }

        return null;
    }

    public Booking getBookingById(String id) {
        for (Booking booking : bookings){
            // Get booking PIN
            String bookingId = booking.getBookingId();

            // Match input against booking
            if (Objects.equals(bookingId, id)) {
                return booking;
            }
        }

        return null;
    }

    public ArrayList<Booking> getActiveBookings(Date date){
        ArrayList<Booking> activeBookings = new ArrayList<>();
        for (Booking booking: bookings){
            // Checks if booking is active by checking if it is after the current date, and it has not been cancelled
            if (booking.getStartTime().after(date) && !booking.getStatus().equals(BookingStatus.CANCELLED)) {
                activeBookings.add(booking);
            }

        }

        return activeBookings;
    }

    public ArrayList<Booking> getInactiveBookings(Date date){
        ArrayList<Booking> InactiveBookings = new ArrayList<>();
        for (Booking booking: bookings){
            // Checks if booking is inactive by checking if it is behind the current date or it is cancelled
            if (booking.getStartTime().before(date) || booking.getStatus().equals(BookingStatus.CANCELLED)) {
                InactiveBookings.add(booking);
            }
        }

        return InactiveBookings;
    }

    // Setters

    public void setBookings(ArrayList<Booking> bookings) {
        this.bookings = bookings;
    }

    public void addSiteType(SiteType type) {
        siteTypes.add(type);
    }

    public void setOperatingHour(int day, OperatingHour hour) {
        this.operatingHours[day-1] = hour;
    }

    private String weeklyOperatingHoursToString() {
        String str = "[";
        for (int i = 0; i < this.operatingHours.length; i++){
            str += DayOfWeek.of(i+1) + ": " + this.operatingHours[i].toString();
            str += i < this.operatingHours.length-1 ? ", " : "";
        }

        return str + "]";
    }

    @Override
    public String toString() {
        return "name = '" + name + '\'' +
                "\nid = '" + id + '\'' +
                "\ndescription = '" + description + '\'' +
                "\nwebsiteUrl = '" + websiteUrl + '\'' +
                "\nphoneNumber = '" + phoneNumber + '\'' +
                "\naddress = '" + address + '\'' +
                "\ntypes = '" + siteTypes + '\'' +
                "\nOperatingHours = '" + weeklyOperatingHoursToString() + "'";
    }
}
