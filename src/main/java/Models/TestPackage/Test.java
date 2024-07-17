package Models.TestPackage;

public abstract class Test {
    // Test information
    private final String id;
    private final String patientId;
    private final String adminId;
    private TestResult result;
    private TestStatus status;
    private String notes;
    private String additionalInfo;

    public Test(String id, String patientId, String adminId, TestResult result, TestStatus status, String notes, String additionalInfo) {
        this.id = id;
        this.patientId = patientId;
        this.adminId = adminId;
        this.result = result;
        this.status = status;
        this.notes = notes;
        this.additionalInfo = additionalInfo;
    }

    public abstract String testType();

    @Override
    public String toString() {
        return "Test{" +
                "id='" + id + '\'' +
                ", type=" + testType() +
                ", patientId='" + patientId + '\'' +
                ", adminId='" + adminId + '\'' +
                ", result=" + result +
                ", status=" + status +
                '}';
    }
}
