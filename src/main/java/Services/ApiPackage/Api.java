package Services.ApiPackage;

import Models.BookingPackage.Booking;
import Models.BookingPackage.BookingStatus;
import Models.SitePackage.Site;
import Models.BookingPackage.Symptom;
import Models.UserPackage.User;

import java.util.ArrayList;
import java.util.Date;

public interface Api {
    String login(String username, String password) throws Exception;
    //boolean verifyToken(String token) throws Exception;
    ArrayList<User> getUsers() throws Exception;
    ArrayList<Site> getSites() throws Exception;
    void postCovidTest(String type, String patientId, String adminId, String bookingId, String result, String status, String notes) throws Exception;
    String postBooking(String customerId, String siteId, Date date, String qr, String url) throws Exception;
    void patchBookingSymptoms(Booking booking, ArrayList<Symptom> symptoms) throws Exception;
    void patchBookingStatus(Booking booking, BookingStatus status) throws Exception;
    void patchBookingVenue(Booking booking, String siteId) throws Exception;
    void patchBookingTiming(Booking booking, Date date) throws Exception;
    void patchNotification(User user) throws Exception;
    void patchModification(Booking booking) throws Exception;
    void deleteBooking(Booking booking) throws Exception;
}
