package cse213.cse213_sporting_club_operations.TanvirMahmud;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXML;
import javafx.event.ActionEvent;
import javafx.scene.chart.PieChart;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

public class DisciplinaryController {
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
        updatePieChart();

        // Setup filtered list
        filteredActions = new FilteredList<>(disciplinaryActions, p -> true);
        disciplinaryTable.setItems(filteredActions);
    }

    private void setupComboBoxes() {
        // Load players from user.bin
        List<String> playerNames = loadPlayerNamesFromFile();

        if (playerNames.isEmpty()) {
            playerNames.add("No players found");
        }

        ObservableList<String> playerList = FXCollections.observableArrayList(playerNames);
        playerComboBox.setItems(playerList);

        filterPlayerComboBox.getItems().add("All Players");
        filterPlayerComboBox.getItems().addAll(playerList);
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

    private List<String> loadPlayerNamesFromFile() {
        List<String> playerNames = new ArrayList<>();

        // Add fallback data in case loading fails
        playerNames.add("Jude Bellingham");
        playerNames.add("Erling Haaland");
        playerNames.add("Florian Wirtz");

        try {
            // Using the same file path as in Goal7 class
            File file = new File("C:\\Users\\Tanvir Mahmud\\Desktop\\SportsClubOperations\\data\\user.bin");
            if (file.exists()) {
                try (FileInputStream fis = new FileInputStream(file);
                     ObjectInputStream ois = new ObjectInputStream(fis)) {

                    Object obj = ois.readObject();

                    if (obj instanceof List<?>) {
                        List<?> userList = (List<?>) obj;
                        for (Object user : userList) {
                            if (user instanceof Player) {
                                Player player = (Player) user;
                                playerNames.add(player.getName());
                            }
                        }
                    }

                    // Clear default players if we successfully loaded at least one
                    if (playerNames.size() > 3) {
                        playerNames = playerNames.subList(3, playerNames.size());
                    }

                    System.out.println("Loaded " + playerNames.size() + " players from user.bin");
                }
            } else {
                System.out.println("User.bin file not found at: " + file.getAbsolutePath());
            }
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("Error loading player data: " + e.getMessage());
            e.printStackTrace();
        }

        return playerNames;
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
        if (disciplinaryActions.isEmpty()) {
            disciplinaryPieChart.setTitle("No Disciplinary Incidents");
            disciplinaryPieChart.setData(FXCollections.observableArrayList(
                    new PieChart.Data("No Data", 1)));
            return;
        }

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

        ObservableList<PieChart.Data> pieChartData = FXCollections.observableArrayList();

        if (redCards > 0) pieChartData.add(new PieChart.Data("Red Cards", redCards));
        if (yellowCards > 0) pieChartData.add(new PieChart.Data("Yellow Cards", yellowCards));
        if (lateTraining > 0) pieChartData.add(new PieChart.Data("Late for Training", lateTraining));
        if (missedTraining > 0) pieChartData.add(new PieChart.Data("Missed Training", missedTraining));
        if (otherViolations > 0) pieChartData.add(new PieChart.Data("Other Violations", otherViolations));

        if (pieChartData.isEmpty()) {
            pieChartData.add(new PieChart.Data("No Data", 1));
        }

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

        showAlert(Alert.AlertType.INFORMATION, "Action Saved",
                "Disciplinary action has been saved successfully.");
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

        showAlert(Alert.AlertType.INFORMATION, "Action Updated",
                "Disciplinary action has been updated successfully.");
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
        showAlert(Alert.AlertType.INFORMATION, "Report Generated",
                "Disciplinary report has been generated and saved.");
    }

    @FXML
    public void dashboard(ActionEvent event) throws IOException {
        SceneSwitcher.switchTo("DashboardHeadCoach.fxml", event);
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
                startDate, endDate, fineAmount, description, leagueReported, location);
    }

    private void showAlert(Alert.AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }


}