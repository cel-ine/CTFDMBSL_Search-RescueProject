import java.time.LocalDateTime;

public class HistoryTable {
    private String incidentReport;
    private String emType;
    private String emSeverity;
    private String incidentNumHistory;
    private LocalDateTime dispatchedTime;
    private String barangayName;
    private String rescueeNameHistory;
    private String numOfRescueeHistory;


    public HistoryTable(String incidentReport, String emType, String emSeverity, String incidentNumHistory,
                        LocalDateTime dispatchedTime, String barangayName, String rescueeNameHistory,
                        String numOfRescueeHistory) {
        this.incidentReport = incidentReport;
        this.emType = emType;
        this.emSeverity = emSeverity;
        this.incidentNumHistory = incidentNumHistory;
        this.dispatchedTime = dispatchedTime;
        this.barangayName = barangayName;
        this.rescueeNameHistory = rescueeNameHistory;
        this.numOfRescueeHistory = numOfRescueeHistory;
        
    }

    public HistoryTable() {
        //TODO Auto-generated constructor stub
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

    public String getRescueeNameHistory() {
        return rescueeNameHistory;
    }
    public void setRescueeNameHistory(String rescueeNameHistory) {
        this.rescueeNameHistory = rescueeNameHistory;
    }

    public String getNumOfRescueeHistory() {
        return numOfRescueeHistory;
    }
    public void setNumOfRescueeHistory(String numOfRescueeHistory) {
        this.numOfRescueeHistory = numOfRescueeHistory;
    }
}