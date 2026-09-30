package dawn.task;

import java.time.LocalDate;

/** Represents a task's description and completion status. */
public class Task {
    private String description;
    private boolean isDone;

    public Task(String description) {
        this(description, false);
    }

    public Task(String description, boolean isDone) {
        this.description = description;
        this.isDone = isDone;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isDone() {
        return isDone;
    }

    public void setDone(boolean done) {
        isDone = done;
    }

    public String getStatusIcon() {
        return isDone ? "X" : " ";
    }

    public String toFileString() {
        return (isDone ? "1" : "0") + " | " + description;
    }

    /**
     * Checks if this task occurs on the specified calendar date.
     *
     * @param date the date to check against
     * @return {@code true} if the task occurs on the given date, {@code false} otherwise
     */
    public boolean isOnDate(LocalDate date) {
        return false;
    }

    /**
     * Checks if the task description contains the specified keyword (case-insensitive).
     *
     * @param keyword the substring keyword to look for
     * @return {@code true} if the description contains the keyword, {@code false} otherwise
     */
    public boolean containsKeyword(String keyword) {
        if (keyword == null || keyword.isEmpty()) {
            return false;
        }
        return description.toLowerCase().contains(keyword.toLowerCase());
    }

    @Override
    public String toString() {
        return String.format("[%s] %s", getStatusIcon(), description);
    }
}
