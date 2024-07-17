package Models.SitePackage;

public class Address {
    private final String latitude;
    private final String longitude;
    private final String unitNumber;
    private final String street;
    private final String street2;
    private final String suburb;
    private final String state;
    private final String postcode;
    private String additionalInfo;

    public Address (String latitude, String longitude, String unitNumber, String street, String street2, String suburb, String state, String postcode, String additionalInfo){
        this.latitude = latitude;
        this.longitude = longitude;
        this.unitNumber = unitNumber;
        this.street = street;
        this.street2 = street2;
        this.suburb = suburb;
        this.state = state;
        this.postcode = postcode;
        this.additionalInfo = additionalInfo;
    }

    public String getSuburb() {
        return suburb;
    }

    @Override
    public String toString() {
        return "{" +
                "unitNumber = '" + unitNumber + '\'' +
                ", street = '" + street + '\'' +
                ", street2 = '" + street2 + '\'' +
                ", suburb = '" + suburb + '\'' +
                ", state = '" + state + '\'' +
                ", postcode = '" + postcode + '\'' +
                '}';
    }
}
