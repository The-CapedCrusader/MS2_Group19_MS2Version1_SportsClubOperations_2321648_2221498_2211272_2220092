package cse213.cse213_sporting_club_operations.TanvirMahmud;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;

import java.io.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class Goal8 {
    // Player selection
    @FXML private ComboBox<String> playerComboBox;
    @FXML private TextField positionField;
    @FXML private TextField contractField;
    @FXML private TextField valueField;

    // Transfer details
    @FXML private ComboBox<String> transferTypeComboBox;
    @FXML private TextField receivingClubField;
    @FXML private TextField transferFeeField;
    @FXML private DatePicker transferDatePicker;
    @FXML private TextArea termsTextArea;

    // Procedure checklist
    @FXML private CheckBox medicalClearanceCheckBox;
    @FXML private CheckBox financialSettlementCheckBox;
    @FXML private CheckBox contractTerminationCheckBox;
    @FXML private CheckBox playerConsentCheckBox;
    @FXML private CheckBox transferDocumentsCheckBox;
    @FXML private CheckBox leagueNotificationCheckBox;

    // Transfer history
    @FXML private TableView<TransferRecord> transferTable;
    @FXML private TableColumn<TransferRecord, String> playerColumn;
    @FXML private TableColumn<TransferRecord, String> typeColumn;
    @FXML private TableColumn<TransferRecord, String> destinationColumn;
    @FXML private TableColumn<TransferRecord, String> feeColumn;
    @FXML private TableColumn<TransferRecord, LocalDate> dateColumn;
    @FXML private TableColumn<TransferRecord, String> statusColumn;

    private ObservableList<TransferRecord> transfers = FXCollections.observableArrayList();
    private ArrayList<Player> players = new ArrayList<>();

    private static final String USER_DATA_FILE = "C:\\Users\\Tanvir Mahmud\\Desktop\\SportsClubOperations\\data\\user.bin";

    @FXML
    public void initialize() {
        // Load player data from user.bin
        loadPlayersFromFile();

        // Initialize transfer types
        transferTypeComboBox.setItems(FXCollections.observableArrayList(
                "Permanent Transfer", "Release/Free Transfer", "Contract Termination",
                "Retirement", "Buy-out Clause Activation", "End of Contract"
        ));

        // Set default transfer date to today
        transferDatePicker.setValue(LocalDate.now());

        // Disable receiving club and fee fields for certain transfer types
        transferTypeComboBox.valueProperty().addListener((obs, oldVal, newVal) -> {
            boolean isTransfer = "Permanent Transfer".equals(newVal) ||
                    "Buy-out Clause Activation".equals(newVal);

            receivingClubField.setDisable(!isTransfer);
            transferFeeField.setDisable(!isTransfer);

            if (!isTransfer) {
                receivingClubField.clear();
                transferFeeField.clear();
            }
        });

        // Setup table columns
        playerColumn.setCellValueFactory(new PropertyValueFactory<>("playerName"));
        typeColumn.setCellValueFactory(new PropertyValueFactory<>("transferType"));
        destinationColumn.setCellValueFactory(data -> {
            String destination = data.getValue().getDestinationClub();
            return new SimpleStringProperty(destination == null || destination.isEmpty() ?
                    "N/A" : destination);
        });
        feeColumn.setCellValueFactory(data -> {
            Double fee = data.getValue().getTransferFee();
            if (fee != null && fee > 0) {
                return new SimpleStringProperty(String.format("$%.2fM", fee));
            } else {
                return new SimpleStringProperty("N/A");
            }
        });
        dateColumn.setCellValueFactory(new PropertyValueFactory<>("transferDate"));
        statusColumn.setCellValueFactory(new PropertyValueFactory<>("status"));

        transferTable.setItems(transfers);

        // Add listener for player selection
        playerComboBox.valueProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null) {
                updatePlayerDetails(newVal);
            } else {
                clearPlayerDetails();
            }
        });
    }

    private void loadPlayersFromFile() {
        try {
            File file = new File(USER_DATA_FILE);
            if (file.exists()) {
                FileInputStream fis = new FileInputStream(file);
                ObjectInputStream ois = new ObjectInputStream(fis);

                List<Player> playerList = (List<Player>) ois.readObject();
                players.clear();
                players.addAll(playerList);

                ois.close();
                fis.close();

                System.out.println("Loaded " + playerList.size() + " players from " + USER_DATA_FILE);

                // Update the ComboBox with loaded players
                ObservableList<String> playerNames = FXCollections.observableArrayList();
                for (Player player : players) {
                    playerNames.add(player.getName());
                }
                playerComboBox.setItems(playerNames);
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

    private void loadSampleData() {
        players.clear();
        players.add(new Player("Jude Bellingham", "20", "Midfielder", "95%", "$120M"));
        players.add(new Player("Erling Haaland", "23", "Striker", "98%", "$180M"));
        players.add(new Player("Florian Wirtz", "20", "Midfielder", "92%", "$85M"));

        // Update ComboBox
        ObservableList<String> playerNames = FXCollections.observableArrayList();
        for (Player player : players) {
            playerNames.add(player.getName());
        }
        playerComboBox.setItems(playerNames);
    }

    private void updatePlayerDetails(String playerName) {
        for (Player player : players) {
            if (player.getName().equals(playerName)) {
                positionField.setText(player.getPosition());
                contractField.setText("June 2025"); // Sample contract end date
                valueField.setText(player.getMarketValue());
                return;
            }
        }
    }

    private void clearPlayerDetails() {
        positionField.clear();
        contractField.clear();
        valueField.clear();
    }

    @FXML
    public void processTransfer() {
        // Validate required fields
        if (playerComboBox.getValue() == null || playerComboBox.getValue().isEmpty()) {
            showAlert("Error", "Please select a player", Alert.AlertType.ERROR);
            return;
        }

        if (transferTypeComboBox.getValue() == null || transferTypeComboBox.getValue().isEmpty()) {
            showAlert("Error", "Please select a transfer type", Alert.AlertType.ERROR);
            return;
        }

        boolean isTransfer = "Permanent Transfer".equals(transferTypeComboBox.getValue()) ||
                "Buy-out Clause Activation".equals(transferTypeComboBox.getValue());

        if (isTransfer) {
            if (receivingClubField.getText().trim().isEmpty()) {
                showAlert("Error", "Please enter the receiving club", Alert.AlertType.ERROR);
                return;
            }

            if (transferFeeField.getText().trim().isEmpty()) {
                showAlert("Error", "Please enter the transfer fee", Alert.AlertType.ERROR);
                return;
            }
        }

        // Verify checklist completion
        if (!allRequiredProceduresCompleted()) {
            boolean proceed = showConfirmation("Incomplete Procedures",
                    "Not all required procedures have been completed. Proceed anyway?");
            if (!proceed) return;
        }

        // Create transfer record
        String playerName = playerComboBox.getValue();
        String transferType = transferTypeComboBox.getValue();
        String destinationClub = receivingClubField.getText().trim();

        Double transferFee = 0.0;
        if (isTransfer && !transferFeeField.getText().trim().isEmpty()) {
            try {
                transferFee = Double.parseDouble(transferFeeField.getText().replaceAll("[^\\d.]", ""));
            } catch (NumberFormatException e) {
                showAlert("Error", "Invalid transfer fee format", Alert.AlertType.ERROR);
                return;
            }
        }

        LocalDate transferDate = transferDatePicker.getValue();

        TransferRecord transfer = new TransferRecord(
                playerName,
                transferType,
                isTransfer ? destinationClub : null,
                isTransfer ? transferFee : 0.0,
                transferDate,
                areAllCheckboxesSelected() ? "Completed" : "Pending",
                medicalClearanceCheckBox.isSelected(),
                financialSettlementCheckBox.isSelected(),
                contractTerminationCheckBox.isSelected(),
                playerConsentCheckBox.isSelected(),
                transferDocumentsCheckBox.isSelected(),
                leagueNotificationCheckBox.isSelected()
        );

        transfers.add(transfer);

        showAlert("Success", "Transfer process initiated successfully", Alert.AlertType.INFORMATION);
        clearForm();
    }

    @FXML
    public void completeTransfer() {
        TransferRecord selectedTransfer = transferTable.getSelectionModel().getSelectedItem();
        if (selectedTransfer == null) {
            showAlert("Error", "Please select a transfer to complete", Alert.AlertType.ERROR);
            return;
        }

        if ("Completed".equals(selectedTransfer.getStatus())) {
            showAlert("Information", "This transfer is already completed", Alert.AlertType.INFORMATION);
            return;
        }

        // Show checklist dialog to complete missing procedures
        Dialog<Boolean> dialog = new Dialog<>();
        dialog.setTitle("Complete Transfer Procedures");
        dialog.setHeaderText("Complete required procedures for " + selectedTransfer.getPlayerName());

        ButtonType completeButtonType = new ButtonType("Complete Transfer", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(completeButtonType, ButtonType.CANCEL);

        // Create checklist pane
        CheckBox medicalBox = new CheckBox("Medical Clearance");
        medicalBox.setSelected(selectedTransfer.isMedicalClearance());

        CheckBox financialBox = new CheckBox("Financial Settlement");
        financialBox.setSelected(selectedTransfer.isFinancialSettlement());

        CheckBox contractBox = new CheckBox("Contract Termination");
        contractBox.setSelected(selectedTransfer.isContractTermination());

        CheckBox consentBox = new CheckBox("Player Consent");
        consentBox.setSelected(selectedTransfer.isPlayerConsent());

        CheckBox documentsBox = new CheckBox("Transfer Documents");
        documentsBox.setSelected(selectedTransfer.isTransferDocuments());

        CheckBox notificationBox = new CheckBox("League Notification");
        notificationBox.setSelected(selectedTransfer.isLeagueNotification());

        VBox content = new VBox(10,
                medicalBox, financialBox, contractBox,
                consentBox, documentsBox, notificationBox);
        dialog.getDialogPane().setContent(content);

        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == completeButtonType) {
                // Update transfer record with new checklist status
                selectedTransfer.setMedicalClearance(medicalBox.isSelected());
                selectedTransfer.setFinancialSettlement(financialBox.isSelected());
                selectedTransfer.setContractTermination(contractBox.isSelected());
                selectedTransfer.setPlayerConsent(consentBox.isSelected());
                selectedTransfer.setTransferDocuments(documentsBox.isSelected());
                selectedTransfer.setLeagueNotification(notificationBox.isSelected());

                // Check if all procedures are now complete
                if (medicalBox.isSelected() && financialBox.isSelected() &&
                        contractBox.isSelected() && consentBox.isSelected() &&
                        documentsBox.isSelected() && notificationBox.isSelected()) {
                    selectedTransfer.setStatus("Completed");
                    return true;
                } else {
                    selectedTransfer.setStatus("Pending");
                    return false;
                }
            }
            return null;
        });

        Optional<Boolean> result = dialog.showAndWait();
        result.ifPresent(allComplete -> {
            // Update the table
            transferTable.refresh();

            if (allComplete) {
                showAlert("Success", "Transfer completed successfully", Alert.AlertType.INFORMATION);
            } else {
                showAlert("Information", "Transfer remains pending until all procedures are completed",
                        Alert.AlertType.INFORMATION);
            }
        });
    }

    @FXML
    public void cancelTransfer() {
        TransferRecord selectedTransfer = transferTable.getSelectionModel().getSelectedItem();
        if (selectedTransfer == null) {
            showAlert("Error", "Please select a transfer to cancel", Alert.AlertType.ERROR);
            return;
        }

        if ("Completed".equals(selectedTransfer.getStatus())) {
            boolean confirm = showConfirmation("Cancel Completed Transfer",
                    "This transfer is already completed. Canceling it may require additional paperwork. Continue?");
            if (!confirm) return;
        }

        boolean confirm = showConfirmation("Confirm Cancellation",
                "Are you sure you want to cancel this transfer?");

        if (confirm) {
            selectedTransfer.setStatus("Cancelled");
            transferTable.refresh();
            showAlert("Success", "Transfer cancelled successfully", Alert.AlertType.INFORMATION);
        }
    }

    private boolean allRequiredProceduresCompleted() {
        return areAllCheckboxesSelected();
    }

    private boolean areAllCheckboxesSelected() {
        return medicalClearanceCheckBox.isSelected() &&
                financialSettlementCheckBox.isSelected() &&
                contractTerminationCheckBox.isSelected() &&
                playerConsentCheckBox.isSelected() &&
                transferDocumentsCheckBox.isSelected() &&
                leagueNotificationCheckBox.isSelected();
    }

    private void clearForm() {
        playerComboBox.setValue(null);
        clearPlayerDetails();
        transferTypeComboBox.setValue(null);
        receivingClubField.clear();
        transferFeeField.clear();
        transferDatePicker.setValue(LocalDate.now());
        termsTextArea.clear();

        // Uncheck all checkboxes
        medicalClearanceCheckBox.setSelected(false);
        financialSettlementCheckBox.setSelected(false);
        contractTerminationCheckBox.setSelected(false);
        playerConsentCheckBox.setSelected(false);
        transferDocumentsCheckBox.setSelected(false);
        leagueNotificationCheckBox.setSelected(false);
    }

    private void showAlert(String title, String message, Alert.AlertType type) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private boolean showConfirmation(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);

        Optional<ButtonType> result = alert.showAndWait();
        return result.isPresent() && result.get() == ButtonType.OK;
    }

    @FXML
    public void generateTransferCertificate() {
        TransferRecord selectedTransfer = transferTable.getSelectionModel().getSelectedItem();
        if (selectedTransfer == null) {
            showAlert("Error", "Please select a completed transfer", Alert.AlertType.ERROR);
            return;
        }

        if (!"Completed".equals(selectedTransfer.getStatus())) {
            showAlert("Error", "Certificate can only be generated for completed transfers",
                    Alert.AlertType.ERROR);
            return;
        }

        // Show certificate preview
        Dialog<Void> dialog = new Dialog<>();
        dialog.setTitle("Transfer Certificate");
        dialog.setHeaderText("Official Transfer Certificate");

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd MMMM yyyy");
        String certificateText = String.format(
                "TRANSFER CERTIFICATE\n\n" +
                        "This document certifies that %s has been officially transferred from " +
                        "Our Club to %s via %s on %s.\n\n" +
                        "All required procedures have been completed and verified:\n" +
                        "- Medical clearance: Completed\n" +
                        "- Financial settlement: Completed\n" +
                        "- Contract termination: Completed\n" +
                        "- Player consent: Obtained\n" +
                        "- Transfer documents: Filed\n" +
                        "- League notification: Submitted\n\n" +
                        "This transfer complies with all league regulations and FIFA transfer rules.\n\n" +
                        "Certificate ID: TRF-%d-%s",
                selectedTransfer.getPlayerName(),
                selectedTransfer.getDestinationClub() != null ? selectedTransfer.getDestinationClub() : "N/A",
                selectedTransfer.getTransferType(),
                selectedTransfer.getTransferDate().format(formatter),
                (int)(Math.random() * 100000),
                LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"))
        );

        TextArea certificateArea = new TextArea(certificateText);
        certificateArea.setEditable(false);
        certificateArea.setWrapText(true);
        certificateArea.setPrefWidth(400);
        certificateArea.setPrefHeight(300);

        dialog.getDialogPane().setContent(certificateArea);
        dialog.getDialogPane().getButtonTypes().add(ButtonType.CLOSE);

        dialog.showAndWait();
    }

    @FXML
    public void goal8Dashboard(ActionEvent actionEvent) throws IOException {
        SceneSwitcher.switchTo("DashboardTransferWindowManager.fxml", actionEvent);
    }

    // Transfer record class

}