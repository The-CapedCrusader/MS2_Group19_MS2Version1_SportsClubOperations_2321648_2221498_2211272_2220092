package cse213.cse213_sporting_club_operations.TanvirMahmud;

import java.time.LocalDate;


public class TerminatedContract {
    private String playerName;
    private String team;
    private String terminationType;
    private LocalDate terminationDate;
    private String reason;
    private boolean compliant;

    public TerminatedContract(String playerName, String team, String terminationType, LocalDate terminationDate, String reason, boolean compliant) {
        this.playerName = playerName;
        this.team = team;
        this.terminationType = terminationType;
        this.terminationDate = terminationDate;
        this.reason = reason;
        this.compliant = compliant;
    }

    public String getPlayerName() {
        return playerName;
    }

    public void setPlayerName(String playerName) {
        this.playerName = playerName;
    }

    public String getTeam() {
        return team;
    }

    public void setTeam(String team) {
        this.team = team;
    }

    public String getTerminationType() {
        return terminationType;
    }

    public void setTerminationType(String terminationType) {
        this.terminationType = terminationType;
    }

    public LocalDate getTerminationDate() {
        return terminationDate;
    }

    public void setTerminationDate(LocalDate terminationDate) {
        this.terminationDate = terminationDate;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public boolean isCompliant() {
        return compliant;
    }

    public void setCompliant(boolean compliant) {
        this.compliant = compliant;
    }
}