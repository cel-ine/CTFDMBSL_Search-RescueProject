import java.time.LocalDate;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class addRescueController {
    @FXML TextField firstNameTF, lastNameTF;
    @FXML DatePicker dateDP;
    @FXML ComboBox<String> barangayLocationCombo, emergencyTypeCombo;
    @FXML Slider severitySlider;
    @FXML TextField numOfRescueeTF, numOfChildTF, numOfAdultsTF, numOfSeniorsTF;
    @FXML Button addRescueBTN;
    
    private TabPaneController tabPaneController; 
   
    public void setTabPaneController(TabPaneController tabPaneController) {
        this.tabPaneController = tabPaneController;
    }
    @FXML 
    public void initialize() {
        ObservableList<BarangayTable> barangayList = DBService.getAllBarangayName();
        ObservableList<String> barangayNames = FXCollections.observableArrayList();
        for (BarangayTable barangay : barangayList) {
            barangayNames.add(barangay.getBarangayName());
        }
        barangayLocationCombo.setItems(barangayNames);
        emergencyTypeCombo.getItems().addAll("Flood", "Fire", "Earthquake", "Landslide");
    }

    @FXML
    private void handleAddRescue() {
        String firstName = firstNameTF.getText();
        String lastName = lastNameTF.getText();
        LocalDate date = dateDP.getValue();
       
        String barangayName = barangayLocationCombo.getValue();
        String type = emergencyTypeCombo.getValue();
        String severity = mapSeverity(severitySlider.getValue());

        if (firstName == null || firstName.trim().isEmpty() ||
            lastName == null || lastName.trim().isEmpty() ||
            date == null ||
            barangayName == null || barangayName.trim().isEmpty() ||
            type == null || type.trim().isEmpty() ||
            numOfChildTF.getText().trim().isEmpty() ||
            numOfAdultsTF.getText().trim().isEmpty() ||
            numOfSeniorsTF.getText().trim().isEmpty()) {
            showAlert("Missing Information", "Please fill in all required fields.");
            return;
        }

        int barangayID = DatabaseHandler.getBarangayIDFromName(barangayName);

        int children, adults, seniors;
        try {
            children = Integer.parseInt(numOfChildTF.getText());
            adults = Integer.parseInt(numOfAdultsTF.getText());
            seniors = Integer.parseInt(numOfSeniorsTF.getText());
            if (children < 0 || adults < 0 || seniors < 0) {
                showAlert("Invalid Input", "Number of children, adults, and seniors must be non-negative.");
                return;
            }
        } catch (NumberFormatException e) {
            showAlert("Invalid Input", "Please enter valid numbers for children, adults, and seniors.");
            return;
        }

    String status = "QUEUED"; 

    PeopleCount person = new PeopleCount(firstName, lastName, children, adults, seniors);
    int peopleID = DatabaseHandler.insertPeople(person);

    if (peopleID != -1) {
        String incidentNumber = DatabaseHandler.generateIncidentNumberFromDB(); 
        Emergency emergency = new Emergency(incidentNumber, barangayID, type, severity, person.getMemberCount(), status, date, peopleID);

        boolean success = DatabaseHandler.insertEmergency(emergency);
        if (success) {
            showAlert("Success", "Rescue successfully recorded.");
            if (tabPaneController != null) {
                tabPaneController.refreshIncidentsTable(); 

                // ADD MARKER
                    tabPaneController.addIncidentMarkerToMap(
                            incidentNumber,
                            barangayLocationCombo.getValue(),
                            status,
                            children + adults + seniors, // total people to show
                            emergencyTypeCombo.getValue() // emergency type
                    );
        }            
        } else {
            showAlert("Error", "Failed to insert emergency.");
        }
    } else {
        showAlert("Error", "Failed to insert people data.");
    }
}

    private String mapSeverity(double value) {
        int rounded = (int) Math.round(value);
        return switch (rounded) {
            case 1 -> "Low";
            case 2 -> "Moderate";
            case 3 -> "High";
            case 4 -> "Critical";
            case 5 -> "Extreme";
            default -> "Low";
        };
    }
    private void showAlert(String title, String message) {
    javafx.scene.control.Alert alert = new javafx.scene.control.Alert(javafx.scene.control.Alert.AlertType.WARNING);
    alert.setTitle(title);
    alert.setHeaderText(null);
    alert.setContentText(message);
    Stage alertStage = (Stage) alert.getDialogPane().getScene().getWindow();
    alertStage.getIcons().add(new javafx.scene.image.Image(getClass().getResourceAsStream("pasigLogo.jpg")));
    alert.showAndWait();
    }
}
