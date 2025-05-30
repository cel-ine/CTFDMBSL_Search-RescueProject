import java.time.LocalDate;
import java.time.LocalDateTime;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class addRescueController {
    @FXML DatePicker dateDP;
    @FXML ComboBox<String> barangayLocationCombo, emergencyTypeCombo, officerInChargeCombo;
    @FXML Slider severitySlider;
    @FXML TextField addressTF, contactNumTF, personInChargeTF, numOfRescueeTF, numOfChildTF, numOfAdultsTF, numOfSeniorsTF;
    @FXML Button addRescueBTN;
    
    private TabPaneController tabPaneController; 
   
    public void setTabPaneController(TabPaneController tabPaneController) {
        this.tabPaneController = tabPaneController;
    }
    @FXML 
    public void initialize() {
        ObservableList<String> officerList = FXCollections.observableArrayList(
            "Capt. Antonio Rivera",
            "Insp. Carla Mendoza",
            "Lt. Joel Santos",
            "Engr. Melissa Cruz",
            "Sgt. Ramon de la Peña",
            "Cpt. Liza Tan",
            "Maj. Dennis Alonzo",
            "Dir. Clarisse Uy",
            "Lt. Col. Martin Aquino",
            "Engr. Paolo Lim",
            "FO1 Nathaniel Ruiz",
            "FO2 Jeanette Malvar",
            "FO3 Edgar Sevilla",
            "Cpt. Rea Domingo",
            "Lt. Ariel Gomez",
            "FO1 Ivy dela Cruz",
            "Sgt. Francis Alviar",
            "Engr. Mico Santos",
            "Insp. Danica Soriano",
            "Cpt. James Bautista",
            "Lt. Katrina Ong",
            "FO1 Josephine Ramos",
            "FO2 Carl Reyes",
            "FO3 Bianca Salazar",
            "Sgt. Louie Tan",
            "Lt. Gen. Rodel Valdez",
            "Maj. Karen Estrella",
            "FO1 Alex Navarro",
            "FO2 Ella Buenaventura",
            "Capt. Vic dela Rosa",
            "Lt. Nicole Ramirez",
            "Engr. Sean Alvarez",
            "Insp. Bea Manalo",
            "FO3 Dominic Fernandez",
            "Sgt. Ricky Corpuz",
            "FO1 Marjorie Gomez",
            "Cpt. Daniel Yulo",
            "Lt. Lea Santiago",
            "FO2 Rico Manansala",
            "Insp. Angela Cruz"
        );

       
    ObservableList<String> busyOfficers = DatabaseHandler.getBusyOfficers();
    ObservableList<String> availableOfficers = FXCollections.observableArrayList();

    for (String officer : officerList) {
        if (!busyOfficers.contains(officer)) {
            availableOfficers.add(officer);
        }
    }

    officerInChargeCombo.setItems(availableOfficers);

    officerInChargeCombo.setEditable(true);

    FilteredList<String> filteredOfficers = new FilteredList<>(availableOfficers, p -> true);
    officerInChargeCombo.setItems(filteredOfficers);

    officerInChargeCombo.getEditor().textProperty().addListener((obs, oldValue, newValue) -> {
        final String selected = officerInChargeCombo.getSelectionModel().getSelectedItem();
        if (selected == null || !selected.equals(officerInChargeCombo.getEditor().getText())) {
            filteredOfficers.setPredicate(item -> {
                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }
                String lowerCaseFilter = newValue.toLowerCase();
                return item.toLowerCase().contains(lowerCaseFilter);
            });
        }
    });

        ObservableList<BarangayTable> barangayList = DBService.getAllBarangayName();
        ObservableList<String> barangayNames = FXCollections.observableArrayList();
        for (BarangayTable barangay : barangayList) {
            barangayNames.add(barangay.getBarangayName());
        }
        barangayLocationCombo.setItems(barangayNames);
        emergencyTypeCombo.getItems().addAll("Flood", "Earthquake", "Landslide");

        // Add this listener:
        barangayLocationCombo.valueProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null && !newVal.trim().isEmpty()) {
                String captain = DatabaseHandler.getBarangayCaptainByBarangayName(newVal);
                personInChargeTF.setText(captain != null ? captain : "");
            } else {
                personInChargeTF.clear();
            }
        });
    }

    @FXML
    private void handleAddRescue() {
        LocalDate date = dateDP.getValue();
        String personInCharge = personInChargeTF.getText();
        String address = addressTF.getText();
        String contactNum = contactNumTF.getText().trim(); // treat as String!
        String barangayName = barangayLocationCombo.getValue();
        String type = emergencyTypeCombo.getValue();
        String severity = mapSeverity(severitySlider.getValue());
        String officerInCharge = officerInChargeCombo.getValue(); 

        // Validate required fields
        if (date == null ||
            barangayName == null || barangayName.trim().isEmpty() ||
            type == null || type.trim().isEmpty() ||
            address == null || address.trim().isEmpty() ||
            contactNum == null || contactNum.trim().isEmpty() || officerInCharge == null || 
            numOfChildTF.getText().trim().isEmpty() ||
            numOfAdultsTF.getText().trim().isEmpty() ||
            numOfSeniorsTF.getText().trim().isEmpty()) {
            showAlert("Missing Information", "Please fill in all required fields.");
            return;
        }
        if (!contactNum.matches("09\\d{9}")) {
        showAlert("Invalid Contact Number", "Contact number must be a valid 11-digit PH mobile number (e.g., 09XXXXXXXXX).");
        return;
    }

        if (!date.equals(LocalDate.now())) {
        showAlert("Invalid", "Only today's date is allowed.");
        return;
    }

        int children, adults, seniors;
        try {
            children = Integer.parseInt(numOfChildTF.getText());
            adults = Integer.parseInt(numOfAdultsTF.getText());
            seniors = Integer.parseInt(numOfSeniorsTF.getText());
            if (children < 0 || adults < 0 || seniors < 0) {
                showAlert("Invalid Input", "Numbers must be non-negative.");
                return;
            }
        } catch (NumberFormatException e) {
            showAlert("Invalid Input", "Please enter valid numbers for children, adults, and seniors.");
            return;
        }

        int barangayID = DatabaseHandler.getBarangayIDFromName(barangayName);

        String status = "QUEUED";

        PeopleCountTable person = new PeopleCountTable(address, contactNum, children, adults, seniors, officerInCharge);
        int peopleID = DatabaseHandler.insertPeople(person);

        if (peopleID != -1) {
            String incidentNumber = DatabaseHandler.generateIncidentNumberFromDB();
            Emergency emergency = new Emergency(
                incidentNumber,
                barangayID,
                type,
                severity,
                children + adults + seniors, // total rescuees
                status,
                LocalDateTime.now(),
                peopleID
            );

            boolean success = DatabaseHandler.insertEmergency(emergency);
            if (success) {
                showAlert("Success", "Rescue successfully recorded.");
                if (tabPaneController != null) {
                    tabPaneController.refreshIncidentsTable();
                    // Add marker to map
                    tabPaneController.addIncidentMarkerToMap(
                        incidentNumber,
                        barangayName,
                        status,
                        children + adults + seniors,
                        type
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
