import java.time.LocalDate;

public class Emergency {
    private String incidentNumber;
    private int barangayID;
    private String emergencyType;
    private String severity;
    private int rescueCount;
    private String status;
    private LocalDate dateIssued;
    private int peopleID;

    public Emergency(String incidentNumber, int barangayID, String emergencyType, String severity, int rescueCount, String status, LocalDate dateIssued, int peopleID) {
        this.incidentNumber = incidentNumber;
        this.barangayID = barangayID;
        this.emergencyType = emergencyType;
        this.severity = severity;
        this.rescueCount = rescueCount;
        this.status = status;
        this.dateIssued = dateIssued;
        this.peopleID = peopleID;
    }
    

    // getters
    public String getIncidentNumber() {
        return incidentNumber;
    }
    
    public void setIncidentNumber(String incidentNumber) {
        this.incidentNumber = incidentNumber;
    }    
    public int getBarangayID() { 
        return barangayID; }
    
    public String getEmergencyType() { 
        return emergencyType; }
    
    public String getSeverity() { 
        return severity; }
    
    public int getRescueCount() { 
        return rescueCount; }
    
    public String getStatus() { 
        return status; }
    
    public LocalDate getDateIssued() { 
        return dateIssued; }
    
    public int getPeopleID() { 
        return peopleID; }
}

