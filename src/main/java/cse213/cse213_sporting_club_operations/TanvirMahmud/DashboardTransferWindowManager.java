package cse213.cse213_sporting_club_operations.TanvirMahmud;

import javafx.event.ActionEvent;

import java.io.IOException;

public class DashboardTransferWindowManager
{
    @javafx.fxml.FXML
    public void initialize() {
    }

    @javafx.fxml.FXML
    public void goal2(ActionEvent actionEvent) throws IOException{
        SceneSwitcher.switchTo("Goal2.fxml", actionEvent);
    }

    @javafx.fxml.FXML
    public void goal1(ActionEvent actionEvent) throws IOException{
        SceneSwitcher.switchTo("goal1_AllPlayers.fxml", actionEvent);
    }

    @javafx.fxml.FXML
    public void goal8(ActionEvent actionEvent) throws IOException{
        SceneSwitcher.switchTo("Goal8.fxml", actionEvent);
    }

    @javafx.fxml.FXML
    public void goal7(ActionEvent actionEvent) throws IOException{
        SceneSwitcher.switchTo("Goal7.fxml",actionEvent);
    }

    @javafx.fxml.FXML
    public void goal6(ActionEvent actionEvent) throws IOException{
        SceneSwitcher.switchTo("Goal6.fxml", actionEvent);
    }

    @javafx.fxml.FXML
    public void goal5(ActionEvent actionEvent) throws IOException{
        SceneSwitcher.switchTo("Goal5.fxml", actionEvent);
    }

    @javafx.fxml.FXML
    public void goal4(ActionEvent actionEvent) throws IOException{
        SceneSwitcher.switchTo("Goal4.fxml", actionEvent);
    }

    @javafx.fxml.FXML
    public void goal3(ActionEvent actionEvent) throws IOException{
        SceneSwitcher.switchTo("Goal3.fxml", actionEvent);
    }

    @javafx.fxml.FXML
    public void signout(ActionEvent actionEvent) throws IOException {
        SceneSwitcher.switchTo("login.fxml", actionEvent);
    }
}