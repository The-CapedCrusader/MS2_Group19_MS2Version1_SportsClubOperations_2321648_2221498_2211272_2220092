package cse213.cse213_sporting_club_operations.TanvirMahmud;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.io.IOException;
import java.text.NumberFormat;
import java.time.LocalDate;
import java.util.Locale;

public class Goal5 {
    // Budget tracking
    @FXML private TextField totalBudgetField;
    @FXML private TextField remainingBudgetField;
    @FXML private TextField salaryCappedField;
    @FXML private TextField salarySpentField;
    @FXML private ProgressBar budgetProgressBar;
    @FXML private ProgressBar salaryProgressBar;

    // Transaction entry
    @FXML private TextField transactionAmountField;
    @FXML private ComboBox<String> transactionTypeComboBox;
    @FXML private TextField transactionDescriptionField;
    @FXML private DatePicker transactionDatePicker;
    @FXML private CheckBox regulationCompliantCheckBox;

    // Transaction table
    @FXML private TableView<Transaction> transactionTable;
    @FXML private TableColumn<Transaction, String> amountColumn;
    @FXML private TableColumn<Transaction, String> typeColumn;
    @FXML private TableColumn<Transaction, String> descriptionColumn;
    @FXML private TableColumn<Transaction, LocalDate> dateColumn;
    @FXML private TableColumn<Transaction, Boolean> compliantColumn;

    // Alerts and notifications
    @FXML private Label alertLabel;

    private double totalBudget = 100000000.0; // $100M default
    private double remainingBudget = 100000000.0;
    private double salaryCap = 60000000.0; // $60M default
    private double salarySpent = 0.0;

    private ObservableList<Transaction> transactions = FXCollections.observableArrayList();
    private NumberFormat currencyFormat = NumberFormat.getCurrencyInstance(Locale.US);

    @FXML
    public void initialize() {
        // Initialize transaction types
        transactionTypeComboBox.setItems(FXCollections.observableArrayList(
                "Player Purchase", "Player Sale", "Salary Increase", "Salary Reduction",
                "Budget Adjustment", "Salary Cap Adjustment"));

        // Initialize date picker to today
        transactionDatePicker.setValue(LocalDate.now());

        // Set up budget displays
        updateBudgetDisplays();

        // Set up transaction table
        amountColumn.setCellValueFactory(data -> new SimpleStringProperty(
                currencyFormat.format(data.getValue().getAmount())));
        typeColumn.setCellValueFactory(new PropertyValueFactory<>("type"));
        descriptionColumn.setCellValueFactory(new PropertyValueFactory<>("description"));
        dateColumn.setCellValueFactory(new PropertyValueFactory<>("date"));
        compliantColumn.setCellValueFactory(new PropertyValueFactory<>("compliant"));

        transactionTable.setItems(transactions);

        // Clear alert
        alertLabel.setText("");

        // Example data for testing
        addSampleTransactions();
    }

    private void addSampleTransactions() {
        addTransaction(15000000.0, "Player Purchase", "Signed midfielder James Rodriguez", LocalDate.now().minusDays(30), true);
        addTransaction(8000000.0, "Player Sale", "Sold defender David Luiz", LocalDate.now().minusDays(15), true);
        addTransaction(5000000.0, "Salary Increase", "Contract renewal for striker", LocalDate.now().minusDays(7), true);
    }

    private void updateBudgetDisplays() {
        totalBudgetField.setText(currencyFormat.format(totalBudget));
        remainingBudgetField.setText(currencyFormat.format(remainingBudget));
        salaryCappedField.setText(currencyFormat.format(salaryCap));
        salarySpentField.setText(currencyFormat.format(salarySpent));

        budgetProgressBar.setProgress(1 - (remainingBudget / totalBudget));
        salaryProgressBar.setProgress(salarySpent / salaryCap);

        // Check for alerts
        checkFinancialAlerts();
    }

