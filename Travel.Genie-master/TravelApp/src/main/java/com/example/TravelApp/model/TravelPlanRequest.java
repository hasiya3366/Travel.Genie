package com.example.TravelApp.model;

public class TravelPlanRequest {
    private String destination;
    private int durationDays;
    private String budget;
    private String season;
    private String companionType;

    public String getDestination() { return destination; }
    public void setDestination(String destination) { this.destination = destination; }
    public int getDurationDays() { return durationDays; }
    public void setDurationDays(int durationDays) { this.durationDays = durationDays; }
    public String getBudget() { return budget; }
    public void setBudget(String budget) { this.budget = budget; }
    public String getSeason() { return season; }
    public void setSeason(String season) { this.season = season; }
    public String getCompanionType() { return companionType; }
    public void setCompanionType(String companionType) { this.companionType = companionType; }
}