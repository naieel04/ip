import java.util.ArrayList;
import java.util.List;

/** Manages task-list updates and logic. */
public class CommandHandler {
    private static final String COMMAND_BYE = "bye";
    private static final String COMMAND_LIST = "list";
    private static final String COMMAND_MARK = "mark";
    private static final String COMMAND_UNMARK = "unmark";
    private static final String COMMAND_TODO = "todo";
    private static final String COMMAND_DEADLINE = "deadline";
    private static final String COMMAND_EVENT = "event";

    private static final int MAX_TASKS = 100;
    private final List<Task> taskList;

    public CommandHandler() {
        this.taskList = new ArrayList<>();
    }

    public List<Task> getTaskList() {
        return taskList;
    }

    /**
     * Executes the given raw command string.
     *
     * @param input the raw command line
     * @return the result message to display, or {@code null} if the exit command was issued
     * @throws DawnException if the command or any required argument is invalid
     */
    public String handleCommand(String input) throws DawnException {
        String[] parsed = Parser.parseCommand(input);
        String command = parsed[0];
        String arguments = parsed[1];

        switch (command) {
        case COMMAND_BYE:
            Parser.requireNoArguments(command, arguments);
            return null; // Return null to signal termination
        case COMMAND_LIST:
            Parser.requireNoArguments(command, arguments);
            return getListString();
        case COMMAND_MARK:
            return updateTask(arguments, true);
        case COMMAND_UNMARK:
            return updateTask(arguments, false);
        case COMMAND_TODO:
            return addTodo(arguments);
        case COMMAND_DEADLINE:
            return addDeadline(arguments);
        case COMMAND_EVENT:
            return addEvent(arguments);
        default:
            throw new DawnException(Parser.unknownCommandMessage(command));
        }
    }

    /** Returns the current task list in insertion order. */
    public String getListString() {
        StringBuilder sb = new StringBuilder("Here are the tasks in your list:\n");
        for (int i = 0; i < taskList.size(); i++) {
            sb.append(String.format("%d.%s\n", i + 1, taskList.get(i)));
        }
        return sb.toString();
    }

    /** Validates a task number before marking or unmarking that task, returning feedback. */
    public String updateTask(String arguments, boolean done) throws DawnException {
        int index = Parser.parseTaskNumber(arguments, done);
        if (index < 0 || index >= taskList.size()) {
            String command = done ? COMMAND_MARK : COMMAND_UNMARK;
            throw new DawnException("Task number not found. Use: " + command + " [task number]");
        }

        Task task = taskList.get(index);
        task.setDone(done);
        String message = done ? "Nice! I've marked this task as done:\n\t"
                : "OK, I've marked this task as not done yet:\n\t";
        return message + task + "\n\n";
    }

    /** Adds a todo only when it has a non-blank description, returning feedback. */
    public String addTodo(String arguments) throws DawnException {
        ensureCapacity();
        String description = Parser.parseTodoArgs(arguments);
        return addTask(new ToDo(description));
    }

    /** Validates a deadline description and marker before adding the task, returning feedback. */
    public String addDeadline(String arguments) throws DawnException {
        ensureCapacity();
        String[] parsed = Parser.parseDeadlineArgs(arguments);
        return addTask(new Deadline(parsed[0], parsed[1]));
    }

    /** Validates an event description, start, and end before adding the task, returning feedback. */
    public String addEvent(String arguments) throws DawnException {
        ensureCapacity();
        String[] parsed = Parser.parseEventArgs(arguments);
        return addTask(new Event(parsed[0], parsed[1], parsed[2]));
    }

    /** Rejects a new task after the list reaches its fixed capacity. */
    private void ensureCapacity() throws DawnException {
        if (taskList.size() >= MAX_TASKS) {
            throw new DawnException("Dawn can store at most " + MAX_TASKS + " tasks.");
        }
    }

    /** Adds an already validated task and returns the addition feedback. */
    private String addTask(Task task) {
        taskList.add(task);
        return "added: " + task.getDescription() + "\n\n";
    }
}