    private void checkFinancialAlerts() {
        if (remainingBudget < 0) {
            alertLabel.setText("WARNING: Transfer budget exceeded!");
            alertLabel.setStyle("-fx-text-fill: red;");
        } else if (salarySpent > salaryCap) {
            alertLabel.setText("WARNING: Salary cap exceeded!");
            alertLabel.setStyle("-fx-text-fill: red;");
        } else if (remainingBudget < totalBudget * 0.1) {
            alertLabel.setText("CAUTION: Less than 10% of transfer budget remaining.");
            alertLabel.setStyle("-fx-text-fill: orange;");
        } else if (salarySpent > salaryCap * 0.9) {
            alertLabel.setText("CAUTION: Approaching salary cap limit.");
            alertLabel.setStyle("-fx-text-fill: orange;");
        } else {
            alertLabel.setText("Financial status: Within budget and salary constraints.");
            alertLabel.setStyle("-fx-text-fill: green;");
        }
    }

    @FXML
    public void processTransaction() {
        // Validate inputs
        if (transactionTypeComboBox.getValue() == null || transactionTypeComboBox.getValue().isEmpty()) {
            showAlert("Error", "Please select a transaction type.", Alert.AlertType.ERROR);
            return;
        }

        if (transactionDescriptionField.getText().trim().isEmpty()) {
            showAlert("Error", "Please enter a transaction description.", Alert.AlertType.ERROR);
            return;
        }

        double amount;
        try {
            amount = Double.parseDouble(transactionAmountField.getText().replace(",", ""));
            if (amount <= 0) {
                showAlert("Error", "Amount must be greater than zero.", Alert.AlertType.ERROR);
                return;
            }
        } catch (NumberFormatException e) {
            showAlert("Error", "Please enter a valid amount.", Alert.AlertType.ERROR);
            return;
        }

        String type = transactionTypeComboBox.getValue();
        String description = transactionDescriptionField.getText().trim();
        LocalDate date = transactionDatePicker.getValue();
        boolean compliant = regulationCompliantCheckBox.isSelected();

        if (!compliant) {
            boolean proceed = showConfirmation("Regulatory Warning",
                    "This transaction may not comply with league financial regulations. Proceed anyway?");
            if (!proceed) return;
        }

        // Update budgets based on transaction type
        updateFinancials(amount, type);

        // Add transaction to the table
        addTransaction(amount, type, description, date, compliant);

        // Clear form
        clearTransactionForm();

        showAlert("Success", "Transaction recorded successfully.", Alert.AlertType.INFORMATION);
    }

    private void updateFinancials(double amount, String type) {
        switch (type) {
            case "Player Purchase":
                remainingBudget -= amount;
                break;
            case "Player Sale":
                remainingBudget += amount;
                break;
            case "Salary Increase":
                salarySpent += amount;
                break;
            case "Salary Reduction":
                salarySpent -= amount;
                if (salarySpent < 0) salarySpent = 0;
                break;
            case "Budget Adjustment":
                totalBudget += amount;
                remainingBudget += amount;
                break;
            case "Salary Cap Adjustment":
                salaryCap += amount;
                break;
        }

        updateBudgetDisplays();
    }

    private void addTransaction(double amount, String type, String description,
                                LocalDate date, boolean compliant) {
        Transaction transaction = new Transaction(amount, type, description, date, compliant);
        transactions.add(transaction);
    }

    @FXML
    public void deleteTransaction() {
        Transaction selectedTransaction = transactionTable.getSelectionModel().getSelectedItem();
        if (selectedTransaction == null) {
            showAlert("Error", "Please select a transaction to delete.", Alert.AlertType.ERROR);
            return;
        }

        boolean confirm = showConfirmation("Confirm Delete",
                "Are you sure you want to delete this transaction? This will update your financial status.");
        if (!confirm) return;

        // Reverse the financial impact of this transaction
        reverseTransaction(selectedTransaction);

        // Remove from the table
        transactions.remove(selectedTransaction);

        showAlert("Success", "Transaction deleted and financials updated.", Alert.AlertType.INFORMATION);
    }

