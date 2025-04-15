
import java.io.IOException;
import java.time.LocalDate;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class TabPaneController {
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
    @FXML TableColumn <ActiveIncidentsTable, String> emergencyTypeCol;
    @FXML TableColumn <ActiveIncidentsTable, String> emergencyStatusCol;
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
}    