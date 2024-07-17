package Screens.BookingScreens;

import Models.BookingPackage.Booking;
import Models.BookingPackage.BookingStatus;
import Models.BookingPackage.Symptom;
import Models.BookingPackage.BookingMemento;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Scanner;

public class BookingViewConsole extends BookingView {
    // Constructor
    public BookingViewConsole(Booking model) {
        super(model);
    }

    // General
    @Override
    public void title(String prompt) {
        System.out.println("\n" + prompt + "\n");
    }

    @Override
    public void message(String prompt) {
        System.out.println(prompt);
    }

    private String input(String prompt) {
        Scanner scanner = new Scanner(System.in);
        System.out.print(prompt);
        return scanner.nextLine();
    }

    private String generateDateString(String date, String datePattern, String time, String timePattern) throws Exception {
        // Validate date format
        SimpleDateFormat dateFormat = new SimpleDateFormat(datePattern);
        dateFormat.parse(date);

        // Validate time format
        SimpleDateFormat timeFormat = new SimpleDateFormat(timePattern);
        timeFormat.parse(time);

        // UTC Format yyyy-MM-dd'T'HH:mm:ss.SSS'Z' (2022-05-12T13:23:00.760Z)
        return date + 'T' + time + ":00.000Z";
    }

    // Display booking
    @Override
    public void displayBooking() {
        if (this.model != null) {
            System.out.println(this.model);;
        } else {
            System.out.println("No Booking Found");
        }
    }

    // Search booking methods
    @Override
    public void search() {
        System.out.println("Choose option:");
        System.out.println("1. Search using PIN");
        System.out.println("2. Search using booking ID");

        // Choose search type
        int searchOption;
        while (true) {
            try {
                searchOption = Integer.parseInt(this.input("Option: "));
                if (searchOption == 1 || searchOption == 2) {
                    break;
                } else {
                    throw new Exception();
                }
            } catch (Exception e) {
                System.out.println("Option not found");
            }
        }

        // Fill form
        if (searchOption == 1) {
            pin = this.input("Enter Booking PIN: ");
            return;
        }

        else if (searchOption == 2) {
            bookingId = this.input("Enter Booking ID: ");
            return;
        }
    }

    // Create booking methods
    @Override
    public void createOnSite() {
        // Fill form
        customerId = this.input("Enter customer ID: ");
        siteId = this.input("Enter site ID: ");

        // Fill date
        String datePattern = "yyyy-MM-dd";
        String timePattern = "HH:mm";

        while (true) {
            String dateInput = input("Enter the date in the format " + datePattern + "(2022-05-12): ");
            String timeInput = this.input("Enter the time in the 24hour format " + timePattern + " (20:17): ");

            try {
                dateString = this.generateDateString(dateInput, datePattern, timeInput, timePattern);
                break;
            } catch (Exception e) {
                System.out.println("Incorrect date/time format");
            }
        }

    }

    @Override
    public void createSystem() {
        // Get intended test location
        System.out.println("Choose option:");
        System.out.println("1. Get tested at the facility");
        System.out.println("2. Get tested at home");

        // Choose test location
        int locationOption;
        while (true) {
            try {
                locationOption = Integer.parseInt(this.input("Option: "));
                if (locationOption == 1 || locationOption == 2) {
                    break;
                } else {
                    throw new Exception();
                }
            } catch (Exception e) {
                System.out.println("Option not found");
            }
        }

        // Fill form
        if (locationOption == 1) {
            customerId = this.input("Enter customer ID: ");
            siteId = this.input("Enter site ID: ");

            // Fill date
            String datePattern = "yyyy-MM-dd";
            String timePattern = "HH:mm";

            while (true) {
                String dateInput = input("Enter the date in the format " + datePattern + "(2022-05-12): ");
                String timeInput = this.input("Enter the time in the 24hour format " + timePattern + " (20:17): ");

                try {
                    dateString = this.generateDateString(dateInput, datePattern, timeInput, timePattern);
                    break;
                } catch (Exception e) {
                    System.out.println("Incorrect date/time format");
                }
            }

            return;
        }

        else if (locationOption == 2) {
            customerId = this.input("Enter customer ID: ");
            siteId = this.input("Enter site ID: ");

            // Fill date
            String datePattern = "yyyy-MM-dd";
            String timePattern = "HH:mm";

            while (true) {
                String dateInput = input("Enter the date in the format " + datePattern + "(2022-05-12): ");
                String timeInput = this.input("Enter the time in the 24hour format " + timePattern + " (20:17): ");

                try {
                    dateString = this.generateDateString(dateInput, datePattern, timeInput, timePattern);
                    break;
                } catch (Exception e) {
                    System.out.println("Incorrect date/time format");
                }
            }

            // Choose how RAT will be obtained
            System.out.println("Choose option:");
            System.out.println("1. Obtain RAT from facility");
            System.out.println("2. Supply own RAT");

            int ratOption;
            while (true) {
                try {
                    ratOption = Integer.parseInt(this.input("Option: "));
                    if (ratOption == 1 || ratOption == 2) {
                        break;
                    } else {
                        throw new Exception();
                    }
                } catch (Exception e) {
                    System.out.println("Option not found");
                }
            }

            if (ratOption == 1) {
                ratObtainLocation = RatObtainLocation.ONSITE;
                return;
            }

            else if (ratOption == 2) {
                ratObtainLocation = RatObtainLocation.HOME;
                return;
            }
        }
    }

