package Services.ApiPackage;

import Models.BookingPackage.BookingMemento;
import Models.BookingPackage.Symptom;
import Models.SitePackage.*;
import Models.BookingPackage.Booking;
import Models.BookingPackage.BookingStatus;
import Models.TestPackage.*;
import Models.UserPackage.User;
import com.fasterxml.jackson.databind.JsonNode;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.google.gson.*;

import java.io.File;
import java.io.FileNotFoundException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.DayOfWeek;
import java.util.ArrayList;
import java.util.Date;
import java.util.Scanner;
import java.util.TimeZone;

public class ApiAdapter implements Api {
    // API Information
    //Read API Key From File
    private final String APIKEY = this.readFileKey("key.txt");
    //private final String ROOTURL = "https://fit3077.com/api/v2/";
    private final String ROOTURL = "http://localhost:3000";

    // Parse date string
    private Date dateBuilder(String date) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        dateFormat.setTimeZone(TimeZone.getTimeZone("Australia/Sydney"));
        try {
            return dateFormat.parse(date);
        } catch (ParseException e) {
            System.out.println(e.getMessage());
            return null;
        }
    }

    // Create date string
    private String dateStringBuilder(Date date) {
        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
            dateFormat.setTimeZone(TimeZone.getTimeZone("Australia/Sydney"));
            return dateFormat.format(date);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return null;
        }
    }

    @Override
    public String login(String username, String password) throws Exception {
        /* 
        // JSON
        String jsonString = "{" +
                "\"userName\":\"" + username + "\"," +
                "\"password\":\"" + password + "\"" +
                "}";

        // Request construction
        String loginUrl = this.ROOTURL + "/user/login";
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder(URI.create(loginUrl + "?jwt=true"))
                .setHeader("Authorization", this.APIKEY)
                .header("Content-Type","application/json") // This header needs to be set when sending a JSON request body.
                .POST(HttpRequest.BodyPublishers.ofString(jsonString))
                .build();

        // Send request
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        // Error handling
        if (response.statusCode() == 403) {
            throw new Exception("User credentials are invalid");
        }

        // Extract jwt
        JsonObject gson = new Gson().fromJson(response.body(), JsonObject.class);
        */

        if(username.equals(password))
            return  "Test-Sucess"; // gson.get("jwt").getAsString();
        else
            throw new Exception("User credentials are invalid");
    }

    /* 
    @Override
    public boolean verifyToken(String token) throws Exception {
        // JSON
        String jsonString = "{\"jwt\":\"" + token + "\"}";

        // Request construction
        String tokenUrl = this.ROOTURL + "/user/verify-token";
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder(URI.create(tokenUrl))
                .setHeader("Authorization", this.APIKEY)
                .header("Content-Type","application/json")
                .POST(HttpRequest.BodyPublishers.ofString(jsonString))
                .build();

        // Send request
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        // Handle response
        return response.statusCode() == 200;
    }
    */
    @Override
    public ArrayList<User> getUsers() throws Exception {
        // Request construction
        String userUrl = this.ROOTURL + "/users";
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest
                .newBuilder(URI.create(userUrl))
                .setHeader("Authorization", this.APIKEY)
                .GET()
                .build();

        // Send request
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        // Error handling
        if (response.statusCode() != 200) {
            throw new Exception("An error has occurred");
        }

        // Extract each user from list
        ObjectNode[] nodes = new ObjectMapper().readValue(response.body(), ObjectNode[].class);
        // Convert user to required form
        ArrayList<User> users = new ArrayList<>();
        for (ObjectNode node : nodes) {
            User user = new User(
                    node.get("id").asText(),
                    node.get("givenName").asText(),
                    node.get("familyName").asText(),
                    node.get("userName").asText(),
                    node.get("phoneNumber").asText(),
                    node.get("isCustomer").asBoolean(),
                    node.get("isReceptionist").asBoolean(),
                    node.get("isHealthcareWorker").asBoolean()
            );
            users.add(user);
            employedSiteBuilder(node, user);
            notificationsBuilder(node, user);
        }


        // Return users
        return users;
    }

    // Parse employed site from json
    private void employedSiteBuilder(JsonNode node, User user) {
        String additionalInfoEmployedSite = node.at("/additionalInfo/employedSite").asText();

        if (!additionalInfoEmployedSite.isBlank()) {
            user.setEmployedSite(additionalInfoEmployedSite);
        }
    }

    // Parse notifications from json
    private void notificationsBuilder(JsonNode node, User user) {
        JsonNode subnode = node.get("additionalInfo").get("notifications");
        if (subnode != null) {
            // For each notification
            JsonArray notificationsArray = JsonParser.parseString(subnode.toString()).getAsJsonArray();
            for (JsonElement notification: notificationsArray) {
                user.addNotification(notification.getAsString());
            }
        }
    }

    @Override
    public ArrayList<Site> getSites() throws Exception {
        // Request construction
        //String siteUrl = this.ROOTURL + "/testing-site?fields=bookings.covidTests";
        String siteUrl = this.ROOTURL + "/sites";
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder(URI.create(siteUrl))
                .setHeader("Authorization", this.APIKEY)
                .GET()
                .build();

        // Send request
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        // Error handling
        if (response.statusCode() != 200) {
            throw new Exception("An error has occurred");
        }


        // Extract each user from list
        ObjectNode[] nodes = new ObjectMapper().readValue(response.body(), ObjectNode[].class);

        // Convert sites to required form
        ArrayList<Site> sites = new ArrayList<>();
        for (ObjectNode node : nodes) {
            sites.add(siteBuilder(node));
        }

        // Return sites
       
        return sites;
    }

    // Parse site from json
    private Site siteBuilder(JsonNode node) throws Exception {
        String category = node.at("/additionalInfo/category").asText();
        String siteId = node.get("id").asText();
        String siteName = node.get("name").asText();
        String desc =  node.get("description").asText();
        String webURL =  node.get("websiteUrl").asText();
        String phoneNumber = node.get("phoneNumber").asText();
        Address addr = this.addressBuilder(node.get("address"));
        String addInfo = node.get("additionalInfo").asText();
        Site site;
        if (category.equalsIgnoreCase("Clinic"))
            site = new Clinic(siteId,siteName,desc,webURL,phoneNumber,addr,addInfo);
        else if (category.equalsIgnoreCase("Pathology"))
            site = new Pathology(siteId,siteName,desc,webURL,phoneNumber,addr,addInfo);
        else if (category.equalsIgnoreCase("Hospital"))
            site = new Hospital(siteId,siteName,desc,webURL,phoneNumber,addr,addInfo);
        else if (category.equalsIgnoreCase("TestingSite"))
            site = new TestingSite(siteId,siteName,desc,webURL,phoneNumber,addr,addInfo);
        else if (category.equalsIgnoreCase("GP"))
            site = new GP(siteId,siteName,desc,webURL,phoneNumber,addr,addInfo);
        else
            site = new Site(siteId,siteName,desc,webURL,phoneNumber,addr,addInfo);
        site.setBookings(this.bookingListBuilder(node.get("bookings"), node.get("id").asText()));
        siteTypeListBuilder(node, site);
        operatingHourListBuilder(node, site);
        
        // Return site
        return site;
    }

    // Parse address from json
    private Address addressBuilder(JsonNode node) {
        Address address = new Address(
                node.get("latitude").asText(),
                node.get("longitude").asText(),
                node.get("unitNumber").asText(),
                node.get("street").asText(),
                node.get("street2").asText(),
                node.get("suburb").asText(),
                node.get("state").asText(),
                node.get("postcode").asText(),
                node.get("additionalInfo").asText()
        );

        // Return address
        return address;
    }

    // Parse site type from json
    private void siteTypeListBuilder(JsonNode node, Site site) {
        // Get AdditionalInfo Types
        JsonNode additionalInfoTypes = node.get("additionalInfo").get("types");
        if(additionalInfoTypes != null) {
            JsonArray typesArray = JsonParser.parseString(additionalInfoTypes.toString()).getAsJsonArray();

            for (int i = 0; i < typesArray.size(); i++)
                site.addSiteType(SiteType.valueOf(typesArray.get(i).getAsString()));

        }
    }

    // Parse operating hours from json
    private void operatingHourListBuilder(JsonNode node, Site site) {
        // Get AdditionalInfo OperatingHours
        JsonNode additionalInfoOperatingHours = node.at("/additionalInfo/operatingHours");

        for (int i = 1; i <= DayOfWeek.values().length;i++){
            JsonNode jsonNode = additionalInfoOperatingHours.get((DayOfWeek.of(i).toString()));
            OperatingHour operatingHours = new OperatingHour(jsonNode.get("openingHour").asText(), jsonNode.get("closingHour").asText());
            site.setOperatingHour(i, operatingHours);
        }
    }

    // Parse booking list from json
    private ArrayList<Booking> bookingListBuilder(JsonNode node, String siteId) throws Exception {
        ArrayList<Booking> bookings = new ArrayList<>();



        for (JsonNode sub : node) {
            Booking booking = new Booking(
                    sub.get("id").asText(),
                    sub.get("customerId").asText(),
                    siteId,
                    dateBuilder(sub.get("startTime").asText()),
                    sub.get("smsPin").asText(),
                    BookingStatus.valueOf(sub.get("status").asText()),
                    this.testListBuilder(sub.get("id").asText()),
                    sub.get("notes").asText(),
                    sub.get("additionalInfo").toString()
            );

            if ((dateBuilder(sub.get("startTime").asText()).before(new Date())) ) {
                booking.setStatus(BookingStatus.LAPSED);
                System.out.println("LAPSED");
                try {
                    this.patchBookingStatus(booking, BookingStatus.LAPSED);
                }catch (Exception e){
                    System.out.println("Error status patching");
                }
            }
            
            symptomListBuilder(sub, booking);
            modificationsBuilder(sub, booking);
            bookings.add(booking);
        }
        

        // Return bookings
        return bookings;
    }

    // Parse test list from json
    private ArrayList<Test> testListBuilder(String siteId) throws Exception {
        ArrayList<Test> tests = new ArrayList<>();
        TestFactory testFactory = new TestFactory();

        // Request construction
        //String siteUrl = this.ROOTURL + "/testing-site?fields=bookings.covidTests";
        String bookingUrl = this.ROOTURL + "/bookings?siteId=" + siteId;
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder(URI.create(bookingUrl))
                .setHeader("Authorization", this.APIKEY)
                .GET()
                .build();

        // Send request
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        // Error handling
        if (response.statusCode() != 200) {
            throw new Exception("An error has occurred");
        }

        // Extract each user from list
        ObjectNode[] bookingNodes = new ObjectMapper().readValue(response.body(), ObjectNode[].class);
        
        for (JsonNode subBookingNode : bookingNodes) {
            JsonNode testNodes = subBookingNode.get("tests");
            for(JsonNode subTest: testNodes){
                Test test = testFactory.getTest(
                    subTest.get("type").asText(),
                    subTest.get("id").asText(),
                    subTest.get("patientId").asText(),
                    subTest.get("administererId").asText(), 
                    TestResult.valueOf(subTest.get("result").asText()),
                    TestStatus.valueOf(subTest.get("status").asText()),
                    subTest.get("notes").asText(),
                    subTest.get("additionalInfo").asText()
                );
                tests.add(test);
            }
        }
        

        // Return tests
        return tests;
    }
    // Parse symptoms from json
    private void symptomListBuilder(JsonNode node, Booking booking){
        String additionalInfoSymptoms = node.at("/additionalInfo/symptoms").asText();

        if (!additionalInfoSymptoms.isBlank()) {
            JsonArray symptomsArray = JsonParser.parseString(additionalInfoSymptoms).getAsJsonArray();

            for (int i = 0; i < symptomsArray.size(); i++)
                booking.addSymptoms(Symptom.valueOf(symptomsArray.get(i).getAsString()));
        }
    }

    // Parse modifications from json
    private void modificationsBuilder(JsonNode node, Booking booking) {
        JsonNode subnode = node.get("additionalInfo").get("modifications");

        if (subnode != null) {
            // For each modification
            JsonArray modificationsArray = JsonParser.parseString(subnode.toString()).getAsJsonArray();
            for (JsonElement modification : modificationsArray) {
                JsonObject change = modification.getAsJsonObject();
                String venue = change.get("venue").getAsString();
                Date timing = dateBuilder(change.get("timing").getAsString());
                Date modifyDate = dateBuilder(change.get("modifyDate").getAsString());
                BookingMemento memento = new BookingMemento(venue, timing, modifyDate);
                booking.addModification(memento);
            }
        }
    }

    @Override
    public void postCovidTest(String type, String patientId, String adminId, String bookingId, String result, String status, String notes) throws Exception {
        String covidTestURL = this.ROOTURL + "/tests";

        // Create JSON
        JsonObject testJson = new JsonObject();
        testJson.addProperty("type", type);
        testJson.addProperty("patientId", patientId);
        testJson.addProperty("administererId", adminId);
        testJson.addProperty("bookingId", bookingId);
        testJson.addProperty("result", result);
        testJson.addProperty("status", status);
        testJson.addProperty("notes", notes);

        JsonObject additionalInfoJson = new JsonObject();
        testJson.add("additionalInfo", additionalInfoJson);

        // Request construction
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder(URI.create(covidTestURL + "?jwt=true"))
                .setHeader("Authorization", this.APIKEY)
                .header("Content-Type","application/json")
                .POST(HttpRequest.BodyPublishers.ofString(testJson.toString()))
                .build();

        // Send request
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        // Error handling
        if (response.statusCode() == 404) {
            throw new Exception("A patient, administerer, and/or booking with the provided ID was not found");
        } else if (response.statusCode() == 400) {
            throw new Exception("JSON Mistake");
        }
    }

    @Override
    public String postBooking(String customerId, String siteId, Date date, String qrCode, String conferenceUrl) throws Exception {
        String bookingURL = this.ROOTURL + "/bookings";

        // Create JSON
        JsonObject testJson = new JsonObject();
        testJson.addProperty("customerId", customerId);
        testJson.addProperty("siteId", siteId);
        testJson.addProperty("startTime", dateStringBuilder(date));

        // Optional JSON parameters
        JsonObject additionalInfoJson = new JsonObject();
        if (qrCode != null) {
            additionalInfoJson.addProperty("qrCode", qrCode);
        }
        if (conferenceUrl != null) {
            additionalInfoJson.addProperty("conferenceUrl", conferenceUrl);
        }

        testJson.add("additionalInfo", additionalInfoJson);

        // Request construction
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder(URI.create(bookingURL + "?jwt=true"))
                .setHeader("Authorization", this.APIKEY)
                .header("Content-Type","application/json")
                .POST(HttpRequest.BodyPublishers.ofString(testJson.toString()))
                .build();

        // Send request
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        // Error handling
        if (response.statusCode() == 404) {
            throw new Exception("A customer or site with the provided ID was not found");
        } else if (response.statusCode() == 400) {
            throw new Exception("JSON Mistake");
        }

        JsonObject gson = new Gson().fromJson(response.body(), JsonObject.class);
        return gson.get("smsPin").getAsString();
    }

    @Override
    public void patchBookingSymptoms(Booking booking, ArrayList<Symptom> symptoms) throws Exception{
        String bookingURL = this.ROOTURL + "/booking/" + booking.getBookingId();

        // Check if current json already has symptoms
        String currentInfo = booking.getAdditionalInfo();
        JsonObject additionalInfoJson = new Gson().fromJson(currentInfo, JsonObject.class);

        if (additionalInfoJson.has("symptoms")) {
            additionalInfoJson.remove("symptoms");
        }

        // Add symptoms to additional info
        JsonArray symptomsJsonArray = new JsonArray();
        for (Symptom symptom: symptoms)
            symptomsJsonArray.add(symptom.toString());
        additionalInfoJson.add("symptoms", symptomsJsonArray);

        // Add additional info to booking
        JsonObject bookingJson = new JsonObject();
        bookingJson.add("additionalInfo", additionalInfoJson);

        // Request construction
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder(URI.create(bookingURL + "?jwt=true"))
                .setHeader("Authorization", this.APIKEY)
                .header("Content-Type","application/json")
                .method("PATCH", HttpRequest.BodyPublishers.ofString(bookingJson.toString()))
                .build();

        // Send request
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        // Error handling
        if (response.statusCode() == 404) {
            throw new Exception("A booking, customer or site with the provided ID was not found");
        } else if (response.statusCode() == 400) {
            throw new Exception("JSON Mistake");
        }
    }

    @Override
    public void patchBookingStatus(Booking booking, BookingStatus status) throws Exception{
        String bookingURL = this.ROOTURL + "/booking/" + booking.getBookingId();


        //JsonObject to patch booking
        JsonObject bookingJson = new JsonObject();

        //Add Status to booking jsonObject
        bookingJson.addProperty("status", status.toString());

        // Request construction
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder(URI.create(bookingURL + "?jwt=true"))
                .setHeader("Authorization", this.APIKEY)
                .header("Content-Type","application/json")
                .method("PATCH", HttpRequest.BodyPublishers.ofString(bookingJson.toString()))
                .build();

        // Send request
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        // Error handling
        if (response.statusCode() == 404) {
            throw new Exception("A booking, customer or site with the provided ID was not found");
        } else if (response.statusCode() == 400) {
            throw new Exception("JSON Mistake");
        }
    }

    @Override
    public void patchBookingVenue(Booking booking, String siteId) throws Exception{
        String bookingURL = this.ROOTURL + "/booking/" + booking.getBookingId();


        //JsonObject to patch booking
        JsonObject bookingJson = new JsonObject();

        //Add Status to booking jsonObject
        bookingJson.addProperty("testingSiteId", siteId);

        // Request construction
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder(URI.create(bookingURL + "?jwt=true"))
                .setHeader("Authorization", this.APIKEY)
                .header("Content-Type","application/json")
                .method("PATCH", HttpRequest.BodyPublishers.ofString(bookingJson.toString()))
                .build();

        // Send request
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        // Error handling
        if (response.statusCode() == 404) {
            throw new Exception("A booking, customer or site with the provided ID was not found");
        } else if (response.statusCode() == 400) {
            throw new Exception("JSON Mistake");
        }
    }

    @Override
    public void patchBookingTiming(Booking booking, Date date) throws Exception {
        String bookingURL = this.ROOTURL + "/booking/" + booking.getBookingId();

        // JsonObject to patch booking
        JsonObject bookingJson = new JsonObject();

        // Add time to booking jsonObject
        bookingJson.addProperty("startTime", dateStringBuilder(date));

        // Request construction
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder(URI.create(bookingURL + "?jwt=true"))
                .setHeader("Authorization", this.APIKEY)
                .header("Content-Type","application/json")
                .method("PATCH", HttpRequest.BodyPublishers.ofString(bookingJson.toString()))
                .build();

        // Send request
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        // Error handling
        if (response.statusCode() == 404) {
            throw new Exception("A booking, customer or site with the provided ID was not found");
        } else if (response.statusCode() == 400) {
            throw new Exception("JSON Mistake");
        }
    }

    @Override
    public void patchNotification(User user) throws Exception{
        String userUrl = this.ROOTURL + "/user/" + user.getId();

        // Check if user has notifications
        ArrayList<String> currentInfo = user.getNotifications();

        if (!currentInfo.isEmpty()) {
            JsonObject additionalInfoJson = this.getUserAdditionalInfo(user);

            // Keep existing employedSite if receptionist
            if (user.getEmployedSite() != null) {
                additionalInfoJson.addProperty("employedSite", user.getEmployedSite());
            }

            // Add notifications to additional info
            JsonArray notificationsJsonArray = new JsonArray();
            for (String change : currentInfo)
                notificationsJsonArray.add(change);
            additionalInfoJson.add("notifications", notificationsJsonArray);

            // Add additional info to bookingJson
            JsonObject bookingJson = new JsonObject();
            bookingJson.add("additionalInfo", additionalInfoJson);

            // Request construction
            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder(URI.create(userUrl + "?jwt=true"))
                    .setHeader("Authorization", this.APIKEY)
                    .header("Content-Type", "application/json")
                    .method("PATCH", HttpRequest.BodyPublishers.ofString(bookingJson.toString()))
                    .build();

            // Send request
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            // Error handling
            if (response.statusCode() == 404) {
                throw new Exception("A booking, customer or site with the provided ID was not found");
            } else if (response.statusCode() == 400) {
                throw new Exception("JSON Mistake");
            }
        }
    }

    @Override
    public void patchModification(Booking booking) throws Exception{
        String userUrl = this.ROOTURL + "/booking/" + booking.getBookingId();

        // Check if booking has modifications
        ArrayList<BookingMemento> currentInfo = booking.getModifications();

        if (!currentInfo.isEmpty()) {
            JsonObject additionalInfoJson = this.getBookingAdditionalInfo(booking);

            // Add modifications to additional info
            JsonArray modificationsJsonArray = new JsonArray();
            for (BookingMemento change : currentInfo) {
                JsonObject json = new JsonObject();
                json.addProperty("venue", change.getVenue());
                json.addProperty("timing", dateStringBuilder(change.getTiming()));
                json.addProperty("modifyDate", dateStringBuilder(change.getModificationTime()));

                modificationsJsonArray.add(json);
            }
            additionalInfoJson.add("modifications", modificationsJsonArray);

            // Add additional info to bookingJson
            JsonObject bookingJson = new JsonObject();
            bookingJson.add("additionalInfo", additionalInfoJson);

            // Request construction
            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder(URI.create(userUrl + "?jwt=true"))
                    .setHeader("Authorization", this.APIKEY)
                    .header("Content-Type", "application/json")
                    .method("PATCH", HttpRequest.BodyPublishers.ofString(bookingJson.toString()))
                    .build();

            // Send request
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            // Error handling
            if (response.statusCode() == 404) {
                throw new Exception("A booking, customer or site with the provided ID was not found");
            } else if (response.statusCode() == 400) {
                throw new Exception("JSON Mistake");
            }
        }
    }

    private JsonObject getUserAdditionalInfo(User user) throws Exception{
        String userURL = this.ROOTURL + "/booking/" + user.getId();

        // Request construction
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder(URI.create(userURL + "?jwt=true"))
                .setHeader("Authorization", this.APIKEY)
                .header("Content-Type", "application/json")
                .GET()
                .build();

        // Send request
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        JsonObject json = JsonParser.parseString(response.body()).getAsJsonObject();

        return json.get("additionalInfo").getAsJsonObject();
    }

    private JsonObject getBookingAdditionalInfo(Booking booking) throws Exception{
        String bookingURL = this.ROOTURL + "/booking/" + booking.getBookingId();

        // Request construction
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder(URI.create(bookingURL + "?jwt=true"))
                .setHeader("Authorization", this.APIKEY)
                .header("Content-Type", "application/json")
                .GET()
                .build();

        // Send request
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        JsonObject json = JsonParser.parseString(response.body()).getAsJsonObject();

        return json.get("additionalInfo").getAsJsonObject();
    }

    @Override
    public void deleteBooking(Booking booking) throws  Exception{
        String bookingURL = this.ROOTURL + "/booking/" + booking.getBookingId();

        // Request construction
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder(URI.create(bookingURL + "?jwt=true"))
                .setHeader("Authorization", this.APIKEY)
                .header("Content-Type","application/json")
                .DELETE()
                .build();

        // Send request
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        // Error handling
        if (response.statusCode() == 404) {
            throw new Exception("A customer or site with the provided ID was not found");
        } else if (response.statusCode() == 400) {
            throw new Exception("Invalid booking ID");
        }
    }

    private String readFileKey(String filename){
        String key = null;
        try {
            File myObj = new File(filename);
            Scanner myReader = new Scanner(myObj);
            key = myReader.nextLine();


            myReader.close();
        } catch (FileNotFoundException e) {
            System.out.println("API Key Not Found");
            e.printStackTrace();
        }
        return key;
    }

