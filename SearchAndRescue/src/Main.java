import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

public class Main extends Application { 
    @Override
    public void start(Stage primaryStage) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("S&RLogin.fxml"));
            Parent root = loader.load();
            primaryStage.setTitle("Pasig SaR System");
            primaryStage.getIcons().add(new Image(getClass().getResourceAsStream("pasigLogo.jpg")));
            Scene scene = new Scene(root, 1366, 768);
            primaryStage.setScene(scene);
            primaryStage.show();
               
        } catch (Exception e) {
            e.printStackTrace();        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}