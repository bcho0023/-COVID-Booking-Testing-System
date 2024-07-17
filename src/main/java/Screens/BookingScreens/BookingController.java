package Screens.BookingScreens;

import Models.BookingPackage.BookingStatus;
import Models.BookingPackage.Symptom;
import Models.TestPackage.TestResult;
import Models.TestPackage.TestStatus;
import Models.UserPackage.User;
import Screens.AbstractFactory;
import Services.ApiPackage.Api;
import Services.ApiPackage.ApiAdapter;
import Services.ConferencePackage.Conference;
import Services.ConferencePackage.ConferenceAdapter;
import Services.QRPackage.QRCode;
import Services.QRPackage.QRCodeAdapter;
import Models.BookingPackage.Booking;
import Models.SitePackage.Site;
import Models.DataModel;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.TimeZone;

public class BookingController {
    // Attributes
    private DataModel model;
    private BookingView view;
    private AbstractFactory factory;
    private SimpleDateFormat dateFormat;

    // Constructor
    public BookingController(AbstractFactory factory) {
        this.factory = factory;
        this.model = DataModel.getInstance();
        this.view = factory.getBookingView(null);
        this.dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        this.dateFormat.setTimeZone(TimeZone.getTimeZone("Australia/Sydney"));
    }

    // Display booking methods
    public void display() {
        view.title("Booking Details");

        // Display booking info
        view.displayBooking();
    }

    // Search booking methods
    public void search() {
        view.title("Booking Search");

        // Get booking for search criteria
        view.search();
        Booking booking = null;

        // Search by booking ID
        if (view.getBookingId() != null) {
            ArrayList<Site> sites = this.model.getSiteCollection().getSites();
            for (Site site: sites) {
                booking = site.getBookingById(view.getBookingId());
                if (booking != null) break;
            }
        }

        // Search by PIN
        else if (view.getPin() != null) {
            ArrayList<Site> sites = this.model.getSiteCollection().getSites();
            for (Site site: sites) {
                booking = site.getBookingByPin(view.getPin());
                if (booking != null) break;
            }
        }

        // Generate new model and view
        this.view = this.factory.getBookingView(booking);
        this.display();
    }

    // Create booking methods
    public void createOnsite() {
        view.title("Create Booking System");

        // Get booking info
        view.createOnSite();

        // Send API request to create booking
        Api api = new ApiAdapter();
        try {
            String pin = api.postBooking(view.getCustomerId(), view.getSiteId(), dateFormat.parse(view.getDateString()), null, null);

            // Display booking message
            view.message("\nBooking PIN generated, memorise the following PIN: " + pin);
        } catch (Exception e) {
            System.out.println("Booking not successful");
            return;
        }
    }

