package cse213.cse213_sporting_club_operations.TanvirMahmud;

import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

public class PerformanceAssessnment_Controller {

    // UI Components for Player Stats Table
    @FXML private TableView<PlayerStats> playerStatsTable;
    @FXML private TableColumn<PlayerStats, String> playerNameColumn;
    @FXML private TableColumn<PlayerStats, String> positionColumn;
    @FXML private TableColumn<PlayerStats, Number> matchRatingColumn;
    @FXML private TableColumn<PlayerStats, Number> fatigueColumn;
    @FXML private TableColumn<PlayerStats, String> injuryStatusColumn;
    @FXML private TableColumn<PlayerStats, Number> trainingProgressColumn;

    // Filter components
    @FXML private ComboBox<String> positionFilterComboBox;
    @FXML private ComboBox<String> statusFilterComboBox;
    @FXML private Slider ratingSlider;

    // Summary components
    @FXML private Label nextMatchLabel;
    @FXML private Label fitnessOverviewLabel;
    @FXML private Label trainingStatusLabel;

    // Performance chart
    @FXML private BarChart<String, Number> performanceChart;
    @FXML private CategoryAxis xAxis;
    @FXML private NumberAxis yAxis;

    private ObservableList<PlayerStats> allPlayers = FXCollections.observableArrayList();
    private ObservableList<PlayerStats> filteredPlayers = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        // Initialize table columns
        playerNameColumn.setCellValueFactory(new PropertyValueFactory<>("playerName"));
        positionColumn.setCellValueFactory(new PropertyValueFactory<>("position"));
        matchRatingColumn.setCellValueFactory(new PropertyValueFactory<>("matchRating"));
        fatigueColumn.setCellValueFactory(new PropertyValueFactory<>("fatigue"));
        injuryStatusColumn.setCellValueFactory(new PropertyValueFactory<>("injuryStatus"));
        trainingProgressColumn.setCellValueFactory(new PropertyValueFactory<>("trainingProgress"));

        // Set up filter combo boxes
        positionFilterComboBox.setItems(FXCollections.observableArrayList(
                "All Positions", "GK", "DF", "MF", "FW"));
        positionFilterComboBox.setValue("All Positions");

        statusFilterComboBox.setItems(FXCollections.observableArrayList(
                "All Players", "Fit", "Injured", "Fatigued"));
        statusFilterComboBox.setValue("All Players");

        // Add filter change listeners
        positionFilterComboBox.getSelectionModel().selectedItemProperty().addListener(
                (obs, oldVal, newVal) -> applyFilters());

        statusFilterComboBox.getSelectionModel().selectedItemProperty().addListener(
                (obs, oldVal, newVal) -> applyFilters());

        ratingSlider.valueProperty().addListener(
                (obs, oldVal, newVal) -> applyFilters());

        // Load player data
        loadPlayerData();

        // Setup performance chart
        setupPerformanceChart();

