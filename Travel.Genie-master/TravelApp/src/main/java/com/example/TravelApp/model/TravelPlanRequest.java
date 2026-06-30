// package com.example.TravelApp.model;

// public class TravelPlanRequest {
//     private String destination;
//     private int durationDays;
//     private String budget;
//     private String season;
//     private String companionType;

//     public String getDestination() { return destination; }
//     public void setDestination(String destination) { this.destination = destination; }
//     public int getDurationDays() { return durationDays; }
//     public void setDurationDays(int durationDays) { this.durationDays = durationDays; }
//     public String getBudget() { return budget; }
//     public void setBudget(String budget) { this.budget = budget; }
//     public String getSeason() { return season; }
//     public void setSeason(String season) { this.season = season; }
//     public String getCompanionType() { return companionType; }
//     public void setCompanionType(String companionType) { this.companionType = companionType; }
// }
package com.example.TravelApp.model;

public class TravelPlanRequest {
    private String destination;
    private int durationDays;
    private String budget;
    private String season;
    private String companionType;

    public TravelPlanRequest() {}

    public TravelPlanRequest(String destination, int durationDays, String budget, String season, String companionType) {
        this.destination = destination;
        this.durationDays = durationDays;
        this.budget = budget;
        this.season = season;
        this.companionType = companionType;
    }

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
