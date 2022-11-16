package Objects;

public class LiftRideDetail {
    private int resortId;
    private String seasonId;
    private String dayId;
    private String skierId;

    private LiftRide liftRide;

    public LiftRideDetail(int resortId, String seasonId, String dayId, String skierId, LiftRide liftRide) {
        this.resortId = resortId;
        this.seasonId = seasonId;
        this.dayId = dayId;
        this.skierId = skierId;
        this.liftRide = liftRide;
    }

    public int getResortId() {
        return resortId;
    }

    public void setResortId(int resortId) {
        this.resortId = resortId;
    }

    public String getSeasonId() {
        return seasonId;
    }

    public void setSeasonId(String seasonId) {
        this.seasonId = seasonId;
    }

    public String getDayId() {
        return dayId;
    }

    public void setDayId(String dayId) {
        this.dayId = dayId;
    }

    public String getSkierId() {
        return skierId;
    }

    public void setSkierId(String skierId) {
        this.skierId = skierId;
    }

    public LiftRide getLiftRide() {
        return liftRide;
    }

    public void setLiftRide(LiftRide liftRide) {
        this.liftRide = liftRide;
    }
}
