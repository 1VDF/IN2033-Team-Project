package boxoffice.database;

import java.time.LocalDate;
import java.time.LocalTime;

public class Meeting {
    private int meetingId;
    private LocalDate date;
    private LocalTime time;
    private int duration; // in minutes
    private int venueID; // The room where the meeting takes place

    // Constructors
    public Meeting(int meetingId, LocalDate date, LocalTime time,
                   int duration, int venueId) {
        this.meetingId = meetingId;
        this.date = date;
        this.time = time;
        this.duration = duration;
        this.venueID = venueID;
    }

    // Getters and Setters
    public int getMeetingId() { return meetingId; }
    public void setMeetingId(int meetingId) { this.meetingId = meetingId; }

    public LocalDate getDate() { return date; }
    public void setDate(LocalDate date) { this.date = date; }

    public LocalTime getTime() { return time; }
    public void setTime(LocalTime time) { this.time = time; }

    public int getDuration() { return duration; }
    public void setDuration(int duration) { this.duration = duration; }

    public int getVenueID() {
        return venueID;
    }

    public void setVenueID(int venueID) {
        this.venueID = venueID;
    }

    @Override
    public String toString() {
        return "entity.Meeting{" +
                "meeting_id=" + meetingId +
                ", date=" + date +
                ", time=" + time +
                ", duration=" + duration +
                ", venue_id=" + venueID +
                '}';
    }
}
