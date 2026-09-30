package dawn.parser;

import dawn.exception.DawnException;
import dawn.task.TaskDateTime;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.format.ResolverStyle;
import java.util.Locale;

/** Parses and validates date and time inputs using strict calendar rules. */
public class DateTimeParser {
    public static final String DATE_TIME_USAGE =
            "yyyy-MM-dd [HHmm] or d/M/yyyy [HHmm] (e.g., 2019-12-02 1800 or 2/12/2019 1800)";

    private static final DateTimeFormatter[] DATE_TIME_FORMATTERS = {
        DateTimeFormatter.ofPattern("uuuu-MM-dd HHmm", Locale.ENGLISH)
                .withResolverStyle(ResolverStyle.STRICT),
        DateTimeFormatter.ofPattern("d/M/uuuu HHmm", Locale.ENGLISH)
                .withResolverStyle(ResolverStyle.STRICT)
    };

    private static final DateTimeFormatter[] DATE_FORMATTERS = {
        DateTimeFormatter.ofPattern("uuuu-MM-dd", Locale.ENGLISH)
                .withResolverStyle(ResolverStyle.STRICT),
        DateTimeFormatter.ofPattern("d/M/uuuu", Locale.ENGLISH)
                .withResolverStyle(ResolverStyle.STRICT)
    };

    /**
     * Parses a date or date-time string strictly into a {@link TaskDateTime}.
     *
     * @param input the raw date/time string
     * @return the parsed {@link TaskDateTime}
     * @throws DawnException if the input is malformed, has invalid values, or unsupported format
     */
    public static TaskDateTime parse(String input) throws DawnException {
        if (input == null || input.trim().isEmpty()) {
            throw new DawnException("The date cannot be blank. Use: " + DATE_TIME_USAGE);
        }
        String trimmed = input.trim();

        for (DateTimeFormatter formatter : DATE_TIME_FORMATTERS) {
            try {
                LocalDateTime dateTime = LocalDateTime.parse(trimmed, formatter);
                return new TaskDateTime(dateTime);
            } catch (DateTimeParseException ignored) {
                // Try next pattern
            }
        }

        for (DateTimeFormatter formatter : DATE_FORMATTERS) {
            try {
                LocalDate date = LocalDate.parse(trimmed, formatter);
                return new TaskDateTime(date);
            } catch (DateTimeParseException ignored) {
                // Try next pattern
            }
        }

        throw new DawnException("Invalid date or time format. Use: " + DATE_TIME_USAGE);
    }
}
