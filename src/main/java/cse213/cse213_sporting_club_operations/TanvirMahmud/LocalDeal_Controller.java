package cse213.cse213_sporting_club_operations.TanvirMahmud;

import javafx.beans.property.SimpleStringProperty;
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
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Optional;

public class LocalDeal_Controller {

    @FXML private ComboBox<String> playerComboBox;
    @FXML private TextField currentClubField;
    @FXML private TextField positionField;
    @FXML private TextField ageField;


    @FXML private TextField receivingClubField;
    @FXML private DatePicker startDatePicker;
    @FXML private DatePicker endDatePicker;
    @FXML private TextField loanFeeField;
    @FXML private CheckBox buyOptionCheckBox;
    @FXML private TextField buyoutPriceField;
    @FXML private ComboBox<String> wagePaymentComboBox;


    @FXML private CheckBox recallClauseCheckBox;
    @FXML private TextArea specialTermsArea;
    @FXML private CheckBox sendingClubNotifiedCheckBox;
    @FXML private CheckBox receivingClubNotifiedCheckBox;


    @FXML private TableView<LoanDeal> loanDealsTable;
    @FXML private TableColumn<LoanDeal, String> playerColumn;
    @FXML private TableColumn<LoanDeal, String> receivingClubColumn;
    @FXML private TableColumn<LoanDeal, String> durationColumn;
    @FXML private TableColumn<LoanDeal, String> loanFeeColumn;
    @FXML private TableColumn<LoanDeal, String> buyOptionColumn;
    @FXML private TableColumn<LoanDeal, String> statusColumn;

    private ObservableList<LoanDeal> loanDeals = FXCollections.observableArrayList();
    private ArrayList<Player> players = new ArrayList<>();

