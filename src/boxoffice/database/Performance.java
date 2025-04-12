package boxoffice.database;

import javafx.beans.property.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

/**
 * Represents a performance in the ticketing system.
 */
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

    /**
     * Constructs a new Performance instance.
     *
     * @param performanceId    The unique ID of the performance.
     * @param title            The title of the performance.
     * @param performanceType  The type of the performance (e.g., "Play", "Concert").
     * @param description      A description of the performance.
     * @param date             The date of the performance.
     * @param startTime        The start time of the performance.
     * @param durationMinutes  The duration of the performance in minutes.
     * @param venueID          The ID of the venue where the performance will take place.
     * @param venueName        The name of the venue.
     */
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
        this.venueName = new SimpleStringProperty(venueName);
    }

    // Property Getters for TableView Binding

    /**
     * @return The performance ID property.
     */
    public IntegerProperty performanceIdProperty() { return performanceId; }

    /**
     * @return The title property.
     */
    public StringProperty titleProperty() { return title; }

    /**
     * @return The performance type property.
     */
    public StringProperty performanceTypeProperty() { return performanceType; }

    /**
     * @return The description property.
     */
    public StringProperty descriptionProperty() { return description; }

    /**
     * @return The date property.
     */
    public StringProperty dateProperty() { return date; }

    /**
     * @return The start time property.
     */
    public StringProperty startTimeProperty() { return startTime; }

    /**
     * @return The duration in minutes property.
     */
    public IntegerProperty durationMinutesProperty() { return durationMinutes; }

    /**
     * @return The venue name property.
     */
    public StringProperty venueNameProperty() { return venueName; }

    // Standard Getters and Setters

    /**
     * @return The unique ID of the performance.
     */
    public int getPerformanceId() { return performanceId.get(); }

    /**
     * @param performanceId The performance ID to set.
     */
    public void setPerformanceId(int performanceId) { this.performanceId.set(performanceId); }

    /**
     * @return The title of the performance.
     */
    public String getTitle() { return title.get(); }

    /**
     * @param title The title to set.
     */
    public void setTitle(String title) { this.title.set(title); }

    /**
     * @return The performance type.
     */
    public String getPerformanceType() { return performanceType.get(); }

    /**
     * @param performanceType The performance type to set.
     */
    public void setPerformanceType(String performanceType) { this.performanceType.set(performanceType); }

    /**
     * @return A description of the performance.
     */
    public String getDescription() { return description.get(); }

    /**
     * @param description The description to set.
     */
    public void setDescription(String description) { this.description.set(description); }

    /**
     * @return The date of the performance.
     */
    public String getDate() { return date.get(); }

    /**
     * @param date The date to set.
     */
    public void setDate(String date) { this.date.set(date); }

    /**
     * @return The start time of the performance.
     */
    public String getStartTime() { return startTime.get(); }

    /**
     * @param startTime The start time to set.
     */
    public void setStartTime(String startTime) { this.startTime.set(startTime); }

    /**
     * @return The duration in minutes of the performance.
     */
    public int getDurationMinutes() { return durationMinutes.get(); }

    /**
     * @param durationMinutes The duration in minutes to set.
     */
    public void setDurationMinutes(int durationMinutes) { this.durationMinutes.set(durationMinutes); }

    /**
     * @return The venue ID where the performance will take place.
     */
    public int getVenueID() { return venueID.get(); }

    /**
     * @param venueName The venue name to set.
     */
    public void setVenueName(String venueName) { this.venueName.set(venueName); }

    /**
     * Returns the performance title along with its start time in a formatted string.
     *
     * @return The formatted display text for the performance.
     */
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

    /**
     * Returns a string representation of the Performance object.
     *
     * @return A string representing the Performance object.
     */
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
