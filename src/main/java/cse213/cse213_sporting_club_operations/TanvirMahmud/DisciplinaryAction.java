package cse213.cse213_sporting_club_operations.TanvirMahmud;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public class DisciplinaryAction {
    private final String playerName;
    private final LocalDate incidentDate;
    private final String incidentType;
    private final String actionType;
    private final LocalDate startDate;
    private final LocalDate endDate;
    private final double fineAmount;
    private final String description;
    private final boolean leagueReported;
    private final String location;

    public DisciplinaryAction(String playerName, LocalDate incidentDate, String incidentType,
                              String actionType, LocalDate startDate, LocalDate endDate,
                              double fineAmount, String description, boolean leagueReported, String location) {
        this.playerName = playerName;
        this.incidentDate = incidentDate;
        this.incidentType = incidentType;
        this.actionType = actionType;
        this.startDate = startDate;
        this.endDate = endDate;
        this.fineAmount = fineAmount;
        this.description = description;
        this.leagueReported = leagueReported;
        this.location = location != null && !location.isEmpty() ? location : "Training Ground";
    }

    public String getPlayerName() { return playerName; }
    public LocalDate getIncidentDate() { return incidentDate; }
    public String getIncidentType() { return incidentType; }
    public String getActionType() { return actionType; }
    public LocalDate getStartDate() { return startDate; }
    public LocalDate getEndDate() { return endDate; }
    public double getFineAmount() { return fineAmount; }
    public String getDescription() { return description; }
    public boolean isLeagueReported() { return leagueReported; }
    public String getLocation() { return location; }

    public String getDuration() {
        if (startDate == null || endDate == null) {
            return "N/A";
        }
        long days = ChronoUnit.DAYS.between(startDate, endDate) + 1;
        return days + " days";
    }

    public String getStatus() {
        if (actionType.equals("Verbal Warning") || actionType.equals("Written Warning")) {
            return "Completed";
        }

        if (actionType.equals("Fine")) {
            return "Pending";
        }

        if (actionType.equals("Suspension")) {
            if (endDate.isBefore(LocalDate.now())) {
                return "Completed";
            } else if (startDate.isAfter(LocalDate.now())) {
                return "Pending";
            } else {
                return "Active";
            }
        }

        return "Pending";
    }
}