    @FXML
    public void initialize() {

        loadPlayersFromBinaryFile();

        wagePaymentComboBox.setItems(FXCollections.observableArrayList(
                "100% by Receiving Club",
                "50% by Each Club",
                "75% Receiving / 25% Sending",
                "25% Receiving / 75% Sending",
                "Custom Agreement"
        ));

        // Set default dates
        startDatePicker.setValue(LocalDate.now());
        endDatePicker.setValue(LocalDate.now().plusMonths(6));

        // Setup table columns
        playerColumn.setCellValueFactory(new PropertyValueFactory<>("playerName"));
        receivingClubColumn.setCellValueFactory(new PropertyValueFactory<>("receivingClub"));
        durationColumn.setCellValueFactory(data -> {
            String start = data.getValue().getStartDate().format(DateTimeFormatter.ofPattern("MM/dd/yyyy"));
            String end = data.getValue().getEndDate().format(DateTimeFormatter.ofPattern("MM/dd/yyyy"));
            long months = ChronoUnit.MONTHS.between(
                    data.getValue().getStartDate(),
                    data.getValue().getEndDate());
            return new SimpleStringProperty(start + " to " + end + " (" + months + " months)");
        });
        loanFeeColumn.setCellValueFactory(data ->
                new SimpleStringProperty("$" + data.getValue().getLoanFee()));
        buyOptionColumn.setCellValueFactory(data -> {
            if (data.getValue().hasBuyOption()) {
                return new SimpleStringProperty("Yes - $" + data.getValue().getBuyoutPrice());
            } else {
                return new SimpleStringProperty("No");
            }
        });
        statusColumn.setCellValueFactory(new PropertyValueFactory<>("status"));

        loanDealsTable.setItems(loanDeals);

        // Add listener for player selection
        playerComboBox.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null) {
                updatePlayerDetails(newVal);
            }
        });


        buyOptionCheckBox.selectedProperty().addListener((obs, oldVal, newVal) -> {
            buyoutPriceField.setDisable(!newVal);
            if (!newVal) {
                buyoutPriceField.clear();
            }
        });



    }

    private void loadPlayersFromBinaryFile() {
        // Try different potential file locations
        String[] possiblePaths = {
                "user.bin",                        // Current directory
                "data/user.bin",                   // Data subdirectory
                "../user.bin",                     // Parent directory
                "src/main/resources/user.bin",     // Resources directory
                "target/classes/user.bin",         // Compiled resources
                System.getProperty("user.dir") + "/user.bin" // Absolute path
        };

        boolean fileLoaded = false;

        for (String path : possiblePaths) {
            File file = new File(path);
            if (file.exists()) {
                try (ObjectInputStream ois = new ObjectInputStream(
                        new FileInputStream(file))) {

                    // Read object from file
                    Object obj = ois.readObject();

                    // Process based on what type of object was stored
                    if (obj instanceof ArrayList<?>) {
                        ArrayList<?> loadedList = (ArrayList<?>) obj;
                        if (!loadedList.isEmpty() && loadedList.get(0) instanceof Player) {
                            ArrayList<Player> playerList = (ArrayList<Player>) loadedList;
                            players.addAll(playerList);

                            // Populate player names in ComboBox
                            ObservableList<String> playerNames = FXCollections.observableArrayList();
                            for (Player player : players) {
                                playerNames.add(player.getName());
                            }
                            playerComboBox.setItems(playerNames);

                            System.out.println("Loaded " + players.size() + " players from " + path);
                            fileLoaded = true;
                            break;
                        }
                    }
                    System.out.println("File found at " + path + " but content format not recognized");

                } catch (IOException | ClassNotFoundException e) {
                    System.out.println("Error reading from " + path + ": " + e.getMessage());
                }
            } else {
                System.out.println("File not found: " + path);
            }
        }

        if (!fileLoaded) {
            System.out.println("Could not load players from binary file. Using default data if available.");
            // Load from Goal1_AllPlayers as fallback
            if (AllPlayers_Controller.playerList != null && !AllPlayers_Controller.playerList.isEmpty()) {
                players.addAll(AllPlayers_Controller.playerList);

                // Populate player names
                ObservableList<String> playerNames = FXCollections.observableArrayList();
                for (Player player : players) {
                    playerNames.add(player.getName());
                }
                playerComboBox.setItems(playerNames);
            } else {
                // Add some sample players as last resort
                loadSamplePlayers();
            }
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

        System.out.println("Using sample player data");
    }



    private void updatePlayerDetails(String playerName) {
        for (Player player : players) {
            if (player.getName().equals(playerName)) {
                // Display player details
                currentClubField.setText("Current club placeholder for " + player.getName());
                positionField.setText(player.getPosition());
                ageField.setText(player.getAge());
                return;
            }
        }
        // Clear fields if player not found
        currentClubField.clear();
        positionField.clear();
        ageField.clear();
    }

    @FXML
    public void processLoan() {
        // Validate required fields
        if (playerComboBox.getValue() == null || playerComboBox.getValue().isEmpty()) {
            showAlert("Error", "Please select a player", Alert.AlertType.ERROR);
            return;
        }

        if (receivingClubField.getText().trim().isEmpty()) {
            showAlert("Error", "Please enter the receiving club", Alert.AlertType.ERROR);
            return;
        }

        if (startDatePicker.getValue() == null || endDatePicker.getValue() == null) {
            showAlert("Error", "Please select both start and end dates", Alert.AlertType.ERROR);
            return;
        }

        if (endDatePicker.getValue().isBefore(startDatePicker.getValue())) {
            showAlert("Error", "End date cannot be before start date", Alert.AlertType.ERROR);
            return;
        }

        if (loanFeeField.getText().trim().isEmpty()) {
            showAlert("Error", "Please enter a loan fee", Alert.AlertType.ERROR);
            return;
        }

        if (buyOptionCheckBox.isSelected() && buyoutPriceField.getText().trim().isEmpty()) {
            showAlert("Error", "Please enter a buyout price", Alert.AlertType.ERROR);
            return;
        }

        if (wagePaymentComboBox.getValue() == null) {
            showAlert("Error", "Please select a wage payment structure", Alert.AlertType.ERROR);
            return;
        }

        if (!sendingClubNotifiedCheckBox.isSelected() || !receivingClubNotifiedCheckBox.isSelected()) {
            showAlert("Error", "Both clubs must be notified before processing loan", Alert.AlertType.ERROR);
            return;
        }

        // Parse numeric values
        double loanFee;
        double buyoutPrice = 0;

        try {
            loanFee = Double.parseDouble(loanFeeField.getText().replace(",", "").replace("$", ""));
        } catch (NumberFormatException e) {
            showAlert("Error", "Please enter a valid loan fee amount", Alert.AlertType.ERROR);
            return;
        }

        if (buyOptionCheckBox.isSelected()) {
            try {
                buyoutPrice = Double.parseDouble(buyoutPriceField.getText().replace(",", "").replace("$", ""));
            } catch (NumberFormatException e) {
                showAlert("Error", "Please enter a valid buyout price amount", Alert.AlertType.ERROR);
                return;
            }
        }

        // Create new loan deal
        LoanDeal loanDeal = new LoanDeal(
                playerComboBox.getValue(),
                receivingClubField.getText().trim(),
                currentClubField.getText().trim(),
                startDatePicker.getValue(),
                endDatePicker.getValue(),
                loanFee,
                buyOptionCheckBox.isSelected(),
                buyoutPrice,
                wagePaymentComboBox.getValue(),
                recallClauseCheckBox.isSelected(),
                specialTermsArea.getText().trim(),
                "Active"
        );

        // Add to table
        loanDeals.add(loanDeal);

        // Clear form
        clearForm();

        showAlert("Success", "Loan deal processed successfully", Alert.AlertType.INFORMATION);
    }

    @FXML
    public void terminateLoan() {
        LoanDeal selectedLoan = loanDealsTable.getSelectionModel().getSelectedItem();
        if (selectedLoan == null) {
            showAlert("Error", "Please select a loan deal to terminate", Alert.AlertType.ERROR);
            return;
        }

        if ("Terminated".equals(selectedLoan.getStatus())) {
            showAlert("Error", "This loan deal is already terminated", Alert.AlertType.ERROR);
            return;
        }

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirm Termination");
        alert.setHeaderText("Terminate Loan Deal");
        alert.setContentText("Are you sure you want to terminate the loan deal for " +
                selectedLoan.getPlayerName() + " from " + selectedLoan.getSendingClub() +
                " to " + selectedLoan.getReceivingClub() + "?");

        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            int index = loanDeals.indexOf(selectedLoan);
            selectedLoan.setStatus("Terminated");
            loanDeals.set(index, selectedLoan);
        }
    }

    @FXML
    public void deleteLoan() {
        LoanDeal selectedLoan = loanDealsTable.getSelectionModel().getSelectedItem();
        if (selectedLoan == null) {
            showAlert("Error", "Please select a loan deal to delete", Alert.AlertType.ERROR);
            return;
        }

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirm Delete");
        alert.setHeaderText("Delete Loan Deal");
        alert.setContentText("Are you sure you want to delete this loan deal record? This action cannot be undone.");

        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            loanDeals.remove(selectedLoan);
        }
    }

    private void clearForm() {
        playerComboBox.setValue(null);
        currentClubField.clear();
        positionField.clear();
        ageField.clear();
        receivingClubField.clear();
        startDatePicker.setValue(LocalDate.now());
        endDatePicker.setValue(LocalDate.now().plusMonths(6));
        loanFeeField.clear();
        buyOptionCheckBox.setSelected(false);
        buyoutPriceField.clear();
        buyoutPriceField.setDisable(true);
        wagePaymentComboBox.setValue(null);
        recallClauseCheckBox.setSelected(false);
        specialTermsArea.clear();
        sendingClubNotifiedCheckBox.setSelected(false);
        receivingClubNotifiedCheckBox.setSelected(false);
    }

    private void showAlert(String title, String message, Alert.AlertType type) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    @FXML
    public void goal6Dashboard(ActionEvent actionEvent) throws IOException {
        SceneSwitcher.switchTo("DashboardTransferWindowManager.fxml", actionEvent);
    }
}