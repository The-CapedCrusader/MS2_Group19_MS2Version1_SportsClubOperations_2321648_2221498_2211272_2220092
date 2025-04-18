package cse213.cse213_sporting_club_operations.TanvirMahmud;

import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.io.IOException;
import java.text.NumberFormat;
import java.util.Comparator;
import java.util.Locale;
import java.util.Optional;

public class Goal7 {
    // Search and filter
    @FXML private ComboBox<String> positionFilterComboBox;
    @FXML private Slider ageFromSlider;
    @FXML private Slider ageToSlider;
    @FXML private Slider valueFromSlider;
    @FXML private Slider valueToSlider;
    @FXML private Label ageFromLabel;
    @FXML private Label ageToLabel;
    @FXML private Label valueFromLabel;
    @FXML private Label valueToLabel;
    @FXML private ComboBox<String> statSortComboBox;
    @FXML private TextField searchField;

    // Player scouting table
    @FXML private TableView<ScoutedPlayer> scoutedPlayersTable;
    @FXML private TableColumn<ScoutedPlayer, String> nameColumn;
    @FXML private TableColumn<ScoutedPlayer, Integer> ageColumn;
    @FXML private TableColumn<ScoutedPlayer, String> positionColumn;
    @FXML private TableColumn<ScoutedPlayer, String> clubColumn;
    @FXML private TableColumn<ScoutedPlayer, Double> valueColumn;
    @FXML private TableColumn<ScoutedPlayer, Integer> technicalColumn;
    @FXML private TableColumn<ScoutedPlayer, Integer> physicalColumn;
    @FXML private TableColumn<ScoutedPlayer, Integer> mentalColumn;
    @FXML private TableColumn<ScoutedPlayer, Integer> goalsColumn;
    @FXML private TableColumn<ScoutedPlayer, Integer> assistsColumn;
    @FXML private TableColumn<ScoutedPlayer, String> scoutRatingColumn;

    // Shortlist table
    @FXML private TableView<ScoutedPlayer> shortlistTable;
    @FXML private TableColumn<ScoutedPlayer, String> shortlistNameColumn;
    @FXML private TableColumn<ScoutedPlayer, String> shortlistPositionColumn;
    @FXML private TableColumn<ScoutedPlayer, String> shortlistClubColumn;
    @FXML private TableColumn<ScoutedPlayer, Double> shortlistValueColumn;
    @FXML private TableColumn<ScoutedPlayer, String> shortlistRatingColumn;
    @FXML private TableColumn<ScoutedPlayer, String> shortlistNotesColumn;

    // Notes
    @FXML private TextArea playerNotesArea;
    @FXML private Label selectedPlayerLabel;

    // Squad needs
    @FXML private ComboBox<String> squadNeedComboBox;
    @FXML private ListView<String> squadNeedsListView;

    private ObservableList<ScoutedPlayer> scoutedPlayers = FXCollections.observableArrayList();
    private ObservableList<ScoutedPlayer> filteredPlayers = FXCollections.observableArrayList();
    private ObservableList<ScoutedPlayer> shortlistedPlayers = FXCollections.observableArrayList();
    private ObservableList<String> squadNeeds = FXCollections.observableArrayList();

    private NumberFormat currencyFormat = NumberFormat.getCurrencyInstance(Locale.US);

