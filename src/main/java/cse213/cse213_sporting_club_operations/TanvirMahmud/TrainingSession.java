package cse213.cse213_sporting_club_operations.TanvirMahmud;

import java.time.LocalDate;
import java.time.LocalTime;

public class TrainingSession implements java.io.Serializable {
    private final long serialVersionUID = 1L;

    private final String sessionType;
    private final LocalDate sessionDate;
    private final LocalTime startTime;
    private final String duration;
    private final String primaryFocus;
    private final String secondaryFocus;
    private final String intensity;
    private final String playerGroup;
    private final String location;
    private final String objectives;
    private final String equipmentRequired;

    public TrainingSession(String sessionType, LocalDate sessionDate, LocalTime startTime,
                           String duration, String primaryFocus, String secondaryFocus,
                           String intensity, String playerGroup, String location,
                           String objectives, String equipmentRequired) {
        this.sessionType = sessionType;
        this.sessionDate = sessionDate;
        this.startTime = startTime;
        this.duration = duration;
        this.primaryFocus = primaryFocus;
        this.secondaryFocus = secondaryFocus;
        this.intensity = intensity;
        this.playerGroup = playerGroup;
        this.location = location;
        this.objectives = objectives;
        this.equipmentRequired = equipmentRequired;
    }

    public String getSessionType() { return sessionType; }
    public LocalDate getSessionDate() { return sessionDate; }
    public LocalTime getStartTime() { return startTime; }
    public String getDuration() { return duration; }
    public String getPrimaryFocus() { return primaryFocus; }
    public String getSecondaryFocus() { return secondaryFocus; }
    public String getIntensity() { return intensity; }
    public String getPlayerGroup() { return playerGroup; }
    public String getLocation() { return location; }
    public String getObjectives() { return objectives; }
    public String getEquipmentRequired() { return equipmentRequired; }
}
