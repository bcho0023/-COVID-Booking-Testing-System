package Services.ConferencePackage;

public class ConferenceAdapter implements Conference {
    @Override
    public String generateConferenceURL() {
        return "https://dummy.url";
    }
}
