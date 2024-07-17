import Actions.LoginAction;
import Actions.MenuAction;
import Models.DataModel;
import Services.ApiPackage.Api;
import Services.ApiPackage.ApiAdapter;

public class Main {
    public static void main(String[] args) {
        // Login
        update();
        new LoginAction().execute();

        // Menu
        while (true) {
            update();
            new MenuAction().execute();
        }
    }

    // Update model
    private static void update() {
        Api api = new ApiAdapter();

        // Update users
        try {
            DataModel.getInstance().getUserCollection().setUsers(api.getUsers());
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        // Update sites
        try {
            DataModel.getInstance().getSiteCollection().setSites(api.getSites());
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
}