    public void createSystem() {
        view.title("Create Booking System");

        // Get booking info
        view.createSystem();

        // Create booking
        Api api = new ApiAdapter();
        QRCode qrService = new QRCodeAdapter();
        Conference urlService = new ConferenceAdapter();

        
        // Check if there time slot has already been taken
        ArrayList<Site> sites = this.model.getSiteCollection().getSites();
        for (Site site: sites) {
            if (site.getId().equals(view.getSiteId())){
                for (Booking booking: site.getBookings()){
                    try {
                        System.out.println(dateFormat.parse(view.getDateString()).toString());
                    } catch (ParseException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                    System.out.println(booking.getStartTime().toString());
                    try {
                        if(dateFormat.parse(view.getDateString()).toString().equals(booking.getStartTime().toString())){
                            view.message("Date/Time already taken");
                            return;
                        }
                    } catch (ParseException e) {
                        System.out.println("Wrong input date format");
                    }
                }
            }
        }
        
        // Send API and service requests to create booking

        // Onsite
        if (view.getRatObtainLocation() == null) {
            try {
                String pin = api.postBooking(view.getCustomerId(), view.getSiteId(), dateFormat.parse(view.getDateString()), null, null);

                // Display booking message
                view.message("\nBooking PIN generated, memorise the following PIN: " + pin);
            } catch (Exception e) {
                System.out.println("Booking not successful");
                System.out.println(e.toString());
            }
        }

        // Home (RAT obtain from home)
        else if (view.getRatObtainLocation() == RatObtainLocation.HOME) {
            try {
                String url = urlService.generateConferenceURL();
                String pin = api.postBooking(view.getCustomerId(), view.getSiteId(), dateFormat.parse(view.getDateString()), null, url);

                // Display booking message
                view.message("\nBooking PIN generated, memorise the following PIN: " + pin);
                view.message("Memorise the following URL to connect for testing: " + url);
            } catch (Exception e) {
                System.out.println("Booking not successful");
            }
        }

        // Home (RAT obtain from facility)
        else if (view.getRatObtainLocation() == RatObtainLocation.ONSITE) {
            try {
                String url = urlService.generateConferenceURL();
                String qr = qrService.generateQRCode();
                String pin = api.postBooking(view.getCustomerId(), view.getSiteId(), dateFormat.parse(view.getDateString()), qr, url);

                // Display booking message
                view.message("\nBooking PIN generated, memorise the following PIN: " + pin);
                view.message("Memorise the following URL to connect for testing: " + url);
                view.message("Memorise the following QR Code to obtain RAT: " + qr);
            } catch (Exception e) {
                System.out.println("Booking not successful");
            }
        }
    }

    // Survey methods
    public void survey(){
        // Search for booking
        this.search();
        Booking booking = view.getModel();

        // Check if booking exists
        if (booking == null) {
            return;
        }

        // Display survey
        view.title("Test Survey System");
        view.survey();

        // Get survey information
        java.lang.String adminId = model.getCurrentUser().getId();
        java.lang.String patientId = view.getPatientId();

        if (adminId != null && patientId != null) {
            // Get symptom list and advise test
            ArrayList<Symptom> symptoms = view.getSymptoms();
            java.lang.String test = symptoms.size() >= 3 ? "PCR" : "RAT";
            view.message("The following test is advised: " + test);

            Api api = new ApiAdapter();
            try {
                api.patchBookingSymptoms(booking, symptoms);
                api.postCovidTest(test, patientId,
                        adminId,
                        booking.getBookingId(),
                        TestResult.PENDING.toString(),
                        TestStatus.CREATED.toString(),
                        null);
                api.patchBookingStatus(booking, BookingStatus.PROCESSED);
                booking.setStatus(BookingStatus.PROCESSED);
                view.message("Survey completed successfully");
            } catch (Exception e) {
                view.message("Survey not completed successfully");
            }
        } else {
            view.message("Missing required Information " + (adminId == null ? "adminId" : "") + " " + (patientId == null ? "patientId" : ""));
        }
    }

    // Modify booking methods
    public void modifySystem() {
        // Search for booking
        this.search();
        Booking booking = view.getModel();

        // Check if booking exists
        if (booking == null) {
            return;
        }

        // Start modifications
        view.title("Modify Booking");
        view.modifySystem();

        // Check if status allows modifications
        if (booking.getStatus() == BookingStatus.CANCELLED || booking.getStatus() == BookingStatus.LAPSED || booking.getStatus() == BookingStatus.COMPLETED) {
            view.message("Booking cannot be further modified");
            return;
        }

        Api api = new ApiAdapter();

        // Send request for reversion
        try {
            if (view.getReversion() != null) {
                // Check if reversion booking date is set in the future
                if (view.getReversion().getTiming().after(new Date())) {
                    booking.revertModification(view.getReversion());
                    api.patchBookingVenue(booking, view.getReversion().getVenue());
                    this.sendCustomerNotification(booking.getCustomerId(), "REVERSION - Booking ID: " + booking.getBookingId() + " at " + new Date());
                    this.sendStaffNotification(booking.getSiteId(), "REVERSION - Booking ID: " + booking.getBookingId() + " at " + new Date());
                    return;
                } else {
                    view.message("Booking cannot be reverted - option chosen must have future date");
                }
            }
        } catch (Exception e) {
            view.message("Booking not successfully reverted");
        }

        // Send request for cancellation
        try {
            if (view.getStatus() == BookingStatus.CANCELLED) {
                booking.setStatus(view.getStatus());
                api.patchBookingStatus(booking, view.getStatus());
                this.sendCustomerNotification(booking.getCustomerId(), "CANCELLATION - Booking ID: " + booking.getBookingId() + " at " + new Date());
                this.sendStaffNotification(booking.getSiteId(), "CANCELLATION - Booking ID: " + booking.getBookingId() + " at " + new Date());
                return;
            }
        } catch (Exception e) {
            view.message("Booking not successfully cancelled");
        }

        // Send request for modification
        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
            String oldSiteId = booking.getSiteId();
            String newSiteId = view.getSiteId();
            String newTiming = view.getDateString();
            String notification = null;

            // Save old booking
            if (newSiteId != null || newTiming != null) {
                booking.saveModification();
                notification = "CHANGED - Booking ID: " + booking.getBookingId() + " at " + new Date() + " - ";
            }

            // If booking venue changed
            if (newSiteId != null) {
                booking.setSiteId(newSiteId);
                api.patchBookingVenue(booking, newSiteId);
                notification += "Site ID: " + newSiteId + " ";
                view.message("Venue successfully modified");
            }

            // If booking timing changed
            if (newTiming != null) {
                booking.setStartTime(dateFormat.parse(newTiming));
                api.patchBookingTiming(booking, dateFormat.parse(newTiming));
                notification += "Time: " + dateFormat.parse(newTiming) + " ";
                view.message("Date/Time successfully modified");
            }

            // Send new modification history to API
            api.patchModification(booking);

            // Send notifications to receptionists
            this.sendStaffNotification(oldSiteId, notification);
            if (newSiteId != null) {
                this.sendStaffNotification(newSiteId, notification);
            }

            // Send notification to user
            this.sendCustomerNotification(booking.getCustomerId(), notification);

        } catch (Exception e) {
            view.message("Booking modifications not successful");
        }
    }

