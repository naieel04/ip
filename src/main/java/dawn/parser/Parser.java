package dawn.parser;

import dawn.command.AddCommand;
import dawn.command.Command;
import dawn.command.DeleteCommand;
import dawn.command.ExitCommand;
import dawn.command.FindCommand;
import dawn.command.ListCommand;
import dawn.command.MarkCommand;
import dawn.command.UnmarkCommand;
import dawn.command.ViewCommand;
import dawn.exception.DawnException;
import dawn.task.Deadline;
import dawn.task.Event;
import dawn.task.TaskDateTime;
import dawn.task.ToDo;

import java.util.Locale;

/** Parses user input and validates command arguments. */
public class Parser {
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
        CommandWord commandWord = CommandWord.fromKeyword(command);
        if (commandWord == null) {
            throw new DawnException(unknownCommandMessage(command));
        }

        switch (commandWord) {
        case BYE:
            requireNoArguments(command, arguments);
            return new ExitCommand();
        case LIST:
            requireNoArguments(command, arguments);
            return new ListCommand();
        case MARK:
            return new MarkCommand(parseTaskNumber(arguments, CommandWord.MARK.usage()));
        case UNMARK:
            return new UnmarkCommand(parseTaskNumber(arguments, CommandWord.UNMARK.usage()));
        case DELETE:
            return new DeleteCommand(parseTaskNumber(arguments, CommandWord.DELETE.usage()));
        case TODO:
            return new AddCommand(new ToDo(parseTodoArgs(arguments)));
        case DEADLINE:
            return new AddCommand(parseDeadlineArgs(arguments));
        case EVENT:
            return new AddCommand(parseEventArgs(arguments));
        case VIEW:
        case SCHEDULE:
            return parseViewArgs(arguments);
        case FIND:
            return parseFindArgs(arguments);
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

    /**
     * Extrapolates and validates the description for a ToDo task from the command arguments.
     *
     * @param arguments the raw command arguments
     * @return the validated task description safely extracted
     * @throws DawnException if the description is blank
     */
    public static String parseTodoArgs(String arguments) throws DawnException {
        if (arguments.isEmpty()) {
            throw new DawnException("A todo needs a description. Use: " + CommandWord.TODO.usage());
        }
        return arguments;
    }

    /**
     * Parses the arguments for a Deadline task into a description and due date.
     *
     * @param arguments the raw command arguments containing description and /by clause
     * @return a constructed Deadline task
     * @throws DawnException if markers are missing or fields are blank
     */
    public static Deadline parseDeadlineArgs(String arguments) throws DawnException {
        int byIndex = findStandaloneMarker(arguments, DEADLINE_MARKER);
        if (byIndex < 0) {
            throw new DawnException("A deadline needs the /by keyword. Use: " + CommandWord.DEADLINE.usage());
        }
        String description = arguments.substring(0, byIndex).trim();
        String dueDateStr = arguments.substring(byIndex + DEADLINE_MARKER.length()).trim();
        if (description.isEmpty()) {
            throw new DawnException("The deadline description cannot be blank. Use: "
                    + CommandWord.DEADLINE.usage());
        }
        if (dueDateStr.isEmpty()) {
            throw new DawnException("The due date cannot be blank. Use: " + CommandWord.DEADLINE.usage());
        }
        TaskDateTime dueDate = DateTimeParser.parseFlexible(dueDateStr);
        return new Deadline(description, dueDate);
    }

    /**
     * Parses the arguments for an Event task into a description, start date, and end date.
     *
     * @param arguments the raw command arguments containing description, /from, and /to clauses
     * @return a constructed Event task
     * @throws DawnException if markers are missing out of order, or fields are blank
     */
    public static Event parseEventArgs(String arguments) throws DawnException {
        int fromIndex = findStandaloneMarker(arguments, EVENT_START_MARKER);
        int toIndex = findStandaloneMarker(arguments, EVENT_END_MARKER);
        if (fromIndex < 0) {
            throw new DawnException("An event needs the /from keyword. Use: " + CommandWord.EVENT.usage());
        }
        if (toIndex < 0) {
            throw new DawnException("An event needs the /to keyword. Use: " + CommandWord.EVENT.usage());
        }
        if (toIndex < fromIndex) {
            throw new DawnException("The /to keyword must come after /from. Use: " + CommandWord.EVENT.usage());
        }
        String description = arguments.substring(0, fromIndex).trim();
        String startStr = arguments.substring(fromIndex + EVENT_START_MARKER.length(), toIndex).trim();
        String endStr = arguments.substring(toIndex + EVENT_END_MARKER.length()).trim();
        if (description.isEmpty()) {
            throw new DawnException("The event description cannot be blank. Use: " + CommandWord.EVENT.usage());
        }
        if (startStr.isEmpty()) {
            throw new DawnException("The event start cannot be blank. Use: " + CommandWord.EVENT.usage());
        }
        if (endStr.isEmpty()) {
            throw new DawnException("The event end cannot be blank. Use: " + CommandWord.EVENT.usage());
        }
        TaskDateTime startDate = DateTimeParser.parseFlexible(startStr);
        TaskDateTime endDate = DateTimeParser.parseFlexible(endStr);
        return new Event(description, startDate, endDate);
    }

    /**
     * Parses the arguments for a view command to extract the target date.
     *
     * @param arguments the raw command arguments
     * @return an executable ViewCommand holding the target date
     * @throws DawnException if the date is blank or malformed
     */
    public static Command parseViewArgs(String arguments) throws DawnException {
        if (arguments.isEmpty()) {
            throw new DawnException("A date is required. Use: " + CommandWord.VIEW.usage());
        }
        TaskDateTime targetDateTime = DateTimeParser.parseStrict(arguments);
        return new ViewCommand(targetDateTime);
    }

    /**
     * Parses the arguments for a find command to extract the search keyword.
     *
     * @param arguments the raw command arguments
     * @return an executable FindCommand holding the extracted keyword
     * @throws DawnException if the keyword is blank
     */
    public static Command parseFindArgs(String arguments) throws DawnException {
        if (arguments.isEmpty()) {
            throw new DawnException("A search keyword is required. Use: " + CommandWord.FIND.usage());
        }
        return new FindCommand(arguments);
    }

    /**
     * Asserts that no trailing arguments were supplied for a command that does not accept any.
     *
     * @param command the command word invoked
     * @param arguments the trailing arguments string
     * @throws DawnException if arguments is not empty
     */
    public static void requireNoArguments(String command, String arguments) throws DawnException {
        if (!arguments.isEmpty()) {
            throw new DawnException("The " + command + " command does not accept arguments. Use: " + command);
        }
    }

    /**
     * Searches for a command marker token bounded by whitespace matching standard separator rules.
     *
     * @param text the text to search within
     * @param marker the specific marker sequence to find
     * @return the zero-based index marking the start of the token, or -1 if no standalone match is found
     */
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

    /**
     * Generates a helpful error message when an unrecognized command is typed, detecting plausible typos.
     *
     * @param commandWord the malformed command word received
     * @return a structured string diagnosing the typo or listing valid commands
     */
    public static String unknownCommandMessage(String commandWord) {
        String normalizedCommand = commandWord.toLowerCase(Locale.ROOT);
        if (normalizedCommand.contains(CommandWord.TODO.keyword())) {
            return "Command not recognised.\nDid you mean: " + CommandWord.TODO.usage() + "?";
        }
        if (normalizedCommand.contains(CommandWord.DEADLINE.keyword())) {
            return "Command not recognised.\nDid you mean: " + CommandWord.DEADLINE.usage() + "?";
        }
        if (normalizedCommand.contains(CommandWord.EVENT.keyword())) {
            return "Command not recognised.\nDid you mean: " + CommandWord.EVENT.usage() + "?";
        }
        if (normalizedCommand.contains(CommandWord.UNMARK.keyword())) {
            return "Command not recognised.\nDid you mean: " + CommandWord.UNMARK.usage() + "?";
        }
        if (normalizedCommand.contains(CommandWord.MARK.keyword())) {
            return "Command not recognised.\nDid you mean: " + CommandWord.MARK.usage() + "?";
        }
        if (normalizedCommand.contains(CommandWord.DELETE.keyword())) {
            return "Command not recognised.\nDid you mean: " + CommandWord.DELETE.usage() + "?";
        }
        if (normalizedCommand.contains(CommandWord.VIEW.keyword())
                || normalizedCommand.contains(CommandWord.SCHEDULE.keyword())) {
            return "Command not recognised.\nDid you mean: " + CommandWord.VIEW.usage() + "?";
        }
        if (normalizedCommand.contains(CommandWord.FIND.keyword())) {
            return "Command not recognised.\nDid you mean: " + CommandWord.FIND.usage() + "?";
        }
        return "Command not recognised.\nSupported commands:\n" + CommandWord.supportedCommands();
    }
}
