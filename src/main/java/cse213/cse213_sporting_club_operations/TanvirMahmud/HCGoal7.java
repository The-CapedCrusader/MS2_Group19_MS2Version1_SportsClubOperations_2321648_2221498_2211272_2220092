package cse213.cse213_sporting_club_operations.TanvirMahmud;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.PieChart;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.io.IOException;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public class HCGoal7 {
    @FXML private ComboBox<String> playerComboBox;
    @FXML private DatePicker incidentDatePicker;
    @FXML private ComboBox<String> incidentTypeComboBox;
    @FXML private TextField locationField;
    @FXML private TextArea descriptionArea;
    @FXML private ComboBox<String> actionTypeComboBox;
    @FXML private TextField fineAmountField;
    @FXML private DatePicker startDatePicker;
    @FXML private DatePicker endDatePicker;
    @FXML private CheckBox leagueReportedCheckBox;
    @FXML private TextArea actionNotesArea;

    @FXML private TableView<DisciplinaryAction> disciplinaryTable;
    @FXML private TableColumn<DisciplinaryAction, String> playerColumn;
    @FXML private TableColumn<DisciplinaryAction, LocalDate> dateColumn;
    @FXML private TableColumn<DisciplinaryAction, String> incidentColumn;
    @FXML private TableColumn<DisciplinaryAction, String> actionColumn;
    @FXML private TableColumn<DisciplinaryAction, String> durationColumn;
    @FXML private TableColumn<DisciplinaryAction, String> statusColumn;

    @FXML private ComboBox<String> filterPlayerComboBox;
    @FXML private ComboBox<String> filterTypeComboBox;
    @FXML private ComboBox<String> statusFilterComboBox;
    @FXML private PieChart disciplinaryPieChart;

    private ObservableList<DisciplinaryAction> disciplinaryActions = FXCollections.observableArrayList();
    private FilteredList<DisciplinaryAction> filteredActions;

    @FXML
    public void initialize() {
        setupComboBoxes();
        setupTable();
        setupListeners();
        loadSampleData();
        updatePieChart();

        // Setup filtered list
        filteredActions = new FilteredList<>(disciplinaryActions, p -> true);
        disciplinaryTable.setItems(filteredActions);
    }

    private void setupComboBoxes() {
        // Players
        ObservableList<String> players = FXCollections.observableArrayList(
                "David De Gea", "Harry Maguire", "Luke Shaw", "Bruno Fernandes",
                "Marcus Rashford", "Cristiano Ronaldo", "Mason Greenwood",
                "Aaron Wan-Bissaka", "Scott McTominay", "Fred", "Jadon Sancho"
        );
        playerComboBox.setItems(players);
        filterPlayerComboBox.getItems().add("All Players");
        filterPlayerComboBox.getItems().addAll(players);
        filterPlayerComboBox.setValue("All Players");

        // Incident types
        ObservableList<String> incidentTypes = FXCollections.observableArrayList(
                "Late for Training", "Missed Training", "Red Card", "Yellow Card Accumulation",
                "Violation of Team Rules", "Media Conduct Violation", "Technical Area Incident",
                "Off-field Misconduct", "Social Media Violation"
        );
        incidentTypeComboBox.setItems(incidentTypes);
        filterTypeComboBox.getItems().add("All Types");
        filterTypeComboBox.getItems().addAll(incidentTypes);
        filterTypeComboBox.setValue("All Types");

        // Action types
        actionTypeComboBox.getItems().addAll(
                "Verbal Warning", "Written Warning", "Fine", "Suspension",
                "Community Service", "Training Ground Penalty", "Demotion to Reserves"
        );

        // Status filter
        statusFilterComboBox.getItems().addAll(
                "All Status", "Active", "Completed", "Pending"
        );
        statusFilterComboBox.setValue("All Status");

        // Set default dates
        incidentDatePicker.setValue(LocalDate.now());
    }

    private void setupTable() {
        playerColumn.setCellValueFactory(new PropertyValueFactory<>("playerName"));
        dateColumn.setCellValueFactory(new PropertyValueFactory<>("incidentDate"));
        incidentColumn.setCellValueFactory(new PropertyValueFactory<>("incidentType"));
        actionColumn.setCellValueFactory(new PropertyValueFactory<>("actionType"));
        durationColumn.setCellValueFactory(new PropertyValueFactory<>("duration"));
        statusColumn.setCellValueFactory(new PropertyValueFactory<>("status"));

        disciplinaryTable.getSelectionModel().selectedItemProperty().addListener(
                (obs, oldSelection, newSelection) -> {
                    if (newSelection != null) {
                        populateForm(newSelection);
                    }
                });
    }

    private void setupListeners() {
        // Enable/disable fields based on action type
        actionTypeComboBox.valueProperty().addListener((obs, oldVal, newVal) -> {
            boolean isFine = "Fine".equals(newVal);
            boolean isSuspension = "Suspension".equals(newVal);

            fineAmountField.setDisable(!isFine);
            startDatePicker.setDisable(!isSuspension);
            endDatePicker.setDisable(!isSuspension);

            if (isSuspension) {
                startDatePicker.setValue(LocalDate.now());
                endDatePicker.setValue(LocalDate.now().plusDays(3));
            }
        });
    }

    private void loadSampleData() {
        disciplinaryActions.addAll(
            new DisciplinaryAction("Bruno Fernandes", LocalDate.now().minusDays(5),
                    "Yellow Card Accumulation", "Suspension",
                    LocalDate.now().minusDays(3), LocalDate.now().minusDays(1),
                    0.0, "Match suspension due to 5 yellow cards", true),
            new DisciplinaryAction("Marcus Rashford", LocalDate.now().minusDays(10),
                    "Late for Training", "Fine",
                    null, null,
                    1000.0, "Player was 45 minutes late for training", false),
            new DisciplinaryAction("Harry Maguire", LocalDate.now().minusDays(15),
                    "Red Card", "Suspension",
                    LocalDate.now().minusDays(14), LocalDate.now().minusDays(7),
                    0.0, "Direct red card for serious foul play", true)
        );
    }

    private void populateForm(DisciplinaryAction action) {
        playerComboBox.setValue(action.getPlayerName());
        incidentDatePicker.setValue(action.getIncidentDate());
        incidentTypeComboBox.setValue(action.getIncidentType());
        locationField.setText(action.getLocation());
        descriptionArea.setText(action.getDescription());
        actionTypeComboBox.setValue(action.getActionType());

        if (action.getFineAmount() > 0) {
            fineAmountField.setText(String.valueOf(action.getFineAmount()));
            fineAmountField.setDisable(false);
        } else {
            fineAmountField.clear();
            fineAmountField.setDisable(true);
        }

        if (action.getStartDate() != null) {
            startDatePicker.setValue(action.getStartDate());
            endDatePicker.setValue(action.getEndDate());
            startDatePicker.setDisable(false);
            endDatePicker.setDisable(false);
        } else {
            startDatePicker.setValue(null);
            endDatePicker.setValue(null);
            startDatePicker.setDisable(true);
            endDatePicker.setDisable(true);
        }

        leagueReportedCheckBox.setSelected(action.isLeagueReported());
        actionNotesArea.setText(action.getDescription());
    }

    private void updatePieChart() {
        // Count incidents by type
        long redCards = disciplinaryActions.stream()
                .filter(a -> "Red Card".equals(a.getIncidentType())).count();
        long yellowCards = disciplinaryActions.stream()
                .filter(a -> "Yellow Card Accumulation".equals(a.getIncidentType())).count();
        long lateTraining = disciplinaryActions.stream()
                .filter(a -> "Late for Training".equals(a.getIncidentType())).count();
        long missedTraining = disciplinaryActions.stream()
                .filter(a -> "Missed Training".equals(a.getIncidentType())).count();
        long otherViolations = disciplinaryActions.size() - redCards - yellowCards
                - lateTraining - missedTraining;

        ObservableList<PieChart.Data> pieChartData = FXCollections.observableArrayList(
                new PieChart.Data("Red Cards", redCards),
                new PieChart.Data("Yellow Cards", yellowCards),
                new PieChart.Data("Late for Training", lateTraining),
                new PieChart.Data("Missed Training", missedTraining),
                new PieChart.Data("Other Violations", otherViolations)
        );

        disciplinaryPieChart.setTitle("Disciplinary Incidents");
        disciplinaryPieChart.setData(pieChartData);
    }

    @FXML
    public void saveDisciplinaryAction() {
        if (!validateInput()) {
            return;
        }

        DisciplinaryAction action = createActionFromForm();
        disciplinaryActions.add(action);
        clearForm();
        updatePieChart();

        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Action Saved");
        alert.setHeaderText(null);
        alert.setContentText("Disciplinary action has been saved successfully.");
        alert.showAndWait();
    }

    @FXML
    public void updateDisciplinaryAction() {
        DisciplinaryAction selected = disciplinaryTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showAlert(Alert.AlertType.WARNING, "No Selection",
                    "Please select a disciplinary action to update.");
            return;
        }

        if (!validateInput()) {
            return;
        }

        disciplinaryActions.remove(selected);
        DisciplinaryAction updated = createActionFromForm();
        disciplinaryActions.add(updated);
        updatePieChart();

        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Action Updated");
        alert.setHeaderText(null);
        alert.setContentText("Disciplinary action has been updated successfully.");
        alert.showAndWait();
    }

    @FXML
    public void deleteDisciplinaryAction() {
        DisciplinaryAction selected = disciplinaryTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showAlert(Alert.AlertType.WARNING, "No Selection",
                    "Please select a disciplinary action to delete.");
            return;
        }

        Alert confirmation = new Alert(Alert.AlertType.CONFIRMATION);
        confirmation.setTitle("Confirm Deletion");
        confirmation.setHeaderText(null);
        confirmation.setContentText("Are you sure you want to delete this disciplinary action?");

        if (confirmation.showAndWait().get() == ButtonType.OK) {
            disciplinaryActions.remove(selected);
            clearForm();
            updatePieChart();
        }
    }

    @FXML
    public void clearForm() {
        playerComboBox.getSelectionModel().clearSelection();
        incidentDatePicker.setValue(LocalDate.now());
        incidentTypeComboBox.getSelectionModel().clearSelection();
        locationField.clear();
        descriptionArea.clear();
        actionTypeComboBox.getSelectionModel().clearSelection();
        fineAmountField.clear();
        fineAmountField.setDisable(true);
        startDatePicker.setValue(null);
        startDatePicker.setDisable(true);
        endDatePicker.setValue(null);
        endDatePicker.setDisable(true);
        leagueReportedCheckBox.setSelected(false);
        actionNotesArea.clear();
    }

    @FXML
    public void applyFilter() {
        String playerFilter = filterPlayerComboBox.getValue();
        String typeFilter = filterTypeComboBox.getValue();
        String statusFilter = statusFilterComboBox.getValue();

        filteredActions.setPredicate(action -> {
            boolean playerMatch = "All Players".equals(playerFilter) ||
                    action.getPlayerName().equals(playerFilter);
            boolean typeMatch = "All Types".equals(typeFilter) ||
                    action.getIncidentType().equals(typeFilter);
            boolean statusMatch = "All Status".equals(statusFilter) ||
                    action.getStatus().equals(statusFilter);

            return playerMatch && typeMatch && statusMatch;
        });
    }

    @FXML
    public void resetFilter() {
        filterPlayerComboBox.setValue("All Players");
        filterTypeComboBox.setValue("All Types");
        statusFilterComboBox.setValue("All Status");
        filteredActions.setPredicate(p -> true);
    }

    @FXML
    public void generateReport() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Report Generated");
        alert.setHeaderText(null);
        alert.setContentText("Disciplinary report has been generated and saved.");
        alert.showAndWait();
    }

    @FXML
    public void dashboard() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("DashboardHeadCoach.fxml"));
            Parent root = loader.load();

            Stage stage = (Stage) playerComboBox.getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Navigation Error",
                    "Could not return to dashboard. Error: " + e.getMessage());
        }
    }

    private boolean validateInput() {
        if (playerComboBox.getValue() == null || incidentTypeComboBox.getValue() == null ||
                actionTypeComboBox.getValue() == null || incidentDatePicker.getValue() == null) {
            showAlert(Alert.AlertType.WARNING, "Incomplete Information",
                    "Please fill in all required fields (player, incident type, date, and action type).");
            return false;
        }

        if ("Fine".equals(actionTypeComboBox.getValue())) {
            try {
                Double.parseDouble(fineAmountField.getText());
            } catch (NumberFormatException e) {
                showAlert(Alert.AlertType.WARNING, "Invalid Fine Amount",
                        "Please enter a valid number for the fine amount.");
                return false;
            }
        }

        if ("Suspension".equals(actionTypeComboBox.getValue())) {
            if (startDatePicker.getValue() == null || endDatePicker.getValue() == null) {
                showAlert(Alert.AlertType.WARNING, "Missing Dates",
                        "Please select start and end dates for the suspension.");
                return false;
            }

            if (endDatePicker.getValue().isBefore(startDatePicker.getValue())) {
                showAlert(Alert.AlertType.WARNING, "Invalid Dates",
                        "End date cannot be before start date.");
                return false;
            }
        }

        return true;
    }

    private DisciplinaryAction createActionFromForm() {
        String player = playerComboBox.getValue();
        LocalDate incidentDate = incidentDatePicker.getValue();
        String incidentType = incidentTypeComboBox.getValue();
        String location = locationField.getText();
        String description = descriptionArea.getText();
        String actionType = actionTypeComboBox.getValue();

        LocalDate startDate = null;
        LocalDate endDate = null;
        if ("Suspension".equals(actionType)) {
            startDate = startDatePicker.getValue();
            endDate = endDatePicker.getValue();
        }

        double fineAmount = 0.0;
        if ("Fine".equals(actionType)) {
            fineAmount = Double.parseDouble(fineAmountField.getText());
        }

        boolean leagueReported = leagueReportedCheckBox.isSelected();

        return new DisciplinaryAction(player, incidentDate, incidentType, actionType,
                startDate, endDate, fineAmount, description, leagueReported);
    }

    private void showAlert(Alert.AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public static class DisciplinaryAction {
        private final String playerName;
        private final LocalDate incidentDate;
        private final String incidentType;
        private final String actionType;
        private final LocalDate startDate;
        private final LocalDate endDate;
        private final double fineAmount;
        private final String description;
        private final boolean leagueReported;
        private final String location;

        public DisciplinaryAction(String playerName, LocalDate incidentDate, String incidentType,
                                 String actionType, LocalDate startDate, LocalDate endDate,
                                 double fineAmount, String description, boolean leagueReported) {
            this.playerName = playerName;
            this.incidentDate = incidentDate;
            this.incidentType = incidentType;
            this.actionType = actionType;
            this.startDate = startDate;
            this.endDate = endDate;
            this.fineAmount = fineAmount;
            this.description = description;
            this.leagueReported = leagueReported;
            this.location = "Training Ground"; // Default location
        }

        public String getPlayerName() { return playerName; }
        public LocalDate getIncidentDate() { return incidentDate; }
        public String getIncidentType() { return incidentType; }
        public String getActionType() { return actionType; }
        public LocalDate getStartDate() { return startDate; }
        public LocalDate getEndDate() { return endDate; }
        public double getFineAmount() { return fineAmount; }
        public String getDescription() { return description; }
        public boolean isLeagueReported() { return leagueReported; }
        public String getLocation() { return location; }

        public String getDuration() {
            if (startDate == null || endDate == null) {
                return "N/A";
            }
            long days = ChronoUnit.DAYS.between(startDate, endDate) + 1;
            return days + " days";
        }

        public String getStatus() {
            if (actionType.equals("Verbal Warning") || actionType.equals("Written Warning")) {
                return "Completed";
            }

            if (actionType.equals("Fine")) {
                return "Pending";
            }

            if (actionType.equals("Suspension")) {
                if (endDate.isBefore(LocalDate.now())) {
                    return "Completed";
                } else if (startDate.isAfter(LocalDate.now())) {
                    return "Pending";
                } else {
                    return "Active";
                }
            }

            return "Pending";
        }
    }
}