package cse213.cse213_sporting_club_operations.TanvirMahmud;

import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class AllPlayers_Controller {
    public static Player playerToEdit = null;
    static List<Player> playerList = new ArrayList<>();

    @javafx.fxml.FXML
    private TableColumn<Player, String> nameTC;
    @javafx.fxml.FXML
    private TableColumn<Player, String> positionTC;
    @javafx.fxml.FXML
    private TableColumn<Player, String> ageTC;
    @javafx.fxml.FXML
    private TableView<Player> table;
    @javafx.fxml.FXML
    private TableColumn<Player, String> fitTC;
    @javafx.fxml.FXML
    private TableColumn<Player, String> valueTC;
    @javafx.fxml.FXML
    private TextField nameTF;
    @javafx.fxml.FXML
    private TextField ageTF;
    @javafx.fxml.FXML
    private TextField positionTF;
    @javafx.fxml.FXML
    private TextField valueTF;
    @javafx.fxml.FXML
    private TextField fitTF;
    @javafx.fxml.FXML
    private Label messageLabel;

    @javafx.fxml.FXML
    public void initialize() {
        nameTC.setCellValueFactory(new PropertyValueFactory<>("name"));
        positionTC.setCellValueFactory(new PropertyValueFactory<>("position"));
        ageTC.setCellValueFactory(new PropertyValueFactory<>("age"));
        fitTC.setCellValueFactory(new PropertyValueFactory<>("fit"));
        valueTC.setCellValueFactory(new PropertyValueFactory<>("marketValue"));

        table.setItems(FXCollections.observableArrayList(playerList));
    }

    private void readFromFile() {
        try (ObjectInputStream inputStream = new ObjectInputStream(new FileInputStream("data.bin"))) {
            Player p = (Player) inputStream.readObject();
            messageLabel.setText(p.toString());
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            messageLabel.setText("Invalid file format!");
        } catch (IOException e) {
            e.printStackTrace();
            messageLabel.setText("Could not read from file");
        }
    }

    @javafx.fxml.FXML
    public void goal1Dashboard(ActionEvent actionEvent) throws IOException {
        SceneSwitcher.switchTo("DashboardTransferWindowManager.fxml", actionEvent);
    }

    @javafx.fxml.FXML
    public void load(ActionEvent actionEvent) {
        try {
            // Create data directory if it doesn't exist
            Files.createDirectories(Path.of("data"));

            try (ObjectInputStream inputStream = new ObjectInputStream(new FileInputStream("data/user.bin"))) {
                playerList.clear();
                table.getItems().clear();

                List<Player> loadedList = (List<Player>) inputStream.readObject();
                playerList.addAll(loadedList);
                table.setItems(FXCollections.observableArrayList(playerList));

                messageLabel.setText("Successfully loaded data");
                //practice
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            messageLabel.setText("Invalid file format");
        } catch (IOException e) {
            e.printStackTrace();
            messageLabel.setText("Could not load data from file!");
        }
    }

    @javafx.fxml.FXML
    public void addPlayer(ActionEvent actionEvent) {
        String name = nameTF.getText();
        String age = ageTF.getText();
        String position = positionTF.getText();
        String fit = fitTF.getText();
        String marketValue = valueTF.getText();

        if (name.isBlank() || age.isBlank() || position.isBlank() || fit.isBlank() || marketValue.isBlank()) {
            messageLabel.setText("Please provide all inputs");
            return;
        }

        for (Player u : playerList) {
            if (u.getName().equals(name)) {
                messageLabel.setText("This player name already exists!");
                return;
            }
        }

        Player player = new Player(name, age, position, fit, marketValue);
        playerList.add(player);
        table.getItems().add(player);
        messageLabel.setText("Player added successfully");

        nameTF.setText("");
        ageTF.setText("");
        positionTF.setText("");
        fitTF.setText("");
        valueTF.setText("");
    }

    public void setMessage(String message) {
        messageLabel.setText(message);
    }

    @javafx.fxml.FXML
    public void save(ActionEvent actionEvent) {
        try {
            // Create data directory if it doesn't exist
            Files.createDirectories(Path.of("data"));

            try (ObjectOutputStream outputStream = new ObjectOutputStream(new FileOutputStream("data/user.bin"))) {
                outputStream.writeObject(playerList);
                messageLabel.setText("Successfully saved to file.");
            }
        } catch (IOException e) {
            e.printStackTrace();
            messageLabel.setText("Could not write to file");
        }
    }

    @javafx.fxml.FXML
    public void logOut(ActionEvent actionEvent) throws IOException {
        SceneSwitcher.switchTo("login.fxml", actionEvent);
    }
}