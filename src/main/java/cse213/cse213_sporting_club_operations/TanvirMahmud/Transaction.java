package cse213.cse213_sporting_club_operations.TanvirMahmud;

import java.time.LocalDate;

public class Transaction {
    private double amount;
    private String type;
    private String description;
    private LocalDate date;
    private boolean compliant;

    public Transaction(double amount, String type, String description, LocalDate date, boolean compliant) {
        this.amount = amount;
        this.type = type;
        this.description = description;
        this.date = date;
        this.compliant = compliant;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public boolean isCompliant() {
        return compliant;
    }

    public void setCompliant(boolean compliant) {
        this.compliant = compliant;
    }

    @Override
    public String toString() {
        return "Transaction{" +
                "amount=" + amount +
                ", type='" + type + '\'' +
                ", description='" + description + '\'' +
                ", date=" + date +
                ", compliant=" + compliant +
                '}';
    }
}