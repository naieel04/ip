import java.util.Locale;

/** Parses user input and validates command arguments. */
public class Parser {
    public static final String TODO_USAGE = "todo [description]";
    public static final String DEADLINE_USAGE = "deadline [description] /by [due date]";
    public static final String EVENT_USAGE = "event [description] /from [start] /to [end]";
    public static final String MARK_USAGE = "mark [task number]";
    public static final String UNMARK_USAGE = "unmark [task number]";

    private static final String DEADLINE_MARKER = "/by";
    private static final String EVENT_START_MARKER = "/from";
    private static final String EVENT_END_MARKER = "/to";

    public static String[] parseCommand(String input) throws DawnException {
        String trimmedInput = input.trim();
        if (trimmedInput.isEmpty()) {
            throw new DawnException(unknownCommandMessage(""));
        }
        String[] parts = trimmedInput.split("\\s+", 2);
        String command = parts[0];
        String arguments = parts.length == 2 ? parts[1].trim() : "";
        return new String[]{command, arguments};
    }

    public static int parseTaskNumber(String arguments, boolean done) throws DawnException {
        String usage = done ? MARK_USAGE : UNMARK_USAGE;
        if (arguments.isEmpty()) {
            throw new DawnException("A task number is required. Use: " + usage);
        }
        if (!arguments.matches("[1-9]\\d*")) {
            throw new DawnException("The task number must be a positive integer. Use: " + usage);
        }
        try {
            return Integer.parseInt(arguments) - 1;
        } catch (NumberFormatException e) {
            throw new DawnException("The task number must be a positive integer. Use: " + usage);
        }
    }

    public static String parseTodoArgs(String arguments) throws DawnException {
        if (arguments.isEmpty()) {
            throw new DawnException("A todo needs a description. Use: " + TODO_USAGE);
        }
        return arguments;
    }

    public static String[] parseDeadlineArgs(String arguments) throws DawnException {
        int byIndex = findStandaloneMarker(arguments, DEADLINE_MARKER);
        if (byIndex < 0) {
            throw new DawnException("A deadline needs the /by keyword. Use: " + DEADLINE_USAGE);
        }
        String description = arguments.substring(0, byIndex).trim();
        String dueDate = arguments.substring(byIndex + DEADLINE_MARKER.length()).trim();
        if (description.isEmpty()) {
            throw new DawnException("The deadline description cannot be blank. Use: " + DEADLINE_USAGE);
        }
        if (dueDate.isEmpty()) {
            throw new DawnException("The due date cannot be blank. Use: " + DEADLINE_USAGE);
        }
        return new String[]{description, dueDate};
    }

    public static String[] parseEventArgs(String arguments) throws DawnException {
        int fromIndex = findStandaloneMarker(arguments, EVENT_START_MARKER);
        int toIndex = findStandaloneMarker(arguments, EVENT_END_MARKER);
        if (fromIndex < 0) {
            throw new DawnException("An event needs the /from keyword. Use: " + EVENT_USAGE);
        }
        if (toIndex < 0) {
            throw new DawnException("An event needs the /to keyword. Use: " + EVENT_USAGE);
        }
        if (toIndex < fromIndex) {
            throw new DawnException("The /to keyword must come after /from. Use: " + EVENT_USAGE);
        }

        String description = arguments.substring(0, fromIndex).trim();
        String start = arguments.substring(fromIndex + EVENT_START_MARKER.length(), toIndex).trim();
        String end = arguments.substring(toIndex + EVENT_END_MARKER.length()).trim();
        if (description.isEmpty()) {
            throw new DawnException("The event description cannot be blank. Use: " + EVENT_USAGE);
        }
        if (start.isEmpty()) {
            throw new DawnException("The event start cannot be blank. Use: " + EVENT_USAGE);
        }
        if (end.isEmpty()) {
            throw new DawnException("The event end cannot be blank. Use: " + EVENT_USAGE);
        }
        return new String[]{description, start, end};
    }

    public static void requireNoArguments(String command, String arguments) throws DawnException {
        if (!arguments.isEmpty()) {
            throw new DawnException("The " + command + " command does not accept arguments. Use: " + command);
        }
    }

    private static int findStandaloneMarker(String text, String marker) {
        int index = text.indexOf(marker);
        while (index >= 0) {
            int markerEnd = index + marker.length();
            boolean leftBoundary = index == 0 || Character.isWhitespace(text.charAt(index - 1));
            boolean rightBoundary = markerEnd == text.length()
                    || Character.isWhitespace(text.charAt(markerEnd));
            if (leftBoundary && rightBoundary) {
                return index;
            }
            index = text.indexOf(marker, markerEnd);
        }
        return -1;
    }

    public static String unknownCommandMessage(String commandWord) {
        String normalizedCommand = commandWord.toLowerCase(Locale.ROOT);
        if (normalizedCommand.contains("todo")) {
            return "Command not recognised. Did you mean: " + TODO_USAGE + "?";
        }
        if (normalizedCommand.contains("deadline")) {
            return "Command not recognised. Did you mean: " + DEADLINE_USAGE + "?";
        }
        if (normalizedCommand.contains("event")) {
            return "Command not recognised. Did you mean: " + EVENT_USAGE + "?";
        }
        if (normalizedCommand.contains("unmark")) {
            return "Command not recognised. Did you mean: " + UNMARK_USAGE + "?";
        }
        if (normalizedCommand.contains("mark")) {
            return "Command not recognised. Did you mean: " + MARK_USAGE + "?";
        }
        return "Command not recognised. Supported commands: todo, deadline, event, list, mark, unmark, bye.";
    }
}
