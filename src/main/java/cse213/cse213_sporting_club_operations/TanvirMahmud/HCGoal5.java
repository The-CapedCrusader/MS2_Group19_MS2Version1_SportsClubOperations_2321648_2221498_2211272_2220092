package cse213.cse213_sporting_club_operations.TanvirMahmud;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.*;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class HCGoal5 {
    @FXML private ComboBox<String> positionFilterComboBox;
    @FXML private TableView<PlayerData> playersTableView;
    @FXML private TableColumn<PlayerData, String> playerNameColumn;
    @FXML private TableColumn<PlayerData, String> positionColumn;
    @FXML private TableColumn<PlayerData, String> currentRoleColumn;

    @FXML private Label selectedPlayerLabel;
    @FXML private BarChart<String, Number> playerCapabilityChart;
    @FXML private BarChart<String, Number> roleSuitabilityChart;
    @FXML private ComboBox<String> roleAssignmentComboBox;
    @FXML private Slider attackingFreedomSlider;

    @FXML private Pane fieldVisualizationPane;
    @FXML private ComboBox<String> formationSelectionComboBox;

    @FXML private Label currentRoleLabel;
    @FXML private ComboBox<String> defensiveDutyComboBox;
    @FXML private ComboBox<String> supportDutyComboBox;
    @FXML private ComboBox<String> attackingDutyComboBox;
    @FXML private TextArea playerInstructionsArea;

    @FXML private BarChart<String, Number> teamBalanceChart;

    @FXML private TableView<TeamRoleData> teamRolesTableView;
    @FXML private TableColumn<TeamRoleData, String> teamPlayerColumn;
    @FXML private TableColumn<TeamRoleData, String> teamPositionColumn;
    @FXML private TableColumn<TeamRoleData, String> teamRoleColumn;
    @FXML private TableColumn<TeamRoleData, String> teamDutyColumn;
    @FXML private TableColumn<TeamRoleData, Number> teamCompatibilityColumn;
    @FXML private TableColumn<TeamRoleData, String> teamNotesColumn;

    private ObservableList<PlayerData> allPlayers = FXCollections.observableArrayList();
    private ObservableList<TeamRoleData> teamRoles = FXCollections.observableArrayList();
    private PlayerData selectedPlayer;
    private Map<String, Map<String, Double>> playerCapabilities = new HashMap<>();

    @FXML
    public void initialize() {
        setupPositionFilter();
        setupPlayerTable();
        setupRoleDropdowns();
        loadFormations();
        setupTeamRolesTable();
        initializeCharts();
        populateSampleData();
    }

    private void setupPositionFilter() {
        positionFilterComboBox.getItems().addAll(
                "All Positions",
                "Goalkeepers",
                "Defenders",
                "Midfielders",
                "Forwards"
        );
        positionFilterComboBox.setValue("All Positions");
    }

    private void setupPlayerTable() {
        playerNameColumn.setCellValueFactory(cellData -> cellData.getValue().playerNameProperty());
        positionColumn.setCellValueFactory(cellData -> cellData.getValue().positionProperty());
        currentRoleColumn.setCellValueFactory(cellData -> cellData.getValue().currentRoleProperty());

        playersTableView.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> {
                    if (newValue != null) {
                        handlePlayerSelection(newValue);
                    }
                });
    }

    private void handlePlayerSelection(PlayerData player) {
        selectedPlayer = player;
        selectedPlayerLabel.setText(player.getPlayerName());
        updatePlayerCapabilityChart(player);
        updateRoleSuitabilityChart(player);
    }

    private void setupRoleDropdowns() {
        roleAssignmentComboBox.getItems().addAll(
                "Goalkeeper - Sweeper Keeper",
                "Goalkeeper - Traditional",
                "Center Back - Stopper",
                "Center Back - Cover",
                "Full Back - Attacking",
                "Full Back - Defensive",
                "Wing Back - Complete",
                "Defensive Midfielder - Anchor",
                "Midfielder - Box to Box",
                "Midfielder - Playmaker",
                "Attacking Midfielder - Shadow Striker",
                "Winger - Inside Forward",
                "Winger - Traditional",
                "Forward - Target Man",
                "Forward - Complete Forward",
                "Forward - Poacher"
        );

        defensiveDutyComboBox.getItems().addAll(
                "Contain", "Close Down", "Mark Tightly", "Tackle Hard", "Cover Space"
        );

        supportDutyComboBox.getItems().addAll(
                "Support Attacks", "Hold Position", "Recycle Possession", "Link Play"
        );

        attackingDutyComboBox.getItems().addAll(
                "Get Forward", "Make Runs", "Shoot on Sight", "Create Chances", "Hold Up Play"
        );
    }

    private void loadFormations() {
        formationSelectionComboBox.getItems().addAll(
                "4-4-2", "4-3-3", "4-2-3-1", "3-5-2", "5-3-2", "3-4-3"
        );
        formationSelectionComboBox.setValue("4-3-3");
    }

    private void setupTeamRolesTable() {
        teamPlayerColumn.setCellValueFactory(cellData -> cellData.getValue().playerNameProperty());
        teamPositionColumn.setCellValueFactory(cellData -> cellData.getValue().positionProperty());
        teamRoleColumn.setCellValueFactory(cellData -> cellData.getValue().roleProperty());
        teamDutyColumn.setCellValueFactory(cellData -> cellData.getValue().dutyProperty());
        teamCompatibilityColumn.setCellValueFactory(cellData -> cellData.getValue().compatibilityProperty());
        teamNotesColumn.setCellValueFactory(cellData -> cellData.getValue().notesProperty());
    }

    private void initializeCharts() {

        playerCapabilityChart.setTitle("Player Attributes");
        playerCapabilityChart.setAnimated(false);


        roleSuitabilityChart.setTitle("Role Suitability");
        roleSuitabilityChart.setAnimated(false);


        teamBalanceChart.setTitle("Team Tactical Balance");
        teamBalanceChart.setAnimated(false);

        updateTeamBalanceChart();
    }

    private void populateSampleData() {

        allPlayers.addAll(

        );


        for (PlayerData player : allPlayers) {
            Map<String, Double> capabilities = new HashMap<>();
            capabilities.put("Passing", Math.random() * 100);
            capabilities.put("Shooting", Math.random() * 100);
            capabilities.put("Dribbling", Math.random() * 100);
            capabilities.put("Defending", Math.random() * 100);
            capabilities.put("Physical", Math.random() * 100);
            playerCapabilities.put(player.getPlayerName(), capabilities);
        }

        // Initial team roles
        updateTeamRolesTable();

        // Apply initial filter (show all players)
        playersTableView.setItems(allPlayers);
    }

    private void updatePlayerCapabilityChart(PlayerData player) {
        playerCapabilityChart.getData().clear();

        Map<String, Double> capabilities = playerCapabilities.get(player.getPlayerName());
        if (capabilities != null) {
            XYChart.Series<String, Number> series = new XYChart.Series<>();
            series.setName("Attributes");

            for (Map.Entry<String, Double> entry : capabilities.entrySet()) {
                series.getData().add(new XYChart.Data<>(entry.getKey(), entry.getValue()));
            }

            playerCapabilityChart.getData().add(series);
        }
    }

    private void updateRoleSuitabilityChart(PlayerData player) {
        roleSuitabilityChart.getData().clear();

        XYChart.Series<String, Number> series = new XYChart.Series<>();
        series.setName("Suitability");

        // Sample role suitability data
        series.getData().add(new XYChart.Data<>("Target Man", Math.random() * 100));
        series.getData().add(new XYChart.Data<>("Playmaker", Math.random() * 100));
        series.getData().add(new XYChart.Data<>("Box to Box", Math.random() * 100));
        series.getData().add(new XYChart.Data<>("Poacher", Math.random() * 100));

        roleSuitabilityChart.getData().add(series);
    }

    private void updateTeamBalanceChart() {
        teamBalanceChart.getData().clear();

        XYChart.Series<String, Number> series = new XYChart.Series<>();
        series.setName("Balance");

        // Sample team balance data
        series.getData().add(new XYChart.Data<>("Defense", 75));
        series.getData().add(new XYChart.Data<>("Midfield", 60));
        series.getData().add(new XYChart.Data<>("Attack", 85));
        series.getData().add(new XYChart.Data<>("Creativity", 70));

        teamBalanceChart.getData().add(series);
    }

    private void updateTeamRolesTable() {
        teamRoles.clear();

        for (PlayerData player : allPlayers) {
            String duty = "";
            if (player.getPosition().equals("Forward")) {
                duty = "Get Forward";
            } else if (player.getPosition().equals("Midfielder")) {
                duty = "Support Attacks";
            } else if (player.getPosition().contains("Back")) {
                duty = "Cover Space";
            } else {
                duty = "Contain";
            }

            double compatibility = Math.random() * 100;
            String notes = compatibility > 80 ? "Excellent fit" :
                    compatibility > 60 ? "Good fit" :
                            compatibility > 40 ? "Average fit" : "Poor fit";

            teamRoles.add(new TeamRoleData(
                    player.getPlayerName(),
                    player.getPosition(),
                    player.getCurrentRole(),
                    duty,
                    compatibility,
                    notes
            ));
        }

        teamRolesTableView.setItems(teamRoles);
    }

    @FXML
    public void applyFilter() {
        String position = positionFilterComboBox.getValue();

        if (position.equals("All Positions")) {
            playersTableView.setItems(allPlayers);
        } else {
            ObservableList<PlayerData> filteredPlayers = FXCollections.observableArrayList();

            for (PlayerData player : allPlayers) {
                if (position.equals("Goalkeepers") && player.getPosition().contains("Goalkeeper")) {
                    filteredPlayers.add(player);
                } else if (position.equals("Defenders") &&
                        (player.getPosition().contains("Back") || player.getPosition().contains("Center"))) {
                    filteredPlayers.add(player);
                } else if (position.equals("Midfielders") && player.getPosition().contains("Midfielder")) {
                    filteredPlayers.add(player);
                } else if (position.equals("Forwards") && player.getPosition().contains("Forward")) {
                    filteredPlayers.add(player);
                }
            }

            playersTableView.setItems(filteredPlayers);
        }
    }

    @FXML
    public void assignRole() {
        String role = roleAssignmentComboBox.getValue();
        if (role != null && selectedPlayer != null) {
            selectedPlayer.setCurrentRole(role);
            currentRoleLabel.setText(role);

            // Update the TableView to reflect changes
            playersTableView.refresh();

            // Update team roles table
            updateTeamRolesTable();

            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Role Assigned");
            alert.setHeaderText(null);
            alert.setContentText("Role '" + role + "' has been assigned to " + selectedPlayer.getPlayerName());
            alert.showAndWait();
        } else {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Cannot Assign Role");
            alert.setHeaderText(null);
            alert.setContentText("Please select a player and a role to assign.");
            alert.showAndWait();
        }
    }

    @FXML
    public void updateFormation() {
        String formation = formationSelectionComboBox.getValue();
        // In a real implementation, this would draw the formation on the field

        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Formation Updated");
        alert.setHeaderText(null);
        alert.setContentText("Formation changed to " + formation);
        alert.showAndWait();
    }

    @FXML
    public void saveRoleInstructions() {
        if (currentRoleLabel.getText().equals("[No role selected]")) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("No Role Selected");
            alert.setHeaderText(null);
            alert.setContentText("Please select a role before saving instructions.");
            alert.showAndWait();
            return;
        }

        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Instructions Saved");
        alert.setHeaderText(null);
        alert.setContentText("Role instructions for '" + currentRoleLabel.getText() + "' have been saved.");
        alert.showAndWait();
    }

    @FXML
    public void optimizeRoles() {
        // In a real implementation, this would run an algorithm to optimize role assignments

        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Roles Optimized");
        alert.setHeaderText(null);
        alert.setContentText("Team roles have been optimized based on player capabilities.");
        alert.showAndWait();

        // Update the team roles table with "optimized" assignments
        updateTeamRolesTable();
    }

    @FXML
    public void saveTeamSetup() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Team Setup Saved");
        alert.setHeaderText(null);
        alert.setContentText("Current team tactical setup has been saved successfully.");
        alert.showAndWait();
    }

    @FXML
    public void generateRoleReport() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Report Generated");
        alert.setHeaderText(null);
        alert.setContentText("Player role assignment report has been generated successfully.");
        alert.showAndWait();
    }

    @FXML
    public void exportTeamSheet() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Team Sheet Exported");
        alert.setHeaderText(null);
        alert.setContentText("Team sheet with player roles and instructions has been exported successfully.");
        alert.showAndWait();
    }

    @FXML
    public void dashboard() {
        try {
            // Navigate back to the Head Coach dashboard
            FXMLLoader loader = new FXMLLoader(getClass().getResource("DashboardHeadCoach.fxml"));
            Parent root = loader.load();

            Stage stage = (Stage) positionFilterComboBox.getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Navigation Error");
            alert.setHeaderText(null);
            alert.setContentText("Could not return to dashboard. Error: " + e.getMessage());
            alert.showAndWait();
        }
    }

    // Data models for tables
    public static class PlayerData {
        private final javafx.beans.property.SimpleStringProperty playerName;
        private final javafx.beans.property.SimpleStringProperty position;
        private final javafx.beans.property.SimpleStringProperty currentRole;

        public PlayerData(String playerName, String position, String currentRole) {
            this.playerName = new javafx.beans.property.SimpleStringProperty(playerName);
            this.position = new javafx.beans.property.SimpleStringProperty(position);
            this.currentRole = new javafx.beans.property.SimpleStringProperty(currentRole);
        }

        public String getPlayerName() { return playerName.get(); }
        public void setPlayerName(String value) { playerName.set(value); }
        public javafx.beans.property.StringProperty playerNameProperty() { return playerName; }

        public String getPosition() { return position.get(); }
        public void setPosition(String value) { position.set(value); }
        public javafx.beans.property.StringProperty positionProperty() { return position; }

        public String getCurrentRole() { return currentRole.get(); }
        public void setCurrentRole(String value) { currentRole.set(value); }
        public javafx.beans.property.StringProperty currentRoleProperty() { return currentRole; }
    }

    public static class TeamRoleData {
        private final javafx.beans.property.SimpleStringProperty playerName;
        private final javafx.beans.property.SimpleStringProperty position;
        private final javafx.beans.property.SimpleStringProperty role;
        private final javafx.beans.property.SimpleStringProperty duty;
        private final javafx.beans.property.SimpleDoubleProperty compatibility;
        private final javafx.beans.property.SimpleStringProperty notes;

        public TeamRoleData(String playerName, String position, String role, String duty,
                            double compatibility, String notes) {
            this.playerName = new javafx.beans.property.SimpleStringProperty(playerName);
            this.position = new javafx.beans.property.SimpleStringProperty(position);
            this.role = new javafx.beans.property.SimpleStringProperty(role);
            this.duty = new javafx.beans.property.SimpleStringProperty(duty);
            this.compatibility = new javafx.beans.property.SimpleDoubleProperty(compatibility);
            this.notes = new javafx.beans.property.SimpleStringProperty(notes);
        }

        public String getPlayerName() { return playerName.get(); }
        public javafx.beans.property.StringProperty playerNameProperty() { return playerName; }

        public String getPosition() { return position.get(); }
        public javafx.beans.property.StringProperty positionProperty() { return position; }

        public String getRole() { return role.get(); }
        public javafx.beans.property.StringProperty roleProperty() { return role; }

        public String getDuty() { return duty.get(); }
        public javafx.beans.property.StringProperty dutyProperty() { return duty; }

        public double getCompatibility() { return compatibility.get(); }
        public javafx.beans.property.DoubleProperty compatibilityProperty() { return compatibility; }

        public String getNotes() { return notes.get(); }
        public javafx.beans.property.StringProperty notesProperty() { return notes; }
    }
}