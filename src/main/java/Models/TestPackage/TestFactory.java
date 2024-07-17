package Models.TestPackage;

public class TestFactory {

    public Test getTest(String testType, String id, String patientId, String adminId, TestResult result, TestStatus status, String notes, String additionalInfo ){
        if(testType.equalsIgnoreCase("RAT"))
            return new RAT(id , patientId,  adminId,  result,  status,  notes,  additionalInfo);
        else{
            return new PCR(id , patientId,  adminId,  result,  status,  notes,  additionalInfo);
        }
    }
}
