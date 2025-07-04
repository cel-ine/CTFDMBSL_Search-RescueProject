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

    
    private ActiveIncidentsTable activeIncidentsTable;

    private int children, adults, seniors;

    public void setDialogStage(Stage dialogStage) {
        this.dialogStage = dialogStage;
    }

    private String emergencyStatus;

    public void setActiveIncidentsTable(ActiveIncidentsTable activeIncidentsTable) {
        this.activeIncidentsTable = activeIncidentsTable;
        this.emergencyStatus = activeIncidentsTable.getEmergencyStatus();
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
    if ("DISPATCHED".equalsIgnoreCase(emergencyStatus)) {
        Alert alert = new Alert(AlertType.ERROR);
        alert.setTitle("Edit Not Allowed");
        alert.setHeaderText("Cannot Edit Rescuee Count");
        alert.setContentText("You cannot update any informations if the rescue is DISPATCHED.");
        alert.showAndWait();
        return;
    }
        try {
            
            children = Integer.parseInt(childrenTF.getText());
            adults = Integer.parseInt(adultTF.getText());
            seniors = Integer.parseInt(seniorsTF.getText());

            if (children < 0 || adults < 0 || seniors < 0) {
                throw new NumberFormatException("Negative values are not allowed.");
            }

           
            if (activeIncidentsTable != null) {
                activeIncidentsTable.setChildren(children);   
                activeIncidentsTable.setAdults(adults);       
                activeIncidentsTable.setSeniors(seniors);     
            }

            saveClicked = true;
            dialogStage.close();  
            
        } catch (NumberFormatException e) {
            
            Alert alert = new Alert(AlertType.ERROR);
            alert.setTitle("Invalid Input");
            alert.setHeaderText("Please enter valid numeric values.");
            alert.setContentText("Make sure all fields contain non-negative numbers.");
            Stage alertStage = (Stage) alert.getDialogPane().getScene().getWindow();
            alertStage.getIcons().add(new javafx.scene.image.Image(getClass().getResourceAsStream("pasigLogo.jpg")));
            alert.showAndWait();
        }
    }
}
