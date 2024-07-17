package Models.SitePackage;

import java.util.ArrayList;

public class SiteCollection {
    // Attributes
    private ArrayList<Site> sites = new ArrayList<>();

    // Getters
    public ArrayList<Site> getSites() {
        return sites;
    }

    public Site getSite(String id) {
        for (Site site: sites) {
            if (site.getId().equals(id)) {
                return site;
            }
        }
        return null;
    }

    // Update
    public void setSites(ArrayList<Site> sites) {
        this.sites = sites;
    }
}
