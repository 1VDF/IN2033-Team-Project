package boxoffice.database;

public class RestrictedView {
    private int restrictedID;

    private int performanceID;

    private int seatID;

    public enum Type {
        partial, full
    }

    private Type type;


    RestrictedView(int restrictedID, int performanceID, int seatID, Type type){
        this.restrictedID = restrictedID;
        this.performanceID = performanceID;
        this.seatID = seatID;
        this.type = type;
    }

    public int getRestrictedID() {
        return restrictedID;
    }

    public void setRestrictedID(int restrictedID) {
        this.restrictedID = restrictedID;
    }

    public int getPerformanceID() {
        return performanceID;
    }

    public void setPerformanceID(int performanceID) {
        this.performanceID = performanceID;
    }

    public int getSeatID() {
        return seatID;
    }

    public void setSeatID(int seatID) {
        this.seatID = seatID;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return "RestrictedView{" +
                "restrictedID=" + restrictedID +
                ", performanceID=" + performanceID +
                ", seatID=" + seatID +
                ", type=" + type +
                '}';
    }
}
