public class EmergencyReport {
    private Integer reportId;
    private String reportWriter;
    private String reportRemarks;
    private String emergencyActionTaken;

    public EmergencyReport(String reportWriter, String reportRemarks, String emergencyActionTaken) {
        this.reportWriter = reportWriter;
        this.reportRemarks = reportRemarks;
        this.emergencyActionTaken = emergencyActionTaken;
    }
    public EmergencyReport(Integer reportId, String reportWriter, String reportRemarks, String emergencyActionTaken) {
        this.reportId = reportId;
        this.reportWriter = reportWriter;
        this.reportRemarks = reportRemarks;
        this.emergencyActionTaken = emergencyActionTaken;
    }

    public Integer getReportId () {
        return reportId; 
    }
    public void setReportId (Integer reportId) {
        this.reportId = reportId;
    }
    public String getReportWriter() {
        return reportWriter;
    }

    public void setReportWriter(String reportWriter) {
        this.reportWriter = reportWriter;
    }

    public String getReportRemarks() {
        return reportRemarks;
    }

    public void setReportRemarks(String reportRemarks) {
        this.reportRemarks = reportRemarks;
    }

    public String getEmergencyActionTaken() {
        return emergencyActionTaken;
    }

    public void setEmergencyActionTaken(String emergencyActionTaken) {
        this.emergencyActionTaken = emergencyActionTaken;
    }
}