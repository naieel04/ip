import java.util.ArrayList;
import java.util.List;

public class CommandHandler {
    private static final String COMMAND_BYE = "bye";
    private static final String COMMAND_LIST = "list";
    private static final String COMMAND_MARK = "mark";
    private static final String COMMAND_UNMARK = "unmark";
    private static final String COMMAND_TODO = "todo";
    private static final String COMMAND_DEADLINE = "deadline";
    private static final String COMMAND_EVENT = "event";
    private static final String COMMAND_ADD = "add";

    private static final String DEADLINE_SEPARATOR = " /by ";
    private static final String EVENT_START_SEPARATOR = " /from ";
    private static final String EVENT_END_SEPARATOR = " /to ";

    private static final int MAX_TASKS = 100;
    private final List<Task> taskList;

    public CommandHandler() {
        this.taskList = new ArrayList<>();
    }

    public List<Task> getTaskList() {
        return taskList;
    }

    /**
     * Parses and executes the given user command.
     *
     * @param input the raw user command line
     * @return true if the command is "bye" requesting exit; false otherwise
     */
    public boolean handleCommand(String input) throws DawnException {
        String trimmed = input.trim();
        if (trimmed.equals(COMMAND_BYE)) {
            return true;
        }

        if (trimmed.equals(COMMAND_LIST)) {
            printList();
            return false;
        }

        if (trimmed.startsWith(COMMAND_UNMARK)) {
            updateTask(trimmed, false);
            return false;
        }

        if (trimmed.startsWith(COMMAND_MARK)) {
            updateTask(trimmed, true);
            return false;
        }

        String commandToProcess = trimmed;
        if (commandToProcess.startsWith(COMMAND_ADD + " ")) {
            commandToProcess = commandToProcess.substring(COMMAND_ADD.length() + 1).trim();
        }

        if (commandToProcess.startsWith(COMMAND_TODO)) {
            String args = commandToProcess.substring(COMMAND_TODO.length());
            addTodo(args);
            return false;
        }

        if (commandToProcess.startsWith(COMMAND_DEADLINE)) {
            String args = commandToProcess.substring(COMMAND_DEADLINE.length());
            addDeadline(args);
            return false;
        }

        if (commandToProcess.startsWith(COMMAND_EVENT)) {
            String args = commandToProcess.substring(COMMAND_EVENT.length());
            addEvent(args);
            return false;
        }

        System.out.println("Invalid command\n");
        return false;
    }

    public void printList() {
        System.out.println("Here are the tasks in your list:");
        for (int i = 0; i < taskList.size(); i++) {
            System.out.printf("%d.%s\n", i + 1, taskList.get(i));
        }
    }

    public void updateTask(String input, boolean done) {
        String digits = input.replaceAll("\\D", "");
        if (digits.isEmpty()) {
            System.out.println("Task not found\n");
            return;
        }
        int index = Integer.parseInt(digits) - 1;
        if (index < 0 || index >= taskList.size()) {
            System.out.println("Task not found\n");
            return;
        }
        Task task = taskList.get(index);
        task.setDone(done);
        String message = done ? "Nice! I've marked this task as done:\n\t"
                : "OK, I've marked this task as not done yet:\n\t";
        System.out.println(message + task + "\n");
    }

    public void addTodo(String args) {
        if (taskList.size() >= MAX_TASKS) {
            System.out.println("Error, 100 tasks present\n");
            return;
        }
        String description = args.trim();
        if (description.isEmpty()) {
            System.out.println("Invalid task: description cannot be blank\n");
            return;
        }
        Task task = new ToDo(description);
        taskList.add(task);
        System.out.println("added: " + task.getDescription() + "\n");
    }

    public void addDeadline(String args) {
        if (taskList.size() >= MAX_TASKS) {
            System.out.println("Error, 100 tasks present\n");
            return;
        }
        if (!args.contains(DEADLINE_SEPARATOR)) {
            System.out.println("Invalid task: description cannot be blank\n");
            return;
        }
        String[] parts = args.split(DEADLINE_SEPARATOR, 2);
        String description = parts[0].trim();
        String dueDate = parts[1].trim();
        if (description.isEmpty() || dueDate.isEmpty()) {
            System.out.println("Invalid task: description cannot be blank\n");
            return;
        }
        Task task = new Deadline(description, dueDate);
        taskList.add(task);
        System.out.println("added: " + task.getDescription() + "\n");
    }

    public void addEvent(String args) {
        if (taskList.size() >= MAX_TASKS) {
            System.out.println("Error, 100 tasks present\n");
            return;
        }
        if (!args.contains(EVENT_START_SEPARATOR) || !args.contains(EVENT_END_SEPARATOR)) {
            System.out.println("Invalid task: description cannot be blank\n");
            return;
        }
        String[] fromParts = args.split(EVENT_START_SEPARATOR, 2);
        String description = fromParts[0].trim();
        String[] toParts = fromParts[1].split(EVENT_END_SEPARATOR, 2);
        String startDate = toParts[0].trim();
        String endDate = toParts[1].trim();
        if (description.isEmpty() || startDate.isEmpty() || endDate.isEmpty()) {
            System.out.println("Invalid task: description cannot be blank\n");
            return;
        }
        Task task = new Event(description, startDate, endDate);
        taskList.add(task);
        System.out.println("added: " + task.getDescription() + "\n");
    }
}
