
import java.sql.SQLException;

import javafx.collections.ObservableList;


//class to handle all datas and interact with the database
public class DBService {
    //login
    public static boolean login(String username, Integer pin) {
        return DatabaseHandler.validateLogin(username, pin);
    }
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
     public static ObservableList<HistoryTable> getHistory() {
        return DatabaseHandler.loadHistory(); 
    }
    

    public static void updateIncidentAndRescuee(
        String incidentNumber,
        String emergencyType,
        String emergencyStatus,
        String emergencySeverity,
        String barangayLocation,
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

