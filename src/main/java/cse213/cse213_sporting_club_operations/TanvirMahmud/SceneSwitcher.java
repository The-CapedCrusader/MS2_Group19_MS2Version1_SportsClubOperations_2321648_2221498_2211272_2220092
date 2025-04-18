package cse213.cse213_sporting_club_operations.TanvirMahmud;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class SceneSwitcher {
    public static Stage stage;

    public static void switchTo(String fxml, ActionEvent actionEvent) throws IOException {
        Parent root = FXMLLoader.load(
                SceneSwitcher.class.getResource(fxml)
        );
        Scene scene = new Scene(root);
        Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        stage.setScene(scene);
    }

    public static void switchTo(String fxml) throws IOException {
        Parent root = FXMLLoader.load(
                SceneSwitcher.class.getResource(fxml)
        );
        Scene scene = new Scene(root);
        stage.setScene(scene);
    }
}