/*    public void tempPatchSite() throws Exception{
        String siteId = "7fbd25ee-5b64-4720-b1f6-4f6d4731260e";
        String bookingURL = this.ROOTURL + "/testing-site/" + siteId;

        // Request construction
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder(URI.create(bookingURL + "?jwt=true"))
                .setHeader("Authorization", this.APIKEY)
                .header("Content-Type","application/json")
                .GET()
                .build();

        // Send request
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        JsonObject json = JsonParser.parseString(response.body()).getAsJsonObject();

        JsonObject additionalInfoJson = json.get("additionalInfo").getAsJsonObject();

        additionalInfoJson.remove("types");
        JsonObject array = additionalInfoJson.get("operatingHours").getAsJsonObject();
        additionalInfoJson.remove("operatingHours");
        additionalInfoJson.addProperty("category", "Clinic");
        JsonArray types = new JsonArray();
        types.add("DRIVETHROUGH");
        types.add("WALKIN");
        additionalInfoJson.add("types",types);
        additionalInfoJson.add("operatingHours", array);

        JsonObject site = new JsonObject();
        site.add("additionalInfo", additionalInfoJson);

        // Request construction
        HttpClient client1 = HttpClient.newHttpClient();
        HttpRequest request1 = HttpRequest.newBuilder(URI.create(bookingURL + "?jwt=true"))
                .setHeader("Authorization", this.APIKEY)
                .header("Content-Type", "application/json")
                .method("PATCH", HttpRequest.BodyPublishers.ofString(site.toString()))
                .build();

        // Send request
        HttpResponse<String> response1 = client1.send(request1, HttpResponse.BodyHandlers.ofString());
    }*/
}
