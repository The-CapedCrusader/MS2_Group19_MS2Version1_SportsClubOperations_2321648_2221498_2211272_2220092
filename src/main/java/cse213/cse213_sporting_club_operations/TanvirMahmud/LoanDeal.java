package cse213.cse213_sporting_club_operations.TanvirMahmud;

import java.time.LocalDate;

public class LoanDeal {
    private  String playerName;
    private String receivingClub;
    private String sendingClub;
    private LocalDate startDate;
    private LocalDate endDate;
    private  double loanFee;
    private  boolean buyOption;
    private  double buyoutPrice;
    private  String wageStructure;
    private boolean recallClause;
    private  String specialTerms;
    private String status;

    public LoanDeal(String playerName, String receivingClub, String sendingClub, LocalDate startDate, LocalDate endDate, double loanFee, boolean buyOption, double buyoutPrice, String wageStructure, boolean recallClause, String specialTerms, String status) {
        this.playerName = playerName;
        this.receivingClub = receivingClub;
        this.sendingClub = sendingClub;
        this.startDate = startDate;
        this.endDate = endDate;
        this.loanFee = loanFee;
        this.buyOption = buyOption;
        this.buyoutPrice = buyoutPrice;
        this.wageStructure = wageStructure;
        this.recallClause = recallClause;
        this.specialTerms = specialTerms;
        this.status = status;
    }

    public String getPlayerName() {
        return playerName;
    }

    public void setPlayerName(String playerName) {
        this.playerName = playerName;
    }

    public String getReceivingClub() {
        return receivingClub;
    }

    public void setReceivingClub(String receivingClub) {
        this.receivingClub = receivingClub;
    }

    public String getSendingClub() {
        return sendingClub;
    }

    public void setSendingClub(String sendingClub) {
        this.sendingClub = sendingClub;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public double getLoanFee() {
        return loanFee;
    }

    public void setLoanFee(double loanFee) {
        this.loanFee = loanFee;
    }

    public boolean isBuyOption() {
        return buyOption;
    }

    public void setBuyOption(boolean buyOption) {
        this.buyOption = buyOption;
    }

    public double getBuyoutPrice() {
        return buyoutPrice;
    }

    public void setBuyoutPrice(double buyoutPrice) {
        this.buyoutPrice = buyoutPrice;
    }

    public String getWageStructure() {
        return wageStructure;
    }

    public void setWageStructure(String wageStructure) {
        this.wageStructure = wageStructure;
    }

    public boolean isRecallClause() {
        return recallClause;
    }

    public void setRecallClause(boolean recallClause) {
        this.recallClause = recallClause;
    }

    public String getSpecialTerms() {
        return specialTerms;
    }

    public void setSpecialTerms(String specialTerms) {
        this.specialTerms = specialTerms;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "LoanDeal{" +
                "playerName='" + playerName + '\'' +
                ", receivingClub='" + receivingClub + '\'' +
                ", sendingClub='" + sendingClub + '\'' +
                ", startDate=" + startDate +
                ", endDate=" + endDate +
                ", loanFee=" + loanFee +
                ", buyOption=" + buyOption +
                ", buyoutPrice=" + buyoutPrice +
                ", wageStructure='" + wageStructure + '\'' +
                ", recallClause=" + recallClause +
                ", specialTerms='" + specialTerms + '\'' +
                ", status='" + status + '\'' +
                '}';
    }

    public boolean hasBuyOption() { return buyOption; }

    public boolean hasRecallClause() { return recallClause; }



}