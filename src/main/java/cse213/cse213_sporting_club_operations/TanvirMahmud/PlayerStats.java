package cse213.cse213_sporting_club_operations.TanvirMahmud;

import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

public class PlayerStats {
    private final SimpleStringProperty playerName;
    private final SimpleStringProperty position;
    private final SimpleDoubleProperty matchRating;
    private final SimpleIntegerProperty fatigue;
    private final SimpleStringProperty injuryStatus;
    private final SimpleIntegerProperty trainingProgress;

    public PlayerStats(String playerName, String position, double matchRating,
                       int fatigue, String injuryStatus, int trainingProgress) {
        this.playerName = new SimpleStringProperty(playerName);
        this.position = new SimpleStringProperty(position);
        this.matchRating = new SimpleDoubleProperty(matchRating);
        this.fatigue = new SimpleIntegerProperty(fatigue);
        this.injuryStatus = new SimpleStringProperty(injuryStatus);
        this.trainingProgress = new SimpleIntegerProperty(trainingProgress);
    }

    public String getPlayerName() { return playerName.get(); }
    public String getPosition() { return position.get(); }
    public double getMatchRating() { return matchRating.get(); }
    public int getFatigue() { return fatigue.get(); }
    public String getInjuryStatus() { return injuryStatus.get(); }
    public int getTrainingProgress() { return trainingProgress.get(); }

    public void setTrainingProgress(int progress) {
        this.trainingProgress.set(Math.min(100, progress));
    }
}