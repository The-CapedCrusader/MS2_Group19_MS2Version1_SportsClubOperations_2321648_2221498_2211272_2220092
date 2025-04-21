package cse213.cse213_sporting_club_operations.TanvirMahmud;

import javafx.beans.property.SimpleStringProperty;

public class YouthPlayer {
    private final SimpleStringProperty name;
    private final SimpleStringProperty age;
    private final SimpleStringProperty position;
    private final SimpleStringProperty potential;
    private final SimpleStringProperty performance;
    private final SimpleStringProperty status;

    public YouthPlayer(String name, String age, String position, String potential,
                       String performance, String status) {
        this.name = new SimpleStringProperty(name);
        this.age = new SimpleStringProperty(age);
        this.position = new SimpleStringProperty(position);
        this.potential = new SimpleStringProperty(potential);
        this.performance = new SimpleStringProperty(performance);
        this.status = new SimpleStringProperty(status);
    }

    public String getName() {
        return name.get();
    }

    public String getAge() {
        return age.get();
    }

    public String getPosition() {
        return position.get();
    }

    public String getPotential() {
        return potential.get();
    }

    public String getPerformance() {
        return performance.get();
    }

    public String getStatus() {
        return status.get();
    }

    public void setStatus(String status) {
        this.status.set(status);
    }

    public SimpleStringProperty nameProperty() {
        return name;
    }

    public SimpleStringProperty ageProperty() {
        return age;
    }

    public SimpleStringProperty positionProperty() {
        return position;
    }

    public SimpleStringProperty potentialProperty() {
        return potential;
    }

    public SimpleStringProperty performanceProperty() {
        return performance;
    }

    public SimpleStringProperty statusProperty() {
        return status;
    }
}
