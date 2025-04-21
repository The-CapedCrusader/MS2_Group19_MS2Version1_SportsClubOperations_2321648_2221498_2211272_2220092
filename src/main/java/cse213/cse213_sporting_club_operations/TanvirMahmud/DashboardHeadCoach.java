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
        SceneSwitcher.switchTo("TrainingSessionScheduler.fxml", actionEvent);
    }

    @javafx.fxml.FXML
    public void goal1(ActionEvent actionEvent) throws IOException{
        SceneSwitcher.switchTo("TeamLineUP.fxml", actionEvent);
    }

    @javafx.fxml.FXML
    public void goal8(ActionEvent actionEvent) throws IOException {
        SceneSwitcher.switchTo("YouthAcademy.fxml", actionEvent);
    }

    @javafx.fxml.FXML
    public void goal7(ActionEvent actionEvent) throws IOException {
        SceneSwitcher.switchTo("Disciplinary.fxml", actionEvent);
    }

    @javafx.fxml.FXML
    public void goal6(ActionEvent actionEvent) throws IOException {
        SceneSwitcher.switchTo("MeetingManagement.fxml", actionEvent);
    }

    @javafx.fxml.FXML
    public void goal5(ActionEvent actionEvent) throws IOException {
        SceneSwitcher.switchTo("RoleAssignment.fxml", actionEvent);
    }

    @javafx.fxml.FXML
    public void goal4(ActionEvent actionEvent) throws IOException{
        SceneSwitcher.switchTo("PerformanceAssessnment.fxml", actionEvent);
    }

    @javafx.fxml.FXML
    public void goal3(ActionEvent actionEvent) throws IOException {
        SceneSwitcher.switchTo("TacticalPlanning.fxml", actionEvent);
    }

    @javafx.fxml.FXML
    public void signoutHC(ActionEvent actionEvent) throws IOException {
        SceneSwitcher.switchTo("login.fxml", actionEvent);
    }
}