import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import java.util.Timer;
import java.util.TimerTask;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;
import javafx.scene.web.WebView;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Duration;

public class TabPaneController {
    @FXML
    private AnchorPane rootPane;
    //🏠🏠🏠 HOME TAB - BARANGAY TABLE
    @FXML private WebView emergencyLocationMap;
    @FXML Text txtDate, txtTime, counterText;
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
    @FXML TextField searchTF;
    @FXML TableView <ActiveIncidentsTable> activeIncidentsTable;
    @FXML Button editButton, saveButton, addRescueButton, completedButton, cancelBTN;
    @FXML TableColumn <ActiveIncidentsTable, String> emergencyTypeCol;
    @FXML TableColumn <ActiveIncidentsTable, String> emergencyStatusCol;
    @FXML TableColumn <ActiveIncidentsTable, String> emergencySeverityCol;
    @FXML TableColumn <ActiveIncidentsTable, String> incidentNumCol;
    @FXML TableColumn <ActiveIncidentsTable, LocalDate> timeCreatedCol;
    @FXML TableColumn <ActiveIncidentsTable, String> locationCol;
    @FXML TableColumn <ActiveIncidentsTable, String> personInChargeCol;
    @FXML TableColumn <ActiveIncidentsTable, Integer> numOfRescueeCol;
    @FXML TableColumn <ActiveIncidentsTable, String> addressCol;
    @FXML TableColumn <ActiveIncidentsTable, Integer> contactNumCol;
    @FXML TableColumn <ActiveIncidentsTable, String> officerInChargeCol;

    private ObservableList<ActiveIncidentsTable> incidentsList = FXCollections.observableArrayList();

    //🗂️🗂️🗂️ INCIDENT REPORT & HISTORY TAB
    @FXML TextField historySearchTF;
    @FXML Button  deleteBTN;
    @FXML TableView <HistoryTable> historyTable;
    @FXML TableColumn <HistoryTable, String> incidentReport;
    @FXML TableColumn <HistoryTable, String> emTypeCol;
    @FXML TableColumn <HistoryTable, String> emSevCol;
    @FXML TableColumn <HistoryTable, String> incidentNumHCol;
    @FXML TableColumn <HistoryTable, LocalDateTime> dispatchedTimeCol;
    @FXML TableColumn <HistoryTable, String> brgyLocCol;
    @FXML TableColumn <HistoryTable, String> nameCol;
    @FXML TableColumn <HistoryTable, String> numOfRescueeHCol;
    @FXML TableColumn <HistoryTable, String> addressHCol;
    @FXML TableColumn <HistoryTable, String> contactNumHCol;
    @FXML TableColumn <HistoryTable, String> officerInChargeHistoryCol;
    private ObservableList<HistoryTable> historyList = FXCollections.observableArrayList();



