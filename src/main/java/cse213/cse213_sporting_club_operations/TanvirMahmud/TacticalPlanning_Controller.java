package cse213.cse213_sporting_club_operations.TanvirMahmud;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.*;

public class TacticalPlanning_Controller {
    // Match details
    @FXML private ComboBox<String> opponentComboBox;
    @FXML private DatePicker matchDatePicker;
    @FXML private ComboBox<String> formationComboBox;

    // Tactics
    @FXML private ComboBox<String> pressingStyleComboBox;
    @FXML private ComboBox<String> defensiveLineComboBox;
    @FXML private TextArea tacticalNotesArea;

    // Player assignments from lineup
    @FXML private TableView<PlayerAssignment> playerAssignmentsTable;
    @FXML private TableColumn<PlayerAssignment, String> playerNameColumn;
    @FXML private TableColumn<PlayerAssignment, String> positionColumn;
    @FXML private TableColumn<PlayerAssignment, String> roleColumn;

    // Strategy list
    @FXML private ListView<String> savedStrategiesListView;
    @FXML private Label lastTrainingSessionLabel;

    private ObservableList<PlayerAssignment> playerAssignments = FXCollections.observableArrayList();
    private Map<String, TacticalStrategy> savedStrategies = new HashMap<>();

    @FXML
    public void initialize() {
        // Initialize date picker
        matchDatePicker.setValue(LocalDate.now());

        // Initialize comboboxes
        opponentComboBox.setItems(FXCollections.observableArrayList(
                "Arsenal FC", "Chelsea FC", "Liverpool FC", "Manchester City",
                "Manchester United", "Tottenham Hotspur", "Everton FC"));

        formationComboBox.setItems(FXCollections.observableArrayList(
                "4-4-2", "4-3-3", "4-2-3-1", "3-5-2", "3-4-3"));

        pressingStyleComboBox.setItems(FXCollections.observableArrayList(
                "High Press", "Medium Press", "Low Block", "Counter-Press"));

        defensiveLineComboBox.setItems(FXCollections.observableArrayList(
                "High Line", "Medium Block", "Low Block", "Offside Trap"));

        // Setup player assignments table
        playerNameColumn.setCellValueFactory(new PropertyValueFactory<>("playerName"));
        positionColumn.setCellValueFactory(new PropertyValueFactory<>("position"));
        roleColumn.setCellValueFactory(new PropertyValueFactory<>("role"));
        playerAssignmentsTable.setItems(playerAssignments);

        // Load saved lineups
        loadLineups();

        // Load last training session
        loadLastTrainingSession();

        // Add listeners for dynamic updates
        formationComboBox.getSelectionModel().selectedItemProperty().addListener(
                (obs, oldVal, newVal) -> updateFormationDisplay(newVal));

        savedStrategiesListView.getSelectionModel().selectedItemProperty().addListener(
                (obs, oldVal, newVal) -> loadSelectedStrategy(newVal));
    }

    private void loadLineups() {
        List<String> lineupNames = HeadCoachDataManager.loadLineupsFromFile();
        if (lineupNames.isEmpty()) {
            showAlert("No Lineups Found", "No saved lineups found. Create a lineup first.",
                    Alert.AlertType.INFORMATION);
            return;
        }

        // Load first lineup for demo
        String firstLineup = lineupNames.get(0);
        Map<String, Object> lineupConfig = HeadCoachDataManager.loadLineupConfiguration(firstLineup);

        @SuppressWarnings("unchecked")
        List<TeamLineUp_Controller.PlayerLineup> players = (List<TeamLineUp_Controller.PlayerLineup>) lineupConfig.get("players");
        if (players != null) {
            playerAssignments.clear();
            for (TeamLineUp_Controller.PlayerLineup player : players) {
                playerAssignments.add(new PlayerAssignment(
                        player.getName(),
                        player.getPosition(),
                        "Standard"  // Default role
                ));
            }
        }

        // Add lineup names to strategy list for demo
        savedStrategiesListView.setItems(FXCollections.observableArrayList(lineupNames));
    }

