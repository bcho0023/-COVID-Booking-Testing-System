package Screens.SystemScreens;

import Actions.Action;
import Models.DataModel;

import java.util.ArrayList;

public abstract class SystemView {
    // Attributes
    protected DataModel model;

    // Input attributes
    protected String username = null;
    protected String password = null;

    // Constructor
    public SystemView(DataModel model) {
        this.model = model;
    }

    // Getters
    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    // General
    public abstract void title(String prompt);

    public abstract  void message(String prompt);

    // Login methods
    public abstract void login();

    // Menu methods
    public abstract  int menu(ArrayList<Action> actions);
}
