import java.io.IOException;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class LoginController {
    @FXML private TextField usernameTF;
    @FXML private PasswordField pinPassField;
    @FXML private Button loginButton;
 
    @FXML 
    private void LoginButtonHandler(ActionEvent event) throws IOException {
        String username = usernameTF.getText();
        String pinText = pinPassField.getText(); 
    
        if (username.isEmpty() || pinText.isEmpty()) {
            showLoginError("Please fill in all fields.");
            return;
        }
    
        try {
            Integer pin = Integer.parseInt(pinText);
            if (DBService.login(username, pin)) {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("SearchAndRescueUI.fxml"));
                Parent root = loader.load();
                Scene scene = new Scene(root, 1366, 768);
                Stage stage = (Stage) loginButton.getScene().getWindow();
                stage.setScene(scene);
            } else {
                showLoginError("Invalid username or PIN. Please try again.");
            }
        } catch (NumberFormatException e) {
            showLoginError("PIN must be a valid number.");
        }
    }
    
    private void showLoginError(String message) {
        Alert alert = new Alert(AlertType.ERROR);
        alert.setTitle("Login Error");
        alert.setHeaderText("Invalid Credentials");
        alert.setContentText(message);
        alert.showAndWait();
    }
}



