package cse213.cse213_sporting_club_operations.TanvirMahmud;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Optional;

public class ContractTermination_Controller {
    // Contract selection
    @FXML private ComboBox<String> playerComboBox;
    @FXML private TextField teamField;
    @FXML private DatePicker terminationDatePicker;

    // Termination details
    @FXML private ToggleGroup terminationTypeGroup;
    @FXML private RadioButton mutualAgreementRadio;
    @FXML private RadioButton clubDecisionRadio;
    @FXML private TextArea reasonTextArea;
    @FXML private CheckBox complianceCheckBox;
    @FXML private CheckBox notificationCheckBox;

    // Termination table
    @FXML private TableView<TerminatedContract> terminationsTable;
    @FXML private TableColumn<TerminatedContract, String> playerColumn;
    @FXML private TableColumn<TerminatedContract, String> teamColumn;
    @FXML private TableColumn<TerminatedContract, String> typeColumn;
    @FXML private TableColumn<TerminatedContract, LocalDate> dateColumn;
    @FXML private TableColumn<TerminatedContract, String> reasonColumn;
    @FXML private TableColumn<TerminatedContract, Boolean> complianceColumn;

    private ObservableList<TerminatedContract> terminatedContracts = FXCollections.observableArrayList();
    private ArrayList<Player> players = new ArrayList<>();

    @FXML
    public void initialize() {

        loadPlayersFromBinaryFile();


        mutualAgreementRadio.setToggleGroup(terminationTypeGroup);
        clubDecisionRadio.setToggleGroup(terminationTypeGroup);
        mutualAgreementRadio.setSelected(true);


        terminationDatePicker.setValue(LocalDate.now());


        playerColumn.setCellValueFactory(new PropertyValueFactory<>("playerName"));
        teamColumn.setCellValueFactory(new PropertyValueFactory<>("team"));
        typeColumn.setCellValueFactory(new PropertyValueFactory<>("terminationType"));
        dateColumn.setCellValueFactory(new PropertyValueFactory<>("terminationDate"));
        reasonColumn.setCellValueFactory(new PropertyValueFactory<>("reason"));
        complianceColumn.setCellValueFactory(new PropertyValueFactory<>("compliant"));

        terminationsTable.setItems(terminatedContracts);


        playerComboBox.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null) {
                updatePlayerDetails(newVal);
            }
        });
    }

    private void loadPlayersFromBinaryFile() {

        String[] possiblePaths = {
                "user.bin",
                "data/user.bin",
                "../user.bin",
                "src/main/resources/user.bin",
                "target/classes/user.bin",
                System.getProperty("user.dir") + "/user.bin"
        };

        boolean fileLoaded = false;

        for (String path : possiblePaths) {
            File file = new File(path);
            if (file.exists()) {
                try (ObjectInputStream ois = new ObjectInputStream(
                        new FileInputStream(file))) {

                    ArrayList<Player> loadedPlayers = (ArrayList<Player>) ois.readObject();
                    players.addAll(loadedPlayers);

                    // Populate player names in ComboBox
                    ObservableList<String> playerNames = FXCollections.observableArrayList();
                    for (Player player : players) {
                        playerNames.add(player.getName());
                    }
                    playerComboBox.setItems(playerNames);

                    System.out.println("Loaded " + players.size() + " players from " + path);
                    fileLoaded = true;
                    break;
                } catch (IOException | ClassNotFoundException e) {
                    System.out.println("Error reading from " + path + ": " + e.getMessage());
                }
            } else {
                System.out.println("File not found: " + path);
            }
        }

        if (!fileLoaded) {
            System.err.println("Could not load players from any file path. Using sample data instead.");
            loadSamplePlayers();
            showAlert("Warning", "Failed to load players from binary file. Using sample data instead.",
                    Alert.AlertType.WARNING);
        }
    }

    private void loadSamplePlayers() {

        players.add(new Player("Messi", "35", "Forward", "95%", "$50M"));
        players.add(new Player("Ronaldo", "38", "Forward", "93%", "$45M"));
        players.add(new Player("Neymar", "31", "Forward", "88%", "$40M"));
        players.add(new Player("Salah", "30", "Forward", "91%", "$35M"));
        players.add(new Player("De Bruyne", "31", "Midfielder", "90%", "$38M"));

        // Populate player names
        ObservableList<String> playerNames = FXCollections.observableArrayList();
        for (Player player : players) {
            playerNames.add(player.getName());
        }
        playerComboBox.setItems(playerNames);
    }

    private void updatePlayerDetails(String playerName) {
        for (Player player : players) {
            if (player.getName().equals(playerName)) {
                // We don't have a team field in Player, using age for demonstration
                teamField.setText("Team for " + player.getName());
                return;
            }
        }
        teamField.clear();
    }

    @FXML
    public void terminateContract() {
        String playerName = playerComboBox.getValue();
        if (playerName == null || playerName.isEmpty()) {
            showAlert("Error", "Please select a player", Alert.AlertType.ERROR);
            return;
        }

        if (terminationDatePicker.getValue() == null) {
            showAlert("Error", "Please select a termination date", Alert.AlertType.ERROR);
            return;
        }

        if (reasonTextArea.getText().trim().isEmpty()) {
            showAlert("Error", "Please provide a reason for termination", Alert.AlertType.ERROR);
            return;
        }

        if (!complianceCheckBox.isSelected()) {
            showAlert("Error", "Contract termination must comply with league regulations", Alert.AlertType.ERROR);
            return;
        }

        if (!notificationCheckBox.isSelected()) {
            showAlert("Error", "Required notifications must be sent", Alert.AlertType.ERROR);
            return;
        }


        String terminationType = mutualAgreementRadio.isSelected() ?
                "Mutual Agreement" : "Club Decision";


        TerminatedContract terminatedContract = new TerminatedContract(
                playerName,
                teamField.getText(),
                terminationType,
                terminationDatePicker.getValue(),
                reasonTextArea.getText().trim(),
                complianceCheckBox.isSelected()
        );


        terminatedContracts.add(terminatedContract);

        // Clear form
        clearForm();

        showAlert("Success", "Contract terminated successfully", Alert.AlertType.INFORMATION);
    }

    @FXML
    public void deleteTermination() {
        TerminatedContract selectedTermination = terminationsTable.getSelectionModel().getSelectedItem();
        if (selectedTermination == null) {
            showAlert("Error", "Please select a termination to delete", Alert.AlertType.ERROR);
            return;
        }

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirm Delete");
        alert.setHeaderText("Delete Termination Record");
        alert.setContentText("Are you sure you want to delete termination record for " +
                selectedTermination.getPlayerName() + "?");

        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            terminatedContracts.remove(selectedTermination);
        }
    }

    private void clearForm() {
        playerComboBox.setValue(null);
        teamField.clear();
        terminationDatePicker.setValue(LocalDate.now());
        mutualAgreementRadio.setSelected(true);
        reasonTextArea.clear();
        complianceCheckBox.setSelected(false);
        notificationCheckBox.setSelected(false);
    }

    private void showAlert(String title, String message, Alert.AlertType type) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    @FXML
    public void goal4Dashboard(ActionEvent actionEvent) throws IOException {
        SceneSwitcher.switchTo("DashboardTransferWindowManager.fxml", actionEvent);
    }
}