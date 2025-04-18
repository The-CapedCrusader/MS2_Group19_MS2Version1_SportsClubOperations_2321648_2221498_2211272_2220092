package cse213.cse213_sporting_club_operations.TanvirMahmud;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class HCGoal1 {
    // Squad players
    @FXML private TableView<Player> squadTable;
    @FXML private TableColumn<Player, String> nameColumn;
    @FXML private TableColumn<Player, String> positionColumn;
    @FXML private TableColumn<Player, String> fitnessColumn;
    @FXML private TableColumn<Player, String> formColumn;

    // Starting lineup
    @FXML private TableView<PlayerLineup> startingLineupTable;
    @FXML private TableColumn<PlayerLineup, String> lineupNameColumn;
    @FXML private TableColumn<PlayerLineup, String> lineupPositionColumn;
    @FXML private TableColumn<PlayerLineup, Number> lineupNumberColumn;
    @FXML private TableColumn<PlayerLineup, String> lineupRoleColumn;

    // Match info
    @FXML private DatePicker matchDatePicker;
    @FXML private TextField opponentField;
    @FXML private ComboBox<String> venueComboBox;
    @FXML private ComboBox<String> formationComboBox;
    @FXML private ComboBox<String> competitionComboBox;

    // Tactical instructions
    @FXML private ComboBox<String> defensiveShapeComboBox;
    @FXML private ComboBox<String> offensiveStyleComboBox;
    @FXML private ComboBox<String> pressingComboBox;
    @FXML private Slider tempoSlider;
    @FXML private TextArea tacticNotesArea;

    // Saved lineups
    @FXML private ListView<String> savedLineupsListView;

    // Track selected positions for drag-and-drop field visualization
    @FXML private VBox fieldVisualization;

    private ObservableList<Player> squadPlayers = FXCollections.observableArrayList();
    private ObservableList<PlayerLineup> startingLineup = FXCollections.observableArrayList();
    private ObservableList<String> savedLineups = FXCollections.observableArrayList();

    private Map<String, List<String>> formationPositions = new HashMap<>();

    @FXML
    public void initialize() {
        // Initialize squad table columns
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        positionColumn.setCellValueFactory(new PropertyValueFactory<>("position"));
        fitnessColumn.setCellValueFactory(new PropertyValueFactory<>("fitness"));
        formColumn.setCellValueFactory(new PropertyValueFactory<>("value"));

        // Initialize lineup table columns
        lineupNameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        lineupPositionColumn.setCellValueFactory(new PropertyValueFactory<>("position"));
        lineupNumberColumn.setCellValueFactory(new PropertyValueFactory<>("number"));
        lineupRoleColumn.setCellValueFactory(new PropertyValueFactory<>("role"));

        // Set default match date
        matchDatePicker.setValue(LocalDate.now());

        // Initialize combo boxes
        venueComboBox.setItems(FXCollections.observableArrayList("Home", "Away", "Neutral"));
        competitionComboBox.setItems(FXCollections.observableArrayList(
                "Premier League", "FA Cup", "Champions League", "League Cup", "Friendly"));
        formationComboBox.setItems(FXCollections.observableArrayList(
                "4-3-3", "4-2-3-1", "4-4-2", "3-5-2", "3-4-3", "5-3-2"));

        // Tactical options
        defensiveShapeComboBox.setItems(FXCollections.observableArrayList(
                "Deep Block", "Mid Block", "High Line", "Offside Trap"));
        offensiveStyleComboBox.setItems(FXCollections.observableArrayList(
                "Possession", "Counter-attack", "Direct", "Tiki-taka", "Wing Play"));
        pressingComboBox.setItems(FXCollections.observableArrayList(
                "High Press", "Mid Press", "Low Press", "Pressing Trap"));

        // Define formation positions
        initializeFormationPositions();

        // Add formation change listener
        formationComboBox.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> {
                    if (newValue != null) {
                        updateFormationDisplay(newValue);
                    }
                });

        // Add squad players - get from database or use sample data
        loadSquadPlayers();

        // Set up double-click to add player to lineup
        squadTable.setRowFactory(tv -> {
            TableRow<Player> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (event.getClickCount() == 2 && !row.isEmpty()) {
                    addPlayerToLineup(row.getItem());
                }
            });
            return row;
        });

        // Set up double-click to remove player from lineup
        startingLineupTable.setRowFactory(tv -> {
            TableRow<PlayerLineup> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (event.getClickCount() == 2 && !row.isEmpty()) {
                    removePlayerFromLineup(row.getItem());
                }
            });
            return row;
        });

        // Load sample saved lineups
        savedLineups.addAll(
                "4-3-3 vs Manchester City (Premier League)",
                "4-2-3-1 vs Bayern Munich (Champions League)",
                "3-5-2 vs Liverpool (FA Cup)"
        );
        savedLineupsListView.setItems(savedLineups);

        // Set up saved lineup selection
        savedLineupsListView.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> {
                    if (newValue != null) {
                        loadSavedLineup(newValue);
                    }
                });
    }

    private void initializeFormationPositions() {
        // 4-3-3 positions
        formationPositions.put("4-3-3", Arrays.asList(
                "GK", "RB", "CB", "CB", "LB", "CDM", "CM", "CM", "RW", "ST", "LW"));

        // 4-2-3-1 positions
        formationPositions.put("4-2-3-1", Arrays.asList(
                "GK", "RB", "CB", "CB", "LB", "CDM", "CDM", "RAM", "CAM", "LAM", "ST"));

        // 4-4-2 positions
        formationPositions.put("4-4-2", Arrays.asList(
                "GK", "RB", "CB", "CB", "LB", "RM", "CM", "CM", "LM", "ST", "ST"));

        // Add other formations similarly
        formationPositions.put("3-5-2", Arrays.asList(
                "GK", "CB", "CB", "CB", "RWB", "CM", "CDM", "CM", "LWB", "ST", "ST"));

        formationPositions.put("3-4-3", Arrays.asList(
                "GK", "CB", "CB", "CB", "RM", "CM", "CM", "LM", "RW", "ST", "LW"));

        formationPositions.put("5-3-2", Arrays.asList(
                "GK", "RWB", "CB", "CB", "CB", "LWB", "CM", "CM", "CM", "ST", "ST"));
    }

    private void loadSquadPlayers() {
        // If data exists from Goal1_AllPlayers, use it
        if (Goal1_AllPlayers.playerList != null && !Goal1_AllPlayers.playerList.isEmpty()) {
            squadPlayers.addAll(Goal1_AllPlayers.playerList);
        } else {
            // Sample data if no players exist
            squadPlayers.add(new Player("David De Gea", "34", "GK", "95%", "$12M"));
            squadPlayers.add(new Player("Trent Alexander-Arnold", "24", "RB", "90%", "$45M"));
            squadPlayers.add(new Player("Virgil van Dijk", "31", "CB", "85%", "$35M"));
            squadPlayers.add(new Player("Raphael Varane", "29", "CB", "80%", "$30M"));
            squadPlayers.add(new Player("Andrew Robertson", "28", "LB", "92%", "$40M"));
            squadPlayers.add(new Player("Rodri", "26", "CDM", "93%", "$50M"));
            squadPlayers.add(new Player("Kevin De Bruyne", "31", "CM", "88%", "$70M"));
            squadPlayers.add(new Player("Bruno Fernandes", "28", "CAM", "87%", "$65M"));
            squadPlayers.add(new Player("Mohamed Salah", "30", "RW", "89%", "$60M"));
            squadPlayers.add(new Player("Harry Kane", "29", "ST", "86%", "$80M"));
            squadPlayers.add(new Player("Son Heung-min", "30", "LW", "91%", "$55M"));
            squadPlayers.add(new Player("Ederson", "29", "GK", "94%", "$35M"));
            squadPlayers.add(new Player("Reece James", "23", "RB", "88%", "$40M"));
            squadPlayers.add(new Player("Ruben Dias", "26", "CB", "90%", "$50M"));
        }

        squadTable.setItems(squadPlayers);
    }

    private void updateFormationDisplay(String formation) {
        // Clear current lineup
        startingLineup.clear();

        // Get position list for selected formation
        List<String> positions = formationPositions.get(formation);
        if (positions == null) return;

        // Create empty lineup with positions
        for (int i = 0; i < 11; i++) {
            startingLineup.add(new PlayerLineup("Select player", positions.get(i), i + 1, "Standard"));
        }

        startingLineupTable.setItems(startingLineup);
    }

    private void addPlayerToLineup(Player player) {
        if (startingLineup.isEmpty()) {
            showAlert("No Formation Selected",
                    "Please select a formation before adding players.",
                    Alert.AlertType.WARNING);
            return;
        }

        // Check if player already in lineup
        for (PlayerLineup lineupPlayer : startingLineup) {
            if (lineupPlayer.getName().equals(player.getName())) {
                showAlert("Player Already Selected",
                        player.getName() + " is already in the lineup.",
                        Alert.AlertType.WARNING);
                return;
            }
        }

        // Find suitable position based on player's natural position
        String playerPosition = player.getPosition();
        int positionIndex = -1;

        // First try to find exact position match
        for (int i = 0; i < startingLineup.size(); i++) {
            if (startingLineup.get(i).getName().equals("Select player") &&
                    startingLineup.get(i).getPosition().equals(playerPosition)) {
                positionIndex = i;
                break;
            }
        }

        // If no exact match, try to find compatible position
        if (positionIndex == -1) {
            for (int i = 0; i < startingLineup.size(); i++) {
                if (startingLineup.get(i).getName().equals("Select player") &&
                        isCompatiblePosition(playerPosition, startingLineup.get(i).getPosition())) {
                    positionIndex = i;
                    break;
                }
            }
        }

        // If still no match, find any open position
        if (positionIndex == -1) {
            for (int i = 0; i < startingLineup.size(); i++) {
                if (startingLineup.get(i).getName().equals("Select player")) {
                    positionIndex = i;
                    break;
                }
            }
        }

        if (positionIndex != -1) {
            PlayerLineup current = startingLineup.get(positionIndex);
            PlayerLineup updated = new PlayerLineup(
                    player.getName(),
                    current.getPosition(),
                    current.getNumber(),
                    "Standard"
            );
            startingLineup.set(positionIndex, updated);
        } else {
            showAlert("Lineup Full",
                    "All positions are filled. Remove a player before adding a new one.",
                    Alert.AlertType.WARNING);
        }
    }

    private boolean isCompatiblePosition(String playerPosition, String lineupPosition) {
        // Define position compatibility
        Map<String, List<String>> compatiblePositions = new HashMap<>();
        compatiblePositions.put("GK", Collections.singletonList("GK"));
        compatiblePositions.put("RB", Arrays.asList("RB", "RWB", "CB"));
        compatiblePositions.put("CB", Arrays.asList("CB", "RB", "LB"));
        compatiblePositions.put("LB", Arrays.asList("LB", "LWB", "CB"));
        compatiblePositions.put("CDM", Arrays.asList("CDM", "CM", "CB"));
        compatiblePositions.put("CM", Arrays.asList("CM", "CDM", "CAM", "RM", "LM"));
        compatiblePositions.put("CAM", Arrays.asList("CAM", "CM", "RAM", "LAM", "ST"));
        compatiblePositions.put("RM", Arrays.asList("RM", "RW", "RAM", "CM", "RWB"));
        compatiblePositions.put("LM", Arrays.asList("LM", "LW", "LAM", "CM", "LWB"));
        compatiblePositions.put("RW", Arrays.asList("RW", "RM", "RAM", "ST"));
        compatiblePositions.put("LW", Arrays.asList("LW", "LM", "LAM", "ST"));
        compatiblePositions.put("ST", Arrays.asList("ST", "CAM", "RW", "LW"));
        compatiblePositions.put("RWB", Arrays.asList("RWB", "RB", "RM"));
        compatiblePositions.put("LWB", Arrays.asList("LWB", "LB", "LM"));
        compatiblePositions.put("RAM", Arrays.asList("RAM", "CAM", "RM", "RW"));
        compatiblePositions.put("LAM", Arrays.asList("LAM", "CAM", "LM", "LW"));

        List<String> compatible = compatiblePositions.get(playerPosition);
        return compatible != null && compatible.contains(lineupPosition);
    }

    private void removePlayerFromLineup(PlayerLineup player) {
        int index = startingLineup.indexOf(player);
        if (index >= 0) {
            // Replace with empty player slot but keep position
            startingLineup.set(index, new PlayerLineup(
                    "Select player",
                    player.getPosition(),
                    player.getNumber(),
                    "Standard"
            ));
        }
    }

    @FXML
    public void saveLineup() {
        // Check if all positions are filled
        boolean lineupComplete = true;
        for (PlayerLineup player : startingLineup) {
            if (player.getName().equals("Select player")) {
                lineupComplete = false;
                break;
            }
        }

        if (!lineupComplete) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Incomplete Lineup");
            alert.setHeaderText("The lineup is not complete");
            alert.setContentText("Some positions don't have players assigned. Save anyway?");

            Optional<ButtonType> result = alert.showAndWait();
            if (result.isPresent() && result.get() != ButtonType.OK) {
                return;
            }
        }

        // Check required fields
        if (opponentField.getText().trim().isEmpty()) {
            showAlert("Missing Information", "Please enter the opponent.", Alert.AlertType.ERROR);
            return;
        }

        if (venueComboBox.getValue() == null) {
            showAlert("Missing Information", "Please select a venue.", Alert.AlertType.ERROR);
            return;
        }

        if (competitionComboBox.getValue() == null) {
            showAlert("Missing Information", "Please select a competition.", Alert.AlertType.ERROR);
            return;
        }

        // Create lineup name
        String formation = formationComboBox.getValue();
        String opponent = opponentField.getText().trim();
        String competition = competitionComboBox.getValue();
        String lineup = formation + " vs " + opponent + " (" + competition + ")";

        // Add to saved lineups if not already present
        if (!savedLineups.contains(lineup)) {
            savedLineups.add(lineup);
            savedLineupsListView.getSelectionModel().select(lineup);

            showAlert("Success", "Lineup saved successfully.", Alert.AlertType.INFORMATION);
        } else {
            // Update existing lineup
            savedLineupsListView.getSelectionModel().select(lineup);
            showAlert("Success", "Lineup updated successfully.", Alert.AlertType.INFORMATION);
        }
    }

    private void loadSavedLineup(String lineupName) {
        // Extract formation from lineup name
        String formation = lineupName.split(" vs ")[0];
        formationComboBox.setValue(formation);

        // Extract opponent and competition
        String[] parts = lineupName.split(" vs ");
        if (parts.length > 1) {
            String opponentAndCompetition = parts[1];
            String opponent = opponentAndCompetition.split(" \\(")[0];
            opponentField.setText(opponent);

            if (opponentAndCompetition.contains("(") && opponentAndCompetition.contains(")")) {
                String competition = opponentAndCompetition.substring(
                        opponentAndCompetition.indexOf("(") + 1,
                        opponentAndCompetition.indexOf(")")
                );
                competitionComboBox.setValue(competition);
            }
        }

        // In a real app, you would load the actual players from a database
        // For this example, we'll just use the formation structure
        updateFormationDisplay(formation);
    }

    @FXML
    public void deleteLineup() {
        String selectedLineup = savedLineupsListView.getSelectionModel().getSelectedItem();
        if (selectedLineup == null) {
            showAlert("No Selection", "Please select a lineup to delete.", Alert.AlertType.ERROR);
            return;
        }

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirm Delete");
        alert.setHeaderText("Delete Lineup");
        alert.setContentText("Are you sure you want to delete the selected lineup?");

        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            savedLineups.remove(selectedLineup);
        }
    }

    @FXML
    public void clearLineup() {
        if (formationComboBox.getValue() == null) {
            showAlert("No Formation", "Please select a formation first.", Alert.AlertType.WARNING);
            return;
        }

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirm Clear");
        alert.setHeaderText("Clear Lineup");
        alert.setContentText("Are you sure you want to clear all players from the lineup?");

        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            updateFormationDisplay(formationComboBox.getValue());
        }
    }

    @FXML
    public void dashboard(ActionEvent actionEvent) throws IOException {
        SceneSwitcher.switchTo("DashboardHeadCoach.fxml", actionEvent);
    }

    private void showAlert(String title, String message, Alert.AlertType type) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    // Inner class for lineup players
    public static class PlayerLineup {
        private final String name;
        private final String position;
        private final int number;
        private final String role;

        public PlayerLineup(String name, String position, int number, String role) {
            this.name = name;
            this.position = position;
            this.number = number;
            this.role = role;
        }

        public String getName() { return name; }
        public String getPosition() { return position; }
        public int getNumber() { return number; }
        public String getRole() { return role; }
    }
}