package Objects;

public class Resort {
    private String resortName;
    private int resortId;

    public Resort(String resortName, int resortId) {
        this.resortName = resortName;
        this.resortId = resortId;
    }

    public String getResortName() {
        return resortName;
    }

    public void setResortName(String resortName) {
        this.resortName = resortName;
    }

    public int getResortId() {
        return resortId;
    }

    public void setResortId(int resortId) {
        this.resortId = resortId;
    }

}
