import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/** Handles exact command-word parsing and task-list updates. */
public class CommandHandler {
    private static final String COMMAND_BYE = "bye";
    private static final String COMMAND_LIST = "list";
    private static final String COMMAND_MARK = "mark";
    private static final String COMMAND_UNMARK = "unmark";
    private static final String COMMAND_TODO = "todo";
    private static final String COMMAND_DEADLINE = "deadline";
    private static final String COMMAND_EVENT = "event";

    private static final String DEADLINE_MARKER = "/by";
    private static final String EVENT_START_MARKER = "/from";
    private static final String EVENT_END_MARKER = "/to";
    private static final String TODO_USAGE = "todo [description]";
    private static final String DEADLINE_USAGE = "deadline [description] /by [due date]";
    private static final String EVENT_USAGE = "event [description] /from [start] /to [end]";
    private static final String MARK_USAGE = "mark [task number]";
    private static final String UNMARK_USAGE = "unmark [task number]";

    private static final int MAX_TASKS = 100;
    private final List<Task> taskList;

    public CommandHandler() {
        this.taskList = new ArrayList<>();
    }

    public List<Task> getTaskList() {
        return taskList;
    }

    /**
     * Processes one command whose first whitespace-delimited word must match exactly.
     *
     * @param input the raw command line
     * @return true only for a valid {@code bye} command
     * @throws DawnException if the command or any required argument is invalid
     */
    public boolean handleCommand(String input) throws DawnException {
        String trimmedInput = input.trim();
        if (trimmedInput.isEmpty()) {
            throw new DawnException(unknownCommandMessage(""));
        }
        String[] parts = trimmedInput.split("\\s+", 2);
        String command = parts[0];
        String arguments = parts.length == 2 ? parts[1].trim() : "";

        switch (command) {
        case COMMAND_BYE:
            requireNoArguments(command, arguments);
            return true;
        case COMMAND_LIST:
            requireNoArguments(command, arguments);
            printList();
            return false;
        case COMMAND_MARK:
            updateTask(arguments, true);
            return false;
        case COMMAND_UNMARK:
            updateTask(arguments, false);
            return false;
        case COMMAND_TODO:
            addTodo(arguments);
            return false;
        case COMMAND_DEADLINE:
            addDeadline(arguments);
            return false;
        case COMMAND_EVENT:
            addEvent(arguments);
            return false;
        default:
            throw new DawnException(unknownCommandMessage(command));
        }
    }

    /** Prints the current task list in insertion order. */
    public void printList() {
        System.out.println("Here are the tasks in your list:");
        for (int i = 0; i < taskList.size(); i++) {
            System.out.printf("%d.%s%n", i + 1, taskList.get(i));
        }
    }

    /** Validates a task number before marking or unmarking that task. */
    public void updateTask(String taskNumber, boolean done) throws DawnException {
        String command = done ? COMMAND_MARK : COMMAND_UNMARK;
        String usage = done ? MARK_USAGE : UNMARK_USAGE;
        if (taskNumber.isEmpty()) {
            throw new DawnException("A task number is required. Use: " + usage);
        }
        if (!taskNumber.matches("[1-9]\\d*")) {
            throw new DawnException("The task number must be a positive integer. Use: " + usage);
        }

        int index;
        try {
            index = Integer.parseInt(taskNumber) - 1;
        } catch (NumberFormatException e) {
            throw new DawnException("The task number must be a positive integer. Use: " + usage);
        }
        if (index < 0 || index >= taskList.size()) {
            throw new DawnException("Task number not found. Use: " + command + " [task number]");
        }

        Task task = taskList.get(index);
        task.setDone(done);
        String message = done ? "Nice! I've marked this task as done:\n\t"
                : "OK, I've marked this task as not done yet:\n\t";
        System.out.println(message + task + "\n");
    }

    /** Adds a todo only when it has a non-blank description. */
    public void addTodo(String description) throws DawnException {
        ensureCapacity();
        if (description.isEmpty()) {
            throw new DawnException("A todo needs a description. Use: " + TODO_USAGE);
        }
        addTask(new ToDo(description));
    }

