// FanDashboardController.java
package cse213.cse213_sporting_club_operations.SilmaSubha;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class FanDashboardController {

    @FXML private TableView<Ticket> homeTable;
    @FXML private TableColumn<Ticket, String> homeNameCol;
    @FXML private TableColumn<Ticket, String> homeMatchCol;
    @FXML private TableColumn<Ticket, String> homeVenueCol;
    @FXML private TableColumn<Ticket, String> homeDateCol;
    @FXML private TableColumn<Ticket, String> homeTypeCol;
    @FXML private TableColumn<Ticket, String> homePriceCol;

    @FXML private TextField nameField;
    @FXML private ComboBox<String> matchBox;
    @FXML private ComboBox<String> dateBox;
    @FXML private ComboBox<String> venueBox;
    @FXML private ComboBox<String> ticketTypeBox;
    @FXML private ComboBox<String> priceBox;
    //update

    private final ObservableList<Ticket> ticketList = FXCollections.observableArrayList();
    private final String DATA_FILE = "tickets.bin";

    @FXML
    public void initialize() {
        // Initialize ComboBoxes with sample data
        matchBox.setItems(FXCollections.observableArrayList("Match 1", "Match 2", "Match 3"));
        dateBox.setItems(FXCollections.observableArrayList("2025-04-20", "2025-04-21", "2025-04-22"));
        venueBox.setItems(FXCollections.observableArrayList("Stadium A", "Stadium B", "Stadium C"));
        ticketTypeBox.setItems(FXCollections.observableArrayList("Regular", "VIP", "VVIP"));
        priceBox.setItems(FXCollections.observableArrayList("500", "1000", "1500"));

        // Set up TableView columns
        homeNameCol.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getName()));
        homeMatchCol.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getMatch()));
        homeVenueCol.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getVenue()));
        homeDateCol.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getDate()));
        homeTypeCol.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getTicketType()));
        homePriceCol.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getPrice()));

        // Load existing tickets from file
        loadTicketsFromFile();
        homeTable.setItems(ticketList);
    }

    @FXML
    private void handlePayment() {
        String name = nameField.getText();
        String match = matchBox.getValue();
        String date = dateBox.getValue();
        String venue = venueBox.getValue();
        String ticketType = ticketTypeBox.getValue();
        String price = priceBox.getValue();

        if (name == null || match == null || date == null || venue == null || ticketType == null || price == null) {
            showAlert("Please fill in all fields.");
            return;
        }

        Ticket ticket = new Ticket(name, match, venue, date, ticketType, price);
        ticketList.add(ticket);
        saveTicketsToFile();
        clearForm();
    }

    private void loadTicketsFromFile() {
        File file = new File(DATA_FILE);
        if (!file.exists()) return;

        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
            List<Ticket> tickets = (List<Ticket>) ois.readObject();
            ticketList.addAll(tickets);
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    private void saveTicketsToFile() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(DATA_FILE))) {
            oos.writeObject(new ArrayList<>(ticketList));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void clearForm() {
        nameField.clear();
        matchBox.getSelectionModel().clearSelection();
        dateBox.getSelectionModel().clearSelection();
        venueBox.getSelectionModel().clearSelection();
        ticketTypeBox.getSelectionModel().clearSelection();
        priceBox.getSelectionModel().clearSelection();
    }

    private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Input Validation");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    @FXML
    private void handleLogout() {
        System.out.println("Logout button clicked.");
    }
}
