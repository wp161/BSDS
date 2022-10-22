package org.example;

import io.swagger.client.model.LiftRide;

public class LiftRideEvent {

  LiftRide liftRide;
  Integer skierID;
  Integer resortID;
  String seasonID;
  String dayID;

  public LiftRideEvent(LiftRide liftRide, Integer skierID, Integer resortID, String seasonID,
      String dayID) {
    this.liftRide = liftRide;
    this.skierID = skierID;
    this.resortID = resortID;
    this.seasonID = seasonID;
    this.dayID = dayID;
  }

  public LiftRide getLiftRide() {
    return liftRide;
  }

  public void setLiftRide(LiftRide liftRide) {
    this.liftRide = liftRide;
  }

  public Integer getSkierID() {
    return skierID;
  }

  public void setSkierID(Integer skierID) {
    this.skierID = skierID;
  }

  public Integer getResortID() {
    return resortID;
  }

  public void setResortID(Integer resortID) {
    this.resortID = resortID;
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
}