package dawn.command;

import dawn.exception.DawnException;
import dawn.parser.Parser;
import dawn.task.Deadline;
import dawn.task.Event;
import dawn.task.Task;
import dawn.task.TaskList;
import dawn.task.ToDo;

/** Manages task-list updates and logic. */
public class CommandHandler {
    private static final String COMMAND_BYE = "bye";
    private static final String COMMAND_LIST = "list";
    private static final String COMMAND_MARK = "mark";
    private static final String COMMAND_UNMARK = "unmark";
    private static final String COMMAND_TODO = "todo";
    private static final String COMMAND_DEADLINE = "deadline";
    private static final String COMMAND_EVENT = "event";

    private final TaskList taskList;

    public CommandHandler() {
        this.taskList = new TaskList();
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
            sb.append(String.format("%d.%s\n", i + 1, taskList.getTask(i)));
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

        Task task = taskList.getTask(index);
        task.setDone(done);
        String message = done ? "Nice! I've marked this task as done:\n\t"
                : "OK, I've marked this task as not done yet:\n\t";
        return message + task + "\n\n";
    }

    /** Adds a todo only when it has a non-blank description, returning feedback. */
    public String addTodo(String arguments) throws DawnException {
        String description = Parser.parseTodoArgs(arguments);
        return addTaskMessage(new ToDo(description));
    }

    /** Validates a deadline description and marker before adding the task, returning feedback. */
    public String addDeadline(String arguments) throws DawnException {
        String[] parsed = Parser.parseDeadlineArgs(arguments);
        return addTaskMessage(new Deadline(parsed[0], parsed[1]));
    }

    /** Validates an event description, start, and end before adding the task, returning feedback. */
    public String addEvent(String arguments) throws DawnException {
        String[] parsed = Parser.parseEventArgs(arguments);
        return addTaskMessage(new Event(parsed[0], parsed[1], parsed[2]));
    }

    /** Adds an already validated task and returns the addition feedback. */
    private String addTaskMessage(Task task) throws DawnException {
        taskList.addTask(task);
        return "added: " + task.getDescription() + "\n\n";
    }
}