    private void loadLastTrainingSession() {
        List<TrainingSession> sessions = HeadCoachDataManager.loadTrainingSessionsFromFile();
        if (!sessions.isEmpty()) {
            // Find the latest session
            TrainingSession latest = sessions.get(sessions.size() - 1);
            lastTrainingSessionLabel.setText(String.format("Last Training: %s - %s focus on %s",
                    latest.getSessionDate(),
                    latest.getPrimaryFocus(),
                    latest.getSessionType()));
        } else {
            lastTrainingSessionLabel.setText("No recent training sessions found");
        }
    }

    private void updateFormationDisplay(String formation) {
        if (formation == null) return;

        // In a real app, this would update a formation visualization
        System.out.println("Updating formation display to: " + formation);
    }

    private void loadSelectedStrategy(String strategyName) {
        if (strategyName == null) return;

        Map<String, Object> config = HeadCoachDataManager.loadLineupConfiguration(strategyName);
        if (config != null) {
            // Update UI with lineup data
            String formation = (String) config.get("formation");
            if (formation != null) {
                formationComboBox.setValue(formation);
            }

            String opponent = (String) config.get("opponent");
            if (opponent != null) {
                opponentComboBox.setValue(opponent);
            }

            @SuppressWarnings("unchecked")
            Map<String, String> tactics = (Map<String, String>) config.get("tactics");
            if (tactics != null) {
                // Update tactics fields if available
                if (tactics.containsKey("pressingStyle")) {
                    pressingStyleComboBox.setValue(tactics.get("pressingStyle"));
                }

                if (tactics.containsKey("defensiveLine")) {
                    defensiveLineComboBox.setValue(tactics.get("defensiveLine"));
                }

                if (tactics.containsKey("notes")) {
                    tacticalNotesArea.setText(tactics.get("notes"));
                }
            }

            // Update player assignments
            @SuppressWarnings("unchecked")
            List<TeamLineUp_Controller.PlayerLineup> players = (List<TeamLineUp_Controller.PlayerLineup>) config.get("players");
            if (players != null) {
                playerAssignments.clear();
                for (TeamLineUp_Controller.PlayerLineup player : players) {
                    playerAssignments.add(new PlayerAssignment(
                            player.getName(),
                            player.getPosition(),
                            "Standard"  // Default role
                    ));
                }
            }
        }
    }

    @FXML
    public void saveStrategy() {
        // Basic validation
        if (opponentComboBox.getSelectionModel().isEmpty() ||
                formationComboBox.getSelectionModel().isEmpty()) {
            showAlert("Missing Information",
                    "Please select an opponent and formation", Alert.AlertType.WARNING);
            return;
        }

        // Prompt for strategy name
        TextInputDialog dialog = new TextInputDialog("Strategy vs " + opponentComboBox.getValue());
        dialog.setTitle("Save Strategy");
        dialog.setHeaderText("Enter a name for this tactical strategy");
        dialog.setContentText("Strategy name:");

        Optional<String> result = dialog.showAndWait();
        if (result.isPresent() && !result.get().trim().isEmpty()) {
            String strategyName = result.get().trim();

            // Create tactics map
            Map<String, String> tactics = new HashMap<>();
            if (pressingStyleComboBox.getValue() != null) {
                tactics.put("pressingStyle", pressingStyleComboBox.getValue());
            }
            if (defensiveLineComboBox.getValue() != null) {
                tactics.put("defensiveLine", defensiveLineComboBox.getValue());
            }
            tactics.put("notes", tacticalNotesArea.getText());

            // Convert player assignments to PlayerLineup objects
            List<TeamLineUp_Controller.PlayerLineup> players = new ArrayList<>();

            for (PlayerAssignment assignment : playerAssignments) {
                // Fixed constructor call - passing jersey number as an integer (default to 0)
                TeamLineUp_Controller.PlayerLineup player = new TeamLineUp_Controller.PlayerLineup(
                        assignment.getPlayerName(),
                        assignment.getPosition(),
                        0,           // Jersey number as integer
                        "Active");   // Status
                players.add(player);
            }

            // Save the lineup configuration
            HeadCoachDataManager.saveLineupConfiguration(
                    strategyName,
                    players,
                    formationComboBox.getValue(),
                    opponentComboBox.getValue(),
                    "League Match", // Default
                    "Home",         // Default
                    tactics
            );

            // Update the list view
            List<String> lineupNames = HeadCoachDataManager.loadLineupsFromFile();
            savedStrategiesListView.setItems(FXCollections.observableArrayList(lineupNames));

            showAlert("Success", "Tactical strategy saved successfully", Alert.AlertType.INFORMATION);
        }
    }

