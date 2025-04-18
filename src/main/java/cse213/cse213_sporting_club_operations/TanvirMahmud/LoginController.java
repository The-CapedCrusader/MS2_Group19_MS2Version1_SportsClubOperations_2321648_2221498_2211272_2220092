package cse213.cse213_sporting_club_operations.TanvirMahmud;

import javafx.event.ActionEvent;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class LoginController {
    @javafx.fxml.FXML
    private TextField usernameTF;
    @javafx.fxml.FXML
    private ComboBox<String> userTypeCB;
    @javafx.fxml.FXML
    private TextField passwordTF;
    @javafx.fxml.FXML
    private Label loginErrorLabel;

    @javafx.fxml.FXML
    public void initialize() {
        userTypeCB.getItems().addAll("TW Manager", "Head Coach");
        userTypeCB.setValue("TW Manager");
    }

    @javafx.fxml.FXML
    public void login(ActionEvent actionEvent) {
        String username = usernameTF.getText();
        String password = passwordTF.getText();
        String userType = userTypeCB.getValue();

        if (username.isEmpty() || password.isEmpty() || userType == null) {
            loginErrorLabel.setText("Username, password and user type are required!");
            return;
        }

        if (validateCredentials(username, password, userType)) {
            loginErrorLabel.setText("Login successful!");
            try {
                if ("TW Manager".equals(userType)) {
                    SceneSwitcher.switchTo("DashboardTransferWindowManager.fxml", actionEvent);
                } else if ("Head Coach".equals(userType)) {
                    SceneSwitcher.switchTo("DashboardHeadCoach.fxml", actionEvent);
                }
            } catch (IOException e) {
                loginErrorLabel.setText("Error loading dashboard!");
                System.err.println("Error switching scene: " + e.getMessage());
            }
        } else {
            loginErrorLabel.setText("Invalid username or password!");
        }
    }

    private boolean validateCredentials(String username, String password, String userType) {
        try (BufferedReader reader = new BufferedReader(new FileReader("data.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length >= 4) {
                    if (parts[1].equals(username) && parts[2].equals(password) && parts[3].equals(userType)) {
                        return true;
                    }
                }
            }
        } catch (IOException e) {
            System.err.println("Error reading credentials file: " + e.getMessage());
        }
        return false;
    }

    @javafx.fxml.FXML
    public void loginSignup(ActionEvent actionEvent) throws IOException {
        try {
            SceneSwitcher.switchTo("signup.fxml", actionEvent);
        } catch (IOException e) {
            loginErrorLabel.setText("Error navigating to signup page!");
            System.err.println("Error switching to signup page: " + e.getMessage());
        }
    }
}