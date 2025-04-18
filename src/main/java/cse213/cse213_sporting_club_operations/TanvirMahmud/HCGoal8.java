package cse213.cse213_sporting_club_operations.TanvirMahmud;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

import java.io.IOException;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

public class HCGoal8 {
    @FXML private TableView<YouthPlayer> youthPlayersTable;
    @FXML private TableColumn<YouthPlayer, String> nameColumn;
    @FXML private TableColumn<YouthPlayer, String> ageColumn;
    @FXML private TableColumn<YouthPlayer, String> positionColumn;
    @FXML private TableColumn<YouthPlayer, String> potentialColumn;
    @FXML private TableColumn<YouthPlayer, String> performanceColumn;
    @FXML private TableColumn<YouthPlayer, String> statusColumn;

    @FXML private TextField searchField;
    @FXML private ComboBox<String> positionFilterComboBox;
    @FXML private ComboBox<String> ageGroupFilterComboBox;

    @FXML private Label playerNameLabel;
    @FXML private Label ageLabel;
    @FXML private Label positionLabel;
    @FXML private Label potentialLabel;
    @FXML private ImageView playerImageView;

    @FXML private BarChart<String, Number> statsBarChart;
    @FXML private CategoryAxis xAxis;
    @FXML private NumberAxis yAxis;

    @FXML private TextArea strengthsArea;
    @FXML private TextArea weaknessesArea;
    @FXML private TextArea coachNotesArea;

    @FXML private DatePicker evaluationDatePicker;
    @FXML private ComboBox<String> promotionStatusComboBox;
    @FXML private TextArea promotionReasonArea;

    private ObservableList<YouthPlayer> allYouthPlayers = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        setupTable();
        setupFilters();
        loadSampleData();
        setupListeners();
        setupPromotionOptions();

