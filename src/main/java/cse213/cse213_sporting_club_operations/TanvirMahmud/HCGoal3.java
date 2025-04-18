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
import java.util.Optional;

public class HCGoal3 {
    // Match details
    @FXML private ComboBox<String> opponentComboBox;
    @FXML private DatePicker matchDatePicker;
    @FXML private ComboBox<String> venueComboBox;
    @FXML private ComboBox<String> competitionComboBox;

    // Formation and strategy
    @FXML private ComboBox<String> formationComboBox;
    @FXML private ComboBox<String> pressingIntensityComboBox;
    @FXML private ComboBox<String> defensiveShapeComboBox;
    @FXML private ComboBox<String> attackingPatternComboBox;
    @FXML private ComboBox<String> possessionStyleComboBox;
    @FXML private Slider tempoSlider;
    @FXML private Slider defenseLineSlider;
    @FXML private Slider widthSlider;

    // Player roles
    @FXML private TableView<PlayerRole> playerRolesTable;
    @FXML private TableColumn<PlayerRole, String> playerNameColumn;
    @FXML private TableColumn<PlayerRole, String> positionColumn;
    @FXML private TableColumn<PlayerRole, String> roleColumn;
    @FXML private TableColumn<PlayerRole, String> specificInstructionsColumn;

    // Strategy adjustments
    @FXML private TextArea counterStrategyArea;
    @FXML private TextArea setpiecesStrategyArea;
    @FXML private TextArea substitutionStrategyArea;

    // Phase of play details
    @FXML private TextArea defensivePhaseArea;
    @FXML private TextArea transitionPhaseArea;
    @FXML private TextArea attackingPhaseArea;

    // Saved strategies
    @FXML private TableView<MatchStrategy> savedStrategiesTable;
    @FXML private TableColumn<MatchStrategy, String> strategyNameColumn;
    @FXML private TableColumn<MatchStrategy, String> opponentColumn;
    @FXML private TableColumn<MatchStrategy, String> dateColumn;
    @FXML private TableColumn<MatchStrategy, String> formationColumn;
    @FXML private TableColumn<MatchStrategy, String> styleColumn;

    // Strategy visualization
    @FXML private VBox visualizationBox;

    private ObservableList<PlayerRole> playerRoles = FXCollections.observableArrayList();
    private ObservableList<MatchStrategy> savedStrategies = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        // Initialize date picker
        matchDatePicker.setValue(LocalDate.now());

        // Initialize comboboxes with options
        opponentComboBox.setItems(FXCollections.observableArrayList(
                "Arsenal FC", "Chelsea FC", "Liverpool FC", "Manchester City",
                "Manchester United", "Tottenham Hotspur", "Everton FC", "Leicester City",
                "West Ham United", "Aston Villa"));

        venueComboBox.setItems(FXCollections.observableArrayList("Home", "Away", "Neutral"));

        competitionComboBox.setItems(FXCollections.observableArrayList(
                "Premier League", "FA Cup", "League Cup", "Champions League",
                "Europa League", "Club Friendly"));

        formationComboBox.setItems(FXCollections.observableArrayList(
                "4-4-2", "4-3-3", "4-2-3-1", "3-5-2", "3-4-3", "5-3-2", "5-4-1"));

        pressingIntensityComboBox.setItems(FXCollections.observableArrayList(
                "High Press", "Medium Press", "Low Press", "Selective Press", "No Press"));

        defensiveShapeComboBox.setItems(FXCollections.observableArrayList(
                "High Line", "Medium Block", "Low Block", "Compact", "Wide"));

        attackingPatternComboBox.setItems(FXCollections.observableArrayList(
                "Direct Play", "Wing Play", "Through the Middle", "Counter-attack", "Set Pieces"));

        possessionStyleComboBox.setItems(FXCollections.observableArrayList(
                "Possession-based", "Direct", "Counter-attacking", "Balanced"));

        // Setup table columns for player roles
        playerNameColumn.setCellValueFactory(new PropertyValueFactory<>("playerName"));
        positionColumn.setCellValueFactory(new PropertyValueFactory<>("position"));
        roleColumn.setCellValueFactory(new PropertyValueFactory<>("role"));
        specificInstructionsColumn.setCellValueFactory(new PropertyValueFactory<>("instructions"));
        playerRolesTable.setItems(playerRoles);

        // Setup saved strategies table
        strategyNameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        opponentColumn.setCellValueFactory(new PropertyValueFactory<>("opponent"));
        dateColumn.setCellValueFactory(data -> {
            LocalDate date = data.getValue().getMatchDate();
            if (date != null) {
                return new SimpleStringProperty(date.format(DateTimeFormatter.ofPattern("MM/dd/yyyy")));
            }
            return new SimpleStringProperty("");
        });
        formationColumn.setCellValueFactory(new PropertyValueFactory<>("formation"));
        styleColumn.setCellValueFactory(new PropertyValueFactory<>("style"));
        savedStrategiesTable.setItems(savedStrategies);

