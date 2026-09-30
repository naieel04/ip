package dawn.parser;

import dawn.command.AddCommand;
import dawn.command.Command;
import dawn.command.DeleteCommand;
import dawn.command.ExitCommand;
import dawn.command.ListCommand;
import dawn.command.MarkCommand;
import dawn.command.ScheduleCommand;
import dawn.command.UnmarkCommand;
import dawn.exception.DawnException;
import dawn.task.Deadline;
import dawn.task.Event;
import dawn.task.TaskDateTime;
import dawn.task.ToDo;

import java.util.Locale;

/** Parses user input and validates command arguments. */
public class Parser {
    public static final String COMMAND_BYE = "bye";
    public static final String COMMAND_LIST = "list";
    public static final String COMMAND_MARK = "mark";
    public static final String COMMAND_UNMARK = "unmark";
    public static final String COMMAND_DELETE = "delete";
    public static final String COMMAND_TODO = "todo";
    public static final String COMMAND_DEADLINE = "deadline";
    public static final String COMMAND_EVENT = "event";
    public static final String COMMAND_SCHEDULE = "schedule";

    public static final String TODO_USAGE = "todo [description]";
    public static final String DEADLINE_USAGE = "deadline [description] /by [due date]";
    public static final String EVENT_USAGE = "event [description] /from [start] /to [end]";
    public static final String MARK_USAGE = "mark [task number]";
    public static final String UNMARK_USAGE = "unmark [task number]";
    public static final String DELETE_USAGE = "delete [task number]";
    public static final String SCHEDULE_USAGE = "schedule [date]";

    private static final String DEADLINE_MARKER = "/by";
    private static final String EVENT_START_MARKER = "/from";
    private static final String EVENT_END_MARKER = "/to";

    /**
     * Parses a raw user input string into an executable {@link Command}.
     *
     * @param input the raw user input line
     * @return the corresponding executable {@link Command}
     * @throws DawnException if the input is malformed or unrecognised
     */
    public static Command parse(String input) throws DawnException {
        String[] parsed = parseCommand(input);
        String command = parsed[0];
        String arguments = parsed[1];

        switch (command) {
        case COMMAND_BYE:
            requireNoArguments(command, arguments);
            return new ExitCommand();
        case COMMAND_LIST:
            requireNoArguments(command, arguments);
            return new ListCommand();
        case COMMAND_MARK:
            return new MarkCommand(parseTaskNumber(arguments, MARK_USAGE));
        case COMMAND_UNMARK:
            return new UnmarkCommand(parseTaskNumber(arguments, UNMARK_USAGE));
        case COMMAND_DELETE:
            return new DeleteCommand(parseTaskNumber(arguments, DELETE_USAGE));
        case COMMAND_TODO:
            return new AddCommand(new ToDo(parseTodoArgs(arguments)));
        case COMMAND_DEADLINE:
            return new AddCommand(parseDeadlineArgs(arguments));
        case COMMAND_EVENT:
            return new AddCommand(parseEventArgs(arguments));
        case COMMAND_SCHEDULE:
            return parseScheduleArgs(arguments);
        default:
            throw new DawnException(unknownCommandMessage(command));
        }
    }

    /**
     * Splits a raw command string into the command word and raw arguments.
     *
     * @param input raw input string
     * @return two-element array with command word and arguments
     * @throws DawnException if input is blank
     */
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

    /**
     * Parses a one-based task number and converts it to a zero-based list index.
     *
     * @param arguments raw command arguments
     * @param usage expected command format shown when validation fails
     * @return the corresponding zero-based task index
     * @throws DawnException if the argument is missing or is not a positive integer
     */
    public static int parseTaskNumber(String arguments, String usage) throws DawnException {
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

    public static Deadline parseDeadlineArgs(String arguments) throws DawnException {
        int byIndex = findStandaloneMarker(arguments, DEADLINE_MARKER);
        if (byIndex < 0) {
            throw new DawnException("A deadline needs the /by keyword. Use: " + DEADLINE_USAGE);
        }
        String description = arguments.substring(0, byIndex).trim();
        String dueDateStr = arguments.substring(byIndex + DEADLINE_MARKER.length()).trim();
        if (description.isEmpty()) {
            throw new DawnException("The deadline description cannot be blank. Use: " + DEADLINE_USAGE);
        }
        if (dueDateStr.isEmpty()) {
            throw new DawnException("The due date cannot be blank. Use: " + DEADLINE_USAGE);
        }
        TaskDateTime dueDate = DateTimeParser.parseFlexible(dueDateStr);
        return new Deadline(description, dueDate);
    }

    public static Event parseEventArgs(String arguments) throws DawnException {
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
        String startStr = arguments.substring(fromIndex + EVENT_START_MARKER.length(), toIndex).trim();
        String endStr = arguments.substring(toIndex + EVENT_END_MARKER.length()).trim();
        if (description.isEmpty()) {
            throw new DawnException("The event description cannot be blank. Use: " + EVENT_USAGE);
        }
        if (startStr.isEmpty()) {
            throw new DawnException("The event start cannot be blank. Use: " + EVENT_USAGE);
        }
        if (endStr.isEmpty()) {
            throw new DawnException("The event end cannot be blank. Use: " + EVENT_USAGE);
        }
        TaskDateTime startDate = DateTimeParser.parseFlexible(startStr);
        TaskDateTime endDate = DateTimeParser.parseFlexible(endStr);
        return new Event(description, startDate, endDate);
    }

    public static Command parseScheduleArgs(String arguments) throws DawnException {
        if (arguments.isEmpty()) {
            throw new DawnException("A date is required. Use: " + SCHEDULE_USAGE);
        }
        TaskDateTime targetDateTime = DateTimeParser.parseStrict(arguments);
        return new ScheduleCommand(targetDateTime.toLocalDate());
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
        if (normalizedCommand.contains("delete")) {
            return "Command not recognised. Did you mean: " + DELETE_USAGE + "?";
        }
        if (normalizedCommand.contains("schedule")) {
            return "Command not recognised. Did you mean: " + SCHEDULE_USAGE + "?";
        }
        return "Command not recognised. Supported commands: todo, deadline, event, list, mark, unmark, delete, bye.";
    }
}