        evaluationDatePicker.setValue(LocalDate.now());
    }

    private void setupTable() {
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        ageColumn.setCellValueFactory(new PropertyValueFactory<>("age"));
        positionColumn.setCellValueFactory(new PropertyValueFactory<>("position"));
        potentialColumn.setCellValueFactory(new PropertyValueFactory<>("potential"));
        performanceColumn.setCellValueFactory(new PropertyValueFactory<>("performance"));
        statusColumn.setCellValueFactory(new PropertyValueFactory<>("status"));

        youthPlayersTable.setItems(allYouthPlayers);
    }

    private void setupFilters() {
        positionFilterComboBox.getItems().addAll(
                "All Positions", "Goalkeeper", "Defender", "Midfielder", "Forward"
        );
        positionFilterComboBox.setValue("All Positions");

        ageGroupFilterComboBox.getItems().addAll(
                "All Ages", "Under 17", "Under 19", "Under 21", "Under 23"
        );
        ageGroupFilterComboBox.setValue("All Ages");

        positionFilterComboBox.valueProperty().addListener((obs, oldVal, newVal) -> applyFilters());
        ageGroupFilterComboBox.valueProperty().addListener((obs, oldVal, newVal) -> applyFilters());
        searchField.textProperty().addListener((obs, oldVal, newVal) -> applyFilters());
    }

    private void setupPromotionOptions() {
        promotionStatusComboBox.getItems().addAll(
                "Ready for Promotion", "Potential Future Promotion",
                "Loan Recommended", "Needs More Development", "Not Suitable"
        );
    }

    private void setupListeners() {
        youthPlayersTable.getSelectionModel().selectedItemProperty().addListener(
                (obs, oldSelection, newSelection) -> {
                    if (newSelection != null) {
                        displayPlayerDetails(newSelection);
                    }
                }
        );
    }

    private void loadSampleData() {
        allYouthPlayers.addAll(
                new YouthPlayer("James Wilson", "18", "Forward", "4.5/5", "4.2/5", "Under Review"),
                new YouthPlayer("Adam Smith", "17", "Midfielder", "4.0/5", "3.8/5", "Under Review"),
                new YouthPlayer("David Brown", "19", "Defender", "3.8/5", "4.0/5", "Under Review"),
                new YouthPlayer("Michael Johnson", "20", "Goalkeeper", "4.2/5", "3.9/5", "Under Review"),
                new YouthPlayer("Thomas Williams", "16", "Forward", "4.7/5", "3.5/5", "Under Review"),
                new YouthPlayer("Robert Davis", "19", "Midfielder", "3.6/5", "3.7/5", "Under Review"),
                new YouthPlayer("William Taylor", "17", "Defender", "3.9/5", "3.8/5", "Under Review"),
                new YouthPlayer("Richard Wilson", "18", "Goalkeeper", "3.5/5", "3.6/5", "Under Review"),
                new YouthPlayer("Charles Martin", "20", "Forward", "4.1/5", "4.3/5", "Under Review"),
                new YouthPlayer("Joseph White", "16", "Midfielder", "4.3/5", "3.4/5", "Under Review")
        );
    }

    private void displayPlayerDetails(YouthPlayer player) {
        playerNameLabel.setText(player.getName());
        ageLabel.setText("Age: " + player.getAge());
        positionLabel.setText("Position: " + player.getPosition());
        potentialLabel.setText("Potential: " + player.getPotential());

        // Set player image (placeholder)
        playerImageView.setImage(new Image(getClass().getResourceAsStream("/images/player_placeholder.png")));

        // Load player statistics into bar chart
        XYChart.Series<String, Number> series = new XYChart.Series<>();
        series.setName("Skills Rating");

        // Sample stats based on position
        if (player.getPosition().equals("Forward")) {
            series.getData().add(new XYChart.Data<>("Pace", 75 + Math.random() * 20));
            series.getData().add(new XYChart.Data<>("Shooting", 70 + Math.random() * 25));
            series.getData().add(new XYChart.Data<>("Dribbling", 65 + Math.random() * 30));
            series.getData().add(new XYChart.Data<>("Passing", 60 + Math.random() * 20));
            series.getData().add(new XYChart.Data<>("Physical", 60 + Math.random() * 20));
        } else if (player.getPosition().equals("Midfielder")) {
            series.getData().add(new XYChart.Data<>("Pace", 60 + Math.random() * 20));
            series.getData().add(new XYChart.Data<>("Passing", 70 + Math.random() * 25));
            series.getData().add(new XYChart.Data<>("Vision", 65 + Math.random() * 30));
            series.getData().add(new XYChart.Data<>("Shooting", 55 + Math.random() * 25));
            series.getData().add(new XYChart.Data<>("Stamina", 65 + Math.random() * 25));
        } else if (player.getPosition().equals("Defender")) {
            series.getData().add(new XYChart.Data<>("Tackling", 70 + Math.random() * 25));
            series.getData().add(new XYChart.Data<>("Strength", 65 + Math.random() * 30));
            series.getData().add(new XYChart.Data<>("Positioning", 65 + Math.random() * 25));
            series.getData().add(new XYChart.Data<>("Heading", 70 + Math.random() * 20));
            series.getData().add(new XYChart.Data<>("Pace", 55 + Math.random() * 25));
        } else {
            series.getData().add(new XYChart.Data<>("Reflexes", 70 + Math.random() * 25));
            series.getData().add(new XYChart.Data<>("Positioning", 65 + Math.random() * 30));
            series.getData().add(new XYChart.Data<>("Handling", 65 + Math.random() * 25));
            series.getData().add(new XYChart.Data<>("Kicking", 60 + Math.random() * 20));
            series.getData().add(new XYChart.Data<>("Diving", 70 + Math.random() * 25));
        }

        statsBarChart.getData().clear();
        statsBarChart.getData().add(series);

        // Load sample player strengths and weaknesses
        loadPlayerAssessment(player);
    }

    private void loadPlayerAssessment(YouthPlayer player) {
        List<String> forwardStrengths = Arrays.asList(
                "Excellent finishing ability", "Good movement off the ball",
                "High work rate", "Strong aerial presence", "Quick and agile"
        );

        List<String> forwardWeaknesses = Arrays.asList(
                "Needs to improve defensive contribution", "Decision making under pressure",
                "Lacks physical strength", "Needs to improve weaker foot", "Consistency issues"
        );

        List<String> midfielderStrengths = Arrays.asList(
                "Excellent passing range", "Good vision and creativity",
                "High work rate", "Tactical awareness", "Good ball control"
        );

        List<String> midfielderWeaknesses = Arrays.asList(
                "Needs to improve defensive positioning", "Physicality in duels",
                "Needs more pace", "Decision making in final third", "Shooting accuracy"
        );

        List<String> defenderStrengths = Arrays.asList(
                "Strong in aerial duels", "Good tackling technique",
                "Positional awareness", "Leadership qualities", "Good concentration"
        );

        List<String> defenderWeaknesses = Arrays.asList(
                "Needs to improve with the ball", "Pace against fast attackers",
                "Tendency to commit fouls", "Building from the back", "Focus in late stages"
        );

        List<String> goalkeeperStrengths = Arrays.asList(
                "Good shot-stopping ability", "Command of the area",
                "Communication with defense", "Good reflexes", "One-on-one situations"
        );

        List<String> goalkeeperWeaknesses = Arrays.asList(
                "Distribution needs improvement", "Decision making on crosses",
                "Positioning for long shots", "Dealing with back passes", "Concentration"
        );

        StringBuilder strengths = new StringBuilder();
        StringBuilder weaknesses = new StringBuilder();

        if (player.getPosition().equals("Forward")) {
            for (String strength : forwardStrengths) {
                if (Math.random() > 0.3) {
                    strengths.append("• ").append(strength).append("\n");
                }
            }
            for (String weakness : forwardWeaknesses) {
                if (Math.random() > 0.3) {
                    weaknesses.append("• ").append(weakness).append("\n");
                }
            }
        } else if (player.getPosition().equals("Midfielder")) {
            for (String strength : midfielderStrengths) {
                if (Math.random() > 0.3) {
                    strengths.append("• ").append(strength).append("\n");
                }
            }
            for (String weakness : midfielderWeaknesses) {
                if (Math.random() > 0.3) {
                    weaknesses.append("• ").append(weakness).append("\n");
                }
            }
        } else if (player.getPosition().equals("Defender")) {
            for (String strength : defenderStrengths) {
                if (Math.random() > 0.3) {
                    strengths.append("• ").append(strength).append("\n");
                }
            }
            for (String weakness : defenderWeaknesses) {
                if (Math.random() > 0.3) {
                    weaknesses.append("• ").append(weakness).append("\n");
                }
            }
        } else {
            for (String strength : goalkeeperStrengths) {
                if (Math.random() > 0.3) {
                    strengths.append("• ").append(strength).append("\n");
                }
            }
            for (String weakness : goalkeeperWeaknesses) {
                if (Math.random() > 0.3) {
                    weaknesses.append("• ").append(weakness).append("\n");
                }
            }
        }

        strengthsArea.setText(strengths.toString());
        weaknessesArea.setText(weaknesses.toString());

        // Sample coach notes
        coachNotesArea.setText("Recent monitoring shows good development in key areas.\n"
                + "Participated in " + (3 + (int)(Math.random() * 5)) + " first team training sessions.\n"
                + "Has shown maturity and readiness to step up when called upon.");
    }

    private void applyFilters() {
        String searchText = searchField.getText().toLowerCase();
        String positionFilter = positionFilterComboBox.getValue();
        String ageGroupFilter = ageGroupFilterComboBox.getValue();

        ObservableList<YouthPlayer> filteredList = FXCollections.observableArrayList();

        for (YouthPlayer player : allYouthPlayers) {
            boolean matchesSearch = player.getName().toLowerCase().contains(searchText);

            boolean matchesPosition = positionFilter.equals("All Positions") ||
                    player.getPosition().equals(positionFilter);

            boolean matchesAge = true;
            if (ageGroupFilter.equals("Under 17")) {
                matchesAge = Integer.parseInt(player.getAge()) < 17;
            } else if (ageGroupFilter.equals("Under 19")) {
                matchesAge = Integer.parseInt(player.getAge()) < 19;
            } else if (ageGroupFilter.equals("Under 21")) {
                matchesAge = Integer.parseInt(player.getAge()) < 21;
            } else if (ageGroupFilter.equals("Under 23")) {
                matchesAge = Integer.parseInt(player.getAge()) < 23;
            }

            if (matchesSearch && matchesPosition && matchesAge) {
                filteredList.add(player);
            }
        }

        youthPlayersTable.setItems(filteredList);
    }

    @FXML
    public void resetFilters() {
        searchField.clear();
        positionFilterComboBox.setValue("All Positions");
        ageGroupFilterComboBox.setValue("All Ages");
        youthPlayersTable.setItems(allYouthPlayers);
    }

    @FXML
    public void saveEvaluation() {
        YouthPlayer selectedPlayer = youthPlayersTable.getSelectionModel().getSelectedItem();
        if (selectedPlayer == null) {
            showAlert(Alert.AlertType.WARNING, "No Selection",
                    "Please select a player to evaluate.");
            return;
        }

        if (promotionStatusComboBox.getValue() == null) {
            showAlert(Alert.AlertType.WARNING, "Incomplete Evaluation",
                    "Please select a promotion status for the player.");
            return;
        }

        selectedPlayer.setStatus(promotionStatusComboBox.getValue());
        youthPlayersTable.refresh();

        showAlert(Alert.AlertType.INFORMATION, "Evaluation Saved",
                "Player evaluation has been saved successfully.");
    }

    @FXML
    public void promotePlayer() {
        YouthPlayer selectedPlayer = youthPlayersTable.getSelectionModel().getSelectedItem();
        if (selectedPlayer == null) {
            showAlert(Alert.AlertType.WARNING, "No Selection",
                    "Please select a player to promote.");
            return;
        }

        if (!"Ready for Promotion".equals(selectedPlayer.getStatus())) {
            showAlert(Alert.AlertType.WARNING, "Not Ready",
                    "This player is not ready for promotion. Please update their status first.");
            return;
        }

        if (promotionReasonArea.getText().trim().isEmpty()) {
            showAlert(Alert.AlertType.WARNING, "Reason Required",
                    "Please provide a reason for promotion.");
            return;
        }

        Alert confirmation = new Alert(Alert.AlertType.CONFIRMATION);
        confirmation.setTitle("Confirm Promotion");
        confirmation.setHeaderText("Promote " + selectedPlayer.getName() + " to First Team");
        confirmation.setContentText("Are you sure you want to promote this player to the first team?");

        if (confirmation.showAndWait().get() == ButtonType.OK) {
            selectedPlayer.setStatus("Promoted to First Team");
            youthPlayersTable.refresh();

            showAlert(Alert.AlertType.INFORMATION, "Player Promoted",
                    selectedPlayer.getName() + " has been promoted to the first team.");
        }
    }

    @FXML
    public void generateReport() {
        // Count promotions by position
        long promotedForwards = allYouthPlayers.stream()
                .filter(p -> p.getPosition().equals("Forward") &&
                        p.getStatus().equals("Promoted to First Team"))
                .count();

        long promotedMidfielders = allYouthPlayers.stream()
                .filter(p -> p.getPosition().equals("Midfielder") &&
                        p.getStatus().equals("Promoted to First Team"))
                .count();

        long promotedDefenders = allYouthPlayers.stream()
                .filter(p -> p.getPosition().equals("Defender") &&
                        p.getStatus().equals("Promoted to First Team"))
                .count();

        long promotedGoalkeepers = allYouthPlayers.stream()
                .filter(p -> p.getPosition().equals("Goalkeeper") &&
                        p.getStatus().equals("Promoted to First Team"))
                .count();

        StringBuilder report = new StringBuilder();
        report.append("Youth Academy Promotion Report\n")
                .append("Date: ").append(LocalDate.now()).append("\n\n")
                .append("Total Players Evaluated: ").append(allYouthPlayers.size()).append("\n")
                .append("Players Promoted: ").append(promotedForwards + promotedMidfielders +
                        promotedDefenders + promotedGoalkeepers).append("\n\n")
                .append("Forwards Promoted: ").append(promotedForwards).append("\n")
                .append("Midfielders Promoted: ").append(promotedMidfielders).append("\n")
                .append("Defenders Promoted: ").append(promotedDefenders).append("\n")
                .append("Goalkeepers Promoted: ").append(promotedGoalkeepers).append("\n\n")
                .append("Players Ready for Promotion: ").append(allYouthPlayers.stream()
                        .filter(p -> p.getStatus().equals("Ready for Promotion")).count()).append("\n")
                .append("Players Needing Development: ").append(allYouthPlayers.stream()
                        .filter(p -> p.getStatus().equals("Needs More Development")).count()).append("\n")
                .append("Players Recommended for Loan: ").append(allYouthPlayers.stream()
                        .filter(p -> p.getStatus().equals("Loan Recommended")).count());

        TextArea reportArea = new TextArea(report.toString());
        reportArea.setEditable(false);
        reportArea.setPrefWidth(400);
        reportArea.setPrefHeight(400);

        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.setTitle("Youth Academy Promotion Report");
        dialog.getDialogPane().setContent(reportArea);
        dialog.getDialogPane().getButtonTypes().add(ButtonType.OK);
        dialog.showAndWait();
    }

    @FXML
    public void dashboard() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("DashboardHeadCoach.fxml"));
            Parent root = loader.load();

            Stage stage = (Stage) searchField.getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Navigation Error",
                    "Could not return to dashboard. Error: " + e.getMessage());
        }
    }

    private void showAlert(Alert.AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public static class YouthPlayer {
        private final SimpleStringProperty name;
        private final SimpleStringProperty age;
        private final SimpleStringProperty position;
        private final SimpleStringProperty potential;
        private final SimpleStringProperty performance;
        private final SimpleStringProperty status;

        public YouthPlayer(String name, String age, String position, String potential,
                           String performance, String status) {
            this.name = new SimpleStringProperty(name);
            this.age = new SimpleStringProperty(age);
            this.position = new SimpleStringProperty(position);
            this.potential = new SimpleStringProperty(potential);
            this.performance = new SimpleStringProperty(performance);
            this.status = new SimpleStringProperty(status);
        }

        public String getName() {
            return name.get();
        }

        public String getAge() {
            return age.get();
        }

        public String getPosition() {
            return position.get();
        }

        public String getPotential() {
            return potential.get();
        }

        public String getPerformance() {
            return performance.get();
        }

        public String getStatus() {
            return status.get();
        }

        public void setStatus(String status) {
            this.status.set(status);
        }

        public SimpleStringProperty nameProperty() {
            return name;
        }

        public SimpleStringProperty ageProperty() {
            return age;
        }

        public SimpleStringProperty positionProperty() {
            return position;
        }

        public SimpleStringProperty potentialProperty() {
            return potential;
        }

        public SimpleStringProperty performanceProperty() {
            return performance;
        }

        public SimpleStringProperty statusProperty() {
            return status;
        }
    }
}