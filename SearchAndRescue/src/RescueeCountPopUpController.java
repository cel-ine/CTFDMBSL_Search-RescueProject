import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class RescueeCountPopUpController {

    @FXML private TextField childrenTF;
    @FXML private TextField adultTF;
    @FXML private TextField seniorsTF;
    @FXML private Button saveBTN;
    @FXML private Button cancelBTN;

    private Stage dialogStage;
    private boolean saveClicked = false;

    // The instance of the ActiveIncidentsTable to update the properties
    private ActiveIncidentsTable activeIncidentsTable;

    // Store the values to be returned
    private int children, adults, seniors;

    public void setDialogStage(Stage dialogStage) {
        this.dialogStage = dialogStage;
    }

    // Setter for ActiveIncidentsTable to update the values
    public void setActiveIncidentsTable(ActiveIncidentsTable activeIncidentsTable) {
        this.activeIncidentsTable = activeIncidentsTable;
    }

    public boolean isSaveClicked() {
        return saveClicked;
    }

    public int getChildren() {
        return children;
    }

    public int getAdults() {
        return adults;
    }

    public int getSeniors() {
        return seniors;
    }

    @FXML
    private void initialize() {
        saveBTN.setOnAction(e -> handleSave());
        cancelBTN.setOnAction(e -> dialogStage.close());
    }

    private void handleSave() {
        try {
            // Parse inputs
            children = Integer.parseInt(childrenTF.getText());
            adults = Integer.parseInt(adultTF.getText());
            seniors = Integer.parseInt(seniorsTF.getText());

            // Ensure values are not negative (optional check)
            if (children < 0 || adults < 0 || seniors < 0) {
                throw new NumberFormatException("Negative values are not allowed.");
            }

            // If everything is valid, update the properties
            if (activeIncidentsTable != null) {
                activeIncidentsTable.setChildren(children);   // Update the table's children count
                activeIncidentsTable.setAdults(adults);       // Update the table's adults count
                activeIncidentsTable.setSeniors(seniors);     // Update the table's seniors count
            }

            saveClicked = true;
            dialogStage.close();  // Close the dialog after saving

        } catch (NumberFormatException e) {
            // Display an error alert if there's a problem with the input
            Alert alert = new Alert(AlertType.ERROR);
            alert.setTitle("Invalid Input");
            alert.setHeaderText("Please enter valid numeric values.");
            alert.setContentText("Make sure all fields contain non-negative numbers.");
            alert.showAndWait();
        }
    }
}