    @FXML
    public void initialize() {
        // Setup position filter options
        positionFilterComboBox.setItems(FXCollections.observableArrayList(
                "All Positions", "Goalkeeper", "Defender", "Midfielder", "Forward"
        ));
        positionFilterComboBox.setValue("All Positions");

        // Setup statistic sort options
        statSortComboBox.setItems(FXCollections.observableArrayList(
                "Overall Rating", "Market Value", "Technical", "Physical", "Mental", "Goals", "Assists"
        ));
        statSortComboBox.setValue("Overall Rating");

        // Setup age sliders
        ageFromSlider.setMin(16);
        ageFromSlider.setMax(40);
        ageFromSlider.setValue(18);
        ageToSlider.setMin(16);
        ageToSlider.setMax(40);
        ageToSlider.setValue(35);

        ageFromSlider.valueProperty().addListener((obs, oldVal, newVal) -> {
            ageFromLabel.setText(String.format("Age From: %d", newVal.intValue()));
            if (newVal.intValue() > ageToSlider.getValue()) {
                ageToSlider.setValue(newVal.doubleValue());
            }
            filterPlayers();
        });

        ageToSlider.valueProperty().addListener((obs, oldVal, newVal) -> {
            ageToLabel.setText(String.format("Age To: %d", newVal.intValue()));
            if (newVal.intValue() < ageFromSlider.getValue()) {
                ageFromSlider.setValue(newVal.doubleValue());
            }
            filterPlayers();
        });

        // Setup value sliders (in millions)
        valueFromSlider.setMin(0);
        valueFromSlider.setMax(100);
        valueFromSlider.setValue(0);
        valueToSlider.setMin(0);
        valueToSlider.setMax(100);
        valueToSlider.setValue(50);

        valueFromSlider.valueProperty().addListener((obs, oldVal, newVal) -> {
            valueFromLabel.setText(String.format("Value From: %s", currencyFormat.format(newVal.doubleValue() * 1_000_000)));
            if (newVal.doubleValue() > valueToSlider.getValue()) {
                valueToSlider.setValue(newVal.doubleValue());
            }
            filterPlayers();
        });

        valueToSlider.valueProperty().addListener((obs, oldVal, newVal) -> {
            valueToLabel.setText(String.format("Value To: %s", currencyFormat.format(newVal.doubleValue() * 1_000_000)));
            if (newVal.doubleValue() < valueFromSlider.getValue()) {
                valueFromSlider.setValue(newVal.doubleValue());
            }
            filterPlayers();
        });

        // Setup initial labels
        ageFromLabel.setText(String.format("Age From: %d", (int)ageFromSlider.getValue()));
        ageToLabel.setText(String.format("Age To: %d", (int)ageToSlider.getValue()));
        valueFromLabel.setText(String.format("Value From: %s", currencyFormat.format(valueFromSlider.getValue() * 1_000_000)));
        valueToLabel.setText(String.format("Value To: %s", currencyFormat.format(valueToSlider.getValue() * 1_000_000)));

        // Setup scouted players table
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        ageColumn.setCellValueFactory(new PropertyValueFactory<>("age"));
        positionColumn.setCellValueFactory(new PropertyValueFactory<>("position"));
        clubColumn.setCellValueFactory(new PropertyValueFactory<>("club"));
        valueColumn.setCellValueFactory(data -> {
            SimpleDoubleProperty property = new SimpleDoubleProperty(data.getValue().getValue());
            return property.asObject();
        });
        valueColumn.setCellFactory(column -> new TableCell<>() {
            @Override
            protected void updateItem(Double value, boolean empty) {
                super.updateItem(value, empty);
                if (empty || value == null) {
                    setText(null);
                } else {
                    setText(currencyFormat.format(value * 1_000_000));
                }
            }
        });
        technicalColumn.setCellValueFactory(new PropertyValueFactory<>("technical"));
        physicalColumn.setCellValueFactory(new PropertyValueFactory<>("physical"));
        mentalColumn.setCellValueFactory(new PropertyValueFactory<>("mental"));
        goalsColumn.setCellValueFactory(new PropertyValueFactory<>("goals"));
        assistsColumn.setCellValueFactory(new PropertyValueFactory<>("assists"));
        scoutRatingColumn.setCellValueFactory(new PropertyValueFactory<>("rating"));

        // Setup shortlist table
        shortlistNameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        shortlistPositionColumn.setCellValueFactory(new PropertyValueFactory<>("position"));
        shortlistClubColumn.setCellValueFactory(new PropertyValueFactory<>("club"));
        shortlistValueColumn.setCellValueFactory(data -> {
            SimpleDoubleProperty property = new SimpleDoubleProperty(data.getValue().getValue());
            return property.asObject();
        });
        shortlistValueColumn.setCellFactory(column -> new TableCell<>() {
            @Override
            protected void updateItem(Double value, boolean empty) {
                super.updateItem(value, empty);
                if (empty || value == null) {
                    setText(null);
                } else {
                    setText(currencyFormat.format(value * 1_000_000));
                }
            }
        });
        shortlistRatingColumn.setCellValueFactory(new PropertyValueFactory<>("rating"));
        shortlistNotesColumn.setCellValueFactory(new PropertyValueFactory<>("notes"));

        // Setup squad needs
        squadNeedComboBox.setItems(FXCollections.observableArrayList(
                "Goalkeeper", "Right Back", "Center Back", "Left Back",
                "Defensive Midfielder", "Central Midfielder", "Attacking Midfielder",
                "Right Winger", "Left Winger", "Striker"
        ));

        // Setup selection listener for scouted players
        scoutedPlayersTable.getSelectionModel().selectedItemProperty().addListener(
                (obs, oldVal, newVal) -> {
                    if (newVal != null) {
                        selectedPlayerLabel.setText(newVal.getName() + " (" + newVal.getPosition() + ", " + newVal.getClub() + ")");
                        playerNotesArea.setText(newVal.getNotes());
                    } else {
                        selectedPlayerLabel.setText("No player selected");
                        playerNotesArea.clear();
                    }
                });

        // Setup filter listeners
        positionFilterComboBox.getSelectionModel().selectedItemProperty().addListener(
                (obs, oldVal, newVal) -> filterPlayers());

        searchField.textProperty().addListener((obs, oldVal, newVal) -> filterPlayers());

        statSortComboBox.getSelectionModel().selectedItemProperty().addListener(
                (obs, oldVal, newVal) -> sortPlayers());

        // Load sample data
        loadSampleData();

        // Set tables
        scoutedPlayersTable.setItems(filteredPlayers);
        shortlistTable.setItems(shortlistedPlayers);
        squadNeedsListView.setItems(squadNeeds);

        // Add initial filter
        filterPlayers();
    }

