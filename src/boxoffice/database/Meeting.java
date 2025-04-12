package boxoffice.database;

import java.time.LocalDate;
import java.time.LocalTime;

/**
 * Represents a meeting in the box office system.
 */
public class Meeting {
    private int meetingId;
    private LocalDate date;
    private LocalTime time;
    private int duration;
    private int venueID;

    /**
     * Constructs a new Meeting instance.
     *
     * @param meetingId The unique ID of the meeting.
     * @param date      The date of the meeting.
     * @param time      The time of the meeting.
     * @param duration  The duration of the meeting in minutes.
     * @param venueId   The venue ID where the meeting is held.
     */
    public Meeting(int meetingId, LocalDate date, LocalTime time,
                   int duration, int venueId) {
        this.meetingId = meetingId;
        this.date = date;
        this.time = time;
        this.duration = duration;
        this.venueID = venueId;
    }

    // Getters and Setters

    /**
     * @return The unique ID of the meeting.
     */
    public int getMeetingId() { return meetingId; }

    /**
     * @param meetingId The meeting ID to set.
     */
    public void setMeetingId(int meetingId) { this.meetingId = meetingId; }

    /**
     * @return The date of the meeting.
     */
    public LocalDate getDate() { return date; }

    /**
     * @param date The date to set for the meeting.
     */
    public void setDate(LocalDate date) { this.date = date; }

    /**
     * @return The time of the meeting.
     */
    public LocalTime getTime() { return time; }

    /**
     * @param time The time to set for the meeting.
     */
    public void setTime(LocalTime time) { this.time = time; }

    /**
     * @return The duration of the meeting in minutes.
     */
    public int getDuration() { return duration; }

    /**
     * @param duration The duration to set for the meeting.
     */
    public void setDuration(int duration) { this.duration = duration; }

    /**
     * @return The venue ID where the meeting will be held.
     */
    public int getVenueID() { return venueID; }

    /**
     * @param venueID The venue ID to set.
     */
    public void setVenueID(int venueID) { this.venueID = venueID; }

    /**
     * Returns a string representation of the Meeting object.
     *
     * @return A string representing the Meeting object.
     */
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
