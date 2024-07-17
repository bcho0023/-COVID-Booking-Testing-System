package Models.RolePackage;

import Actions.CreateBookingSystemAction;
import Actions.ModifyBookingSystemAction;
import Actions.SearchSiteAction;
import Actions.ProfileAction;
import Actions.SearchBookingAction;

public class CustomerRole extends Role {

    public CustomerRole() {
        this.name = "Customer";
        this.actions.add(new ProfileAction());
        this.actions.add(new SearchSiteAction());
        this.actions.add(new SearchBookingAction());
        this.actions.add(new CreateBookingSystemAction());
        this.actions.add(new ModifyBookingSystemAction());
    }
}
