
import java.io.IOException;
import java.time.LocalDate;
import java.util.Set;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Duration;

public class TabPaneController {
    @FXML
    private AnchorPane rootPane;
    //🏠🏠🏠 HOME TAB - BARANGAY TABLE
    @FXML TableView <BarangayTable> brgyTable; 
    @FXML TableColumn <BarangayTable, String> brgyNameCol;
    @FXML TableColumn <BarangayTable, Integer> brgyRescueCountCol;  
    @FXML TableColumn <BarangayTable, Double> brgyDistanceCol;
    private ObservableList<BarangayTable> barangayList = FXCollections.observableArrayList(); 

    //🏠🏠🏠 HOME TAB - BARANGAY DESCRIPTION TABLE
    @FXML TableView <BarangayDescTable> brgyDescriptionTable; 
    @FXML TableColumn <BarangayDescTable, String> brgyNameCol2;
    @FXML TableColumn <BarangayDescTable, String> brgyCaptCol;
    @FXML TableColumn <BarangayDescTable, String> brgyHazardCol;
    @FXML TableColumn <BarangayDescTable, Integer> brgyPopulationCol;
    private ObservableList<BarangayDescTable> barangayDescList = FXCollections.observableArrayList();
    
    //🚨🚨🚨 ACTIVE INCIDENTS TAB 
    @FXML TableView <ActiveIncidentsTable> activeIncidentsTable;
    @FXML Button editButton, saveButton, addRescueButton, dispatchButton;
    @FXML TableColumn <ActiveIncidentsTable, String> emergencyTypeCol;
    @FXML TableColumn <ActiveIncidentsTable, String> emergencyStatusCol;
    @FXML TableColumn <ActiveIncidentsTable, String> emergencySeverityCol;
    @FXML TableColumn <ActiveIncidentsTable, Integer> incidentNumCol;
    @FXML TableColumn <ActiveIncidentsTable, LocalDate> timeCreatedCol;
    @FXML TableColumn <ActiveIncidentsTable, String> locationCol;
    @FXML TableColumn <ActiveIncidentsTable, String> rescueeNameCol;
    @FXML TableColumn <ActiveIncidentsTable, Integer> numOfRescueeCol;
    private ObservableList<ActiveIncidentsTable> incidentsList = FXCollections.observableArrayList();

    public void initialize() {
        System.out.println("Initialize is running");
        //HOME TAB - BARANGAY TABLE
        brgyNameCol.setCellValueFactory(new PropertyValueFactory<>("barangayName"));
        brgyRescueCountCol.setCellValueFactory(new PropertyValueFactory<>("barangayRescueCount"));
        brgyDistanceCol.setCellValueFactory(new PropertyValueFactory<>("barangayDistance"));
       
       
        //HOME TAB - BARANGAY DESCRIPTION
        brgyNameCol2.setCellValueFactory(new PropertyValueFactory<>("barangayName"));
        brgyCaptCol.setCellValueFactory(new PropertyValueFactory<>("barangayCaptain"));
        brgyHazardCol.setCellValueFactory(new PropertyValueFactory<>("barangayHazard"));
        brgyPopulationCol.setCellValueFactory(new PropertyValueFactory<>("barangayPopulation"));
        loadBarangayDescTable();

        //ACTIVE INCIDENTS TAB 
        emergencyTypeCol.setCellValueFactory(new PropertyValueFactory<>("emergencyType"));
        emergencyStatusCol.setCellValueFactory(new PropertyValueFactory<>("emergencyStatus"));
        emergencySeverityCol.setCellValueFactory(new PropertyValueFactory<>("emergencySeverity"));
        incidentNumCol.setCellValueFactory(new PropertyValueFactory<>("incidentNumber"));
        timeCreatedCol.setCellValueFactory(new PropertyValueFactory<>("dateIssued"));
        locationCol.setCellValueFactory(new PropertyValueFactory<>("barangayLocation"));
        rescueeNameCol.setCellValueFactory(new PropertyValueFactory<>("rescueeName"));
        numOfRescueeCol.setCellValueFactory(new PropertyValueFactory<>("numOfRescuee"));
        refreshIncidentsTable();

        brgyDistanceCol.setCellFactory(column -> new TableCell<BarangayTable, Double>() {
        @Override
        protected void updateItem(Double item, boolean empty) {
            super.updateItem(item, empty);
            if (empty || item == null) {
                    setText(null);
            } else {
                    setText(item + " km");
            }
        }
    });    
        loadBarangayTable();

    }

