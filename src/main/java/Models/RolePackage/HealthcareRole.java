package Models.RolePackage;

import Actions.AdminPanelAction;
import Actions.SearchSiteAction;
import Actions.SurveyAction;

public class HealthcareRole extends Role {

    public HealthcareRole() {
        this.name = "Healthcare Worker";
        this.actions.add(new AdminPanelAction());
        this.actions.add(new SearchSiteAction());
        this.actions.add(new SurveyAction());
    }
}
