package dawn.task;

import java.time.LocalDate;

/** Subclass Event: A task with a description, start date, and end date. */
public class Event extends Task {
    private TaskDateTime startDate;
    private TaskDateTime endDate;

    public Event(String description, TaskDateTime startDate, TaskDateTime endDate) {
        super(description);
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public Event(String description, TaskDateTime startDate, TaskDateTime endDate, boolean isDone) {
        super(description, isDone);
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public TaskDateTime getStartDate() {
        return startDate;
    }

    public void setStartDate(TaskDateTime startDate) {
        this.startDate = startDate;
    }

    public TaskDateTime getEndDate() {
        return endDate;
    }

    public void setEndDate(TaskDateTime endDate) {
        this.endDate = endDate;
    }

    // Backwards-compatibility aliases
    public String getStartDateTime() {
        return startDate.toDisplayString();
    }

    public String getEndDateTime() {
        return endDate.toDisplayString();
    }

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

    @Override
    public String toFileString() {
        return "E | " + super.toFileString() + " | " + startDate.toStorageString() + " | " + endDate.toStorageString();
    }

    @Override
    public String toString() {
        return "[E]" + super.toString() + " (from: " + startDate.toDisplayString() + " to: " + endDate.toDisplayString() + ")";
    }
}
