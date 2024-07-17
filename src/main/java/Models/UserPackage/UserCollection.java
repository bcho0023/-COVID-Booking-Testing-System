package Models.UserPackage;

import java.util.ArrayList;
import java.util.HashMap;

public class UserCollection {
    // Attributes
    private HashMap<String, User> users = new HashMap<>();

    // Getters
    public HashMap<String, User> getUsers() {
        return users;
    }

    public User getUserByUsername(String username) {
        return users.get(username);
    }

    public User getUserById(String id) {
        for (User user: users.values()) {
            if (id.equals(user.getId())) {
                return user;
            }
        }
        return null;
    }

    // Update
    public void setUsers(ArrayList<User> users) {
        this.users.clear();
        for (User user : users) {
            this.users.put(user.getUsername(), user);
        }
    }
}
