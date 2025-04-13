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
    @FXML Button addRescueBTN;
    @FXML TextField firstNameTF, lastNameTF;
    @FXML DatePicker dateDP;
    @FXML ComboBox<String> barangayLocationCombo, emergencyTypeCombo;
    @FXML Slider severitySlider;
    @FXML TextField numOfRescueeTF, numOfChildTF, numOfAdultsTF, numOfSeniorsTF;

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
        String selectedFN = firstNameTF.getText();
        String selectedLN = lastNameTF.getText(); 
        LocalDate selectedDate = (dateDP.getValue() != null) ? dateDP.getValue() : null;
        
        String selectedBarangay = barangayLocationCombo.getValue();

        String selectedEmergencyType = emergencyTypeCombo.getValue();
        double sliderVal = severitySlider.getValue();
        String selectedSeverity = mapSeverity(sliderVal);

        int rescueeCount = Integer.parseInt(numOfRescueeTF.getText());
        int childrenCount = Integer.parseInt(numOfChildTF.getText());
        int adultCount = Integer.parseInt(numOfAdultsTF.getText());
        int seniorsCount = Integer.parseInt(numOfSeniorsTF.getText());


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