        // Load sample data
        loadSampleData();

        // Add listeners
        formationComboBox.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null) {
                updateFormationVisualization(newVal);
            }
        });

        savedStrategiesTable.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null) {
                loadStrategy(newVal);
            }
        });
    }

    private void loadSampleData() {
        // Sample player roles
        playerRoles.add(new PlayerRole("David de Gea", "GK", "Sweeper Keeper", "Stay on line, distribute quickly"));
        playerRoles.add(new PlayerRole("Luke Shaw", "LB", "Attacking Fullback", "Overlap, provide width"));
        playerRoles.add(new PlayerRole("Harry Maguire", "CB", "Ball Playing Defender", "Start build-up, cover wide spaces"));
        playerRoles.add(new PlayerRole("Raphael Varane", "CB", "Stopper", "Aggressive pressing, cover depth"));
        playerRoles.add(new PlayerRole("Aaron Wan-Bissaka", "RB", "Defensive Fullback", "Stay back, tight marking"));
        playerRoles.add(new PlayerRole("Scott McTominay", "CDM", "Ball Winner", "Break up play, simple passes"));

        // Sample saved strategies
        savedStrategies.add(new MatchStrategy(
                "Counter Strategy vs City",
                "Manchester City",
                LocalDate.now().plusDays(10),
                "5-3-2",
                "Counter-attacking",
                "Away",
                "Premier League"
        ));

        savedStrategies.add(new MatchStrategy(
                "High Press vs Arsenal",
                "Arsenal FC",
                LocalDate.now().plusDays(17),
                "4-3-3",
                "Pressing",
                "Home",
                "Premier League"
        ));

        savedStrategies.add(new MatchStrategy(
                "Possession Setup vs Newcastle",
                "Newcastle United",
                LocalDate.now().plusDays(24),
                "4-2-3-1",
                "Possession-based",
                "Home",
                "FA Cup"
        ));
    }

    private void updateFormationVisualization(String formation) {
        // In a real implementation, this would update a graphical representation
        System.out.println("Updating visualization for formation: " + formation);
        // visualizationBox would be updated with a custom node showing the formation
    }

    private void loadStrategy(MatchStrategy strategy) {
        // Populate form fields with selected strategy
        opponentComboBox.setValue(strategy.getOpponent());
        matchDatePicker.setValue(strategy.getMatchDate());
        venueComboBox.setValue(strategy.getVenue());
        competitionComboBox.setValue(strategy.getCompetition());
        formationComboBox.setValue(strategy.getFormation());

        // Additional strategy details would be loaded here
        // This is simplified for the example
    }

    @FXML
    public void saveStrategy() {
        // Validate required fields
        if (opponentComboBox.getValue() == null || formationComboBox.getValue() == null) {
            showAlert("Error", "Please select an opponent and formation", Alert.AlertType.ERROR);
            return;
        }

        // Create strategy name
        TextInputDialog dialog = new TextInputDialog("Strategy vs " + opponentComboBox.getValue());
        dialog.setTitle("Save Strategy");
        dialog.setHeaderText("Enter a name for this tactical strategy");
        dialog.setContentText("Strategy name:");

        Optional<String> result = dialog.showAndWait();
        if (result.isPresent() && !result.get().trim().isEmpty()) {
            String strategyName = result.get().trim();

            // Determine style based on selections
            String style = possessionStyleComboBox.getValue() != null ?
                    possessionStyleComboBox.getValue() : "Balanced";

            // Create new strategy
            MatchStrategy newStrategy = new MatchStrategy(
                    strategyName,
                    opponentComboBox.getValue(),
                    matchDatePicker.getValue(),
                    formationComboBox.getValue(),
                    style,
                    venueComboBox.getValue(),
                    competitionComboBox.getValue()
            );

            // Add to list
            savedStrategies.add(newStrategy);

            showAlert("Success", "Strategy saved successfully", Alert.AlertType.INFORMATION);
        }
    }

    @FXML
    public void deleteStrategy() {
        MatchStrategy selectedStrategy = savedStrategiesTable.getSelectionModel().getSelectedItem();
        if (selectedStrategy == null) {
            showAlert("Error", "Please select a strategy to delete", Alert.AlertType.ERROR);
            return;
        }

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Delete Strategy");
        alert.setHeaderText("Delete " + selectedStrategy.getName());
        alert.setContentText("Are you sure you want to delete this strategy? This cannot be undone.");

        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            savedStrategies.remove(selectedStrategy);
        }
    }

    @FXML
    public void addPlayerRole() {
        Dialog<PlayerRole> dialog = new Dialog<>();
        dialog.setTitle("Add Player Role");
        dialog.setHeaderText("Define player role and instructions");

        // Set the button types
        ButtonType saveButtonType = new ButtonType("Save", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(saveButtonType, ButtonType.CANCEL);

        // Create the fields and labels
        ComboBox<String> playerCombo = new ComboBox<>();
        playerCombo.setItems(FXCollections.observableArrayList(
                "David de Gea", "Luke Shaw", "Harry Maguire", "Raphael Varane",
                "Aaron Wan-Bissaka", "Scott McTominay", "Bruno Fernandes",
                "Paul Pogba", "Jadon Sancho", "Mason Greenwood", "Cristiano Ronaldo"
        ));

        ComboBox<String> positionCombo = new ComboBox<>();
        positionCombo.setItems(FXCollections.observableArrayList(
                "GK", "LB", "CB", "RB", "CDM", "CM", "CAM", "LW", "RW", "ST"
        ));

        ComboBox<String> roleCombo = new ComboBox<>();
        roleCombo.setItems(FXCollections.observableArrayList(
                "Sweeper Keeper", "Ball Playing Defender", "Stopper", "Attacking Fullback",
                "Wing Back", "Defensive Fullback", "Ball Winner", "Deep Lying Playmaker",
                "Box to Box Midfielder", "Advanced Playmaker", "Winger", "Inside Forward",
                "Target Man", "Complete Forward", "Poacher"
        ));

        TextField instructionsField = new TextField();
        instructionsField.setPromptText("Enter specific instructions");

        // Create and arrange scenes
        VBox content = new VBox(10);
        content.getChildren().addAll(
                new Label("Player:"), playerCombo,
                new Label("Position:"), positionCombo,
                new Label("Role:"), roleCombo,
                new Label("Instructions:"), instructionsField
        );
        dialog.getDialogPane().setContent(content);

        // Convert the result
        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == saveButtonType) {
                if (playerCombo.getValue() != null && positionCombo.getValue() != null &&
                        roleCombo.getValue() != null) {
                    return new PlayerRole(
                            playerCombo.getValue(),
                            positionCombo.getValue(),
                            roleCombo.getValue(),
                            instructionsField.getText()
                    );
                }
            }
            return null;
        });

        Optional<PlayerRole> result = dialog.showAndWait();
        result.ifPresent(role -> {
            playerRoles.add(role);
        });
    }

    @FXML
    public void editPlayerRole() {
        PlayerRole selectedRole = playerRolesTable.getSelectionModel().getSelectedItem();
        if (selectedRole == null) {
            showAlert("Error", "Please select a player role to edit", Alert.AlertType.ERROR);
            return;
        }

        // Similar to addPlayerRole but pre-populate fields
        // Implementation omitted for brevity
    }

    @FXML
    public void removePlayerRole() {
        PlayerRole selectedRole = playerRolesTable.getSelectionModel().getSelectedItem();
        if (selectedRole == null) {
            showAlert("Error", "Please select a player role to remove", Alert.AlertType.ERROR);
            return;
        }

        playerRoles.remove(selectedRole);
    }

    @FXML
    public void generatePDF() {
        // Generate a PDF report of the tactical strategy
        showAlert("Information", "PDF generation would be implemented here", Alert.AlertType.INFORMATION);
    }

    @FXML
    public void clearForm() {
        opponentComboBox.setValue(null);
        matchDatePicker.setValue(LocalDate.now());
        venueComboBox.setValue(null);
        competitionComboBox.setValue(null);
        formationComboBox.setValue(null);
        pressingIntensityComboBox.setValue(null);
        defensiveShapeComboBox.setValue(null);
        attackingPatternComboBox.setValue(null);
        possessionStyleComboBox.setValue(null);

        tempoSlider.setValue(50);
        defenseLineSlider.setValue(50);
        widthSlider.setValue(50);

        counterStrategyArea.clear();
        setpiecesStrategyArea.clear();
        substitutionStrategyArea.clear();
        defensivePhaseArea.clear();
        transitionPhaseArea.clear();
        attackingPhaseArea.clear();

        playerRoles.clear();
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

    // Inner classes
    public static class PlayerRole {
        private final String playerName;
        private final String position;
        private final String role;
        private final String instructions;

        public PlayerRole(String playerName, String position, String role, String instructions) {
            this.playerName = playerName;
            this.position = position;
            this.role = role;
            this.instructions = instructions;
        }

        public String getPlayerName() { return playerName; }
        public String getPosition() { return position; }
        public String getRole() { return role; }
        public String getInstructions() { return instructions; }
    }

    public static class MatchStrategy {
        private final String name;
        private final String opponent;
        private final LocalDate matchDate;
        private final String formation;
        private final String style;
        private final String venue;
        private final String competition;

        public MatchStrategy(String name, String opponent, LocalDate matchDate, String formation,
                             String style, String venue, String competition) {
            this.name = name;
            this.opponent = opponent;
            this.matchDate = matchDate;
            this.formation = formation;
            this.style = style;
            this.venue = venue;
            this.competition = competition;
        }

        public String getName() { return name; }
        public String getOpponent() { return opponent; }
        public LocalDate getMatchDate() { return matchDate; }
        public String getFormation() { return formation; }
        public String getStyle() { return style; }
        public String getVenue() { return venue; }
        public String getCompetition() { return competition; }
    }
}