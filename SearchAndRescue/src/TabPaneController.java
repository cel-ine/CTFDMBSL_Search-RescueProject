
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
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
import javafx.scene.control.Alert;
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

    //🗂️🗂️🗂️ INCIDENT REPORT & HISTORY TAB
    @FXML TableView <HistoryTable> historyTable;
    @FXML TableColumn <HistoryTable, String> incidentReport;
    @FXML TableColumn <HistoryTable, String> emTypeCol;
    @FXML TableColumn <HistoryTable, String> emSevCol;
    @FXML TableColumn <HistoryTable, String> incidentNumHCol;
    @FXML TableColumn <HistoryTable, LocalDateTime> dispatchedTimeCol;
    @FXML TableColumn <HistoryTable, String> brgyLocCol;
    @FXML TableColumn <HistoryTable, String> nameCol;
    @FXML TableColumn <HistoryTable, String> numOfRescueeHCol;
    private ObservableList<HistoryTable> historyList = FXCollections.observableArrayList();



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
       
        //HISTORY TAB
        incidentReport.setCellValueFactory(new PropertyValueFactory<>("incidentReport"));
        emTypeCol.setCellValueFactory(new PropertyValueFactory<>("emType"));
        emSevCol.setCellValueFactory(new PropertyValueFactory<>("emSeverity"));
        incidentNumHCol.setCellValueFactory(new PropertyValueFactory<>("incidentNumHistory"));
        dispatchedTimeCol.setCellValueFactory(new PropertyValueFactory<>("dispatchedTime"));
        brgyLocCol.setCellValueFactory(new PropertyValueFactory<>("barangayName"));
        nameCol.setCellValueFactory(new PropertyValueFactory<>("rescueeNameHistory"));
        numOfRescueeHCol.setCellValueFactory(new PropertyValueFactory<>("numOfRescueeHistory"));
        incidentReport.setCellValueFactory(new PropertyValueFactory<>("incidentReport"));
        loadHistoryTable();

        numOfRescueeCol.setCellFactory(column -> {
            TableCell<ActiveIncidentsTable, Integer> cell = new TableCell<>() {
                @Override
                protected void updateItem(Integer item, boolean empty) {
                    super.updateItem(item, empty);
                    if (empty || item == null) {
                        setText(null);
                    } else {
                        setText(item.toString());
                    }
                }
            };
        
            cell.setOnMouseClicked(event -> {
                if (!cell.isEmpty() && event.getClickCount() == 1) {
                    ActiveIncidentsTable selectedIncident = activeIncidentsTable.getItems().get(cell.getIndex());
                    openRescueePopup(selectedIncident);
                }
            });
        
            return cell;
        });  
       
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
    //🚨🚨🚨 EDIT BUTTON - FOR ACTIVE INCIDENTS TABLE. ONCE CLICKED, SOME OF THE CELLS BECOME EDITABLE
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
            rescueeNameCol
        );
    }

    //🚨🚨🚨 SAVE BUTTON - ONCE CLICKED, THE VALUES ARE EDITED NOW IN THE DATABASE 
    @FXML
    private void handleSaveButton() {
        Label messageLabel = new Label("Changes have been saved successfully!");
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
        for (ActiveIncidentsTable incident : activeIncidentsTable.getItems()) {
            String incidentNumber = incident.getIncidentNumber();
            String emergencyType = incident.getEmergencyType();
            String emergencyStatus = incident.getEmergencyStatus();
            String emergencySeverity = incident.getEmergencySeverity();
            String barangayLocation = incident.getBarangayLocation();
    
            int children = incident.getChildren();
            int adults = incident.getAdults();
            int seniors = incident.getSeniors();
    
            if (children == 0 && adults == 0 && seniors == 0) {
                int[] previousCounts = DBService.getPeopleCountsByIncidentNumber(incidentNumber);
                children = previousCounts[0];
                adults = previousCounts[1];
                seniors = previousCounts[2];
            }
    
            String[] nameParts = incident.getRescueeName().split(" ", 2);
            String firstName = nameParts.length > 0 ? nameParts[0] : "";
            String lastName = nameParts.length > 1 ? nameParts[1] : "";
    
            int numOfRescuee = children + adults + seniors;
    
            DBService.updateIncidentAndRescuee(
                incidentNumber,
                emergencyType,
                emergencyStatus,
                emergencySeverity,
                barangayLocation,
                firstName,
                lastName,
                numOfRescuee,
                children,
                adults,
                seniors
            );
        }
    
        activeIncidentsTable.refresh();
        System.out.println("All incidents updated in table!");
    }
    
    //🚨🚨🚨 FOR INPUTTING THE DETAILED NUMBER OF RESCUEE (# of children, adults, seniors)
    private void openRescueePopup(ActiveIncidentsTable selectedIncident) {
    try {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("S&RRescueeCountPopUp.fxml"));
        AnchorPane page = loader.load();

        Stage dialogStage = new Stage();
        dialogStage.setTitle("Rescuee Breakdown");
        dialogStage.initModality(Modality.APPLICATION_MODAL);
        dialogStage.initOwner(rootPane.getScene().getWindow());
        dialogStage.setScene(new Scene(page));

        RescueeCountPopUpController controller = loader.getController();
        controller.setDialogStage(dialogStage);

        dialogStage.showAndWait();

        if (controller.isSaveClicked()) {
            int children = controller.getChildren();
            int adults = controller.getAdults();
            int seniors = controller.getSeniors();

            selectedIncident.setChildren(children);
            selectedIncident.setAdults(adults);
            selectedIncident.setSeniors(seniors);
            selectedIncident.setNumOfRescuee(children + adults + seniors);

            activeIncidentsTable.refresh();
        }

    } catch (IOException e) {
        e.printStackTrace();
    }
}

    
    //🚨🚨🚨 DISPATCH BUTTON
    @FXML
    private void handleDispatchButtonClick() {
        ActiveIncidentsTable selectedIncident = activeIncidentsTable.getSelectionModel().getSelectedItem();

        if (selectedIncident == null) {
            showAlert("No Incident Selected", "Please select an incident to dispatch.");
            return;
        }

        String incidentNumber = selectedIncident.getIncidentNumber();
        String barangayName = selectedIncident.getBarangayLocation();

        int barangayID = DatabaseHandler.getBarangayIDFromName(barangayName);
        if (barangayID == -1) {
            showAlert("Invalid Barangay", "The selected Barangay could not be found.");
            return;
        }

        boolean success = DatabaseHandler.insertToHistory(incidentNumber, barangayID);

        if (success) {
            selectedIncident.setEmergencyStatus("Dispatched");
            activeIncidentsTable.refresh();
            activeIncidentsTable.getItems().sort((a, b) ->
                Boolean.compare(
                    a.getEmergencyStatus().equalsIgnoreCase("Dispatched"),
                    b.getEmergencyStatus().equalsIgnoreCase("Dispatched")
                )
            );

            showAlert("Dispatch Successful", "Incident dispatched and recorded in history.");
        } else {
            showAlert("Dispatch Failed", "An error occurred while dispatching the incident.");
        }
    }



    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
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
    private void loadHistoryTable() { 
    List<HistoryTable> data = DBService.getHistory();
    System.out.println("Fetched history count: " + data.size()); // DEBUG: check if data is retrieved
    historyList.setAll(data);
    historyTable.setItems(historyList);
}

    public void refreshIncidentsTable() {
        ObservableList<ActiveIncidentsTable> data = DBService.getActiveIncidents();
        activeIncidentsTable.setItems(data);
    }
}

    