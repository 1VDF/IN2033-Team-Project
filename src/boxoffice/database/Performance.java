package boxoffice.database;

import javafx.beans.property.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class Performance {

    private IntegerProperty performanceId;
    private StringProperty title;
    private StringProperty performanceType;
    private StringProperty description;
    private StringProperty date;
    private StringProperty startTime;
    private IntegerProperty durationMinutes;
    private IntegerProperty venueID;
    private StringProperty venueName;

    // Updated Constructor with venueName
    public Performance(int performanceId, String title, String performanceType,
                       String description, LocalDate date, LocalTime startTime,
                       int durationMinutes, int venueID, String venueName) {
        this.performanceId = new SimpleIntegerProperty(performanceId);
        this.title = new SimpleStringProperty(title);
        this.performanceType = new SimpleStringProperty(performanceType);
        this.description = new SimpleStringProperty(description);
        this.date = new SimpleStringProperty(date.toString());
        this.startTime = new SimpleStringProperty(startTime.toString());
        this.durationMinutes = new SimpleIntegerProperty(durationMinutes);
        this.venueID = new SimpleIntegerProperty(venueID);
        this.venueName = new SimpleStringProperty(venueName); // Initialize venue name
    }

    // Property Getters for TableView Binding
    public IntegerProperty performanceIdProperty() { return performanceId; }
    public StringProperty titleProperty() { return title; }
    public StringProperty performanceTypeProperty() { return performanceType; }
    public StringProperty descriptionProperty() { return description; }
    public StringProperty dateProperty() { return date; }
    public StringProperty startTimeProperty() { return startTime; }
    public IntegerProperty durationMinutesProperty() { return durationMinutes; }
    public StringProperty venueNameProperty() { return venueName; } // Venue Name Binding

    // Standard Getters
    public int getPerformanceId() { return performanceId.get(); }
    public String getTitle() { return title.get(); }
    public String getPerformanceType() { return performanceType.get(); }
    public String getDescription() { return description.get(); }
    public String getDate() { return date.get(); }
    public String getStartTime() { return startTime.get(); }
    public int getDurationMinutes() { return durationMinutes.get(); }
    public int getVenueID() { return venueID.get(); }
    public String getVenueName() { return venueName.get(); }

    // Standard Setters
    public void setPerformanceId(int performanceId) { this.performanceId.set(performanceId); }
    public void setTitle(String title) { this.title.set(title); }
    public void setPerformanceType(String performanceType) { this.performanceType.set(performanceType); }
    public void setDescription(String description) { this.description.set(description); }
    public void setDate(String date) { this.date.set(date); }
    public void setStartTime(String startTime) { this.startTime.set(startTime); }
    public void setDurationMinutes(int durationMinutes) { this.durationMinutes.set(durationMinutes); }
    public void setVenueName(String venueName) { this.venueName.set(venueName); }

    public String getDisplayText() {
        try {
            LocalTime time = LocalTime.parse(this.getStartTime());
            return String.format("%s (%s)",
                    this.getTitle(),
                    time.format(DateTimeFormatter.ofPattern("HH:mm")));
        } catch (Exception e) {
            return this.getTitle();
        }
    }

    @Override
    public String toString() {
        return "Performance{" +
                "performance_id=" + performanceId.get() +
                ", title='" + title.get() + '\'' +
                ", performance_type='" + performanceType.get() + '\'' +
                ", description='" + description.get() + '\'' +
                ", date=" + date.get() +
                ", start_time=" + startTime.get() +
                ", duration_minutes=" + durationMinutes.get() +
                ", venue_id=" + venueID.get() +
                ", venue_name='" + venueName.get() + '\'' +
                '}';
    }
}
