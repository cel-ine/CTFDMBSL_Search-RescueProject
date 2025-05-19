
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class AddReportController {
    @FXML private Button submitBTN;
    @FXML private TextField reporterName;
    @FXML private TextField dateOfIncident;
    @FXML private TextField incidentNum;
    @FXML private TextField incidentType;
    @FXML private TextField incidentSev;
    @FXML private TextField adultCount;
    @FXML private TextField childrenCount;
    @FXML private TextField seniorCount;
    @FXML private TextField incidentLoc;
    @FXML private TextField incidentReportWrittenBy;
    @FXML private TextArea actionTaken;
    @FXML private TextArea remarks;
    
    private Stage dialogStage;
    
    private int historyID;

    public void setHistoryData(HistoryTable history) {
    this.historyID = history.getHistoryID(); 

    reporterName.setText(history.getRescueeNameHistory());
    dateOfIncident.setText(history.getDispatchedTime().toString());
    incidentNum.setText(history.getIncidentNumHistory());
    incidentType.setText(history.getEmType());
    incidentSev.setText(history.getEmSeverity());
    incidentLoc.setText(history.getBarangayName());

    try {
        int[] counts = DBService.getPeopleCountsByIncidentNumber(history.getIncidentNumHistory());
        childrenCount.setText(String.valueOf(counts[0]));
        adultCount.setText(String.valueOf(counts[1]));
        seniorCount.setText(String.valueOf(counts[2]));
    } catch (Exception e) {
        childrenCount.setText("0");
        adultCount.setText("0");
        seniorCount.setText("0");
    }

    EmergencyReport report = DatabaseHandler.getReportByHistoryID(historyID);

    if (report != null) {
        incidentReportWrittenBy.setText(report.getReportWriter());
        remarks.setText(report.getReportRemarks());
        actionTaken.setText(report.getEmergencyActionTaken());
        // incidentReportWrittenBy.setDisable(false);
        // remarks.setDisable(false);
        // actionTaken.setDisable(false);
        
    } else {
        incidentReportWrittenBy.clear();
        remarks.clear();
        actionTaken.clear();
        // incidentReportWrittenBy.setDisable(true);
        // remarks.setDisable(true);
        // actionTaken.setDisable(true);
    }
}
    @FXML 
    private void handleSubmit() { 
        int historyID = this.historyID;

        String writer = incidentReportWrittenBy.getText();
        String remarksText = remarks.getText();
        String action = actionTaken.getText();

        EmergencyReport existingReport = DatabaseHandler.getReportByHistoryID(historyID);
        if (existingReport != null) {
            javafx.scene.control.Alert alert = new javafx.scene.control.Alert(javafx.scene.control.Alert.AlertType.WARNING);
            alert.setTitle("Report Exists");
            alert.setHeaderText("A report for this incident already exists.");
            alert.setContentText("You cannot submit another report for this incident.");
            alert.showAndWait();
            return; 
        }

        if (DatabaseHandler.historyIDExists(historyID)) {
            DatabaseHandler.submitReport(historyID, writer, remarksText, action);

            EmergencyReport report = DatabaseHandler.getReportByHistoryID(historyID);
            if (report != null) {
                incidentReportWrittenBy.setText(report.getReportWriter());
                remarks.setText(report.getReportRemarks());
                actionTaken.setText(report.getEmergencyActionTaken());
            }

            javafx.scene.control.Alert alert = new javafx.scene.control.Alert(javafx.scene.control.Alert.AlertType.INFORMATION);
            alert.setTitle("Report Submission");
            alert.setHeaderText("Report Submitted Successfully");
            alert.setContentText("The report has been submitted successfully.");
            alert.showAndWait();

            if (dialogStage != null) {
                dialogStage.close();
            }
        } else {
            System.err.println("historyID does not exist in History table!");
        }
    }

    @FXML
    private void handleClose() {
        if (dialogStage != null) {
            dialogStage.close();
        }
    }
}