    private void loadSampleData() {
        // Add sample scouted players
        scoutedPlayers.addAll(
                new ScoutedPlayer("Kylian Mbappé", 24, "Forward", "PSG", 180.0, 92, 95, 85, 28, 17, "A+", ""),
                new ScoutedPlayer("Erling Haaland", 22, "Forward", "Man City", 170.0, 90, 94, 84, 36, 8, "A+", ""),
                new ScoutedPlayer("Phil Foden", 22, "Midfielder", "Man City", 110.0, 88, 82, 86, 11, 9, "A", ""),
                new ScoutedPlayer("Jude Bellingham", 19, "Midfielder", "Dortmund", 100.0, 85, 83, 88, 7, 13, "A", ""),
                new ScoutedPlayer("Jamal Musiala", 20, "Midfielder", "Bayern", 90.0, 89, 84, 85, 12, 10, "A", ""),
                new ScoutedPlayer("Alphonso Davies", 22, "Defender", "Bayern", 75.0, 84, 96, 82, 2, 9, "A-", ""),
                new ScoutedPlayer("Florian Wirtz", 20, "Midfielder", "Leverkusen", 85.0, 86, 79, 84, 10, 15, "A-", ""),
                new ScoutedPlayer("Eduardo Camavinga", 20, "Midfielder", "Real Madrid", 60.0, 82, 85, 83, 3, 5, "B+", ""),
                new ScoutedPlayer("Gavi", 18, "Midfielder", "Barcelona", 70.0, 84, 78, 87, 4, 7, "B+", ""),
                new ScoutedPlayer("Nico Williams", 20, "Forward", "Athletic Bilbao", 50.0, 83, 90, 79, 8, 6, "B+", ""),
                new ScoutedPlayer("Joško Gvardiol", 21, "Defender", "RB Leipzig", 80.0, 81, 85, 84, 1, 2, "A-", ""),
                new ScoutedPlayer("Youssoufa Moukoko", 18, "Forward", "Dortmund", 30.0, 79, 83, 76, 9, 3, "B", ""),
                new ScoutedPlayer("Xavi Simons", 20, "Midfielder", "PSV", 30.0, 81, 82, 80, 15, 11, "B", ""),
                new ScoutedPlayer("Hugo Larsson", 19, "Midfielder", "Eintracht Frankfurt", 25.0, 78, 80, 79, 4, 7, "B-", ""),
                new ScoutedPlayer("Warren Zaïre-Emery", 17, "Midfielder", "PSG", 35.0, 79, 82, 81, 3, 6, "B", ""),
                new ScoutedPlayer("Endrick", 17, "Forward", "Palmeiras", 60.0, 77, 83, 75, 12, 4, "B+", ""),
                new ScoutedPlayer("Mathys Tel", 18, "Forward", "Bayern", 25.0, 76, 80, 75, 7, 3, "B-", ""),
                new ScoutedPlayer("Karim Adeyemi", 21, "Forward", "Dortmund", 35.0, 82, 94, 76, 9, 6, "B", ""),
                new ScoutedPlayer("Benjamin Šeško", 20, "Forward", "RB Leipzig", 40.0, 79, 86, 77, 14, 5, "B", ""),
                new ScoutedPlayer("Giorgio Scalvini", 19, "Defender", "Atalanta", 40.0, 80, 79, 81, 2, 1, "B", ""),
                new ScoutedPlayer("Mike Maignan", 28, "Goalkeeper", "Milan", 45.0, 85, 82, 87, 0, 0, "A-", ""),
                new ScoutedPlayer("Andreas Skov Olsen", 23, "Forward", "Club Brugge", 20.0, 80, 78, 79, 11, 8, "B-", ""),
                new ScoutedPlayer("Viktor Gyökeres", 25, "Forward", "Sporting", 42.0, 83, 85, 82, 22, 7, "B+", ""),
                new ScoutedPlayer("Leny Yoro", 17, "Defender", "Lille", 45.0, 78, 79, 76, 1, 0, "B", "")
        );

        // Add sample shortlisted players
        shortlistedPlayers.add(new ScoutedPlayer("Jude Bellingham", 19, "Midfielder", "Dortmund", 100.0, 85, 83, 88, 7, 13, "A",
                "Perfect box-to-box midfielder. Great leadership qualities despite his age. Primary target for summer window."));
        shortlistedPlayers.add(new ScoutedPlayer("Nico Williams", 20, "Forward", "Athletic Bilbao", 50.0, 83, 90, 79, 8, 6, "B+",
                "Lightning quick winger with good end product. Buyout clause makes him attainable. Should be contacted soon."));

        // Add sample squad needs
        squadNeeds.addAll(
                "Central Midfielder - Box-to-Box Profile",
                "Right Winger - Pace and Dribbling",
                "Striker - Target Man Profile"
        );
    }

