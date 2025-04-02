package boxoffice.database;

public class RestrictedView {
    private int restrictedID;

    private int performanceID;

    private int seat_number;

    private String row_number;

    RestrictedView(int restrictedID, int performanceID, int seat_number, String row_number){
        this.restrictedID = restrictedID;
        this.performanceID = performanceID;
        this.seat_number = seat_number;
        this.row_number = row_number;
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

    public int getSeat_number() {
        return seat_number;
    }

    public void setSeat_number(int seat_number) {
        this.seat_number = seat_number;
    }

    public String getRow_number() {
        return row_number;
    }

    public void setRow_number(String row_number) {
        this.row_number = row_number;
    }

    @Override
    public String toString() {
        return "entity.restricted_view_seat{" +
                "restricted_id=" + restrictedID +
                ", performance_id=" + performanceID +
                ", seat_number=" + seat_number +
                ", row_number='" + row_number + '\'' +
                '}';
    }
}
