package cse213.cse213_sporting_club_operations.SilmaSubha;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

public class MainApplication extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/cse213/fproject/login.fxml"));
        Parent root = loader.load();

        Scene scene = new Scene(root);
        primaryStage.setTitle("Sports Club Management System");
        primaryStage.setScene(scene);
        primaryStage.setResizable(false);

        // Set application icon (now using try-with-resources)
        try (var is = getClass().getResourceAsStream("/com/cse213/fproject/sportsClub.jpg")) {
            if (is != null) {
                primaryStage.getIcons().add(new Image(is));
            }
        } catch (Exception e) {
            System.err.println("Warning: Could not load application icon");
        }

        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}