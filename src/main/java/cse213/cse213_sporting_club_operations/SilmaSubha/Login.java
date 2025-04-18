package cse213.cse213_sporting_club_operations.SilmaSubha;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import javafx.stage.Window;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

public class Login {

    // Login Tab Components
    @FXML private TextField    loginUserNo;
    @FXML private ComboBox<String> loginDepartment;
    @FXML private PasswordField loginPassword;
    @FXML private Button       loginButton;

    // Sign Up Tab Components
    @FXML private TextField    signupUserNo;
    @FXML private ComboBox<String> signupDepartment;
    @FXML private PasswordField signupPassword;
    @FXML private Button       signupButton;

    private final String[] departments     = {"FAN", "Finance Manager"};
    private final Map<String, User> userDatabase = new HashMap<>();
    private static final String USER_DB_FILE = "userdata.bin";

    @FXML
    public void initialize() {
        // Populate department dropdowns
        loginDepartment.getItems().addAll(departments);
        signupDepartment.getItems().addAll(departments);

        loadUserData();
        loginDepartment.getSelectionModel().selectFirst();
        signupDepartment.getSelectionModel().selectFirst();
    }

    @FXML
    private void handleLogin(ActionEvent event) {
        String userId     = loginUserNo.getText().trim();
        String password   = loginPassword.getText().trim();
        String department = loginDepartment.getValue();

        if (userId.isEmpty() || password.isEmpty() || department == null) {
            showAlert("Login Failed", "Please fill all fields", loginButton.getScene().getWindow());
            return;
        }

        if (!userId.matches("\\d{7}")) {
            showAlert("Invalid User ID", "User ID must be exactly 7 digits", loginButton.getScene().getWindow());
            return;
        }

        User registered = userDatabase.get(userId);
        if (registered != null
                && registered.getDepartment().equals(department)
                && registered.getPassword().equals(hashPassword(password))) {
            openDashboard(department, event);
        } else {
            showAlert("Login Failed", "Invalid credentials or department", loginButton.getScene().getWindow());
        }
    }

    @FXML
    private void handleSignup(ActionEvent event) {
        String userId     = signupUserNo.getText().trim();
        String department = signupDepartment.getValue();
        String password   = signupPassword.getText().trim();

        if (userId.isEmpty() || password.isEmpty() || department == null) {
            showAlert("Signup Error", "Please fill all fields", signupButton.getScene().getWindow());
            return;
        }

        if (!userId.matches("\\d{7}")) {
            showAlert("Invalid User ID", "User ID must be exactly 7 digits", signupButton.getScene().getWindow());
            return;
        }

        if (password.length() < 6) {
            showAlert("Weak Password", "Password must be at least 6 characters", signupButton.getScene().getWindow());
            return;
        }

        if (userDatabase.containsKey(userId)) {
            showAlert("Signup Error", "That User ID already exists", signupButton.getScene().getWindow());
        } else {
            userDatabase.put(userId, new User(department, hashPassword(password)));
            saveUserData();
            clearSignupFields();
            showAlert("Success", "Sign-up complete! You can now log in.", signupButton.getScene().getWindow());
        }
    }

    private void openDashboard(String department, ActionEvent event) {
        try {
            String fxmlPath = department.equals("Finance Manager")
                    ? "/com/cse213/fproject/FinanceManager.fxml"
                    : "/com/cse213/fproject/fan.fxml";
            String title = department.equals("Finance Manager")
                    ? "Finance Manager Dashboard"
                    : "Fan Dashboard";

            // Close login window
            Stage loginStage = (Stage)((Button)event.getSource()).getScene().getWindow();
            loginStage.close();

            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
            Parent root = loader.load();

            Stage stage = new Stage();
            stage.setTitle(title);
            if (department.equals("Finance Manager")) {
                stage.setScene(new Scene(root, 1280, 780));
                stage.setResizable(false);
            } else {
                stage.setScene(new Scene(root, 800, 600));
            }
            stage.show();
        } catch (IOException e) {
            showAlert("Error", "Could not load dashboard:\n" + e.getMessage(), null);
            e.printStackTrace();
        }
    }

    private void clearSignupFields() {
        signupUserNo.clear();
        signupPassword.clear();
        signupDepartment.getSelectionModel().selectFirst();
    }

    private String hashPassword(String password) {
        return Integer.toString(password.hashCode());
    }

    @SuppressWarnings("unchecked")
    private void loadUserData() {
        File file = new File(USER_DB_FILE);
        if (!file.exists()) return;

        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
            Object obj = ois.readObject();
            if (obj instanceof Map) {
                userDatabase.putAll((Map<String, User>) obj);
            }
        } catch (Exception e) {
            showAlert("Error", "Failed to load user data", null);
            e.printStackTrace();
        }
    }

    private void saveUserData() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(USER_DB_FILE))) {
            oos.writeObject(userDatabase);
        } catch (IOException e) {
            showAlert("Error", "Failed to save user data", null);
            e.printStackTrace();
        }
    }

    private void showAlert(String title, String message, Window owner) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        if (owner != null) alert.initOwner(owner);
        alert.showAndWait();
    }

    private static class User implements Serializable {
        private static final long serialVersionUID = 1L;
        private final String department;
        private final String password;
        public User(String department, String password) {
            this.department = department;
            this.password   = password;
        }
        public String getDepartment() { return department; }
        public String getPassword()   { return password;   }
    }
}
