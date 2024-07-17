package Models.RolePackage;

import Actions.*;

public class AdminRole extends Role {

    public AdminRole() {
        this.name = "Administrator";
        this.actions.add(new AdminPanelAction());
        this.actions.add(new SearchSiteAction());
        this.actions.add(new SearchBookingAction());
        this.actions.add(new CreateBookingOnsiteAction());
        this.actions.add(new SurveyAction());
    }
}
