import java.util.HashSet;
import java.util.Set;
import javafx.collections.FXCollections;

import javafx.collections.ObservableList;
import javafx.scene.control.TableColumn;
import javafx.scene.control.cell.ComboBoxTableCell;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.util.converter.IntegerStringConverter;

public class TableEditor {
    private static final Set<ActiveIncidentsTable> editedIncidentTable = new HashSet<>(); // Set to store edited incidents

    public static Set<ActiveIncidentsTable> getEditedRows() {
        return editedIncidentTable;
    }

    public static void makeActiveIncidentsTableEditable(
        TableColumn <ActiveIncidentsTable, String> emergencyTypeCol, //combobox
        TableColumn <ActiveIncidentsTable, String> emergencyStatusCol) { //combobox
        //TableColumn <ActiveIncidentsTable, String> locationCol) { //combobox
        // TableColumn <ActiveIncidentsTable, String> rescueeNameCol) {
    
    ObservableList<BarangayTable> barangayTableList = DBService.getAllBarangayName();

    ObservableList<String> barangayNames = FXCollections.observableArrayList();
    for (BarangayTable b : barangayTableList) {
        barangayNames.add(b.getBarangayName());
    
    }
     
    emergencyTypeCol.setCellFactory(ComboBoxTableCell.forTableColumn("Flood", "Earthquake", "Landslide"));    
    emergencyTypeCol.setOnEditCommit(event -> {
        ActiveIncidentsTable incident = event.getRowValue();
        String newEmergencyType = event.getNewValue();
        incident.setEmergencyType(newEmergencyType);
        editedIncidentTable.add(incident);   

     });

     emergencyStatusCol.setCellFactory(ComboBoxTableCell.forTableColumn("QUEUED", "DISPATCHED", "ON SCENE"));
     emergencyStatusCol.setOnEditCommit(event -> {
        ActiveIncidentsTable incident = event.getRowValue();
        String newEmergencyStatus = event.getNewValue();
        incident.setEmergencyStatus(newEmergencyStatus);
        editedIncidentTable.add(incident);   
     });

    //  locationCol.setCellFactory(ComboBoxTableCell.forTableColumn(barangayNames));
    //  locationCol.setOnEditCommit(event -> {
    //      ActiveIncidentsTable incident = event.getRowValue();
    //      String newBarangayName = event.getNewValue();
    //      incident.setBarangayLocation(newBarangayName);
    //      editedIncidentTable.add(incident);
    //  });

    //  rescueeNameCol.setCellFactory(TextFieldTableCell.forTableColumn());
    //  rescueeNameCol.setOnEditCommit(event -> {
    //      ActiveIncidentsTable incident = event.getRowValue();
    //      String newRescueeName = event.getNewValue();
    //      incident.setRescueeName(newRescueeName);
    //      editedIncidentTable.add(incident);   
    //  });

    // numOfRescueeCol.setCellFactory(TextFieldTableCell.forTableColumn(new IntegerStringConverter()));
    // numOfRescueeCol.setOnEditCommit(event -> {
    //     ActiveIncidentsTable incident = event.getRowValue();
    //     Integer newNumOfRescuee = event.getNewValue(); // Now it's an Integer
    //     incident.setNumOfRescuee(newNumOfRescuee); 
    //     editedIncidentTable.add(incident);   
    // });


    }
    public static void clearEditedRows() {
        editedIncidentTable.clear();
  }
}
