package dawn.task;

import dawn.exception.DawnException;
import dawn.parser.DateTimeParser;

import java.time.LocalDate;

/** Subclass Event: A task with a description, start date, and end date. */
public class Event extends Task {
    private String startDate;
    private String endDate;

    public Event(String description, String startDate, String endDate) {
        super(description);
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public Event(String description, String startDate, String endDate, boolean isDone) {
        super(description, isDone);
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    // Backwards-compatibility aliases
    public String getStartDateTime() {
        return startDate;
    }

    public String getEndDateTime() {
        return endDate;
    }

    @Override
    public boolean isOnDate(LocalDate date) {
        try {
            LocalDate start = DateTimeParser.parse(startDate).toLocalDate();
            LocalDate end = DateTimeParser.parse(endDate).toLocalDate();
            return !date.isBefore(start) && !date.isAfter(end);
        } catch (DawnException e) {
            try {
                LocalDate start = DateTimeParser.parse(startDate).toLocalDate();
                return start.equals(date);
            } catch (DawnException ignored) {
                return false;
            }
        }
    }

    @Override
    public String toFileString() {
        return "E | " + super.toFileString() + " | " + startDate + " | " + endDate;
    }

    @Override
    public String toString() {
        return "[E]" + super.toString() + " (from: " + startDate + " to: " + endDate + ")";
    }
}
