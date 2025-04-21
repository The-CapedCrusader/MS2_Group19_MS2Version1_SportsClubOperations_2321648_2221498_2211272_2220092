package cse213.cse213_sporting_club_operations.TanvirMahmud;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class Goal2 {
    @FXML
    private TableView<Player> acceptedTable;
    @FXML
    private TableView<Player> rejectedTable;

    @FXML
    private TableColumn<Player, String> acceptedNameColumn;
    @FXML
    private TableColumn<Player, String> acceptedAgeColumn;
    @FXML
    private TableColumn<Player, String> acceptedPositionColumn;
    @FXML
    private TableColumn<Player, String> acceptedFitColumn;
    @FXML
    private TableColumn<Player, String> acceptedValueColumn;

    @FXML
    private TableColumn<Player, String> rejectedNameColumn;
    @FXML
    private TableColumn<Player, String> rejectedAgeColumn;
    @FXML
    private TableColumn<Player, String> rejectedPositionColumn;
    @FXML
    private TableColumn<Player, String> rejectedFitColumn;
    @FXML
    private TableColumn<Player, String> rejectedValueColumn;

    private ObservableList<Player> acceptedPlayers = FXCollections.observableArrayList();
    private ObservableList<Player> rejectedPlayers = FXCollections.observableArrayList();

    private static final String USER_DATA_FILE = "C:\\Users\\Tanvir Mahmud\\Desktop\\SportsClubOperations\\data\\user.bin";

    @FXML
    public void initialize() {
        setupTableColumns();
        loadPlayerData();
    }

    private void setupTableColumns() {
        // Setup accepted players table
        acceptedNameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        acceptedAgeColumn.setCellValueFactory(new PropertyValueFactory<>("age"));
        acceptedPositionColumn.setCellValueFactory(new PropertyValueFactory<>("position"));
        acceptedFitColumn.setCellValueFactory(new PropertyValueFactory<>("fit"));
        acceptedValueColumn.setCellValueFactory(new PropertyValueFactory<>("marketValue"));

        // Setup rejected players table
        rejectedNameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        rejectedAgeColumn.setCellValueFactory(new PropertyValueFactory<>("age"));
        rejectedPositionColumn.setCellValueFactory(new PropertyValueFactory<>("position"));
        rejectedFitColumn.setCellValueFactory(new PropertyValueFactory<>("fit"));
        rejectedValueColumn.setCellValueFactory(new PropertyValueFactory<>("marketValue"));

        // Set items to the tables
        acceptedTable.setItems(acceptedPlayers);
        rejectedTable.setItems(rejectedPlayers);
    }

    private void loadPlayerData() {
        // Clear existing data
        acceptedPlayers.clear();
        rejectedPlayers.clear();

        try {
            File file = new File(USER_DATA_FILE);
            if (file.exists()) {
                FileInputStream fis = new FileInputStream(file);
                ObjectInputStream ois = new ObjectInputStream(fis);

                List<Player> loadedPlayers = (List<Player>) ois.readObject();

                // Add all players to accepted list
                acceptedPlayers.addAll(loadedPlayers);

                ois.close();
                fis.close();

                System.out.println("Loaded " + loadedPlayers.size() + " players from User.bin");
            } else {
                System.out.println("User.bin file not found at: " + USER_DATA_FILE);
                showAlert("File Not Found", "User.bin file not found. Please create it first.", Alert.AlertType.WARNING);
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Error loading from User.bin: " + e.getMessage());
            showAlert("Error Loading Data", "Error: " + e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    @FXML
    public void rejectPlayer() {
        Player selectedPlayer = acceptedTable.getSelectionModel().getSelectedItem();
        if (selectedPlayer == null) {
            showAlert("No Selection", "Please select a player to reject", Alert.AlertType.WARNING);
            return;
        }

        // Confirm rejection
        boolean confirmed = showConfirmation("Confirm Rejection",
                "Are you sure you want to reject " + selectedPlayer.getName() + "?");
        if (!confirmed) {
            return;
        }

        // Move player from accepted to rejected
        acceptedPlayers.remove(selectedPlayer);
        rejectedPlayers.add(selectedPlayer);

        // Save changes to file
        saveDataToFile();

        showAlert("Player Rejected", selectedPlayer.getName() + " has been rejected",
                Alert.AlertType.INFORMATION);
    }

    @FXML
    public void acceptRejectedPlayer() {
        Player selectedPlayer = rejectedTable.getSelectionModel().getSelectedItem();
        if (selectedPlayer == null) {
            showAlert("No Selection", "Please select a player to accept", Alert.AlertType.WARNING);
            return;
        }

        // Confirm acceptance
        boolean confirmed = showConfirmation("Confirm Acceptance",
                "Are you sure you want to accept " + selectedPlayer.getName() + "?");
        if (!confirmed) {
            return;
        }

        // Move player from rejected to accepted
        rejectedPlayers.remove(selectedPlayer);
        acceptedPlayers.add(selectedPlayer);

        // Save changes to file
        saveDataToFile();

        showAlert("Player Accepted", selectedPlayer.getName() + " has been accepted",
                Alert.AlertType.INFORMATION);
    }

    @FXML
    public void deleteRejectedPlayers() {
        if (rejectedPlayers.isEmpty()) {
            showAlert("No Rejected Players", "There are no rejected players to delete",
                    Alert.AlertType.INFORMATION);
            return;
        }

        boolean confirmed = showConfirmation("Confirm Deletion",
                "Are you sure you want to permanently delete all rejected players? This cannot be undone.",
                "Deleted players will be removed from User.bin permanently.");
        if (!confirmed) {
            return;
        }

        // Remove rejected players and save to file
        rejectedPlayers.clear();
        saveDataToFile();

        showAlert("Players Deleted", "All rejected players have been permanently deleted",
                Alert.AlertType.INFORMATION);
    }

    private void saveDataToFile() {
        try {
            // Create parent directories if they don't exist
            File directory = new File("C:\\Users\\Tanvir Mahmud\\Desktop\\SportsClubOperations\\data");
            if (!directory.exists()) {
                directory.mkdirs();
            }

            // Save only the accepted players to User.bin
            FileOutputStream fos = new FileOutputStream(USER_DATA_FILE);
            ObjectOutputStream oos = new ObjectOutputStream(fos);

            // Convert ObservableList to ArrayList
            ArrayList<Player> playersToSave = new ArrayList<>(acceptedPlayers);

            oos.writeObject(playersToSave);
            oos.close();
            fos.close();

            System.out.println("Saved " + playersToSave.size() + " players to User.bin");
        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Error Saving Data", "Error: " + e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    @FXML
    public void refreshData() {
        loadPlayerData();
    }

    @FXML
    public void goal2Dashboard(ActionEvent actionEvent) throws IOException {
        SceneSwitcher.switchTo("DashboardTransferWindowManager.fxml", actionEvent);
    }

    private void showAlert(String title, String message, Alert.AlertType type) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private boolean showConfirmation(String title, String message) {
        return showConfirmation(title, message, null);
    }

    private boolean showConfirmation(String title, String message, String details) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        if (details != null) {
            alert.getDialogPane().setExpandableContent(new Label(details));
        }

        Optional<ButtonType> result = alert.showAndWait();
        return result.isPresent() && result.get() == ButtonType.OK;
    }
}