package Models;

import Models.SitePackage.SiteCollection;
import Models.UserPackage.User;
import Models.UserPackage.UserCollection;

public class DataModel {
    // Attributes
    private UserCollection userCollection = new UserCollection();
    private SiteCollection siteCollection = new SiteCollection();
    private String currentUsername = null;

    // Singleton pattern
    private static DataModel instance = null;
    private DataModel() {}
    public static DataModel getInstance() {
        if (instance == null) {
            instance = new DataModel();
        }
        return instance;
    }

    // Getters
    public UserCollection getUserCollection() {
        return userCollection;
    }

    public SiteCollection getSiteCollection() {
        return siteCollection;
    }

    public User getCurrentUser() {
        return userCollection.getUserByUsername(this.currentUsername);
    }

    // Setters
    public void setCurrentUsername(String username) {
        this.currentUsername = username;
    }
}
