package cse213.cse213_sporting_club_operations.TanvirMahmud;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.io.IOException;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class TrainingSessionScheduler_Controller {
    // Session planning
    @FXML private ComboBox<String> sessionTypeComboBox;
    @FXML private DatePicker sessionDatePicker;
    @FXML private ComboBox<String> startTimeComboBox;
    @FXML private ComboBox<String> durationComboBox;
    @FXML private ComboBox<String> intensityComboBox;
    @FXML private TextField locationField;

    // Session focus
    @FXML private ComboBox<String> primaryFocusComboBox;
    @FXML private ComboBox<String> secondaryFocusComboBox;
    @FXML private TextArea sessionObjectivesArea;
    @FXML private TextArea equipmentRequiredArea;

    @FXML private ListView<String> selectedPlayersListView;
    @FXML private TextField searchPlayerField;
    @FXML private ListView<String> availablePlayersListView;

    // Training schedule table
    @FXML private TableView<TrainingSession> trainingScheduleTable;
    @FXML private TableColumn<TrainingSession, String> typeColumn;
    @FXML private TableColumn<TrainingSession, String> dateColumn;
    @FXML private TableColumn<TrainingSession, String> timeColumn;
    @FXML private TableColumn<TrainingSession, String> durationColumn;
    @FXML private TableColumn<TrainingSession, String> focusColumn;
    @FXML private TableColumn<TrainingSession, String> intensityColumn;
    @FXML private TableColumn<TrainingSession, String> groupColumn;

    private ObservableList<TrainingSession> trainingSessions = FXCollections.observableArrayList();
    private ObservableList<String> availablePlayers = FXCollections.observableArrayList();
    private ObservableList<String> selectedPlayers = FXCollections.observableArrayList();
    private ObservableList<String> savedLineups = FXCollections.observableArrayList();
    private Map<String, List<TeamLineUp_Controller.PlayerLineup>> lineupPlayers = new HashMap<>();

    @FXML
    public void initialize() {
        // Initialize data directory
        HeadCoachDataManager.initializeDataDirectory();

        // Initialize session type options
        sessionTypeComboBox.setItems(FXCollections.observableArrayList(
                "Team Training", "Small Group Training", "Individual Session",
                "Recovery Session", "Tactical Briefing", "Match Simulation"
        ));

        // Initialize time options
        ObservableList<String> timeOptions = FXCollections.observableArrayList();
        for (int hour = 6; hour <= 20; hour++) {
            timeOptions.add(String.format("%02d:00", hour));
            timeOptions.add(String.format("%02d:30", hour));
        }
        startTimeComboBox.setItems(timeOptions);

        // Initialize duration options
        durationComboBox.setItems(FXCollections.observableArrayList(
                "30 minutes", "45 minutes", "60 minutes", "90 minutes", "120 minutes"
        ));

        // Initialize intensity options
        intensityComboBox.setItems(FXCollections.observableArrayList(
                "Light", "Medium", "High", "Match Intensity", "Recovery"
        ));

        // Initialize focus options
        ObservableList<String> focusOptions = FXCollections.observableArrayList(
                "Tactical Organization", "Technical Skills", "Physical Conditioning",
                "Set Pieces", "Attacking Patterns", "Defensive Structure",
                "Transition Play", "Goalkeeper Training", "Finishing", "Recovery"
        );
        primaryFocusComboBox.setItems(focusOptions);
        secondaryFocusComboBox.setItems(focusOptions);



        // Set up default date to today
        sessionDatePicker.setValue(LocalDate.now());

        // Setup players list from user.bin
        loadPlayersFromUserBin();

        // Load lineups from file
        loadSavedLineupsFromFile();

        // Setup filter for player search
        searchPlayerField.textProperty().addListener((observable, oldValue, newValue) -> {
            filterAvailablePlayers(newValue);
        });



        // Setup training schedule table columns
        typeColumn.setCellValueFactory(new PropertyValueFactory<>("sessionType"));
        dateColumn.setCellValueFactory(cellData -> {
            LocalDate date = cellData.getValue().getSessionDate();
            return new SimpleStringProperty(date.format(DateTimeFormatter.ofPattern("MM/dd/yyyy")));
        });
        timeColumn.setCellValueFactory(cellData -> {
            LocalTime time = cellData.getValue().getStartTime();
            return new SimpleStringProperty(time.format(DateTimeFormatter.ofPattern("HH:mm")));
        });
        durationColumn.setCellValueFactory(new PropertyValueFactory<>("duration"));
        focusColumn.setCellValueFactory(cellData -> {
            String primary = cellData.getValue().getPrimaryFocus();
            String secondary = cellData.getValue().getSecondaryFocus();
            return new SimpleStringProperty(primary + (secondary.isEmpty() ? "" : " / " + secondary));
        });
        intensityColumn.setCellValueFactory(new PropertyValueFactory<>("intensity"));
        groupColumn.setCellValueFactory(new PropertyValueFactory<>("playerGroup"));

        // Set table data
        trainingScheduleTable.setItems(trainingSessions);

        // Add sample training sessions
        loadTrainingSessionsFromFile();

        // If no sessions loaded, add sample sessions

    }

    private void loadPlayersFromUserBin() {
        // Clear current players
        availablePlayers.clear();

        // Load players from user.bin
        ArrayList<Player> players = HeadCoachDataManager.loadPlayersFromUserBin();

        if (players != null && !players.isEmpty()) {
            for (Player player : players) {
                availablePlayers.add(player.getName() + " (" + player.getPosition() + ")");
            }
        }

        availablePlayersListView.setItems(availablePlayers);
        selectedPlayersListView.setItems(selectedPlayers);
    }

    private void loadSavedLineupsFromFile() {
        // Clear current lineups
        savedLineups.clear();
        lineupPlayers.clear();

        // Load lineups from file
        List<String> lineups = HeadCoachDataManager.loadLineupsFromFile();

        if (lineups != null && !lineups.isEmpty()) {
            savedLineups.addAll(lineups);

            // Load player data for each lineup
            for (String lineup : lineups) {
                Map<String, Object> config = HeadCoachDataManager.loadLineupConfiguration(lineup);
                if (config != null && !config.isEmpty()) {
                    @SuppressWarnings("unchecked")
                    List<TeamLineUp_Controller.PlayerLineup> players = (List<TeamLineUp_Controller.PlayerLineup>) config.get("players");
                    if (players != null && !players.isEmpty()) {
                        lineupPlayers.put(lineup, players);
                    }
                }
            }
        }
    }

    private void showLineupSelectionDialog() {
        if (savedLineups.isEmpty()) {
            showAlert("No Lineups", "No saved lineups found. Create lineups in the Team Selection section first.", Alert.AlertType.WARNING);

            return;
        }

        ChoiceDialog<String> dialog = new ChoiceDialog<>(savedLineups.get(0), savedLineups);
        dialog.setTitle("Select Lineup");
        dialog.setHeaderText("Choose a saved lineup to load players from");
        dialog.setContentText("Lineup:");

        Optional<String> result = dialog.showAndWait();
        if (result.isPresent()) {
            String selectedLineup = result.get();
            loadPlayersFromLineup(selectedLineup);
        }
    }

    private void loadPlayersFromLineup(String lineupName) {
        selectedPlayers.clear();

        List<TeamLineUp_Controller.PlayerLineup> players = lineupPlayers.get(lineupName);
        if (players != null && !players.isEmpty()) {
            for (TeamLineUp_Controller.PlayerLineup player : players) {
                if (!player.getName().equals("Select player")) {
                    selectedPlayers.add(player.getName() + " (" + player.getPosition() + ")");
                }
            }

        } else {
            showAlert("Lineup Empty", "No players found in this lineup.", Alert.AlertType.WARNING);

        }
    }




    private void filterAvailablePlayers(String searchText) {
        if (searchText == null || searchText.isEmpty()) {
            availablePlayersListView.setItems(availablePlayers);
            return;
        }

        ObservableList<String> filteredPlayers = FXCollections.observableArrayList();
        for (String player : availablePlayers) {
            if (player.toLowerCase().contains(searchText.toLowerCase())) {
                filteredPlayers.add(player);
            }
        }
        availablePlayersListView.setItems(filteredPlayers);
    }

    private void preSelectPlayerGroup(String group) {
        selectedPlayers.clear();

        switch (group) {
            case "Full Squad":
                selectedPlayers.addAll(availablePlayers);
                break;
            case "Goalkeepers":
                for (String player : availablePlayers) {
                    if (player.contains("(GK)")) {
                        selectedPlayers.add(player);
                    }
                }
                break;
            case "Defenders":
                for (String player : availablePlayers) {
                    if (player.contains("(DF)") || player.contains("(RB)") ||
                            player.contains("(LB)") || player.contains("(CB)")) {
                        selectedPlayers.add(player);
                    }
                }
                break;
            case "Midfielders":
                for (String player : availablePlayers) {
                    if (player.contains("(MF)") || player.contains("(CM)") ||
                            player.contains("(CDM)") || player.contains("(CAM)") ||
                            player.contains("(RM)") || player.contains("(LM)")) {
                        selectedPlayers.add(player);
                    }
                }
                break;
            case "Forwards":
                for (String player : availablePlayers) {
                    if (player.contains("(FW)") || player.contains("(ST)") ||
                            player.contains("(RW)") || player.contains("(LW)")) {
                        selectedPlayers.add(player);
                    }
                }
                break;
            case "Starting XI":
                // Try to load the most recent lineup if available
                if (!savedLineups.isEmpty()) {
                    loadPlayersFromLineup(savedLineups.get(0));
                } else {
                    // Take first 11 players if no lineup available
                    for (int i = 0; i < Math.min(11, availablePlayers.size()); i++) {
                        selectedPlayers.add(availablePlayers.get(i));
                    }
                }
                break;
        }
    }

    private void loadTrainingSessionsFromFile() {
        // Load training sessions from file
        List<TrainingSession> sessions = HeadCoachDataManager.loadTrainingSessionsFromFile();
        if (sessions != null && !sessions.isEmpty()) {
            trainingSessions.addAll(sessions);
        }
    }

    private void saveTrainingSessionsToFile() {
        HeadCoachDataManager.saveTrainingSessionsToFile(new ArrayList<>(trainingSessions));
    }


    @FXML
    public void addSelectedPlayer() {
        String selectedPlayer = availablePlayersListView.getSelectionModel().getSelectedItem();
        if (selectedPlayer != null && !selectedPlayers.contains(selectedPlayer)) {
            selectedPlayers.add(selectedPlayer);
        }
    }

    @FXML
    public void addAllPlayers() {
        for (String player : availablePlayers) {
            if (!selectedPlayers.contains(player)) {
                selectedPlayers.add(player);
            }
        }
    }

    @FXML
    public void removeSelectedPlayer() {
        String selectedPlayer = selectedPlayersListView.getSelectionModel().getSelectedItem();
        if (selectedPlayer != null) {
            selectedPlayers.remove(selectedPlayer);
        }
    }

    @FXML
    public void removeAllPlayers() {
        selectedPlayers.clear();
    }

    @FXML
    public void scheduleTrainingSession() {
        // Validate required fields
        if (sessionTypeComboBox.getValue() == null) {
            showAlert("Missing Information", "Please select a session type.", Alert.AlertType.ERROR);
            return;
        }

        if (sessionDatePicker.getValue() == null) {
            showAlert("Missing Information", "Please select a date.", Alert.AlertType.ERROR);
            return;
        }

        if (startTimeComboBox.getValue() == null) {
            showAlert("Missing Information", "Please select a start time.", Alert.AlertType.ERROR);
            return;
        }

        if (durationComboBox.getValue() == null) {
            showAlert("Missing Information", "Please select a duration.", Alert.AlertType.ERROR);
            return;
        }

        if (intensityComboBox.getValue() == null) {
            showAlert("Missing Information", "Please select an intensity level.", Alert.AlertType.ERROR);
            return;
        }

        if (primaryFocusComboBox.getValue() == null) {
            showAlert("Missing Information", "Please select a primary focus.", Alert.AlertType.ERROR);
            return;
        }

        if (locationField.getText().trim().isEmpty()) {
            showAlert("Missing Information", "Please enter a location.", Alert.AlertType.ERROR);
            return;
        }

        if (selectedPlayers.isEmpty()) {
            showAlert("Missing Information", "Please select at least one player.", Alert.AlertType.ERROR);
            return;
        }

        // Parse time from the combo box
        String timeString = startTimeComboBox.getValue();
        String[] parts = timeString.split(":");
        LocalTime startTime = LocalTime.of(Integer.parseInt(parts[0]), Integer.parseInt(parts[1]));

        // Get secondary focus value safely
        String secondaryFocus = secondaryFocusComboBox.getValue();

        // Create new training session
        TrainingSession session = new TrainingSession(
                sessionTypeComboBox.getValue(),
                sessionDatePicker.getValue(),
                startTime,
                durationComboBox.getValue(),
                primaryFocusComboBox.getValue(),
                secondaryFocus != null ? secondaryFocus : "",
                intensityComboBox.getValue(),
                intensityComboBox.getValue(),

                locationField.getText().trim(),
                sessionObjectivesArea.getText().trim(),
                equipmentRequiredArea.getText().trim()
        );

        // Check for conflicting sessions
        boolean hasConflict = false;
        for (TrainingSession existingSession : trainingSessions) {
            if (existingSession.getSessionDate().equals(session.getSessionDate())) {
                // Simple check for time overlap (in a real app, you'd check the actual time ranges)
                if (existingSession.getStartTime().equals(session.getStartTime())) {
                    hasConflict = true;
                    break;
                }
            }
        }

        if (hasConflict) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Time Conflict");
            alert.setHeaderText("There is already a session scheduled at this time");
            alert.setContentText("Do you want to schedule this session anyway?");

            Optional<ButtonType> result = alert.showAndWait();
            if (result.isPresent() && result.get() != ButtonType.OK) {
                return;
            }
        }

        // Add to table
        trainingSessions.add(session);

        // Save to file
        saveTrainingSessionsToFile();

        // Clear form (optional - could leave filled for quick entry of similar sessions)
        clearForm();

        showAlert("Success", "Training session scheduled successfully.", Alert.AlertType.INFORMATION);
    }

    @FXML
    public void updateTrainingSession() {
        TrainingSession selectedSession = trainingScheduleTable.getSelectionModel().getSelectedItem();
        if (selectedSession == null) {
            showAlert("No Selection", "Please select a training session to update.", Alert.AlertType.ERROR);
            return;
        }

        // Fill form with selected session details
        sessionTypeComboBox.setValue(selectedSession.getSessionType());
        sessionDatePicker.setValue(selectedSession.getSessionDate());
        startTimeComboBox.setValue(selectedSession.getStartTime().format(DateTimeFormatter.ofPattern("HH:mm")));
        durationComboBox.setValue(selectedSession.getDuration());
        intensityComboBox.setValue(selectedSession.getIntensity());
        primaryFocusComboBox.setValue(selectedSession.getPrimaryFocus());
        secondaryFocusComboBox.setValue(selectedSession.getSecondaryFocus());

        locationField.setText(selectedSession.getLocation());
        sessionObjectivesArea.setText(selectedSession.getObjectives());
        equipmentRequiredArea.setText(selectedSession.getEquipmentRequired());

        // Remove the selected session
        trainingSessions.remove(selectedSession);

        // Save changes to file
        saveTrainingSessionsToFile();
    }

    @FXML
    public void deleteTrainingSession() {
        TrainingSession selectedSession = trainingScheduleTable.getSelectionModel().getSelectedItem();
        if (selectedSession == null) {
            showAlert("No Selection", "Please select a training session to delete.", Alert.AlertType.ERROR);
            return;
        }

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirm Delete");
        alert.setHeaderText("Delete Training Session");
        alert.setContentText("Are you sure you want to delete this training session?");

        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            trainingSessions.remove(selectedSession);
            saveTrainingSessionsToFile();
        }
    }

    @FXML
    public void generateWeeklySchedule() {
        StringBuilder schedule = new StringBuilder();
        schedule.append("Weekly Training Schedule\n");
        schedule.append("======================\n\n");

        // Group by day of week
        LocalDate startOfWeek = LocalDate.now();
        while (startOfWeek.getDayOfWeek() != DayOfWeek.MONDAY) {
            startOfWeek = startOfWeek.minusDays(1);
        }

        // Sort and format sessions by day
        for (int i = 0; i < 7; i++) {
            LocalDate currentDate = startOfWeek.plusDays(i);
            String dayName = currentDate.getDayOfWeek().toString();
            String dateStr = currentDate.format(DateTimeFormatter.ofPattern("MMM dd, yyyy"));

            schedule.append(dayName).append(" - ").append(dateStr).append("\n");
            schedule.append("----------------------------\n");

            boolean hasSession = false;
            for (TrainingSession session : trainingSessions) {
                if (session.getSessionDate().equals(currentDate)) {
                    hasSession = true;
                    schedule.append(session.getStartTime().format(DateTimeFormatter.ofPattern("HH:mm")))
                            .append(" - ")
                            .append(session.getSessionType())
                            .append(" (")
                            .append(session.getDuration())
                            .append(") - ")
                            .append(session.getPlayerGroup())
                            .append("\n");
                    schedule.append("    Focus: ")
                            .append(session.getPrimaryFocus());

                    if (!session.getSecondaryFocus().isEmpty()) {
                        schedule.append(" / ").append(session.getSecondaryFocus());
                    }

                    schedule.append(" - ").append(session.getIntensity()).append("\n");
                    schedule.append("    Location: ").append(session.getLocation()).append("\n\n");
                }
            }

            if (!hasSession) {
                schedule.append("No sessions scheduled\n\n");
            }
        }

        // Display in a text dialog
        TextArea textArea = new TextArea(schedule.toString());
        textArea.setEditable(false);
        textArea.setWrapText(true);
        textArea.setPrefWidth(600);
        textArea.setPrefHeight(400);

        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.setTitle("Weekly Training Schedule");
        dialog.getDialogPane().setContent(textArea);
        dialog.getDialogPane().getButtonTypes().add(ButtonType.CLOSE);
        dialog.showAndWait();
    }

    @FXML
    public void manageLineups(ActionEvent actionEvent) throws IOException {
        SceneSwitcher.switchTo("TeamLineUP.fxml", actionEvent);
    }


    private void clearForm() {
        sessionTypeComboBox.setValue(null);
        sessionDatePicker.setValue(LocalDate.now());
        startTimeComboBox.setValue(null);
        durationComboBox.setValue(null);
        intensityComboBox.setValue(null);
        locationField.clear();
        primaryFocusComboBox.setValue(null);
        secondaryFocusComboBox.setValue(null);
        sessionObjectivesArea.clear();
        equipmentRequiredArea.clear();

        selectedPlayers.clear();
    }

    private void showAlert(String title, String message, Alert.AlertType type) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    @FXML
    public void dashboard(ActionEvent actionEvent) throws IOException {
        SceneSwitcher.switchTo("DashboardHeadCoach.fxml", actionEvent);
    }



}