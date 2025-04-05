import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application { //THIS SHOULD BE THE LOGIN PAGE!! -- FOR TRIAL LANG MUNA KAYA HOMEPAGE 
    @Override
    public void start(Stage primaryStage) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("SearchAndRescueUI.fxml"));
            Parent root = loader.load();

            System.out.println("SearchAndRescue.fxml loaded!");

            Scene scene = new Scene(root, 1366, 768);
            primaryStage.setScene(scene);
            primaryStage.show();

        } catch (Exception e) {
            e.printStackTrace();        }
    }

    public static void main(String[] args) {
        System.out.println("Launching application...");
        launch(args);
    }
}