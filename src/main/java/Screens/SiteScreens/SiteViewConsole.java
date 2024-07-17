package Screens.SiteScreens;

import Models.SitePackage.Site;
import Models.SitePackage.SiteType;

import java.util.ArrayList;
import java.util.Locale;
import java.util.Scanner;

public class SiteViewConsole extends SiteView {
    // Constructor
    public SiteViewConsole(Site model) {
        super(model);
    }

    // General
    @Override
    public void title(String prompt) {
        System.out.println("\n" + prompt + "\n");
    }

    private String input(String prompt) {
        Scanner scanner = new Scanner(System.in);
        System.out.print(prompt);
        return scanner.nextLine();
    }

    // Search methods
    @Override
    public void displaySites(ArrayList<Site> sites) {
        System.out.println();
        for (Site site: sites) {
            System.out.println(site + "\n");
        }
    }

    @Override
    public void search() {
        System.out.println("Choose search option: ");
        System.out.println("1. Search by Suburb");
        System.out.println("2. Search by Facility Type");
        System.out.println("3. Display All");

        int option;
        while (true) {
            try {
                option = Integer.parseInt(this.input("Option: "));
                if (option == 1 || option == 2 || option == 3) {
                    break;
                } else {
                    throw new Exception();
                }
            } catch (Exception e){
                System.out.println("Option not found");
            }
        }

        if (option == 1) {
            suburb = this.input("Enter suburb: ");
            return;
        }

        else if (option == 2) {
            // Options
            System.out.println("Type options:");
            for (SiteType type : SiteType.values()) {
                System.out.println(type);
            }

            // Enter option
            while (true) {
                try {
                    // Check if valid siteType
                    String input = this.input("Option: ").toUpperCase(Locale.ROOT);
                    type = SiteType.valueOf(input);
                    return;
                } catch (Exception e) {
                    System.out.println("No such type found");
                }
            }
        }

    }
}
