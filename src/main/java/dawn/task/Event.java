package dawn.task;

import java.time.LocalDate;

/** Subclass Event: A task with a description, start date, and end date. */
public class Event extends Task {
    private TaskDateTime startDate;
    private TaskDateTime endDate;

    /**
     * Constructs a new Event task with the specified description, start date, and end date.
     *
     * @param description the description of the event
     * @param startDate the start date of the event
     * @param endDate the end date of the event
     */
    public Event(String description, TaskDateTime startDate, TaskDateTime endDate) {
        super(description);
        this.startDate = startDate;
        this.endDate = endDate;
    }

    /**
     * Constructs an Event task with a specific completion status.
     *
     * @param description the description of the event
     * @param startDate the start date of the event
     * @param endDate the end date of the event
     * @param isDone {@code true} if the event is already completed
     */
    public Event(String description, TaskDateTime startDate, TaskDateTime endDate, boolean isDone) {
        super(description, isDone);
        this.startDate = startDate;
        this.endDate = endDate;
    }

    /**
     * Retrieves the start date of this event.
     *
     * @return the start date encapsulation
     */
    public TaskDateTime getStartDate() {
        return startDate;
    }

    /**
     * Updates the start date of this event.
     *
     * @param startDate the new start date
     */
    public void setStartDate(TaskDateTime startDate) {
        this.startDate = startDate;
    }

    /**
     * Retrieves the end date of this event.
     *
     * @return the end date encapsulation
     */
    public TaskDateTime getEndDate() {
        return endDate;
    }

    /**
     * Updates the end date of this event.
     *
     * @param endDate the new end date
     */
    public void setEndDate(TaskDateTime endDate) {
        this.endDate = endDate;
    }

    /**
     * Gets the display-formatted start string (Backwards-compatibility alias).
     *
     * @return the string representation of the start date
     */
    public String getStartDateTime() {
        return startDate.toDisplayString();
    }

    /**
     * Gets the display-formatted end string (Backwards-compatibility alias).
     *
     * @return the string representation of the end date
     */
    public String getEndDateTime() {
        return endDate.toDisplayString();
    }

    /**
     * Checks if this event is running on the specified calendar date.
     *
     * @param date the date to check against
     * @return {@code true} if the event spans across or starts on the given date, {@code false} otherwise
     */
    @Override
    public boolean isOnDate(LocalDate date) {
        if (startDate.hasDate() && endDate.hasDate()) {
            LocalDate start = startDate.toLocalDate();
            LocalDate end = endDate.toLocalDate();
            return !date.isBefore(start) && !date.isAfter(end);
        }
        if (startDate.hasDate()) {
            return startDate.toLocalDate().equals(date);
        }
        return false;
    }

    /**
     * Converts the event into a formatted string suitable for persistent storage.
     *
     * @return the storage-formatted string representing this event
     */
    @Override
    public String toFileString() {
        return "E | " + super.toFileString() + " | " + startDate.toStorageString() + " | " + endDate.toStorageString();
    }

    /**
     * Returns the string representation of this event for UI display.
     *
     * @return the formatted event string
     */
    @Override
    public String toString() {
        return "[E]" + super.toString() + " (from: " + startDate.toDisplayString() + " to: " + endDate.toDisplayString() + ")";
    }
}
