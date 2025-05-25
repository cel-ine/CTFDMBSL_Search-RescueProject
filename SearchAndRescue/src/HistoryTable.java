import java.time.LocalDateTime;

public class HistoryTable {
    private int historyID;
    private String incidentReport;
    private String emType;
    private String emSeverity;
    private String incidentNumHistory;
    private LocalDateTime dispatchedTime;
    private String barangayName;
    private String personInChargeHistory;
    private String numOfRescueeHistory;
    private String addressHistory;
    private String contactNumHistory;
    private String headRescue;


    public HistoryTable(String incidentReport, String emType, String emSeverity, String incidentNumHistory,
                        LocalDateTime dispatchedTime, String barangayName, String personInChargeHistory, String numOfRescueeHistory, String addressHistory, String contactNumHistory) {
        this.incidentReport = incidentReport;
        this.emType = emType;
        this.emSeverity = emSeverity;
        this.incidentNumHistory = incidentNumHistory;
        this.dispatchedTime = dispatchedTime;
        this.barangayName = barangayName;
        this.personInChargeHistory = personInChargeHistory;
        this.numOfRescueeHistory = numOfRescueeHistory;
        this.addressHistory = addressHistory;
        this.contactNumHistory = contactNumHistory;
    }
    public HistoryTable(Integer historyID, String incidentReport, String emType, String emSeverity, String incidentNumHistory,
                        LocalDateTime dispatchedTime, String barangayName, String personInChargeHistory, String numOfRescueeHistory, String addressHistory, String contactNumHistory) {
        this.historyID = historyID;
        this.incidentReport = incidentReport;
        this.emType = emType;
        this.emSeverity = emSeverity;
        this.incidentNumHistory = incidentNumHistory;
        this.dispatchedTime = dispatchedTime;
        this.barangayName = barangayName;
        this.personInChargeHistory = personInChargeHistory;
        this.numOfRescueeHistory = numOfRescueeHistory;
        this.addressHistory = addressHistory;
        this.contactNumHistory = contactNumHistory;
    }


    public HistoryTable() {
    }

    public Integer getHistoryID() {
        return historyID;
    }
    public void setHistoryID(Integer historyID) {
        this.historyID = historyID;
    }
    public String getIncidentReport() {
        return incidentReport;
    }
    public void setIncidentReport(String incidentReport) {
        this.incidentReport = incidentReport;
    }

    public String getEmType() {
        return emType;
    }
    public void setEmType(String emType) {
        this.emType = emType;
    }   

    public String getEmSeverity() {
        return emSeverity;
    }
    public void setEmSeverity(String emSeverity) {
        this.emSeverity = emSeverity;
    }

    public String getIncidentNumHistory() {
        return incidentNumHistory;
    }
    public void setIncidentNumHistory(String incidentNumHistory) {
        this.incidentNumHistory = incidentNumHistory;
    }

    public LocalDateTime getDispatchedTime() {
        return dispatchedTime;
    }
    public void setDispatchedTime(LocalDateTime dispatchedTime) {
        this.dispatchedTime = dispatchedTime;
    }

    public String getBarangayName() {
        return barangayName;
    }
    public void setBarangayName(String barangayName) {
        this.barangayName = barangayName;
    }

    public String getNumOfRescueeHistory() {
        return numOfRescueeHistory;
    }
    public void setNumOfRescueeHistory(String numOfRescueeHistory) {
        this.numOfRescueeHistory = numOfRescueeHistory;
    }

    public String getAddressHistory() {
        return addressHistory;
    }
    public void setAddressHistory(String addressHistory) {
        this.addressHistory = addressHistory;
    }
    public String getContactNumHistory() {
        return contactNumHistory;
    }
    public void setContactNumHistory(String contactNumHistory) {
        this.contactNumHistory = contactNumHistory;
    }

    public String getPersonInChargeHistory() {
        return personInChargeHistory;
    }
    public void setPersonInChargeHistory (String personInChargeHistory) {
        this.personInChargeHistory = personInChargeHistory;
    }

     public String getHeadRescue() {
        return headRescue;
    }
    public void setHeadRescue (String headRescue) {
        this.headRescue = headRescue;
    }
}
