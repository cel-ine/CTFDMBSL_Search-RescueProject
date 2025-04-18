
import java.sql.SQLException;

import javafx.collections.ObservableList;


//class to handle all datas and interact with the database
public class DBService {
    //HOMEPAGE
    public static ObservableList<BarangayTable> getAllBarangayInfo() {
        return DatabaseHandler.displayBarangay();
    }
    public static ObservableList<BarangayDescTable> getAllBarangayDescription() {
        return DatabaseHandler.displayBarangayDesc();
    }

    // active incidents tab
    public static ObservableList<ActiveIncidentsTable> getActiveIncidents(){
        return DatabaseHandler.displayAllActiveIncidents();
    }


    // ADD RESCUE FXML
    public static ObservableList<BarangayTable> getAllBarangayName() {
        return DatabaseHandler.loadBarangays(); 
    }
    

    public static void updateIncidentAndRescuee(
        String incidentNumber,
        String emergencyType,
        String emergencyStatus,
        String emergencySeverity,
        String barangayLocation,
        String firstName,
        String lastName,
        int numOfRescuee,
        int children,
        int adults,
        int seniors
    ) {
        try {
            DatabaseHandler.updateActiveIncident(
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
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static int[] getPeopleCountsByIncidentNumber(String incidentNumber) {
        try {
            return DatabaseHandler.getPeopleCountsByIncidentNumber(incidentNumber);
        } catch (SQLException e) {
            e.printStackTrace();
            return new int[]{0, 0, 0};
        }
    }
    
}