package cse213.cse213_sporting_club_operations.TanvirMahmud;

import java.io.Serializable;

public class Player implements Serializable {
    private String name;
    private String age;
    private String position;
    private String fit;
    private String marketValue;

    public Player(String name, String age, String position, String fit, String marketValue) {
        this.name = name;
        this.age = age;
        this.position = position;
        this.fit = fit;
        this.marketValue = marketValue;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public String getFit() {
        return fit;
    }

    public void setFit(String fit) {
        this.fit = fit;
    }

    public String getMarketValue() {
        return marketValue;
    }

    public void setMarketValue(String marketValue) {
        this.marketValue = marketValue;
    }

    @Override
    public String toString() {
        return name + " (" + position + ")";
    }
}