package Objects;

public class LiftRide {
    private Integer liftID;
    private Integer time;

    public LiftRide(Integer liftID, Integer time) {
        this.liftID = liftID;
        this.time = time;
    }

    public Integer getLiftID() {
        return liftID;
    }

    public void setLiftID(Integer liftID) {
        this.liftID = liftID;
    }

    public Integer getTime() {
        return time;
    }

    public void setTime(Integer time) {
        this.time = time;
    }
}
