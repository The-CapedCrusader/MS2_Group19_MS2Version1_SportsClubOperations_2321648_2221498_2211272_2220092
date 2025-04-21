package cse213.cse213_sporting_club_operations.TanvirMahmud;

import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.List;

public class Goal2 {
    @javafx.fxml.FXML
    private TableColumn<Player, String> acTC;
    @javafx.fxml.FXML
    private TableColumn<Player, String> rejTC;
    @javafx.fxml.FXML
    private TableView<Player> table;

    @javafx.fxml.FXML
    public void initialize() {

        acTC.setCellValueFactory(new PropertyValueFactory<>("name"));
        rejTC.setCellValueFactory(new PropertyValueFactory<>("name"));

        loadAndCategorizePlayerData();
    }

    private void loadAndCategorizePlayerData() {
        List<Player> allPlayers = new ArrayList<>();


        if (!Goal1_AllPlayers.playerList.isEmpty()) {
            allPlayers.addAll(Goal1_AllPlayers.playerList);
        } else {
            // If that's empty, try to load from file
            try (ObjectInputStream inputStream = new ObjectInputStream(new FileInputStream("data/user.bin"))) {
                List<Player> loadedList = (List<Player>) inputStream.readObject();
                allPlayers.addAll(loadedList);
            } catch (IOException | ClassNotFoundException e) {
                System.out.println("Could not load player data: " + e.getMessage());
            }
        }

        List<Player> acceptedPlayers = new ArrayList<>();
        List<Player> rejectedPlayers = new ArrayList<>();

        // Categorize players based on fitness level
        for (Player player : allPlayers) {
            try {
                // Remove % if present and trim whitespace
                String fitValue = player.getFit().replace("%", "").trim();
                int fitnessLevel = Integer.parseInt(fitValue);

                // Players with fitness > 80% are accepted
                if (fitnessLevel > 80) {
                    acceptedPlayers.add(player);
                } else {
                    rejectedPlayers.add(player);
                }
            } catch (NumberFormatException e) {
                // If we can't parse the fitness value, reject the player
                rejectedPlayers.add(player);
            }
        }

        // Use observable list for players based on fit value
        List<Player> displayPlayers = new ArrayList<>();
        displayPlayers.addAll(acceptedPlayers);
        displayPlayers.addAll(rejectedPlayers);

        table.setItems(FXCollections.observableArrayList(displayPlayers));
    }

    @javafx.fxml.FXML
    public void goal2Dashboard(ActionEvent actionEvent) throws IOException {
        SceneSwitcher.switchTo("DashboardTransferWindowManager.fxml", actionEvent);
    }

    @javafx.fxml.FXML
    public void refreshData(ActionEvent actionEvent) {
        loadAndCategorizePlayerData();
    }
}