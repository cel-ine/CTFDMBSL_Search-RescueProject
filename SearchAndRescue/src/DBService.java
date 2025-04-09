
import javafx.collections.ObservableList;


//class to handle all datas and interact with the database
public class DBService {
    public static ObservableList<BarangayTable> getAllBarangayInfo() {
        return DatabaseHandler.displayBarangay();
    }
    public static ObservableList<BarangayDescTable> getAllBarangayDescription() {
        return DatabaseHandler.displayBarangayDesc();
    }
}