    public void initialize() {
         try {
            String htmlFilePath = getClass().getResource("Location.html").toExternalForm();
            emergencyLocationMap.getEngine().load(htmlFilePath);
        } catch (Exception e) {
            System.err.println("Error loading HTML file: " + e.getMessage());
            e.printStackTrace();
        }

        startDateTimeUpdater();
        Counter();
        //HOME TAB - BARANGAY TABLE
        brgyNameCol.setCellValueFactory(new PropertyValueFactory<>("barangayName"));
        brgyRescueCountCol.setCellValueFactory(new PropertyValueFactory<>("barangayRescueCount"));
        brgyDistanceCol.setCellValueFactory(new PropertyValueFactory<>("barangayDistance"));
        loadBarangayTable();
       
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
        personInChargeCol.setCellValueFactory(new PropertyValueFactory<>("personInCharge"));
        numOfRescueeCol.setCellValueFactory(new PropertyValueFactory<>("numOfRescuee"));
        addressCol.setCellValueFactory(new PropertyValueFactory<>("address"));
        contactNumCol.setCellValueFactory(new PropertyValueFactory<>("contactNum"));
        officerInChargeCol.setCellValueFactory(new PropertyValueFactory<>("officerInCharge"));

        searchTF.textProperty().addListener((observable, oldValue, newValue) -> filterActiveIncidentsTable(newValue));

        refreshIncidentsTable();
        applyUrgentRowHighlighting();

        //HISTORY TAB
        incidentReport.setCellValueFactory(new PropertyValueFactory<>("incidentReport")); //incidenttttt
        emTypeCol.setCellValueFactory(new PropertyValueFactory<>("emType"));
        emSevCol.setCellValueFactory(new PropertyValueFactory<>("emSeverity"));
        incidentNumHCol.setCellValueFactory(new PropertyValueFactory<>("incidentNumHistory"));
        dispatchedTimeCol.setCellValueFactory(new PropertyValueFactory<>("dispatchedTime"));
        brgyLocCol.setCellValueFactory(new PropertyValueFactory<>("barangayName"));
        nameCol.setCellValueFactory(new PropertyValueFactory<>("personInChargeHistory"));
        numOfRescueeHCol.setCellValueFactory(new PropertyValueFactory<>("numOfRescueeHistory"));
        addressHCol.setCellValueFactory(new PropertyValueFactory<>("addressHistory"));
        contactNumHCol.setCellValueFactory(new PropertyValueFactory<>("contactNumHistory"));
        officerInChargeHistoryCol.setCellValueFactory(new PropertyValueFactory<>("officerInChargeHistory"));


        historySearchTF.textProperty().addListener((observable, oldValue, newValue) -> filterHistoryTable(newValue));
        loadHistoryTable();

        incidentReport.setCellFactory(column -> { //REPORT COLUMN BE CLICKABLE AND SHOW THE POP UP 
            TableCell<HistoryTable, String> cell = new TableCell<>() {
                @Override
                protected void updateItem(String item, boolean empty) {
                    super.updateItem(item, empty);
                    if (empty) {
                        setText(null);
                        setGraphic(null);
                    } else {
                        HistoryTable history = getTableView().getItems().get(getIndex());
                        
                        boolean hasReport = history.getIncidentReport() != null && !history.getIncidentReport().isEmpty();
                        if (hasReport) {
                            setText("     📝"); 
                        } else {
                            setText("     ➕"); 
                        }
                    }
                }
            };
            cell.setOnMouseClicked(event -> {
                if (!cell.isEmpty() && event.getClickCount() == 1) {
                    HistoryTable selectedHistory = cell.getTableView().getItems().get(cell.getIndex());
                    addReportPopUp(selectedHistory);
                }
            });
            return cell;
        });

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
    }

    
    private void startDateTimeUpdater() {
        Timer timer = new Timer(true);
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                Platform.runLater(() -> {
                    updateDate();
                    updateTime();
                });
            }
        }, 0, 1000);
    }

    //🏠🏠🏠 HOME TAB
    private void updateDate() {
        LocalDate currentDate = LocalDate.now(ZoneId.of("Asia/Manila"));
        txtDate.setText(currentDate.format(DateTimeFormatter.ofPattern("EEEE, MMMM d, yyyy")));
    }

    private void updateTime() {
        LocalTime currentTimePH = LocalTime.now(ZoneId.of("Asia/Manila"));
        txtTime.setText(currentTimePH.format(DateTimeFormatter.ofPattern("hh:mm:ss a")));
    }
    
    private void Counter() {
        LocalDate today = LocalDate.now();
        int count = DatabaseHandler.getActiveIncidentCountByStatusAndDate("QUEUED", today);
        counterText.setText("Daily Total Active Incidents: " + count);

    }

    
    //🚨🚨🚨 ACTIVE INCIDENTS TAB
    private void filterActiveIncidentsTable (String searchText) { // 🔍 SEARCH FUNCTION ACTIVE INCIDENTS TABLE
        ObservableList<ActiveIncidentsTable> filteredList = incidentsList.filtered(incdident ->
            incdident.getEmergencyType().toLowerCase().contains(searchText.toLowerCase()) ||
            incdident.getEmergencyStatus().toLowerCase().contains(searchText.toLowerCase()) ||
            incdident.getEmergencySeverity().toLowerCase().contains(searchText.toLowerCase()) ||
            incdident.getBarangayLocation().toLowerCase().contains(searchText.toLowerCase())
        );
        activeIncidentsTable.setItems(filteredList);
    }

    @FXML
    private void openAddRescuePopUp(ActionEvent event) { 
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("addRescueUI.fxml"));
            Parent root = loader.load();

            addRescueController AddRescueController = loader.getController();
            AddRescueController.setTabPaneController(this);

            Stage stage = new Stage();
            AddRescueController.setDialogStage(stage); 
            stage.setScene(new Scene(root));
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setTitle("Add new rescue");
            stage.getIcons().add(new Image(getClass().getResourceAsStream("pasigLogo.jpg")));
            stage.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    // MAP
    public void addIncidentMarkerToMap(String incidentNumber, String barangayName, String status, int totalPeople,
            String emergencyType) {
        Platform.runLater(() -> {
            String script = String.format(
                    "addIncidentMarker('%s', '%s', '%s', %d, '%s');",
                    incidentNumber.replace("'", "\\'"),
                    barangayName.replace("'", "\\'"),
                    status.replace("'", "\\'"),
                    totalPeople,
                    emergencyType.replace("'", "\\'"));
            emergencyLocationMap.getEngine().executeScript(script);
        });
    }

    @FXML
    private void handleEditButtonClick() { //EDIT BUTTON - FOR ACTIVE INCIDENTS TABLE. ONCE CLICKED, SOME OF THE CELLS BECOME EDITABLE
        Label messageLabel = new Label("You are now in editing mode. You can only Edit Emergency Status and Severity.");
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
            emergencyStatusCol
        );
    }

    //🚨🚨🚨 SAVE BUTTON - ONCE CLICKED, THE VALUES ARE EDITED NOW IN THE DATABASE 
    @FXML
    private void handleSaveButton() {
        StringBuilder blockedIncidents = new StringBuilder();
        boolean anySaved = false;
        int totalToSave = TableEditor.getEditedRows().size();
        int blockedCount = 0;

        for (ActiveIncidentsTable incident : TableEditor.getEditedRows()) {
            String incidentNumber = incident.getIncidentNumber();

            // Block ALL edits if already in history
            if (DatabaseHandler.isAlreadyInHistory(incidentNumber)) {
                blockedIncidents.append(incidentNumber).append("\n");
                blockedCount++;
                continue;
            }

            String emergencyStatus = incident.getEmergencyStatus();
            String emergencyType = incident.getEmergencyType();
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

            int numOfRescuee = children + adults + seniors;

            DBService.updateIncidentAndRescuee(
                incidentNumber,
                emergencyType,
                emergencyStatus,
                emergencySeverity,
                barangayLocation,
                numOfRescuee,
                children,
                adults,
                seniors
            );

            int barangayID = DatabaseHandler.getBarangayIDFromName(barangayLocation);

            if (emergencyStatus.equalsIgnoreCase("COMPLETED")) {
                Integer historyID = DatabaseHandler.insertToHistory(incidentNumber, barangayID);
                if (historyID != null) {
                    System.out.println("Inserted into history with ID: " + historyID);
                }
            } else {
                if (DatabaseHandler.isAlreadyInHistory(incidentNumber)) {
                    DatabaseHandler.removeFromHistory(incidentNumber);
                    loadHistoryTable();
                }
            }
            anySaved = true;
        }

        if (blockedCount == totalToSave) {
            showAlert(
                "Edit Not Allowed",
                "You cannot edit any information for the following emergencies that is already marked completed and in the history:\n\n" + blockedIncidents
            );
            TableEditor.getEditedRows().clear();
            return;
        }

        if (blockedIncidents.length() > 0) {
            showAlert(
                "Edit Not Allowed",
                "You cannot edit any information for the following emergencies that are already marked as completed and in the history:\n\n" + blockedIncidents
            );
        }

        if (anySaved) {
            Label messageLabel = new Label("Changes has been saved successfully!");
            messageLabel.setStyle("-fx-background-color: rgba(0, 0, 0, 0.7); -fx-text-fill: white; -fx-padding: 10px;");
            messageLabel.setMaxWidth(800);
            messageLabel.setMaxHeight(50);
            messageLabel.setTranslateX(600);
            messageLabel.setTranslateY(400);
            rootPane.getChildren().add(messageLabel);

            activeIncidentsTable.setEditable(false);

            Timeline timeline = new Timeline(
                new KeyFrame(Duration.seconds(3), event -> rootPane.getChildren().remove(messageLabel))
            );
            timeline.play();

            activeIncidentsTable.refresh();
            applyUrgentRowHighlighting();
            loadHistoryTable();
        }
        TableEditor.getEditedRows().clear();
    }

    @FXML
    private void handleCancelButton() {
        activeIncidentsTable.setEditable(false);
        activeIncidentsTable.refresh();
        activeIncidentsTable.getItems();
        refreshIncidentsTable();
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
        dialogStage.setTitle("Update Rescuee Count");
        dialogStage.getIcons().add(new Image(getClass().getResourceAsStream("pasigLogo.jpg")));
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
    private void handleRescueCompletedButtonClick() {
        ActiveIncidentsTable selectedIncident = activeIncidentsTable.getSelectionModel().getSelectedItem();

        if (selectedIncident == null) {
            showAlert("No Incident Selected", "Please select an incident to mark as completed.");
            return;
        }
        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
        confirm.setTitle("Confirm");
        confirm.setHeaderText(null);
        confirm.setContentText("Are you sure you want to mark this this incident as completed?");
        Stage alertStage = (Stage) confirm.getDialogPane().getScene().getWindow();
        alertStage.getIcons().add(new javafx.scene.image.Image(getClass().getResourceAsStream("pasigLogo.jpg")));
        if (confirm.showAndWait().orElse(ButtonType.CANCEL) != ButtonType.OK) {
            return;
        } 

        String incidentNumber = selectedIncident.getIncidentNumber();
        String barangayName = selectedIncident.getBarangayLocation();
        int barangayID = DatabaseHandler.getBarangayIDFromName(barangayName);

        if (barangayID == -1) {
            showAlert("Invalid Barangay", "The selected Barangay could not be found.");
            return;
        }

        if (DatabaseHandler.isAlreadyCompleted(incidentNumber)) {
            showAlert("Already marked Complete", "This incident has already been completed.");
            return;
        }

        Integer historyID = DatabaseHandler.insertToHistory(incidentNumber, barangayID);
        if (historyID != null) {
            incidentsList.remove(selectedIncident);            
            showAlert("Incident completed", "Incident is marked as complete and recorded in history.");
            loadHistoryTable();
            refreshIncidentsTable();
            removeIncidentFromMap(incidentNumber);

        } else {
            showAlert("Marking as complete Failed", "An error occurred while marking the incident.");
        }
    }
    public void removeIncidentFromMap(String incidentNumber) {
        Platform.runLater(() -> {
            String script = String.format(
                    "removeIncidentFromMap('%s');",
                    incidentNumber.replace("'", "\\'"));
            emergencyLocationMap.getEngine().executeScript(script);
        });
    }
    ///🥀🥀🥀 HISTORY TAB
    private void filterHistoryTable (String searchHistory) { // 🔍 SEARCH FUNCTION ACTIVE INCIDENTS TABLE
        ObservableList<HistoryTable> filteredList = historyList.filtered(history ->
            history.getEmType().toLowerCase().contains(searchHistory.toLowerCase()) ||
            history.getEmSeverity().toLowerCase().contains(searchHistory.toLowerCase()) ||
            history.getIncidentNumHistory().toLowerCase().contains(searchHistory.toLowerCase()) ||
            history.getBarangayName().toLowerCase().contains(searchHistory.toLowerCase())
        );
        historyTable.setItems(filteredList);
    }

    private void addReportPopUp(HistoryTable selectedHistory) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("S&RRescueReport.fxml"));
            Parent root = loader.load();
            AddReportController controller = loader.getController();
            controller.setHistoryData(selectedHistory);
            Stage stage = new Stage();
            stage.setTitle("Incident Details");
            stage.setScene(new Scene(root));
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.initOwner(rootPane.getScene().getWindow());
            stage.getIcons().add(new Image(getClass().getResourceAsStream("pasigLogo.jpg")));
            stage.showAndWait();
            loadHistoryTable();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleDeleteHistory() {
    HistoryTable selectedHistory = historyTable.getSelectionModel().getSelectedItem();
    if (selectedHistory == null) {
        showAlert("No Selection", "Please select a history record to delete.");
        return;
    }

    Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
    confirm.setTitle("Delete Confirmation");
    confirm.setHeaderText(null);
    confirm.setContentText("Are you sure you want to delete this history record?");
    Stage alertStage = (Stage) confirm.getDialogPane().getScene().getWindow();
    alertStage.getIcons().add(new Image(getClass().getResourceAsStream("pasigLogo.jpg")));
    if (confirm.showAndWait().orElse(ButtonType.CANCEL) != ButtonType.OK) {
        return;
    }

    boolean success = DatabaseHandler.deleteFromHistory(selectedHistory.getIncidentNumHistory());
    if (success) {
        showAlert("Deleted", "History record deleted successfully.");
        loadHistoryTable();
    } else {
        showAlert("Delete Failed", "Could not delete the selected history record.");
    }
} 

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        Stage alertStage = (Stage) alert.getDialogPane().getScene().getWindow();
        alertStage.getIcons().add(new javafx.scene.image.Image(getClass().getResourceAsStream("pasigLogo.jpg")));
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
        historyList.setAll(data); 
        historyTable.setItems(historyList); 
    }
    public void refreshIncidentsTable() {
        incidentsList.setAll(DBService.getActiveIncidents());
        activeIncidentsTable.setItems(incidentsList);
        applyUrgentRowHighlighting();
    }

    //🎗️🎗️🎗️ styles for columns in active incidents table
    private void applyUrgentRowHighlighting() {
        emergencyStatusCol.setCellFactory(column -> new TableCell<>() {
        @Override
        protected void updateItem(String item, boolean empty) {
            super.updateItem(item, empty);

            if (item == null || empty) {
                setText(null);    
                setStyle("");     
            } else {
                setText(item);    
                    
                if (item.equalsIgnoreCase("QUEUED")) {
                    setStyle("-fx-background-color: #ba0f0f; -fx-text-fill: white; -fx-background-insets: 4 2");
                } else if (item.equalsIgnoreCase("DISPATCHED")) {
                    setStyle("-fx-background-color: #FFA500; -fx-text-fill: white; -fx-background-insets: 4 2");
                } else if (item.equalsIgnoreCase("ON SCENE")) {
                    setStyle("-fx-background-color: #1368bd; -fx-text-fill: white; -fx-background-insets: 4 2");
                } else if (item.equalsIgnoreCase("COMPLETED")) {
                    setStyle("-fx-background-color: #21b111; -fx-text-fill: white; -fx-background-insets: 4 2");
                } else {
                    setStyle(""); // Default style
                }
            }
        }
    });
        
        incidentNumCol.setCellFactory(column -> new TableCell<>() {
        @Override
        protected void updateItem(String item, boolean empty) {
            super.updateItem(item, empty);

            if (item == null || empty) {
                setText(null);
                setStyle("");
            } else {
                setText(item);

                ActiveIncidentsTable rowItem = getTableView().getItems().get(getIndex());
                String status = rowItem.getEmergencyStatus();

                if (status.equalsIgnoreCase("QUEUED")) {
                    setStyle("-fx-text-fill: #ba0f0f; -fx-font-weight: bold;");
                } else if (status.equalsIgnoreCase("DISPATCHED")) {
                    setStyle("-fx-text-fill: #FFA500; -fx-font-weight: bold;");
                } else if (status.equalsIgnoreCase("ON SCENE")) {
                    setStyle("-fx-text-fill: #1368bd; -fx-font-weight: bold;");
                } else if (status.equalsIgnoreCase("COMPLETED")) {
                    setStyle("-fx-text-fill: #21b111; -fx-font-weight: bold;");
                } else {
                    setStyle("-fx-text-fill: black;");
                }
            }
        }
    });
    } 
}


