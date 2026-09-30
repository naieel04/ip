package dawn.task;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.util.Objects;

/** Encapsulates a task date, date-time, or freeform text value. */
public class TaskDateTime {
    private static final DateTimeFormatter DISPLAY_DATE_FORMATTER =
            DateTimeFormatter.ofPattern("dd MMM yyyy", Locale.ENGLISH);
    private static final DateTimeFormatter DISPLAY_DATETIME_FORMATTER =
            DateTimeFormatter.ofPattern("dd MMM yyyy HH:mm", Locale.ENGLISH);
    private static final DateTimeFormatter STORAGE_DATE_FORMATTER =
            DateTimeFormatter.ofPattern("uuuu-MM-dd", Locale.ENGLISH);
    private static final DateTimeFormatter STORAGE_DATETIME_FORMATTER =
            DateTimeFormatter.ofPattern("uuuu-MM-dd HHmm", Locale.ENGLISH);

    private final LocalDate date;
    private final LocalDateTime dateTime;
    private final String rawText;

    public TaskDateTime(LocalDate date) {
        this.date = Objects.requireNonNull(date);
        this.dateTime = null;
        this.rawText = null;
    }

    public TaskDateTime(LocalDateTime dateTime) {
        this.dateTime = Objects.requireNonNull(dateTime);
        this.date = null;
        this.rawText = null;
    }

    public TaskDateTime(String rawText) {
        this.rawText = Objects.requireNonNull(rawText).trim();
        this.date = null;
        this.dateTime = null;
    }

    public boolean hasTime() {
        return dateTime != null;
    }

    public boolean hasDate() {
        return date != null || dateTime != null;
    }

    public LocalDate toLocalDate() {
        if (dateTime != null) {
            return dateTime.toLocalDate();
        }
        return date;
    }

    public boolean isOnDate(LocalDate targetDate) {
        if (targetDate == null) {
            return false;
        }
        LocalDate thisDate = toLocalDate();
        return thisDate != null && thisDate.equals(targetDate);
    }

    public String toDisplayString() {
        if (dateTime != null) {
            return dateTime.format(DISPLAY_DATETIME_FORMATTER);
        }
        if (date != null) {
            return date.format(DISPLAY_DATE_FORMATTER);
        }
        return rawText;
    }

    public String toStorageString() {
        if (dateTime != null) {
            return dateTime.format(STORAGE_DATETIME_FORMATTER);
        }
        if (date != null) {
            return date.format(STORAGE_DATE_FORMATTER);
        }
        return rawText;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof TaskDateTime other)) {
            return false;
        }
        return Objects.equals(date, other.date)
                && Objects.equals(dateTime, other.dateTime)
                && Objects.equals(rawText, other.rawText);
    }

    @Override
    public int hashCode() {
        return Objects.hash(date, dateTime, rawText);
    }

    @Override
    public String toString() {
        return toDisplayString();
    }
}
