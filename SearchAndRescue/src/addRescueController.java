import java.time.LocalDate;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;

public class addRescueController {
    @FXML TextField firstNameTF, lastNameTF;
    @FXML DatePicker dateDP;
    @FXML ComboBox<String> barangayLocationCombo, emergencyTypeCombo;
    @FXML Slider severitySlider;
    @FXML TextField numOfRescueeTF, numOfChildTF, numOfAdultsTF, numOfSeniorsTF;
    @FXML Button addRescueBTN;
    
    private TabPaneController tabPaneController; 
   
   // private ObservableList<String> barangayList = FXCollections.observableArrayList();

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

    String type = emergencyTypeCombo.getValue();
    String severity = mapSeverity(severitySlider.getValue());
    String status = "QUEUED"; // default status
    int barangayID = DatabaseHandler.getBarangayIDFromName(barangayLocationCombo.getValue());
    int children = Integer.parseInt(numOfChildTF.getText());
    int adults = Integer.parseInt(numOfAdultsTF.getText());
    int seniors = Integer.parseInt(numOfSeniorsTF.getText());

    PeopleCount person = new PeopleCount(firstName, lastName, children, adults, seniors);
    int peopleID = DatabaseHandler.insertPeople(person);

    if (peopleID != -1) {
        String incidentNumber = DatabaseHandler.generateIncidentNumber(); 
        
        Emergency emergency = new Emergency(incidentNumber, barangayID, type, severity, person.getMemberCount(), status, date, peopleID);

        boolean success = DatabaseHandler.insertEmergency(emergency);
        if (success) {
            System.out.println("Rescue successfully recorded.");
            if (tabPaneController != null) {
                tabPaneController.refreshIncidentsTable(); 
            }            
        } else {
            System.out.println("Failed to insert emergency.");
        }
    } else {
        System.out.println("Failed to insert people data.");
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
}
