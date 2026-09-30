package dawn.task;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.util.Objects;

/** Encapsulates a task date or date-time value. */
public class TaskDateTime {
    private static final DateTimeFormatter DISPLAY_DATE_FORMATTER =
            DateTimeFormatter.ofPattern("MMM dd yyyy", Locale.ENGLISH);
    private static final DateTimeFormatter DISPLAY_DATETIME_FORMATTER =
            DateTimeFormatter.ofPattern("MMM dd yyyy, h:mma", Locale.ENGLISH);
    private static final DateTimeFormatter STORAGE_DATE_FORMATTER =
            DateTimeFormatter.ofPattern("yyyy-MM-dd", Locale.ENGLISH);
    private static final DateTimeFormatter STORAGE_DATETIME_FORMATTER =
            DateTimeFormatter.ofPattern("yyyy-MM-dd HHmm", Locale.ENGLISH);

    private final LocalDate date;
    private final LocalDateTime dateTime;

    public TaskDateTime(LocalDate date) {
        this.date = Objects.requireNonNull(date);
        this.dateTime = null;
    }

    public TaskDateTime(LocalDateTime dateTime) {
        this.dateTime = Objects.requireNonNull(dateTime);
        this.date = null;
    }

    public boolean hasTime() {
        return dateTime != null;
    }

    public LocalDate toLocalDate() {
        return dateTime != null ? dateTime.toLocalDate() : date;
    }

    public String toDisplayString() {
        if (dateTime != null) {
            return dateTime.format(DISPLAY_DATETIME_FORMATTER);
        }
        return date.format(DISPLAY_DATE_FORMATTER);
    }

    public String toStorageString() {
        if (dateTime != null) {
            return dateTime.format(STORAGE_DATETIME_FORMATTER);
        }
        return date.format(STORAGE_DATE_FORMATTER);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof TaskDateTime other)) {
            return false;
        }
        return Objects.equals(date, other.date) && Objects.equals(dateTime, other.dateTime);
    }

    @Override
    public int hashCode() {
        return Objects.hash(date, dateTime);
    }

    @Override
    public String toString() {
        return toDisplayString();
    }
}
