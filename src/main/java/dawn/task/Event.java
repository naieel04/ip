package dawn.task;

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
    public String toString() {
        return "[E]" + super.toString() + " (from: " + startDate + " to: " + endDate + ")";
    }
}
