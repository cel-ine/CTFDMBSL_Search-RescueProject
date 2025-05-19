import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.List;
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
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
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
    @FXML Button editButton, saveButton, addRescueButton, dispatchButton, cancelBTN;
    @FXML TableColumn <ActiveIncidentsTable, String> emergencyTypeCol;
    @FXML TableColumn <ActiveIncidentsTable, String> emergencyStatusCol;
    @FXML TableColumn <ActiveIncidentsTable, String> emergencySeverityCol;
    @FXML TableColumn <ActiveIncidentsTable, String> incidentNumCol;
    @FXML TableColumn <ActiveIncidentsTable, LocalDate> timeCreatedCol;
    @FXML TableColumn <ActiveIncidentsTable, String> locationCol;
    @FXML TableColumn <ActiveIncidentsTable, String> rescueeNameCol;
    @FXML TableColumn <ActiveIncidentsTable, Integer> numOfRescueeCol;
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
        rescueeNameCol.setCellValueFactory(new PropertyValueFactory<>("rescueeName"));
        numOfRescueeCol.setCellValueFactory(new PropertyValueFactory<>("numOfRescuee"));
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
        nameCol.setCellValueFactory(new PropertyValueFactory<>("rescueeNameHistory"));
        numOfRescueeHCol.setCellValueFactory(new PropertyValueFactory<>("numOfRescueeHistory"));
        incidentReport.setCellValueFactory(new PropertyValueFactory<>("incidentReport"));
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
                            setText("        📝"); 
                        } else {
                            setText("        ➕"); 
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
        txtDate.setText("📆 " + currentDate.format(DateTimeFormatter.ofPattern("EEEE, MMMM d, yyyy")));
    }

    private void updateTime() {
        LocalTime currentTimePH = LocalTime.now(ZoneId.of("Asia/Manila"));
        txtTime.setText("⏰ " + currentTimePH.format(DateTimeFormatter.ofPattern("hh:mm:ss a")));
    }
    
    private void Counter() {
        LocalDate today = LocalDate.now();
        int count = DatabaseHandler.getActiveIncidentCountByStatusAndDate("QUEUED", today);
        counterText.setText("Total Active Incidents: " + count);

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
            stage.setScene(new Scene(root));
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleEditButtonClick() { //EDIT BUTTON - FOR ACTIVE INCIDENTS TABLE. ONCE CLICKED, SOME OF THE CELLS BECOME EDITABLE
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
        for (ActiveIncidentsTable incident : activeIncidentsTable.getItems()) {
            String incidentNumber = incident.getIncidentNumber();
            String emergencyStatus = incident.getEmergencyStatus();

            if ("DISPATCHED".equalsIgnoreCase(emergencyStatus)) {
                int historyID = DatabaseHandler.getHistoryIDByIncidentNumber(incidentNumber);
                if (historyID != -1) {
                    EmergencyReport report = DatabaseHandler.getReportByHistoryID(historyID);
                    if (report != null) {
                        showAlert("Save Not Allowed", "You cannot change the status of a dispatched emergency that already has an incident report.");
                        return; // Block saving all changes
                    }
                }
            }
        }

        Label messageLabel = new Label("Changes have been saved successfully!");
        messageLabel.setStyle("-fx-background-color: rgba(0, 0, 0, 0.7); -fx-text-fill: white; -fx-padding: 10px;");
        messageLabel.setMaxWidth(800);
        messageLabel.setMaxHeight(50);
        messageLabel.setTranslateX(400);
        messageLabel.setTranslateY(400);
        rootPane.getChildren().add(messageLabel);

        activeIncidentsTable.setEditable(false);

        Timeline timeline = new Timeline(
            new KeyFrame(Duration.seconds(3), event -> rootPane.getChildren().remove(messageLabel))
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

            int barangayID = DatabaseHandler.getBarangayIDFromName(barangayLocation);

            if (emergencyStatus.equalsIgnoreCase("DISPATCHED")) {
                if (!DatabaseHandler.isAlreadyInHistory(incidentNumber)) {
                    Integer historyID = DatabaseHandler.insertToHistory(incidentNumber, barangayID);
                    if (historyID != null) {
                        System.out.println("Inserted into history with ID: " + historyID);
                    }
                }
            } else {
                if (DatabaseHandler.isAlreadyInHistory(incidentNumber)) {
                    DatabaseHandler.removeFromHistory(incidentNumber);
                    loadHistoryTable();
                }
            }
        }

    activeIncidentsTable.refresh();
    applyUrgentRowHighlighting();
    loadHistoryTable();
}
    @FXML
    private void handleCancelButton() {
        activeIncidentsTable.setEditable(false);
        activeIncidentsTable.refresh();
        applyUrgentRowHighlighting();
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

        if (DatabaseHandler.isAlreadyDispatched(incidentNumber)) {
            showAlert("Already Dispatched", "This incident has already been dispatched.");
            return;
        }

        Integer historyID = DatabaseHandler.insertToHistory(incidentNumber, barangayID);
        if (historyID != null) {
            activeIncidentsTable.getItems().remove(selectedIncident);
            showAlert("Dispatch Successful", "Incident dispatched and recorded in history.");
            loadHistoryTable();

            refreshIncidentsTable();
        } else {
            showAlert("Dispatch Failed", "An error occurred while dispatching the incident.");
        }
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

    // ALERT POP-UP
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
                } else if (item.equalsIgnoreCase("ENROUTE")) {
                    setStyle("-fx-background-color: #FFA500; -fx-text-fill: white; -fx-background-insets: 4 2");
                } else if (item.equalsIgnoreCase("ON SCENE")) {
                    setStyle("-fx-background-color: #1368bd; -fx-text-fill: white; -fx-background-insets: 4 2");
                } else if (item.equalsIgnoreCase("DISPATCHED")) {
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
                } else if (status.equalsIgnoreCase("ENROUTE")) {
                    setStyle("-fx-text-fill: #FFA500; -fx-font-weight: bold;");
                } else if (status.equalsIgnoreCase("ON SCENE")) {
                    setStyle("-fx-text-fill: #1368bd; -fx-font-weight: bold;");
                } else if (status.equalsIgnoreCase("DISPATCHED")) {
                    setStyle("-fx-text-fill: #21b111; -fx-font-weight: bold;");
                } else {
                    setStyle("-fx-text-fill: black;");
                }
            }
        }
    });
    } 
}