package Screens.UserScreens;

import Actions.Action;
import Models.UserPackage.User;

import java.util.ArrayList;

public abstract class UserView {
    // Attributes
    protected User model;

    // Constructor
    public UserView(User model) {
        this.model = model;
    }

    // General
    public abstract void title(String prompt);

    public abstract void message(String prompt);

    public abstract void displayBookings(String title, ArrayList<String> bookings);

    public abstract int menu(ArrayList<Action> actions);

    // Profile methods
    public abstract void profile();

    // Admin panel methods
    public abstract void adminPanel();

    // Notification methods
    public abstract void notifications();

}
