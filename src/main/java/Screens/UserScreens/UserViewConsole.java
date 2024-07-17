package Screens.UserScreens;

import Actions.Action;
import Models.UserPackage.User;

import java.util.ArrayList;
import java.util.Scanner;

public class UserViewConsole extends UserView {
    // Constructor
    public UserViewConsole(User model) {
        super(model);
    }

    // General
    @Override
    public void title(String prompt) {
        System.out.println("\n" + prompt + "\n");
    }

    @Override
    public void message(String prompt) {
        System.out.println(prompt);
        System.out.flush();
    }

    private String input(String prompt) {
        Scanner scanner = new Scanner(System.in);
        System.out.print(prompt);
        return scanner.nextLine();
    }

    @Override
    public void displayBookings(String title, ArrayList<String> bookings) {
        System.out.println(title);
        for (int i = 0; i < bookings.size(); i++) {
            String booking = bookings.get(i);
            System.out.println(i + ". " + booking);
        }
    }

    @Override
    public int menu(ArrayList<Action> actions) {
        System.out.println("\nChoose option:");
        for (int i = 0; i < actions.size(); i++) {
            System.out.println(i + ". " + actions.get(i).name());
        }

        while (true) {
            try {
                int option = Integer.parseInt(this.input("Option: "));
                if (option < actions.size()) {
                    return option;
                } else {
                    throw new Exception();
                }
            } catch (Exception e) {
                System.out.println("Option not found");
            }
        }
    }

    // Profile methods
    @Override
    public void profile() {
        System.out.println("Details");
        System.out.println("Username: " + model.getUsername());
        System.out.println("Name: " + model.getGivenName() + " " + model.getFamilyName());
        System.out.println("User ID: " + model.getId());
        System.out.println("Phone Number: " + model.getPhoneNumber());
    }

    // Admin panel methods
    @Override
    public void adminPanel() {

    }

    // Notification methods
    @Override
    public void notifications() {
        // Print each notification message
        ArrayList<String> notifications = model.getNotifications();
        for (String message: notifications) {
            System.out.println(message);
        }
    }
}
