
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
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
    @FXML private TextField reportAddressTF;
    @FXML private TextField reportContactTF;
    @FXML private TextField headRescueTF;
    @FXML private TextArea actionTaken;
    @FXML private TextArea remarks;
    
    private Stage dialogStage;
    
    private int historyID;

    public void setHistoryData(HistoryTable history) {
    this.historyID = history.getHistoryID(); 

    reporterName.setText(history.getPersonInChargeHistory());
    dateOfIncident.setText(history.getDispatchedTime().toString());
    incidentNum.setText(history.getIncidentNumHistory());
    incidentType.setText(history.getEmType());
    incidentSev.setText(history.getEmSeverity());
    incidentLoc.setText(history.getBarangayName());
    reportAddressTF.setText(history.getAddressHistory());
    reportContactTF.setText(history.getContactNumHistory());
    headRescueTF.setText(history.getOfficerInChargeHistory());

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
       
        
    } else {
        incidentReportWrittenBy.clear();
        remarks.clear();
        actionTaken.clear();
    }
}
    @FXML 
    private void handleSubmit() { 
        int historyID = this.historyID;

        String writer = incidentReportWrittenBy.getText();
        String remarksText = remarks.getText();
        String action = actionTaken.getText();
    

        if (writer == null || writer.trim().isEmpty() ||
        remarksText == null || remarksText.trim().isEmpty() ||
        action == null || action.trim().isEmpty()) {
        javafx.scene.control.Alert alert = new javafx.scene.control.Alert(javafx.scene.control.Alert.AlertType.WARNING);
        alert.setTitle("Missing Information");
        alert.setHeaderText("Please fill in all required fields.");
        alert.setContentText("Writer, Remarks, Action Taken, and Head Rescue must not be empty.");
        Stage alertStage = (Stage) alert.getDialogPane().getScene().getWindow();
        alertStage.getIcons().add(new Image(getClass().getResourceAsStream("pasigLogo.jpg")));
        alert.showAndWait();
        return;
    }
        
        EmergencyReport existingReport = DatabaseHandler.getReportByHistoryID(historyID);
        if (existingReport != null) {
            javafx.scene.control.Alert alert = new javafx.scene.control.Alert(javafx.scene.control.Alert.AlertType.WARNING);
            alert.setTitle("Report Exists");
            alert.setHeaderText("A report for this incident already exists.");
            alert.setContentText("You cannot submit another report for this incident.");
            alert.setTitle("Pasig SaR System");
            Stage alertStage = (Stage) alert.getDialogPane().getScene().getWindow();
            alertStage.getIcons().add(new Image(getClass().getResourceAsStream("pasigLogo.jpg")));
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
            Stage alertStage = (Stage) alert.getDialogPane().getScene().getWindow();
            alertStage.getIcons().add(new Image(getClass().getResourceAsStream("pasigLogo.jpg")));
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