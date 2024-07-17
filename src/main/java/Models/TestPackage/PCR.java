package Models.TestPackage;

public class PCR extends Test{

    public PCR(String id, String patientId, String adminId, TestResult result, TestStatus status, String notes, String additionalInfo) {
        super(id, patientId, adminId, result, status, notes, additionalInfo);
    }

    @Override
    public String testType() {
        return "PCR";
    }
}