    private void filterPlayers() {
        filteredPlayers.clear();

        String searchText = searchField.getText().toLowerCase();
        String positionFilter = positionFilterComboBox.getValue();
        int minAge = (int) ageFromSlider.getValue();
        int maxAge = (int) ageToSlider.getValue();
        double minValue = valueFromSlider.getValue();
        double maxValue = valueToSlider.getValue();

        for (ScoutedPlayer player : scoutedPlayers) {
            // Apply position filter
            if (!"All Positions".equals(positionFilter) && !player.getPosition().contains(positionFilter)) {
                continue;
            }

            // Apply age filter
            if (player.getAge() < minAge || player.getAge() > maxAge) {
                continue;
            }

            // Apply value filter
            if (player.getValue() < minValue || player.getValue() > maxValue) {
                continue;
            }

            // Apply text search
            if (!searchText.isEmpty() &&
                    !player.getName().toLowerCase().contains(searchText) &&
                    !player.getClub().toLowerCase().contains(searchText) &&
                    !player.getPosition().toLowerCase().contains(searchText)) {
                continue;
            }

            filteredPlayers.add(player);
        }

        sortPlayers();
    }

    private void sortPlayers() {
        String sortCriteria = statSortComboBox.getValue();

        switch (sortCriteria) {
            case "Market Value":
                filteredPlayers.sort(Comparator.comparing(ScoutedPlayer::getValue).reversed());
                break;
            case "Technical":
                filteredPlayers.sort(Comparator.comparing(ScoutedPlayer::getTechnical).reversed());
                break;
            case "Physical":
                filteredPlayers.sort(Comparator.comparing(ScoutedPlayer::getPhysical).reversed());
                break;
            case "Mental":
                filteredPlayers.sort(Comparator.comparing(ScoutedPlayer::getMental).reversed());
                break;
            case "Goals":
                filteredPlayers.sort(Comparator.comparing(ScoutedPlayer::getGoals).reversed());
                break;
            case "Assists":
                filteredPlayers.sort(Comparator.comparing(ScoutedPlayer::getAssists).reversed());
                break;
            case "Overall Rating":
            default:
                filteredPlayers.sort((p1, p2) -> {
                    // Convert string ratings to numerical values for sorting
                    return getRatingValue(p2.getRating()) - getRatingValue(p1.getRating());
                });
                break;
        }
    }

