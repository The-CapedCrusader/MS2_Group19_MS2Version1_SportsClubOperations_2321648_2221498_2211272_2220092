package cse213.cse213_sporting_club_operations.TanvirMahmud;

public class Contract {
    private String playerName;
    private String salary;
    private String bonus;
    private int contractLength;
    private String releaseClause;

    public Contract(String playerName, String salary, String bonus, int contractLength, String releaseClause) {
        this.playerName = playerName;
        this.salary = salary;
        this.bonus = bonus;
        this.contractLength = contractLength;
        this.releaseClause = releaseClause;
    }

    public String getPlayerName() {
        return playerName;
    }

    public void setPlayerName(String playerName) {
        this.playerName = playerName;
    }

    public String getSalary() {
        return salary;
    }

    public void setSalary(String salary) {
        this.salary = salary;
    }

    public String getBonus() {
        return bonus;
    }

    public void setBonus(String bonus) {
        this.bonus = bonus;
    }

    public int getContractLength() {
        return contractLength;
    }

    public void setContractLength(int contractLength) {
        this.contractLength = contractLength;
    }

    public String getReleaseClause() {
        return releaseClause;
    }

    public void setReleaseClause(String releaseClause) {
        this.releaseClause = releaseClause;
    }

    @Override
    public String toString() {
        return "Contract{" +
                "playerName='" + playerName + '\'' +
                ", salary='" + salary + '\'' +
                ", bonus='" + bonus + '\'' +
                ", contractLength=" + contractLength +
                ", releaseClause='" + releaseClause + '\'' +
                '}';
    }
}