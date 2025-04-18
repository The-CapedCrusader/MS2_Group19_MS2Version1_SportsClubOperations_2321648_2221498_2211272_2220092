package cse213.cse213_sporting_club_operations.TanvirMahmud;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.io.IOException;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

public class HCGoal2 {
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

    // Player group management
    @FXML private ComboBox<String> playerGroupComboBox;
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

    @FXML
    public void initialize() {
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

        // Initialize player groups
        playerGroupComboBox.setItems(FXCollections.observableArrayList(
                "Full Squad", "Starting XI", "Defenders", "Midfielders",
                "Forwards", "Goalkeepers", "Youth Players", "Injured Players"
        ));

        // Set up default date to today
        sessionDatePicker.setValue(LocalDate.now());

        // Setup players list
        populateSamplePlayers();

        // Setup filter for player search
        searchPlayerField.textProperty().addListener((observable, oldValue, newValue) -> {
            filterAvailablePlayers(newValue);
        });

        // Setup player group selection action
        playerGroupComboBox.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> {
                    if (newValue != null) {
                        preSelectPlayerGroup(newValue);
                    }
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
        addSampleTrainingSessions();
    }

    private void populateSamplePlayers() {
        String[] players = {
                "John Smith (GK)", "David Johnson (GK)",
                "Michael Brown (DF)", "Robert Davis (DF)", "James Wilson (DF)", "James Harris (DF)",
                "Daniel Martinez (MF)", "Paul Robinson (MF)", "Mark Thompson (MF)", "Steven Garcia (MF)",
                "Andrew Clark (FW)", "Richard Lewis (FW)", "Thomas Lee (FW)", "Christopher Walker (FW)"
        };

        availablePlayers.addAll(players);
        availablePlayersListView.setItems(availablePlayers);
        selectedPlayersListView.setItems(selectedPlayers);
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
                    if (player.contains("(DF)")) {
                        selectedPlayers.add(player);
                    }
                }
                break;
            case "Midfielders":
                for (String player : availablePlayers) {
                    if (player.contains("(MF)")) {
                        selectedPlayers.add(player);
                    }
                }
                break;
            case "Forwards":
                for (String player : availablePlayers) {
                    if (player.contains("(FW)")) {
                        selectedPlayers.add(player);
                    }
                }
                break;
            // Other groups would be implemented similarly
        }
    }

    private void addSampleTrainingSessions() {
        // Add some sample training sessions for the current week
        LocalDate today = LocalDate.now();

        // Find the start of the week (Monday)
        LocalDate startOfWeek = today;
        while (startOfWeek.getDayOfWeek() != DayOfWeek.MONDAY) {
            startOfWeek = startOfWeek.minusDays(1);
        }

        trainingSessions.add(new TrainingSession(
                "Team Training",
                startOfWeek,
                LocalTime.of(10, 0),
                "90 minutes",
                "Tactical Organization",
                "Transition Play",
                "Medium",
                "Full Squad",
                "Training Ground Field 1",
                "Implement new defensive structure against counter-attacks",
                "Training mannequins, cones, bibs, tactical board"
        ));

        trainingSessions.add(new TrainingSession(
                "Small Group Training",
                startOfWeek.plusDays(1),
                LocalTime.of(10, 0),
                "60 minutes",
                "Technical Skills",
                "Finishing",
                "High",
                "Forwards",
                "Training Ground Field 2",
                "Improve shooting accuracy from crosses",
                "Goals, balls, cones, crossing machine"
        ));

        trainingSessions.add(new TrainingSession(
                "Recovery Session",
                startOfWeek.plusDays(2),
                LocalTime.of(11, 0),
                "45 minutes",
                "Recovery",
                "",
                "Light",
                "Starting XI",
                "Indoor Facility",
                "Active recovery after match",
                "Foam rollers, resistance bands, yoga mats"
        ));

        trainingSessions.add(new TrainingSession(
                "Team Training",
                startOfWeek.plusDays(3),
                LocalTime.of(10, 0),
                "120 minutes",
                "Set Pieces",
                "Defensive Structure",
                "Medium",
                "Full Squad",
                "Training Ground Field 1",
                "Work on defensive and offensive set pieces",
                "Goals, balls, cones, free kick wall"
        ));

        trainingSessions.add(new TrainingSession(
                "Match Simulation",
                startOfWeek.plusDays(4),
                LocalTime.of(10, 0),
                "90 minutes",
                "Tactical Organization",
                "Match Intensity",
                "High",
                "Full Squad",
                "Main Stadium",
                "Full match simulation with tactical focus",
                "Match kit, balls, referee equipment"
        ));
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
                playerGroupComboBox.getValue() != null ? playerGroupComboBox.getValue() : "Custom Group",
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
        playerGroupComboBox.setValue(selectedSession.getPlayerGroup());
        locationField.setText(selectedSession.getLocation());
        sessionObjectivesArea.setText(selectedSession.getObjectives());
        equipmentRequiredArea.setText(selectedSession.getEquipmentRequired());

        // Remove the selected session
        trainingSessions.remove(selectedSession);
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
        playerGroupComboBox.setValue(null);
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
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("DashboardHeadCoach.fxml"));
            Parent root = loader.load();

            Stage stage = (Stage) sessionDatePicker.getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            showAlert("Navigation Error", "Could not navigate to Dashboard. Error: " + e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    // Model class for training sessions
    public static class TrainingSession {
        private final String sessionType;
        private final LocalDate sessionDate;
        private final LocalTime startTime;
        private final String duration;
        private final String primaryFocus;
        private final String secondaryFocus;
        private final String intensity;
        private final String playerGroup;
        private final String location;
        private final String objectives;
        private final String equipmentRequired;

        public TrainingSession(String sessionType, LocalDate sessionDate, LocalTime startTime,
                               String duration, String primaryFocus, String secondaryFocus,
                               String intensity, String playerGroup, String location,
                               String objectives, String equipmentRequired) {
            this.sessionType = sessionType;
            this.sessionDate = sessionDate;
            this.startTime = startTime;
            this.duration = duration;
            this.primaryFocus = primaryFocus;
            this.secondaryFocus = secondaryFocus;
            this.intensity = intensity;
            this.playerGroup = playerGroup;
            this.location = location;
            this.objectives = objectives;
            this.equipmentRequired = equipmentRequired;
        }

        public String getSessionType() { return sessionType; }
        public LocalDate getSessionDate() { return sessionDate; }
        public LocalTime getStartTime() { return startTime; }
        public String getDuration() { return duration; }
        public String getPrimaryFocus() { return primaryFocus; }
        public String getSecondaryFocus() { return secondaryFocus; }
        public String getIntensity() { return intensity; }
        public String getPlayerGroup() { return playerGroup; }
        public String getLocation() { return location; }
        public String getObjectives() { return objectives; }
        public String getEquipmentRequired() { return equipmentRequired; }
    }
}