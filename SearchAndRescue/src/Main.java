import java.util.Optional;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import SessionManager; 

public class Main extends Application { 
    @Override
    public void start(Stage primaryStage) {
        try {
            Parent root;

            if (SessionManager.shouldAutoLogin()) {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("SearchAndRescueUI.fxml")); 
                root = loader.load();
            } else {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("S&RLogin.fxml"));
                root = loader.load();
            }

            primaryStage.setTitle("Pasig SaR System");
            primaryStage.getIcons().add(new Image(getClass().getResourceAsStream("pasigLogo.jpg")));
            Scene scene = new Scene(root, 1366, 768);
            primaryStage.setScene(scene);
            primaryStage.show();

            primaryStage.setOnCloseRequest(event -> {
                event.consume(); 
                showLogoutConfirmation(primaryStage);
            });

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void showLogoutConfirmation(Stage stage) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Logout Confirmation");
        alert.setHeaderText("Do you want to logout?");
        alert.setContentText("Choosing 'Yes' will log you out and require sign in next time.");
        Stage alertStage = (Stage) alert.getDialogPane().getScene().getWindow();
        alertStage.getIcons().add(new javafx.scene.image.Image(getClass().getResourceAsStream("pasigLogo.jpg")));
        ButtonType yesBtn = new ButtonType("Yes", ButtonBar.ButtonData.YES);
        ButtonType noBtn = new ButtonType("No", ButtonBar.ButtonData.NO);

        alert.getButtonTypes().setAll(yesBtn, noBtn);

        Optional<ButtonType> result = alert.showAndWait();

        if (result.isPresent() && result.get() == yesBtn) {
            SessionManager.clearSavedLogin(); 
            stage.close();
        } else {
            stage.close(); 
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