    @FXML
    public void editPlayerRole() {
        PlayerAssignment selected = playerAssignmentsTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showAlert("No Selection", "Please select a player to edit role", Alert.AlertType.WARNING);
            return;
        }

        Dialog<String> dialog = new Dialog<>();
        dialog.setTitle("Edit Player Role");
        dialog.setHeaderText("Assign role to " + selected.getPlayerName());

        // Set button types
        ButtonType saveButtonType = new ButtonType("Save", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(saveButtonType, ButtonType.CANCEL);

        // Create role combobox
        ComboBox<String> roleCombo = new ComboBox<>();
        roleCombo.setItems(FXCollections.observableArrayList(
                "Standard", "Captain", "Set Piece Taker", "Playmaker",
                "Target Man", "Sweeper", "Free Role"));
        roleCombo.setValue(selected.getRole());

        // Add combobox to dialog
        VBox content = new VBox(10);
        content.getChildren().addAll(new Label("Select Role:"), roleCombo);
        dialog.getDialogPane().setContent(content);

        // Convert result
        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == saveButtonType) {
                return roleCombo.getValue();
            }
            return null;
        });

        // Show dialog and process result
        Optional<String> result = dialog.showAndWait();
        result.ifPresent(role -> {
            int index = playerAssignments.indexOf(selected);
            playerAssignments.set(index, new PlayerAssignment(
                    selected.getPlayerName(),
                    selected.getPosition(),
                    role));
        });
    }

    @FXML
    public void dashboard(ActionEvent event) throws IOException {
        SceneSwitcher.switchTo("DashboardHeadCoach.fxml", event);
    }

    private void showAlert(String title, String message, Alert.AlertType type) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    // Model classes
    public static class PlayerAssignment {
        private final String playerName;
        private final String position;
        private final String role;

        public PlayerAssignment(String playerName, String position, String role) {
            this.playerName = playerName;
            this.position = position;
            this.role = role;
        }

        public String getPlayerName() { return playerName; }
        public String getPosition() { return position; }
        public String getRole() { return role; }
    }

    public static class TacticalStrategy implements Serializable {
        private static final long serialVersionUID = 1L;

        private String name;
        private String opponent;
        private String formation;
        private LocalDate matchDate;
        private Map<String, String> tactics;
        private List<PlayerAssignment> playerAssignments;

        // Getters and setters
        public String getName() { return name; }
        public void setName(String name) { this.name = name; }

        public String getOpponent() { return opponent; }
        public void setOpponent(String opponent) { this.opponent = opponent; }

        public String getFormation() { return formation; }
        public void setFormation(String formation) { this.formation = formation; }

        public LocalDate getMatchDate() { return matchDate; }
        public void setMatchDate(LocalDate matchDate) { this.matchDate = matchDate; }

        public Map<String, String> getTactics() { return tactics; }
        public void setTactics(Map<String, String> tactics) { this.tactics = tactics; }

        public List<PlayerAssignment> getPlayerAssignments() { return playerAssignments; }
        public void setPlayerAssignments(List<PlayerAssignment> assignments) {
            this.playerAssignments = assignments;
        }
    }
}