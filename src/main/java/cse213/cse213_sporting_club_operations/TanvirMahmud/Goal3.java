package cse213.cse213_sporting_club_operations.TanvirMahmud;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.io.IOException;
import java.util.Optional;

public class Goal3 {
    // Player selection
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
    private Button deleteContract;

    @FXML
    public void initialize() {
        // Set up player dropdown
        if (!Goal1_AllPlayers.playerList.isEmpty()) {
            playerComboBox.setItems(FXCollections.observableArrayList(Goal1_AllPlayers.playerList));
        }

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

    @Deprecated
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

    @Deprecated
    public void deleteContract(ActionEvent event) {
        Contract selectedContract = contractsTable.getSelectionModel().getSelectedItem();
        if (selectedContract == null) {
            showAlert("Error", "Please select a contract to delete");
            return;
        }

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirm Delete");
        alert.setHeaderText("Delete Contract");
        alert.setContentText("Are you sure you want to delete " + selectedContract.getPlayerName() + "'s contract?");

        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            contracts.remove(selectedContract);
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

    // Contract class to hold contract details
    public static class Contract {
        private final String playerName;
        private final String salary;
        private final String bonus;
        private final int contractLength;
        private final String releaseClause;

        public Contract(String playerName, String salary, String bonus,
                        int contractLength, String releaseClause) {
            this.playerName = playerName;
            this.salary = salary;
            this.bonus = bonus;
            this.contractLength = contractLength;
            this.releaseClause = releaseClause;
        }

        public String getPlayerName() { return playerName; }
        public String getSalary() { return salary; }
        public String getBonus() { return bonus; }
        public int getContractLength() { return contractLength; }
        public String getReleaseClause() { return releaseClause; }
    }
}