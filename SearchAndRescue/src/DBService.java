
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
    
}