package boxoffice.models;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Performance {
    private final StringProperty performanceName;
    private final StringProperty performanceDate;
    private final StringProperty performanceTime;
    private final StringProperty performanceLocation;

    public Performance(String performanceName, String performanceDate, String performanceTime, String performanceLocation) {
        this.performanceName = new SimpleStringProperty(performanceName);
        this.performanceDate = new SimpleStringProperty(performanceDate);
        this.performanceTime = new SimpleStringProperty(performanceTime);
        this.performanceLocation = new SimpleStringProperty(performanceLocation);
    }

    // Getters and setters for properties
    public String getPerformanceName() {
        return performanceName.get();
    }

    public void setPerformanceName(String performanceName) {
        this.performanceName.set(performanceName);
    }

    public StringProperty performanceNameProperty() {
        return performanceName;
    }

    public String getPerformanceDate() {
        return performanceDate.get();
    }

    public void setPerformanceDate(String performanceDate) {
        this.performanceDate.set(performanceDate);
    }

    public StringProperty performanceDateProperty() {
        return performanceDate;
    }

    public String getPerformanceTime() {
        return performanceTime.get();
    }

    public void setPerformanceTime(String performanceTime) {
        this.performanceTime.set(performanceTime);
    }

    public StringProperty performanceTimeProperty() {
        return performanceTime;
    }

    public String getPerformanceLocation() {
        return performanceLocation.get();
    }

    public void setPerformanceLocation(String performanceLocation) {
        this.performanceLocation.set(performanceLocation);
    }

    public StringProperty performanceLocationProperty() {
        return performanceLocation;
    }

    @Override
    public String toString() {
        return "Performance{" +
                "performanceName='" + performanceName.get() + '\'' +
                ", performanceDate='" + performanceDate.get() + '\'' +
                ", performanceTime='" + performanceTime.get() + '\'' +
                ", performanceLocation='" + performanceLocation.get() + '\'' +
                '}';
    }
}