    public void modifyPhone() {
        // Search for booking
        this.search();
        Booking booking = view.getModel();

        // Check if booking exists
        if (booking == null) {
            return;
        }

        // Verify user
        view.verifyUser();
        if (!booking.getCustomerId().equals(view.getCustomerId())) {
            view.message("Unauthorised user");
            return;
        }

        // Start modifications
        view.title("Modify Booking");
        view.modifyPhone();

        // Check if status allows modifications
        if (booking.getStatus() == BookingStatus.CANCELLED || booking.getStatus() == BookingStatus.LAPSED || booking.getStatus() == BookingStatus.COMPLETED) {
            view.message("Booking cannot be further modified");
            return;
        }

        Api api = new ApiAdapter();

        // Send request for reversion
        try {
            if (view.getReversion() != null) {
                // Check if reversion booking date is set in the future
                if (view.getReversion().getTiming().after(new Date())) {
                    booking.revertModification(view.getReversion());
                    api.patchBookingVenue(booking, view.getReversion().getVenue());
                    this.sendCustomerNotification(booking.getCustomerId(), "REVERSION - Booking ID: " + booking.getBookingId() + " at " + new Date());
                    this.sendStaffNotification(booking.getSiteId(), "REVERSION - Booking ID: " + booking.getBookingId() + " at " + new Date());
                    return;
                } else {
                    view.message("Booking cannot be reverted - option chosen must have future date");
                }
            }
        } catch (Exception e) {
            view.message("Booking not successfully reverted");
        }

        // Send request for cancellation
        try {
            if (view.getStatus() == BookingStatus.CANCELLED) {
                booking.setStatus(view.getStatus());
                api.patchBookingStatus(booking, view.getStatus());
                this.sendCustomerNotification(booking.getCustomerId(), "CANCELLATION - Booking ID: " + booking.getBookingId() + " at " + new Date());
                this.sendStaffNotification(booking.getSiteId(), "CANCELLATION - Booking ID: " + booking.getBookingId() + " at " + new Date());
                return;
            }
        } catch (Exception e) {
            view.message("Booking not successfully cancelled");
        }

        // Send request for modification
        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
            String oldSiteId = booking.getSiteId();
            String newSiteId = view.getSiteId();
            String newTiming = view.getDateString();
            String notification = null;

            // Save old booking
            if (newSiteId != null || newTiming != null) {
                booking.saveModification();
                notification = "CHANGED - Booking ID: " + booking.getBookingId() + " at " + new Date() + " - ";
            }

            // If booking venue changed
            if (newSiteId != null) {
                booking.setSiteId(newSiteId);
                api.patchBookingVenue(booking, newSiteId);
                notification += "Site ID: " + newSiteId + " ";
                view.message("Venue successfully modified");
            }

            // If booking timing changed
            if (newTiming != null) {
                booking.setStartTime(dateFormat.parse(newTiming));
                api.patchBookingTiming(booking, dateFormat.parse(newTiming));
                notification += "Time: " + dateFormat.parse(newTiming) + " ";
                view.message("Date/Time successfully modified");
            }

            // Send new modification history to API
            api.patchModification(booking);

            // Send notifications to receptionists
            this.sendStaffNotification(oldSiteId, notification);
            if (newSiteId != null) {
                this.sendStaffNotification(newSiteId, notification);
            }

            // Send notification to user
            this.sendCustomerNotification(booking.getCustomerId(), notification);

        } catch (Exception e) {
            view.message("Booking modifications not successful");
        }
    }

    // Delete booking methods
    public void delete() {
        view.title("Delete Booking");

        // Search for booking
        this.search();
        Booking booking = view.getModel();

        // Check if booking exists
        if (booking == null) {
            return;
        }

        // Display page
        view.delete();

        // Send request
        Api api = new ApiAdapter();
        try {
            this.sendStaffNotification(booking.getSiteId(), "DELETION - Booking ID: " + booking.getBookingId() + " at " + new Date());
            api.deleteBooking(booking);
        } catch (Exception e) {
            view.message("Error in booking deletion");
        }
    }

    // Send notifications to receptionists at site about booking change
    private void sendStaffNotification(String siteId, String message) {
        Api api = new ApiAdapter();
        HashMap<String, User> users = model.getUserCollection().getUsers();

        // Send notifications to receptionists at site
        for (User user: users.values()) {
            if (siteId.equals(user.getEmployedSite())) {
                try {
                    user.addNotification(message);
                    user.getNotifications().get(0);
                    api.patchNotification(user);
                } catch (Exception e) {
                    view.message("Could not send notification to: " + user.getUsername());
                }
            }
        }
    }

    // Send notifications to customer whose booking changed
    private void sendCustomerNotification(String customerId, String message) {
        Api api = new ApiAdapter();
        User user = model.getUserCollection().getUserById(customerId);
        try {
            user.addNotification(message);
            api.patchNotification(user);
        } catch (Exception e) {
            view.message("Could not send notification to: " + user.getUsername());
        }
    }
}