    private int getRatingValue(String rating) {
        switch (rating) {
            case "A+": return 10;
            case "A": return 9;
            case "A-": return 8;
            case "B+": return 7;
            case "B": return 6;
            case "B-": return 5;
            case "C+": return 4;
            case "C": return 3;
            case "C-": return 2;
            case "D": return 1;
            default: return 0;
        }
    }

    @FXML
    public void saveNotes() {
        ScoutedPlayer selectedPlayer = scoutedPlayersTable.getSelectionModel().getSelectedItem();
        if (selectedPlayer == null) {
            showAlert("Error", "No player selected", Alert.AlertType.ERROR);
            return;
        }

        selectedPlayer.setNotes(playerNotesArea.getText());
        showAlert("Success", "Notes saved for " + selectedPlayer.getName(), Alert.AlertType.INFORMATION);
    }

    @FXML
    public void addToShortlist() {
        ScoutedPlayer selectedPlayer = scoutedPlayersTable.getSelectionModel().getSelectedItem();
        if (selectedPlayer == null) {
            showAlert("Error", "No player selected to add to shortlist", Alert.AlertType.ERROR);
            return;
        }

        // Check if player already in shortlist
        for (ScoutedPlayer shortlistedPlayer : shortlistedPlayers) {
            if (shortlistedPlayer.getName().equals(selectedPlayer.getName())) {
                showAlert("Information", selectedPlayer.getName() + " is already in the shortlist", Alert.AlertType.INFORMATION);
                return;
            }
        }

        // Add to shortlist
        shortlistedPlayers.add(selectedPlayer);
        showAlert("Success", selectedPlayer.getName() + " added to shortlist", Alert.AlertType.INFORMATION);
    }