    private void reverseTransaction(Transaction transaction) {
        double amount = transaction.getAmount();
        String type = transaction.getType();

        // Apply opposite effect
        switch (type) {
            case "Player Purchase":
                remainingBudget += amount;
                break;
            case "Player Sale":
                remainingBudget -= amount;
                break;
            case "Salary Increase":
                salarySpent -= amount;
                if (salarySpent < 0) salarySpent = 0;
                break;
            case "Salary Reduction":
                salarySpent += amount;
                break;
            case "Budget Adjustment":
                totalBudget -= amount;
                remainingBudget -= amount;
                break;
            case "Salary Cap Adjustment":
                salaryCap -= amount;
                break;
        }

        updateBudgetDisplays();
    }

    @FXML
    public void adjustBudget() {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Adjust Total Budget");
        dialog.setHeaderText("Enter new total transfer budget:");
        dialog.setContentText("Budget amount:");

        dialog.showAndWait().ifPresent(result -> {
            try {
                double newBudget = Double.parseDouble(result.replace(",", ""));
                if (newBudget < 0) {
                    showAlert("Error", "Budget cannot be negative.", Alert.AlertType.ERROR);
                    return;
                }

                double adjustment = newBudget - totalBudget;
                totalBudget = newBudget;
                remainingBudget += adjustment;

                addTransaction(adjustment, "Budget Adjustment",
                        "Manual budget adjustment to " + currencyFormat.format(newBudget),
                        LocalDate.now(), true);

                updateBudgetDisplays();

            } catch (NumberFormatException e) {
                showAlert("Error", "Please enter a valid number.", Alert.AlertType.ERROR);
            }
        });
    }

    @FXML
    public void adjustSalaryCap() {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Adjust Salary Cap");
        dialog.setHeaderText("Enter new salary cap:");
        dialog.setContentText("Salary cap amount:");

        dialog.showAndWait().ifPresent(result -> {
            try {
                double newSalaryCap = Double.parseDouble(result.replace(",", ""));
                if (newSalaryCap < 0) {
                    showAlert("Error", "Salary cap cannot be negative.", Alert.AlertType.ERROR);
                    return;
                }

                double adjustment = newSalaryCap - salaryCap;
                salaryCap = newSalaryCap;

                addTransaction(adjustment, "Salary Cap Adjustment",
                        "Manual salary cap adjustment to " + currencyFormat.format(newSalaryCap),
                        LocalDate.now(), true);

                updateBudgetDisplays();

            } catch (NumberFormatException e) {
                showAlert("Error", "Please enter a valid number.", Alert.AlertType.ERROR);
            }
        });
    }

    private void clearTransactionForm() {
        transactionAmountField.clear();
        transactionTypeComboBox.setValue(null);
        transactionDescriptionField.clear();
        transactionDatePicker.setValue(LocalDate.now());
        regulationCompliantCheckBox.setSelected(true);
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

        return alert.showAndWait().orElse(ButtonType.CANCEL) == ButtonType.OK;
    }

    @FXML
    public void goal5Dashboard(ActionEvent actionEvent) throws IOException {
        SceneSwitcher.switchTo("DashboardTransferWindowManager.fxml", actionEvent);
    }

    // Transaction model class
    public static class Transaction {
        private final double amount;
        private final String type;
        private final String description;
        private final LocalDate date;
        private final boolean compliant;

        public Transaction(double amount, String type, String description,
                           LocalDate date, boolean compliant) {
            this.amount = amount;
            this.type = type;
            this.description = description;
            this.date = date;
            this.compliant = compliant;
        }

        public double getAmount() { return amount; }
        public String getType() { return type; }
        public String getDescription() { return description; }
        public LocalDate getDate() { return date; }
        public boolean isCompliant() { return compliant; }
    }
}