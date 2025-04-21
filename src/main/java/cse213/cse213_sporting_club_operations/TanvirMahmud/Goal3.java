package cse213.cse213_sporting_club_operations.TanvirMahmud;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.io.IOException;
import java.util.Optional;
import java.io.FileInputStream;
import java.io.ObjectInputStream;
import java.util.ArrayList;

public class Goal3 {

    @FXML private ComboBox<Player> playerComboBox;

    // Contract details inputs
    @FXML private TextField salaryField;
    @FXML private TextField bonusField;
    @FXML private Spinner<Integer> contractLengthSpinner;
    @FXML private TextField releaseClauseField;

    // Contract list view
    @FXML private TableView<Contract> contractsTable;
    @FXML private TableColumn<Contract, String> playerColumn;
    @FXML private TableColumn<Contract, String> salaryColumn;
    @FXML private TableColumn<Contract, String> bonusColumn;
    @FXML private TableColumn<Contract, Integer> lengthColumn;
    @FXML private TableColumn<Contract, String> releaseColumn;

    private ObservableList<Contract> contracts = FXCollections.observableArrayList();



    @FXML

    public void initialize() {
        // Load players from user.bin
        loadPlayersFromBinaryFile();

        // Set up contract length spinner (1-5 years)
        SpinnerValueFactory<Integer> valueFactory =
                new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 5, 1);
        contractLengthSpinner.setValueFactory(valueFactory);

        // Set up table columns
        playerColumn.setCellValueFactory(new PropertyValueFactory<>("playerName"));
        salaryColumn.setCellValueFactory(new PropertyValueFactory<>("salary"));
        bonusColumn.setCellValueFactory(new PropertyValueFactory<>("bonus"));
        lengthColumn.setCellValueFactory(new PropertyValueFactory<>("contractLength"));
        releaseColumn.setCellValueFactory(new PropertyValueFactory<>("releaseClause"));

        contractsTable.setItems(contracts);
    }

    private void loadPlayersFromBinaryFile() {
        try (ObjectInputStream ois = new ObjectInputStream(
                new FileInputStream("data/user.bin"))) {

            ArrayList<Player> loadedPlayers = (ArrayList<Player>) ois.readObject();
            ObservableList<Player> playerList = FXCollections.observableArrayList(loadedPlayers);
            playerComboBox.setItems(playerList);

            System.out.println("Loaded " + playerList.size() + " players from user.bin");
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("Error loading players from user.bin: " + e.getMessage());
            e.printStackTrace();
            showAlert("Error", "Failed to load players from binary file: " + e.getMessage());
        }
    }

    @FXML
    public void createContract(ActionEvent event) {
        Player selectedPlayer = playerComboBox.getValue();
        if (selectedPlayer == null) {
            showAlert("Error", "Please select a player");
            return;
        }

        try {
            String salary = salaryField.getText().trim();
            String bonus = bonusField.getText().trim();
            int length = contractLengthSpinner.getValue();
            String releaseClause = releaseClauseField.getText().trim();

            if (salary.isEmpty() || releaseClause.isEmpty()) {
                showAlert("Error", "Salary and release clause are required");
                return;
            }

            Contract contract = new Contract(
                    selectedPlayer.getName(),
                    salary,
                    bonus.isEmpty() ? "N/A" : bonus,
                    length,
                    releaseClause
            );

            contracts.add(contract);
            clearForm();

            showAlert("Success", "Contract created for " + selectedPlayer.getName());

        } catch (Exception e) {
            showAlert("Error", "Invalid input: " + e.getMessage());
        }
    }



    private void clearForm() {
        playerComboBox.setValue(null);
        salaryField.clear();
        bonusField.clear();
        contractLengthSpinner.getValueFactory().setValue(1);
        releaseClauseField.clear();
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    @FXML
    public void goal3Dashboard(ActionEvent actionEvent) throws IOException {
        SceneSwitcher.switchTo("DashboardTransferWindowManager.fxml", actionEvent);
    }



}