    @FXML
    public void removeFromShortlist() {
        ScoutedPlayer selectedPlayer = shortlistTable.getSelectionModel().getSelectedItem();
        if (selectedPlayer == null) {
            showAlert("Error", "No player selected to remove", Alert.AlertType.ERROR);
            return;
        }

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirm Removal");
        alert.setHeaderText("Remove from Shortlist");
        alert.setContentText("Are you sure you want to remove " + selectedPlayer.getName() + " from the shortlist?");

        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            shortlistedPlayers.remove(selectedPlayer);
        }
    }

    @FXML
    public void addSquadNeed() {
        String selectedNeed = squadNeedComboBox.getValue();
        if (selectedNeed == null || selectedNeed.isEmpty()) {
            showAlert("Error", "Please select a position need", Alert.AlertType.ERROR);
            return;
        }

        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Add Squad Need");
        dialog.setHeaderText("Specify player profile needed");
        dialog.setContentText("Enter details for " + selectedNeed + " need:");

        Optional<String> result = dialog.showAndWait();
        if (result.isPresent() && !result.get().trim().isEmpty()) {
            String needDetail = selectedNeed + " - " + result.get().trim();
            squadNeeds.add(needDetail);
        }
    }

    @FXML
    public void removeSquadNeed() {
        String selectedNeed = squadNeedsListView.getSelectionModel().getSelectedItem();
        if (selectedNeed == null) {
            showAlert("Error", "No squad need selected to remove", Alert.AlertType.ERROR);
            return;
        }

        squadNeeds.remove(selectedNeed);
    }

    @FXML
    public void generateReport() {
        if (shortlistedPlayers.isEmpty()) {
            showAlert("Error", "No players in shortlist to generate report", Alert.AlertType.ERROR);
            return;
        }

        StringBuilder report = new StringBuilder();
        report.append("SCOUTING REPORT - TARGET PLAYERS\n\n");
        report.append("Squad Needs:\n");

        for (String need : squadNeeds) {
            report.append("- ").append(need).append("\n");
        }

        report.append("\nShortlisted Players (").append(shortlistedPlayers.size()).append("):\n\n");

        for (ScoutedPlayer player : shortlistedPlayers) {
            report.append("Name: ").append(player.getName()).append("\n");
            report.append("Position: ").append(player.getPosition()).append("\n");
            report.append("Age: ").append(player.getAge()).append("\n");
            report.append("Club: ").append(player.getClub()).append("\n");
            report.append("Value: ").append(currencyFormat.format(player.getValue() * 1_000_000)).append("\n");
            report.append("Scout Rating: ").append(player.getRating()).append("\n");
            if (player.getNotes() != null && !player.getNotes().isEmpty()) {
                report.append("Notes: ").append(player.getNotes()).append("\n");
            }
            report.append("\n");
        }

        TextArea reportArea = new TextArea(report.toString());
        reportArea.setEditable(false);
        reportArea.setWrapText(true);
        reportArea.setPrefWidth(500);
        reportArea.setPrefHeight(400);

        Dialog<Void> dialog = new Dialog<>();
        dialog.setTitle("Scouting Report");
        dialog.setHeaderText("Generated Scouting Report");
        dialog.getDialogPane().setContent(reportArea);
        dialog.getDialogPane().getButtonTypes().add(ButtonType.OK);
        dialog.showAndWait();
    }

    private void showAlert(String title, String message, Alert.AlertType type) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    @FXML
    public void goal7Dashboard(ActionEvent actionEvent) throws IOException {
        SceneSwitcher.switchTo("DashboardTransferWindowManager.fxml", actionEvent);
    }

    // Model class for scouted players
    public static class ScoutedPlayer {
        private final String name;
        private final int age;
        private final String position;
        private final String club;
        private final double value; // in millions
        private final int technical;
        private final int physical;
        private final int mental;
        private final int goals;
        private final int assists;
        private final String rating;
        private String notes;

        public ScoutedPlayer(String name, int age, String position, String club,
                             double value, int technical, int physical, int mental,
                             int goals, int assists, String rating, String notes) {
            this.name = name;
            this.age = age;
            this.position = position;
            this.club = club;
            this.value = value;
            this.technical = technical;
            this.physical = physical;
            this.mental = mental;
            this.goals = goals;
            this.assists = assists;
            this.rating = rating;
            this.notes = notes;
        }

        public String getName() { return name; }
        public int getAge() { return age; }
        public String getPosition() { return position; }
        public String getClub() { return club; }
        public double getValue() { return value; }
        public int getTechnical() { return technical; }
        public int getPhysical() { return physical; }
        public int getMental() { return mental; }
        public int getGoals() { return goals; }
        public int getAssists() { return assists; }
        public String getRating() { return rating; }
        public String getNotes() { return notes; }
        public void setNotes(String notes) { this.notes = notes; }
    }
}