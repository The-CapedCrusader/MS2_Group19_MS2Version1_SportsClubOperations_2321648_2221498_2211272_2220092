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
        SceneSwitcher.switchTo("PlayerEvaluation.fxml", actionEvent);
    }

    @javafx.fxml.FXML
    public void goal1(ActionEvent actionEvent) throws IOException{
        SceneSwitcher.switchTo("AllPlayers.fxml", actionEvent);
    }

    @javafx.fxml.FXML
    public void goal8(ActionEvent actionEvent) throws IOException{
        SceneSwitcher.switchTo("PlayerTransfer.fxml", actionEvent);
    }

    @javafx.fxml.FXML
    public void goal7(ActionEvent actionEvent) throws IOException{
        SceneSwitcher.switchTo("PlayerScouting.fxml",actionEvent);
    }

    @javafx.fxml.FXML
    public void goal6(ActionEvent actionEvent) throws IOException{
        SceneSwitcher.switchTo("LocalDeal.fxml", actionEvent);
    }

    @javafx.fxml.FXML
    public void goal5(ActionEvent actionEvent) throws IOException{
        SceneSwitcher.switchTo("FinancialManagement.fxml", actionEvent);
    }

    @javafx.fxml.FXML
    public void goal4(ActionEvent actionEvent) throws IOException{
        SceneSwitcher.switchTo("ContractTermination.fxml", actionEvent);
    }

    @javafx.fxml.FXML
    public void goal3(ActionEvent actionEvent) throws IOException{
        SceneSwitcher.switchTo("CreateContract.fxml", actionEvent);
    }

    @javafx.fxml.FXML
    public void signout(ActionEvent actionEvent) throws IOException {
        SceneSwitcher.switchTo("login.fxml", actionEvent);
    }
}