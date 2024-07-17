package Models.RolePackage;

import Actions.Action;

import java.util.ArrayList;

public abstract class Role {
    // Role information
    protected String name;
    protected ArrayList<Action> actions = new ArrayList<>();

    // Methods
    public String name() {
        return this.name;
    }
    
    public ArrayList<Action> getActions() {
        return this.actions;
    }
}
