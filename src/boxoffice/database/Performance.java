package boxoffice.database;

import java.time.LocalDate;
import java.time.LocalTime;

public class Performance {
    private int performanceId;
    private String title;
    private String performanceType;
    private String description;
    private LocalDate date;
    private LocalTime startTime;
    private int durationMinutes;

    // Constructor
    public Performance(int performanceId, String title, String performanceType,
                       String description, LocalDate date, LocalTime startTime,
                       int durationMinutes) {
        this.performanceId = performanceId;
        this.title = title;
        this.performanceType = performanceType;
        this.description = description;
        this.date = date;
        this.startTime = startTime;
        this.durationMinutes = durationMinutes;
    }

    // Getters and Setters
    public int getPerformanceId() { return performanceId; }
    public void setPerformanceId(int performanceId) { this.performanceId = performanceId; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getPerformanceType() { return performanceType; }
    public void setPerformanceType(String performanceType) { this.performanceType = performanceType; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public LocalDate getDate() { return date; }
    public void setDate(LocalDate date) { this.date = date; }

    public LocalTime getStartTime() { return startTime; }
    public void setStartTime(LocalTime startTime) { this.startTime = startTime; }

    public int getDurationMinutes() { return durationMinutes; }
    public void setDurationMinutes(int durationMinutes) { this.durationMinutes = durationMinutes; }

    @Override
    public String toString() {
        return "entity.performance{" +
                "performance_id" + performanceId +
                ", title='" + title + '\'' +
                ", performance_type='" + performanceType + '\'' +
                ", description='" + description + '\'' +
                ", date=" + date +
                ", start_time=" + startTime +
                ", duration_inutes=" + durationMinutes +
                '}';
    }
}
