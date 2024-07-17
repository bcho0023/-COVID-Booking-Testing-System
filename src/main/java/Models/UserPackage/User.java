package Models.UserPackage;

import Actions.Action;
import Models.RolePackage.AdminRole;
import Models.RolePackage.CustomerRole;
import Models.RolePackage.HealthcareRole;
import Models.RolePackage.Role;

import java.util.ArrayList;

public class User {
    // Roles
    private ArrayList<Role> userRoles = new ArrayList<>();

    // Staff information
    private String id;
    private String givenName;
    private String familyName;
    private String username;
    private String phoneNumber;
    private boolean isCustomer;
    private boolean isAdmin;
    private boolean isHealthcareWorker;
    private String employedSite;
    private ArrayList<String> notifications;

    // Constructor
    public User(String id, String givenName, String familyName, String username, String phoneNumber, boolean isCustomer, boolean isAdmin, boolean isHealthcareWorker) {
        this.id = id;
        this.givenName = givenName;
        this.familyName = familyName;
        this.username = username;
        this.phoneNumber = phoneNumber;
        this.isCustomer = isCustomer;
        this.isAdmin = isAdmin;
        this.isHealthcareWorker = isHealthcareWorker;
        this.employedSite = null;
        this.notifications = new ArrayList<>();

        // Set user roles
        this.generateRoles();
    }

    // Getter
    public String getUsername() {
        return username;
    }
    public String getGivenName(){
        return givenName;
    }
    public String getFamilyName() {
        return familyName;
    }
    public String getPhoneNumber() {
        return phoneNumber;
    }
    public String getEmployedSite() {
        return employedSite;
    }
    public String getId() {
        return id;
    }
    public ArrayList<String> getNotifications() {
        return notifications;
    }

    //Setter
    public void setEmployedSite(String employedSite) {
        this.employedSite = employedSite;
    }

    public void addNotification(String notification) {
        this.notifications.add(notification);
    }

    // Generate user roles
    public void generateRoles() {
        if (this.isCustomer) {
            this.userRoles.add(new CustomerRole());
        }
        if (this.isAdmin) {
            this.userRoles.add(new AdminRole());
        }
        if (this.isHealthcareWorker) {
            this.userRoles.add(new HealthcareRole());
        }
    }

    // Get permissible action list
    public ArrayList<Action> getActions() {
        ArrayList<Action> actions = new ArrayList<>();
        for (Role role : this.userRoles) {
            for (Action action : role.getActions()) {
                if (!checkAction(actions, action)) {
                    actions.add(action);
                }
            }
        }
        return actions;
    }

    // Check if action list contains action
    private boolean checkAction(ArrayList<Action> list, Action target) {
        return list.stream().anyMatch(x -> target.getClass().isInstance(x));
    }
}
