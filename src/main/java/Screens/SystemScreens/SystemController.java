package Screens.SystemScreens;

import Actions.Action;
import Models.DataModel;
import Screens.AbstractFactory;
import Services.ApiPackage.Api;
import Services.ApiPackage.ApiAdapter;

import java.util.ArrayList;

public class SystemController {
    // Attributes
    private DataModel model;
    private SystemView view;
    private AbstractFactory factory;

    // Constructor
    public SystemController(AbstractFactory factory) {
        this.factory = factory;
        this.model = DataModel.getInstance();
        this.view = factory.getSystemView(model);
    }

    // Login methods
    public void login() {
        view.title("Login System");

        // Get login form and attempt login
        while (true) {
            view.login();
            boolean success = this.authenticate(view.getUsername(), view.getPassword());

            // Display if successful
            if (success) {
                model.setCurrentUsername(view.getUsername());
                view.message("Login successful");
                return;
            }
        }
    }

    // Menu methods
    public void menu() {
        view.title("COVID Booking & Testing System");

        // Get action list
        ArrayList<Action> actions = model.getCurrentUser().getActions();

        // Display action menu and choose action
        int option = view.menu(actions);
        actions.get(option).execute();
    }

    // Private methods
    private boolean authenticate(String username, String password) {
        Api api = new ApiAdapter();
        try {
            api.login(username, password);
            return true;
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return false;
    }
}
