package cse213.cse213_sporting_club_operations.TanvirMahmud;

import java.time.LocalDate;

public class TransferRecord {
    private  String playerName;
    private String transferType;
    private  String destinationClub;
    private  Double transferFee;
    private LocalDate transferDate;
    private String status;
    private boolean medicalClearance;
    private boolean financialSettlement;
    private boolean contractTermination;
    private boolean playerConsent;
    private boolean transferDocuments;
    private boolean leagueNotification;

    public TransferRecord(String playerName, String transferType, String destinationClub,
                          Double transferFee, LocalDate transferDate, String status,
                          boolean medicalClearance, boolean financialSettlement,
                          boolean contractTermination, boolean playerConsent,
                          boolean transferDocuments, boolean leagueNotification) {
        this.playerName = playerName;
        this.transferType = transferType;
        this.destinationClub = destinationClub;
        this.transferFee = transferFee;
        this.transferDate = transferDate;
        this.status = status;
        this.medicalClearance = medicalClearance;
        this.financialSettlement = financialSettlement;
        this.contractTermination = contractTermination;
        this.playerConsent = playerConsent;
        this.transferDocuments = transferDocuments;
        this.leagueNotification = leagueNotification;
    }


    public String getPlayerName() { return playerName; }
    public String getTransferType() { return transferType; }
    public String getDestinationClub() { return destinationClub; }
    public Double getTransferFee() { return transferFee; }
    public LocalDate getTransferDate() { return transferDate; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public boolean isMedicalClearance() { return medicalClearance; }
    public void setMedicalClearance(boolean medicalClearance) { this.medicalClearance = medicalClearance; }

    public boolean isFinancialSettlement() { return financialSettlement; }
    public void setFinancialSettlement(boolean financialSettlement) {
        this.financialSettlement = financialSettlement;
    }

    public boolean isContractTermination() { return contractTermination; }
    public void setContractTermination(boolean contractTermination) {
        this.contractTermination = contractTermination;
    }

    public boolean isPlayerConsent() { return playerConsent; }
    public void setPlayerConsent(boolean playerConsent) { this.playerConsent = playerConsent; }

    public boolean isTransferDocuments() { return transferDocuments; }
    public void setTransferDocuments(boolean transferDocuments) {
        this.transferDocuments = transferDocuments;
    }

    public boolean isLeagueNotification() { return leagueNotification; }
    public void setLeagueNotification(boolean leagueNotification) {
        this.leagueNotification = leagueNotification;
    }

    @Override
    public String toString() {
        return "TransferRecord{" +
                "playerName='" + playerName + '\'' +
                ", transferType='" + transferType + '\'' +
                ", destinationClub='" + destinationClub + '\'' +
                ", transferFee=" + transferFee +
                ", transferDate=" + transferDate +
                ", status='" + status + '\'' +
                ", medicalClearance=" + medicalClearance +
                ", financialSettlement=" + financialSettlement +
                ", contractTermination=" + contractTermination +
                ", playerConsent=" + playerConsent +
                ", transferDocuments=" + transferDocuments +
                ", leagueNotification=" + leagueNotification +
                '}';
    }
}