    /** Validates a deadline description and due-date marker before adding the task. */
    public void addDeadline(String details) throws DawnException {
        ensureCapacity();
        int byIndex = findStandaloneMarker(details, DEADLINE_MARKER);
        if (byIndex < 0) {
            throw new DawnException("A deadline needs the /by keyword. Use: " + DEADLINE_USAGE);
        }
        String description = details.substring(0, byIndex).trim();
        String dueDate = details.substring(byIndex + DEADLINE_MARKER.length()).trim();
        if (description.isEmpty()) {
            throw new DawnException("The deadline description cannot be blank. Use: " + DEADLINE_USAGE);
        }
        if (dueDate.isEmpty()) {
            throw new DawnException("The due date cannot be blank. Use: " + DEADLINE_USAGE);
        }
        addTask(new Deadline(description, dueDate));
    }

    /** Validates an event description, start, and end before adding the task. */
    public void addEvent(String details) throws DawnException {
        ensureCapacity();
        int fromIndex = findStandaloneMarker(details, EVENT_START_MARKER);
        int toIndex = findStandaloneMarker(details, EVENT_END_MARKER);
        if (fromIndex < 0) {
            throw new DawnException("An event needs the /from keyword. Use: " + EVENT_USAGE);
        }
        if (toIndex < 0) {
            throw new DawnException("An event needs the /to keyword. Use: " + EVENT_USAGE);
        }
        if (toIndex < fromIndex) {
            throw new DawnException("The /to keyword must come after /from. Use: " + EVENT_USAGE);
        }

        String description = details.substring(0, fromIndex).trim();
        String start = details.substring(fromIndex + EVENT_START_MARKER.length(), toIndex).trim();
        String end = details.substring(toIndex + EVENT_END_MARKER.length()).trim();
        if (description.isEmpty()) {
            throw new DawnException("The event description cannot be blank. Use: " + EVENT_USAGE);
        }
        if (start.isEmpty()) {
            throw new DawnException("The event start cannot be blank. Use: " + EVENT_USAGE);
        }
        if (end.isEmpty()) {
            throw new DawnException("The event end cannot be blank. Use: " + EVENT_USAGE);
        }
        addTask(new Event(description, start, end));
    }

    /** Finds a marker only when it is bounded by whitespace or the input edge. */
    private int findStandaloneMarker(String text, String marker) {
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

    /** Rejects arguments supplied to commands whose grammar has no arguments. */
    private void requireNoArguments(String command, String arguments) throws DawnException {
        if (!arguments.isEmpty()) {
            throw new DawnException("The " + command + " command does not accept arguments. Use: " + command);
        }
    }

    /** Rejects a new task after the list reaches its fixed capacity. */
    private void ensureCapacity() throws DawnException {
        if (taskList.size() >= MAX_TASKS) {
            throw new DawnException("Dawn can store at most " + MAX_TASKS + " tasks.");
        }
    }

    /** Adds an already validated task and reports the addition. */
    private void addTask(Task task) {
        taskList.add(task);
        System.out.println("added: " + task.getDescription() + "\n");
    }

    /** Returns a targeted usage suggestion for a malformed known command word. */
    private String unknownCommandMessage(String commandWord) {
        String normalizedCommand = commandWord.toLowerCase(Locale.ROOT);
        if (normalizedCommand.contains(COMMAND_TODO)) {
            return "Command not recognised. Did you mean: " + TODO_USAGE + "?";
        }
        if (normalizedCommand.contains(COMMAND_DEADLINE)) {
            return "Command not recognised. Did you mean: " + DEADLINE_USAGE + "?";
        }
        if (normalizedCommand.contains(COMMAND_EVENT)) {
            return "Command not recognised. Did you mean: " + EVENT_USAGE + "?";
        }
        if (normalizedCommand.contains(COMMAND_UNMARK)) {
            return "Command not recognised. Did you mean: " + UNMARK_USAGE + "?";
        }
        if (normalizedCommand.contains(COMMAND_MARK)) {
            return "Command not recognised. Did you mean: " + MARK_USAGE + "?";
        }
        return "Command not recognised. Supported commands: todo, deadline, event, list, mark, unmark, bye.";
    }
}
