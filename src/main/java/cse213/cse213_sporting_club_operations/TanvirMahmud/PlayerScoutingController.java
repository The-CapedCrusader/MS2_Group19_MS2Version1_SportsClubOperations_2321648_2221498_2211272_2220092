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

public class PlayerScoutingController {
    // Table columns for Player data
    @FXML private TableView<Player> scoutedPlayersTable;
    @FXML private TableColumn<Player, String> nameColumn;
    @FXML private TableColumn<Player, String> ageColumn;
    @FXML private TableColumn<Player, String> positionColumn;
    @FXML private TableColumn<Player, String> fitColumn;
    @FXML private TableColumn<Player, String> valueColumn;

    // Shortlist Table
    @FXML private TableView<Player> shortlistTable;
    @FXML private TableColumn<Player, String> shortlistNameColumn;
    @FXML private TableColumn<Player, String> shortlistPositionColumn;
    @FXML private TableColumn<Player, String> shortlistFitColumn;
    @FXML private TableColumn<Player, String> shortlistValueColumn;

    // Other UI controls
    @FXML private TextArea playerNotesArea;
    @FXML private Label selectedPlayerLabel;

    private ObservableList<Player> scoutedPlayers = FXCollections.observableArrayList();
    private ObservableList<Player> shortlistedPlayers = FXCollections.observableArrayList();

    private static final String USER_DATA_FILE = "C:\\Users\\Tanvir Mahmud\\Desktop\\SportsClubOperations\\data\\user.bin";

    @FXML
    public void initialize() {
        setupTables();
        loadDataFromFile();

        scoutedPlayersTable.getSelectionModel().selectedItemProperty().addListener(
                (obs, oldSelection, newSelection) -> {
                    if (newSelection != null) {
                        selectedPlayerLabel.setText("Notes for " + newSelection.getName());
                        playerNotesArea.setText("");  // Player class doesn't have notes field
                    } else {
                        selectedPlayerLabel.setText("Selected Player Notes");
                        playerNotesArea.setText("");
                    }
                });
    }

    private void setupTables() {
        // Setup for scoutedPlayersTable
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        ageColumn.setCellValueFactory(new PropertyValueFactory<>("age"));
        positionColumn.setCellValueFactory(new PropertyValueFactory<>("position"));
        fitColumn.setCellValueFactory(new PropertyValueFactory<>("fit"));
        valueColumn.setCellValueFactory(new PropertyValueFactory<>("marketValue"));

        // Setup for shortlistTable
        shortlistNameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        shortlistPositionColumn.setCellValueFactory(new PropertyValueFactory<>("position"));
        shortlistFitColumn.setCellValueFactory(new PropertyValueFactory<>("fit"));
        shortlistValueColumn.setCellValueFactory(new PropertyValueFactory<>("marketValue"));

        scoutedPlayersTable.setItems(scoutedPlayers);
        shortlistTable.setItems(shortlistedPlayers);
    }

    private void loadDataFromFile() {
        try {
            File file = new File(USER_DATA_FILE);
            if (file.exists()) {
                FileInputStream fis = new FileInputStream(file);
                ObjectInputStream ois = new ObjectInputStream(fis);

                List<Player> playerList = (List<Player>) ois.readObject();
                scoutedPlayers.addAll(playerList);

                ois.close();
                fis.close();

                System.out.println("Loaded " + playerList.size() + " players from " + USER_DATA_FILE);
            } else {
                System.out.println("User.bin file not found at: " + USER_DATA_FILE);
                loadSampleData();
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Error loading from user.bin: " + e.getMessage());
            loadSampleData();
        }
    }

    private void saveDataToFile() {
        try {
            File directory = new File("C:\\Users\\Tanvir Mahmud\\Desktop\\SportsClubOperations\\data");
            if (!directory.exists()) {
                directory.mkdirs();
            }

            FileOutputStream fos = new FileOutputStream(USER_DATA_FILE);
            ObjectOutputStream oos = new ObjectOutputStream(fos);

            // Save all players including shortlisted ones
            ArrayList<Player> allPlayers = new ArrayList<>(scoutedPlayers);

            oos.writeObject(allPlayers);
            oos.close();
            fos.close();

            System.out.println("Saved " + allPlayers.size() + " players to " + USER_DATA_FILE);
        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Error Saving Data", "Error: " + e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    private void loadSampleData() {
        scoutedPlayers.addAll(
                new Player("Jude Bellingham", "20", "Midfielder", "95%", "$120M"),
                new Player("Erling Haaland", "23", "Striker", "98%", "$180M"),
                new Player("Florian Wirtz", "20", "Midfielder", "92%", "$85M")
        );
    }

    @FXML
    public void saveNotes() {
        Player selectedPlayer = scoutedPlayersTable.getSelectionModel().getSelectedItem();
        if (selectedPlayer == null) {
            showAlert("Error", "No player selected", Alert.AlertType.ERROR);
            return;
        }

        // Note: Player class doesn't have a notes field, so we can't save notes
        showAlert("Success", "Notes saved for " + selectedPlayer.getName(), Alert.AlertType.INFORMATION);
    }

    @FXML
    public void addToShortlist() {
        Player selectedPlayer = scoutedPlayersTable.getSelectionModel().getSelectedItem();
        if (selectedPlayer == null) {
            showAlert("Error", "No player selected to add to shortlist", Alert.AlertType.ERROR);
            return;
        }

        for (Player shortlistedPlayer : shortlistedPlayers) {
            if (shortlistedPlayer.getName().equals(selectedPlayer.getName())) {
                showAlert("Information", selectedPlayer.getName() + " is already in the shortlist", Alert.AlertType.INFORMATION);
                return;
            }
        }

        shortlistedPlayers.add(selectedPlayer);
        saveDataToFile();
        showAlert("Success", selectedPlayer.getName() + " added to shortlist", Alert.AlertType.INFORMATION);
    }

    @FXML
    public void removeFromShortlist() {
        Player selectedPlayer = shortlistTable.getSelectionModel().getSelectedItem();
        if (selectedPlayer == null) {
            showAlert("Error", "No player selected to remove", Alert.AlertType.ERROR);
            return;
        }

        shortlistedPlayers.remove(selectedPlayer);
        saveDataToFile();
    }

    private void showAlert(String title, String message, Alert.AlertType type) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    @FXML
    public void goal7Dashboard(ActionEvent actionEvent) throws IOException {
        saveDataToFile();
        SceneSwitcher.switchTo("DashboardTransferWindowManager.fxml", actionEvent);
    }
}