    //🚨🚨🚨 ACTIVE INCIDENTS TAB
    @FXML
    private void openAddRescuePopUp(ActionEvent event) { 
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("addRescueUI.fxml"));
            Parent root = loader.load();

            addRescueController AddRescueController = loader.getController();
            AddRescueController.setTabPaneController(this);

            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleEditButtonClick() {
        Label messageLabel = new Label("You are now in editing mode. Click the cells you want to modify except for Severity, Incident#, Date Issued.");
        messageLabel.setStyle("-fx-background-color: rgba(0, 0, 0, 0.7); -fx-text-fill: white; -fx-padding: 10px;");
        
        messageLabel.setMaxWidth(800);  
        messageLabel.setMaxHeight(50);  

        messageLabel.setTranslateX(400);
        messageLabel.setTranslateY(400);

        rootPane.getChildren().add(messageLabel);

        Timeline timeline = new Timeline(
            new KeyFrame(Duration.seconds(3), event -> {
                rootPane.getChildren().remove(messageLabel);
            })
        );
        timeline.play();

        activeIncidentsTable.setEditable(true);
        TableEditor.makeActiveIncidentsTableEditable(
            emergencyTypeCol,
            emergencyStatusCol,
            locationCol,
            rescueeNameCol,
            numOfRescueeCol
        );
    }

    
    @FXML 
    private void handleSaveButton() {
        // Iterate through the table rows and update the edited incidents in the database
        for (ActiveIncidentsTable incident : activeIncidentsTable.getItems()) {
            // Get the updated values from the table columns
            String emergencyType = incident.getEmergencyType();
            String emergencyStatus = incident.getEmergencyStatus();
            String emergencySeverity = incident.getEmergencySeverity();
            String incidentNumber = incident.getIncidentNumber();
            String barangayLocation = incident.getBarangayLocation();
            String[] rescueeNameParts = incident.getRescueeName().split(" ", 2); // Split the name into first and last
            String firstName = rescueeNameParts.length > 0 ? rescueeNameParts[0] : "";
            String lastName = rescueeNameParts.length > 1 ? rescueeNameParts[1] : "";
            int totalRescuees = incident.getNumOfRescuee();

            // Call the method to update the database with the edited incident data
            DBService.updateIncidentAndRescuee(
                incidentNumber, // Use incident number to identify the record
                emergencyType,
                emergencyStatus,
                emergencySeverity, // updated severity value
                barangayLocation,
                firstName,
                lastName,
                totalRescuees // Total number of people
            );
        }

        System.out.println("Updated incidents successfully!");
    }








    //LOAD INFORMATIONS 
    private void loadBarangayTable() { 
        barangayList.setAll(DBService.getAllBarangayInfo());
        brgyTable.setItems(barangayList);
    }
    private void loadBarangayDescTable() { 
        barangayDescList.setAll(DBService.getAllBarangayDescription());
        brgyDescriptionTable.setItems(barangayDescList);
    }
    public void refreshIncidentsTable() {
        ObservableList<ActiveIncidentsTable> data = DBService.getActiveIncidents();
        activeIncidentsTable.setItems(data);
    }

    
    // saveBtn.setOnAction(e -> {
    //     Set<ActiveIncidentsTable> editedRows = TableEditor.getEditedRows();
    //     for (ActiveIncidentsTable incident : editedRows) {
    //         DBService.updateIncident(incident);
    //     }
    //     TableEditor.clearEditedRows();
    //     refreshIncidentsTable();
    // });    
}

    