    // Survey methods
    @Override
    public void survey() {
        // Fill form
        patientId = this.input("Enter patient ID: ");

        // Print symptom options
        System.out.println("Add patient symptoms:");
        for (Symptom symptom: Symptom.values()){
            System.out.println(symptom.toString());
        }
        System.out.println("Type 'SKIP' if no symptoms to add or close-contact");

        // Identify symptoms
        symptoms = new ArrayList<>();
        while (true) {
            // Input next symptom
            String input = this.input("Add symptom: ");

            // End symptom input method if skip chosen
            if (input.equals("SKIP")) {
                break;
            }

            // Try adding symptom to list
            try {
                Symptom symptom = Symptom.valueOf(input);
                if (!symptoms.contains(symptom)) {
                    symptoms.add(symptom);
                } else {
                    System.out.println("This symptom has already been added");
                }
            } catch (Exception e) {
                System.out.println("Symptom '" + input + "' not found");
            }
        }
    }

    // Modify methods
    @Override
    public void modifySystem() {
        // Choose type of modification
        int modifyOption;
        while (true) {
            System.out.println("Choose Options");
            System.out.println("1. Modify booking venue");
            System.out.println("2. Modify booking time");
            System.out.println("3. Cancel booking");
            System.out.println("4. Revert booking");
            System.out.println("5. Exit");

            // Choose option
            while (true) {
                try {
                    modifyOption = Integer.parseInt(this.input("Option: "));
                    if (modifyOption == 1 || modifyOption== 2 || modifyOption == 3 || modifyOption == 4 || modifyOption == 5) {
                        break;
                    } else {
                        throw new Exception();
                    }
                } catch (Exception e) {
                    System.out.println("Option not found");
                }
            }

            // Modify based on option chosen
            switch (modifyOption) {
                case 1 -> {
                    siteId = this.input("Enter new venue ID: ");
                }

                case 2 -> {
                    java.lang.String datePattern = "yyyy-MM-dd";
                    java.lang.String timePattern = "HH:mm";

                    while (true) {
                        java.lang.String dateInput = this.input("Enter the new date in the format " + datePattern + "(2022-05-12): ");
                        java.lang.String timeInput = this.input("Enter the new time in the 24hour format " + timePattern + " (20:17): ");

                        try {
                            dateString = this.generateDateString(dateInput, datePattern, timeInput, timePattern);
                            break;
                        } catch (Exception e) {
                            System.out.println("Incorrect date/time format");
                        }
                    }
                }

                case 3 -> {
                    status = BookingStatus.CANCELLED;
                }

                case 4 -> {
                    int historyOption;
                    while (true) {
                        // Display modifications
                        ArrayList<BookingMemento> changes = model.getModifications();
                        System.out.println("Modification History");
                        for(int i = 0; i < changes.size(); i++){
                            System.out.println(i + ". " + changes.get(i));
                        }

                        // Choose option
                        try {
                            historyOption = Integer.parseInt(this.input("Change Option: "));
                            if (0 <= historyOption && historyOption < changes.size()) {
                                reversion = changes.get(historyOption);
                                break;
                            } else {
                                throw new Exception();
                            }
                        } catch (Exception e){
                            System.out.println("Option not found");
                        }
                    }
                }

                case 5 -> {
                    return;
                }
            }
        }
    }

    @Override
    public void modifyPhone() {
        // Choose type of modification
        int modifyOption;
        while (true) {
            System.out.println("Choose Options");
            System.out.println("1. Modify booking venue");
            System.out.println("2. Modify booking time");
            System.out.println("3. Cancel booking");
            System.out.println("4. Revert booking");
            System.out.println("5. Exit");

            while (true) {
                try {
                    modifyOption = Integer.parseInt(this.input("Option: "));
                    if (modifyOption == 1 || modifyOption== 2 || modifyOption == 3 || modifyOption == 4 || modifyOption == 5) {
                        break;
                    } else {
                        throw new Exception();
                    }
                } catch (Exception e) {
                    System.out.println("Option not found");
                }
            }

            // Modify based on option chosen
            switch (modifyOption) {
                case 1 -> {
                    siteId = this.input("Enter new venue ID: ");
                }

                case 2 -> {
                    String datePattern = "yyyy-MM-dd";
                    String timePattern = "HH:mm";

                    while (true) {
                        String dateInput = this.input("Enter the new date in the format " + datePattern + "(2022-05-12): ");
                        String timeInput = this.input("Enter the new time in the 24hour format " + timePattern + " (20:17): ");

                        try {
                            dateString = this.generateDateString(dateInput, datePattern, timeInput, timePattern);
                            break;
                        } catch (Exception e) {
                            System.out.println("Incorrect date/time format");
                        }
                    }
                }

                case 3 -> {
                    status = BookingStatus.CANCELLED;
                }

                case 4 -> {
                    ArrayList<BookingMemento> changes = model.getModifications();

                    // Display previous modifications
                    System.out.println("\nOld Bookings");

                    for (int i = 0; i < changes.size(); i++) {
                        System.out.println(i + ". " + changes.get(i));
                    }

                    // Choose option
                    int historyOption;
                    while (true) {
                        try {
                            historyOption = Integer.parseInt(this.input("Change Option: "));
                            if (historyOption >= 0 && historyOption < changes.size()) {
                                reversion = changes.get(historyOption);
                                return;
                            } else {
                                throw new Exception();
                            }
                        } catch (Exception e) {
                            System.out.println("Option not found");
                            break;
                        }
                    }
                }

                case 5 -> {
                    return;
                }
            }
        }
    }

    // Delete methods
    @Override
    public void delete() {

    }

    // Other methods
    @Override
    public void verifyUser() {
        customerId = this.input("Enter user ID: ");
    }
}
