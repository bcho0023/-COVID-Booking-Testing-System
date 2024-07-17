package Screens.UserScreens;

import Actions.Action;
import Actions.DeleteBookingAction;
import Actions.ModifyBookingPhoneAction;
import Actions.ModifyBookingSystemAction;
import Actions.SearchBookingAction;
import Actions.ExitAction;
import Actions.NotificationAction;
import Models.BookingPackage.Booking;
import Models.SitePackage.Site;
import Models.DataModel;
import Screens.AbstractFactory;

import java.util.ArrayList;
import java.util.Date;

public class UserController {
    // Attributes
    private DataModel model;
    private UserView view;
    private AbstractFactory factory;

    // Constructor
    public UserController(AbstractFactory factory) {
        this.factory = factory;
        this.model = DataModel.getInstance();
        this.view = factory.getUserView(model.getCurrentUser());
    }

    // Profile methods
    public void profile() {
        view.title("User Profile");

        // Display user information
        view.profile();

        // Display user bookings
        view.displayBookings("\nActive Bookings", this.getActiveBookings());
        view.displayBookings("\nInactive Bookings", this.getInactiveBookings());

        // Possible actions from profile
        ArrayList<Action> actions = new ArrayList<>();
        actions.add(new ModifyBookingSystemAction());
        actions.add(new ExitAction());

        // Execute action
        int option = view.menu(actions);
        actions.get(option).execute();
    }

    // Admin panel methods
    public void adminPanel() {
        view.title("Admin Booking Interface");

        // Display admin panel
        view.adminPanel();

        // Display bookings at employed site
        view.displayBookings("Bookings At Current Site", this.getSiteBookings(model.getCurrentUser().getEmployedSite()));

        // Action list from admin panel
        ArrayList<Action> actions = new ArrayList<>();
        actions.add(new NotificationAction());
        actions.add(new SearchBookingAction());
        actions.add(new ModifyBookingPhoneAction());
        actions.add(new DeleteBookingAction());
        actions.add(new ExitAction());

        // Execute action
        int option = view.menu(actions);
        actions.get(option).execute();
    }

    // Notification methods
    public void notifications() {
        view.title("Notifications");

        // Display notifications
        view.notifications();
    }

    // Private methods
    private ArrayList<String> getActiveBookings() {
        String userId = this.model.getCurrentUser().getId();
        Date date = new Date();
        ArrayList<String> bookings = new ArrayList<>();

        // Get current user active bookings
        ArrayList<Site> sites = this.model.getSiteCollection().getSites();
        for (Site site : sites) {
            ArrayList<Booking> actives = site.getActiveBookings(date);
            for (Booking active : actives) {
                if (active.getCustomerId().equals(userId)) {
                    bookings.add(site.getName() + " (" + active.getStartTime() + ")");
                }
            }
        }

        // Return bookings
        return bookings;
    }

    private ArrayList<String> getInactiveBookings() {
        String userId = this.model.getCurrentUser().getId();
        Date date = new Date();
        ArrayList<String> bookings = new ArrayList<>();

        // Get current user active bookings
        ArrayList<Site> sites = this.model.getSiteCollection().getSites();
        for (Site site : sites) {
            ArrayList<Booking> inactives = site.getInactiveBookings(date);
            for (Booking inactive : inactives) {
                if (inactive.getCustomerId().equals(userId)) {
                    bookings.add(site.getName() + " (" + inactive.getStartTime() + ")");
                }
            }
        }

        // Return bookings
        return bookings;
    }

    private ArrayList<String> getSiteBookings(String siteId) {
        ArrayList<String> bookings = new ArrayList<>();

        Site site = model.getSiteCollection().getSite(siteId);
        if (siteId != null) {
            for (Booking booking : site.getBookings()) {
                bookings.add("Booking ID: " + booking.getBookingId() +
                        ", Customer ID: " + booking.getCustomerId() +
                        ", Status: " + booking.getStatus() +
                        ", Time: " + booking.getStartTime());
            }
        }

        // Return bookings
        return bookings;
    }
}
