package cse213.cse213_sporting_club_operations.TanvirMahmud;

import javafx.event.ActionEvent;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

public class SignupController {
    @javafx.fxml.FXML
    private TextField signupNameTF;
    @javafx.fxml.FXML
    private TextField signupemailTF;
    @javafx.fxml.FXML
    private TextField signuppasswordTF;
    @javafx.fxml.FXML
    private Label signupLabel;
    @javafx.fxml.FXML
    private ComboBox<String> signupCB;

    @javafx.fxml.FXML
    public void initialize() {
        signupCB.getItems().addAll("TW Manager", "Head Coach");
        signupCB.setValue("TW Manager");
    }

    @javafx.fxml.FXML
    public void signup(ActionEvent actionEvent) {
        if (signupNameTF.getText().isEmpty() || signupemailTF.getText().isEmpty()
                || signuppasswordTF.getText().isEmpty() || signupCB.getValue() == null) {
            signupLabel.setText("All fields are required!");
            return;
        }

        try (BufferedWriter writer = new BufferedWriter(new FileWriter("data.txt", true))) {
            writer.write(signupNameTF.getText() + "," +
                    signupemailTF.getText() + "," +
                    signuppasswordTF.getText() + "," +
                    signupCB.getValue());
            writer.newLine();
            signupLabel.setText("Account created successfully!");

            signupNameTF.clear();
            signupemailTF.clear();
            signuppasswordTF.clear();
        } catch (IOException e) {
            signupLabel.setText("Could not save account information!");
        }
    }

    @javafx.fxml.FXML
    public void signupLogin(ActionEvent actionEvent) throws IOException {
        SceneSwitcher.switchTo("login.fxml", actionEvent);
    }
}