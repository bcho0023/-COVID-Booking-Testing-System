package Screens.SiteScreens;

import Models.SitePackage.Site;
import Models.SitePackage.SiteType;

import java.util.ArrayList;

public abstract class SiteView {
    protected Site model;

    // Input attributes
    protected String suburb = null;
    protected SiteType type = null;

    // Constructor
    public SiteView(Site model) {
        this.model = model;
    }

    // Getters
    public String getSuburb() {
        return suburb;
    }

    public SiteType getType() {
        return type;
    }

    // General
    public abstract void title(String prompt);

    // Search methods
    public abstract void displaySites(ArrayList<Site> sites);

    public abstract void search();
}
