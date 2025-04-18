package cse213.cse213_sporting_club_operations.TanvirMahmud;

import javafx.event.ActionEvent;

import java.io.IOException;

public class DashboardHeadCoach
{
    @javafx.fxml.FXML
    public void initialize() {
    }

    @javafx.fxml.FXML
    public void goal2(ActionEvent actionEvent) throws IOException {
        SceneSwitcher.switchTo("HCGoal2.fxml", actionEvent);
    }

    @javafx.fxml.FXML
    public void goal1(ActionEvent actionEvent) throws IOException{
        SceneSwitcher.switchTo("HCGoal1.fxml", actionEvent);
    }

    @javafx.fxml.FXML
    public void goal8(ActionEvent actionEvent) throws IOException {
        SceneSwitcher.switchTo("HCGoal8.fxml", actionEvent);
    }

    @javafx.fxml.FXML
    public void goal7(ActionEvent actionEvent) throws IOException {
        SceneSwitcher.switchTo("HCGoal7.fxml", actionEvent);
    }

    @javafx.fxml.FXML
    public void goal6(ActionEvent actionEvent) throws IOException {
        SceneSwitcher.switchTo("HCGoal6.fxml", actionEvent);
    }

    @javafx.fxml.FXML
    public void goal5(ActionEvent actionEvent) throws IOException {
        SceneSwitcher.switchTo("HCGoal5.fxml", actionEvent);
    }

    @javafx.fxml.FXML
    public void goal4(ActionEvent actionEvent) throws IOException{
        SceneSwitcher.switchTo("HCGoal4.fxml", actionEvent);
    }

    @javafx.fxml.FXML
    public void goal3(ActionEvent actionEvent) throws IOException {
        SceneSwitcher.switchTo("HCGoal3.fxml", actionEvent);
    }

    @javafx.fxml.FXML
    public void signoutHC(ActionEvent actionEvent) throws IOException {
        SceneSwitcher.switchTo("login.fxml", actionEvent);
    }
}