        // Set summary information
        updateSummaryInformation();
    }

    private void loadPlayerData() {
        try {
            // Load players from lineups
            List<String> lineupNames = HeadCoachDataManager.loadLineupsFromFile();
            if (!lineupNames.isEmpty()) {
                String latestLineup = lineupNames.get(lineupNames.size() - 1);
                Map<String, Object> lineupConfig = HeadCoachDataManager.loadLineupConfiguration(latestLineup);

                if (lineupConfig != null) {
                    @SuppressWarnings("unchecked")
                    List<TeamLineUp_Controller.PlayerLineup> players = (List<TeamLineUp_Controller.PlayerLineup>) lineupConfig.get("players");
                    if (players != null) {
                        for (TeamLineUp_Controller.PlayerLineup player : players) {
                            // Create player stats with data from lineups and simulated stats
                            PlayerStats stats = new PlayerStats(
                                    player.getName(),
                                    player.getPosition(),
                                    generateRandomRating(),
                                    generateRandomFatigue(),
                                    generateInjuryStatus(),
                                    generateTrainingProgress()
                            );
                            allPlayers.add(stats);
                        }
                    }
                }
            }

            // Load training session data to enhance player information
            List<TrainingSession> sessions = HeadCoachDataManager.loadTrainingSessionsFromFile();
            if (!sessions.isEmpty()) {
                TrainingSession latestSession = sessions.get(sessions.size() - 1);

                // Update some players' training progress based on the latest session
                String focusArea = latestSession.getPrimaryFocus();
                for (PlayerStats player : allPlayers) {
                    // Players who match the focus area get an extra boost
                    if ((focusArea.equals("Defense") && player.getPosition().startsWith("DF")) ||
                            (focusArea.equals("Attack") && player.getPosition().startsWith("FW")) ||
                            (focusArea.equals("Midfield") && player.getPosition().startsWith("MF")) ||
                            (focusArea.equals("Goalkeeping") && player.getPosition().equals("GK"))) {
                        player.setTrainingProgress(player.getTrainingProgress() + 10);
                    }
                }
            }

            // Apply initial filters
            filteredPlayers.addAll(allPlayers);
            playerStatsTable.setItems(filteredPlayers);

        } catch (Exception e) {
            showAlert("Data Loading Error", "Failed to load player data: " + e.getMessage(),
                    Alert.AlertType.ERROR);
            e.printStackTrace();
        }
    }

    private void applyFilters() {
        filteredPlayers.clear();

        String positionFilter = positionFilterComboBox.getValue();
        String statusFilter = statusFilterComboBox.getValue();
        double ratingFilter = ratingSlider.getValue();

        for (PlayerStats player : allPlayers) {
            boolean positionMatch = positionFilter.equals("All Positions") ||
                    player.getPosition().startsWith(positionFilter.substring(0, Math.min(2, positionFilter.length())));

            boolean statusMatch = statusFilter.equals("All Players") ||
                    (statusFilter.equals("Injured") && player.getInjuryStatus().equals("Injured")) ||
                    (statusFilter.equals("Fit") && player.getInjuryStatus().equals("Fit")) ||
                    (statusFilter.equals("Fatigued") && player.getFatigue() > 70);

            boolean ratingMatch = player.getMatchRating() >= ratingFilter;

            if (positionMatch && statusMatch && ratingMatch) {
                filteredPlayers.add(player);
            }
        }

        // Update chart with filtered data
        updatePerformanceChart();
    }

    private void setupPerformanceChart() {
        xAxis.setLabel("Players");
        yAxis.setLabel("Rating");
        performanceChart.setTitle("Player Performance Overview");
        updatePerformanceChart();
    }

    private void updatePerformanceChart() {
        performanceChart.getData().clear();

        // Create match rating series
        XYChart.Series<String, Number> ratingSeries = new XYChart.Series<>();
        ratingSeries.setName("Match Rating");

        // Create training progress series
        XYChart.Series<String, Number> progressSeries = new XYChart.Series<>();
        progressSeries.setName("Training Progress");

        // Get top 5 players by rating for the chart
        filteredPlayers.stream()
                .sorted(Comparator.comparing(PlayerStats::getMatchRating).reversed())
                .limit(5)
                .forEach(player -> {
                    ratingSeries.getData().add(new XYChart.Data<>(player.getPlayerName(), player.getMatchRating()));
                    progressSeries.getData().add(new XYChart.Data<>(player.getPlayerName(), player.getTrainingProgress()));
                });

        performanceChart.getData().addAll(ratingSeries, progressSeries);
    }

    private void updateSummaryInformation() {
        // Count number of fit players
        long fitPlayers = allPlayers.stream()
                .filter(p -> p.getInjuryStatus().equals("Fit"))
                .count();

        // Calculate average fatigue
        double avgFatigue = allPlayers.stream()
                .mapToDouble(PlayerStats::getFatigue)
                .average()
                .orElse(0);

        // Set summary labels
        nextMatchLabel.setText("Next Match: League Match vs " + getRandomOpponent() +
                " (" + LocalDate.now().plusDays(3).format(DateTimeFormatter.ofPattern("dd/MM/yyyy")) + ")");

        fitnessOverviewLabel.setText(String.format("Squad Fitness: %d/%d players fit, Average Fatigue: %.1f%%",
                fitPlayers, allPlayers.size(), avgFatigue));

        trainingStatusLabel.setText("Latest Training Session: " + getLatestTrainingFocus());
    }

    @FXML
    public void dashboard(ActionEvent event) throws IOException {
        SceneSwitcher.switchTo("DashboardHeadCoach.fxml", event);
    }

    private String getRandomOpponent() {
        String[] opponents = {"Arsenal", "Chelsea", "Liverpool", "Manchester City", "Tottenham"};
        return opponents[(int)(Math.random() * opponents.length)];
    }

    private String getLatestTrainingFocus() {
        try {
            List<TrainingSession> sessions = HeadCoachDataManager.loadTrainingSessionsFromFile();
            if (!sessions.isEmpty()) {
                TrainingSession latest = sessions.get(sessions.size() - 1);
                return latest.getPrimaryFocus() + " (" + latest.getSessionDate() + ")";
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "No recent training sessions";
    }

    private double generateRandomRating() {
        return 5.0 + (Math.random() * 5.0); // Rating between 5.0 and 10.0
    }

    private int generateRandomFatigue() {
        return (int)(Math.random() * 100); // Fatigue between 0 and 100
    }

    private String generateInjuryStatus() {
        return Math.random() > 0.85 ? "Injured" : "Fit"; // 15% chance of injury
    }

    private int generateTrainingProgress() {
        return (int)(Math.random() * 100); // Progress between 0 and 100
    }

    private void showAlert(String title, String message, Alert.AlertType type) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    // Model class for player statistics
    public static class PlayerStats {
        private final SimpleStringProperty playerName;
        private final SimpleStringProperty position;
        private final SimpleDoubleProperty matchRating;
        private final SimpleIntegerProperty fatigue;
        private final SimpleStringProperty injuryStatus;
        private final SimpleIntegerProperty trainingProgress;

        public PlayerStats(String playerName, String position, double matchRating,
                           int fatigue, String injuryStatus, int trainingProgress) {
            this.playerName = new SimpleStringProperty(playerName);
            this.position = new SimpleStringProperty(position);
            this.matchRating = new SimpleDoubleProperty(matchRating);
            this.fatigue = new SimpleIntegerProperty(fatigue);
            this.injuryStatus = new SimpleStringProperty(injuryStatus);
            this.trainingProgress = new SimpleIntegerProperty(trainingProgress);
        }

        public String getPlayerName() { return playerName.get(); }
        public String getPosition() { return position.get(); }
        public double getMatchRating() { return matchRating.get(); }
        public int getFatigue() { return fatigue.get(); }
        public String getInjuryStatus() { return injuryStatus.get(); }
        public int getTrainingProgress() { return trainingProgress.get(); }

        public void setTrainingProgress(int progress) {
            this.trainingProgress.set(Math.min(100, progress));
        }
    }
}