package Screens.SiteScreens;

import Models.SitePackage.Site;
import Models.SitePackage.SiteType;
import Models.DataModel;
import Screens.AbstractFactory;

import java.util.ArrayList;

public class SiteController {
    // Attributes
    private DataModel model;
    private SiteView view;
    private AbstractFactory factory;

    // Constructor
    public SiteController(AbstractFactory factory) {
        this.factory = factory;
        this.model = DataModel.getInstance();
        this.view = factory.getSiteView(null);
    }

    // Functionality
    public void search() {
        view.title("Searching Site System");

        // Get search criteria
        view.search();

        // Get site list
        ArrayList<Site> sites = new ArrayList<>();

        // Suburb
        if (view.getSuburb() != null) {
            String suburb = view.getSuburb();
            for (Site site: this.model.getSiteCollection().getSites()) {
                if (site.getAddress().getSuburb().equalsIgnoreCase(suburb)) {
                    sites.add(site);
                }
            }
        }

        // Facility type
        else if (view.getType() != null) {
            SiteType type = view.getType();
            for (Site site : this.model.getSiteCollection().getSites()) {
                if (site.getSiteTypes().contains(type)) {
                    sites.add(site);
                }
            }
        }

        // Display all
        else {
            sites = this.model.getSiteCollection().getSites();
        }

        view.displaySites(sites);
    }

}
