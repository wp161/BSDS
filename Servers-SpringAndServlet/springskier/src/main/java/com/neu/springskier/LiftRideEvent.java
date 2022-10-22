package com.neu.springskier;

public class LiftRideEvent {
    private LiftRide liftRide;
    private Integer resortId;
    private String seasonID;
    private String dayID;
    private Integer skierID;

    public LiftRideEvent(LiftRide liftRide, Integer resortId, String seasonID, String dayID, Integer skierID) {
        this.liftRide = liftRide;
        this.resortId = resortId;
        this.seasonID = seasonID;
        this.dayID = dayID;
        this.skierID = skierID;
    }

    public LiftRide getLiftRide() {
        return liftRide;
    }

    public void setLiftRide(LiftRide liftRide) {
        this.liftRide = liftRide;
    }

    public Integer getResortId() {
        return resortId;
    }

    public void setResortId(Integer resortId) {
        this.resortId = resortId;
    }

    public String getSeasonID() {
        return seasonID;
    }

    public void setSeasonID(String seasonID) {
        this.seasonID = seasonID;
    }

    public String getDayID() {
        return dayID;
    }

    public void setDayID(String dayID) {
        this.dayID = dayID;
    }

    public Integer getSkierID() {
        return skierID;
    }

    public void setSkierID(Integer skierID) {
        this.skierID = skierID;
    }
}
